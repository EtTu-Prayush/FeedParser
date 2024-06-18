import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import FeedPage from './FeedPage';
import HomePage from './HomePage';
import { Container, AppBar, Toolbar, Typography, Button } from '@mui/material';

function App() {
    return (
        <Router>
            <Container>
                <AppBar position="static">
                    <Toolbar>
                        <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
                            Feed Reader
                        </Typography>
                        {/* <Button color="inherit" href="/">Home</Button> */}
                        <Button color="inherit" href="/feeds">Feeds</Button>
                    </Toolbar>
                </AppBar>
                <Routes>
                    <Route path="/" element={<HomePage />} />
                    <Route path="/feeds" element={<FeedPage />} />
                </Routes>
            </Container>
        </Router>
    );
}

export default App;
