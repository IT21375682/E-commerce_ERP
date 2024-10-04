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
import { useParams } from "react-router-dom"; // Import useParams from React Router
import axios from "axios";
import { toast, ToastContainer } from 'react-toastify'; // Import toast components
import 'react-toastify/dist/ReactToastify.css'; // Import toastify CSS

const EditProduct = () => {
    const { id } = useParams(); // Get product ID from the URL
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
    const [imagePreview, setImagePreview] = useState(""); // State for image preview

    // Fetch the active categories and vendors when the component mounts
    useEffect(() => {
        const fetchActiveCategories = async () => {
            try {
                const response = await axios.get("https://localhost:5004/api/Category/active");
                setActiveCategories(response.data); // Set active categories
            } catch (error) {
                console.error("Error fetching active categories:", error);
            }
        };

        const fetchActiveVendors = async () => {
            try {
                const response = await axios.get("https://localhost:5004/api/User/active");
                const vendors = response.data.filter(user => user.role === "VENDOR");
                setActiveVendors(vendors); // Set only the vendors
            } catch (error) {
                console.error("Error fetching active vendors:", error);
            }
        };

        fetchActiveCategories();
        fetchActiveVendors();
    }, []);

    // Fetch the product details when the component mounts
    useEffect(() => {
        const fetchProduct = async () => {
            try {
                const response = await axios.get(`https://localhost:5004/api/Product/${id}`); // Fetch product data by ID
                setProduct(response.data); // Set the product state with the fetched data

                // Set the image preview if an image exists
                if (response.data.productImage) {
                    setImagePreview(`data:image/jpeg;base64,${response.data.productImage}`); // Update this based on your image type
                }
            } catch (error) {
                console.error("Error fetching product:", error);
            }
        };

        fetchProduct();
    }, [id]); // Only depend on the product ID

    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;
        if (type === "file") {
            const file = e.target.files[0];
            const reader = new FileReader();

            reader.onloadend = () => {
                const base64String = reader.result.replace(/^data:image\/(png|jpeg|jpg);base64,/, ""); // Remove the prefix
                setProduct((prevProduct) => ({
                    ...prevProduct,
                    productImage: base64String, // Store Base64 string
                }));
                setImagePreview(reader.result); // Update the preview state
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
            const response = await axios.put(`https://localhost:5004/api/Product/${id}`, product, {
                headers: {
                    'Content-Type': 'application/json', // Set the content type to JSON
                },
            });
            console.log(response.data); // Handle successful response
            toast.success('Product updated successfully!', {
                position: "top-right",
                autoClose: 3000, // Auto close after 3 seconds
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
            });
        } catch (error) {
            console.error("Error updating product:", error); // Handle error

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

    return (
        <Container className="mt-10" fluid>
            <Row>
                <Col className="order-xl-1" xl="12">
                    <Card className="bg-secondary shadow mt-7">
                        <CardHeader className="bg-white border-0">
                            <Row className="align-items-center">
                                <Col xs="8">
                                    <h3 className="mb-0">Edit Product</h3>
                                </Col>
                            </Row>
                        </CardHeader>
                        <CardBody>
                            <Form onSubmit={handleSubmit}>
                                <h6 className="heading-small text-muted mb-4">Product Information</h6>
                                <div className="pl-lg-4">
                                    <Row>
                                        <Col lg="6">
                                            <FormGroup>
                                                <label className="form-control-label" htmlFor="input-name">
                                                    Product Name
                                                </label>
                                                <Input
                                                    className="form-control-alternative"
                                                    id="input-name"
                                                    name="name"
                                                    placeholder="Product Name"
                                                    type="text"
                                                    value={product.name}
                                                    onChange={handleChange}
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
                                                    value={product.categoryId} // Set the currently selected category ID
                                                    onChange={handleChange}
                                                >
                                                    <option value="">Select Category</option>
                                                    {activeCategories.map((category) => (
                                                        <option key={category.id} value={category.id}>
                                                            {category.categoryName} {/* Display category name */}
                                                        </option>
                                                    ))}
                                                </Input>
                                            </FormGroup>
                                        </Col>
                                    </Row>
                                    <Row>
                                        <Col lg="6">
                                            <FormGroup>
                                                <label className="form-control-label" htmlFor="input-description">
                                                    Description
                                                </label>
                                                <Input
                                                    className="form-control-alternative"
                                                    id="input-description"
                                                    name="description"
                                                    placeholder="Product Description"
                                                    type="textarea"
                                                    value={product.description}
                                                    onChange={handleChange}
                                                />
                                            </FormGroup>
                                        </Col>
                                        <Col lg="6">
                                            <FormGroup>
                                                <label className="form-control-label" htmlFor="input-price">
                                                    Price
                                                </label>
                                                <Input
                                                    className="form-control-alternative"
                                                    id="input-price"
                                                    name="price"
                                                    placeholder="Price"
                                                    type="number"
                                                    value={product.price}
                                                    onChange={handleChange}
                                                />
                                            </FormGroup>
                                        </Col>
                                    </Row>
                                    <Row>
                                        <Col lg="6">
                                            <FormGroup>
                                                <label className="form-control-label" htmlFor="input-available-stock">
                                                    Available Stock
                                                </label>
                                                <Input
                                                    className="form-control-alternative"
                                                    id="input-available-stock"
                                                    name="availableStock"
                                                    placeholder="Available Stock"
                                                    type="number"
                                                    value={product.availableStock}
                                                    onChange={handleChange}
                                                />
                                            </FormGroup>
                                        </Col>
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
                                                    value={product.vendorId} // Set the currently selected vendor ID
                                                    onChange={handleChange}
                                                >
                                                    <option value="">Select Vendor</option>
                                                    {activeVendors.map((vendor) => (
                                                        <option key={vendor.id} value={vendor.id}>
                                                            {vendor.vendorName || vendor.name} {/* Display vendor name */}
                                                        </option>
                                                    ))}
                                                </Input>
                                            </FormGroup>

                                        </Col>
                                    </Row>
                                    <Row>
                                        {/* Column for the Choose File Button */}
                                        <Col lg="6">
                                            <FormGroup>
                                                <label className="form-control-label" htmlFor="input-image">
                                                    Product Image
                                                </label>
                                                <Input
                                                    className="form-control-alternative"
                                                    id="input-image"
                                                    name="productImage"
                                                    type="file"
                                                    onChange={handleChange} // The handler to handle image file input
                                                />
                                            </FormGroup>
                                        </Col>

                                        {/* Column for Image Preview */}
                                        <Col lg="6">
                                            {imagePreview && ( // Display the image preview if it exists
                                                <img
                                                    src={imagePreview}
                                                    alt="Image Preview"
                                                    style={{ width: '100%', height: 'auto', marginTop: '10px' }}
                                                />
                                            )}
                                        </Col>
                                    </Row>

                                </div>
                                <hr className="my-4" />
                                <Row>
                                    <Col className="text-center">
                                        <Button color="primary" type="submit" size="md" onClick={handleSubmit}>
                                            Save
                                        </Button>
                                       
                                        <Button color="danger" onClick={() => {/* Handle cancel logic here */ }} size="md">
                                            Cancel
                                        </Button>
                                         {/* Toast container */}
                                         <ToastContainer />
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

export default EditProduct;
