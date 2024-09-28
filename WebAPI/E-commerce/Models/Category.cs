namespace E_commerce.Models
{
    public class Category
    {
        public string Id { get; set; }  // Unique Category ID
        public string CategoryName { get; set; }
        public bool IsActive { get; set; }  // Whether the Category is active or deactivated
        public DateTime CreatedAt { get; set; }
        public DateTime UpdatedAt { get; set; }

    }
}
