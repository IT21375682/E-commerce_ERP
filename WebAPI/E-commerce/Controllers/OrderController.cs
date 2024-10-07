/*
 * File: OrderController.cs
 * Author: Krithiga D. B
 * Description: This controller handles API requests related to orders. 
 * It provides endpoints to fetch, create, update, and delete orders.
 */

using E_commerce.DTOs;
using E_commerce.Models;
using E_commerce.Services;
using Microsoft.AspNetCore.Mvc;
using MongoDB.Bson;
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

        //[HttpPatch("{orderId}/status/{statusType}")]
        //public async Task<IActionResult> UpdateOrderStatus(string orderId, string statusType)
        //{
        //    await _orderService.UpdateOrderStatus(orderId, statusType);
        //    return NoContent();
        //}

        // Updates the processed status of a order: PATCH api/Order/{orderId}/status/{statusType}
        [HttpPatch("{orderId}/status/{statusType}")]
        public async Task<IActionResult> UpdateOrderStatus(string orderId, string statusType, [FromBody] string customMessage = null)
        {
            try
            {
                await _orderService.UpdateOrderStatus(orderId, statusType, customMessage);
                return NoContent(); // Return 204 No Content if the update was successful
            }
            catch (Exception ex)
            {
                // Handle exceptions (e.g., order not found, email sending failed, etc.)
                return StatusCode(500, $"Internal server error: {ex.Message}"); // You may want to log the error here
            }
        }


        // Cancels an order: POST api/Order/{orderId}/cancel
        [HttpPost("{orderId}/cancel")]
        public async Task<IActionResult> CancelOrder(string orderId, [FromBody] string customMessage = null)
        {
            try
            {
                await _orderService.CancelOrder(orderId, customMessage);
                return NoContent(); // Return 204 No Content if the cancellation was successful
            }
            catch (Exception ex)
            {
                // Handle exceptions (e.g., order not found, email sending failed, etc.)
                return StatusCode(500, $"Internal server error: {ex.Message}"); // You may want to log the error here
            }
        }

        //// HTTP GET method to get order status by ID
        //[HttpGet("{orderId}/status")]
        //public IActionResult GetOrderStatus(string orderId)
        //{
        //    // Call service method to get the order status
        //    var status = _orderService.GetOrderStatusById(orderId);

        //    if (status == null)
        //    {
        //        // Return a 404 response if the order is not found
        //        return NotFound(new { Message = "Order not found" });
        //    }

        //    // Return the order status
        //    return Ok(status);
        //}

        // HTTP GET method to get order status by OrderID
        [HttpGet("{orderId}/status")]
        public ActionResult<string> GetLatestOrderStatus(string orderId)
        {
            if (string.IsNullOrEmpty(orderId))
            {
                return BadRequest("Order ID cannot be null or empty.");
            }

            var latestStatus = _orderService.GetLatestOrderStatusById(orderId);

            if (latestStatus == "Unknown")
            {
                return NotFound("Order status not found.");
            }

            return Ok(latestStatus);
        }

        // GET method to get orders per vendorId "api/order/orderList/{vendorId}/{orderId}"
        [HttpGet("{vendorId}/{orderId}")]
        public ActionResult<List<ProductItem>> GetProductsByVendor(string vendorId, string orderId)
        {
            try
            {
                var products = _orderService.GetProductsByVendorId(orderId, vendorId);
                return Ok(products);
            }
            catch (Exception ex)
            {
                return NotFound(ex.Message);
            }
        }

        // Update method to edit order's product delivery status according to condition "api/order/orderList/{vendorId}/{orderId}"
        [HttpPatch("update-delivery-status/{orderId}/{vendorId}/{productId}")]
        public IActionResult UpdateProductDeliveryStatus(string orderId, string vendorId, string productId)
        {
            try
            {
                // Convert productId from string to ObjectId
                var productObjectId = new ObjectId(productId);

                _orderService.UpdateProductDeliveryStatus(orderId, vendorId, productObjectId);
                return NoContent(); // 204 No Content
            }
            catch (FormatException ex)
            {
                // Handle the case where the productId is not a valid ObjectId
                return BadRequest(new { Message = "Invalid Product ID format." });
            }
            catch (Exception ex)
            {
                // Return a 404 with the exception message
                return NotFound(new { Message = ex.Message }); // Return a more structured response
            }
        }

        // GET api/Order/details/{id}
        [HttpGet("single-order-detail/{id}")]
        public ActionResult<SingleOrderItemDto> GetSingleOrderOrderDetails(string id)
        {
            var orderDetails = _orderService.GetSingleOrderWithProductDetails(id);
            if (orderDetails == null)
            {
                return NotFound();
            }
            return Ok(orderDetails);
        }


    }

}
