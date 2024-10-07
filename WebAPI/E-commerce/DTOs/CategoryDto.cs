/*
 * File: CategoryDto.cs
 * Author: Sanjayan
 * Registration No: IT21375514
 * Description: This file defines the CategoryDto class, which represents the data transfer object for categories in the e-commerce system.
 * It includes fields for category identification and name.
 */

namespace E_commerce.DTOs
{
    public class CategoryDto
    {
        public string Id { get; set; }               // Unique ID for the category
        public string CategoryName { get; set; }      // Name of the category
    }
}
