using E_commerce.Models;
using MongoDB.Driver;
using System.Collections.Generic;

namespace E_commerce.Repositories
{
    public class IProductRepository
    {
        private readonly IMongoCollection<Product> _products;

        public IProductRepository(IMongoDatabase database)
        {
            _products = database.GetCollection<Product>("Products");
        }

        public IEnumerable<Product> GetAllProducts()
        {
            return _products.Find(product => true).ToList();
        }

        public Product GetProductById(string id)
        {
            return _products.Find(product => product.Id == id).FirstOrDefault();
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
    }
}

