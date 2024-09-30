using E_commerce.Models;
using E_commerce.Services;
using MongoDB.Bson;
using MongoDB.Driver;
using System;
using System.Collections.Generic;
using System.Linq;

    namespace E_commerce.Repositories
{
    public class IProductRepository
    {
        private readonly IMongoCollection<Product> _products;
        private readonly IMongoCollection<Category> _categories;
        private readonly IMongoCollection<User> _users;  // Collection for users (vendors)
        private readonly EmailService _emailService;

        public IProductRepository(IMongoDatabase database, EmailService emailService)
        {
            _products = database.GetCollection<Product>("Products");
            _categories = database.GetCollection<Category>("Categories");
            _users = database.GetCollection<User>("Users");  // Initialize the users collection
            _emailService = emailService;

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
        //public (bool isSuccess, string message) UpdateStock(string productId, int quantity)
        //{
        //    var product = GetProductById(productId);
        //    if (product != null)
        //    {
        //        product.AvailableStock += quantity;  // Update available stock
        //        product.StockLastUpdated = DateTime.UtcNow;  // Update last modified time

        //        UpdateProduct(productId, product);  // Save the updated product

        //        // Check for low stock alert
        //        if (product.AvailableStock < 50)
        //        {
        //            return (true, $"Alert: Stock for product '{product.Name}' is below 50. Current stock: {product.AvailableStock}");
        //        }

        //        return (true, "Stock updated successfully.");
        //    }
        //    else
        //    {
        //        throw new Exception($"Product with ID {productId} not found.");
        //    }
        //}

        //public (bool isSuccess, string message) RemoveStock(string productId, int quantity)
        //{
        //    var product = GetProductById(productId);
        //    if (product != null)
        //    {
        //        if (product.AvailableStock >= quantity)
        //        {
        //            product.AvailableStock -= quantity;  // Remove the stock
        //            product.StockLastUpdated = DateTime.UtcNow;  // Update last modified time

        //            UpdateProduct(productId, product);  // Save the updated product

        //            // Check for low stock alert
        //            if (product.AvailableStock < 50)
        //            {
        //                return (true, $"Alert: Stock for product '{product.Name}' is below 50. Current stock: {product.AvailableStock}");
        //            }

        //            return (true, "Stock removed successfully.");
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
    }
}
