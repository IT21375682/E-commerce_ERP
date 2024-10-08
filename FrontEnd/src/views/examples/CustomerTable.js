import {
  Badge,
  Card,
  CardHeader,
  Table,
  Container,
  Row,
  Button,
  FormGroup,
  Col,
  Input,
} from "reactstrap";
import React, { useState, useEffect } from "react"; 
import axios from "axios";

const CustomerTable = () => {
  const [users, setUsers] = useState([]); // Initialize state to hold user data
  const [loading, setLoading] = useState(true); // State to track loading status
  const [searchTerm, setSearchTerm] = useState(""); // State to hold search input

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const response = await axios.get('https://localhost:5004/api/User'); // Fetch the users using Axios
        setUsers(response.data); // Store the user data in the state
        setLoading(false); // Set loading to false once data is fetched
      } catch (error) {
        console.error('Error fetching users:', error); // Handle any errors
        setLoading(false); // Set loading to false even if there's an error
      }
    };

    fetchUsers(); // Call the Axios fetch function when the component mounts
  }, []); // Empty dependency array means this useEffect runs once when the component mounts

  // Filter the users to only include those with the role of 'CUSTOMER' and who are active
  const activeCustomers = users.filter((user) => user.role === 'CUSTOMER' && user.isActive);

  const CustomerRequests = users.filter((user) => user.role === 'CUSTOMER' && !user.isActive && user.isNew);

  // // Get the count of inactive customers
  // const CustomerRequestsCount = CustomerRequests.length;

  // Filter active customers based on search term (name, email, or phone)
  const filteredCustomers = activeCustomers.filter((customer) => 
    customer.name?.toLowerCase().includes(searchTerm.toLowerCase()) ||
    customer.email?.toLowerCase().includes(searchTerm.toLowerCase()) ||
    customer.phone?.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const UserStatus = async (customerId) => {
    // Confirmation dialog
    const confirmDelete = window.confirm('Are you sure you want to deactivate this Customer account?');
    
    if (confirmDelete) {
      try {
        // Call the API to update the customer's active status
        const response = await axios.put(
          `https://localhost:5004/api/User/${customerId}/status`, 
          { isActive: false }, 
          { headers: { 'Content-Type': 'application/json' } }
        );

        if (response.status === 204) { // No Content status returned on successful update
          alert('Customer account has been deactivated successfully.');

          // Update the local state to reflect the deactivated customer
          setUsers((prevUsers) =>
            prevUsers.map((user) =>
              user.id === customerId ? { ...user, isActive: false } : user
            )
          );
        } else {
          alert('Failed to deactivate the Customer.');
        }
      } catch (error) {
        console.error('Error deactivating Customer:', error);
        alert('An error occurred while trying to deactivate the Customer.');
      }
    }
  };

  return (
    <>
      <Container className="mt-10" fluid>
        {/* Table */}
        <Row>
          <div className="col mt-7">
            <Card className="shadow">
              <CardHeader className="border-0">
                <Row>
                  <Col md="8">
                    <h3 className="mb-0">Active Customers</h3>
                  </Col>
                  <Col md="4" className="text-right">
                    <FormGroup>
                      <Input
                        type="text"
                        placeholder="Search by name, email, or phone"
                        value={searchTerm}  // Bind the input field to the searchTerm state
                        onChange={(e) => setSearchTerm(e.target.value)} // Update searchTerm when input changes
                      />
                    </FormGroup>
                  </Col>
                </Row>
              </CardHeader>
              <Table className="align-items-center table-flush" responsive>
                <thead className="thead-light">
                  <tr>
                    <th colSpan="6" className="text-right">
                      <Button
                        color="primary"
                        size="md"
                        onClick={() => window.location.href = "/admin/Customer-Requests"} // Or use React Router for navigation
                        className="text-end"
                      >
                        Customer Requests
                        <div
                          style={{
                            backgroundColor: '#C0392B', // Dark red color
                            color: 'white', // Text color
                            borderRadius: '50%', // Make it circular
                            width: '25px', // Width of the badge
                            height: '25px', // Height of the badge
                            display: 'flex',
                            justifyContent: 'center',
                            alignItems: 'center',
                            position: 'absolute',
                            top: '1px',
                            left: '1px',
                            fontSize: '12px', // Adjust font size
                              }}
                        >
                   {CustomerRequests.length} {/* Display the count */}
                   </div>
                      </Button>
                    </th>
                  </tr>
                  <tr>
                    <th scope="col">Name</th>
                    <th scope="col">Email</th>
                    <th scope="col">Phone</th>
                    <th scope="col">Address</th>
                    <th scope="col">Status</th>
                    <th scope="col" />
                    <th scope="col" />
                  </tr>
                </thead>
                <tbody>
                  {/* Display a loading message while data is being fetched */}
                  {loading ? (
                    <tr>
                      <td colSpan="7" className="text-center">Loading...</td>
                    </tr>
                  ) : (
                    filteredCustomers.length > 0 ? (
                      filteredCustomers.map((customer) => (
                        <tr key={customer.id}>
                          <td>{customer.name}</td>
                          <td>{customer.email}</td>
                          <td>{customer.phone}</td>
                          <td>
                            {customer.address
                              ? `${customer.address?.street || 'N/A'}, ${customer.address?.city || 'N/A'}, ${customer.address?.postalCode || 'N/A'}, ${customer.address?.country || 'N/A'}` 
                              : 'No Address'}
                          </td>
                          <td>
                            <Badge color="success">Active</Badge>
                          </td>
                          <td>
                            <Button
                              color="primary"
                              size="md"
                              onClick={() => window.location.href = `/admin/customer/${customer.id}`} // Navigate to customer edit page
                              className="text-end"
                            >
                              View
                            </Button>
                          </td>
                          <td>
                            <Button
                              color="danger"
                              size="md"
                              onClick={() => UserStatus(customer.id)} // Deactivate customer
                              className="text-end"
                            >
                              Deactivate
                            </Button>
                          </td>
                        </tr>
                      ))
                    ) : (
                      <tr>
                        <td colSpan="7" className="text-center">No active customers found</td>
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

export default CustomerTable;
