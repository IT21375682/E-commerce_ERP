/*
 * File: CommentController.cs
 * Author: Sanjayan
 * Registration No: IT21375514
 * Description: This file defines the API endpoints for managing comments in the e-commerce system. 
 * It includes routes for adding, retrieving, updating, and deleting comments.
 */

using E_commerce.DTOs;
using E_commerce.Models;
using E_commerce.Services;
using Microsoft.AspNetCore.Mvc;
using System.Collections.Generic;

namespace E_commerce.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class CommentController : ControllerBase
    {
        private readonly CommentService _commentService;

        // Initializes the controller with the service.
        public CommentController(CommentService commentService)
        {
            _commentService = commentService;
        }

        // GET api/Comment - Retrieves all comments.
        [HttpGet]
        public ActionResult<List<Comment>> GetAllComments()
        {
            var comments = _commentService.GetAllComments();
            return Ok(comments);
        }

        // GET api/Comment/{id} - Retrieves a comment by its ID.
        [HttpGet("{id}")]
        public ActionResult<Comment> GetCommentById(string id)
        {
            var comment = _commentService.GetCommentById(id);
            if (comment == null)
            {
                return NotFound();
            }
            return comment;
        }


        // POST api/Comment - Adds a new comment.
        [HttpPost]
        public IActionResult AddComment([FromBody] Comment comment)
        {
            try
            {
                _commentService.AddComment(comment);
                return CreatedAtAction(nameof(GetCommentById), new { id = comment.id }, comment);
            }
            catch (Exception ex)
            {
                return StatusCode(500, "Internal server error: " + ex.Message);
            }
        }

        // GET api/Comment/user/{userId} - Retrieves comments by user ID.
        [HttpGet("user/{userId}")]
        public ActionResult<List<CommentDto>> GetCommentsByUserId(string userId)
        {
            var comments = _commentService.GetCommentsByUserId(userId);
            return Ok(comments);
        }

        // GET api/Comment/vendor/{vendorId} - Retrieves comments by vendor ID.
        [HttpGet("vendor/{vendorId}")]
        public ActionResult<List<CommentDto>> GetCommentsByVendorId(string vendorId)
        {
            var comments = _commentService.GetCommentsByVendorId(vendorId);
            return Ok(comments);
        }

        // GET api/Comment/product/{productId} - Retrieves comments by product ID.
        [HttpGet("product/{productId}")]
        public ActionResult<List<CommentDto>> GetCommentsByProductId(string productId)
        {
            var comments = _commentService.GetCommentsByProductId(productId);
            return Ok(comments);
        }


        // PUT api/Comment/{id} - Updates an existing comment.
        [HttpPut("{id}")]
        public IActionResult UpdateComment(string id, [FromBody] Comment updatedComment)
        {
            _commentService.UpdateComment(id, updatedComment);
            return NoContent();
        }

        // DELETE api/Comment/{id} - Deletes a comment by its ID.
        [HttpDelete("{id}")]
        public IActionResult DeleteComment(string id)
        {
            _commentService.DeleteComment(id);
            return NoContent();
        }

        // GET api/Comment/vendor/{vendorId}/average-rating - Retrieves the average rating for a specific vendor
        [HttpGet("vendor/{vendorId}/average-rating")]
        public ActionResult<double> GetAverageRatingByVendorId(string vendorId)
        {
            var averageRating = _commentService.GetAverageRatingByVendorId(vendorId);
            return Ok(averageRating);
        }

        // GET api/Comment/product/{productId}/average-rating - Retrieves the average rating for a specific product
        [HttpGet("product/{productId}/average-rating")]
        public ActionResult<double> GetAverageRatingByProductId(string productId)
        {
            var averageRating = _commentService.GetAverageRatingByProductId(productId);
            return Ok(averageRating);
        }

        // POST api/Comment - Adds or updates a comment based on userId, vendorId, and productId.
        [HttpPost("mycomment")]
        public IActionResult AddOrUpdateComment([FromBody] Comment comment)
        {
            try
            {
                _commentService.AddOrUpdateComment(comment);
                return Ok(comment);
            }
            catch (Exception ex)
            {
                return StatusCode(500, "Internal server error: " + ex.Message);
            }
        }
    }
}
