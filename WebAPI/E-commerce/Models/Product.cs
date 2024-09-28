namespace E_commerce.Models
{
    public class Product
    {
        public string Id { get; set; }  // Unique product ID
        public string Name { get; set; }
        public string CategoryId { get; set; }
        public string Description { get; set; }
        public decimal Price { get; set; }
        public int AvailableStock { get; set; }  // Stock level of the product
        public bool IsActive { get; set; }  // Whether the product is active or deactivated
        public string VendorId { get; set; }  // The vendor who owns the product
        public DateTime CreatedAt { get; set; }
        public DateTime StockLastUpdated { get; set; } 

    }
}
