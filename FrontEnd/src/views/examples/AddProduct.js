import React, { useState } from 'react';
import {
  Button,
  Card,
  CardHeader,
  CardBody,
  FormGroup,
  Form,
  Input,
  Container,
  Row,
  Col,
} from 'reactstrap';

const AddProduct = () => {
  const [menuImage, setMenuImage] = useState(null);

  const handleImageUpload = (event) => {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onloadend = () => {
        setMenuImage(reader.result);
      };
      reader.readAsDataURL(file);
    }
  };

  return (
    <Container fluid className="mt-7">
      <Row>
        <Col lg="1" />
        <Col lg="10">
          <Card className="shadow mb-4">
            <CardHeader className="py-3">
              <h1 className="m-0 font-weight-bold text-primary text-center">
                <b>Add Menu Item</b>
              </h1>
            </CardHeader>
            <CardBody>
              <Form>
                <Row>
                  <Col xl="6" lg="6" md="12">
                    <FormGroup>
                      <label htmlFor="txtProductName">Product Name</label>
                      <Input
                        type="text"
                        id="txtProductName"
                        className="form-control-user"
                        required
                        style={{ borderRadius: '10px' }}
                      />
                    </FormGroup>

                    <Row>
                      <Col xs="12" sm="6">
                        <FormGroup>
                          <label htmlFor="ddlSelectCategory">Category</label>
                          <Input type="select" id="ddlSelectCategory">
                            <option value="">Select category</option>
                            <option value="COMBO">COMBO</option>
                            <option value="POPCORN">POPCORN</option>
                            <option value="SOFT DRINKS">SOFT DRINKS</option>
                            <option value="COFFEE">COFFEE</option>
                            <option value="CHOCOLATES">CHOCOLATES</option>
                          </Input>
                        </FormGroup>
                      </Col>

                      <Col xs="12" sm="6">
                        <FormGroup>
                          <label htmlFor="txtSize">Size</label>
                          <Input
                            type="text"
                            id="txtSize"
                            className="form-control-user"
                            required
                            style={{ borderRadius: '10px' }}
                          />
                        </FormGroup>
                      </Col>
                    </Row>

                    <FormGroup>
                      <label htmlFor="AdditionalInf">Additional Description</label>
                      <Input
                        type="textarea"
                        id="AdditionalInf"
                        rows="2"
                        className="form-control-user"
                        required
                        style={{ borderRadius: '10px' }}
                      />
                    </FormGroup>

                    <FormGroup>
                      <label htmlFor="txtUnitPrice">Unit Price</label>
                      <div style={{ position: 'relative' }}>
                        <Input
                          type="number"
                          id="txtUnitPrice"
                          className="form-control-user"
                          placeholder="00.00"
                          step="0.01"
                          required
                          style={{ paddingLeft: '50px' }}
                        />
                        <span
                          style={{
                            position: 'absolute',
                            top: '50%',
                            left: '10px',
                            transform: 'translateY(-50%)',
                            color: '#aaa',
                            pointerEvents: 'none',
                            fontSize: '0.85rem',
                          }}
                        >
                          Rs.
                        </span>
                      </div>
                    </FormGroup>
                  </Col>

                  <Col xl="4" lg="4" md="10" className="mx-auto mb-4">
                    <div
                      style={{
                        maxWidth: '100%',
                        height: '240px',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        color: '#fff',
                        background: '#aaa',
                      }}
                    >
                      {menuImage ? (
                        <img src={menuImage} alt="Menu" className="img-fluid" />
                      ) : (
                        'No image selected'
                      )}
                    </div>
                    <Input
                      type="file"
                      id="imageInput"
                      accept="image/*"
                      onChange={handleImageUpload}
                      className="mt-3"
                    />
                    <Button color="primary" className="btn-block mt-3">
                      UPLOAD PRODUCT IMAGE
                    </Button>
                  </Col>
                </Row>

                <Row className="mt-4">
                  <Col lg="4" />
                  <Col lg="2">
                    <Button color="success" className="btn-block">
                      <i className="fas fa-check"></i> Create
                    </Button>
                  </Col>
                  <Col lg="2">
                    <Button color="danger" className="btn-block">
                      <i className="fas fa-trash"></i> Cancel
                    </Button>
                  </Col>
                  <Col lg="4" />
                </Row>

                <Row className="mt-3">
                  <Col lg="3" />
                  <Col lg="6">
                    <div id="divValidationError" />
                  </Col>
                  <Col lg="3" />
                </Row>
              </Form>
            </CardBody>
          </Card>
        </Col>
        <Col lg="1" />
      </Row>
    </Container>
  );
};

export default AddProduct;
