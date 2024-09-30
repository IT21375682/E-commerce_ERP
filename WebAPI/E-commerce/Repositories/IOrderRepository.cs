/*
 * File: IOrderRepository.cs
 * Author: Sanjayan
 * Description: This repository interfaces with the database to perform CRUD operations on orders.
 */

using E_commerce.Models;
using MongoDB.Bson;
using MongoDB.Driver;
using System.Collections.Generic;

namespace E_commerce.Repositories
{
    public class IOrderRepository
    {
        private readonly IMongoCollection<Order> _orders;

        public IOrderRepository(IMongoDatabase database)
        {
            _orders = database.GetCollection<Order>("Orders");
        }

        // Fetches all orders from the database
        public IEnumerable<Order> GetAllOrders()
        {
            return _orders.Find(order => true).ToList();
        }

        // Fetches an order by its ID
        public Order GetOrderById(string id)
        {
            return _orders.Find(order => order.Id == id).FirstOrDefault();
        }

        // Retrieves all orders for a specific user by userId
        public List<Order> GetOrdersByUserId(string userId)
        {
            // Filter orders based on userId
            var filter = Builders<Order>.Filter.Eq(o => o.UserId, userId);

            // Get all matching orders
            return _orders.Find(filter).ToList();
        }


        // Inserts a new order into the database
        public void CreateOrder(Order order)
        {
            foreach (var item in order.ProductItems)
            {
                item.Delivered = false;
            }
            order.Status = new OrderStatus();
            _orders.InsertOne(order);
        }

        // Updates an existing order
        public void UpdateOrder(string id, Order updatedOrder)
        {
            // Fetch the existing order to retain the _id
            var existingOrder = _orders.Find(order => order.Id == id).FirstOrDefault();

            if (existingOrder != null)
            {
                // Keep the original _id
                updatedOrder.Id = existingOrder.Id;

                // Now replace the document with the updated fields while keeping the _id
                _orders.ReplaceOne(order => order.Id == id, updatedOrder);
            }
        }


        // Deletes an order from the database
        public void DeleteOrder(string id)
        {
            _orders.DeleteOne(order => order.Id == id);
        }

        // Updates the processed status of a product in an order
        public void UpdateProductProcessedStatus(string orderId, string productId, bool processed)
        {
            // Filter for the order by Id and the specific product by productId
            var filter = Builders<Order>.Filter.And(
                Builders<Order>.Filter.Eq(o => o.Id, orderId),
                Builders<Order>.Filter.ElemMatch(o => o.ProductItems, p => p.ProductId == productId)
            );

            // Use the positional operator `$` to update the `Processed` field of the matched product
            var update = Builders<Order>.Update.Set("ProductItems.$.Processed", processed);

            // Perform the update
            _orders.UpdateOne(filter, update);
        }


        // Updates the order status and timestamp
        public void UpdateOrderStatus(string orderId, string statusType)
        {
            var filter = Builders<Order>.Filter.Eq(o => o.Id, orderId);
            var update = Builders<Order>.Update;

            var updates = new List<UpdateDefinition<Order>>();

            switch (statusType.ToLower())
            {
                case "pending":
                    updates.Add(update.Set(o => o.Status.Pending, true));
                    updates.Add(update.Set(o => o.Status.PendingDate, DateTime.Now));
                    break;
                case "processing":
                    updates.Add(update.Set(o => o.Status.Processing, true));
                    updates.Add(update.Set(o => o.Status.ProcessingDate, DateTime.Now));
                    break;
                case "dispatched":
                    updates.Add(update.Set(o => o.Status.Dispatched, true));
                    updates.Add(update.Set(o => o.Status.DispatchedDate,    DateTime.Now));
                    break;
                case "partially_delivered":
                    updates.Add(update.Set(o => o.Status.Dispatched, true));
                    updates.Add(update.Set(o => o.Status.DispatchedDate, DateTime.Now));
                    break;
                case "delivered":
                    updates.Add(update.Set(o => o.Status.Delivered, true));
                    updates.Add(update.Set(o => o.Status.DeliveredDate, DateTime.Now));
                    break;
                case "canceled":
                    updates.Add(update.Set(o => o.Status.Canceled, true));
                    updates.Add(update.Set(o => o.Status.CanceledDate, DateTime.Now));
                    break;
            }

            // Combine all the updates into a single update definition
            var combinedUpdate = update.Combine(updates);

            _orders.UpdateOne(filter, combinedUpdate);
        }
    }

}
