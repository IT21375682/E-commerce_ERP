using E_commerce.Models;
using MongoDB.Driver;
using System.Collections.Generic;
using System;
using MongoDB.Bson;
using E_commerce.DTOs;

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
        // Check if a user with the given email already exists
        public bool IsEmailTaken(string email)
        {
            return _users.Find(user => user.Email == email).Any();
        }

        //Create User
        public void CreateUser(User user)
        {
            if (string.IsNullOrEmpty(user.Id))
            {
                user.Id = ObjectId.GenerateNewId().ToString(); // Generate unique Id
            }
            user.CreatedAt = DateTime.Now;
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
        public void ToggleUserActivation(string id, bool activate,bool isNew)
        {
            var update = Builders<User>.Update
           .Set(u => u.IsActive, activate)
           .Set(u => u.IsNew, isNew);
            _users.UpdateOne(user => user.Id == id, update);
        }


        //Get All active users
        public IEnumerable<User> GetActiveUsers()
        {
            return _users.Find(user => user.IsActive).ToList(); // Fetch only active users
        }


        //Get All active users
        public IEnumerable<User> GetDeactiveUsers()
        {
            return _users.Find(user => !user.IsActive).ToList(); // Fetch only active users
        }

        // Retrieve a user by Id
        public UserDto GetUserNameById(string id)
        {
            return _users.Find(user => user.Id == id)
                         .Project(user => new UserDto
                         {
                             Id = user.Id,
                             Name = user.Name
                         }).FirstOrDefault();
        }
        
        //Update user active status
        public void UpdateUser(User user)
        {
            _users.ReplaceOne(u => u.Id == user.Id, user);
        }

        // Deactivate Customer Account
        public void UpdateUserDeactiveStatus(string userId, User user)
        {
            _users.ReplaceOne(existingUser => existingUser.Id == userId, user);
        }


    }
}
