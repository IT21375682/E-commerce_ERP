
/*
 * File: Category.cs
 * Author: Krithiga. D. B
 * Description: This file contains the definition of the Category class, which models a category of product in the e-commerce system.
 * Each category has name, status
 */
using MongoDB.Bson.Serialization.Attributes;
using MongoDB.Bson;

namespace E_commerce.Models
{
    public class Category
    {
        [BsonId]
        [BsonRepresentation(BsonType.ObjectId)]
        public string? Id { get; set; }  // Unique Category ID
        public string CategoryName { get; set; }
        public bool IsActive { get; set; }  // Whether the Category is active or deactivated
        public DateTime? CreatedAt { get; set; }
        public DateTime? UpdatedAt { get; set; }

    }
}
