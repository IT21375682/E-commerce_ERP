/*
 * File: CommentDto.cs
 * Author: Sanjayan
 * Registration No: IT21375514
 * Description: This file defines the CommentDto class, which represents the data transfer object for comments in the e-commerce system.
 * It includes fields for user, vendor, product details, and comment information.
 */

using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;
using System;

namespace E_commerce.DTOs
{
    public class CommentDto
    {
        public string id { get; set; }      // Unique ID for the comment
        public string userId { get; set; }   // ID of the user who posted the comment
        public string username { get; set; }  // Username of the user who posted the comment
        public string vendorId { get; set; }  // ID of the vendor being commented on
        public string vendorName { get; set; } // Name of the vendor
        public string productId { get; set; }  // ID of the product being commented on
        public string productName { get; set; } // Name of the product
        public DateTime date { get; set; }     // Date of the comment
        public int rating { get; set; }        // Rating for the product/vendor
        public string commentText { get; set; } // Actual comment text
    }
}
