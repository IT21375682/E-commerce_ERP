/*
 * File: CartController.cs
 * Author: Sanjayan
 * Registration No: IT21375514
 * Description: This file contains the CartController class that handles HTTP requests related to carts.
 */

using Microsoft.AspNetCore.Mvc;
using E_commerce.Models;
using E_commerce.Services;
using System.Threading.Tasks;

namespace E_commerce.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class CartController : ControllerBase
    {
        private readonly CartService _cartService;

        // Initializes the controller with the service.
        public CartController(CartService cartService)
        {
            _cartService = cartService;
        }

        // GET: api/Cart - Retrieves all cart details
        [HttpGet]
        public async Task<IActionResult> GetAllCarts()
        {
            var carts = await _cartService.GetAllCarts();
            return Ok(carts);
        }

        // GET: api/Cart/{userId} - Retrieves all cart details of a user
        [HttpGet("{userId}")]
        public async Task<IActionResult> GetCart(string userId)
        {
            var cart = await _cartService.GetCartByUserId(userId);
            if (cart == null)
            {
                return NotFound();
            }
            return Ok(cart);
        }

        // POST: api/Cart - Create a cart 
        [HttpPost]
        public async Task<IActionResult> CreateCart([FromBody] Cart cart)
        {
            await _cartService.CreateCart(cart);
            return CreatedAtAction(nameof(GetCart), new { userId = cart.UserId }, cart);
        }

        // PUT: api/Cart/{cartId} - Update a cart details
        [HttpPut("{cartId}")]
        public async Task<IActionResult> UpdateCart(string cartId, [FromBody] Cart cart)
        {
            await _cartService.UpdateCart(cartId, cart);
            return NoContent();
        }

        // DELETE: api/Cart/{cartId} - Delete a cart
        [HttpDelete("{cartId}")]
        public async Task<IActionResult> DeleteCart(string cartId)
        {
            await _cartService.DeleteCart(cartId);
            return NoContent();
        }

        // PATCH: api/Cart/{cartId}/product - Add or Update a cart details
        [HttpPatch("{cartId}/product")]
        public async Task<IActionResult> AddOrUpdateProductInCart(string cartId, [FromBody] CartProductItem product)
        {
            await _cartService.AddOrUpdateProductInCart(cartId, product);
            return NoContent();
        }

        // GET: api/Cart/{userId}/product-details - Get a cart details with addition data
        [HttpGet("{userId}/product-details")]
        public async Task<IActionResult> GetCartWithProductByUserId(string userId)
        {
            var cart = await _cartService.GetCartWithProductByUserId(userId);
            if (cart == null)
            {
                return NotFound();
            }
            return Ok(cart);
        }

        // PUT: api/Cart/{cartId} - Update or Remove cart details
        [HttpPut("update-remove/{cartId}")]
        public async Task<IActionResult> UpdateCartWithRemove(string cartId, [FromBody] Cart cart)
        {
            // Remove products with count of 0
            cart.Products.RemoveAll(product => product.Count == 0);

            await _cartService.UpdateCart(cartId, cart);
            return NoContent();
        }

        // GET: api/Cart/{userId}/product-count - Get cart product count
        [HttpGet("{userId}/product-count")]
        public async Task<IActionResult> GetProductCountByUserId(string userId)
        {
            int productCount = await _cartService.GetProductCountByUserId(userId);
            return Ok(productCount);
        }

    }
}
