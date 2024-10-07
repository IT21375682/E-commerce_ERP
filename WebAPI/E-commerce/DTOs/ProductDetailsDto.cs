/*
 * File: ProductDetailsDto.cs
 * Author: Sanjayan
 * Registration No: IT21375514
 * Description: This file defines the ProductDetailsDto class, which represents the data transfer object for product details in the e-commerce system.
 * It includes fields for product information, vendor details, and stock management.
 */

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
