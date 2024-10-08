/*
 * File: UserController.cs
 * Author: Shandeep. J    IT21375682
 * Description: This file defines the API endpoints for managing User in the e-commerce system. 
 * It includes routes for adding, retrieving, updating, and deleting User details.
 */


using E_commerce.DTOs;
using E_commerce.Models;
using E_commerce.Services;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using System.Collections.Generic;

namespace E_commerce.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class UserController : ControllerBase
    {
        private readonly UserService _userService;

        // Initializes the controller with the service.
        public UserController(UserService userService)
        {
            _userService = userService;
        }

        // GET: api/User - Fetch all users
        [HttpGet]
        public ActionResult<IEnumerable<User>> GetAllUsers()
        {
            var users = _userService.GetAllUsers();
            return Ok(users);
        }

        //GET :api/User/active- Fetch all Active users only
        [HttpGet("active")]
        public ActionResult<IEnumerable<User>> GetActiveUsers()
        {
            var activeUsers = _userService.GetActiveUsers();
            return Ok(activeUsers);
        }





        //GET :api/User/Deactive- Fetch all Active users only
        [HttpGet("Deactive")] 
        public ActionResult<IEnumerable<User>> GetDeactiveUsers()
        {
            var DeactiveUsers = _userService.GetDeactiveUsers();
            return Ok(DeactiveUsers);
        }


        // GET: api/User/{id} - Fetch a user by ID
        [HttpGet("{id}")]
        public ActionResult<User> GetUserById(string id)
        {
            var user = _userService.GetUserById(id);
            if (user == null)
            {
                return NotFound();
            }
            return Ok(user);
        }


        [HttpPost]
        public ActionResult<User> CreateUser([FromBody] User newUser)
        {
            try
            {
                _userService.CreateUser(newUser);
                return CreatedAtAction(nameof(GetUserById), new { id = newUser.Id }, newUser);

            }
            catch (ArgumentException ex)
            {
                // Return a 400 Bad Request with the error message
                return BadRequest(new { message = ex.Message });
            }
        }


        // PUT: api/User/{id} - Update an existing user
        [HttpPut("{id}")]
        public IActionResult UpdateUser(string id, [FromBody] User updatedUser)
        {
            var existingUser = _userService.GetUserById(id);
            if (existingUser == null)
            {
                return NotFound();
            }

            // Ensure that the user ID remains the same during update
            updatedUser.Id = id;
            _userService.UpdateUser(id, updatedUser);
            return NoContent();
        }

        // DELETE: api/User/{id} - Delete a user by ID
        [HttpDelete("{id}")]
        public IActionResult DeleteUser(string id)
        {
            var existingUser = _userService.GetUserById(id);
            if (existingUser == null)
            {
                return NotFound();
            }

            _userService.DeleteUser(id);
            return NoContent();
        }
        

        //Login
        [HttpPost("login")]
        public async Task<IActionResult> Login([FromBody] Login model)
        {
            try
            {
                var user = await _userService.LoginAsync(model.Email, model.Password);
                return Ok(new { Token = user });
            }
            catch (Exception ex)
            {
                if (ex.Message == "User account is not active")
                {
                    return BadRequest(new { Message = "Your account is not active. Please contact support." });
                }
                return BadRequest(new { Message = ex.Message });
            }
        }

        // GET: api/User/{id} - Fetch a user name by ID
        [HttpGet("Only/{id}")]
        public ActionResult<UserDto> GetUserNameById(string id)
        {
            var user = _userService.GetUserNameById(id);
            if (user == null)
            {
                return NotFound();
            }
            return Ok(user);
        }
        // PUT: api/User/{id}/status - change Active status of User
        [HttpPut("{id}/status")]
        public IActionResult ToggleUserStatus(string id, [FromBody] ToggleStatusRequest request)
        {
            if (request == null || !ModelState.IsValid)
            {
                return BadRequest("Invalid request.");
            }
            _userService.ToggleUserStatus(id, request.IsActive, request.IsNew);
            return NoContent();
        }


        // PATCH: api/User/{id} - Partially update an existing user
        [HttpPatch("UpdateUser/{id}")]
        public async Task<IActionResult> PatchUser(string id, [FromBody] UserUpdateDto updatedFields)
        {
            var existingUser = _userService.GetUserById(id);
            if (existingUser == null)
            {
                return NotFound();
            }
            
            // Update only the fields that are provided
            if (updatedFields.Name != null)
            {
                existingUser.Name = updatedFields.Name;
            }
            if (updatedFields.Phone != null)
            {
                existingUser.Phone = updatedFields.Phone;
            }
            if (updatedFields.Address != null)
            {
                existingUser.Address = updatedFields.Address;
            }

            // Update the user in the service
            _userService.UpdateUser(id, existingUser);

            // Generate a new JWT token after update
            var token = await _userService.GenerateTokenAfterUpdate(existingUser.Email);
            return Ok(new { Token = token });
        }

        // GET: api/User/check-email?email=test@example.com
        [HttpGet("check-email")]
        public IActionResult CheckEmail(string email)
        {
            if (string.IsNullOrEmpty(email))
            {
                return BadRequest("Email is required.");
            }

            bool isTaken = _userService.IsEmailTaken(email);
            return Ok(new { isUnique = !isTaken });
        }

        // Deactivate Customer Account
        [HttpPut("{userId}/my-account-deactivate")]
        public IActionResult DeactivateMyUserAccount(string userId)
        {
            var user = _userService.GetUserById(userId);
            if (user == null)
            {
                return NotFound("User not found");
            }

            _userService.DeactivateUserAccount(userId);
            return NoContent();
        }


    }



}
