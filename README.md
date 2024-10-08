# ClickCart - E-Commerce System

## Project Overview

ClickCart is a comprehensive end-to-end E-commerce system designed for seamless interaction between customers, vendors, and administrators. The system consists of three primary components: a Web API, a Web Application for back-office management, and a Mobile Application for customer interaction.

- **Web API**: Serves as the backbone of the application, facilitating all data management and business logic. (Folder Name : WebAPI)
- **Web Application**: Developed using React, it provides a robust interface for Administrators, Vendors, and Customer Service Representatives (CSRs) to manage users, orders, products, and inventory. (Folder Name : FrontEnd)
- **Mobile Application**: Enables customers to browse products, manage their carts, place orders, and provide feedback on their shopping experiences. (Folder Name : Mobile/EADEcommerce)

## Features

### Web Application

The Web Application is designed for administrative tasks and back-office management, including:

- **User Management**: 
  - Create and manage users with roles such as Administrator, Vendor, and CSR.
  - Role-based access control ensures that only authorized users can perform specific functions.

- **Product Management**: 
  - Vendors can create, update, and delete products using unique Product IDs.
  - Manage product listings' activation and deactivation status.

- **Order Management**: 
  - Facilitate the creation and management of customer orders.
  - Track order status from processing to delivery, including updates and cancellations.

- **Inventory Management**: 
  - Monitor stock levels and generate alerts for low inventory.
  - Remove stock entries only if they are not part of pending orders.

- **Vendor Management**: 
  - Administrators can create and manage vendor profiles.
  - Customers can rank vendors and leave comments.

- **Category Management**: 
  - Manage product categories, including activation and deactivation.

### Mobile Application

The Mobile Application is designed for customers, providing features such as:

- **Product Browsing**: 
  - Search and filter products by category and view detailed product information.

- **Cart Management**: 
  - Add products to the cart, modify quantities, and remove items.

- **Order Management**: 
  - Place orders directly from the cart and view order history.
  - Track the status of orders in real time.

- **User Profile Management**: 
  - Create and modify user accounts; account activation is handled by CSRs or Administrators.

- **Feedback System**: 
  - Customers can leave comments and ratings for products and vendors based on their purchase experiences.

### Web API

The Web API is the core service for the ClickCart system, designed to handle all client requests and facilitate communication between the web and mobile applications. Key features include:

- **User Management**: 
  - Handle user roles with role-based access control.

- **Product Management**: 
  - Manage product listings, including activation and deactivation.

- **Order Management**: 
  - Full lifecycle order management with status tracking and updates.

- **Inventory Management**: 
  - Track inventory levels and notify vendors of low stock.

- **Vendor Management**: 
  - Create and manage vendor profiles and handle user comments and rankings.

- **Category Management**: 
  - Manage product categories, including their status.

## System Requirements

- **Web API**: 
  - Developed in C# and hosted on IIS.
  - Uses a NoSQL database for data storage.

- **Web Application**: 
  - Developed using React.
  - Accessible via modern web browsers.

- **Mobile Application**: 
  - Developed as a pure Android application.
  - Requires Android 30 or higher for compatibility

## Additional Information

For more details on specific components, please refer to the individual README files in the respective project directories. For issues or feature requests, feel free to reach out to the project maintainers.
