using E_commerce.DTOs;
using E_commerce.Models;
using E_commerce.Services;
using Microsoft.AspNetCore.Mvc;
using System.Collections.Generic;

namespace E_commerce.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ProductController : ControllerBase
    {
        private readonly ProductService _productService;

        public ProductController(ProductService productService)
        {
            _productService = productService;
        }

        [HttpGet]
        public ActionResult<IEnumerable<Product>> GetAllProducts()
        {
            var products = _productService.GetAllProducts();
            return Ok(products);
        }

        [HttpGet("{id}")]
        public ActionResult<Product> GetProductById(string id)
        {
            var product = _productService.GetProductById(id);
            if (product == null)
            {
                return NotFound();
            }
            return Ok(product);
        }

        [HttpGet("category/{categoryId}")]
        public ActionResult<IEnumerable<Product>> GetProductsByCategoryId(string categoryId)
        {
            var products = _productService.GetProductsByCategoryId(categoryId);

            if (products == null || !products.Any())
            {
                return NotFound("No products found for the given category.");
            }

            return Ok(products);
        }

        [HttpPost]
        public ActionResult<Product> CreateProduct([FromBody] Product newProduct)
        {
            try
            {
                _productService.CreateProduct(newProduct);
                return CreatedAtAction(nameof(GetProductById), new { id = newProduct.Id }, newProduct);
            }
            catch (Exception ex)
            {
                return StatusCode(500, "Internal server error: " + ex.Message);
            }
        }

        [HttpPut("{id}")]
        public IActionResult UpdateProduct(string id, [FromBody] Product updatedProduct)
        {
            var existingProduct = _productService.GetProductById(id);
            if (existingProduct == null)
            {
                return NotFound();
            }

            _productService.UpdateProduct(id, updatedProduct);
            return NoContent();
        }

        [HttpDelete("{id}")]
        public IActionResult DeleteProduct(string id)
        {
            var existingProduct = _productService.GetProductById(id);
            if (existingProduct == null)
            {
                return NotFound();
            }

            _productService.DeleteProduct(id);
            return NoContent();
        }

        //[HttpPost("{productId}/stock/update")]
        //public ActionResult UpdateStock(string productId, [FromBody] int quantity)
        //{
        //    _productService.UpdateStock(productId, quantity);
        //    return NoContent();
        //}

        //[HttpPost("{productId}/stock/remove")]
        //public ActionResult RemoveStock(string productId, [FromBody] int quantity)
        //{
        //    _productService.RemoveStock(productId, quantity);
        //    return NoContent();
        //}
        [HttpPut("{id}/stock")]
        public ActionResult UpdateStock(string id, [FromBody] int quantity)
        {
            var (isSuccess, message) = _productService.UpdateStock(id, quantity);
            if (isSuccess)
            {
                return Ok(message);
            }
            return BadRequest(message);
        }

        [HttpDelete("{id}/stock")]
        public ActionResult RemoveStock(string id, [FromBody] int quantity)
        {
            var (isSuccess, message) = _productService.RemoveStock(id, quantity);
            if (isSuccess)
            {
                return Ok(message);
            }
            return BadRequest(message);
        }

        // Get available stock by product ID
        [HttpGet("stock/{productId}")]
        public ActionResult<int> GetAvailableStockById(string productId)
        {
            try
            {
                var stock = _productService.GetProductStockById(productId);
                return Ok(stock);
            }
            catch (Exception ex)
            {
                return NotFound(ex.Message);
            }
        }

        // GET: api/product/active
        [HttpGet("active")]
        public ActionResult<IEnumerable<Product>> GetAllActiveProducts()
        {
            var products = _productService.GetAllActiveProducts();
            return Ok(products);
        }

        // GET: api/product/active/category/{categoryId}
        [HttpGet("active/category/{categoryId}")]
        public ActionResult<IEnumerable<Product>> GetAllActiveCategoryProducts(string categoryId)
        {
            var products = _productService.GetAllActiveCategoryProducts(categoryId);
            return Ok(products);
        }

        // GET: api/product/active-categories-products
        [HttpGet("active-categories-products")]
        public ActionResult<IEnumerable<(Category, IEnumerable<Product>)>> GetAllActiveCategoryAndActiveProducts()
        {
            var result = _productService.GetAllActiveCategoryAndActiveProducts();
            return Ok(result);
        }

        [HttpGet("active-details")]
        public ActionResult<IEnumerable<ProductDetailsDto>> GetAllActiveProductsWithDetails()
        {
            var products = _productService.GetAllActiveProductsWithDetails();
            return Ok(products);
        }

        [HttpGet("active-details/{productId}")]
        public ActionResult<ProductDetailsDto> GetActiveProductWithDetailsById(string productId)
        {
            var product = _productService.GetActiveProductWithDetailsById(productId);

            if (product == null)
            {
                return NotFound($"Product with Id {productId} not found or is inactive.");
            }

            return Ok(product);
        }

        [HttpGet("active-details/category/{categoryId}")]
        public ActionResult<IEnumerable<ProductDetailsDto>> GetAllActiveProductsWithDetailsByCategory(string? categoryId = null)
        {
            var products = _productService.GetActiveProductsWithDetailsByCategory(categoryId);
            return Ok(products);
        }


    }
}
