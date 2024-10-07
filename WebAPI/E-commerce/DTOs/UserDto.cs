/*
 * File: UserDto.cs
 * Author: Sanjayan
 * Registration No: IT21375514
 * Description: This file defines the UserDto class, which represents the data transfer object for users in the e-commerce system.
 * It includes fields for user identification and user-related information.
 */

namespace E_commerce.DTOs
{
    public class UserDto
    {
        public string Id { get; set; } // Unique ID for the user
        public string Name { get; set; } // Name of the user
    }
}
