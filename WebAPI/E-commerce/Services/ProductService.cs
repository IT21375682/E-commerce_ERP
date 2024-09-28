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

        public IEnumerable<Product> GetAllProducts()
        {
            return _productRepository.GetAllProducts();
        }

        public Product GetProductById(string id)
        {
            return _productRepository.GetProductById(id);
        }

        public IEnumerable<Product> GetProductsByCategoryId(string categoryId)
        {
            return _productRepository.GetProductsByCategoryId(categoryId);
        }

        public void CreateProduct(Product product)
        {
            _productRepository.CreateProduct(product);
        }

        public void UpdateProduct(string id, Product product)
        {
            _productRepository.UpdateProduct(id, product);
        }

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
        public (bool isSuccess, string message) UpdateStock(string productId, int quantity)
        {
            return _productRepository.UpdateStock(productId, quantity);
        }

        public (bool isSuccess, string message) RemoveStock(string productId, int quantity)
        {
            return _productRepository.RemoveStock(productId, quantity);
        }

    }
}
