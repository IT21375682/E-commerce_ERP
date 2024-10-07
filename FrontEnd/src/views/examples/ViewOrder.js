import {
    Card,
    CardHeader,
    Table,
    Container,
    Row,
    Button,
    Badge
} from "reactstrap";
import React, { useState, useEffect } from "react";
import axios from "axios";
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import StatusIndicator from './StatusIndicator';
import { Modal, ModalHeader, ModalBody, ModalFooter, Input } from "reactstrap";
import { jwtDecode } from "jwt-decode";

const ViewOrder = () => {
    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(true);
    const [users, setUsers] = useState([]);
    const [expandedRow, setExpandedRow] = useState(null);
    const [productDetails, setProductDetails] = useState({});
    const [orderStatuses, setOrderStatuses] = useState({});
    const [editOrder, setEditOrder] = useState(null); // Holds the order being edited
    const [isEditModalOpen, setIsEditModalOpen] = useState(false); // Controls modal visibility
    const [isEditMode, setIsEditMode] = useState(false);
    const [canceledOrders, setCanceledOrders] = useState({}); // State to track canceled orders
    const [editedProductId, setEditedProductId] = useState(null);
    const [showMarkAsDelivered, setShowMarkAsDelivered] = useState({}); // Track which products should show the button
    const [cancellationNote, setCancellationNote] = useState('');
    const [showCancellationNote, setShowCancellationNote] = useState(false);
    const [vendorId, setVendorId] = useState(jwtDecode(localStorage.getItem("token")).Id);
    const [role, setRole] = useState();
    const [vendorProducts, setVendorProducts] = useState([]);

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
    // Fetch vendor's products if role is 'VENDOR'
    useEffect(() => {
        const fetchVendorProducts = async () => {
            try {
                const response = await axios.get(`https://localhost:5004/api/Product/vendor/${vendorId}`);
                setVendorProducts(response.data);
            } catch (error) {
                console.error("Error fetching vendor products:", error);
            }
        };

        if (role === 'VENDOR' && vendorId) {
            fetchVendorProducts();
        }
    }, [role, vendorId]);

    const filterOrdersByVendorProducts = (orders) => {
        if (vendorProducts.length === 0) {
            return orders; // If there are no vendor products, return all orders
        }

        return orders.filter(order =>
            order.productItems.some(productItem =>
                vendorProducts.some(vendorProduct =>
                    vendorProduct.id === (productItem.ProductId || productItem.productId)
                )
            )
        );
    };

    const fetchOrders = async () => {
        setLoading(true);
        try {
            const response = await axios.get('https://localhost:5004/api/Order');
            console.log(response.data); // Log the fetched orders to the console
            setLoading(false);
            return response.data;
        } catch (error) {
            console.error('Error fetching orders:', error);
            setLoading(false);
            return [];
        }
    };

    const fetchOrderStatus = async (orderId) => {
        try {
            const response = await axios.get(`https://localhost:5004/api/Order/${orderId}/status`);
            console.log(`Fetched status for Order ID ${orderId}:`, response.data); // Ensure this structure matches expected
            return response.data;
        } catch (error) {
            console.error(`Error fetching order status for Order ID ${orderId}:`, error);
            return null;
        }
    };


    const fetchProductDetails = async (productId) => {
        if (!productId) {
            console.error('Invalid productId:', productId);
            return null;
        }
        try {
            const response = await axios.get(`https://localhost:5004/api/Product/${productId}`);
            console.log(response.data);
            return response.data;
        } catch (error) {
            console.error('Error fetching product details:', error);
            return null;
        }
    };

    const fetchUsers = async () => {
        try {
            const response = await axios.get("https://localhost:5004/api/User");
            return response.data;
        } catch (error) {
            console.error("Error fetching users:", error);
            return [];
        }
    };

    const mergeOrdersWithUsers = (orders = [], users = []) => {
        return orders.map(order => {
            const user = users.find(u => u.id === order.userId);
            return { ...order, userName: user ? user.name : 'Unknown' };
        });
    };

    useEffect(() => {
        const fetchData = async () => {
            setLoading(true);
            const [ordersData, usersData] = await Promise.all([fetchOrders(), fetchUsers()]);
            const filteredOrders = role === 'VENDOR' ? filterOrdersByVendorProducts(ordersData) : ordersData;
            // const mergedOrders = mergeOrdersWithUsers(ordersData, usersData);
            const mergedOrders = mergeOrdersWithUsers(filteredOrders, usersData);
            setOrders(mergedOrders);
            setLoading(false);
        };

        fetchData();
    }, [vendorProducts]);

    // Fetch order statuses once orders are loaded
    useEffect(() => {
        const fetchStatuses = async () => {
            const statusPromises = orders.map(order => fetchOrderStatus(order.id));
            const statuses = await Promise.all(statusPromises);

            const statusMap = {};
            orders.forEach((order, index) => {
                statusMap[order.id] = statuses[index];
            });

            setOrderStatuses(statusMap);
            console.log('Order Statuses:', statusMap);
        };

        if (orders.length > 0) {
            fetchStatuses();
        }
    }, [orders]);


    const toggleRow = async (orderId) => {
        if (expandedRow === orderId) {
            setExpandedRow(null);
        } else {
            setExpandedRow(orderId);
            const order = orders.find((order) => order.id === orderId);
            if (order && order.productItems) {
                for (const productItem of order.productItems) {
                    const productId = productItem.ProductId || productItem.productId;
                    if (productId) {
                        const product = await fetchProductDetails(productId);
                        setProductDetails((prevDetails) => ({
                            ...prevDetails,
                            [productId]: product || { Name: 'Product Not Found', Price: 'N/A', ImageUrl: null },
                        }));
                    }
                }
            }
        }
    };
    const formatStatus = (status) => {
        if (!status) {
            console.log("Undefined status:", status); // Debugging Log
            return { text: 'Unknown', color: 'secondary' };
        }

        // Check if status is a string
        if (typeof status === 'string') {
            switch (status.toLowerCase()) {
                case 'delivered':
                    return { text: 'Delivered', color: 'success' };
                case 'dispatched':
                    return { text: 'Dispatched', color: 'info' };
                case 'processing':
                    return { text: 'Processing', color: 'warning' };
                case 'partially delivered': // Update this line to match the string format
                    return { text: 'Partially Delivered', color: 'primary' };
                case 'pending':
                    return { text: 'Pending', color: 'primary' };
                case 'canceled':
                    return { text: 'Canceled', color: 'danger' };
                default:
                    return { text: 'Status unknown', color: 'secondary' };
            }
        }

        // Check if status is an object with boolean properties
        if (typeof status === 'object') {
            if (status.delivered) return { text: 'Delivered', color: 'success' };
            if (status.dispatched) return { text: 'Dispatched', color: 'info' };
            if (status.processing) return { text: 'Processing', color: 'warning' };
            if (status.partially_delivered) return { text: 'Partially Delivered', color: 'primary' }; // Make sure this matches the key in the object
            if (status.pending) return { text: 'Pending', color: 'primary' };
            if (status.canceled) return { text: 'Canceled', color: 'danger' };
        }

        // Fallback for unknown structures
        console.log("Unrecognized status structure:", status);
        return { text: 'Status unknown', color: 'secondary' };
    };


    // const handleDelete = async (orderId) => {
    //     console.log("Delete order with ID:", orderId);
    //     try {
    //         await axios.delete(`https://localhost:5004/api/Order/${orderId}`);
    //         toast.success("Order deleted successfully");
    //         setOrders(orders.filter(order => order.id !== orderId)); // Remove the deleted order from the UI
    //     } catch (error) {
    //         console.error("Error deleting order:", error);
    //         toast.error("Failed to delete order");
    //     }
    // };
    const handleEdit = (orderId) => {
        const order = orders.find(order => order.id === orderId);
        setEditOrder(order);
        // setIsEditMode(true); // Set edit mode to true when editing
        setIsEditModalOpen(true);
    };


    const handleEditProduct = (orderId, productId) => {
        const order = orders.find(order => order.id === orderId);
        setEditOrder(order);
        setIsEditMode(true);
        setEditedProductId(productId); // Set the currently edited product ID
        setShowMarkAsDelivered((prev) => ({ ...prev, [productId]: true })); // Show "Mark as Delivered" button for this product
    };

    // const handleCancel = (orderId, productId) => {
    //     if (!orderId || !productId) {
    //         console.error("Order ID or Product ID is undefined.");
    //         return;
    //     }

    //     // Remove the "Mark as Delivered" button for the canceled product
    //     setShowMarkAsDelivered((prev) => ({ ...prev, [productId]: false })); 

    //     // Optional: If you want to update the product's status in the order (e.g., to canceled), you could also do that
    //     setEditOrder((prevOrder) => ({
    //         ...prevOrder,
    //         productItems: prevOrder.productItems.map(item =>
    //             item.ProductId === productId || item.productId === productId
    //                 ? { ...item, Delivered: false, Canceled: true } // Marking as canceled
    //                 : item
    //         )
    //     }));

    //     // Here you can also make an API call to cancel the order or perform any necessary logic
    // };


    const handleStatusChange = (newStatus) => {
        setEditOrder(prevOrder => ({
            ...prevOrder,
            Status: newStatus
        }));

        // Reset the cancellation note if the status changes away from "Canceled"
        if (newStatus !== 'Canceled') {
            setCancellationNote('');
        }
    };

    const handleMarkAsDelivered = async (productId) => {
        try {
            // Fetch the vendor ID from the product table using the product ID
            const productResponse = await axios.get(`https://localhost:5004/api/Product/${productId}`);
            const vendorId = productResponse.data.vendorId; // Adjust this according to your API response structure

            // Get the orderId (assuming it's stored in the editOrder state)
            const orderId = editOrder.id; // or however you are storing the order ID

            // Call the API to update delivery status
            await axios.patch(`https://localhost:5004/api/Order/update-delivery-status/${orderId}/${vendorId}/${productId}`);

            // Update the local state to reflect that the item has been delivered
            setEditOrder(prevOrder => ({
                ...prevOrder,
                productItems: prevOrder.productItems.map(item =>
                    item.ProductId === productId || item.productId === productId
                        ? { ...item, Delivered: true }
                        : item
                )
            }));

            // Hide the button after marking as delivered
            setShowMarkAsDelivered(prev => ({ ...prev, [productId]: false }));

            // Optionally, show a success message
            toast.success('Product marked as delivered successfully!');

        } catch (error) {
            console.error('Error marking product as delivered:', error);
            toast.error('Failed to mark product as delivered.');
        }
    };


    const saveOrderChanges = async () => {
        try {
            const customMessage = editOrder.Status === 'Canceled' ? editOrder.cancellationNote || editOrder.CancellationNote : null;

            // Update the order status
            // await axios.patch(`https://localhost:5004/api/Order/${editOrder.id}/status/${editOrder.Status||editOrder.status}`);
            await axios.patch(`https://localhost:5004/api/Order/${editOrder.id}/status/${editOrder.Status || editOrder.status}`, customMessage);
            toast.success(`Order status updated to "${editOrder.Status}" successfully`);

            // Optionally refresh orders or handle updated state here
            // const ordersData = await fetchOrders(); // Fetch updated orders
            // setOrders(ordersData);

            setIsEditModalOpen(false);
        } catch (error) {
            console.error('Error updating order status:', error);
            toast.error('Failed to update order status');
        }
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
                                    <th scope="col">Order Details</th>
                                </tr>
                            </thead>
                            <tbody>
                                {loading ? (
                                    <tr>
                                        <td colSpan="6" className="text-center">Loading...</td>
                                    </tr>
                                ) : (
                                    orders.length > 0 ? (
                                        orders.map((order) => (
                                            <React.Fragment key={order.id}>
                                                <tr >

                                                    <td>{order.id}</td>
                                                    <td>{order.userName}</td>
                                                    <td>{order.Total || order.total}</td>
                                                    <td>
                                                        <Badge color={formatStatus(orderStatuses[order.id]).color}>
                                                            {formatStatus(orderStatuses[order.id]).text}
                                                            {console.log('Order ID:', order.id, 'Status:', orderStatuses[order.id])} {/* Debugging Log */}
                                                        </Badge>
                                                    </td>
                                                    <td>
                                                        {order.Date || !isNaN(new Date(order.Date || order.date).getTime())
                                                            ? new Date(order.Date || order.date).toISOString().split('T')[0]
                                                            : null}
                                                    </td>
                                                    <td>
                                                        <Button
                                                            color={expandedRow === order.id ? "secondary" : "primary"}
                                                            onClick={() => toggleRow(order.id)}
                                                        >
                                                            {expandedRow === order.id ? (
                                                                <>
                                                                    <i className="fas fa-chevron-up"></i> Hide Details
                                                                </>
                                                            ) : (
                                                                <>
                                                                    <i className="fas fa-chevron-down"></i> Show Details
                                                                </>
                                                            )}
                                                        </Button>
                                                    </td>
                                                </tr>
                                                {expandedRow === order.id && (
                                                    <tr style={{ backgroundColor: '#e6ecf0' }}>
                                                        <td colSpan="6">
                                                            <div>
                                                                <h5>Product Details</h5>
                                                                <Table>
                                                                    <thead>
                                                                        <tr>
                                                                            <th>Product ID</th>
                                                                            <th>Image</th>
                                                                            <th>Product Name</th>
                                                                            <th>Price</th>
                                                                            <th>Quantity</th>
                                                                            <th>Delivered</th>
                                                                        </tr>
                                                                    </thead>
                                                                    <tbody>
                                                                        {/* {order.productItems && order.productItems.length > 0 ? (
                                                                            order.productItems.map((productItem, index) => { */}
                                                                        {order.productItems && order.productItems.length > 0 ? (
                                                                            (role === 'VENDOR'
                                                                                ? order.productItems.filter(productItem =>
                                                                                    vendorProducts.some(vendorProduct =>
                                                                                        vendorProduct.id === (productItem.ProductId || productItem.productId)
                                                                                    )
                                                                                )
                                                                                : order.productItems
                                                                            ).map((productItem, index) => {
                                                                                const product = productDetails[productItem.ProductId || productItem.productId];
                                                                                return (
                                                                                    <tr key={index}>
                                                                                        <td>{productItem.ProductId || productItem.productId}</td>
                                                                                        <td>
                                                                                            {product?.productImage ? (
                                                                                                <img
                                                                                                    src={`data:image/jpeg;base64,${product.productImage}`}
                                                                                                    alt={product.Name || product.name}
                                                                                                    style={{ width: '50px' }}
                                                                                                />
                                                                                            ) : 'No Image'}
                                                                                        </td>
                                                                                        <td>{product?.Name || product?.name || 'Loading...'}</td>
                                                                                        <td>{product ? `$${product.Price || product.price || 'N/A'}` : 'Loading...'}</td>
                                                                                        <td>{productItem.Count || productItem.count}</td>
                                                                                        <td >
                                                                                            <Badge color={productItem.Delivered || productItem.delivered ? "success" : "danger"}>
                                                                                                {productItem.Delivered || productItem.delivered ? "Delivered" : "Not Delivered"}
                                                                                            </Badge>
                                                                                            {/* Show the "Delivered" button only in edit mode and if not delivered */}
                                                                                            {isEditMode &&
                                                                                                // Show the button only if the product has not been delivered and is not canceled
                                                                                                !(productItem.Delivered || productItem.delivered) &&
                                                                                                !productItem.Canceled && (
                                                                                                    <Button
                                                                                                        color="success"
                                                                                                        onClick={() => handleMarkAsDelivered(productItem.ProductId || productItem.productId)}>
                                                                                                        Mark as Delivered
                                                                                                    </Button>
                                                                                                )}

                                                                                        </td>
                                                                                    </tr>
                                                                                );
                                                                            })
                                                                        ) : (
                                                                            <tr>
                                                                                <td colSpan="6" className="text-center">No product items available</td>
                                                                            </tr>
                                                                        )}
                                                                    </tbody>
                                                                </Table>
                                                                <br />
                                                                <div>
                                                                    <StatusIndicator status={order.Status || order.status} />
                                                                    {/* <div style={{ textAlign: 'right', marginTop: '10px' }}>
                                                                        <Button color="warning" onClick={() => handleEdit(order.id)} style={{ marginRight: '5px' }}>
                                                                            Edit
                                                                        </Button>
                                                                        <Button color="danger" onClick={() => handleDelete(order.id)}>
                                                                            Delete
                                                                        </Button>
                                                                    </div> */}
                                                                    <div className="d-flex justify-content-end">
                                                                    {role === 'VENDOR' && (
    <Button color="warning" onClick={() => handleEditProduct(order.id)}>Edit Product Status</Button>
)}
                                                                        <Button color="warning" onClick={() => handleEdit(order.id)}>Edit Order Status</Button>
                                                                        {/* <Button color="danger" onClick={() => handleDelete(order.id)}>Delete</Button> */}
                                                                        {/* <Button 
                        color="danger" 
                        onClick={() => handleCancel(order.id)} // Now productId is defined
                    >
                        Cancel
                    </Button> */}
                                                                    </div>

                                                                </div>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                )}
                                            </React.Fragment>
                                        ))
                                    ) : (
                                        <tr>
                                            <td colSpan="6" className="text-center">No orders found</td>
                                        </tr>
                                    )
                                )}
                            </tbody>
                        </Table>
                    </Card>
                </div>
            </Row>
            {/* Edit Order Modal */}
            <Modal isOpen={isEditModalOpen} toggle={() => setIsEditModalOpen(!isEditModalOpen)}>
                <ModalHeader toggle={() => setIsEditModalOpen(!isEditModalOpen)}>Edit Order</ModalHeader>
                <ModalBody>
                    {editOrder && (
                        <div>
                            <h5>Order ID: {editOrder.id}</h5>
                            <h6>Customer Name: {editOrder.userName}</h6>
                            <div>
                                <label>Status:</label>
                                <select value={editOrder.Status} onChange={(e) => handleStatusChange(e.target.value)}>
                                    <option value="Pending">Pending</option>
                                    <option value="Processing">Processing</option>
                                    <option value="Dispatched">Dispatched</option>
                                    <option value="Delivered">Delivered</option>
                                    <option value="Canceled">Canceled</option>
                                </select>
                            </div>
                            {/* Conditionally render the cancellation note textbox */}
                            {editOrder.Status === 'Canceled' && (
                                <div>
                                    <label>Cancellation Note:</label>
                                    <br />
                                    <textarea
                                        style={{ width: '100%', height: '100px', resize: 'vertical' }} // Adjust height as necessary
                                        value={editOrder.cancellationNote}
                                        onChange={(e) => setCancellationNote(e.target.value)}
                                        placeholder="Enter cancellation reason..."
                                    />
                                </div>
                            )}
                        </div>
                    )}
                </ModalBody>
                <ModalFooter>
                    <Button color="primary" onClick={saveOrderChanges}>Save Changes</Button>
                    <Button color="secondary" onClick={() => setIsEditModalOpen(false)}>Cancel</Button>
                </ModalFooter>
            </Modal>

        </Container>
    );
};

export default ViewOrder;
