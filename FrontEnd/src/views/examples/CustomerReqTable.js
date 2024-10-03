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
  Input
} from "reactstrap";
import Header from "components/Headers/Header.js";
import React, { useState, useEffect } from "react"; 
import axios from "axios";

const CustomerReqTable = () => {
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

  // Filter the users to only include those with the role of 'CUSTOMER' and who are inactive
  const inactiveCustomers = users.filter((user) => user.role === 'CUSTOMER' && !user.isActive && user.isNew);

  // Filter inactive customers based on search term (name, email, or phone)
  const filteredCustomers = inactiveCustomers.filter((customer) =>
    (customer.name?.toLowerCase() || "").includes(searchTerm.toLowerCase()) ||
    (customer.email?.toLowerCase() || "").includes(searchTerm.toLowerCase()) ||
    (customer.phone?.toLowerCase() || "").includes(searchTerm.toLowerCase())
  );

  const UserStatus = async (customerId) => {
    // Confirmation dialog
    const confirmActivate = window.confirm('Are you sure you want to activate this customer account?');
    
    if (confirmActivate) {
      try {
        // Call the API to update the customer's active status
        const response = await axios.put(
          `https://localhost:5004/api/User/${customerId}/status`, 
          { isActive: true, isNew: false }, 
          { headers: { 'Content-Type': 'application/json' } }
        );

        if (response.status === 204) { // No Content status returned on successful update
          alert('Customer account has been activated successfully.');

          // Update the local state to reflect the activated customer
          setUsers((prevUsers) =>
            prevUsers.map((user) =>
              user.id === customerId ? { ...user, isActive: true, isNew: false } : user
            )
          );
        } else {
          alert('Failed to activate the customer.');
        }
      } catch (error) {
        console.error('Error activating customer:', error);
        alert('An error occurred while trying to activate the customer.');
      }
    }
  };

  return (
    <>
      <Container className="mt--7" fluid>
        {/* Table */}
        <Row>
          <div className="col mt-7">
            <Card className="shadow">
              <CardHeader className="border-0">
                <Row>
                  <Col md="8">
                    <h3 className="mb-0">Customer Requests</h3>
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
                        onClick={() => window.location.href = "/admin/InactiveCus-table"} // Or use React Router for navigation
                        className="text-end"
                      >
                        Inactive Customers
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
                            <Badge color="danger">Inactive</Badge>
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
                              color="success"
                              size="md"
                              onClick={() => UserStatus(customer.id)} // Activate customer
                              className="text-end"
                            >
                              Activate
                            </Button>
                          </td>
                        </tr>
                      ))
                    ) : (
                      <tr>
                        <td colSpan="7" className="text-center">No inactive customers found</td>
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

export default CustomerReqTable;
