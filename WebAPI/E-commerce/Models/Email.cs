/*
 * File: Email.cs
 * Author: Krithiga. D. B
 * Description: This file contains the definition of the Email class, which models an email to be sent as notification in the e-commerce system.
 * Each email has to-email id, subject and message
 */
namespace E_commerce.Models
{
    public class Email
    {
        public string ToEmail { get; set; }
        public string Subject { get; set; }
        public string Message { get; set; }

    }
}
