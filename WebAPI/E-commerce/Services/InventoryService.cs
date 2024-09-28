//using E_commerce.Models;
//using E_commerce.Repositories;
//using System;

//namespace E_commerce.Services
//{
//    public class InventoryService
//    {
//        private readonly InventoryRepository _inventoryRepository;

//        public InventoryService(InventoryRepository inventoryRepository)
//        {
//            _inventoryRepository = inventoryRepository;
//        }

//        public Inventory GetInventoryByProductId(string productId)
//        {
//            return _inventoryRepository.GetInventoryByProductId(productId);
//        }

//        public void UpdateStock(string productId, int quantity)
//        {
//            var inventory = _inventoryRepository.GetInventoryByProductId(productId);
//            if (inventory == null)
//                throw new Exception("Inventory not found");

//            if (inventory.AvailableStock < quantity)
//                throw new Exception("Not enough stock available");

//            _inventoryRepository.UpdateStock(productId, quantity);

//            // Check for low stock and send alert if needed
//            if (inventory.AvailableStock <= 10)  // Example threshold
//            {
//                SendLowStockAlert(productId);
//            }
//        }

//        public void AddStock(string productId, int quantity)
//        {
//            var inventory = _inventoryRepository.GetInventoryByProductId(productId);
//            if (inventory == null)
//                throw new Exception("Inventory not found");

//            _inventoryRepository.AddStock(productId, quantity);
//        }

//        private void SendLowStockAlert(string productId)
//        {
//            // Logic to notify vendor of low stock
//            Console.WriteLine($"Alert: Product {productId} is low on stock.");
//        }
//    }
//}
