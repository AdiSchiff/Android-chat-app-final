const mongoose = require('mongoose');
const User = require('./User');

const Schema = mongoose.Schema;

const Message = new Schema({
    id: {
        type: Number,
    },
    created: {
        type: Date,
        default: Date.now
    },
    userInfo: {
        type: User.userInfo,
        required: true
    },
    content: {
        type: String,
        required: true,
    }
});

const Chat = new Schema({
    id: {
        type: Number,
        required: true
    },
    users: {
        type: [User.userInfo],
    },
    messages: [Message]
});

module.exports = mongoose.model('Chat', Chat);