using E_commerce.Models;
using E_commerce.Services;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

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


        // Fetch all users with a custom URL: /api/User/all
        //[HttpGet("all")]
     
        // GET: api/User
        [HttpGet]
        public ActionResult<IEnumerable<User>> GetAllUsers()
        {
            var users = _userService.GetAllUsers();
            return Ok(users);
        }
        


         // GET: api/User/{id}
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

        // POST: api/User
        [HttpPost]
        public ActionResult<User> CreateUser([FromBody] User newUser)
        {
            _userService.CreateUser(newUser);
            return CreatedAtAction(nameof(GetUserById), new { id = newUser.Id }, newUser);
        }



        // PUT: api/User/{id}
        [HttpPut("{id}")]
        public IActionResult UpdateUser(string id, [FromBody] User updatedUser)
        {
            var existingUser = _userService.GetUserById(id);
            if (existingUser == null)
            {
                return NotFound();
            }

            _userService.UpdateUser(id, updatedUser);
            return NoContent();
        }



        // DELETE: api/User/{id}
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



    }


 
}
