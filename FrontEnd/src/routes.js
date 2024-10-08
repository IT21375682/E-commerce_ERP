import AddVendor from "views/examples/AddVendor.js";
import Login from "views/examples/Login.js";
import Tables from "views/examples/VendorTable.js";
import AddProduct from "views/examples/AddProduct";
import ViewProduct from "views/examples/ViewProducts";
import CustomerTable from "views/examples/CustomerTable.js"
import CustomerReqTable from "views/examples/CustomerReqTable.js"
import ViewVendor from "views/examples/ViewVendor";
import ViewCustomer from "views/examples/ViewCustomer";
import InactiveCusTable from "views/examples/InactiveCusTable";
import EditProduct from "views/examples/EditProduct";
import ViewCategory from "views/examples/ViewCategory";
import ViewOrder from "views/examples/ViewOrder";
import AddChategory from "views/examples/AddChategory";
import ViewComments from "views/examples/ViewComments";
import InventoryManagement from "views/examples/inventoryManagement";

var routes = [
  {
    path: "/add-vendor",
    name: "Add Vendor",
    icon: "ni ni-single-02 text-yellow",
    component: <AddVendor />,
    layout: "/admin",
  },
  {
    path: "/add-product",
    name: "Add Product",
    icon: "ni ni-single-02 text-yellow",
    component: <AddProduct />,
    layout: "/admin",
  },
  {
    path: "/view-vendor",
    name: "Vendor ",
    icon: "ni ni-bullet-list-67 text-red",
    component: <Tables />,
    layout: "/admin",
  },
  {
    path: "/view-product",
    name: "View Product ",
    icon: "ni ni-bullet-list-67 text-red",
    component: < ViewProduct />,
    layout: "/admin",
  },
  {
    path: "/Customer-table",
    name: "customer ",
    icon: "ni ni-bullet-list-67 text-red",
    component: <CustomerTable />,
    layout: "/admin",
  },
  {
    path: "/InactiveCus-table",
    name: "customer ",
    icon: "ni ni-bullet-list-67 text-red",
    component: <InactiveCusTable />,
    layout: "/admin",
  },
  {
    path: "/Customer-Requests",
    name: "customer ",
    icon: "ni ni-bullet-list-67 text-red",
    component: <CustomerReqTable />,
    layout: "/admin",
  },
  {
    path: "/vendor/:vendorId",
    name: "customer ",
    icon: "ni ni-bullet-list-67 text-red",
    component: <ViewVendor />,
    layout: "/admin",
  },
  {
    path: "/customer/:customerId",
    name: "customer ",
    icon: "ni ni-bullet-list-67 text-red",
    component: <ViewCustomer />,
    layout: "/admin",
  },
  {
    path: "/login",
    name: "Login",
    icon: "ni ni-key-25 text-info",
    component: <Login />,
    layout: "/auth",
  },
  {
    path: "/edit-product/:id",
    name: "Edit Product",
    icon: "ni ni-circle-08 text-pink",
    component: <EditProduct />,
    layout: "/admin",
  },
  {
    path: "/view-category",
    name: "view Category",
    icon: "ni ni-circle-08 text-pink",
    component: <ViewCategory />,
    layout: "/admin",
  },
  {
    path: "/add-category",
    name: "AddChategory",
    icon: "ni ni-circle-08 text-pink",
    component: <AddChategory />,
    layout: "/admin",
  },
  {
    path: "/view-comments",
    name: "ViewComments",
    icon: "ni ni-circle-08 text-pink",
    component: <ViewComments />,
    layout: "/admin",
  },
  {
    path: "/view-order",
    name: "view Order",
    icon: "ni ni-circle-08 text-pink",
    component: <ViewOrder />,
    layout: "/admin",
  },
  {
    path: "/inventory-mgt",
    name: "Inventory Management",
    icon: "ni ni-circle-08 text-pink",
    component: <InventoryManagement />,
    layout: "/admin",
  },
];
export default routes;
