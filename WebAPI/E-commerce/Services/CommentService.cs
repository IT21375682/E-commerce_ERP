/*
 * File: CommentService.cs
 * Author: Sanjayan
 * Registration No: IT21375514
 * Description: This file defines the service layer for managing business logic related to comments, such as validation and interaction with the repository.
 */

using E_commerce.DTOs;
using E_commerce.Models;
using E_commerce.Repositories;
using System.Collections.Generic;

namespace E_commerce.Services
{
    public class CommentService
    {
        private readonly ICommentRepository _commentRepository;
        private readonly IUserRepository _userRepository;
        private readonly IProductRepository _productRepository;

        // Initializes the CommentService with the repository.
        public CommentService(ICommentRepository commentRepository, IUserRepository userRepository, IProductRepository productRepository)
        {
            _commentRepository = commentRepository;
            _userRepository = userRepository;
            _productRepository = productRepository;
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

        // Retrieves all comments by a specific user ID.
        public List<CommentDto> GetCommentsByUserId(string userId)
        {
            var comments = _commentRepository.GetCommentsByUserId(userId);
            return MapCommentsToDtos(comments);
        }

        // Retrieves comments by a specific vendor ID.
        public List<CommentDto> GetCommentsByVendorId(string vendorId)
        {
            var comments = _commentRepository.GetCommentsByVendorId(vendorId);
            return MapCommentsToDtos(comments);
        }

        // Retrieves comments by a specific product ID.
        public List<CommentDto> GetCommentsByProductId(string productId)
        {
            var comments = _commentRepository.GetCommentsByProductId(productId);
            return MapCommentsToDtos(comments);
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

        // Retrieves the average rating for a specific vendor
        public double GetAverageRatingByVendorId(string vendorId)
        {
            return _commentRepository.GetAverageRatingByVendorId(vendorId);
        }

        // Retrieves the average rating for a specific product
        public double GetAverageRatingByProductId(string productId)
        {
            return _commentRepository.GetAverageRatingByProductId(productId);
        }

        // Adds or updates a comment based on userId, vendorId, and productId.
        public void AddOrUpdateComment(Comment comment)
        {
            _commentRepository.AddOrUpdateComment(comment);
        }

        // Helper method to map comments to DTOs
        private List<CommentDto> MapCommentsToDtos(List<Comment> comments)
        {
            var commentDtos = new List<CommentDto>();

            foreach (var comment in comments)
            {
                var user = _userRepository.GetUserById(comment.userId); // Retrieve user info
                var vendor = _userRepository.GetUserById(comment.vendorId); // Retrieve vendor info
                var product = _productRepository.GetProductById(comment.productId); // Retrieve product info

                commentDtos.Add(new CommentDto
                {
                    id = comment.id,
                    userId = comment.userId,
                    username = user?.Name,
                    vendorId = comment.vendorId,
                    vendorName = vendor?.Name,
                    productId = comment.productId,
                    productName = product?.Name,
                    date = comment.date,
                    rating = comment.rating,
                    commentText = comment.commentText
                });
            }

            return commentDtos;
        }
    }
}
