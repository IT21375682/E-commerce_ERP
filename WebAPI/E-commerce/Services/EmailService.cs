using System.Net.Mail;
using MailKit.Net.Smtp;
using MimeKit;
using System.Threading.Tasks;


namespace E_commerce.Services
{
    public class EmailService
    {
        public async Task SendEmailAsync(string toEmail, string subject, string message)
        {
            Console.WriteLine("Vendor Email : " + toEmail);
            var emailMessage = new MimeMessage();

            emailMessage.From.Add(new MailboxAddress("EAD", "krithigadb96@gmail.com"));
            emailMessage.To.Add(new MailboxAddress("", toEmail));
            emailMessage.Subject = subject;

            var bodyBuilder = new BodyBuilder
            {
                HtmlBody = message
            };
            emailMessage.Body = bodyBuilder.ToMessageBody();

            using (var client = new MailKit.Net.Smtp.SmtpClient())
            {
                try
                {
                    await client.ConnectAsync("smtp.gmail.com", 587, MailKit.Security.SecureSocketOptions.StartTls);

                    await client.AuthenticateAsync("krithigadb96@gmail.com", "kwez vjgj swzz yafo");
                    await client.SendAsync(emailMessage);
                }
                catch (Exception ex)
                {
                    throw new InvalidOperationException($"Error sending email: {ex.Message}", ex);
                }
                finally
                {
                    await client.DisconnectAsync(true);
                    client.Dispose();
                }
            }
        }


    }
}
