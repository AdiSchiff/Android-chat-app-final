package com.example.chatify.ViewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class MessagesViewModelFactory implements ViewModelProvider.Factory {
    private final String token;
    private final int contactID;

    public MessagesViewModelFactory(String token, int contactID) {
        this.token = token;
        this.contactID = contactID;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MessagesViewModel.class)) {
            return (T) new MessagesViewModel(token, contactID);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
