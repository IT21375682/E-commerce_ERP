namespace E_commerce.Models
{
    public class Product
    {
        public string ProductId { get; set; }  // Unique Product ID
        public string VendorId { get; set; }  // ID of the vendor who created the product
        public string Name { get; set; }
        public string Description { get; set; }
        public string Category { get; set; }  // Product category (e.g., Electronics, Clothing)
        public double Price { get; set; }
        public bool IsActive { get; set; }  // Whether the product is active or inactive
        public int StockQuantity { get; set; }  // Available stock for the product
        public DateTime CreatedAt { get; set; }
        public DateTime? UpdatedAt { get; set; }
    }
}
