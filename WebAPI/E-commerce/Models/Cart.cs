/*
 * File: Cart.cs
 * Author: Sanjayan
 * Description: This file contains the definition of the Cart class, which models a shopping cart in the e-commerce system.
 * Each cart is associated with a user and contains products with their respective counts.
 */

using MongoDB.Bson.Serialization.Attributes;
using MongoDB.Bson;
using System;
using System.Collections.Generic;

namespace E_commerce.Models
{
    public class Cart
    {
        [BsonId] // Indicates this is the primary key
        public ObjectId CartId { get; set; } // MongoDB generated ID
        public string UserId { get; set; } // Reference to the user who owns the cart
        public List<CartProductItem> Products { get; set; } = new List<CartProductItem>(); // List of products with their counts
    }

    public class CartProductItem
    {
        public string ProductId { get; set; } // Product ID
        public int Count { get; set; } // Quantity of the product
    }
}
