import {
  Badge,
  Card,
  CardHeader,
  Table,
  Container,
  Row,
  Button
} from "reactstrap";
import Header from "components/Headers/Header.js";
import React, { useState, useEffect } from "react"; 
import axios from "axios";

const CustomerTable = () => {
  const [users, setUsers] = useState([]); // Initialize state to hold user data
  const [loading, setLoading] = useState(true); // State to track loading status

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const response = await axios.get('https://localhost:7179/api/User'); // Fetch the users using Axios
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

  const UserStatus = async (customerId) => {
    // Confirmation dialog
    const confirmDelete = window.confirm('Are you sure you want to deactivate this Customer account?');
    
    if (confirmDelete) {
      try {
        // Call the API to update the vendor's active status
        const response = await axios.put(
          `https://localhost:7179/api/User/${customerId}/status`, 
          { isActive: false }, 
          { headers: { 'Content-Type': 'application/json' } }
        );

        if (response.status === 204) { // No Content status returned on successful update
          alert('Customer account has been deactivated successfully.');

          // Update the local state to reflect the deactivated vendor
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
  }
  return (
    <>
      <Container className="mt--7" fluid>
        {/* Table */}
        <Row>
          <div className="col mt-7">
            <Card className="shadow">
              <CardHeader className="border-0">
                <h3 className="mb-0">Active Customers</h3>
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
                    activeCustomers.length > 0 ? (
                      activeCustomers.map((customer) => (
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
                              onClick={() => UserStatus(customer.id)} // Navigate to activate customer page
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
