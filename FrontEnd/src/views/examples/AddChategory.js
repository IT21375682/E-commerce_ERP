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
import { toast, ToastContainer } from 'react-toastify'; // Import toast components
import 'react-toastify/dist/ReactToastify.css'; // Import toastify CSS

const AddCategory = () => {
  const [category, setCategory] = useState({
    categoryName: "",
    isActive: true,
  });

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setCategory((prevCategory) => ({
      ...prevCategory,
      [name]: type === "checkbox" ? checked : value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault(); // Prevent the default form submission behavior
    try {
      const response = await axios.post('https://localhost:5004/api/Category', category, {
        headers: {
          'Content-Type': 'application/json', // Set the content type to JSON
        },
      });
      console.log(response.data); // Handle successful response
      toast.success('Category added successfully!', {
        position: "top-right",
        autoClose: 3000, // Auto close after 3 seconds
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
      });
      // Optionally reset the form
      setCategory({ categoryName: "", isActive: false });
    } catch (error) {
      console.error("Error creating category:", error);
      toast.error('Error adding category. Please try again.', {
        position: "top-right",
        autoClose: 3000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
      });
    }
  };

  return (
    <Container className="mt-10" fluid>
      {/* Toast container */}
      <ToastContainer />
      <Row>
        <Col className="order-xl-1" xl="12">
          <Card className="bg-secondary shadow mt-7">
            <CardHeader className="bg-white border-0">
              <Row className="align-items-center">
                <Col xs="8">
                  <h3 className="mb-0">Add New Category</h3>
                </Col>
              </Row>
            </CardHeader>
            <CardBody>
              <Form onSubmit={handleSubmit}>
                <h6 className="heading-small text-muted mb-4">
                  Category Information
                </h6>
                <div className="pl-lg-4">
                  <Row>
                    <Col lg="6">
                      <FormGroup>
                        <label
                          className="form-control-label"
                          htmlFor="input-category-name"
                        >
                          Category Name
                        </label>
                        <Input
                          className="form-control-alternative"
                          id="input-category-name"
                          name="categoryName"
                          placeholder="Category Name"
                          type="text"
                          value={category.categoryName} // Bind input value to state
                          onChange={handleChange} // Handle input change
                        />
                      </FormGroup>
                     </Col>
                  </Row>
                  {/* <Row>
                    <Col lg="6">
                      <FormGroup>
                        <label className="form-control-label" htmlFor="input-is-active">
                          Is Active
                        </label>
                        <div>
                          <Input
                            id="input-is-active"
                            name="isActive"
                            type="checkbox"
                            checked={category.isActive} // Bind checkbox to state
                            onChange={handleChange} // Handle checkbox change
                          />{" "}
                          <label htmlFor="input-is-active">Active Category</label>
                        </div>
                      </FormGroup>
                    </Col> 
                  </Row>*/}
                </div> 
                <hr className="my-4" />
                <Row>
                  <Col className="text-center">
                    <Button color="primary" type="submit" size="md">
                      Save
                    </Button>
                    <Button color="danger" onClick={() => setCategory({ categoryName: "", isActive: false })} size="md">
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
  );
};

export default AddCategory;
