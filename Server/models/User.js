const mongoose = require('mongoose');

const Schema = mongoose.Schema;
const User = new Schema({
    username: {
        type: String,
    },
    password: {
        type: String,
    },
    displayName: {
        type: String,
    },
    profilePic: {
        type: String,
    },
});

const userInfo = {
    username: {
        type: String,
    },
    displayName: {
        type: String,
    },
    profilePic: {
        type: String,
    }
}
const user = mongoose.model('User',User);
module.exports = {user, userInfo};