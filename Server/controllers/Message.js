const messageService = require('../services/Message');
const userService = require('../services/User');
const loginController = require("./Login");

const getMessageById = async (req, res) => {
    const token = req.headers.authorization.split(' ')[1];
    const decodedUsername = loginController.isLoggedIn(token);
    if(decodedUsername === -1){
        return res.status(404).send()
    }
    const response = await messageService.getMessageById(req.params.id);
    res.status(200).json(response);
}

const createNewMessageById = async (req, res) => {
    const token = req.headers.authorization.split(' ')[1];
    const decodedUsername = loginController.isLoggedIn(token);
    if(decodedUsername === -1){
        return res.status(404).send()
    }
    const userInfo = await userService.getUser(decodedUsername);
    if(!userInfo){
        res.status(404).send();
    }
    const sender = {
        username: userInfo.username,
        displayName: userInfo.displayName,
        profilePic: userInfo.profilePic
    };
    const response = await messageService.createNewMessageById(req.params.id, sender, req.body.msg)
    if(response !== -1){
        res.status(200).json(response);
    }
}

module.exports = {getMessageById, createNewMessageById};
