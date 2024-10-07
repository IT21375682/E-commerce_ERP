/*
 * File: SingleOrderItemDto.cs
 * Author: Sanjayan
 * Registration No: IT21375514
 * Description: This file defines the SingleOrderItemDto class, which represents the data transfer object for a single order in the e-commerce system.
 * It includes fields for user, date, total amount, product items, order status, and cancellation notes.
 */

using MongoDB.Bson.Serialization.Attributes;
using MongoDB.Bson;

namespace E_commerce.DTOs
{
    public class SingleOrderItemDto
    {
        public string? Id { get; set; }              // Unique ID for the order
        public string UserId { get; set; }            // ID of the user who placed the order
        public DateTime Date { get; set; }            // Date when the order was placed
        public decimal Total { get; set; }            // Total amount of the order
        public List<SingleOrderProductItem> ProductItems { get; set; } // List of products in the order
        public SingleOrderStatus Status { get; set; } // Current status of the order
        public string? CancellationNote { get; set; } // Note for cancellation, if applicable
    }

    /*
     * File: SingleOrderProductItem.cs
     * Author: Sanjayan
     * Description: This file defines the SingleOrderProductItem class, which represents a product item within a single order.
     * It includes fields for product details such as ID, name, image, price, count, and delivery status.
     */
    public class SingleOrderProductItem
    {
        public string ProductId { get; set; }        // Unique ID of the product
        public string ProductName { get; set; }      // Name of the product
        public string ProductImg { get; set; }       // Image URL of the product
        public string ProductPrice { get; set; }     // Price of the product
        public int Count { get; set; }               // Quantity of the product ordered
        public bool Delivered { get; set; }          // Delivery status of the product
    }

    /*
     * File: SingleOrderStatus.cs
     * Author: Sanjayan
     * Description: This file defines the SingleOrderStatus class, which represents the status of a single order.
     * It includes fields for various stages of order processing, including dates for each status.
     */
    public class SingleOrderStatus
    {
        public bool? Pending { get; set; }                  // Indicates if the order is pending
        public DateTime? PendingDate { get; set; }          // Date when the order was marked as pending

        public bool? Processing { get; set; }               // Indicates if the order is being processed
        public DateTime? ProcessingDate { get; set; }       // Date when the order was marked as processing

        public bool? Dispatched { get; set; }               // Indicates if the order has been dispatched
        public DateTime? DispatchedDate { get; set; }       // Date when the order was dispatched

        public bool? Partially_Delivered { get; set; } = false; // Indicates if the order is partially delivered
        public DateTime? Partially_Delivered_Date { get; set; } // Date when the order was partially delivered

        public bool? Delivered { get; set; }                 // Indicates if the order has been delivered
        public DateTime? DeliveredDate { get; set; }         // Date when the order was delivered

        public bool? Canceled { get; set; }                  // Indicates if the order has been canceled
        public DateTime? CanceledDate { get; set; }          // Date when the order was canceled

        public bool? StockReduced { get; set; }              // Indicates if the stock has been reduced for this order
    }
}
