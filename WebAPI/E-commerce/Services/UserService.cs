using E_commerce.DTOs;
using E_commerce.Models;
using E_commerce.Repositories;

namespace E_commerce.Services
{
    public class UserService
    {
        private readonly IUserRepository _userRepository;
        private readonly JwtService _jwtService;

        public UserService(IUserRepository userRepository, JwtService jwtService)
        {
            _userRepository = userRepository;
            _jwtService = jwtService;
        }

        public IEnumerable<User> GetAllUsers()
        {
            return _userRepository.GetAllUsers();
        }

        public User GetUserById(string id)
        {
            return _userRepository.GetUserById(id);
        }

        public User GetUserByEmail(string email)
        {
            return _userRepository.GetUserByEmail(email);
        }


        public void CreateUser(User user)
        {
            user.Password = PasswordHasher.HashPassword(user.Password);
            _userRepository.CreateUser(user);
        }



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

    }
}
