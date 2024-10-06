import {
    Badge,
    Card,
    CardHeader,
    Table,
    Container,
    Row,
    Button,
    Input
} from "reactstrap";
import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { toast, ToastContainer } from 'react-toastify'; // Import toast components
import 'react-toastify/dist/ReactToastify.css'; // Import toastify CSS
import { jwtDecode } from "jwt-decode";


const InventoryManagement = () => {
    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [vendorId, setVendorId] = useState(jwtDecode(localStorage.getItem("token")).Id);
    const [role, setRole] = useState();
    const [view, setView] = useState("activeProducts");  // State to manage the selected view
    const navigate = useNavigate();
    const [categories, setCategories] = useState([]);
    const [stockInputs, setStockInputs] = useState({}); // New state to store stock input values

    // Function to handle the deletion of a product
    const addstock = async (productId) => {
        const apiUrl = `https://localhost:5004/api/Product/${productId}/stock`;
        const stockAmount = parseInt(stockInputs[productId], 10);

        if (!stockAmount || stockAmount <= 0) {
            toast.error('Please enter a valid stock quantity.', {
                position: "top-right",
                autoClose: 3000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
            });
            return;
        }

        try {
            console.log("Stock Amount:", stockAmount);
            await axios.put(apiUrl, JSON.stringify(stockAmount), { headers: { 'Content-Type': 'application/json' } });

            setProducts(prevProducts =>
                prevProducts.map(product =>
                    product.id === productId
                        ? { ...product, availableStock: product.availableStock + stockAmount }
                        : product
                )
            );

            toast.success('Product stock added successfully!', {
                position: "top-right",
                autoClose: 3000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
            });
        } catch (error) {
            console.error('Error adding product stock:', error);
            if (error.response) {
                console.error('Server Response:', error.response.data);
                console.error('Error Details:', error.response.data.errors);
            }
            toast.error('Error adding product stock. Please try again.', {
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


    const removestock = async (productId) => {
        const apiUrl = `https://localhost:5004/api/Product/${productId}/stock`;
        const stockAmount = parseInt(stockInputs[productId], 10);

        if (!stockAmount || stockAmount <= 0) {
            toast.error('Please enter a valid stock quantity.', {
                position: "top-right",
                autoClose: 3000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
            });
            return;
        }

        try {
            console.log("Stock Amount:", stockAmount);
            await axios.request({
                url: apiUrl,
                method: 'DELETE',
                data: JSON.stringify(stockAmount),
                headers: { 'Content-Type': 'application/json' }
            });

            setProducts(prevProducts =>
                prevProducts.map(product =>
                    product.id === productId
                        ? { ...product, availableStock: product.availableStock - stockAmount }
                        : product
                )
            );

            toast.success('Product stock removed successfully!', {
                position: "top-right",
                autoClose: 3000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
            });
        } catch (error) {
            console.error('Error removing product stock:', error);
            if (error.response) {
                console.error('Server Response:', error.response.data);
                console.error('Error Details:', error.response.data.errors);
            }
            toast.error('Error removing product stock. Please try again.', {
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

    const handleStockInputChange = (productId, value) => {
        setStockInputs(prev => ({ ...prev, [productId]: value }));
    };


    useEffect(() => {

        const token = localStorage.getItem("token"); // Retrieve the token from localStorage
        if (token) {
            try {
                const decodedToken = jwtDecode(token);
                setRole(decodedToken.role);
                setVendorId(decodedToken.id);
                console.log("Decoded Token:", decodedToken);
                console.log("Vendor ID : " + decodedToken.id)
                console.log("Role : " + role)

            } catch (error) {
                console.error("Failed to decode token", error);
            }
        }
    }, []);

    // Function to fetch products based on the selected view
    const fetchProducts = async (view) => {
        setLoading(true); // Start loading

        let apiUrl = 'https://localhost:5004/api/Product';


        try {
            const response = await axios.get(apiUrl); // Fetch products based on the selected view
            let fetchedProducts = response.data;

            const token = localStorage.getItem("token");
            const decodedToken = jwtDecode(token);

            if (decodedToken.role === "VENDOR") {
                console.log("VENDOR role detected");
                console.log("Vendor ID from token:", decodedToken.id);

                // Apply filter to fetchedProducts based on vendorId
                fetchedProducts = fetchedProducts.filter(product => product.vendorId === decodedToken.id);

                // Log all vendor IDs in the filtered products
                console.log("Filtered Product Vendor IDs:");
                fetchedProducts.forEach(product => {
                    console.log(product.vendorId);
                });
            }

            // Set the filtered products in state
            setProducts(fetchedProducts);
            setLoading(false);
        } catch (error) {
            console.error('Error fetching products:', error);
            setLoading(false);
        }
    };


    // Function to fetch categories
    const fetchCategories = async () => {
        try {
            const response = await axios.get('https://localhost:5004/api/Category'); // API call to fetch categories
            setCategories(response.data); // Set categories in state
        } catch (error) {
            console.error('Error fetching categories:', error);
        }
    };

    // Fetch products when the component mounts or when the view changes
    useEffect(() => {
        fetchProducts(view);
        fetchCategories();
    }, [view]);

    return (
        <Container className="mt-10" fluid>
            {/* Toast container */}
            <ToastContainer />
            <Row>
                <div className="col mt-7">
                    <Card className="shadow">
                        <CardHeader className="border-0">
                            <h3 className="mb-0">Inventories</h3>
                        </CardHeader>
                        <Table className="align-items-center table-flush" responsive>
                            <thead className="thead-light">
                                <tr>
                                    <th scope="col">Image</th>
                                    <th scope="col">Name</th>
                                    <th scope="col">Stock</th>
                                    <th scope="col">Category</th>
                                    <th scope="col">IsActive</th>
                                    <th scope="col">Add Stock</th>
                                    <th scope="col">Remove stock</th>
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
                                                <td>{product.availableStock}</td>
                                                <td>
                                                    {
                                                        // Find the category name based on categoryId
                                                        categories.find(category => category.id === product.categoryId)?.categoryName || 'N/A'
                                                    }
                                                </td>
                                                <td>
                                                    <Badge color={product.isActive ? "success" : "danger"}>
                                                        {product.isActive ? "Active" : "Inactive"}
                                                    </Badge>
                                                </td>
                                                <td>
                                                    <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                                                        <Input
                                                            type="number"
                                                            min="1"
                                                            placeholder="Qty"
                                                            value={stockInputs[product.id] || ''}
                                                            onChange={(e) => handleStockInputChange(product.id, e.target.value)}
                                                            style={{ width: '80px' }}
                                                        />
                                                        <Button
                                                            color="success"
                                                            size="md"
                                                            onClick={() => addstock(product.id)}
                                                        >
                                                            Add Stock
                                                        </Button>
                                                    </div>
                                                </td>

                                                <td>
                                                    <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                                                        <Input
                                                            type="number"
                                                            min="1"
                                                            placeholder="Qty"
                                                            value={stockInputs[product.id] || ''}
                                                            onChange={(e) => handleStockInputChange(product.id, e.target.value)}
                                                            style={{ width: '80px' }}
                                                        />
                                                        <Button
                                                            color="danger"
                                                            size="md"
                                                            onClick={() => removestock(product.id)}
                                                        >
                                                            Remove Stock
                                                        </Button>
                                                    </div>
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

export default InventoryManagement;
