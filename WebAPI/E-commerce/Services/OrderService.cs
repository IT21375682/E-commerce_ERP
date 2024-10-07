/*
 * File: OrderService.cs
 * Author: Krithiga D. B
 * Description: This service handles the business logic for order operations such as fetching, creating, updating, and deleting orders.
 */

using E_commerce.DTOs;
using E_commerce.Models;
using E_commerce.Repositories;
using MongoDB.Bson;
using MongoDB.Driver;
using System.Collections.Generic;

namespace E_commerce.Services
{
    public class OrderService
    {
        private readonly IMongoCollection<Order> _orders;
        private readonly IOrderRepository _orderRepository;
        private readonly IUserRepository _userRepository;
        private readonly IProductRepository _productRepository;
        private readonly EmailService _emailService;  // Inject email service for sending emails

        public OrderService(IUserRepository userRepository, IOrderRepository orderRepository, EmailService emailService, IProductRepository productRepository, IMongoCollection<Order> orders)
        {
            _orderRepository = orderRepository;
            _userRepository = userRepository;
            _productRepository = productRepository;
            _emailService = emailService;
            _orders = orders; // Initialize the orders collection

        }

        // Retrieves all orders from the repository
        public IEnumerable<Order> GetAllOrders()
        {
            return _orderRepository.GetAllOrders();
        }

        // Retrieves an order by its ID
        public Order GetOrderById(string id)
        {
            return _orderRepository.GetOrderById(id);
        }

        // Service method to get orders for a specific user
        public List<Order> GetOrdersByUserId(string userId)
        {
            return _orderRepository.GetOrdersByUserId(userId);
        }


        // Creates a new order
        public void CreateOrder(Order order)
        {
            _orderRepository.CreateOrder(order);
        }

        // Updates an existing order
        public void UpdateOrder(string id, Order order)
        {
            _orderRepository.UpdateOrder(id, order);
        }

        // Deletes an order by its ID
        public void DeleteOrder(string id)
        {
            _orderRepository.DeleteOrder(id);
        }

        // Updates the processed status of products in an order
        public void UpdateProductProcessedStatus(string orderId, string productId, bool processed)
        {
            _orderRepository.UpdateProductProcessedStatus(orderId, productId, processed);
        }

        // Updates the order status and timestamp
        // Updates the order status and sends email only when canceled
        public async Task UpdateOrderStatus(string orderId, string statusType, string customMessage = null)
        {
            if (statusType.ToLower() == "canceled")
            {
                // Call repository to update status to 'canceled' and pass the custom message
                await _orderRepository.UpdateOrderStatus(orderId, statusType, customMessage);
            }
            else
            {
                // For other statuses, just update the order status without sending an email
                await _orderRepository.UpdateOrderStatus(orderId, statusType, null);
            }
        }


        // Updates the order status as Cancel with message
        public async Task CancelOrder(string orderId, string customMessage = null)
        {
            customMessage = customMessage ?? "Dear customer, your order has been canceled due to stock unavailability.";

            // Call repository to update order status to 'canceled' and pass the custom message
            await _orderRepository.UpdateOrderStatus(orderId, "canceled", customMessage);

            // Send the cancellation email to the user
            var order = _orderRepository.GetOrderById(orderId); // Assuming you have made this async
            if (order != null)
            {
                // Convert the UserId to ObjectId
                var userId = new ObjectId(order.UserId); // Assuming order.UserId is a string representation of ObjectId

                // Fetch the user by ObjectId asynchronously
                var user = _userRepository.GetUserById(userId.ToString()); // Use the ObjectId as string
                if (user != null)
                {
                    string subject = $"{orderId} Cancelled";
                    await _emailService.SendEmailAsync(user.Email, subject, customMessage); // Await the SendEmailAsync
                }
            }
        }

        //public OrderStatus GetOrderStatusById(string orderId)
        //{
        //    // Call repository method to get order status
        //    return _orderRepository.GetOrderStatusById(orderId);
        //}

        // Retrieves the latest order status by date
        public string GetLatestOrderStatusById(string orderId)
        {
            var objectId = new ObjectId(orderId);

            // Get the order status using the repository
            var orderStatus = _orderRepository.GetOrderStatusById(orderId);

            // Return the last updated status
            return GetLatestStatus(orderStatus);
        }

        // helper method to retrieve the latest order status by date
        private string GetLatestStatus(OrderStatus status)
        {
            // Create a dictionary to store statuses and their corresponding dates
            var statusUpdates = new Dictionary<string, DateTime?>()
    {
        { "Pending", status.PendingDate },
        { "Processing", status.ProcessingDate },
        { "Dispatched", status.DispatchedDate },
        { "Partially Delivered", status.Partially_Delivered_Date },
        { "Delivered", status.DeliveredDate },
        { "Canceled", status.CanceledDate }
    };

            // Filter out statuses with null dates and get the latest status by date
            var latestStatus = statusUpdates
                .Where(s => s.Value != null) // Only consider statuses with non-null dates
                .OrderByDescending(s => s.Value) // Order by date descending
                .FirstOrDefault();

            return latestStatus.Key ?? "Unknown"; // Return the latest status or "Unknown" if none found
        }


        // Method to get products in an order filtered by VendorId
        public List<ProductItem> GetProductsByVendorId(string orderId, string vendorId)
        {
            var order = _orderRepository.GetOrderById(orderId);
            if (order == null)
            {
                throw new Exception($"Order with ID: {orderId} not found.");
            }

            // Filter product items by VendorId
            var filteredProducts = order.ProductItems
                .Where(item => {
                    var product = _productRepository.GetProductById(item.ProductId.ToString());
                    return product != null && product.VendorId == vendorId;
                })
                .ToList();

            return filteredProducts;
        }

        // method to update the order status as delivered
        public void UpdateProductDeliveryStatus(string orderId, string vendorId, ObjectId productId)
        {
            _orderRepository.UpdateProductDeliveryStatus(orderId, vendorId, productId);
        }

        // get method the single order with details
        public SingleOrderItemDto GetSingleOrderWithProductDetails(string orderId)
        {
            // Fetch the order by orderId
            var order = _orderRepository.GetOrderById(orderId);
            if (order == null)
            {
                return null; // Handle order not found case
            }

            // Initialize the DTO
            SingleOrderItemDto orderDto = new SingleOrderItemDto
            {
                Id = order.Id,
                UserId = order.UserId,
                Date = order.Date,
                Total = order.Total,
                Status = new SingleOrderStatus
                {
                    Pending = order.Status.Pending,
                    PendingDate = order.Status.PendingDate,
                    Processing = order.Status.Processing,
                    ProcessingDate = order.Status.ProcessingDate,
                    Dispatched = order.Status.Dispatched,
                    DispatchedDate = order.Status.DispatchedDate,
                    Partially_Delivered = order.Status.Partially_Delivered,
                    Partially_Delivered_Date = order.Status.Partially_Delivered_Date,
                    Delivered = order.Status.Delivered,
                    DeliveredDate = order.Status.DeliveredDate,
                    Canceled = order.Status.Canceled,
                    CanceledDate = order.Status.CanceledDate,
                    StockReduced = order.Status.StockReduced
                },
                CancellationNote = order.CancellationNote,
                ProductItems = new List<SingleOrderProductItem>()
            };

            // For each product in the order, fetch additional details like name, price, and image
            foreach (var productItem in order.ProductItems)
            {
                var product = _productRepository.GetProductById(productItem.ProductId);
                if (product != null)
                {
                    orderDto.ProductItems.Add(new SingleOrderProductItem
                    {
                        ProductId = productItem.ProductId,
                        ProductName = product.Name,
                        ProductPrice = product.Price.ToString(),
                        ProductImg = product.ProductImage,
                        Count = productItem.Count,
                        Delivered = productItem.Delivered
                    });
                }
            }

            return orderDto;
        }


    }
}