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

const ViewCategory = () => {
    const [categories, setCategories] = useState([]); // Updated state for categories
    const [loading, setLoading] = useState(true);
    const [view, setView] = useState("activeCategories");  // State to manage the selected view
    const navigate = useNavigate();

    // Function to fetch categories based on the selected view
    const fetchCategories = async (view) => {
        setLoading(true); // Start loading

        let apiUrl;
        switch (view) {
            case "activeCategories":
                apiUrl = 'http://192.168.8.102:5004/api/Category/active'; // API for active categories
                break;
            case "deactivatedCategories":
                apiUrl = 'http://192.168.8.102:5004/api/Category/deactive'; // API for deactivated categories
                break;
            case "allCategories":
            default:
                apiUrl = 'http://192.168.8.102:5004/api/Category'; // API for all categories
                break;
        }

        try {
            const response = await axios.get(apiUrl); // Fetch categories based on the selected view
            setCategories(response.data);
            setLoading(false);
        } catch (error) {
            console.error('Error fetching categories:', error);
            setLoading(false);
        }
    };

    // Function to toggle category status
    const toggleCategoryStatus = async (categoryId, isActive) => {
        const apiUrl = isActive
            ? `http://192.168.8.102:5004/api/Category/deactive/${categoryId}` // Deactivate API
            : `http://192.168.8.102:5004/api/Category/activate/${categoryId}`;  // Activate API

        try {
            await axios.put(apiUrl); // Call the appropriate API
            fetchCategories(view); // Refresh the category list after status change
        } catch (error) {
            console.error('Error changing category status:', error);
        }
    };

    // Function to handle the deletion of a category
    const deleteCategory = async (categoryId) => {
        const apiUrl = `http://192.168.8.102:5004/api/Category/${categoryId}`; // Delete API URL

        try {
            await axios.delete(apiUrl); // Send DELETE request to the API
            // Remove the category from the state after successful deletion
            setCategories(categories.filter(category => category.id !== categoryId));
            toast.success('Category deleted successfully!', {
                position: "top-right",
                autoClose: 3000, // Auto close after 3 seconds
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
            });
        } catch (error) {
            console.error('Error deleting category:', error);
            toast.error('Error deleting category. Please try again.', {
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

    // Fetch categories when the component mounts or when the view changes
    useEffect(() => {
        fetchCategories(view);
    }, [view]);

    return (
        <Container className="mt-10" fluid>
            {/* Toast container */}
            <ToastContainer />
            <Row>
                <div className="col mt-7">
                    <Card className="shadow">
                        <CardHeader className="border-0">
                            <h3 className="mb-0">Categories</h3>
                        </CardHeader>
                        <Row className="mb-3 align-items-center">
                            <div className="col">
                                <div className="d-flex justify-content-center">
                                    <Button
                                        color={view === "allCategories" ? "secondary" : "info"}
                                        onClick={() => setView("allCategories")}
                                        className="mr-2"
                                    >
                                        All Categories
                                    </Button>
                                    <Button
                                        color={view === "activeCategories" ? "secondary" : "primary"}
                                        onClick={() => setView("activeCategories")}
                                        className="mr-2"
                                    >
                                        Active Categories
                                    </Button>
                                    <Button
                                        color={view === "deactivatedCategories" ? "secondary" : "warning"}
                                        onClick={() => setView("deactivatedCategories")}
                                        className="mr-2"
                                    >
                                        Deactivated Categories
                                    </Button>
                                </div>
                            </div>
                            <div className="col-auto p-4">
                                <Button color="primary" size="md" onClick={() => window.location.href = "/admin/add-category"}>
                                    <i className="fa fa-plus mr-1"></i> Add Category
                                </Button>
                            </div>
                        </Row>

                        <Table className="align-items-center table-flush" responsive>
                            <thead className="thead-light">
                                <tr>
                                    <th scope="col">ID</th>
                                    <th scope="col">Category Name</th>
                                    <th scope="col">IsActive</th>
                                    <th scope="col">Created At</th>
                                    <th scope="col">Updated At</th>
                                    <th scope="col">Edit</th>
                                    <th scope="col">Delete</th>
                                    <th scope="col">Status</th>
                                </tr>
                            </thead>
                            <tbody>
                                {loading ? (
                                    <tr>
                                        <td colSpan="8" className="text-center">Loading...</td>
                                    </tr>
                                ) : (
                                    categories.length > 0 ? (
                                        categories.map((category) => (
                                            <tr key={category.id}>
                                                <td>{category.id}</td>
                                                <td>{category.categoryName}</td>
                                                <td>
                                                    <Badge color={category.isActive ? "success" : "danger"}>
                                                        {category.isActive ? "Active" : "Inactive"}
                                                    </Badge>
                                                </td>
                                                <td>{new Date(category.createdAt).toLocaleDateString()}</td>
                                                <td>{new Date(category.updatedAt).toLocaleDateString()}</td>
                                                <td>
                                                    <Button
                                                        color="primary"
                                                        size="md"
                                                        onClick={() => navigate(`/admin/edit-category/${category.id}`)}
                                                    >
                                                        Edit
                                                    </Button>
                                                </td>
                                                <td>
                                                    <Button
                                                        color="danger"
                                                        size="md"
                                                        onClick={() => deleteCategory(category.id)} // Call the deleteCategory function
                                                    >
                                                        Delete
                                                    </Button>
                                                </td>
                                                <td>
                                                    <Button
                                                        color={category.isActive ? "warning" : "success"}
                                                        size="md"
                                                        onClick={() => toggleCategoryStatus(category.id, category.isActive)}
                                                    >
                                                        {category.isActive ? "Deactivate" : "Activate"}
                                                    </Button>
                                                </td>
                                            </tr>
                                        ))
                                    ) : (
                                        <tr>
                                            <td colSpan="8" className="text-center">No categories found</td>
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

export default ViewCategory;
