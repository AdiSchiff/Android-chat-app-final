const chatController = require('../controllers/Chat');
const messageController = require('../controllers/Message');

const express = require('express');
var router = express.Router();

router.route('/')
    .get(chatController.getChats)
    .post(chatController.createNewChat);
router.route('/:id')
    .get(chatController.getChatById)
    .delete(chatController.deleteChatById);
router.route('/:id/Messages')
    .get(messageController.getMessageById)
    .post(messageController.createNewMessageById);

module.exports = router;