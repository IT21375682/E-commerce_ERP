using E_commerce.Models;
using MongoDB.Driver;
using System.Collections.Generic;

namespace E_commerce.Repositories
{
    public class IUserRepository 
    {
        private readonly IMongoCollection<User> _users;

        public IUserRepository(IMongoDatabase database)
        {
            _users = database.GetCollection<User>("Users");
        }

        public IEnumerable<User> GetAllUsers()
        {
            return _users.Find(user => true).ToList();
        }

        public User GetUserById(string id)
        {
            return _users.Find(user => user.Id == id).FirstOrDefault();
        }

        public void CreateUser(User user)
        {
            _users.InsertOne(user);
        }

        public void UpdateUser(string id, User user)
        {
            _users.ReplaceOne(existingUser => existingUser.Id == id, user);
        }

        public void DeleteUser(string id)
        {
            _users.DeleteOne(user => user.Id == id);
        }
    }
}
