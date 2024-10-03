using MongoDB.Bson;
﻿using E_commerce.DTOs;
using E_commerce.Models;
using E_commerce.Services;
using MongoDB.Driver;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Numerics;

    namespace E_commerce.Repositories
{
    public class IProductRepository
    {
        private readonly IMongoCollection<Product> _products;
        private readonly IMongoCollection<Category> _categories;
        private readonly IMongoCollection<User> _users;  // Collection for users (vendors)
        private readonly EmailService _emailService;
        private readonly IMongoCollection<User> _vendors; // Add this to fetch vendor details
        private readonly IMongoCollection<Comment> _comments; // Add this to fetch average ratings


        public IProductRepository(IMongoDatabase database, EmailService emailService)
        {
            _products = database.GetCollection<Product>("Products");
            _users = database.GetCollection<User>("Users");  // Initialize the users collection
            _emailService = emailService;
            _categories = database.GetCollection<Category>("Category");
            _vendors = database.GetCollection<User>("Users"); // Assuming users table has vendor info
            _comments = database.GetCollection<Comment>("Comments"); // Assuming comments have ratings
        }

        public IEnumerable<Product> GetAllProducts()
        {
            return _products.Find(product => true).ToList();
        }

        public Product GetProductById(string id)
        {
            return _products.Find(product => product.Id == ObjectId.Parse(id).ToString()).FirstOrDefault();
        }

        public IEnumerable<Product> GetProductsByCategoryId(string categoryId)
        {
            return _products.Find(product => product.CategoryId == categoryId).ToList();
        }

        public void CreateProduct(Product product)
        {
            product.Id = ObjectId.GenerateNewId().ToString();
            _products.InsertOne(product);
        }

        public void UpdateProduct(string id, Product product)
        {
            product.Id = ObjectId.Parse(id).ToString(); // Ensure the Id is of type ObjectId
            _products.ReplaceOne(existingProduct => existingProduct.Id == product.Id, product);
        }

        public void DeleteProduct(string id)
        {
            _products.DeleteOne(product => product.Id == ObjectId.Parse(id).ToString());
        }

        public (bool isSuccess, string message) UpdateStock(string productId, int quantity)
        {
            var product = GetProductById(productId);
            if (product != null)
            {
                product.AvailableStock += quantity;  // Update available stock
                product.StockLastUpdated = DateTime.UtcNow;  // Update last modified time

                UpdateProduct(productId, product);  // Save the updated product

                // Check for low stock alert
                if (product.AvailableStock < 50)
                {
                    var vendor = GetVendorById(product.VendorId);  // Fetch vendor information
                    if (vendor != null)
                    {
                        SendLowStockEmail(product, vendor.Email);  // Send email to vendor
                    }

                    return (true, $"Alert: Stock for product '{product.Name}' is below 50. Current stock: {product.AvailableStock}");
                }

                return (true, "Stock updated successfully.");
            }
            else
            {
                throw new Exception($"Product with ID {productId} not found.");
            }
        }

        public (bool isSuccess, string message) RemoveStock(string productId, int quantity)
        {
            var product = GetProductById(productId);
            if (product != null)
            {
                if (product.AvailableStock >= quantity)
                {
                    product.AvailableStock -= quantity;  // Remove the stock
                    product.StockLastUpdated = DateTime.UtcNow;  // Update last modified time

                    UpdateProduct(productId, product);  // Save the updated product

                    // Check for low stock alert
                    if (product.AvailableStock < 50)
                    {
                        var vendor = GetVendorById(product.VendorId);  // Fetch vendor information
                        if (vendor != null)
                        {
                            Console.WriteLine("Email start");
                            SendLowStockEmail(product, vendor.Email);  // Send email to vendor
                        }

                        return (true, $"Alert: Stock for product '{product.Name}' is below 50. Current stock: {product.AvailableStock}");
                    }

                    return (true, "Stock removed successfully.");
                }
                else
                {
                    throw new Exception($"Not enough stock to remove for product ID {productId}. Available stock: {product.AvailableStock}");
                }
            }
            else
            {
                throw new Exception($"Product with ID {productId} not found.");
            }
        }

        private User GetVendorById(string vendorId)
        {
            Console.WriteLine("VendorId : " + vendorId);
            return _users.Find(user => user.Id.ToString() == vendorId).FirstOrDefault();  // Fetch the vendor from the users collection
        }

        private async void SendLowStockEmail(Product product, string vendorEmail)
        {
            Console.WriteLine("Email start 1");
            var subject = $"Low Stock Alert for {product.Name}";
            var message = $"Dear Vendor,<br/><br/>" +
                          $"The stock for product <strong>{product.Name}</strong> (ID: {product.Id}) is running low.<br/>" +
                          $"Current stock level: <strong>{product.AvailableStock}</strong>.<br/><br/>" +
                          $"Please restock as soon as possible.<br/><br/>" +
                          $"Best Regards,<br/>E-commerce Team";

            // Send the email
            await _emailService.SendEmailAsync(vendorEmail, subject, message);
        }

        public int GetAvailableStockById(string productId)
        {
            var product = _products.Find(p => p.Id == productId && p.IsActive).FirstOrDefault();
            if (product == null)
                throw new Exception("Product not found or inactive.");

            return product.AvailableStock;
        }

        // Get all active products
        public IEnumerable<Product> GetAllActiveProducts()
        {
            return _products.Find(product => product.IsActive).ToList();
        }

        public IEnumerable<Product> GetAllDeActivatedProducts()
        {
            return _products.Find(product => !product.IsActive).ToList();
        }


        // Get all active products in a specific active category
        public IEnumerable<Product> GetAllActiveCategoryProducts(string categoryId)
        {
            var activeCategory = _categories.Find(category => category.Id == categoryId && category.IsActive).FirstOrDefault();
            if (activeCategory != null)
            {
                return _products.Find(product => product.CategoryId == categoryId && product.IsActive).ToList();
            }
            return Enumerable.Empty<Product>();
        }

        // Get all active categories and their active products
        public IEnumerable<(Category, IEnumerable<Product>)> GetAllActiveCategoryAndActiveProducts()
        {
            var activeCategories = _categories.Find(category => category.IsActive).ToList();
            var result = new List<(Category, IEnumerable<Product>)>();

            foreach (var category in activeCategories)
            {
                var activeProducts = _products.Find(product => product.CategoryId == category.Id && product.IsActive).ToList();
                result.Add((category, activeProducts));
            }

            return result;
        }
        // Method to get active products with category and vendor details
        public IEnumerable<ProductDetailsDto> GetAllActiveProductsWithDetails()
        {
            // Fetch active products
            var activeProducts = _products.Find(product => product.IsActive).ToList();

            // Get active category IDs
            var activeCategories = _categories.Find(category => category.IsActive).ToList();
            var activeCategoryIds = activeCategories.Select(c => c.Id).ToHashSet();

            var categoryLookup = activeCategories.ToDictionary(c => c.Id, c => c.CategoryName);
            var vendorLookup = _vendors.Find(vendor => true).ToList()
                .ToDictionary(v => v.Id.ToString(), v => v.Name);


            // Build the product details DTOs
            var productDetails = activeProducts.Select(product => new ProductDetailsDto
            {
                ProductId = product.Id,
                Name = product.Name,
                ProductImage = product.ProductImage,
                CategoryId = product.CategoryId,
                Description = product.Description,
                Price = product.Price,
                AvailableStock = product.AvailableStock,
                IsActive = product.IsActive,
                VendorId = product.VendorId,
                CreatedAt = product.CreatedAt,
                StockLastUpdated = product.StockLastUpdated,
                ProductCategoryName = categoryLookup.TryGetValue(product.CategoryId, out var categoryName) ? categoryName : null,
                VendorName = vendorLookup.TryGetValue(product.VendorId, out var vendorName) ? vendorName : null,
                AverageRating = GetAverageRatingByProductId(product.Id)
            });

            return productDetails;
        }

        // Method to get details of a single active product by ID
        public ProductDetailsDto GetActiveProductWithDetailsById(string productId)
        {
            // Fetch the product by ID and check if it's active
            var product = _products.Find(product => product.Id == productId && product.IsActive).FirstOrDefault();

            if (product == null)
                return null;

            // Fetch active category details
            var activeCategory = _categories.Find(category => category.Id == product.CategoryId && category.IsActive).FirstOrDefault();
            var categoryName = activeCategory?.CategoryName;

            // Fetch vendor details
            var vendor = _vendors.Find(vendor => vendor.Id == product.VendorId).FirstOrDefault();
            var vendorName = vendor?.Name;

            // Build the product details DTO
            return new ProductDetailsDto
            {
                ProductId = product.Id,
                Name = product.Name,
                ProductImage = product.ProductImage,
                CategoryId = product.CategoryId,
                Description = product.Description,
                Price = product.Price,
                AvailableStock = product.AvailableStock,
                IsActive = product.IsActive,
                VendorId = product.VendorId,
                CreatedAt = product.CreatedAt,
                StockLastUpdated = product.StockLastUpdated,
                ProductCategoryName = categoryName,
                VendorName = vendorName,
                AverageRating = GetAverageRatingByProductId(product.Id)
            };
        }


        private double GetAverageRatingByProductId(string productId)
        {
            var ratings = _comments.Find(comment => comment.productId == productId)
                                   .ToList()
                                   .Select(c => c.rating);
            return ratings.Any() ? ratings.Average() : 0;
        }

        public async Task ToggleIsActiveAsync(string productId)
        {
            var filter = Builders<Product>.Filter.Eq(p => p.Id, productId);

            // Fetch the current product
            var product = await _products.Find(filter).FirstOrDefaultAsync();

            if (product != null)
            {
                // Toggle the IsActive value
                var newIsActiveValue = !product.IsActive;

                // Update with the new IsActive value
                var update = Builders<Product>.Update
                    .Set(p => p.IsActive, newIsActiveValue)
                    .CurrentDate(p => p.StockLastUpdated); // Update the stock last modified date (optional)

                // Apply the update
                await _products.UpdateOneAsync(filter, update);
            }
        }

        public async Task UpdateManyProductsAsync(FilterDefinition<Product> filter, UpdateDefinition<Product> update)
        {
            await _products.UpdateManyAsync(filter, update);
        }




    }
}
