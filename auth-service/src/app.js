require('dotenv').config();
const express = require('express');
const mongoose = require('mongoose');
const authRoutes = require('./routes/auth');

const app = express();
const PORT = process.env.PORT || 3001;
const MONGO_URI = process.env.MONGO_URI || 'mongodb://localhost:27017/authdb';

// Middleware
app.use(express.json());

// Routes
app.use('/auth', authRoutes);

// Health check
app.get('/health', (req, res) => {
    res.json({ status: 'UP', service: 'auth-service' });
});

// Connexion MongoDB puis démarrage du serveur
mongoose.connect(MONGO_URI)
    .then(() => {
        console.log('>>> Connecté à MongoDB');
        app.listen(PORT, () => {
            console.log(`>>> auth-service démarré sur le port ${PORT}`);
        });
    })
    .catch((err) => {
        console.error('Erreur connexion MongoDB:', err);
        process.exit(1);
    });