namespace E_commerce.Models
{
    public class Order
    {
        public string OrderId { get; set; }  // Unique order ID
        public string CustomerId { get; set; }  // The customer who placed the order
        public List<Product> Products { get; set; }  // List of products in the order
        public string Status { get; set; }  // Order status (e.g., Processing, Delivered)
        public DateTime OrderDate { get; set; }
        public DateTime? DeliveryDate { get; set; }
    }
}
