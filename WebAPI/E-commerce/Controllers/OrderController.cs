/*
 * File: OrderController.cs
 * Author: Sanjayan
 * Description: This controller handles API requests related to orders. 
 * It provides endpoints to fetch, create, update, and delete orders.
 */

using E_commerce.Models;
using E_commerce.Services;
using Microsoft.AspNetCore.Mvc;
using System.Collections.Generic;

namespace E_commerce.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class OrderController : ControllerBase
    {
        private readonly OrderService _orderService;

        public OrderController(OrderService orderService)
        {
            _orderService = orderService;
        }

        // Fetches all orders: GET api/Order
        [HttpGet]
        public ActionResult<IEnumerable<Order>> GetAllOrders()
        {
            var orders = _orderService.GetAllOrders();
            return Ok(orders);
        }

        // Fetches a specific order by its ID: GET api/Order/{id}
        [HttpGet("{id}")]
        public ActionResult<Order> GetOrderById(string id)
        {
            var order = _orderService.GetOrderById(id);
            if (order == null)
            {
                return NotFound();
            }
            return Ok(order);
        }

        // Get all orders by userId GET api/Order/user/{userId}
        [HttpGet("user/{userId}")]
        public IActionResult GetOrdersByUserId(string userId)
        {
            var orders = _orderService.GetOrdersByUserId(userId);

            // Return 404 if no orders found
            if (orders == null || orders.Count == 0)
            {
                return NotFound();
            }

            // Return the orders
            return Ok(orders);
        }


        // Creates a new order: POST api/Order
        [HttpPost]
        public ActionResult<Order> CreateOrder([FromBody] Order newOrder)
        {
            _orderService.CreateOrder(newOrder);
            return CreatedAtAction(nameof(GetOrderById), new { id = newOrder.Id.ToString() }, newOrder);
        }

        // Updates an order: PUT api/Order/{id}
        [HttpPut("{id}")]
        public IActionResult UpdateOrder(string id, [FromBody] Order updatedOrder)
        {
            var existingOrder = _orderService.GetOrderById(id);
            if (existingOrder == null)
            {
                return NotFound();
            }

            _orderService.UpdateOrder(id, updatedOrder);
            return NoContent();
        }

        // Deletes an order: DELETE api/Order/{id}
        [HttpDelete("{id}")]
        public IActionResult DeleteOrder(string id)
        {
            var existingOrder = _orderService.GetOrderById(id);
            if (existingOrder == null)
            {
                return NotFound();
            }

            _orderService.DeleteOrder(id);
            return NoContent();
        }

        // Updates the processed status of a product: PATCH api/Order/{orderId}/product/{productId}
        [HttpPatch("{orderId}/product/{productId}")]
        public IActionResult UpdateProductProcessedStatus(string orderId, string productId, [FromBody] bool processed)
        {
            _orderService.UpdateProductProcessedStatus(orderId, productId, processed);
            return NoContent();
        }

        // Updates the order status: PATCH api/Order/{orderId}/status/{statusType}
        [HttpPatch("{orderId}/status/{statusType}")]
        public IActionResult UpdateOrderStatus(string orderId, string statusType)
        {
            _orderService.UpdateOrderStatus(orderId, statusType);
            return NoContent();
        }
    }

}
