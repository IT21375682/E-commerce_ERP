using E_commerce.Models;
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


        public IProductRepository(IMongoDatabase database)
        {
            _products = database.GetCollection<Product>("Products");
            _categories = database.GetCollection<Category>("Categories");

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

    }
}
