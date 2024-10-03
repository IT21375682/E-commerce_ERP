/*
 * File: CartWithProductDetailsDto.cs
 * Author: Sanjayan
 * Description: This file defines the CartWithProductDetailsDto class, which represents the cart
 * along with detailed product information including product name and price.
 */

namespace E_commerce.DTO
{
    public class CartWithProductDetailsDto
    {
        public string CartId { get; set; }
        public string UserId { get; set; }
        public List<ProductDetailsDto> Products { get; set; }
    }

    public class ProductDetailsDto
    {
        public string ProductId { get; set; }
        public string ProductName { get; set; }
        public decimal Price { get; set; }
        public int Count { get; set; }
    }
}
