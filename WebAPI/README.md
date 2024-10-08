---
# ClickCart- E-Commerce Web Service

## Project Overview

This Web API serves as the backbone for an end-to-end E-commerce system, designed to handle all business logic and data management. It provides a centralized service that processes requests from both the web and mobile applications, facilitating operations such as user, product, order, inventory, and vendor management. The API is built using C# and is hosted on IIS, with a NoSQL database used for data storage.

## Features

- **User Management**: Manage different user roles (Administrator, Vendor, and CSR) with role-based access control.
- **Product Management**: Allows vendors to manage products and their activation status.
- **Order Management**: Enables full lifecycle order management, including status tracking, updates, and cancellations.
- **Inventory Management**: Track product inventory, manage stock levels, and notify vendors of low stock.
- **Vendor Management**: Administrators can create vendor profiles, and customers can leave rankings and comments on vendors.
- **Comment Management**: Allows users to add comments on products or vendors.
- **Cart Management**: User add products to cart and update or delete before placing order.
- **Category Management**: Manage product categories, activation or deactivation of products.

## System Requirements

- **Server**: IIS to host the Web API.
- **Database**: NoSQL database for data storage.
- **Technology**: The Web API is developed using C#.

## How to Connect to the Web API

### Method 1: Direct Connection via IP Address

1. Open the `Program.cs` file and configure it with the server's IP address and the desired port for the API.
2. Modify the `launchSettings.json` file to set the `applicationUrl`:
   ```json
   "applicationUrl": "http://<Your_IP_Address>:<Port>"
   ```
3. Launch the API from your IDE or through the command line.

### Method 2: Deploy to IIS

1. In Visual Studio, publish the Web API project by selecting **Build > Publish** and choose IIS as the deployment target.
2. On your IIS server:
   - Create a new website and specify the path to the published files.
   - Set the IP address and port for the API and ensure these ports are accessible.
   - Start the website, making the API available at `http://<Your_Server_IP>:<Port>`.

### Method 3: Local Development with IIS Express

1. Configure `launchSettings.json` for IIS Express:
   ```json
   "iisSettings": {
       "windowsAuthentication": false,
       "anonymousAuthentication": true,
       "iisExpress": {
           "applicationUrl": "http://localhost:<Port>",
           "sslPort": <SSL_Port>
       }
   }
   ```
2. Launch the API using IIS Express from within Visual Studio for local testing.

---