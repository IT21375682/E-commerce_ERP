/*
 * File: CartRepository.cs
 * Author: Sanjayan
 * Description: This file contains the CartRepository class that handles database operations related to carts.
 */

using MongoDB.Driver;
using E_commerce.Models;
using System.Collections.Generic;
using System.Threading.Tasks;
using MongoDB.Bson;

namespace E_commerce.Repositories
{
    public class ICartRepository
    {
        private readonly IMongoCollection<Cart> _carts;

        public ICartRepository(IMongoDatabase database)
        {
            _carts = database.GetCollection<Cart>("Carts");
        }

        // Get all carts
        public async Task<List<Cart>> GetAllCarts()
        {
            return await _carts.Find(cart => true).ToListAsync();
        }

        // Get a cart by user ID
        public async Task<Cart> GetCartByUserId(string userId)
        {
            return await _carts.Find(cart => cart.UserId == userId).FirstOrDefaultAsync();
        }

        // Create a new cart
        public async Task CreateCart(Cart cart)
        {
            await _carts.InsertOneAsync(cart);
        }

        // Update a cart
        public async Task UpdateCart(string cartId, Cart cart)
        {
            var objectId = new ObjectId(cartId);
            await _carts.ReplaceOneAsync(c => c.CartId == objectId, cart);
        }

        // Delete a cart
        public async Task DeleteCart(string cartId)
        {
            var objectId = new ObjectId(cartId);
            await _carts.DeleteOneAsync(c => c.CartId == objectId);
        }

        // Add or update a product in the cart
        public async Task AddOrUpdateProductInCart(string cartId, CartProductItem product)
        {
            var objectId = new ObjectId(cartId);
            var filter = Builders<Cart>.Filter.Eq(c => c.CartId, objectId);
            var cart = await _carts.Find(filter).FirstOrDefaultAsync();

            if (cart != null)
            {
                // Check if the product already exists in the cart
                var existingProduct = cart.Products.Find(p => p.ProductId == product.ProductId);

                if (existingProduct != null)
                {
                    // Update the product count if it exists
                    existingProduct.Count = product.Count;

                    // Remove the product from the cart if count is 0
                    if (existingProduct.Count == 0)
                    {
                        cart.Products.Remove(existingProduct);
                    }
                }
                else
                {
                    // Add new product if it doesn't exist
                    cart.Products.Add(product);
                }

                // Update the cart in the database
                await _carts.ReplaceOneAsync(filter, cart);
            }
        }
    }
}
