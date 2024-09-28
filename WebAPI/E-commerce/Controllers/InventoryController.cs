//using E_commerce.Models;
//using E_commerce.Services;
//using Microsoft.AspNetCore.Mvc;

//namespace E_commerce.Controllers
//{
//    [Route("api/[controller]")]
//    [ApiController]
//    public class InventoryController : ControllerBase
//    {
//        private readonly InventoryService _inventoryService;

//        public InventoryController(InventoryService inventoryService)
//        {
//            _inventoryService = inventoryService;
//        }

//        // Get inventory by product ID
//        [HttpGet("{productId}")]
//        public ActionResult<Inventory> GetInventoryByProductId(string productId)
//        {
//            var inventory = _inventoryService.GetInventoryByProductId(productId);
//            if (inventory == null)
//            {
//                return NotFound();
//            }
//            return Ok(inventory);
//        }

//        // Update stock (subtract stock)
//        [HttpPut("update/{productId}")]
//        public IActionResult UpdateStock(string productId, [FromBody] int quantity)
//        {
//            try
//            {
//                _inventoryService.UpdateStock(productId, quantity);
//                return NoContent();
//            }
//            catch (Exception ex)
//            {
//                return BadRequest(ex.Message);
//            }
//        }

//        // Add stock
//        [HttpPut("add/{productId}")]
//        public IActionResult AddStock(string productId, [FromBody] int quantity)
//        {
//            try
//            {
//                _inventoryService.AddStock(productId, quantity);
//                return NoContent();
//            }
//            catch (Exception ex)
//            {
//                return BadRequest(ex.Message);
//            }
//        }
//    }
//}
