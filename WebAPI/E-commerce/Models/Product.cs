/*
 * File: Product.cs
 * Author: Krithiga. D. B
 * Description: This file contains the definition of the Product class, which models an Product in the e-commerce system.
 * Each order has product details such as image, name, price associated vendorId and categoryId as well as this product class contains the inventory details as stock
 */
using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

namespace E_commerce.Models
{
    public class Product
    {
        [BsonId]
        [BsonRepresentation(BsonType.ObjectId)]  // Tells MongoDB to store this as an ObjectId but treat it as a string in C#
        public string? Id { get; set; }  // Unique product ID

        [BsonElement("name")]
        public string Name { get; set; }

        [BsonElement("productImage")]
        public string ProductImage { get; set; }

        [BsonElement("categoryId")]
        public string CategoryId { get; set; }

        [BsonElement("description")]
        public string Description { get; set; }
        
        [BsonElement("price")]
        public decimal Price { get; set; }

        [BsonElement("availableStock")]
        public int AvailableStock { get; set; }  // Stock level of the product

        [BsonElement("isActive")]
        public bool IsActive { get; set; } = true;// Whether the product is active or deactivated

        [BsonElement("vendorId")]
        public string VendorId { get; set; }  // The vendor who owns the product

        [BsonElement("createdAt")]
        public DateTime CreatedAt { get; set; }

        [BsonElement("stockLastUpdated")]
        public DateTime StockLastUpdated { get; set; } 

    }
}
