using E_commerce.DTOs;
using E_commerce.Models;
using E_commerce.Repositories;
using MongoDB.Driver;

namespace E_commerce.Services
{
    public class CategoryService
    {
            private readonly ICategoryRepository _categoryRepository; // Update this if you're using the interface
            private readonly IProductRepository _productRepository;

        public CategoryService(ICategoryRepository categoryRepository, IProductRepository productRepository) // Update this if you're using the interface
            {
                 _categoryRepository = categoryRepository;
                 _productRepository= productRepository;
            }

            public IEnumerable<Category> GetAllCategories()
            {
                return _categoryRepository.GetAllCategories();
            }

        public IEnumerable<Category> GetAllActiveCategories()
        {
            return _categoryRepository.GetAllActiveCategories();
        }

        public IEnumerable<Category> GetAllDeactivatedCategories()
        {
            return _categoryRepository.GetAllDeactivatedCategories();
        }

        public  Task<Category> GetCategoryById(string id)
        {
            return  _categoryRepository.GetCategoryById(id); // Call the async method
        }

        public void CreateCategory(Category category)
            {
                _categoryRepository.CreateCategory(category);
            }

            public void UpdateCategory(string id, Category category)
            {
                _categoryRepository.UpdateCategory(id, category);
            }

            //public void DeleteCategory(string id)
            //{
            //    _categoryRepository.DeleteCategory(id);
            //}

            public IEnumerable<CategoryDto> GetAllActiveCategoriesNames()
            {
                return _categoryRepository.GetAllActiveCategoriesNames();
            }

        public async Task ToggleCategoryIsActiveAsync(string categoryId)
        {
            var filter = Builders<Category>.Filter.Eq(c => c.Id, categoryId);

            // Fetch the current category asynchronously
            var category = await _categoryRepository.GetCategoryById(categoryId);

            if (category != null)
            {
                // Toggle the IsActive value for the category
                var newIsActiveValue = !category.IsActive;

                // Update the category with the new IsActive value
                var updateCategory = Builders<Category>.Update
                    .Set(c => c.IsActive, newIsActiveValue);

                await _categoryRepository.UpdateCategoryAsync(filter, updateCategory); // Use the async update method

                // If the category is being deactivated (changing from true to false)
                if (!newIsActiveValue)
                {
                    // Deactivate all active products under this category
                    await DeactivateProductsByCategoryAsync(categoryId);
                }
            }
        }


        // Method to deactivate products under the category
        private async Task DeactivateProductsByCategoryAsync(string categoryId)
        {
            var productFilter = Builders<Product>.Filter.Eq(p => p.CategoryId, categoryId) &
                                Builders<Product>.Filter.Eq(p => p.IsActive, true); // Only target active products

            var updateProducts = Builders<Product>.Update
                .Set(p => p.IsActive, false) // Deactivate all products under the category
                .CurrentDate(p => p.StockLastUpdated); // Update the last modified date

            // Update all products under this category
            await _productRepository.UpdateManyProductsAsync(productFilter, updateProducts); // Call a method to update products
        }




        public async Task CheckAndDelete(string categoryId)
        {
            var filter = Builders<Category>.Filter.Eq(c => c.Id, categoryId);

            // Fetch the current category asynchronously
            var category = await _categoryRepository.GetCategoryById(categoryId);

            if (category != null)
            {
                // If the category is being deactivated (changing from true to false)
                if (category.IsActive)
                {
                    throw new InvalidOperationException("Cannot delete category with active status.");


                    //// Build the filter to count active products under this category
                    //var productFilter = Builders<Product>.Filter.Eq(p => p.CategoryId, categoryId) & Builders<Product>.Filter.Eq(p => p.IsActive, true);
                    //var activeProductsCount = await _productRepository.CountAsync(productFilter);

                    //if (activeProductsCount > 0)
                    //{
                    //    // Throw an exception or return a custom result indicating there are active products
                    //    throw new InvalidOperationException("Cannot delete category with active products.");
                    //}
                }

                // Toggle the IsActive value for the category
                //var newIsActiveValue = !category.IsActive;

                // Update the category with the new IsActive value
                //var updateCategory = Builders<Category>.Update
                //    .Set(c => c.IsActive, newIsActiveValue);

                _categoryRepository.DeleteCategory(categoryId);// Use the async update method

                //// Optionally deactivate all products under this category if deactivated
                //if (!newIsActiveValue)
                //{
                //    await DeactivateProductsByCategoryAsync(categoryId);
                //}
            }
        }

        }
        
}
