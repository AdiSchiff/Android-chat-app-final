package com.example.chatify.Repositories;

import static com.example.chatify.ViewModels.MyApplication.context;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.chatify.API.ContactsAPI;
import com.example.chatify.AppDB;
import com.example.chatify.Daos.ContactDao;
import com.example.chatify.Entities.Contact;
import com.example.chatify.Objects.LastMsg;
import com.example.chatify.Objects.UserInfo;
import com.example.chatify.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ContactsRepository {
    private ContactDao dao;
    private ContactListData contactListData;
    private ContactsAPI api;
    private String token;

    public ContactsRepository(String token) {
        this.token = token;
        AppDB db = AppDB.getInstance();
        dao = db.contactDao();
        contactListData = new ContactListData();
        api = ContactsAPI.getInstance();
    }

    public void logout() {
        dao.clear();
    }

    class ContactListData extends MutableLiveData<List<Contact>> {
        public ContactListData() {
            super();
            setValue(new LinkedList<>());
        }

        @Override
        protected void onActive() {

            super.onActive();
            new Thread(()->{
                contactListData.postValue(dao.getAll());
            }).start();
        }
    }

    public LiveData<List<Contact>> getAll() {
        //extract the contacts from the room
        return contactListData;
    }

    public void add(final String username) {
        CompletableFuture<Contact> future = api.addNewContact(token, username);
        future.thenAccept(contact -> {
            dao.insert(contact);
            reload();
        }).exceptionally(error -> {
            Toast.makeText(context, error.getCause().getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        });
    }

    public void delete(final int id) {
        CompletableFuture<String> future = api.deleteContact(token, id);
        future.thenAccept(result -> {
                    dao.delete(id);
                    reload();
        }).exceptionally(error -> {
            Toast.makeText(context, error.getCause().getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        });
    }

    public void reload() {
        CompletableFuture<List<Contact>> future = api.getChats(token);
        future.thenAccept(contacts -> {
            dao.clear();
            for (Contact c : contacts) {
                dao.insert(c);
            }
//            List<Contact> contactsTemp = dao.getAll();
            contactListData.setValue(contacts);
        }).exceptionally(error -> {
            Toast.makeText(context, error.getCause().getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        });
    }
}
