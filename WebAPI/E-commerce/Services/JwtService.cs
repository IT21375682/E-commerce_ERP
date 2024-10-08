
/*
 * File: JwtService.cs
 * Author: Shandee.J   IT21375682
 * Description: This service handle JWT token genrating function
 */


using System;
using System.Collections.Generic;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;
using Microsoft.Extensions.Configuration;
using Microsoft.IdentityModel.Tokens;

namespace E_commerce.Services
{
    public class JwtService
    {
        private readonly string _secretKey;

        public JwtService(string secretKey) // Accept string directly
        {
            _secretKey = secretKey;
        }

        public string GenerateToken(string email, string role, string Name, string userId)
        {
            var tokenHandler = new JwtSecurityTokenHandler();
            var key = Encoding.ASCII.GetBytes(_secretKey);

            // Use simple claim names like "email", "role", "username", and "id"
            var claims = new List<Claim>
            {
                new Claim("email", email),
                new Claim("role", role),
                new Claim("Name", Name),
                new Claim("id", userId)
            };

            var tokenDescriptor = new SecurityTokenDescriptor
            {
                Subject = new ClaimsIdentity(claims),
                Expires = DateTime.UtcNow.AddHours(1), // Token expiration time
                SigningCredentials = new SigningCredentials(
                    new SymmetricSecurityKey(key),
                    SecurityAlgorithms.HmacSha256Signature
                )
            };

            var token = tokenHandler.CreateToken(tokenDescriptor);
            return tokenHandler.WriteToken(token);
        }
    }
}