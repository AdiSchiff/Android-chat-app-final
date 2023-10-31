const express = require('express');
const router = express.Router();

const userController = require('../controllers/User');

router.route('/').post(userController.createNewUser);
router.route('/:username').get(userController.getUser);

module.exports = router;
