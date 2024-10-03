using MongoDB.Bson.Serialization.Attributes;
using MongoDB.Bson;
using System;

namespace E_commerce.Models
{
    public class User
    {
        [BsonId] // Marking this as the primary key
        [BsonRepresentation(BsonType.ObjectId)] // Allows MongoDB to handle it as ObjectId while using string in C#
        public string? Id { get; set; }
        public string Name { get; set; }  // First name of the user
        public string Email { get; set; }   // Email for login and contact
        public string Password { get; set; }   // User password (make sure to store it securely, hashed)
        public string ?Phone { get; set; }   // Contact number
        public Address ?Address { get; set; }  // Embedded Address class for user address details
        public string Role { get; set; }  // Administrator, Vendor, CSR, Customer
        public bool IsActive { get; set; }  // To track if the user is active or deactivated
        public bool IsNew { get; set; }
        public DateTime CreatedAt { get; set; }  // Date of account creation

    }

    public class Address
    {
        public string ?Street { get; set; }
        public string ?City { get; set; }
        public string ?PostalCode { get; set; }
        public string ?Country { get; set; }
    }
}
