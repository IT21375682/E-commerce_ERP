/*
 * File: Comment.cs
 * Author: Sanjayan
 * Description: This file defines the Comment class, which models a user comment for a product/vendor in the e-commerce system.
 * It includes fields for user, vendor, product, comment details, and a rating system.
 */

using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;
using System;

namespace E_commerce.Models
{
    public class Comment
    {
        [BsonId] // MongoDB auto-generated ID
        [BsonRepresentation(BsonType.ObjectId)]
        public string? id { get; set; }   // Unique ID for the comment

        public string userId { get; set; }   // ID of the user who posted the comment
        public string vendorId { get; set; } // ID of the vendor being commented on
        public string productId { get; set; }    // ID of the product being commented on
        public DateTime date { get; set; } = DateTime.Now; // Date of the comment
        public int rating { get; set; } = 0;  // Rating for the product/vendor
        public string commentText { get; set; } = "";  // Actual comment text
    }
}
