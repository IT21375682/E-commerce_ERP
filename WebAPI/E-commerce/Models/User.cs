/*
 * File: User.cs
 * Author: ShanJeep.J   IT21375682    
 * Description: This file defines the User class, which models a user details for a vendor/Admin/Customer/CSR in the e-commerce system.
 * It includes fields for user demographic info and Role.
 */

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
        public string Name { get; set; }  //  name of the user
        public string Email { get; set; }   // Email for login and contact
        public string Password { get; set; }   // User password 
        public string ?Phone { get; set; }   // Contact number
        public Address ?Address { get; set; }  // Embedded Address class for user address details
        public string Role { get; set; }  // ADMIN, VENDOR, CSR, CUSTOMER
        public bool IsActive { get; set; }  // To track if the user is active or 
        public DateTime CreatedAt { get; set; }  // Date of account creation
        public bool? IsNew { get; set; } = true; //
    }

    public class Address
    {
        public string ?Street { get; set; }
        public string ?City { get; set; }
        public string ?PostalCode { get; set; }
        public string ?Country { get; set; }
    }

    public class ToggleStatusRequest
    {
        public bool IsActive { get; set; }
        public bool IsNew { get; set; }

    }
}
