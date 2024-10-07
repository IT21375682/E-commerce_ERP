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
import { toast, ToastContainer } from 'react-toastify'; // Import toast components
import 'react-toastify/dist/ReactToastify.css'; // Import toastify CSS
import { useNavigate } from 'react-router-dom';


const AddProduct = () => {
  const [product, setProduct] = useState({
    name: "",
    productImage: "",
    categoryId: "",
    description: "",
    price: 0,
    availableStock: 0,
    isActive: false,
    vendorId: "",
  });

  const [activeCategories, setActiveCategories] = useState([]);
  const [activeVendors, setActiveVendors] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchActiveCategories = async () => {
      try {
        const response = await axios.get("https://localhost:5004/api/Category/active");
        setActiveCategories(response.data); // Assuming the API returns an array of categories
      //   toast.success('Product Added successfully!', {
      //     position: "top-right",
      //     autoClose: 3000, // Auto close after 3 seconds
      //     hideProgressBar: false,
      //     closeOnClick: true,
      //     pauseOnHover: true,
      //     draggable: true,
      //     progress: undefined,
      // });
      } catch (error) {
        console.error("Error fetching active categories:", error);
        toast.error('Error updating product. Please try again.', {
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
    const fetchActiveVendors = async () => {
      try {
        const response = await axios.get("https://localhost:5004/api/User/active");
        // console.log(response.data); // Log the API response to see what data is returned

        const vendors = response.data.filter(user => user.role === "VENDOR");
        setActiveVendors(vendors); // Set only the vendors
      } catch (error) {
        console.error("Error fetching active vendors:", error);
      }
    };

    fetchActiveCategories();
    fetchActiveVendors();
  }, []);

  // Log activeVendors whenever it changes
  useEffect(() => {
    console.log(activeVendors); // Log the updated state
  }, [activeVendors]);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    if (type === "file") {
      const file = e.target.files[0];
      const reader = new FileReader();

      reader.onloadend = () => {
        const base64String = reader.result
          .replace(/^data:image\/(png|jpeg|jpg);base64,/, ""); // Remove the prefix

        setProduct((prevProduct) => ({
          ...prevProduct,
          productImage: base64String, // Store Base64 string
        }));
      };

      if (file) {
        reader.readAsDataURL(file); // Convert file to Base64 string
      }
    } else {
      setProduct((prevProduct) => ({
        ...prevProduct,
        [name]: type === "checkbox" ? checked : value,
      }));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault(); // Prevent the default form submission behavior
    try {
      const response = await axios.post('https://localhost:5004/api/Product', product, {
        headers: {
          'Content-Type': 'application/json', // Set the content type to JSON
        },
      });
      console.log(response.data); // Handle successful response
      toast.success('Product Added successfully!', {
            position: "top-right",
            autoClose: 3000, // Auto close after 3 seconds
            hideProgressBar: false,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
        });
      // Optionally reset the form or redirect the user
    } catch (error) {
      console.error("Error creating product:", error); // Handle error
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
                    <h3 className="mb-0">Add New Product</h3>
                  </Col>
                </Row>
              </CardHeader>
              <CardBody>
                <Form onSubmit={handleSubmit}>
                  <h6 className="heading-small text-muted mb-4">
                    Product Information
                  </h6>
                  <div className="pl-lg-4">
                    <Row>
                      <Col lg="6">
                        <FormGroup>
                          <label
                            className="form-control-label"
                            htmlFor="input-name"
                          >
                            Product Name
                          </label>
                          <Input
                            className="form-control-alternative"
                            id="input-name"
                            name="name" // Set the name to correspond with state
                            placeholder="Product Name"
                            type="text"
                            value={product.name} // Bind input value to state
                            onChange={handleChange} // Handle input change
                          />
                        </FormGroup>
                      </Col>
                      <Col lg="6">
                        <FormGroup>
                          <label className="form-control-label" htmlFor="input-category-id">
                            Category
                          </label>
                          <Input
                            className="form-control-alternative"
                            id="input-category-id"
                            name="categoryId"
                            type="select"
                            value={product.categoryId}
                            onChange={handleChange}
                          >
                            <option value="">Select Category</option>
                            {activeCategories.map((category) => (
                              <option key={category.id} value={category.id}>
                                {category.categoryName}
                              </option>
                            ))}
                          </Input>
                        </FormGroup>
                      </Col>
                    </Row>
                    <Row>
                      <Col lg="6">
                        <FormGroup>
                          <label
                            className="form-control-label"
                            htmlFor="input-description"
                          >
                            Description
                          </label>
                          <Input
                            className="form-control-alternative"
                            id="input-description"
                            name="description" // Set the name to correspond with state
                            placeholder="Product Description"
                            type="textarea"
                            value={product.description} // Bind input value to state
                            onChange={handleChange} // Handle input change
                          />
                        </FormGroup>
                      </Col>
                      <Col lg="6">
                        <FormGroup>
                          <label
                            className="form-control-label"
                            htmlFor="input-price"
                          >
                            Price
                          </label>
                          <Input
                            className="form-control-alternative"
                            id="input-price"
                            name="price" // Set the name to correspond with state
                            placeholder="Price"
                            type="number"
                            value={product.price} // Bind input value to state
                            onChange={handleChange} // Handle input change
                          />
                        </FormGroup>
                      </Col>
                    </Row>
                    <Row>
                      <Col lg="6">
                        <FormGroup>
                          <label
                            className="form-control-label"
                            htmlFor="input-available-stock"
                          >
                            Available Stock
                          </label>
                          <Input
                            className="form-control-alternative"
                            id="input-available-stock"
                            name="availableStock" // Set the name to correspond with state
                            placeholder="Available Stock"
                            type="number"
                            value={product.availableStock} // Bind input value to state
                            onChange={handleChange} // Handle input change
                          />
                        </FormGroup>
                      </Col>
                      <Col lg="6">
                        <FormGroup>
                          <label
                            className="form-control-label"
                            htmlFor="input-image"
                          >
                            Product Image
                          </label>
                          <Input
                            className="form-control-alternative"
                            id="input-image"
                            name="productImage" // Set the name to correspond with state
                            type="file"
                            onChange={handleChange} // Handle input change
                          />
                        </FormGroup>
                      </Col>
                    </Row>
                    <Row>
                      <Col lg="6">

                        <FormGroup>
                          <label className="form-control-label" htmlFor="input-vendor-id">
                            Vendor
                          </label>
                          <Input
                            className="form-control-alternative"
                            id="input-vendor-id"
                            name="vendorId"
                            type="select"
                            value={product.vendorId}
                            onChange={handleChange} // Make sure this updates the state correctly
                          >
                            <option value="">Select Vendor</option>
                            {activeVendors.map((vendor) => (
                              <option key={vendor.id} value={vendor.id}>
                                {vendor.vendorName || vendor.name} {/* Check correct property */}
                              </option>
                            ))}
                          </Input>
                        </FormGroup>
                      </Col>
                      <Col lg="6">
                      </Col>
                    </Row>
                  </div>
                  <hr className="my-4" />
                  <Row>
                    <Col className="text-center">
                      <Button color="primary" type="submit" size="md">
                        Save
                      </Button>
                      <Button color="danger" onClick={() => navigate('/admin/view-product')} size="md">
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

export default AddProduct;
