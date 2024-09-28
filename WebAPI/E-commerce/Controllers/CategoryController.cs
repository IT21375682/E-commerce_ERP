using E_commerce.Models;
using E_commerce.Services;
using Microsoft.AspNetCore.Mvc;
using System.Collections.Generic;
using System;

namespace E_commerce.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class CategoryController : ControllerBase
    {
        private readonly CategoryService _categoryService;

        public CategoryController(CategoryService categoryService)
        {
            _categoryService = categoryService;
        }

        // Get all categories
        [HttpGet]
        public ActionResult<IEnumerable<Category>> GetAllCategories()
        {
            var category = _categoryService.GetAllCategories();
            return Ok(category);
        }

        // Get category by id
        [HttpGet("{id}")]
        public ActionResult<Category> GetCategoryById(string id)
        {
            var category = _categoryService.GetCategoryById(id);
            if (category == null)
            {
                return NotFound();
            }
            return Ok(category);
        }

        // Create new category
        [HttpPost]
        public ActionResult<Category> CreateCategory([FromBody] Category newCategory)
        {
            try
            {
                _categoryService.CreateCategory(newCategory);
                return CreatedAtAction(nameof(GetCategoryById), new { id = newCategory.Id }, newCategory);
            }
            catch (Exception ex)
            {
                return StatusCode(500, "Internal server error: " + ex.Message);
            }
        }

        // Update existing category by id
        [HttpPut("{id}")]
        public IActionResult UpdateCategory(string id, [FromBody] Category updatedCategory)
        {
            var existingCategory = _categoryService.GetCategoryById(id);
            if (existingCategory == null)
            {
                return NotFound();
            }

            _categoryService.UpdateCategory(id, updatedCategory);
            return NoContent();
        }

        // Delete category by id
        [HttpDelete("{id}")]
        public IActionResult DeleteCategory(string id)
        {
            var existingCategory = _categoryService.GetCategoryById(id);
            if (existingCategory == null)
            {
                return NotFound();
            }

            _categoryService.DeleteCategory(id);
            return NoContent();
        }
    }
}
