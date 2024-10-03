import React, { useState } from "react";
import {
  Nav,
  NavItem,
  NavLink,
  TabContent,
  TabPane,
  Row,
  Col,
  Button,
} from "reactstrap";
import classnames from "classnames";

const ViewProduct = () => {
  // State to track active main tab (Active/Deactivated)
  const [activeMainTab, setActiveMainTab] = useState("1");

  // State to track active category tab within each main tab
  const [activeCategoryTab, setActiveCategoryTab] = useState({
    active: "COMBO",
    deactivated: "COMBO",
  });

  const toggleMainTab = (tab) => {
    if (activeMainTab !== tab) setActiveMainTab(tab);
  };

  const toggleCategoryTab = (tab, status) => {
    if (status === "active") {
      setActiveCategoryTab((prev) => ({ ...prev, active: tab }));
    } else {
      setActiveCategoryTab((prev) => ({ ...prev, deactivated: tab }));
    }
  };

  const categories = ["COMBO", "POPCORN", "SOFT DRINKS", "COFFEE", "CHOCOLATES"];

  return (
    <div class="container-fluid">
    <div className="container mt-4">
      <Row className="mb-3">
        <Col>
          <h4 className="text-center">Foods & Beverages</h4>
        </Col>
        <Col className="text-right">
          <Button color="primary" href="/AddCanteenMenu">
            <i className="fa fa-plus-square" style={{ fontSize: 20, color: "white" }}></i> Add Menu Item
          </Button>
        </Col>
      </Row>

      {/* Main Tabs: Active / Deactivated */}
      <Nav tabs>
        <NavItem>
          <NavLink
            className={classnames({ active: activeMainTab === "1" })}
            onClick={() => toggleMainTab("1")}
          >
            Active
          </NavLink>
        </NavItem>
        <NavItem>
          <NavLink
            className={classnames({ active: activeMainTab === "2" })}
            onClick={() => toggleMainTab("2")}
          >
            Deactivated
          </NavLink>
        </NavItem>
      </Nav>

      <TabContent activeTab={activeMainTab}>
        {/* Active Tab Content */}
        <TabPane tabId="1">
          <Nav tabs>
            {categories.map((category, idx) => (
              <NavItem key={idx}>
                <NavLink
                  className={classnames({ active: activeCategoryTab.active === category })}
                  onClick={() => toggleCategoryTab(category, "active")}
                >
                  {category}
                </NavLink>
              </NavItem>
            ))}
          </Nav>
          <TabContent activeTab={activeCategoryTab.active}>
            {categories.map((category, idx) => (
              <TabPane tabId={category} key={idx}>
                <h5>{category} Menu (Active)</h5>
                {/* Render your active items here for the given category */}
              </TabPane>
            ))}
          </TabContent>
        </TabPane>

        {/* Deactivated Tab Content */}
        <TabPane tabId="2">
          <Nav tabs>
            {categories.map((category, idx) => (
              <NavItem key={idx}>
                <NavLink
                  className={classnames({ active: activeCategoryTab.deactivated === category })}
                  onClick={() => toggleCategoryTab(category, "deactivated")}
                >
                  {category}
                </NavLink>
              </NavItem>
            ))}
          </Nav>
          <TabContent activeTab={activeCategoryTab.deactivated}>
            {categories.map((category, idx) => (
              <TabPane tabId={category} key={idx}>
                <h5>{category} Menu (Deactivated)</h5>
                {/* Render your deactivated items here for the given category */}
              </TabPane>
            ))}
          </TabContent>
        </TabPane>
      </TabContent>
    </div>
    </div>
  );
};

export default ViewProduct;
