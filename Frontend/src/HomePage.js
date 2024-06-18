// HomePage.js
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Input, Button, Typography, Form, Layout } from 'antd';
import axios from 'axios';

const { Content } = Layout;

const HomePage = () => {
    const [url, setUrl] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async () => {
        try {
            await axios.post('http://localhost:4000/api/feeds', { url });
            navigate('/feeds');
        } catch (error) {
            console.error('Error adding feed:', error);
        }
    };

    return (
        <Layout style={{ padding: '24px' }}>
            <Content>
                <Typography.Title level={2}>Welcome to the Feed Reader</Typography.Title>
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
            </Content>
        </Layout>
    );
};

export default HomePage;
