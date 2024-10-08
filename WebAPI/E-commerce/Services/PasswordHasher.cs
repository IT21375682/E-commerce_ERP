﻿/*
 * File: PasswordHasher.cs
 * Author: Shandeep .J   IT21375682
 * Description: This service handles hashing the password and verify the combinations
 */



using System;
using System.Security.Cryptography;
using Microsoft.AspNetCore.Cryptography.KeyDerivation;

namespace E_commerce.Services
{
    //Password hashing function
    public static class PasswordHasher
    {
        public static string HashPassword(string password)
        {
            // Generate a salt
            byte[] salt = new byte[16];
            using (var rng = RandomNumberGenerator.Create())
            {
                rng.GetBytes(salt);
            }

            // Hash the password
            var hashed = Convert.ToBase64String(KeyDerivation.Pbkdf2(
                password: password,
                salt: salt,
                prf: KeyDerivationPrf.HMACSHA256,
                iterationCount: 10000,
                numBytesRequested: 32));

            // Combine salt and hashed password
            return $"{Convert.ToBase64String(salt)}.{hashed}";
        }
        //Password and hashedpassword verifcation
        public static bool VerifyPassword(string password, string hashedPassword)
        {
            var parts = hashedPassword.Split('.');
            var salt = Convert.FromBase64String(parts[0]);
            var storedHash = parts[1];

            var hashed = Convert.ToBase64String(KeyDerivation.Pbkdf2(
                password: password,
                salt: salt,
                prf: KeyDerivationPrf.HMACSHA256,
                iterationCount: 10000,
                numBytesRequested: 32));

            return hashed == storedHash;
        }
    }
}