
// import React, { useEffect, useState } from 'react';
// import { Input, Button, Layout, Typography, Row, Col, Card, Pagination, Modal, Form } from 'antd';
// import axios from 'axios';
// import moment from 'moment';
// import './FeedPage.css';
// import DynamicDropdown from './DynamicDropdown';

// const { Header, Content } = Layout;
// const { TextArea } = Input;
// const { Title, Text, Link } = Typography;

// const FeedPage = () => {
//     const [feeds, setFeeds] = useState([]);
//     const [url, setUrl] = useState('');
//     const [currentPage, setCurrentPage] = useState(1);
//     const [isUnstructured, setIsUnstructured] = useState(false);
//     const [metadata, setMetadata] = useState({ title: '', description: '', link: '', imageUrl: '', publishDate: '' });
//     const [jsonContent, setJsonContent] = useState(null);
//     const feedsPerPage = 18;

//     useEffect(() => {
//         fetchFeeds();
//     }, []);

//     const fetchFeeds = async () => {
//         try {
//             const response = await axios.get('http://localhost:4000/api/feeds');
//             const updatedFeeds = response.data.map(feed => ({
//                 ...feed,
//                 epochTime: parseDate(feed.publishDate)
//             }));
//             const sortedFeeds = updatedFeeds.sort((a, b) => b.epochTime - a.epochTime);
//             setFeeds(sortedFeeds);
//         } catch (error) {
//             console.error('Error fetching feeds:', error);
//         }
//     };

//     const handleSubmit = async (e) => {
//         console.log("Button clicked")
//         e.preventDefault();
//         try {
//             const response = await axios.post('http://localhost:4000/api/feeds', { url });
//             if (response.data.status === 'unstructured') {
//                 setJsonContent(response.data.content);
//                 setIsUnstructured(true);
//             } else {
//                 setUrl('');
//                 fetchFeeds();
//             }
//         } catch (error) {
//             console.error('Error adding feed:', error);
//         }
//     };


//     const handleMetadataSubmit = async () => {
//         try {
//             const requestData = {
//                 url: url,
//                 metadata: metadata
//             };
    
//             await axios.post('http://localhost:4000/api/submit-metadata', requestData);
//             setIsUnstructured(false);
//             setUrl('');
//             setMetadata({ title: '', description: '', link: '', imageUrl: '', publishDate: '' });
//             fetchFeeds();
//             // const response = await axios.get('http://localhost:4000/api/feeds');
//             // const updatedFeeds = response.data.map(feed => ({
//             //     ...feed,
//             //     epochTime: parseDate(feed.publishDate)
//             // }));
//             // const sortedFeeds = updatedFeeds.sort((a, b) => b.epochTime - a.epochTime);
//             // setFeeds(sortedFeeds);
//         } catch (error) {
//             console.error('Error submitting metadata:', error);
//         }
//     };

//     const handleClearFeeds = async () => {
//         try {
//             await axios.post('http://localhost:4000/api/feeds/clear');
//             fetchFeeds();
//             const response = await axios.get('http://localhost:4000/api/feeds');
//             const updatedFeeds = response.data.map(feed => ({
//                 ...feed,
//                 epochTime: parseDate(feed.publishDate)
//             }));
//             const sortedFeeds = updatedFeeds.sort((a, b) => b.epochTime - a.epochTime);
//             setFeeds(sortedFeeds);
//         } catch (error) {
//             console.error('Error clearing feeds:', error);
//         }
//     };

//     const indexOfLastFeed = currentPage * feedsPerPage;
//     const indexOfFirstFeed = indexOfLastFeed - feedsPerPage;
//     const currentFeeds = feeds.slice(indexOfFirstFeed, indexOfLastFeed);

//     const handlePageChange = (page) => {
//         setCurrentPage(page);
//     };

//     const parseDate = (dateString) => {
//         const formats = [
//             "YYYY-MM-DDTHH:mm:ssZ",
//             "ddd, DD MMM YYYY HH:mm:ss ZZ",
//             "YYYY-MM-DDTHH:mm:ssZ",
//             "ddd, DD MMM YYYY HH:mm:ss ZZ",
//             "YYYY-MM-DDTHH:mm:ss[Z]"
//         ];
//         return moment(dateString, formats, true).valueOf();
//     };

//     const handleFieldChange = (field) => (value) => {
//         setMetadata({ ...metadata, [field]: value });
//     };

//     return (
//         <Layout className="feed-page">
//             <Header className="feed-page-header">
//                 <div className="overlay"></div>
//                 <div className="header-content">
//                     <Title level={1} className="title-text">My Feed</Title>
//                 </div>
//             </Header>
//             <Content className="feed-page-content">
//                 <div className="form-container">
//                     <form onSubmit={handleSubmit}>
//                         <TextArea
//                             placeholder="Enter feed URL"
//                             value={url}
//                             onChange={(e) => setUrl(e.target.value)}
//                             autoSize={{ minRows: 1, maxRows: 1 }}
//                         />
//                         <div className="button-group">
//                             <Button type="primary" htmlType="submit" style={{ backgroundColor: 'black', borderColor: 'black' }}>Add Feed</Button>
//                             <Button type="danger" onClick={handleClearFeeds}>Clear Feeds</Button>
//                         </div>
//                     </form>
//                 </div>
//                 <Row gutter={[16, 16]} className="feed-list">
//                     {currentFeeds.map((feed) => (
//                         <Col key={feed.guid} xs={24} sm={12} md={8}>
//                             <Card
//                                 hoverable
//                                 cover={feed.imgURL && <img src={feed.imgURL} alt="Feed Image" />}
//                             >
//                                 <Card.Meta
//                                     title={<Link href={feed.link} target="_blank">{feed.title}</Link>}
//                                     description={
//                                         <>
//                                             <Text>
//                                                 {feed.description.length > 100
//                                                     ? `${feed.description.substring(0, 100)}...`
//                                                     : feed.description}
//                                             </Text>
//                                             <br />
//                                             <Text type="secondary">
//                                                 Published on: {moment(feed.epochTime).format('MMMM DD, YYYY')}
//                                             </Text>
//                                         </>
//                                     }
//                                 />
//                             </Card>
//                         </Col>
//                     ))}
//                 </Row>
//                 <Modal
//                     title="Provide Metadata"
//                     visible={isUnstructured}

//                     onOk={handleMetadataSubmit} 
//                     onCancel={() => setIsUnstructured(false)}
//                 >
//                     <Form layout="vertical">
//                         <Form.Item label="Title">
//                             <DynamicDropdown
//                                 jsonContent={jsonContent}
//                                 onChange={handleFieldChange('title')}
//                             />
//                         </Form.Item>
//                         <Form.Item label="Description">
//                             <DynamicDropdown
//                                 jsonContent={jsonContent}
//                                 onChange={handleFieldChange('description')}
//                             />
//                         </Form.Item>
//                         <Form.Item label="Link">
//                             <DynamicDropdown
//                                 jsonContent={jsonContent}
//                                 onChange={handleFieldChange('link')}
//                             />
//                         </Form.Item>
//                         <Form.Item label="Image URL">
//                             <DynamicDropdown
//                                 jsonContent={jsonContent}
//                                 onChange={handleFieldChange('imageUrl')}
//                             />
//                         </Form.Item>
//                         <Form.Item label="Publish Date">
//                             <DynamicDropdown
//                                 jsonContent={jsonContent}
//                                 onChange={handleFieldChange('publishDate')}
//                             />
//                         </Form.Item>
//                     </Form>
//                 </Modal>
//                 {/* <Pagination
//                     current={currentPage}
//                     // pageSize={feedsPerPage}
//                     pageSize={100000}
//                     total={feeds.length}
//                     onChange={handlePageChange}
//                     className="feed-pagination"
//                 /> */}
                
//             </Content>
//         </Layout>
//     );
// };

// export default FeedPage;





import React, { useEffect, useState } from 'react';
import { Input, Button, Layout, Typography, Row, Col, Card, Modal, Form, notification } from 'antd';
import axios from 'axios';
import moment from 'moment';
import './FeedPage.css';
import DynamicDropdown from './DynamicDropdown';

const { Header, Content } = Layout;
const { TextArea } = Input;
const { Title, Text, Link } = Typography;

const FeedPage = () => {
    const [feeds, setFeeds] = useState([]);
    const [url, setUrl] = useState('');
    const [currentPage, setCurrentPage] = useState(1);
    const [isUnstructured, setIsUnstructured] = useState(false);
    const [metadata, setMetadata] = useState({ title: '', description: '', link: '', imageUrl: '', publishDate: '' });
    const [jsonContent, setJsonContent] = useState(null);
    const feedsPerPage = 18;

    useEffect(() => {
        fetchFeeds();
    }, []);

    const fetchFeeds = async () => {
        try {
            const response = await axios.get('http://localhost:4000/api/feeds');
            const updatedFeeds = response.data.map(feed => ({
                ...feed,
                epochTime: parseDate(feed.publishDate)
            }));
            const sortedFeeds = updatedFeeds.sort((a, b) => b.epochTime - a.epochTime);
            setFeeds(sortedFeeds);
        } catch (error) {
            console.error('Error fetching feeds:', error);
        }
    };

    const isValidUrl = (string) => {
        const res = string.match(/(http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/);
        return (res !== null);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!isValidUrl(url)) {
            notification.error({
                message: 'Invalid URL',
                description: 'Please enter a valid URL.',
            });
            return;
        }

        try {
            const response = await axios.post('http://localhost:4000/api/feeds', { url });
            if (response.data.status === 'unstructured') {
                setJsonContent(response.data.content);
                setIsUnstructured(true);
            } else {
                setUrl('');
                fetchFeeds();
            }
        } catch (error) {
            console.error('Error adding feed:', error);
        }
    };

    const handleMetadataSubmit = async () => {
        try {
            const requestData = {
                url: url,
                metadata: metadata
            };

            await axios.post('http://localhost:4000/api/submit-metadata', requestData);
            setIsUnstructured(false);
            setUrl('');
            setMetadata({ title: '', description: '', link: '', imageUrl: '', publishDate: '' });
            fetchFeeds();
        } catch (error) {
            console.error('Error submitting metadata:', error);
        }
    };

    const handleClearFeeds = async () => {
        try {
            await axios.post('http://localhost:4000/api/feeds/clear');
            fetchFeeds();
        } catch (error) {
            console.error('Error clearing feeds:', error);
        }
    };

    const indexOfLastFeed = currentPage * feedsPerPage;
    const indexOfFirstFeed = indexOfLastFeed - feedsPerPage;
    const currentFeeds = feeds.slice(indexOfFirstFeed, indexOfLastFeed);

    const handlePageChange = (page) => {
        setCurrentPage(page);
    };

    const parseDate = (dateString) => {
        const formats = [
            "YYYY-MM-DDTHH:mm:ssZ",
            "ddd, DD MMM YYYY HH:mm:ss ZZ",
            "YYYY-MM-DDTHH:mm:ssZ",
            "ddd, DD MMM YYYY HH:mm:ss ZZ",
            "YYYY-MM-DDTHH:mm:ss[Z]"
        ];
        return moment(dateString, formats, true).valueOf();
    };

    const handleFieldChange = (field) => (value) => {
        setMetadata({ ...metadata, [field]: value });
    };

    return (
        <Layout className="feed-page">
            <Header className="feed-page-header">
                <div className="overlay"></div>
                <div className="header-content">
                    <Title level={1} className="title-text">My Feed</Title>
                </div>
            </Header>
            <Content className="feed-page-content">
                <div className="form-container">
                    <form onSubmit={handleSubmit}>
                        <TextArea
                            placeholder="Enter feed URL"
                            value={url}
                            onChange={(e) => setUrl(e.target.value)}
                            autoSize={{ minRows: 1, maxRows: 1 }}
                        />
                        <div className="button-group">
                            <Button type="primary" htmlType="submit" style={{ backgroundColor: 'black', borderColor: 'black' }}>Add Feed</Button>
                            <Button type="danger" onClick={handleClearFeeds}>Clear Feeds</Button>
                        </div>
                    </form>
                </div>
                <Row gutter={[16, 16]} className="feed-list">
                    {currentFeeds.map((feed) => (
                        <Col key={feed.guid} xs={24} sm={12} md={8}>
                            <Card
                                hoverable
                                cover={feed.imgURL && <img src={feed.imgURL} alt="Feed Image" />}
                            >
                                <Card.Meta
                                    title={<Link href={feed.link} target="_blank">{feed.title}</Link>}
                                    description={
                                        <>
                                            <Text>
                                                {feed.description.length > 100
                                                    ? `${feed.description.substring(0, 100)}...`
                                                    : feed.description}
                                            </Text>
                                            <br />
                                            <Text type="secondary">
                                                Published on: {moment(feed.epochTime).format('MMMM DD, YYYY')}
                                            </Text>
                                        </>
                                    }
                                />
                            </Card>
                        </Col>
                    ))}
                </Row>
                <Modal
                    title="Provide Metadata"
                    visible={isUnstructured}
                    onOk={handleMetadataSubmit}
                    onCancel={() => setIsUnstructured(false)}
                >
                    <Form layout="vertical">
                        <Form.Item label="Title">
                            <DynamicDropdown
                                jsonContent={jsonContent}
                                onChange={handleFieldChange('title')}
                            />
                        </Form.Item>
                        <Form.Item label="Description">
                            <DynamicDropdown
                                jsonContent={jsonContent}
                                onChange={handleFieldChange('description')}
                            />
                        </Form.Item>
                        <Form.Item label="Link">
                            <DynamicDropdown
                                jsonContent={jsonContent}
                                onChange={handleFieldChange('link')}
                            />
                        </Form.Item>
                        <Form.Item label="Image URL">
                            <DynamicDropdown
                                jsonContent={jsonContent}
                                onChange={handleFieldChange('imageUrl')}
                            />
                        </Form.Item>
                        <Form.Item label="Publish Date">
                            <DynamicDropdown
                                jsonContent={jsonContent}
                                onChange={handleFieldChange('publishDate')}
                            />
                        </Form.Item>
                    </Form>
                </Modal>
            </Content>
        </Layout>
    );
};

export default FeedPage;
