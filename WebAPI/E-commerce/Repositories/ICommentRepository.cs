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
            var objectId = new ObjectId(id);
            return _comments.Find(comment => comment.CommentId == objectId).FirstOrDefault();
        }

        // Adds a new comment to the database.
        public void AddComment(Comment comment)
        {
            _comments.InsertOne(comment);
        }

        // Retrieves all comments by a specific user ID.
        public List<Comment> GetCommentsByUserId(string userId)
        {
            return _comments.Find(comment => comment.UserId == userId).ToList();
        }

        // Retrieves comments by a specific vendor ID.
        public List<Comment> GetCommentsByVendorId(string vendorId)
        {
            return _comments.Find(comment => comment.VendorId == vendorId).ToList();
        }

        // Retrieves comments by a specific product ID.
        public List<Comment> GetCommentsByProductId(string productId)
        {
            return _comments.Find(comment => comment.ProductId == productId).ToList();
        }

        // Updates an existing comment by its ID.
        public void UpdateComment(string id, Comment updatedComment)
        {
            var objectId = new ObjectId(id);
            _comments.ReplaceOne(comment => comment.CommentId == objectId, updatedComment);
        }

        // Deletes a comment by its ID.
        public void DeleteComment(string id)
        {
            var objectId = new ObjectId(id);
            _comments.DeleteOne(comment => comment.CommentId == objectId);
        }
    }
}
