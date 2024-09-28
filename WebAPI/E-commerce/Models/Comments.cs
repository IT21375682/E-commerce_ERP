namespace E_commerce.Models
{
    public class Comment
    {
        public string CommentId { get; set; }  // Unique identifier for the comment
        public string Id { get; set; }  // ID of the user who made the comment
        public string ProductId { get; set; }  // ID of the product the comment is associated with
        public DateTime Date { get; set; }  // Date the comment was made
        public int Rating { get; set; }  // Rating given to the product (e.g., 1-5 stars)
        public string CommentText { get; set; }  // The actual comment text
    }
}
