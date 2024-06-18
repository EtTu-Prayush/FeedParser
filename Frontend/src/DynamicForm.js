import React, { useState } from 'react';
import axios from 'axios';
import { Modal, Button, Form } from 'react-bootstrap';

const DynamicForm = () => {
    const [fields, setFields] = useState({});
    const [formData, setFormData] = useState({});
    const [show, setShow] = useState(false);
    const [xmlInput, setXmlInput] = useState('');
    const [jsonInput, setJsonInput] = useState('');
    const [metadata, setMetadata] = useState({
        title: '',
        url: '',
        description: '',
        imageUrl: '',
        publishDate: ''
    });

    const handleInputChange = (e, path) => {
        setFormData({
            ...formData,
            [path]: e.target.value,
        });
    };

    const handleMetadataChange = (e) => {
        const { name, value } = e.target;
        setMetadata({
            ...metadata,
            [name]: value
        });
    };

    const parseXML = async () => {
        const response = await axios.post('/api/parse/xml', xmlInput, { headers: { 'Content-Type': 'application/xml' } });
        setFields(response.data);
        setShow(true);
    };

    const parseJSON = async () => {
        const response = await axios.post('/api/parse/json', jsonInput, { headers: { 'Content-Type': 'application/json' } });
        setFields(response.data);
        setShow(true);
    };

    const handleSubmit = async () => {
        await axios.post('/api/parse/submit', metadata, { headers: { 'Content-Type': 'application/json' } });
        setShow(false);
    };

    return (
        <div>
            <textarea placeholder="Enter XML here" value={xmlInput} onChange={(e) => setXmlInput(e.target.value)} />
            <button onClick={parseXML}>Parse XML</button>
            <textarea placeholder="Enter JSON here" value={jsonInput} onChange={(e) => setJsonInput(e.target.value)} />
            <button onClick={parseJSON}>Parse JSON</button>

            <Modal show={show} onHide={() => setShow(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Enter Feed Metadata</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form>
                        <Form.Group>
                            <Form.Label>Title</Form.Label>
                            <Form.Control type="text" name="title" value={metadata.title} onChange={handleMetadataChange} />
                        </Form.Group>
                        <Form.Group>
                            <Form.Label>URL</Form.Label>
                            <Form.Control type="text" name="url" value={metadata.url} onChange={handleMetadataChange} />
                        </Form.Group>
                        <Form.Group>
                            <Form.Label>Description</Form.Label>
                            <Form.Control type="text" name="description" value={metadata.description} onChange={handleMetadataChange} />
                        </Form.Group>
                        <Form.Group>
                            <Form.Label>Image URL</Form.Label>
                            <Form.Control type="text" name="imageUrl" value={metadata.imgURL} onChange={handleMetadataChange} />
                        </Form.Group>
                        <Form.Group>
                            <Form.Label>Publish Date</Form.Label>
                            <Form.Control type="text" name="publishDate" value={metadata.publishDate} onChange={handleMetadataChange} />
                        </Form.Group>
                        <Button variant="primary" onClick={handleSubmit}>Submit</Button>
                    </Form>
                </Modal.Body>
            </Modal>
        </div>
    );
};

export default DynamicForm;


