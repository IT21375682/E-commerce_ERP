/*
 * File: IOrderRepository.cs
 * Author: Sanjayan
 * Description: This repository interfaces with the database to perform CRUD operations on orders.
 */

using E_commerce.Models;
using E_commerce.Services;
using MongoDB.Bson;
using MongoDB.Driver;
using System.Collections.Generic;

namespace E_commerce.Repositories
{
    public class IOrderRepository
    {
        private readonly IMongoCollection<Order> _orders;
        private readonly IMongoCollection<Product> _products;
        private readonly IProductRepository _productRepository;
        private readonly IUserRepository _userRepository;  // Injecting UserRepository
        private readonly EmailService _emailService;

        public IOrderRepository(IMongoDatabase database, IProductRepository productRepository,IUserRepository userRepository, EmailService emailService)
        {
            _orders = database.GetCollection<Order>("Orders");
            _products = database.GetCollection<Product>("Products");
            _userRepository = userRepository;   // Assign UserRepository
            _productRepository = productRepository; // Injecting product repository
            _emailService = emailService; // Assign EmailService

        }

        // Fetches all orders from the database
        public IEnumerable<Order> GetAllOrders()
        {
            return _orders.Find(order => true).ToList();
        }

        // Fetches an order by its ID
        public Order GetOrderById(string id)
        {
            var objectId = new ObjectId(id);
            return _orders.Find(order => order.Id == objectId.ToString()).FirstOrDefault();
        }

        // Retrieves all orders for a specific user by userId
        public List<Order> GetOrdersByUserId(string userId)
        {
            // Filter orders based on userId
            var filter = Builders<Order>.Filter.Eq(o => o.UserId, userId);

            // Get all matching orders
            return _orders.Find(filter).ToList();
        }


        //// Inserts a new order into the database
        //public void CreateOrder(Order order)
        //{
        //    foreach (var item in order.ProductItems)
        //    {
        //        item.Processed = false;
        //    }
        //    order.Status = new OrderStatus();
        //    _orders.InsertOne(order);
        //}

        // Inserts a new order into the database
        public void CreateOrder(Order order)
        {

            order.Id = ObjectId.GenerateNewId().ToString();

            order.Date = order.Date == default(DateTime) ? DateTime.Now : order.Date;
            order.Status.Pending = true;
            order.Status.PendingDate = DateTime.Now;
            // Check stock availability before creating the order
            foreach (var item in order.ProductItems)
            {
                var product = _productRepository.GetProductById(item.ProductId.ToString());
                if (product == null || product.AvailableStock < item.Count)
                {
                    throw new Exception($"Not enough stock for product ID: {item.ProductId}");
                }
                item.Delivered = false;
                // Reduce the stock for the product
                //_productRepository.RemoveStock(item.ProductId.ToString(), item.Count);
            }

            //order.Status = new OrderStatus();
            _orders.InsertOne(order);
        }

        // Updates an existing order
        //public void UpdateOrder(string id, Order updatedOrder)
        //{
        //    var objectId = new ObjectId(id);

        //    // Fetch the existing order to retain the _id
        //    var existingOrder = _orders.Find(order => order.Id == objectId).FirstOrDefault();

        //    if (existingOrder != null)
        //    {
        //        // Keep the original _id
        //        updatedOrder.Id = existingOrder.Id;

        //        // Now replace the document with the updated fields while keeping the _id
        //        _orders.ReplaceOne(order => order.Id == objectId, updatedOrder);
        //    }
        //}
        public void UpdateOrder(string id, Order updatedOrder)
        {
            var objectId = new ObjectId(id);
            var existingOrder = _orders.Find(order => order.Id == objectId.ToString()).FirstOrDefault();

            if (existingOrder != null)
            {
                // Check stock availability for the new order items
                foreach (var item in updatedOrder.ProductItems)
                {
                    var product = _productRepository.GetProductById(item.ProductId.ToString());
                    if (product == null || product.AvailableStock < item.Count)
                    {
                        throw new Exception($"Not enough stock for product ID: {item.ProductId}");
                    }
                }

                // Restore stock for the old order items
                foreach (var item in existingOrder.ProductItems)
                {
                    _productRepository.UpdateStock(item.ProductId.ToString(), item.Count); // Restore old stock
                }

                // Keep the original _id and update with new order details
                updatedOrder.Id = existingOrder.Id;
                _orders.ReplaceOne(order => order.Id == objectId.ToString(), updatedOrder);

                // Deduct new stock for the updated order items
                foreach (var item in updatedOrder.ProductItems)
                {
                    _productRepository.RemoveStock(item.ProductId.ToString(), item.Count);
                }
            }
        }


        // Deletes an order from the database
        public void DeleteOrder(string id)
        {
            var objectId = new ObjectId(id);
            _orders.DeleteOne(order => order.Id == objectId.ToString());
        }

        // Updates the processed status of a product in an order
        public void UpdateProductProcessedStatus(string orderId, string productId, bool processed)
        {
            var objectId = new ObjectId(orderId);

            // Filter for the order by Id and the specific product by productId
            var filter = Builders<Order>.Filter.And(
                Builders<Order>.Filter.Eq(o => o.Id, objectId.ToString()),
                Builders<Order>.Filter.ElemMatch(o => o.ProductItems, p => p.ProductId.ToString() == productId.ToString())
            );

            // Use the positional operator `$` to update the `Processed` field of the matched product
            var update = Builders<Order>.Update.Set("ProductItems.$.Processed", processed);

            // Perform the update
            _orders.UpdateOne(filter, update);
        }
        private string CreateCancellationMessage(User user, Order order, string customMessage)
        {
            return $@"
    <p>Dear {user.Name},</p>
    <p>We regret to inform you that your order with ID {order.Id} has been canceled.</p>
    <p><strong>Cancellation Reason:</strong> {customMessage}</p>
    <p>If you have any questions or concerns, feel free to reach out to our support team.</p>
    <p>We apologize for the inconvenience and hope to serve you better in the future.</p>
    <p>Best regards,<br>E-commerce Team</p>";
        }

        private string CreateDeliveryMessage(User user, Order order)
        {
            return $@"
        <p>Dear {user.Name},</p>
        <p>We are pleased to inform you that your order with ID {order.Id} has been successfully delivered.</p>
        <p><strong>Order Details:</strong></p>
        {GenerateOrderDetails(order)}
        <p>Thank you for shopping with us!</p>
        <p>Best regards,<br>E-commerce Team</p>";
        }

        // Helper to generate a detailed order summary in HTML format
        private string GenerateOrderDetails(Order order)
        {
            string details = "<ul>";
            foreach (var item in order.ProductItems)
            {
                details += $"<li>Product ID: {item.ProductId}, Quantity: {item.Count}</li>";
            }
            details += "</ul>";
            return details;
        }




        // Updates the order status and timestamp
        //public async Task UpdateOrderStatus(string orderId, string statusType, string customMessage = null)
        //{
        //    var objectId = new ObjectId(orderId);
        //    var filter = Builders<Order>.Filter.Eq(o => o.Id, objectId.ToString());
        //    var update = Builders<Order>.Update;

        //    var updates = new List<UpdateDefinition<Order>>();

        //    switch (statusType.ToLower())
        //    {
        //        case "pending":
        //            updates.Add(update.Set(o => o.Status.Pending, true));
        //            updates.Add(update.Set(o => o.Status.PendingDate, DateTime.Now));
        //            break;
        //        case "processing":
        //            updates.Add(update.Set(o => o.Status.Processing, true));
        //            updates.Add(update.Set(o => o.Status.ProcessingDate, DateTime.Now));
        //            break;
        //        case "dispatched":
        //            updates.Add(update.Set(o => o.Status.Dispatched, true));
        //            updates.Add(update.Set(o => o.Status.DispatchedDate, DateTime.Now));
        //            break;
        //        case "delivered":
        //            //updates.Add(update.Set(o => o.Status.Delivered, true));
        //            //updates.Add(update.Set(o => o.Status.DeliveredDate, DateTime.Now));
        //            //break;
        //            updates.Add(update.Set(o => o.Status.Delivered, true));
        //            updates.Add(update.Set(o => o.Status.DeliveredDate, DateTime.Now));

        //            // Send delivery confirmation email with order details
        //            var deliveredOrder = await _orders.Find(o => o.Id == objectId.ToString()).FirstOrDefaultAsync();
        //            if (deliveredOrder != null)
        //            {
        //                var user = _userRepository.GetUserById(deliveredOrder.UserId); // Using synchronous method
        //                if (user != null)
        //                {
        //                    string subject = $"Your Order {deliveredOrder.Id} Has Been Delivered";
        //                    string message = customMessage ?? CreateDeliveryMessage(user, deliveredOrder);

        //                    await _emailService.SendEmailAsync(user.Email, subject, message);
        //                }
        //            }
        //            break;
        //        case "canceled":
        //            updates.Add(update.Set(o => o.Status.Canceled, true));
        //            updates.Add(update.Set(o => o.Status.CanceledDate, DateTime.Now));

        //            // Send cancellation email with custom message
        //            var order = await _orders.Find(o => o.Id == objectId.ToString()).FirstOrDefaultAsync();
        //            if (order != null)
        //            {
        //                var user = _userRepository.GetUserById(order.UserId); // Using synchronous method
        //                if (user != null)
        //                {
        //                    string subject = $"Order {order.Id} Canceled";
        //                    string message = customMessage ?? CreateCancellationMessage(user, order);

        //                    await _emailService.SendEmailAsync(user.Email, subject, message);
        //                }
        //            }
        //            break;
        //    }

        //    // Combine all the updates into a single update definition
        //    var combinedUpdate = update.Combine(updates);

        //    _orders.UpdateOne(filter, combinedUpdate);
        //}

        //public async Task UpdateOrderStatus(string orderId, string statusType, string customMessage = null)
        //{
        //    var objectId = new ObjectId(orderId);
        //    var filter = Builders<Order>.Filter.Eq(o => o.Id, objectId.ToString());
        //    var update = Builders<Order>.Update;

        //    var updates = new List<UpdateDefinition<Order>>();

        //    // Get the current order details to check the statuses
        //    var order = await _orders.Find(o => o.Id == objectId.ToString()).FirstOrDefaultAsync();
        //    if (order == null)
        //    {
        //        throw new Exception($"Order with ID: {orderId} not found.");
        //    }

        //    // Check if any non-pending status is true
        //    bool anyNonPendingStatusTrue = order.Status.Processing == true ||
        //                                   order.Status.Dispatched == true ||
        //                                   order.Status.Partially_Delivered == true ||
        //                                   order.Status.Delivered == true ||
        //                                   order.Status.Canceled == true;


        //    // Stock reduction should occur in two cases:
        //    // 1. If any non-pending status is already true (because we're not concerned about stock being reduced again)
        //    // 2. If transitioning from pending to a non-pending status and stock hasn't been reduced yet
        //    if (anyNonPendingStatusTrue || statusType.ToLower() != "Pending")
        //    {
        //        foreach (var item in order.ProductItems)
        //        {
        //            var (isSuccess, message) = _productRepository.RemoveStock(item.ProductId, item.Count);
        //            if (!isSuccess)
        //            {
        //                throw new Exception($"Failed to reduce stock for product {item.ProductId}: {message}");
        //            }
        //        }

        //    }

        //    // Handle each status change
        //    switch (statusType.ToLower())
        //    {
        //        case "pending":
        //            updates.Add(update.Set(o => o.Status.Pending, true));
        //            updates.Add(update.Set(o => o.Status.PendingDate, DateTime.Now));
        //            break;

        //        case "processing":
        //            if (order.Status.Processing != true) // If processing hasn't been set yet
        //            {
        //                updates.Add(update.Set(o => o.Status.Processing, true));
        //                updates.Add(update.Set(o => o.Status.ProcessingDate, DateTime.Now));
        //            }
        //            break;

        //        case "dispatched":
        //            if (order.Status.Dispatched != true) // If dispatched hasn't been set yet
        //            {
        //                updates.Add(update.Set(o => o.Status.Dispatched, true));
        //                updates.Add(update.Set(o => o.Status.DispatchedDate, DateTime.Now));
        //            }
        //            break;

        //        case "delivered":
        //            if (order.Status.Delivered != true) // If delivered hasn't been set yet
        //            {
        //                updates.Add(update.Set(o => o.Status.Delivered, true));
        //                updates.Add(update.Set(o => o.Status.DeliveredDate, DateTime.Now));

        //                // Send delivery confirmation email
        //                var user = _userRepository.GetUserById(order.UserId);
        //                if (user != null)
        //                {
        //                    string subject = $"Your Order {order.Id} Has Been Delivered";
        //                    string message = customMessage ?? CreateDeliveryMessage(user, order);

        //                    await _emailService.SendEmailAsync(user.Email, subject, message);
        //                }
        //            }
        //            break;

        //        case "canceled":
        //            if (order.Status.Canceled != true) // If canceled hasn't been set yet
        //            {
        //                updates.Add(update.Set(o => o.Status.Canceled, true));
        //                updates.Add(update.Set(o => o.Status.CanceledDate, DateTime.Now));
        //                updates.Add(update.Set(o => o.CancellationNote, customMessage)); // Set cancellation note


        //                // Send cancellation email
        //                var user = _userRepository.GetUserById(order.UserId);
        //                if (user != null)
        //                {
        //                    string subject = $"Order {order.Id} Canceled";
        //                    string message = customMessage ?? CreateCancellationMessage(user, order);

        //                    await _emailService.SendEmailAsync(user.Email, subject, message);
        //                }
        //            }
        //            break;
        //    }

        //    // Combine all updates and apply to the order
        //    var combinedUpdate = update.Combine(updates);
        //    _orders.UpdateOne(filter, combinedUpdate);
        //}
        public async Task UpdateOrderStatus(string orderId, string statusType, string customMessage = null)
        {
            var objectId = new ObjectId(orderId);
            var filter = Builders<Order>.Filter.Eq(o => o.Id, objectId.ToString());
            var update = Builders<Order>.Update;

            var updates = new List<UpdateDefinition<Order>>();

            // Get the current order details to check the statuses
            var order = await _orders.Find(o => o.Id == objectId.ToString()).FirstOrDefaultAsync();
            if (order == null)
            {
                throw new Exception($"Order with ID: {orderId} not found.");
            }
            // Check if the order has been dispatched
            if (order.Status.Dispatched == true && statusType.ToLower() == "canceled")
            {
                throw new Exception("Order is already dispatched; cancellation is not allowed.");
            }

            // Check if any non-pending status is true
            bool anyNonPendingStatusTrue = order.Status.Processing == true ||
                                           order.Status.Dispatched == true ||
                                           order.Status.Partially_Delivered == true ||
                                           order.Status.Delivered == true ||
                                           order.Status.Canceled == true;

            // Stock reduction should only occur if transitioning from Pending to a non-pending status,
            // and stock hasn't been reduced yet
            bool stockReduced = order.Status.StockReduced ?? false;

            if (!anyNonPendingStatusTrue && !stockReduced && statusType.ToLower() != "pending")
            {
                foreach (var item in order.ProductItems)
                {
                    var (isSuccess, message) = _productRepository.RemoveStock(item.ProductId, item.Count);
                    if (!isSuccess)
                    {
                        throw new Exception($"Failed to reduce stock for product {item.ProductId}: {message}");
                    }
                }
                // Mark stock as reduced in the order status
                updates.Add(update.Set(o => o.Status.StockReduced, true));
            }

            // Handle each status change
            switch (statusType.ToLower())
            {
                case "pending":
                    updates.Add(update.Set(o => o.Status.Pending, true));
                    updates.Add(update.Set(o => o.Status.PendingDate, DateTime.Now));
                    break;

                case "processing":
                    if (order.Status.Processing != true)
                    {
                        updates.Add(update.Set(o => o.Status.Processing, true));
                        updates.Add(update.Set(o => o.Status.ProcessingDate, DateTime.Now));
                    }
                    break;

                case "dispatched":
                    if (order.Status.Dispatched != true)
                    {
                        updates.Add(update.Set(o => o.Status.Dispatched, true));
                        updates.Add(update.Set(o => o.Status.DispatchedDate, DateTime.Now));
                    }
                    break;

                case "delivered":
                    if (order.Status.Delivered != true)
                    {
                        updates.Add(update.Set(o => o.Status.Delivered, true));
                        updates.Add(update.Set(o => o.Status.DeliveredDate, DateTime.Now));

                        var user = _userRepository.GetUserById(order.UserId);
                        if (user != null)
                        {
                            string subject = $"Your Order {order.Id} Has Been Delivered";
                            string message = customMessage ?? CreateDeliveryMessage(user, order);
                            await _emailService.SendEmailAsync(user.Email, subject, message);
                        }
                    }
                    break;

                case "canceled":
                    if (order.Status.Canceled != true)
                    {
                        updates.Add(update.Set(o => o.Status.Canceled, true));
                        updates.Add(update.Set(o => o.Status.CanceledDate, DateTime.Now));
                        updates.Add(update.Set(o => o.CancellationNote, customMessage)); // Save customMessage as the note

                        var user = _userRepository.GetUserById(order.UserId);
                        if (user != null)
                        {
                            string subject = $"Order {order.Id} Canceled";
                            string message = CreateCancellationMessage(user, order, customMessage); // Pass the customMessage here
                            await _emailService.SendEmailAsync(user.Email, subject, message);
                        }
                    }
                    break;
            }

            //// Combine all updates and apply to the order
            //var combinedUpdate = update.Combine(updates);
            //_orders.UpdateOne(filter, combinedUpdate);
            if (updates.Count > 0) // Ensure there are updates before calling UpdateOne
            {
                var combinedUpdate = update.Combine(updates);
                await _orders.UpdateOneAsync(filter, combinedUpdate);
            }
            else
            {
                // If no updates, log or handle accordingly
                throw new Exception("No updates to apply to the order.");
            }
        }



        //Get OrderStatus Onlyby OrderId
        public OrderStatus GetOrderStatusById(string orderId)
        {
            var objectId = new ObjectId(orderId);

            // Use projection to select the Status field
            var projection = Builders<Order>.Projection.Expression(o => o.Status);

            // Find the order by its ID and project the Status field
            var orderStatus = _orders.Find(o => o.Id == objectId.ToString())
                                     .Project(projection)
                                     .FirstOrDefault();

            return orderStatus;
        }

        public List<ProductItem> GetProductsByVendorId(string orderId, string vendorId)
        {
            // Retrieve the order by ID
            var objectId = new ObjectId(orderId);
            var order = _orders.Find(o => o.Id == objectId.ToString()).FirstOrDefault();

            if (order == null)
            {
                throw new Exception($"Order with ID: {orderId} not found.");
            }

            // Filter product items by VendorId
            var filteredProducts = order.ProductItems
                .Where(item =>
                {
                    var product = _productRepository.GetProductById(item.ProductId.ToString());
                    return product != null && product.VendorId == vendorId;
                })
                .ToList();

            return filteredProducts;
        }


        //public void UpdateProductDeliveryStatus(string orderId, string vendorId, string productId)
        //{
        //    var objectId = new ObjectId(orderId);
        //    var order = _orders.Find(o => o.Id == objectId).FirstOrDefault();

        //    if (order == null)
        //    {
        //        throw new Exception($"Order with ID: {orderId} not found.");
        //    }

        //    // Check if the product belongs to the specified vendor
        //    var productItem = order.ProductItems.FirstOrDefault(p => p.ProductId == productId);
        //    if (productItem != null)
        //    {
        //        var product = _productRepository.GetProductById(productId);
        //        if (product != null && product.VendorId == vendorId)
        //        {
        //            // Set delivered status to true
        //            productItem.Delivered = true;

        //            // Check if at least one product is delivered
        //            bool anyDelivered = order.ProductItems.Any(p => p.Delivered);

        //            // Set the order status to Partially_Delivered if any product item is delivered
        //            if (anyDelivered)
        //            {
        //                order.Status.Partially_Delivered = true; // Update status
        //                order.Status.Partially_Delivered_Date = DateTime.Now; // Update timestamp
        //            }
        //        }
        //        else
        //        {
        //            throw new Exception($"Product with ID: {productId} does not belong to vendor ID: {vendorId}.");
        //        }
        //    }
        //    else
        //    {
        //        throw new Exception($"ProductItem with ID: {productId} not found in the order.");
        //    }

        //    // Save the updated order back to the database
        //    var updateDefinition = Builders<Order>.Update
        //        .Set(o => o.ProductItems, order.ProductItems)
        //        .Set(o => o.Status, order.Status); // Update the Order status

        //    _orders.UpdateOne(o => o.Id == objectId, updateDefinition); // Perform the update
        //}

        //public void UpdateProductDeliveryStatus(string orderId, string vendorId, ObjectId productId) // Accept ObjectId instead of string
        //{
        //    var objectId = new ObjectId(orderId);
        //    var order = _orders.Find(o => o.Id == objectId.ToString()).FirstOrDefault();

        //    if (order == null)
        //    {
        //        throw new Exception($"Order with ID: {orderId} not found.");
        //    }

        //    // Check if the product belongs to the specified vendor
        //    var productItem = order.ProductItems.FirstOrDefault(p => p.ProductId == productId.ToString()); // No change needed here
        //    if (productItem != null)
        //    {
        //        var product = _productRepository.GetProductById(productId.ToString()); // Make sure this accepts ObjectId
        //        if (product != null && product.VendorId == vendorId)
        //        {
        //            // Set delivered status to true
        //            productItem.Delivered = true;

        //            // Check if all product items are delivered
        //            bool allDelivered = order.ProductItems.All(p => p.Delivered);

        //            if (allDelivered)
        //            {
        //                // Set the order status to Delivered
        //                order.Status.Delivered = true;
        //                order.Status.DeliveredDate = DateTime.Now; // Update timestamp
        //                var user = _userRepository.GetUserById(order.UserId); // Synchronous method to fetch user
        //                if (user != null)
        //                {
        //                    string subject = $"Your Order {order.Id} Has Been Fully Delivered";
        //                    string message = CreateDeliveryMessage(user, order);

        //                    // Send email notification
        //                    _emailService.SendEmailAsync(user.Email, subject, message);
        //                }
        //            }
        //            else
        //            {
        //                // Set the order status to Partially_Delivered if at least one item is delivered
        //                order.Status.Partially_Delivered = true;
        //                order.Status.Partially_Delivered_Date = DateTime.Now; // Update timestamp
        //            }
        //        }
        //        else
        //        {
        //            throw new Exception($"Product with ID: {productId} does not belong to vendor ID: {vendorId}.");
        //        }
        //    }
        //    else
        //    {
        //        throw new Exception($"ProductItem with ID: {productId} not found in the order.");
        //    }

        //    // Save the updated order back to the database
        //    var updateDefinition = Builders<Order>.Update
        //        .Set(o => o.ProductItems, order.ProductItems)
        //        .Set(o => o.Status, order.Status); // Update the Order status

        //    _orders.UpdateOne(o => o.Id == objectId.ToString(), updateDefinition); // Perform the update
        //}


        public void UpdateProductDeliveryStatus(string orderId, string vendorId, ObjectId productId)
        {
            var objectId = new ObjectId(orderId);
            var order = _orders.Find(o => o.Id == objectId.ToString()).FirstOrDefault();
            bool anyNonPendingStatusTrue = order.Status.Processing == true ||
                                          order.Status.Dispatched == true ||
                                          order.Status.Partially_Delivered == true ||
                                          order.Status.Delivered == true ||
                                          order.Status.Canceled == true;

            if (order == null)
            {
                throw new Exception($"Order with ID: {orderId} not found.");
            }

            // Check if the product belongs to the specified vendor
            var productItem = order.ProductItems.FirstOrDefault(p => p.ProductId == productId.ToString());
            if (productItem != null)
            {
                var product = _productRepository.GetProductById(productId.ToString());
                if (product != null && product.VendorId == vendorId)
                {
                    // Set delivered status to true if it's not already delivered
                    if (!productItem.Delivered)
                    {
                        productItem.Delivered = true; // Mark product item as delivered

                        // Check if any status other than 'Pending' is already true
                        if (!(anyNonPendingStatusTrue))
                        {
                            // Reduce stock if no other status is true (i.e., first time reducing stock)
                            product.AvailableStock -= productItem.Count;
                            _productRepository.RemoveStock(productId.ToString(), product.AvailableStock);
                        }
                    }

                    // Check if all product items are delivered
                    bool allDelivered = order.ProductItems.All(p => p.Delivered);

                    if (allDelivered)
                    {
                        // Set the order status to Delivered
                        order.Status.Delivered = true;
                        order.Status.DeliveredDate = DateTime.Now; // Update timestamp

                        var user = _userRepository.GetUserById(order.UserId); // Fetch user details
                        if (user != null)
                        {
                            string subject = $"Your Order {order.Id} Has Been Fully Delivered";
                            string message = CreateDeliveryMessage(user, order);

                            // Send email notification
                            _emailService.SendEmailAsync(user.Email, subject, message);
                        }
                    }
                    else
                    {
                        // Set the order status to Partially_Delivered if at least one item is delivered
                        order.Status.Partially_Delivered = true;
                        order.Status.Partially_Delivered_Date = DateTime.Now; // Update timestamp
                    }
                }
                else
                {
                    throw new Exception($"Product with ID: {productId} does not belong to vendor ID: {vendorId}.");
                }
            }
            else
            {
                throw new Exception($"ProductItem with ID: {productId} not found in the order.");
            }

            // Save the updated order back to the database
            var updateDefinition = Builders<Order>.Update
                .Set(o => o.ProductItems, order.ProductItems)
                .Set(o => o.Status, order.Status); // Update the order status

            _orders.UpdateOne(o => o.Id == objectId.ToString(), updateDefinition); // Perform the update
        }





    }

}
