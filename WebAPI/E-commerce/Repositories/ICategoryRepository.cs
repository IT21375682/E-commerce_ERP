using E_commerce.DTOs;
using E_commerce.Models;
using MongoDB.Driver;

namespace E_commerce.Repositories
{
    public class ICategoryRepository
    {
            private readonly IMongoCollection<Category> _category;
            private readonly IMongoCollection<Product> _product;

            public ICategoryRepository(IMongoDatabase database)
            {
                _category = database.GetCollection<Category>("Category");

            _product = database.GetCollection<Product>("Product");
        }

            public IEnumerable<Category> GetAllCategories()
            {
                return _category.Find(category => true).ToList();
            }

            public IEnumerable<Category> GetAllActiveCategories()
            {
                return _category.Find(category => category.IsActive == true).ToList();
            }

        public IEnumerable<Category> GetAllDeactivatedCategories()
        {
            return _category.Find(category => category.IsActive == false).ToList();
        }

        public Task<Category> GetCategoryById(string id)
        {
            return  _category.Find(c => c.Id == id).FirstOrDefaultAsync();
        }

        public async Task UpdateCategoryAsync(FilterDefinition<Category> filter, UpdateDefinition<Category> update)
        {
            await _category.UpdateOneAsync(filter, update);
        }
        public void CreateCategory(Category category)
            {
                 _category.InsertOne(category);
            }

            public void UpdateCategory(string id, Category category)
            {
                _category.ReplaceOne(existingCategory => existingCategory.Id == id, category);
            }

            public void DeleteCategory(string id)
            {
                 _category.DeleteOne(category => category.Id == id);
            }

            public IEnumerable<CategoryDto> GetAllActiveCategoriesNames()
            {
                // Using projection to map to DTO
                return _category.Find(category => category.IsActive)
                                .Project(category => new CategoryDto
                                {
                                    Id = category.Id,
                                    CategoryName = category.CategoryName
                                }).ToList();
            }

        public async Task ToggleCategoryIsActiveAsync(string categoryId)
        {
            var filter = Builders<Category>.Filter.Eq(c => c.Id, categoryId);

            // Fetch the current category
            var category = await _category.Find(filter).FirstOrDefaultAsync();

            if (category != null)
            {
                // Toggle the IsActive value for the category
                var newIsActiveValue = !category.IsActive;

                // Update the category with the new IsActive value
                var updateCategory = Builders<Category>.Update
                    .Set(c => c.IsActive, newIsActiveValue);

                await _category.UpdateOneAsync(filter, updateCategory);

                // If the category is being deactivated (changing from true to false)
                if (!newIsActiveValue)
                {
                    // Find and update all active products under this category
                    var productFilter = Builders<Product>.Filter.Eq(p => p.CategoryId, categoryId) &
                                        Builders<Product>.Filter.Eq(p => p.IsActive, true); // Only target active products

                    var updateProducts = Builders<Product>.Update
                        .Set(p => p.IsActive, false) // Deactivate all products under the category
                        .CurrentDate(p => p.StockLastUpdated); // Update the last modified date

                    // Update all products under this category
                    await _product.UpdateManyAsync(productFilter, updateProducts);
                }
            }
        }

        public async Task<long> CountAsync(FilterDefinition<Product> filter)
        {
            return await _product.CountDocumentsAsync(filter);
        }






    }
}
