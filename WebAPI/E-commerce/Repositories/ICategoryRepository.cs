using E_commerce.Models;
using MongoDB.Driver;

namespace E_commerce.Repositories
{
    public class ICategoryRepository
    {
            private readonly IMongoCollection<Category> _category;

            public ICategoryRepository(IMongoDatabase database)
            {
                _category = database.GetCollection<Category>("Category");
            }

            public IEnumerable<Category> GetAllCategories()
            {
                return _category.Find(category => true).ToList();
            }

            public Category GetCategoryById(string id)
            {
                return _category.Find(category => category.Id == id).FirstOrDefault();
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
        }
}
