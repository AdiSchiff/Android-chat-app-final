package com.example.chatify.Repositories;

import static com.example.chatify.ViewModels.MyApplication.context;

import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.chatify.API.ContactsAPI;
import com.example.chatify.API.MessagesAPI;
import com.example.chatify.AppDB;
import com.example.chatify.Daos.ContactDao;
import com.example.chatify.Daos.MessageDao;
import com.example.chatify.Entities.Contact;
import com.example.chatify.Entities.Message;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MessagesRepository {
    private MessageDao dao;
    private MessageListData messageListData;
    private MessagesAPI api;
    private String token;
    private int contactID;

    public MessagesRepository(String token, int contactID) {
        this.token = token;
        AppDB db = AppDB.getInstance();
        dao = db.messageDao();
        messageListData = new MessageListData();
        api = MessagesAPI.getInstance();
        this.contactID = contactID;
    }

    class MessageListData extends MutableLiveData<List<Message>> {
        public MessageListData() {
            super();
            setValue(new LinkedList<Message>());
        }

        @Override
        protected void onActive() {

            super.onActive();
            new Thread(()->{
                messageListData.postValue(dao.getAll(contactID));
            }).start();
        }
    }

    public LiveData<List<Message>> getAll() {
        //extract the contacts from the room
        return messageListData;
    }

    public void add(final String content) {
        CompletableFuture<Message> future = api.sendMessage(token, contactID, content);
        future.thenAccept(message -> {
            dao.insert(message);
            reload();
        }).exceptionally(error -> {
            Toast.makeText(context, error.getCause().getMessage(), Toast.LENGTH_LONG).show();
            return null;
        });
    }

    public void reload() {
        CompletableFuture<List<Message>> future = api.getMessages(token, contactID);
        future.thenAccept(messages -> {
            dao.clear();
            for (Message m : messages) {
                dao.insert(m);
            }
            List<Message> messageTemp = dao.getAll(contactID);
            messageListData.postValue(messageTemp);
        }).exceptionally(error -> {
            Toast.makeText(context, error.getCause().getMessage(), Toast.LENGTH_LONG).show();
            return null;
        });
    }
}
