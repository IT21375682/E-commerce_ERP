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
import React, { useState } from "react";
import axios from "axios";

const AddVendor = () => {
  // Set state for user details
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
    role: 'VENDOR', // Default to VENDOR since this form is for adding vendors
    isActive: true, // Default active status
    password: '', // Placeholder for password
  });

  // Handle form input change
  const handleChange = (e) => {
    const { name, value } = e.target;
    
    // Check if it's a nested address field
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

  // Submit the form data
  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      // Send form data to backend API (assuming there's an endpoint to add users)
      const response = await axios.post('https://localhost:5004/api/User', formData);
      alert('Vendor added successfully:', response.data);
      // Handle success (e.g., show notification or redirect)
    } catch (error) {
      console.error('Error adding vendor:', error);
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
                    <h3 className="mb-0">Add Vendor</h3>
                  </Col>
                </Row>
              </CardHeader>
              <CardBody>
                <Form onSubmit={handleSubmit}>
                  <h6 className="heading-small text-muted mb-4">
                    Vendor information
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
                          />
                        </FormGroup>
                      </Col>
                      <Col lg="6">
                        <FormGroup>
                          <label className="form-control-label" htmlFor="input-password">
                            Password
                          </label>
                          <Input
                            className="form-control-alternative"
                            id="input-password"
                            name="password"
                            placeholder="Vendor password"
                            type="password"
                            value={formData.password}
                            onChange={handleChange}
                          />
                        </FormGroup>
                      </Col>
                    </Row>
                  </div>
                  <hr className="my-4" />
                  {/* Address */}
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
                          />
                        </FormGroup>
                      </Col>
                    </Row>
                  </div>
                  <hr className="my-4" />
                  <Row>
                    <Col className="text-center">
                      <Button color="primary" size="md" type="submit">
                        Save
                      </Button>
                      <Button
                        color="danger"
                        size="md"
                        onClick={(e) => e.preventDefault()}
                      >
                        Cancel
                      </Button>
                    </Col>
                  </Row>
                </Form>
              </CardBody>
            </Card>
          </Col>
        </Row>
      </Container>
    </>
  );
};

export default AddVendor;
