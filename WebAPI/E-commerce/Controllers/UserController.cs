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
        [HttpGet("active")] // New endpoint to get active users
        public ActionResult<IEnumerable<User>> GetActiveUsers()
        {
            var activeUsers = _userService.GetActiveUsers();
            return Ok(activeUsers);
        }





        //GET :api/User/Deactive- Fetch all Active users only
        [HttpGet("Deactive")] // New endpoint to get active users
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
                return BadRequest(new { Message = ex.Message });
            }
        }
    }


   








}
