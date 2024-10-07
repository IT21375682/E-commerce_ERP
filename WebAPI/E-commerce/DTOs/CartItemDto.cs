/*
 * File: CartItemDto.cs
 * Author: Sanjayan
 * Registration No: IT21375514
 * Description: This file defines the CartItemDto class, which represents the cart
 * along with detailed product information including product name and price.
 */

using System.Collections.Generic;

namespace E_commerce.DTO
{
    // Represents a shopping cart that contains product details.
    public class CartWithProductDetailsDto
    {
        public string CartId { get; set; }  // Unique identifier for the cart
        public string UserId { get; set; }  // ID of the user who owns the cart
        public List<ProductDetailsDto> Products { get; set; }  // List of products in the cart
    }

    // Represents detailed information about a product in the cart.
    public class ProductDetailsDto
    {
        public string ProductId { get; set; }  // Unique identifier for the product
        public string ProductName { get; set; } // Name of the product
        public string ProductImage { get; set; } // URL or path to the product image

        public decimal Price { get; set; }  // Price of the product
        public int Count { get; set; }      // Quantity of the product in the cart
    }
}
