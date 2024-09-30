using E_commerce.Models;
using E_commerce.Repositories;

namespace E_commerce.Services
{
    public class CategoryService
    {
            private readonly ICategoryRepository _categoryRepository; // Update this if you're using the interface

            public CategoryService(ICategoryRepository categoryRepository) // Update this if you're using the interface
            {
                 _categoryRepository = categoryRepository;
            }

            public IEnumerable<Category> GetAllCategories()
            {
                return _categoryRepository.GetAllCategories();
            }

        public IEnumerable<Category> GetAllActiveCategories()
        {
            return _categoryRepository.GetAllActiveCategories();
        }

        public Category GetCategoryById(string id)
            {
                return _categoryRepository.GetCategoryById(id);
            }

            public void CreateCategory(Category category)
            {
                _categoryRepository.CreateCategory(category);
            }

            public void UpdateCategory(string id, Category category)
            {
                _categoryRepository.UpdateCategory(id, category);
            }

            public void DeleteCategory(string id)
            {
                _categoryRepository.DeleteCategory(id);
            }
        }
}
