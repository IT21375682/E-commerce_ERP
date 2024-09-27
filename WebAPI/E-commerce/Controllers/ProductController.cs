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
    }
}
