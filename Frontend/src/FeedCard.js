import React from 'react';
import { Card, CardContent, Typography, Link, CardMedia } from '@mui/material';
import { LazyLoadImage } from 'react-lazy-load-image-component';
import 'react-lazy-load-image-component/src/effects/blur.css';

const FeedCard = ({ feed }) => (
    <Card style={{ margin: '10px', height: '100%' }}>
        {feed.imgURL ? (
            <CardMedia>
                <LazyLoadImage
                    effect="blur"
                    src={feed.imgURL} // Assuming feed.imgURL contains the image URL
                    style={{ height: '140px', width: '100%', objectFit: 'cover' }}
                />
            </CardMedia>
        ) : (
            <CardMedia style={{ height: '140px', width: '100%' }} />
        )}
        <CardContent>
            <Typography gutterBottom variant="h5" component="div">
                <Link href={feed.link} target="_blank" rel="noopener">{feed.title}</Link>
            </Typography>
            <Typography variant="body2" color="text.secondary">
                {feed.description.length > 100 ? feed.description.substring(0, 100) + '...' : feed.description}
            </Typography>
        </CardContent>
    </Card>
);

export default FeedCard;
