import {
  Badge,
  Card,
  CardHeader,
  Table,
  Container,
  Row,
  Button
} from "reactstrap";
import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { toast, ToastContainer } from 'react-toastify'; // Import toast components
import 'react-toastify/dist/ReactToastify.css'; // Import toastify CSS

const ViewProduct = () => {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [view, setView] = useState("activeProducts");  // State to manage the selected view
  const navigate = useNavigate();

  // Function to fetch products based on the selected view
  const fetchProducts = async (view) => {
    setLoading(true); // Start loading

    let apiUrl;
    switch (view) {
      case "activeProducts":
        apiUrl = 'https://localhost:5004/api/Product/active';
        break;
      case "deactivatedProducts":
        apiUrl = 'https://localhost:5004/api/Product/deactivated';
        break;
      case "allProducts":
      default:
        apiUrl = 'https://localhost:5004/api/Product';
        break;
    }

    try {
      const response = await axios.get(apiUrl); // Fetch products based on the selected view
      setProducts(response.data);
      setLoading(false);
    } catch (error) {
      console.error('Error fetching products:', error);
      setLoading(false);
    }
  };
  const toggleProductStatusus = async (productId) => {
    const apiUrl = `https://localhost:5004/api/Product/${productId}/toggle-active`; // Toggle Active API
  
    try {
      await axios.patch(apiUrl); // Send PUT request to toggle active status
      fetchProducts(view); // Refresh the product list after status change
    } catch (error) {
      console.error('Error toggling product status:', error);
    }
  };
  // Function to handle the activation/deactivation of a product
  const toggleProductStatus = async (productId, isActive) => {
    const apiUrl = isActive
      ? `https://localhost:5004/api/Product/deactivate/${productId}` // Deactivate API
      : `https://localhost:5004/api/Product/activate/${productId}`;  // Activate API

    try {
      await axios.put(apiUrl); // Call the appropriate API
      fetchProducts(view); // Refresh the product list after status change
    } catch (error) {
      console.error('Error changing product status:', error);
    }
  };

  // Function to handle the deletion of a product
  const deleteProduct = async (productId) => {
    const apiUrl = `https://localhost:5004/api/Product/${productId}`; // Delete API URL

    try {
      await axios.delete(apiUrl); // Send DELETE request to the API
      // Remove the product from the state after successful deletion
      setProducts(products.filter(product => product.id !== productId));
      toast.success('Product deleted successfully!', {
        position: "top-right",
        autoClose: 3000, // Auto close after 3 seconds
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
    });
    } catch (error) {
      console.error('Error deleting product:', error);
      toast.error('Error deleting product. Please try again.', {
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

  // Fetch products when the component mounts or when the view changes
  useEffect(() => {
    fetchProducts(view);
  }, [view]);

  return (
    <Container className="mt-10" fluid>
       {/* Toast container */}
       <ToastContainer />
      <Row>
        <div className="col mt-7">
          <Card className="shadow">
            <CardHeader className="border-0">
              <h3 className="mb-0">Products</h3>
            </CardHeader>
            <Row className="mb-3 align-items-center">
              <div className="col">
                <div className="d-flex justify-content-center">
                  <Button
                    color={view === "allProducts" ? "secondary" : "info"}
                    onClick={() => setView("allProducts")}
                    className="mr-2"
                  >
                    All Products
                  </Button>
                  <Button
                    color={view === "activeProducts" ? "secondary" : "primary"}
                    onClick={() => setView("activeProducts")}
                    className="mr-2"
                  >
                    Active Products
                  </Button>
                  <Button
                    color={view === "deactivatedProducts" ? "secondary" : "warning"}
                    onClick={() => setView("deactivatedProducts")}
                    className="mr-2"
                  >
                    Deactivated Products
                  </Button>
                </div>
              </div>
              <div className="col-auto p-4">
                <Button color="primary" size="md" onClick={() => window.location.href = "/admin/add-product"}>
                  <i className="fa fa-plus mr-1"></i> Add Product
                </Button>
              </div>
            </Row>

            <Table className="align-items-center table-flush" responsive>
              <thead className="thead-light">
                <tr>
                  <th scope="col">Image</th>
                  <th scope="col">Name</th>
                  <th scope="col">Price</th>
                  <th scope="col">Category</th>
                  <th scope="col">IsActive</th>
                  <th scope="col">Edit</th>
                  <th scope="col">Delete</th>
                  <th scope="col">Status</th>
                </tr>
              </thead>
              <tbody>
                {loading ? (
                  <tr>
                    <td colSpan="7" className="text-center">Loading...</td>
                  </tr>
                ) : (
                  products.length > 0 ? (
                    products.map((product) => (
                      <tr key={product.id}>
                        <td>
                          {product.productImage ? (
                            <img
                              src={`data:image/jpeg;base64,${product.productImage}`}
                              alt={product.name}
                              style={{ width: '50px', height: '50px', objectFit: 'cover' }}
                              loading="lazy"
                            />
                          ) : (
                            'No Image'
                          )}
                        </td>
                        <td>{product.name}</td>
                        <td>{product.price}</td>
                        <td>{product.categoryName || 'N/A'}</td>
                        <td>
                          <Badge color={product.isActive ? "success" : "danger"}>
                            {product.isActive ? "Active" : "Inactive"}
                          </Badge>
                        </td>
                        <td>
                          <Button
                            color="primary"
                            size="md"
                            onClick={() => navigate(`/admin/edit-product/${product.id}`)}
                          >
                            Edit
                          </Button>
                        </td>
                        <td>
                          <Button
                            color="danger"
                            size="md"
                            onClick={() => deleteProduct(product.id)} // Call the deleteProduct function
                          >
                            Delete
                          </Button>
                        </td>
                        <td>
                          <Button
                            color={product.isActive ? "warning" : "success"}
                            size="md"
                            onClick={() => toggleProductStatusus(product.id, product.isActive)}
                          >
                            {product.isActive ? "Deactivate" : "Activate"}
                          </Button>
                        </td>
                      </tr>
                    ))
                  ) : (
                    <tr>
                      <td colSpan="7" className="text-center">No products found</td>
                    </tr>
                  )
                )}
              </tbody>
            </Table>
          </Card>
        </div>
      </Row>
    </Container>
  );
};

export default ViewProduct;
