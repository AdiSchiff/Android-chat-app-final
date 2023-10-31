const User = require('../models/User');

const createNewUser = async (username, password, displayName, profilePic) => {
    const newUser = new User.user({
        "username": username,
        "password": password,
        "displayName": displayName,
        "profilePic": profilePic
    });
    return await newUser.save();
};

const getUser = async (username) => {
    return User.user.findOne({username: username});
};

module.exports = {createNewUser, getUser};

