const ChatModel = require('../models/Chat');

const getMessageById = async (id) => {
    try {
        const chatExist = await ChatModel.findOne({id: id});
        if (!chatExist) {
            throw new Error('Chat was not found');
        }
        return chatExist.messages.reverse();
    } catch (error) {
        throw new Error('Error occurred while retrieving messages: ' + error.message);
    }
};

const createNewMessageById = async (id, sender, text) => {
    return await ChatModel.findOne({id: id}).then(
    async chatExist => {
        if (!chatExist) {
            return -1;
        } else {
            const newMessage = ({
                id: id,
                userInfo: sender,
                content: text
            });
            await chatExist.messages.push(newMessage);
            await chatExist.save();
            const addedMessage = chatExist.messages[chatExist.messages.length - 1];
            return {
                created: addedMessage.created, // Access the created date
                userInfo: sender,
                content: text
            };
        }
    })
}

module.exports = {getMessageById, createNewMessageById};
