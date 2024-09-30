/*
 * File: CartService.cs
 * Author: Sanjayan
 * Description: This file defines the service layer for managing business logic related to carts, 
 * including operations for retrieving, creating, updating, and deleting carts, as well as adding or updating products within a cart.
 */

using E_commerce.Models;
using E_commerce.Repositories;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace E_commerce.Services
{
    public class CartService
    {
        private readonly ICartRepository _cartRepository;

        // Initializes the CartService with the repository.
        public CartService(ICartRepository cartRepository)
        {
            _cartRepository = cartRepository;
        }

        // Retrieves all carts.
        public async Task<List<Cart>> GetAllCarts()
        {
            return await _cartRepository.GetAllCarts();
        }

        // Retrieves a cart by user ID.
        public async Task<Cart> GetCartByUserId(string userId)
        {
            return await _cartRepository.GetCartByUserId(userId);
        }

        // Creates a new cart.
        public async Task CreateCart(Cart cart)
        {
            await _cartRepository.CreateCart(cart);
        }

        // Updates an existing cart.
        public async Task UpdateCart(string cartId, Cart cart)
        {
            await _cartRepository.UpdateCart(cartId, cart);
        }

        // Deletes a cart by its ID.
        public async Task DeleteCart(string cartId)
        {
            await _cartRepository.DeleteCart(cartId);
        }

        // Adds or updates a product in the cart. Removes the product if count is 0.
        public async Task AddOrUpdateProductInCart(string cartId, CartProductItem product)
        {
            await _cartRepository.AddOrUpdateProductInCart(cartId, product);
        }
    }
}
