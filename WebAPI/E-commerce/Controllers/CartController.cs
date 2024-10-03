/*
 * File: CartController.cs
 * Author: Sanjayan
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

        public CartController(CartService cartService)
        {
            _cartService = cartService;
        }

        // GET: api/Cart
        [HttpGet]
        public async Task<IActionResult> GetAllCarts()
        {
            var carts = await _cartService.GetAllCarts();
            return Ok(carts);
        }

        // GET: api/Cart/{userId}
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

        // POST: api/Cart
        [HttpPost]
        public async Task<IActionResult> CreateCart([FromBody] Cart cart)
        {
            await _cartService.CreateCart(cart);
            return CreatedAtAction(nameof(GetCart), new { userId = cart.UserId }, cart);
        }

        // PUT: api/Cart/{cartId}
        [HttpPut("{cartId}")]
        public async Task<IActionResult> UpdateCart(string cartId, [FromBody] Cart cart)
        {
            await _cartService.UpdateCart(cartId, cart);
            return NoContent();
        }

        // DELETE: api/Cart/{cartId}
        [HttpDelete("{cartId}")]
        public async Task<IActionResult> DeleteCart(string cartId)
        {
            await _cartService.DeleteCart(cartId);
            return NoContent();
        }

        // PATCH: api/Cart/{cartId}/product
        [HttpPatch("{cartId}/product")]
        public async Task<IActionResult> AddOrUpdateProductInCart(string cartId, [FromBody] CartProductItem product)
        {
            await _cartService.AddOrUpdateProductInCart(cartId, product);
            return NoContent();
        }

        // GET: api/Cart/{userId}/product-details
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

        // PUT: api/Cart/{cartId}
        [HttpPut("update-remove/{cartId}")]
        public async Task<IActionResult> UpdateCartWithRemove(string cartId, [FromBody] Cart cart)
        {
            // Remove products with count of 0
            cart.Products.RemoveAll(product => product.Count == 0);

            await _cartService.UpdateCart(cartId, cart);
            return NoContent();
        }

        // GET: api/Cart/{userId}/product-count
        [HttpGet("{userId}/product-count")]
        public async Task<IActionResult> GetProductCountByUserId(string userId)
        {
            int productCount = await _cartService.GetProductCountByUserId(userId);
            return Ok(productCount);
        }

    }
}
