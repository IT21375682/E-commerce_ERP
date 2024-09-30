namespace E_commerce.DTOs
{
    public class ProductDetailsDto
    {
        public string ProductId { get; set; }  // Unique product ID
        public string Name { get; set; }  // Product name
        public string ProductImage { get; set; }  // URL or path to product image
        public string CategoryId { get; set; }  // Category ID
        public string Description { get; set; }  // Product description
        public decimal Price { get; set; }  // Product price
        public int AvailableStock { get; set; }  // Available stock level
        public bool IsActive { get; set; }  // Whether the product is active or deactivated
        public string VendorId { get; set; }  // The vendor who owns the product
        public DateTime CreatedAt { get; set; }  // Creation date of the product
        public DateTime StockLastUpdated { get; set; }  // Date when stock was last updated
        public string ProductCategoryName { get; set; }  // Category name for the product
        public string VendorName { get; set; }  // Name of the vendor
        public double AverageRating { get; set; }  // Average rating of the product
    }
}
