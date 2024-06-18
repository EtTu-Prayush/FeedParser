// FeedInputForm.js
import React, { useState } from 'react';
import axios from 'axios';
import { Input, Button, Form } from 'antd';

const FeedInputForm = ({ onSubmit }) => {
    const [url, setUrl] = useState('');

    const handleSubmit = async () => {
        try {
            await axios.post('http://localhost:4000/api/feeds', { url });
            onSubmit();
            setUrl('');
        } catch (error) {
            console.error('Error adding feed:', error);
        }
    };

    return (
        <Form onFinish={handleSubmit}>
            <Form.Item>
                <Input
                    value={url}
                    onChange={(e) => setUrl(e.target.value)}
                    placeholder="Enter feed URL"
                />
            </Form.Item>
            <Form.Item>
                <Button type="primary" htmlType="submit">
                    Add Feed
                </Button>
            </Form.Item>
        </Form>
    );
};

export default FeedInputForm;
