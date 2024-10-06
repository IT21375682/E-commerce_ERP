import {
  Badge,
  Card,
  CardHeader,
  Table,
  Container,
  Row,
  Input,
  FormGroup,
  Col
} from "reactstrap";
import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from 'react-router-dom';
import { jwtDecode } from "jwt-decode";
import StarRatings from 'react-star-ratings'; // Import the StarRatings component

const ViewComments = () => {
  const [comments, setComments] = useState([]);  // Initialize state to hold comment data
  const [loading, setLoading] = useState(true);  // State to track loading status
  const [searchTerm, setSearchTerm] = useState(""); // State to hold search input

  const navigate = useNavigate();

  useEffect(() => {
    const vendorId = jwtDecode(localStorage.getItem("token")).id;
    const fetchComments = async () => {
      try {
        const response = await axios.get(`https://localhost:5004/api/Comment/vendor/${vendorId}`);
        setComments(response.data);  // Store the comments data in the state
        setLoading(false);  // Set loading to false once data is fetched
      } catch (error) {
        console.error('Error fetching comments:', error);  // Handle any errors
        setLoading(false);  // Set loading to false even if there's an error
      }
    };

    fetchComments();  // Fetch comments when component mounts
  }, []);  // Empty dependency array means this useEffect runs once when the component mounts

  // Calculate the average rating
  const calculateAverageRating = () => {
    if (comments.length === 0) return 0; // Avoid division by zero
    const totalRating = comments.reduce((sum, comment) => sum + comment.rating, 0);
    return totalRating / comments.length; // Return average rating
  };

  // Filter comments based on search term (username, product name, or comment text)
  const filteredComments = comments.filter((comment) =>
    comment.username.toLowerCase().includes(searchTerm.toLowerCase()) ||
    comment.productName.toLowerCase().includes(searchTerm.toLowerCase()) ||
    comment.commentText.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const averageRating = calculateAverageRating(); // Get the average rating

  return (
    <>
      <Container className="mt-10" fluid>
        <Row>
          <div className="col mt-7">
            <Card className="shadow">
              <CardHeader className="border-0">
                <Row>
                  <Col md="8" className="d-flex align-items-center">
                    <h3 className="mb-0 mr-4">My Comments</h3>
                    <StarRatings
                      rating={averageRating}
                      starRatedColor="gold" // Color of the filled stars
                      numberOfStars={5} // Total number of stars
                      starDimension="30px" // Size of the stars
                      starSpacing="2px" // Space between stars
                      className="ml-2" // Add margin to the left for spacing
                    />                  </Col>
                  <Col md="4" className="text-right">
                    <FormGroup>
                      <Input
                        type="text"
                        placeholder="Search by username, product name, or comment"
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                      />
                    </FormGroup>
                  </Col>
                </Row>
              </CardHeader>
              <Table className="align-items-center table-flush" responsive>
                <thead className="thead-light">
                  <tr>
                    <th scope="col">Username</th>
                    <th scope="col">Product Name</th>
                    <th scope="col">Date</th>
                    <th scope="col">Rating</th>
                    <th scope="col">Comment</th>
                  </tr>
                </thead>
                <tbody>
                  {loading ? (
                    <tr>
                      <td colSpan="5" className="text-center">Loading...</td>
                    </tr>
                  ) : (
                    filteredComments.length > 0 ? (
                      filteredComments.map((comment) => (
                        <tr key={comment.id}>
                          <td>{comment.username}</td>
                          <td>{comment.productName}</td>
                          <td>{new Date(comment.date).toLocaleDateString()}</td>
                          <td>
                            <StarRatings
                              rating={comment.rating} // Assuming rating is a number from 0 to 5
                              starRatedColor="gold" // Color of the filled stars
                              numberOfStars={5} // Total number of stars
                              starDimension="20px" // Size of the stars
                              starSpacing="2px" // Space between stars
                            />
                          </td>
                          <td>{comment.commentText}</td>
                        </tr>
                      ))
                    ) : (
                      <tr>
                        <td colSpan="5" className="text-center">No comments found</td>
                      </tr>
                    )
                  )}
                </tbody>
              </Table>
            </Card>
          </div>
        </Row>
      </Container>
    </>
  );
};

export default ViewComments;