import {
    Card,
    CardHeader,
    Table,
    Container,
    Row,
} from "reactstrap";
import React, { useState, useEffect } from "react";
import axios from "axios";
import { toast, ToastContainer } from 'react-toastify'; // Import toast components
import 'react-toastify/dist/ReactToastify.css'; // Import toastify CSS

const ViewOrder = () => {
    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(true);
    const [users, setUsers] = useState([]);

    // Function to fetch orders and return the data
    // const fetchOrders = async () => {
    //     setLoading(true);
    //     const apiUrl = 'https://localhost:5004/api/Order';
    //     try {
    //         const response = await axios.get(apiUrl);
    //         setLoading(false);
    //         return response.data;  // Return orders data

    //     } catch (error) {
    //         console.error('Error fetching orders:', error);
    //         setLoading(false);
    //         return []; // Return empty array in case of error
    //     }
    // };

    // // Function to fetch users and return the data
    // const fetchUsers = async () => {
    //     try {
    //         const response = await axios.get("https://localhost:5004/api/User");
    //         return response.data;  // Return users data
    //     } catch (error) {
    //         console.error("Error fetching users:", error);
    //         return []; // Return empty array in case of error
    //     }
    // };

    // // Merge orders with users based on userId
    // const mergeOrdersWithUsers = (orders, users) => {
    //     return orders.map(order => {
    //         const user = users.find(u => u.id === order.userId);
    //         return { ...order, userName: user ? user.name : 'Unknown' };
    //     });
    // };

    // useEffect(() => {
    //     const fetchData = async () => {
    //         setLoading(true);
    //         // Fetch both orders and users concurrently
    //         const [ordersData, usersData] = await Promise.all([fetchOrders(), fetchUsers()]);
    
    //         // Log the orders data before merging
    //         console.log('Fetched Orders Data:', ordersData);
            
    //         // Merge the fetched data
    //         const mergedOrders = mergeOrdersWithUsers(ordersData, usersData);
    //         setOrders(mergedOrders);
    //         setLoading(false);
    //     };
    
    //     fetchData();
    // }, []);

    // // Function to format the status object into a readable string
    // const formatStatus = (status) => {
    //     if (!status) return 'Unknown';

    //     if (status.delivered) return `Delivered`;
    //     if (status.dispatched) return `Dispatched`;
    //     if (status.processing) return `Processing`;
    //     if (status.pending) return `Pending`;
    //     if (status.canceled) return `Canceled`;

    //     return 'Status unknown';
    // };
    const fetchOrders = async () => {
        setLoading(true);
        const apiUrl = 'https://localhost:5004/api/Order';
        try {
            const response = await axios.get(apiUrl);
            setLoading(false);
            return response.data;  // Return orders data
        } catch (error) {
            console.error('Error fetching orders:', error);
            setLoading(false);
            return []; // Return empty array in case of error
        }
    };

    const fetchUsers = async () => {
        try {
            const response = await axios.get("https://localhost:5004/api/User");
            return response.data;  // Return users data
        } catch (error) {
            console.error("Error fetching users:", error);
            return []; // Return empty array in case of error
        }
    };

    const mergeOrdersWithUsers = (orders, users) => {
        return orders.map(order => {
            const user = users.find(u => u.id === order.userId);
            return { ...order, userName: user ? user.name : 'Unknown' };
        });
    };

    useEffect(() => {
        const fetchData = async () => {
            setLoading(true);
            const [ordersData, usersData] = await Promise.all([fetchOrders(), fetchUsers()]);

            // Log the fetched orders before merging
            console.log('Fetched Orders Data:', JSON.stringify(ordersData, null, 2));

            const mergedOrders = mergeOrdersWithUsers(ordersData, usersData);
            setOrders(mergedOrders);
            console.log('Merged Orders:', mergedOrders); // Log merged orders structure
            setLoading(false);
        };

        fetchData();
    }, []);

    const formatStatus = (status) => {
        if (!status) return 'Unknown';
        if (status.delivered) return `Delivered`;
        if (status.dispatched) return `Dispatched`;
        if (status.processing) return `Processing`;
        if (status.pending) return `Pending`;
        if (status.canceled) return `Canceled`;
        return 'Status unknown';
    };

    return (
        <Container className="mt-10" fluid>
            <ToastContainer />
            <Row>
                <div className="col mt-7">
                    <Card className="shadow">
                        <CardHeader className="border-0">
                            <h3 className="mb-0">Orders</h3>
                        </CardHeader>

                        <Table className="align-items-center table-flush" responsive>
                            <thead className="thead-light">
                                <tr>
                                    <th scope="col">Order ID</th>
                                    <th scope="col">Customer Name</th>
                                    <th scope="col">Total Price</th>
                                    <th scope="col">Status</th>
                                    <th scope="col">Order Date</th>
                                </tr>
                            </thead>
                            <tbody>
                                {loading ? (
                                    <tr>
                                        <td colSpan="5" className="text-center">Loading...</td>
                                    </tr>
                                ) : (
                                    orders.length > 0 ? (
                                        orders.map((order) => (
                                            <tr key={order.id}>
                                                <td>{order.id}</td>
                                                <td>{order.userName}</td> {/* Display the associated user's name */}
                                                <td>{order.Total || order.total}</td> {/* Display the total price */}                                                <td>{formatStatus(order.status)}</td>
                                                <td>
    {order.Date || !isNaN(new Date(order.Date || order.date).getTime())  // Check if Date exists and is valid
        ? new Date(order.Date || order.date).toISOString().split('T')[0] // Print as YYYY-MM-DD
        : null} 
</td>


                                            </tr>
                                        ))
                                    ) : (
                                        <tr>
                                            <td colSpan="5" className="text-center">No orders found</td>
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

export default ViewOrder;
