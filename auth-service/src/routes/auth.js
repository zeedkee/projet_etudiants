const express = require('express');
const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');
const User = require('../models/User');

const router = express.Router();

// ========================
// POST /auth/register
// ========================
router.post('/register', async (req, res) => {
    try {
        const { username, password } = req.body;

        // Validation
        if (!username || !password) {
            return res.status(400).json({ message: 'Username et password sont obligatoires' });
        }

        if (password.length < 6) {
            return res.status(400).json({ message: 'Le mot de passe doit avoir au moins 6 caractères' });
        }

        // Vérifier si l'utilisateur existe déjà
        const existingUser = await User.findOne({ username });
        if (existingUser) {
            return res.status(409).json({ message: 'Ce nom d\'utilisateur existe déjà' });
        }

        // Hasher le mot de passe
        const hashedPassword = await bcrypt.hash(password, 10);

        // Créer l'utilisateur
        const user = await User.create({
            username,
            password: hashedPassword
        });

        res.status(201).json({
            id: user._id,
            username: user.username
        });

    } catch (error) {
        console.error('Erreur register:', error);
        res.status(500).json({ message: 'Erreur serveur' });
    }
});

// ========================
// POST /auth/login
// ========================
router.post('/login', async (req, res) => {
    try {
        const { username, password } = req.body;

        // Validation
        if (!username || !password) {
            return res.status(400).json({ message: 'Username et password sont obligatoires' });
        }

        // Chercher l'utilisateur
        const user = await User.findOne({ username });
        if (!user) {
            return res.status(401).json({ message: 'Identifiants invalides' });
        }

        // Vérifier le mot de passe
        const isPasswordValid = await bcrypt.compare(password, user.password);
        if (!isPasswordValid) {
            return res.status(401).json({ message: 'Identifiants invalides' });
        }

        // Générer le JWT
        const token = jwt.sign(
            {
                userId: user._id,
                username: user.username
            },
            process.env.JWT_SECRET,
            { expiresIn: '1h' }
        );

        res.status(200).json({ token });

    } catch (error) {
        console.error('Erreur login:', error);
        res.status(500).json({ message: 'Erreur serveur' });
    }
});

module.exports = router;