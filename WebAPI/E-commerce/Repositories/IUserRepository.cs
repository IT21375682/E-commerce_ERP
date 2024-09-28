using E_commerce.Models;
using MongoDB.Driver;
using System.Collections.Generic;
using System;
using MongoDB.Bson;

namespace E_commerce.Repositories
{
    public class IUserRepository   // Rename the class to `UserRepository` to follow naming conventions
    {
        private readonly IMongoCollection<User> _users;

        public IUserRepository(IMongoDatabase database)
        {
            _users = database.GetCollection<User>("Users");
        }

        // Retrieve all users
        public IEnumerable<User> GetAllUsers()
        {
            return _users.Find(user => true).ToList();
        }

        // Retrieve a user by Id
        public User GetUserById(string id)
        {
            return _users.Find(user => user.Id == id).FirstOrDefault();
        }
        public User GetUserByEmail (string email)
        {
            return _users.Find(user => user.Email == email).FirstOrDefault();
        }
        // Create a new user
        public void CreateUser(User user)
        {
            //// Ensure the user has an Id
            if (string.IsNullOrEmpty(user.Id))
            {
                user.Id = ObjectId.GenerateNewId().ToString(); // Generate a unique Id if not provided
            }
            user.CreatedAt = DateTime.Now; // Set the creation time
            _users.InsertOne(user);
        }

        // Update an existing user by Id
        public void UpdateUser(string id, User user)
        {
            user.Id = id; // Ensure the Id remains the same
            _users.ReplaceOne(existingUser => existingUser.Id == id, user);
        }

        // Delete a user by Id
        public void DeleteUser(string id)
        {
            _users.DeleteOne(user => user.Id == id);
        }

        // Activate or deactivate a user account
        public void ToggleUserActivation(string id, bool activate)
        {
            var update = Builders<User>.Update.Set(u => u.IsActive, activate);
            _users.UpdateOne(user => user.Id == id, update);
        }
    }
}
