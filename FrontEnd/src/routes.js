import Index from "views/Index.js";
import AddVendor from "views/examples/AddVendor.js";
import Register from "views/examples/Register.js";
import Login from "views/examples/Login.js";
import Tables from "views/examples/VendorTable.js";
import Icons from "views/examples/Icons.js";
import AddProduct from "views/examples/AddProduct";
import ViewProduct from "views/examples/ViewProducts";
import EditProduct from "views/examples/EditProduct";
import ViewCategory from "views/examples/ViewCategory";

var routes = [
  {
    path: "/index",
    name: "Dashboard",
    icon: "ni ni-tv-2 text-primary",
    component: <Index />,
    layout: "/admin",
  },
  {
    path: "/icons",
    name: "Icons",
    icon: "ni ni-planet text-blue",
    component: <Icons />,
    layout: "/admin",
  },
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
    path: "/tables",
    name: "Vendor ",
    icon: "ni ni-bullet-list-67 text-red",
    component: <Tables />,
    layout: "/admin",
  },
  {
    path: "/view-product",
    name: "View Product ",
    icon: "ni ni-bullet-list-67 text-red",
    component: < ViewProduct/>,
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
    path: "/register",
    name: "Register",
    icon: "ni ni-circle-08 text-pink",
    component: <Register />,
    layout: "/auth",
  },
  {
    path: "/edit-product/:id",
    name: "Edit Product",
    icon: "ni ni-circle-08 text-pink",
    component: <EditProduct/>,
    layout: "/admin",
  },
  {
    path: "/view-category",
    name: "view Category",
    icon: "ni ni-circle-08 text-pink",
    component: <ViewCategory/>,
    layout: "/admin",
  },
];
export default routes;
