import {
  Button,
  Card,
  CardHeader,
  CardBody,
  FormGroup,
  Form,
  Input,
  Container,
  Row,
  Col,
} from "reactstrap";
import React, { useState, useEffect } from "react";
import axios from "axios";
import { useParams } from "react-router-dom"; // Assume you're using react-router for navigation

const ViewCustomer = () => {
  // Get vendor ID from URL (if using react-router)
  const { customerId } = useParams();

  // State for storing vendor data and edit mode
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    phone: '',
    address: {
      street: '',
      city: '',
      postalCode: '',
      country: ''
    },
    role: 'CUSTOMER',
    isActive: true,
    password: '',
  });
  
  const [isEditMode, setIsEditMode] = useState(false); // State for toggling between view and edit mode


  
  useEffect(() => {
    // Fetch vendor details when component mounts
    const fetchVendorDetails = async () => {
      try {
        const response = await axios.get(`https://localhost:7179/api/User/${customerId}`); // Replace with actual endpoint
        setFormData(response.data);
      } catch (error) {
        console.error('Error fetching vendor details:', error);
      }
    };

    fetchVendorDetails();
  }, [customerId]);

  // Handle form input change
  const handleChange = (e) => {
    const { name, value } = e.target;
    
    if (name.includes('address.')) {
      const addressField = name.split('.')[1];
      setFormData((prevData) => ({
        ...prevData,
        address: {
          ...prevData.address,
          [addressField]: value
        }
      }));
    } else {
      setFormData({
        ...formData,
        [name]: value
      });
    }
  };

  // Handle form submission for editing
  const handleSave = async (e) => {
    e.preventDefault();

    try {
      // Send updated form data to backend API using PUT method
      await axios.put(`https://localhost:7179/api/User/${customerId}`, formData); // Replace with actual endpoint
      alert('Vendor details updated successfully.');
      setIsEditMode(false); // Exit edit mode after saving
    } catch (error) {
      console.error('Error updating vendor details:', error);
    }
  };

  return (
    <>
      <Container className="mt--7" fluid>
        <Row>
          <Col className="order-xl-1" xl="12">
            <Card className="bg-secondary shadow mt-7">
              <CardHeader className="bg-white border-0">
                <Row className="align-items-center">
                  <Col xs="8">
                    <h3 className="mb-0">{isEditMode ? "Edit Vendor" : "Vendor Details"}</h3>
                  </Col>
                  <Col xs="4" className="text-right">
                    {!isEditMode && (
                      <Button
                        color="primary"
                        size="md"
                        onClick={() => setIsEditMode(true)} // Switch to edit mode
                      >
                        Edit
                      </Button>
                    )}
                  </Col>
                </Row>
              </CardHeader>
              <CardBody>
                <Form onSubmit={handleSave}>
                  <h6 className="heading-small text-muted mb-4">
                    Customer information
                  </h6>
                  <div className="pl-lg-4">
                    <Row>
                      <Col lg="6">
                        <FormGroup>
                          <label className="form-control-label" htmlFor="input-name">
                            Name
                          </label>
                          <Input
                            className="form-control-alternative"
                            id="input-name"
                            name="name"
                            placeholder="Vendor name"
                            type="text"
                            value={formData.name}
                            onChange={handleChange}
                            disabled={!isEditMode} // Disable input if not in edit mode
                          />
                        </FormGroup>
                      </Col>
                      <Col lg="6">
                        <FormGroup>
                          <label className="form-control-label" htmlFor="input-email">
                            Email address
                          </label>
                          <Input
                            className="form-control-alternative"
                            id="input-email"
                            name="email"
                            placeholder="Vendor email"
                            type="email"
                            value={formData.email}
                            onChange={handleChange}
                            disabled={!isEditMode} // Disable input if not in edit mode
                          />
                        </FormGroup>
                      </Col>
                    </Row>
                    <Row>
                      <Col lg="6">
                        <FormGroup>
                          <label className="form-control-label" htmlFor="input-phone">
                            Phone
                          </label>
                          <Input
                            className="form-control-alternative"
                            id="input-phone"
                            name="phone"
                            placeholder="Vendor phone"
                            type="text"
                            value={formData.phone}
                            onChange={handleChange}
                            disabled={!isEditMode} // Disable input if not in edit mode
                          />
                        </FormGroup>
                      </Col>
                    </Row>
                  </div>
                  <hr className="my-4" />
                  <h6 className="heading-small text-muted mb-4">
                    Contact information
                  </h6>
                  <div className="pl-lg-4">
                    <Row>
                      <Col md="12">
                        <FormGroup>
                          <label className="form-control-label" htmlFor="input-address">
                            Address
                          </label>
                          <Input
                            className="form-control-alternative"
                            id="input-address"
                            name="address.street"
                            placeholder="Street address"
                            type="text"
                            value={formData.address.street}
                            onChange={handleChange}
                            disabled={!isEditMode} // Disable input if not in edit mode
                          />
                        </FormGroup>
                      </Col>
                    </Row>
                    <Row>
                      <Col lg="4">
                        <FormGroup>
                          <label className="form-control-label" htmlFor="input-city">
                            City
                          </label>
                          <Input
                            className="form-control-alternative"
                            id="input-city"
                            name="address.city"
                            placeholder="City"
                            type="text"
                            value={formData.address.city}
                            onChange={handleChange}
                            disabled={!isEditMode} // Disable input if not in edit mode
                          />
                        </FormGroup>
                      </Col>
                      <Col lg="4">
                        <FormGroup>
                          <label className="form-control-label" htmlFor="input-country">
                            Country
                          </label>
                          <Input
                            className="form-control-alternative"
                            id="input-country"
                            name="address.country"
                            placeholder="Country"
                            type="text"
                            value={formData.address.country}
                            onChange={handleChange}
                            disabled={!isEditMode} // Disable input if not in edit mode
                          />
                        </FormGroup>
                      </Col>
                      <Col lg="4">
                        <FormGroup>
                          <label className="form-control-label" htmlFor="input-postal-code">
                            Postal code
                          </label>
                          <Input
                            className="form-control-alternative"
                            id="input-postal-code"
                            name="address.postalCode"
                            placeholder="Postal code"
                            type="text"
                            value={formData.address.postalCode}
                            onChange={handleChange}
                            disabled={!isEditMode} // Disable input if not in edit mode
                          />
                        </FormGroup>
                      </Col>
                    </Row>
                  </div>
                  {isEditMode && (
                    <Row className="mt-4">
                      <Col className="text-center">
                        <Button color="primary" size="md" type="submit">
                          Save
                        </Button>
                        <Button
                          color="secondary"
                          size="md"
                          onClick={() => setIsEditMode(false)} // Cancel editing
                        >
                          Cancel
                        </Button>
                      </Col>
                    </Row>
                  )}
                </Form>
              </CardBody>
            </Card>
          </Col>
        </Row>
      </Container>
    </>
  );
};

export default ViewCustomer;
