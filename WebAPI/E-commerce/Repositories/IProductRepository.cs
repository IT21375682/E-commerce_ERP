using E_commerce.DTOs;
using E_commerce.Models;
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
        private readonly IMongoCollection<User> _vendors; // Add this to fetch vendor details
        private readonly IMongoCollection<Comment> _comments; // Add this to fetch average ratings


        public IProductRepository(IMongoDatabase database)
        {
            _products = database.GetCollection<Product>("Products");
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
            return _products.Find(product => product.Id == id).FirstOrDefault();
        }

        public IEnumerable<Product> GetProductsByCategoryId(string categoryId)
        {
            return _products.Find(product => product.CategoryId == categoryId).ToList();
        }

        public void CreateProduct(Product product)
        {
            _products.InsertOne(product);
        }

        public void UpdateProduct(string id, Product product)
        {
            _products.ReplaceOne(existingProduct => existingProduct.Id == id, product);
        }

        public void DeleteProduct(string id)
        {
            _products.DeleteOne(product => product.Id == id);
        }

        //public void UpdateStock(string productId, int quantity)
        //{
        //    var product = GetProductById(productId);
        //    if (product != null)
        //    {
        //        product.AvailableStock += quantity;  // Update available stock
        //        product.StockLastUpdated = DateTime.UtcNow;  // Update last modified time

        //        UpdateProduct(productId, product);  // Save the updated product
        //    }
        //    else
        //    {
        //        throw new Exception($"Product with ID {productId} not found.");
        //    }
        //}

        //public void RemoveStock(string productId, int quantity)
        //{
        //    var product = GetProductById(productId);
        //    if (product != null)
        //    {
        //        if (product.AvailableStock >= quantity)
        //        {
        //            product.AvailableStock -= quantity;  // Remove the stock
        //            product.StockLastUpdated = DateTime.UtcNow;  // Update last modified time

        //            UpdateProduct(productId, product);  // Save the updated product
        //        }
        //        else
        //        {
        //            throw new Exception($"Not enough stock to remove for product ID {productId}. Available stock: {product.AvailableStock}");
        //        }
        //    }
        //    else
        //    {
        //        throw new Exception($"Product with ID {productId} not found.");
        //    }
        //}
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


    }
}
