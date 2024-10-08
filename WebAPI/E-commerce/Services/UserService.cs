/*
 * File: UserService.cs
 * Author: Shandeep .J    IT21375682
 * Description: This file defines the service layer for managing business logic related to User, such as validation and interaction with the repository.
 */

using E_commerce.DTOs;
using E_commerce.Models;
using E_commerce.Repositories;

namespace E_commerce.Services
{
    public class UserService
    {
        private readonly IUserRepository _userRepository;
        private readonly JwtService _jwtService;
        //initialize UserRepesitory and Jwtservice
        public UserService(IUserRepository userRepository, JwtService jwtService)
        {
            _userRepository = userRepository;
            _jwtService = jwtService;
        }
        //GET all User
        public IEnumerable<User> GetAllUsers()
        {
            return _userRepository.GetAllUsers();
        }
        //Get user by ID 
        public User GetUserById(string id)
        {
            return _userRepository.GetUserById(id);
        }
        //Get user by email
        public User GetUserByEmail(string email)
        {
            return _userRepository.GetUserByEmail(email);
        }

        //Create user
        public void CreateUser(User user)
        {
            // Check if the email is already taken
            if (_userRepository.IsEmailTaken(user.Email))
            {
                throw new ArgumentException("Email is already in use");
            }

            user.Password = PasswordHasher.HashPassword(user.Password); // Hash the password
            _userRepository.CreateUser(user);
        }


        //Login
        public Task<string> LoginAsync(string email, string password)
        {
            if (string.IsNullOrEmpty(email) || string.IsNullOrEmpty(password))
                throw new ArgumentException("Email or password is missing");

            var user = GetUserByEmail(email);
            if (user == null)
                throw new Exception("User not found");

            if (!PasswordHasher.VerifyPassword(password, user.Password))
                throw new Exception("Invalid password");

            if (!user.IsActive)
                throw new Exception("User account is not active");

            if (_jwtService == null)
                throw new NullReferenceException("_jwtService is not initialized");

            return Task.FromResult(_jwtService.GenerateToken(user.Email, user.Role, user.Name, user.Id));
        }


        //Update user data
        public void UpdateUser(string id, User user)
        {
            _userRepository.UpdateUser(id, user);
        }

        public void DeleteUser(string id)
        {
            _userRepository.DeleteUser(id);
        }

        public UserDto GetUserNameById(string id)
        {
            return _userRepository.GetUserNameById(id);
        }

        //Get All active Users
        public IEnumerable<User> GetActiveUsers()
        {
            return _userRepository.GetActiveUsers(); // Fetch active users
        }



        //Get All Deactived Users
        public IEnumerable<User> GetDeactiveUsers()
        {
            return _userRepository.GetDeactiveUsers(); // Fetch active users
        }

        public void ToggleUserStatus(string id, bool isActive,bool isNew)
        {
            var user = _userRepository.GetUserById(id);
            if (user != null)
            {
                user.IsActive = isActive; // Update the isActive status
                user.IsNew = isNew; // Update the isNew status
                _userRepository.UpdateUser(user); // Save the changes
            }
        }
        
        //Regenerate a token after the user is updated
        public Task<string> GenerateTokenAfterUpdate(string email)
        {
            var user = GetUserByEmail(email);
            if (user == null)
            {
                throw new Exception("User not found");
            }

            if (_jwtService == null)
                throw new NullReferenceException("_jwtService is not initialized");

            return Task.FromResult(_jwtService.GenerateToken(user.Email, user.Role, user.Name, user.Id));
        }


        // Check if a user with the given email already exists
        public bool IsEmailTaken(string email)
        {
            return _userRepository.GetAllUsers().Any(user => user.Email == email);
        }

        //Deactivate user account
        public void DeactivateUserAccount(string userId)
        {
            var user = _userRepository.GetUserById(userId);
            if (user == null)
            {
                throw new Exception("User not found");
            }

            user.IsActive = false; // Deactivate the account
            _userRepository.UpdateUserDeactiveStatus(userId, user); // Save the changes
        }

    }
}
