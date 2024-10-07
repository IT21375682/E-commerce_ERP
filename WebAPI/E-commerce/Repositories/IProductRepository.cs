/*
 * File: ProductRepository.cs
 * Author: krithiga
 * Description: This file contains the ProductRepository class that handles database operations related to products and inventory.
 */

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
        private readonly IMongoCollection<Order> _orders;

        public IProductRepository(IMongoDatabase database, EmailService emailService)
        {
            _products = database.GetCollection<Product>("Products");
            _users = database.GetCollection<User>("Users");  // Initialize the users collection
            _emailService = emailService;
            _categories = database.GetCollection<Category>("Category");
            _vendors = database.GetCollection<User>("Users"); // Assuming users table has vendor info
            _comments = database.GetCollection<Comment>("Comments"); // Assuming comments have ratings
            _orders = database.GetCollection<Order>("Orders");

        }

        //get all products
        public IEnumerable<Product> GetAllProducts()
        {
            return _products.Find(product => true).ToList();
        }

        //get product by ID
        public Product GetProductById(string id)
        {
            if (!ObjectId.TryParse(id, out _))
            {
                Console.WriteLine($"Invalid ObjectId format: {id}");
                return null; // Return null if id is not a valid ObjectId
            }

            return _products.Find(product => product.Id == id).FirstOrDefault();
        }

        //get product by cetegoryID
        public IEnumerable<Product> GetProductsByCategoryId(string categoryId)
        {
            return _products.Find(product => product.CategoryId == categoryId).ToList();
        }

//get products by vendorID
        public IEnumerable<Product> GetProductsByVendorId(string vendorId)
        {
            return _products.Find(product => product.VendorId == vendorId).ToList();
        }

        //create product
        public void CreateProduct(Product product)
        {
            product.Id = ObjectId.GenerateNewId().ToString();
            product.IsActive = true; // Set IsActive to true
            _products.InsertOne(product);
        }

        //Update product by by id
        public void UpdateProduct(string id, Product product)
        {
            product.Id = ObjectId.Parse(id).ToString(); // Ensure the Id is of type ObjectId
            _products.ReplaceOne(existingProduct => existingProduct.Id == product.Id, product);
        }

        //delete product by id
        public void DeleteProduct(string id)
        {
            _products.DeleteOne(product => product.Id == ObjectId.Parse(id).ToString());
        }

        //update stock by by id & quantity
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

        //remove stock by id & quantity
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

        //get vendorId by productId
        private User GetVendorById(string vendorId)
        {
            Console.WriteLine("VendorId : " + vendorId);
            return _users.Find(user => user.Id.ToString() == vendorId).FirstOrDefault();  // Fetch the vendor from the users collection
        }

        //send low stock email to vendors 
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

        //get available stock per product by id
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

        // Get all deactive products
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

        // Method to get average rating of a single product by ID
        private double GetAverageRatingByProductId(string productId)
        {
            var ratings = _comments.Find(comment => comment.productId == productId)
                                   .ToList()
                                   .Select(c => c.rating);
            return ratings.Any() ? ratings.Average() : 0;
        }

        // Method to get product details with category details
        public IEnumerable<ProductDetailsDto> GetActiveProductsWithDetailsByCategory(string categoryId)
        {
            // Fetch active products
            var activeProducts = _products.Find(product => product.IsActive).ToList();

            activeProducts = activeProducts.Where(p => p.CategoryId == categoryId).ToList();

            // Get active category and vendor details
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
        //public async Task ToggleIsActiveAsync(string productId)
        //      {
        //          var filter = Builders<Product>.Filter.Eq(p => p.Id, productId);

        //          // Fetch the current product
        //          var product = await _products.Find(filter).FirstOrDefaultAsync();

        //          if (product != null)
        //          {
        //              // Toggle the IsActive value
        //              var newIsActiveValue = !product.IsActive;

        //              // Update with the new IsActive value
        //              var update = Builders<Product>.Update
        //                  .Set(p => p.IsActive, newIsActiveValue)
        //                  .CurrentDate(p => p.StockLastUpdated); // Update the stock last modified date (optional)

        //              // Apply the update
        //              await _products.UpdateOneAsync(filter, update);
        //          }
        //      }

        //public async Task ToggleIsActiveAsync(string productId)
        //{
        //    var filter = Builders<Product>.Filter.Eq(p => p.Id, productId);

        //    // Fetch the current product
        //    var product = await _products.Find(filter).FirstOrDefaultAsync();

        //    if (product != null && product.IsActive)
        //    {
        //        // Find orders with the product and active statuses
        //        var orderFilter = Builders<Order>.Filter.And(
        //            Builders<Order>.Filter.ElemMatch(o => o.ProductItems, pi => pi.ProductId == productId),
        //            Builders<Order>.Filter.Or(
        //                Builders<Order>.Filter.Eq(o => o.Status.Pending, true),
        //                Builders<Order>.Filter.Eq(o => o.Status.Processing, true),
        //                Builders<Order>.Filter.Eq(o => o.Status.Dispatched, true),
        //                Builders<Order>.Filter.Eq(o => o.Status.Partially_Delivered, true)
        //            )
        //        );

        //        // Get orders that match the filter
        //        var activeOrders = await _orders.Find(orderFilter).ToListAsync();

        //        // Check if the latest status in each order is active
        //        bool hasRecentActiveStatus = activeOrders.Any(order =>
        //        {
        //            // Get the latest status
        //            string latestStatus = GetLatestStatus(order.Status);

        //            // Return true if the latest status is an active one
        //            return latestStatus == "Pending" || latestStatus == "Processing" ||
        //                   latestStatus == "Dispatched" || latestStatus == "Partially Delivered";
        //        });

        //        // Proceed to deactivate if no orders have a recent active status
        //        if (!hasRecentActiveStatus)
        //        {
        //            var newIsActiveValue = !product.IsActive;

        //            var update = Builders<Product>.Update
        //                .Set(p => p.IsActive, newIsActiveValue)
        //                .CurrentDate(p => p.StockLastUpdated); // Optionally update stock last modified date

        //            await _products.UpdateOneAsync(filter, update);
        //        }
        //        else
        //        {
        //            Console.WriteLine("Product cannot be deactivated as it is part of orders with recent active statuses.");
        //        }
        //    }
        //}

        // Method to toggle status
        public async Task ToggleIsActiveAsync(string productId)
        {
            var filter = Builders<Product>.Filter.Eq(p => p.Id, productId);

            // Fetch the current product
            var product = await _products.Find(filter).FirstOrDefaultAsync();

            if (product != null)
            {
                bool newIsActiveValue;

                // Find orders with the product regardless of their status
                var orderFilter = Builders<Order>.Filter.ElemMatch(o => o.ProductItems, pi => pi.ProductId == productId);
                var allOrders = await _orders.Find(orderFilter).ToListAsync();

                // If the product is currently inactive, always activate it
                if (!product.IsActive)
                {
                    newIsActiveValue = true;
                    Console.WriteLine("Activating product since it is currently inactive.");
                }
                else // The product is currently active
                {
                    // Check for orders with Canceled or Delivered statuses
                    var canceledOrDeliveredOrderFilter = Builders<Order>.Filter.And(
                        Builders<Order>.Filter.ElemMatch(o => o.ProductItems, pi => pi.ProductId == productId),
                        Builders<Order>.Filter.Or(
                            Builders<Order>.Filter.Eq(o => o.Status.Canceled, true),
                            Builders<Order>.Filter.Eq(o => o.Status.Delivered, true)
                        )
                    );

                    var canceledOrDeliveredOrders = await _orders.Find(canceledOrDeliveredOrderFilter).ToListAsync();

                    // Deactivate if there are Canceled or Delivered orders
                    if (canceledOrDeliveredOrders.Any())
                    {
                        newIsActiveValue = false;
                        Console.WriteLine("Deactivating product as it is part of orders with Canceled or Delivered statuses.");
                    }
                    else
                    {
                        // Check for active orders
                        var activeOrderFilter = Builders<Order>.Filter.And(
                            Builders<Order>.Filter.ElemMatch(o => o.ProductItems, pi => pi.ProductId == productId),
                            Builders<Order>.Filter.Or(
                                Builders<Order>.Filter.Eq(o => o.Status.Pending, true),
                                Builders<Order>.Filter.Eq(o => o.Status.Processing, true),
                                Builders<Order>.Filter.Eq(o => o.Status.Dispatched, true),
                                Builders<Order>.Filter.Eq(o => o.Status.Partially_Delivered, true)
                            )
                        );

                        // Get active orders
                        var activeOrders = await _orders.Find(activeOrderFilter).ToListAsync();

                        // If there are no active orders, deactivate the product
                        if (!activeOrders.Any())
                        {
                            newIsActiveValue = false;
                            Console.WriteLine("Deactivating product as there are no active orders.");
                        }
                        else
                        {
                            newIsActiveValue = true;
                            Console.WriteLine("Keeping product active as it is part of active orders.");
                        }
                    }
                }

                // Update the product's IsActive value
                var update = Builders<Product>.Update
                    .Set(p => p.IsActive, newIsActiveValue)
                    .CurrentDate(p => p.StockLastUpdated); // Optionally update stock last modified date

                await _products.UpdateOneAsync(filter, update);
            }
        }

        // Helper method to get the latest status based on date
        private string GetLatestStatus(OrderStatus status)
        {
            var statusUpdates = new Dictionary<string, DateTime?>()
    {
        { "Pending", status.PendingDate },
        { "Processing", status.ProcessingDate },
        { "Dispatched", status.DispatchedDate },
        { "Partially Delivered", status.Partially_Delivered_Date },
        { "Delivered", status.DeliveredDate },
        { "Canceled", status.CanceledDate }
    };

            var latestStatus = statusUpdates
                .Where(s => s.Value != null) // Filter out null dates
                .OrderByDescending(s => s.Value) // Order by the most recent date
                .FirstOrDefault();

            return latestStatus.Key ?? "Unknown";
        }


        // Updates multiple products based on a filter with specified updates.
        public async Task UpdateManyProductsAsync(FilterDefinition<Product> filter, UpdateDefinition<Product> update)
        {
            await _products.UpdateManyAsync(filter, update);
        }

        // Counts the documents in the collection based on a specific filter.
        public async Task<long> CountAsync(FilterDefinition<Product> filter)
        {
            return await _products.CountDocumentsAsync(filter);
        }
    }
}
