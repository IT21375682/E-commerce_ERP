namespace E_commerce.Models
{
    public class User
    {
        public string Id { get; set; }  // Unique identifier (could be email)
        public string Name { get; set; }
        public string Email { get; set; }
        public string Role { get; set; } // Administrator, Vendor, CSR, Customer
        public bool IsActive { get; set; } // To track if the user is active or deactivated
        public DateTime CreatedAt { get; set; }
    }

}
