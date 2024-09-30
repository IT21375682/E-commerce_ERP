/*
 * File: Order.cs
 * Author: Sanjayan
 * Description: This file contains the definition of the Order class, which models an order in the e-commerce system.
 * Each order has multiple products, counts, and processed statuses, as well as an overall order status with nested states.
 */

using MongoDB.Bson.Serialization.Attributes;
using MongoDB.Bson;
using System;
using System.Collections.Generic;

namespace E_commerce.Models
{
    public class Order
    {
        [BsonId] // Indicates this is the primary key
        [BsonRepresentation(BsonType.ObjectId)]
        public string Id { get; set; } // MongoDB generated ID
        public string UserId { get; set; }   // Reference to the user who placed the order
        public DateTime Date { get; set; }   // Date when the order was placed
        public decimal Total { get; set; }   // Total price of the order
        public List<ProductItem> ProductItems { get; set; }  // List of products with quantities and processed status
        public OrderStatus Status { get; set; } = new OrderStatus();   // Initialize Overall status of the order
    }

    // Represents each product in the order
    public class ProductItem
    {
        public string ProductId { get; set; }  // Product identifier
        public int Count { get; set; }         // Quantity of the product
        public bool Processed { get; set; }    // Whether the product is processed or not
    }

    // Represents the nested status of an order
    public class OrderStatus
    {
        public bool? Pending { get; set; } = false;    // Default to false, True if order is pending
        public DateTime? PendingDate { get; set; } = null;  // Date when the order became pending

        public bool? Processing { get; set; } = false;  // Default to false, True if order is processing
        public DateTime? ProcessingDate { get; set; } = null;  // Date when processing started

        public bool? Dispatched { get; set; } = false;  // Default to false, True if order is dispatched
        public DateTime? DispatchedDate { get; set; } = null;  // Date when order was dispatched

        public bool? Delivered { get; set; } = false;   // Default to false, True if order is delivered
        public DateTime? DeliveredDate { get; set; } = null;  // Date when order was delivered

        public bool? Canceled { get; set; } = false;    // Default to false, True if order is canceled
        public DateTime? CanceledDate { get; set; } = null;   // Date when order was canceled
    }
}
