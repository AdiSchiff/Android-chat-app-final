package com.example.chatify.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.chatify.Entities.Contact;
import com.example.chatify.Entities.Message;
import com.example.chatify.Repositories.ContactsRepository;
import com.example.chatify.Repositories.MessagesRepository;

import java.util.List;

public class MessagesViewModel extends ViewModel {

    private final MessagesRepository messagesRepository;

    public MessagesViewModel(String token, int contactID) {
        messagesRepository = new MessagesRepository(token, contactID);
    }

    public LiveData<List<Message>> getMessages() {
        return messagesRepository.getAll();
    }

    public void sendMessage(String content) {
        messagesRepository.add(content);
    }

    public void reload() {
        messagesRepository.reload();
    }
}