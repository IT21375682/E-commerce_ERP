import {
  Badge,
  Card,
  CardHeader,
  Table,
  Container,
  Row,
  Button,
  Input, // Add Input component from reactstrap
  FormGroup,
  Col
} from "reactstrap";
import React, { useState, useEffect } from "react"; 
import axios from "axios";
import { useNavigate } from 'react-router-dom';
const VendorTable = () => {
  const [users, setUsers] = useState([]);  // Initialize state to hold user data
  const [loading, setLoading] = useState(true);  // State to track loading status
  const [searchTerm, setSearchTerm] = useState(""); // State to hold search input

  const navigate = useNavigate();

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const response = await axios.get('https://localhost:7179/api/User');  // Fetch the users using Axios
        setUsers(response.data);  // Store the user data in the state
        setLoading(false);  // Set loading to false once data is fetched
      } catch (error) {
        console.error('Error fetching users:', error);  // Handle any errors
        setLoading(false);  // Set loading to false even if there's an error
      }
    };

    fetchUsers();  // Call the Axios fetch function when the component mounts
  }, []);  // Empty dependency array means this useEffect runs once when the component mounts

  // Filter the users to only include those with the role of 'VENDOR'
  const vendors = users.filter((user) => user.role === 'VENDOR');

  // Filter vendors based on search term (name, email, or phone)
  const filteredVendors = vendors.filter((vendor) => 
    vendor.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
    vendor.email.toLowerCase().includes(searchTerm.toLowerCase()) ||
    vendor.phone.toLowerCase().includes(searchTerm.toLowerCase())  
  );

  const handleDelete = async (vendorId) => {
    // Confirmation dialog
    const confirmDelete = window.confirm('Are you sure you want to deactivate this vendor account?');
    
    if (confirmDelete) {
      try {
        // Call the API to update the vendor's active status
        const response = await axios.put(
          `https://localhost:7179/api/User/${vendorId}/status`, 
          { isActive: false }, 
          { headers: { 'Content-Type': 'application/json' } }
        );

        if (response.status === 204) { // No Content status returned on successful update
          alert('Vendor account has been deactivated successfully.');

          // Update the local state to reflect the deactivated vendor
          setUsers((prevUsers) =>
            prevUsers.map((user) =>
              user.id === vendorId ? { ...user, isActive: false } : user
            )
          );
        } else {
          alert('Failed to deactivate the vendor.');
        }
      } catch (error) {
        console.error('Error deactivating vendor:', error);
        alert('An error occurred while trying to deactivate the vendor.');
      }
    }
  }

  
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
                    <h3 className="mb-0">Vendors</h3>
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
                        onClick={() => window.location.href = "/admin/add-vendor"} // Or use React Router for navigation
                        className="text-end"
                      >
                        Add Vendor
                      </Button>
                    </th>
                  </tr>
                  <tr>
                    <th scope="col">Name</th>
                    <th scope="col">Email</th>
                    <th scope="col">Phone</th>
                    <th scope="col">Address</th>
                    <th scope="col">IsActive</th>
                    <th scope="col" />
                    <th scope="col" />
                  </tr>
                </thead>
                <tbody>
                  {loading ? (
                    <tr>
                      <td colSpan="7" className="text-center">Loading...</td>
                    </tr>
                  ) : (
                    filteredVendors.length > 0 ? (
                      filteredVendors.map((vendor) => (
                        <tr key={vendor.id}>
                          <td>{vendor.name}</td>
                          <td>{vendor.email}</td>
                          <td>{vendor.phone}</td>
                          <td>{vendor.address 
                            ? `${vendor.address?.street || 'N/A'}, ${vendor.address?.city || 'N/A'}, ${vendor.address?.postalCode || 'N/A'}, ${vendor.address?.country || 'N/A'}` 
                            : 'No Address'}</td>
                          <td>
                            <Badge color={vendor.isActive ? "success" : "danger"}>
                              {vendor.isActive ? "Active" : "Inactive"}
                            </Badge>
                          </td>
                          <td>
                          <Button
                                color="primary"
                                size="md"
                                onClick={() => navigate(`/admin/vendor/${vendor.id}`)} // Navigate to the vendor details page with the vendor's ID
                                className="text-end"
                              >
                                View
                              </Button> 
                          </td>
                          <td>
                            <Button
                              color="danger"
                              size="md"
                              onClick={() => handleDelete(vendor.id)} // Or use React Router for navigation
                              className="text-end"
                            >
                              Deactivate
                            </Button>
                          </td>
                        </tr>
                      ))
                    ) : (
                      <tr>
                        <td colSpan="7" className="text-center">No vendors found</td>
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

export default VendorTable;
  