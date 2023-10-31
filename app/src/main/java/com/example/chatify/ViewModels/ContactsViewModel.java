package com.example.chatify.ViewModels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.chatify.AppDB;
import com.example.chatify.Entities.Contact;
import com.example.chatify.Repositories.ContactsRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ContactsViewModel extends ViewModel {

    private final ContactsRepository contactsRepository;
    public ContactsViewModel(String token){
        contactsRepository = new ContactsRepository(token);
    }

    public LiveData<List<Contact>> getContacts() {
        return contactsRepository.getAll();
    }

    public void addNewContact(String username) {
        contactsRepository.add(username);
    }

    public void delete(int id) {
        contactsRepository.delete(id);
    }

    public void reload() {
        contactsRepository.reload();
    }

    public void logout() {
        contactsRepository.logout();
    }
}