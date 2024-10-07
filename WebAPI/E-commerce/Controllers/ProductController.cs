/*
 * File: ProductController.cs
 * Author: Krithiga D. B
 * Description: This controller handles API requests related to products. 
 * It provides endpoints to fetch, create, update, and delete products, toggle status of products and handle products inventory.
 */
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


        // GET all Products api/Product
        [HttpGet]
        public ActionResult<IEnumerable<Product>> GetAllProducts()
        {
            var products = _productService.GetAllProducts();
            return Ok(products);
        }

        // GET a single product per Id api/Product/{id}
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

        // GET all products per categoryId api/Product/category/{categoryId}
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

        // GET all products per vendorId api/Product/vendor/{vendorId}
        [HttpGet("vendor/{vendorId}")]
        public ActionResult<IEnumerable<Product>> GetProductsByVendorId(string vendorId)
        {
            var products = _productService.GetProductsByVendorId(vendorId);

            if (products == null || !products.Any())
            {
                return NotFound("No products found for the given vendor.");
            }

            return Ok(products);
        }

        // Create product api/Product
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

        // Update product per productId api/Product/{id}
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

        // DELETE a product per productId api/Product/vendor/{vendorId}
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

        // Update or ADD inventory stock api/Product/{id}/stock
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

        // Remove or reduce inventory stock api/Product/{id}/stock
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

        // GET active products api/product/active
        [HttpGet("active")]
        public ActionResult<IEnumerable<Product>> GetAllActiveProducts()
        {
            var products = _productService.GetAllActiveProducts();
            return Ok(products);
        }

        // GET inactive products api/product/deactivated
        [HttpGet("deactivated")]
        public ActionResult<IEnumerable<Product>> GetAllDeActivatedProducts()
        {
            var products = _productService.GetAllDeActivatedProducts();
            return Ok(products);
        }

        // GET active category products api/product/active/category/{categoryId}
        [HttpGet("active/category/{categoryId}")]
        public ActionResult<IEnumerable<Product>> GetAllActiveCategoryProducts(string categoryId)
        {
            var products = _productService.GetAllActiveCategoryProducts(categoryId);
            return Ok(products);
        }

        // GET All Active Category And Active Products api/product/active-categories-products
        [HttpGet("active-categories-products")]
        public ActionResult<IEnumerable<(Category, IEnumerable<Product>)>> GetAllActiveCategoryAndActiveProducts()
        {
            var result = _productService.GetAllActiveCategoryAndActiveProducts();
            return Ok(result);
        }

        //Get All Active Products With Details
        [HttpGet("active-details")]
        public ActionResult<IEnumerable<ProductDetailsDto>> GetAllActiveProductsWithDetails()
        {
            var products = _productService.GetAllActiveProductsWithDetails();
            return Ok(products);
        }

        //Geta single Active Product Details
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

        //Geta Active Product Details by categoryId
        [HttpGet("active-details/category/{categoryId}")]
        public ActionResult<IEnumerable<ProductDetailsDto>> GetAllActiveProductsWithDetailsByCategory(string? categoryId = null)
        {
            var products = _productService.GetActiveProductsWithDetailsByCategory(categoryId);
            return Ok(products);
        }

        //update Product status
        [HttpPatch("{id}/toggle-active")]
        public async Task<IActionResult> ToggleIsActive(string id)
        {
            await _productService.ToggleIsActiveAsync(id);
            return NoContent(); // 204 No Content response after successful update
        }


    }
}
