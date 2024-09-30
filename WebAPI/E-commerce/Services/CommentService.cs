/*
 * File: CommentService.cs
 * Author: Sanjayan
 * Description: This file defines the service layer for managing business logic related to comments, such as validation and interaction with the repository.
 */

using E_commerce.Models;
using E_commerce.Repositories;
using System.Collections.Generic;

namespace E_commerce.Services
{
    public class CommentService
    {
        private readonly ICommentRepository _commentRepository;

        // Initializes the CommentService with the repository.
        public CommentService(ICommentRepository commentRepository)
        {
            _commentRepository = commentRepository;
        }

        // Retrieves all comments.
        public List<Comment> GetAllComments()
        {
            return _commentRepository.GetAllComments();
        }

        // Retrieves a comment by its ID.
        public Comment GetCommentById(string id)
        {
            return _commentRepository.GetCommentById(id);
        }

        // Adds a new comment.
        public void AddComment(Comment comment)
        {
            _commentRepository.AddComment(comment);
        }

        // Retrieves comments by user ID.
        public List<Comment> GetCommentsByUserId(string userId)
        {
            return _commentRepository.GetCommentsByUserId(userId);
        }

        // Retrieves comments by vendor ID.
        public List<Comment> GetCommentsByVendorId(string vendorId)
        {
            return _commentRepository.GetCommentsByVendorId(vendorId);
        }

        // Retrieves comments by product ID.
        public List<Comment> GetCommentsByProductId(string productId)
        {
            return _commentRepository.GetCommentsByProductId(productId);
        }

        // Updates an existing comment.
        public void UpdateComment(string id, Comment updatedComment)
        {
            _commentRepository.UpdateComment(id, updatedComment);
        }

        // Deletes a comment by its ID.
        public void DeleteComment(string id)
        {
            _commentRepository.DeleteComment(id);
        }
    }
}
