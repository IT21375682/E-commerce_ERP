/*
 * File: OrderService.cs
 * Author: Sanjayan
 * Description: This service handles the business logic for order operations such as fetching, creating, updating, and deleting orders.
 */

using E_commerce.Models;
using E_commerce.Repositories;
using System.Collections.Generic;

namespace E_commerce.Services
{
    public class OrderService
    {
        private readonly IOrderRepository _orderRepository;

        public OrderService(IOrderRepository orderRepository)
        {
            _orderRepository = orderRepository;
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
        public void UpdateOrderStatus(string orderId, string statusType)
        {
            _orderRepository.UpdateOrderStatus(orderId, statusType);
        }
    }
}
