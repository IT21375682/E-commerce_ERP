//using E_commerce.Models;
//using MongoDB.Driver;

//namespace E_commerce.Repositories
//{
//    public class InventoryRepository
//    {
//        private readonly IMongoCollection<Inventory> _inventory;

//        public InventoryRepository(IMongoDatabase database)
//        {
//            _inventory = database.GetCollection<Inventory>("Inventory");
//        }

//        public Inventory GetInventoryByProductId(string productId)
//        {
//            return _inventory.Find(inv => inv.ProductId == productId).FirstOrDefault();
//        }

//        public void UpdateStock(string productId, int quantity)
//        {
//            var inventory = GetInventoryByProductId(productId);
//            if (inventory != null)
//            {
//                inventory.AvailableStock -= quantity;
//                _inventory.ReplaceOne(inv => inv.ProductId == productId, inventory);
//            }
//        }

//        public void AddStock(string productId, int quantity)
//        {
//            var inventory = GetInventoryByProductId(productId);
//            if (inventory != null)
//            {
//                inventory.AvailableStock += quantity;
//                _inventory.ReplaceOne(inv => inv.ProductId == productId, inventory);
//            }
//        }
//    }

//}
