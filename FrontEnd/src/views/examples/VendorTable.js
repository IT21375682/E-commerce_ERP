import {
  Badge,
  Card,
  CardHeader,
  Table,
  Container,
  Row,
  Button
} from "reactstrap";
// core components
import Header from "components/Headers/Header.js";
import React, { useState, useEffect } from "react"; // Import hooks for state management and side effects
import axios from "axios";

const VendorTable = () => {
  const [users, setUsers] = useState([]);  // Initialize state to hold user data
  const [loading, setLoading] = useState(true);  // State to track loading status

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

  return (
    <>
      {/* <Header /> */}
      {/* Page content */}
      <Container className="mt--7" fluid>
        {/* Table */}
        <Row>
          <div className="col mt-7">
            <Card className="shadow">
              <CardHeader className="border-0">
                <h3 className="mb-0">Vendors</h3>
              </CardHeader>
              <Table className="align-items-center table-flush" responsive>
                <thead className="thead-light">
                  <tr>
                  <th colSpan="6" className="text-right"> {/* Align the button to the right */}
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
                  {/* Display a loading message while data is being fetched */}
                  {loading ? (
                    <tr>
                      <td colSpan="7" className="text-center">Loading...</td>
                    </tr>
                  ) : (
                    vendors.length > 0 ? (
                      vendors.map((vendor) => (
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
                      onClick={() => window.location.href = "/admin/edit-vendor"} // Or use React Router for navigation
                      className="text-end"
                    >
                      Edit
                    </Button>
                  </td>
                  <td>
                    <Button
                      color="danger"
                      size="md"
                      onClick={() => window.location.href = "/admin/delete-vendor"} // Or use React Router for navigation
                      className="text-end"
                    >
                      Delete
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
