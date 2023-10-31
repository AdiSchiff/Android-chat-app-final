const chatService = require('../services/Chat');
const Chat = require("../models/Chat");
const user = require("../services/User")
const loginController = require("./Login")
const {userInfo} = require("../models/User");

const createNewChat = async (req, res) => {

    const token = req.headers.authorization.split(' ')[1];
    const decodedUsername = await loginController.isLoggedIn(token);
    if (decodedUsername === -1) {
        return res.status(401).send()
    }
    //first we will create the users array
    const currentUser = await user.getUser(decodedUsername);
    const otherUser = await user.getUser(req.body.username);
    if (otherUser === null) {
        //there is no user registered with the given username
        return res.status(404).send();
    }
    if (currentUser.username === otherUser.username) {
        return res.status(403).send();
    }
    const talkers = [currentUser, otherUser];
    const newChat = new Chat({
        id: await chatService.defineId(Chat),
        users: talkers
    });
    await newChat.save();
    const finalRes = {
        id: newChat.id,
        userInfo: {
            username: otherUser.username,
            displayName: otherUser.displayName,
            profilePic: otherUser.profilePic
        }
    };
    return res.status(200).json(finalRes);
};

const getChats = async (req, res) => {
    const token = req.headers.authorization.split(' ')[1];
    const decodedUsername = await loginController.isLoggedIn(token);
    if (decodedUsername === -1) {
        return res.status(401).send()
    }
    const chatsArr = await chatService.getChats(decodedUsername);
    const tempChatsArr = Array.from(chatsArr);
    const conversations = [];
    let i = 0;
    for (; i < tempChatsArr.length; i++) {
        var otherUser;
        // Find the user that is not me
        if (tempChatsArr[i].users[0].username === decodedUsername) {
            otherUser = tempChatsArr[i].users[1];
        } else if (tempChatsArr[i].users[1].username === decodedUsername) {
            otherUser = tempChatsArr[i].users[0];
        } else {
            continue;
        }

        var lastMessage;
        if (tempChatsArr[i].messages.length > 0) {
            // If there are messages in the current chat
            const lastMsgIndex = tempChatsArr[i].messages.length - 1;
            lastMessage = {
                id: tempChatsArr[i].messages[lastMsgIndex].id,
                created: tempChatsArr[i].messages[lastMsgIndex].created,
                content: tempChatsArr[i].messages[lastMsgIndex].content
            };
        } else {
            // There are no messages in the chat
            lastMessage = null;
        }

        const conversation = {
            id: tempChatsArr[i].id,
            userInfo: {
                username: otherUser.username,
                displayName: otherUser.displayName,
                profilePic: otherUser.profilePic
            },
            lastMsg: lastMessage
        };

        conversations.push(conversation);
    }

    res.status(200).json(conversations);
};


const getChatById = async (req, res) => {
    const token = req.headers.authorization.split(' ')[1];
    if (loginController.isLoggedIn(token) !== -1) {
        const chat = await chatService.getChatById(req.params.id);
        if (!chat) {
            return res.status(404).send();
        }
        return res.status(200).json(chat);
    }
    res.status(401).send()
};

const deleteChatById = async (req, res) => {
    const token = req.headers.authorization.split(' ')[1];
    if (loginController.isLoggedIn(token) !== -1) {
        const chat = await chatService.deleteChatById(req.params.id);
        if (!chat) {
            return res.status(404).send();
        }
        return res.status(200).json(chat);
    }
    res.status(401).send();
}


module.exports = {createNewChat, getChats, getChatById, deleteChatById}