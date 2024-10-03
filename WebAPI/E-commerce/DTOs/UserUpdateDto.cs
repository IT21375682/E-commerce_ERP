using E_commerce.Models;

namespace E_commerce.DTOs
{
    public class UserUpdateDto
    {
        public string? Name { get; set; }
        public string? Phone { get; set; }
        public Address? Address { get; set; }
    }
}
