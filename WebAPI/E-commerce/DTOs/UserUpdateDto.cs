/*
 * File: UserUpdateDto.cs
 * Author: Sanjayan
 * Registration No: IT21375514
 * Description: This file defines the UserUpdateDto class, which represents the data transfer object 
 * for updating user information in the e-commerce system. It includes fields for the user's name, 
 * phone number, and address details.
 */

using E_commerce.Models;

namespace E_commerce.DTOs
{
    public class UserUpdateDto
    {
        public string? Name { get; set; }         // Name of the user (nullable)
        public string? Phone { get; set; }        // Phone number of the user (nullable)
        public Address? Address { get; set; }     // Address details of the user (nullable)
    }
}
