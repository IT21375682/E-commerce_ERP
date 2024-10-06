import { useState, useEffect } from "react";
import { NavLink as NavLinkRRD, Link } from "react-router-dom";
import { PropTypes } from "prop-types";
import { jwtDecode } from "jwt-decode";
// To decode JWT tokens
import {
  Collapse,
  UncontrolledDropdown,
  DropdownToggle,
  DropdownMenu,
  DropdownItem,
  Media,
  NavbarBrand,
  Navbar,
  NavItem,
  NavLink,
  Nav,
  Container,
  Row,
  Col,
  Form,
  Input,
  InputGroup,
  InputGroupAddon,
  InputGroupText
} from "reactstrap";

const Sidebar = (props) => {
  const [collapseOpen, setCollapseOpen] = useState();
  const [role, setRole] = useState();
  const [email, setEmail] = useState();

  // Decode JWT token and get the role
  useEffect(() => {

    if (!localStorage.getItem("token")) {
      // If token is not present, redirect to login
      window.location.href = "/auth/login";
    }
    const token = localStorage.getItem("token"); // Retrieve the token from localStorage
    if (token) {
      try {
        const decodedToken = jwtDecode(token);
        setRole(decodedToken.role);
        setEmail(decodedToken.email);
        console.log("Role : " + decodedToken.role + ": " + role)
      } catch (error) {
        console.error("Failed to decode token", error);
      }
    }
  }, []);

  // Toggles collapse between opened and closed (true/false)
  const toggleCollapse = () => {
    setCollapseOpen((data) => !data);
  };

  // Closes the collapse
  const closeCollapse = () => {
    setCollapseOpen(false);
  };

  const { logo } = props;
  let navbarBrandProps;
  if (logo && logo.innerLink) {
    navbarBrandProps = {
      to: logo.innerLink,
      tag: Link,
    };
  } else if (logo && logo.outterLink) {
    navbarBrandProps = {
      href: logo.outterLink,
      target: "_blank",
    };
  }

  return (
    <Navbar
      className="navbar-vertical fixed-left navbar-light bg-white"
      expand="md"
      id="sidenav-main"
    >
      <Container fluid>
        {/* Toggler */}
        <button
          className="navbar-toggler"
          type="button"
          onClick={toggleCollapse}
        >
          <span className="navbar-toggler-icon" />
        </button>

        {/* Brand */}
        {logo ? (
          <NavbarBrand className="pt-0" {...navbarBrandProps}>
            <img alt={logo.imgAlt} className="navbar-brand-img" src={logo.imgSrc} />
          </NavbarBrand>
        ) : null}

        {/* Collapse */}
        <Collapse navbar isOpen={collapseOpen}>
          {/* Collapse header */}
          <div className="navbar-collapse-header d-md-none">
            <Row>
              {logo ? (
                <Col className="collapse-brand" xs="6">
                  {logo.innerLink ? (
                    <Link to={logo.innerLink}>
                      <img alt={logo.imgAlt} src={logo.imgSrc} />
                    </Link>
                  ) : (
                    <a href={logo.outterLink}>
                      <img alt={logo.imgAlt} src={logo.imgSrc} />
                    </a>
                  )}
                </Col>
              ) : null}
              <Col className="collapse-close" xs="6">
                <button className="navbar-toggler" type="button" onClick={toggleCollapse}>
                  <span />
                  <span />
                </button>
              </Col>
            </Row>
          </div>

          {/* Navigation */}
          <Nav className="mb-md-3" navbar>
            {/* Vendor-specific items */}
            {(role === "VENDOR") && (
              <>
                <NavItem>
                  <NavLink href="/admin/view-product">
                    <i className="ni ni-ui-04" />
                    Product Management
                  </NavLink>
                </NavItem>
                <NavItem>
                  <NavLink href="/admin/view-order">
                    <i className="ni ni-ui-04" />
                    Order Management
                  </NavLink>
                </NavItem>
                <NavItem>
                  <NavLink href="/admin/view-category">
                    <i className="ni ni-ui-04" />
                    Category Management
                  </NavLink>
                </NavItem>
              </>
            )}

            {/* Non-vendor-specific items */}
            {(role == "ADMIN" || role === 'CSR') && (
              <>
                <NavItem>
                  <NavLink href="/admin/tables">
                    <i className="ni ni-ui-04" />
                    Vendor Management
                  </NavLink>
                </NavItem>
                <NavItem>
                  <NavLink href="/admin/Customer-table">
                    <i className="ni ni-ui-04" />
                    Customer Management
                  </NavLink>
                </NavItem>
                <NavItem>
                  <NavLink href="/admin/view-product">
                    <i className="ni ni-ui-04" />
                    Product Management
                  </NavLink>
                </NavItem>
                <NavItem>
                  <NavLink href="/admin/view-order">
                    <i className="ni ni-ui-04" />
                    Order Management
                  </NavLink>
                </NavItem>
                <NavItem>
                  <NavLink href="/admin/view-category">
                    <i className="ni ni-ui-04" />
                    Category Management
                  </NavLink>
                </NavItem>
                <NavItem>
                  <NavLink href="/admin/inventory-mgt">
                    <i className="ni ni-ui-04" />
                    Inventory Management
                  </NavLink>
                </NavItem>


              </>
            )}
          </Nav>
        </Collapse>
      </Container>
    </Navbar>
  );
};

Sidebar.defaultProps = {
  routes: [{}],
};

Sidebar.propTypes = {
  // links that will be displayed inside the component
  routes: PropTypes.arrayOf(PropTypes.object),
  logo: PropTypes.shape({
    innerLink: PropTypes.string,
    outterLink: PropTypes.string,
    imgSrc: PropTypes.string.isRequired,
    imgAlt: PropTypes.string.isRequired,
  }),
};

export default Sidebar;
