const express = require('express');
var app = express();
const http = require("http");
const {Server} = require("socket.io");
const bodyParser = require('body-parser');
app.use(bodyParser.urlencoded({extended: true}));
app.use(express.json({limit: "1000mb"}));
app.use(bodyParser.urlencoded({extended: true}));

const cors = require('cors');
app.use(cors());

const custumeEnv = require('custom-env');
custumeEnv.env(process.env.NODE_ENV, './config');

const mongoose = require('mongoose');
mongoose.connect(process.env.CONNECTION_STRING, {
    useNewUrlParser: true,
    useUnifiedTopology: true
});

app.use(express.static('public'));

const user = require('./routes/User')
app.use('/api/Users',user);

const login = require('./routes/Login')
app.use('/api/Tokens',login);

const chat = require('./routes/Chat')
app.use('/api/Chats',chat);

app.listen(process.env.PORT);
const server = http.createServer(app)
const io = new Server(server, {
    cors: {
        origin: "http://localhost:3000",
        methods: ["GET", "POST", "DELETE", "PUT"],
    },
});

io.on("connection", (socket) => {
    console.log(`User Connected: ${socket.id}`);
    socket.on("newMessage", (id) => {
        console.log("server received message " + id)
        socket.broadcast.emit('newMessage',id);
    });
    // socket.on("newContact", () => {
    //     console.log("received new contact")
    //     socket.broadcast.emit('newContact');
    // });
    // socket.on("deleteContact", (id) => {
    //     console.log("received delete")
    //     socket.broadcast.emit('deleteContact',id);
    // });
    // socket.on("disconnect", () => {
    //     console.log("User Disconnected", socket.id);
    // });
});

server.listen(3001, () => {
    console.log("SERVER IS RUNNING");
});