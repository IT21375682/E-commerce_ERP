/*
 * File: CommentRepository.cs
 * Author: Sanjayan
 * Description: This file implements the repository pattern for managing CRUD operations on comments in MongoDB.
 */

using E_commerce.Models;
using MongoDB.Bson;
using MongoDB.Driver;
using System.Collections.Generic;
using System.Xml.Linq;

namespace E_commerce.Repositories
{
    public class ICommentRepository
    {
        private readonly IMongoCollection<Comment> _comments;

        // Initializes the CommentRepository with the MongoDB context.
        public ICommentRepository(IMongoDatabase database)
        {
            _comments = database.GetCollection<Comment>("Comments");
        }

        // Retrieves all comments.
        public List<Comment> GetAllComments()
        {
            return _comments.Find(comment => true).ToList(); // Retrieves all comments
        }

        // Retrieves a comment by its ID.
        public Comment GetCommentById(string id)
        {
            return _comments.Find(comment => comment.id == id).FirstOrDefault();
        }

        // Adds a new comment to the database.
        public void AddComment(Comment comment)
        {
            _comments.InsertOne(comment);
        }

        // Retrieves all comments by a specific user ID.
        public List<Comment> GetCommentsByUserId(string userId)
        {
            return _comments.Find(comment => comment.userId == userId).ToList();
        }

        // Retrieves comments by a specific vendor ID.
        public List<Comment> GetCommentsByVendorId(string vendorId)
        {
            return _comments.Find(comment => comment.vendorId == vendorId).ToList();
        }

        // Retrieves comments by a specific product ID.
        public List<Comment> GetCommentsByProductId(string productId)
        {
            return _comments.Find(comment => comment.productId == productId).ToList();
        }

        // Updates an existing comment by its ID.
        public void UpdateComment(string id, Comment updatedComment)
        {
            _comments.ReplaceOne(comment => comment.id == id, updatedComment);
        }

        // Deletes a comment by its ID.
        public void DeleteComment(string id)
        {
            _comments.DeleteOne(comment => comment.id == id);
        }

        // Retrieves the average rating for a specific vendor
        public double GetAverageRatingByVendorId(string vendorId)
        {
            return _comments.AsQueryable()
                            .Where(comment => comment.vendorId == vendorId)
                            .Average(comment => comment.rating);
        }

        // Retrieves the average rating for a specific product
        public double GetAverageRatingByProductId(string productId)
        {
            return _comments.AsQueryable()
                            .Where(comment => comment.productId == productId)
                            .Average(comment => comment.rating);
        }

        // Adds or updates a comment in the database based on userId, vendorId, and productId.
        public void AddOrUpdateComment(Comment comment)
        {
            var existingComment = _comments.Find(c => c.userId == comment.userId
                                                      && c.userId == comment.userId
                                                      && c.productId == comment.productId)
                                             .FirstOrDefault();

            if (existingComment != null)
            {
                comment.id = existingComment.id; // Set the existing ID to update
                _comments.ReplaceOne(c => c.id == existingComment.id, comment);
            }
            else
            {
                // Set default values if not passed
                comment.rating = comment.rating != 0 ? comment.rating : 0;
                comment.commentText = string.IsNullOrEmpty(comment.commentText) ? "" : comment.commentText;
                _comments.InsertOne(comment);
            }
        }
    }
}
