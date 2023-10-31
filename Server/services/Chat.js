const Chat = require('../models/Chat');

async function defineId(chat){
    let currentId = await chat.findOne().sort({id:-1}).select('id').lean();
    currentId = currentId ? currentId.id : 0;
    return currentId+1;
}

const getChats = async (username) => {
    return await Chat.find({});
};

const getChatById = async (id) => {
    return await Chat.findById(id);
};

const deleteChatById = async (id) => {
    return await Chat.findOneAndDelete({id});
};

module.exports = { defineId, getChats, getChatById, deleteChatById }