require('dotenv').config();
const express = require('express');
const axios = require('axios');
const path = require('path');

const app = express();
const PORT = process.env.PORT;
const SERVICE_URL = process.env.PASTE_SERVICE_URL;
const CREATE_EP = process.env.CREATE_ENDPOINT;
const GET_EP = process.env.GET_ENDPOINT;

app.use(express.json());
app.use(express.static('public'));

app.put('/api/create', async (req, res) => {
    try {
        const { paste } = req.body;
        const createTime = Date.now();
        
        const response = await axios.put(
            `${SERVICE_URL}/${CREATE_EP}`,
            { paste, create_time: createTime },
            { headers: { 'Content-Type': 'application/json' } }
        );
        res.json(response.data);
    } catch (error) {
        console.error('Create error:', error.message);
        res.status(error.response?.status || 500).json({ error: 'Failed to create paste' });
    }
});

app.get('/api/get/:id', async (req, res) => {
    try {
        const response = await axios.get(
            `${SERVICE_URL}/${GET_EP}/${req.params.id}`
        );
        res.json(response.data);
    } catch (error) {
        console.error('Get error:', error.message);
        res.status(error.response?.status || 500).json({ error: 'Paste not found' });
    }
});

app.listen(PORT, () => {
    console.log(`Web client running at http://localhost:${PORT}`);
});