using MongoDB.Bson.Serialization.Attributes;
using MongoDB.Bson;

namespace E_commerce.DTOs
{
    public class SingleOrderItemDto
    {
        public string? Id { get; set; }
        public string UserId { get; set; }
        public DateTime Date { get; set; }
        public decimal Total { get; set; }
        public List<SingleOrderProductItem> ProductItems { get; set; }
        public SingleOrderStatus Status { get; set; }
        public string? CancellationNote { get; set; }

    }

    public class SingleOrderProductItem
    {
        public string ProductId { get; set; }
        public string ProductName { get; set; }
        public string ProductImg { get; set; }
        public string ProductPrice { get; set; }
        public int Count { get; set; }  
        public bool Delivered { get; set; } 

    }

    public class SingleOrderStatus
    {
        public bool? Pending { get; set; }
        public DateTime? PendingDate { get; set; }

        public bool? Processing { get; set; }
        public DateTime? ProcessingDate { get; set; }

        public bool? Dispatched { get; set; }
        public DateTime? DispatchedDate { get; set; }

        public bool? Partially_Delivered { get; set; } = false;
        public DateTime? Partially_Delivered_Date { get; set; }

        public bool? Delivered { get; set; }
        public DateTime? DeliveredDate { get; set; }

        public bool? Canceled { get; set; }
        public DateTime? CanceledDate { get; set; }

        public bool? StockReduced { get; set; }
    }
}
