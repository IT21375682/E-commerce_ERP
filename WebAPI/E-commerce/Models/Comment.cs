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
        public ObjectId CommentId { get; set; }   // Unique ID for the comment

        public string UserId { get; set; }   // ID of the user who posted the comment
        public string VendorId { get; set; } // ID of the vendor being commented on
        public string ProductId { get; set; }    // ID of the product being commented on
        public DateTime Date { get; set; } = DateTime.Now; // Date of the comment
        public int Rating { get; set; }  // Rating for the product/vendor
        public string CommentText { get; set; }  // Actual comment text
    }
}
