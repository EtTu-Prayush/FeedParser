// FeedList.js
import React, { useEffect, useState } from 'react';
import FeedCard from './FeedCard';
import FeedInputForm from './FeedInputForm';
import axios from 'axios';
import { Row, Col } from 'antd';

const FeedList = () => {
    const [feeds, setFeeds] = useState([]);

    useEffect(() => {
        fetchFeeds();
    }, []);

    const fetchFeeds = async () => {
        try {
            const response = await axios.get('http://localhost:4000/api/feeds');
            setFeeds(response.data);
        } catch (error) {
            console.error('Error fetching feeds:', error);
        }
    };

    return (
        <div>
            <FeedInputForm onSubmit={fetchFeeds} />
            <Row gutter={[16, 16]}>
                {feeds.map(feed => (
                    <Col xs={24} sm={12} md={8} key={feed.guid}>
                        <FeedCard feed={feed} />
                    </Col>
                ))}
            </Row>
        </div>
    );
};

export default FeedList;
