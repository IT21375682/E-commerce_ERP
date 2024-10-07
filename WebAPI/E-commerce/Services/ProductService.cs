/*
 * File: ProductService.cs
 * Author: Krithiga D. B
 * Description: This service handles the business logic for products operations such as fetching, creating, updating, and deleting products.
 */
using E_commerce.DTOs;
using E_commerce.Models;
using E_commerce.Repositories;
using System.Collections.Generic;

namespace E_commerce.Services
{
    public class ProductService
    {
        private readonly IProductRepository _productRepository; // Update this if you're using the interface

        public ProductService(IProductRepository productRepository) // Update this if you're using the interface
        {
            _productRepository = productRepository;
        }

        // Fetches all products.
        public IEnumerable<Product> GetAllProducts()
        {
            return _productRepository.GetAllProducts();
        }

        // Fetches a product by its ID.
        public Product GetProductById(string id)
        {
            return _productRepository.GetProductById(id);
        }

        // Fetches a product by its categoryId.
        public IEnumerable<Product> GetProductsByCategoryId(string categoryId)
        {
            return _productRepository.GetProductsByCategoryId(categoryId);
        }

        // Fetches a product by its vendorId.
        public IEnumerable<Product> GetProductsByVendorId(string vendorId)
        {
            return _productRepository.GetProductsByVendorId(vendorId);
        }

        //Creates a product
        public void CreateProduct(Product product)
        {
            _productRepository.CreateProduct(product);
        }

        //Updates a product by id
        public void UpdateProduct(string id, Product product)
        {
            _productRepository.UpdateProduct(id, product);
        }

        //Delete a product by id
        public void DeleteProduct(string id)
        {
            _productRepository.DeleteProduct(id);
        }
        //public void UpdateStock(string productId, int quantity)
        //{
        //    _productRepository.UpdateStock(productId, quantity);
        //}

        //public void RemoveStock(string productId, int quantity)
        //{
        //    _productRepository.RemoveStock(productId, quantity);
        //}

        //Update or Add stock by id & quantity
        public (bool isSuccess, string message) UpdateStock(string productId, int quantity)
        {
            return _productRepository.UpdateStock(productId, quantity);
        }

        //remove or reduce stock by id & quantity
        public (bool isSuccess, string message) RemoveStock(string productId, int quantity)
        {
            return _productRepository.RemoveStock(productId, quantity);
        }

        //Get product by id & display stock
        public int GetProductStockById(string productId)
        {
            return _productRepository.GetAvailableStockById(productId);
        }

        //Get all active products
        public IEnumerable<Product> GetAllActiveProducts()
        {
            return _productRepository.GetAllActiveProducts();
        }

        //Get all DeActivated products
        public IEnumerable<Product> GetAllDeActivatedProducts()
        {
            return _productRepository.GetAllDeActivatedProducts();
        }

        //Get all Active category products
        public IEnumerable<Product> GetAllActiveCategoryProducts(string categoryId)
        {
            return _productRepository.GetAllActiveCategoryProducts(categoryId);
        }

        //Get all Active category & active products
        public IEnumerable<(Category, IEnumerable<Product>)> GetAllActiveCategoryAndActiveProducts()
        {
            return _productRepository.GetAllActiveCategoryAndActiveProducts();
        }

        //Get all Active products details
        public IEnumerable<ProductDetailsDto> GetAllActiveProductsWithDetails()
        {
            return _productRepository.GetAllActiveProductsWithDetails();
        }

        //Get Active product details
        public ProductDetailsDto GetActiveProductWithDetailsById(string productId)
        {
            return _productRepository.GetActiveProductWithDetailsById(productId);
        }

        //Get Active product with details by category
        public IEnumerable<ProductDetailsDto> GetActiveProductsWithDetailsByCategory(string? categoryId = null)
        {
            return _productRepository.GetActiveProductsWithDetailsByCategory(categoryId);
        }

        //Update Active or Inactive status
        public async Task ToggleIsActiveAsync(string productId)
        {
            await _productRepository.ToggleIsActiveAsync(productId);
        }

    }
}
