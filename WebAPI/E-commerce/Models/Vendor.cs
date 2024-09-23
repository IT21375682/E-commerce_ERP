namespace E_commerce.Models
{
    public class Vendor
    {
        public string VendorId { get; set; }  // Unique vendor ID
        public string Name { get; set; }
        public string Email { get; set; }
        public float AverageRating { get; set; }  // Average customer rating for the vendor
        public List<string> Comments { get; set; }  // Customer comments on the vendor
    }
}
