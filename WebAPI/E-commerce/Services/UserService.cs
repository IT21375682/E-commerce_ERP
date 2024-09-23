using E_commerce.Models;
using E_commerce.Repositories;

namespace E_commerce.Services
{
    public class UserService
    {
        private readonly IUserRepository _userRepository;

        public UserService(IUserRepository userRepository)
        {
            _userRepository = userRepository;
        }

        public IEnumerable<User> GetAllUsers()
        {
            return _userRepository.GetAllUsers();
        }

        public User GetUserById(string id)
        {
            return _userRepository.GetUserById(id);
        }

        public void CreateUser(User user)
        {
            _userRepository.CreateUser(user);
        }

        public void UpdateUser(string id, User user)
        {
            _userRepository.UpdateUser(id, user);
        }

        public void DeleteUser(string id)
        {
            _userRepository.DeleteUser(id);
        }


    }
}
