package com.example.chatify.API;

import static com.example.chatify.ViewModels.MyApplication.context;

import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.chatify.Daos.ContactDao;
import com.example.chatify.Entities.Contact;
import com.example.chatify.Objects.ChatsArray;
import com.example.chatify.Objects.ContactNoMsg;
import com.example.chatify.Objects.LastMsg;
import com.example.chatify.Objects.UserInfo;
import com.example.chatify.Objects.Username;
import com.example.chatify.R;
import com.example.chatify.ViewModels.MyApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ContactsAPI {
    private static ContactsAPI instance;
    private Retrofit retrofit;
    private WebServiceAPI webServiceAPI;

    private ContactsAPI() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public static ContactsAPI getInstance() {
        if (instance == null) {
            instance = new ContactsAPI();
        }
        return instance;
    }

    public void setRetrofit(String url){
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public CompletableFuture<List<Contact>> getChats(String token) {
        Call<List<ChatsArray>> call = this.webServiceAPI.getContacts(token);
        CompletableFuture<List<Contact>> future = new CompletableFuture<>();

        call.enqueue(new Callback<List<ChatsArray>>() {
            @Override
            public void onResponse(Call<List<ChatsArray>> call, Response<List<ChatsArray>> response) {
                if (response.isSuccessful()) {
                    List<Contact> contacts = new ArrayList<>();
                    List<ChatsArray> responseList = response.body();
                    for (ChatsArray responseContact : responseList) {
                        int id = responseContact.getId();
                        UserInfo userInfo = new UserInfo(responseContact.getUserInfo().getUsername(), responseContact.getUserInfo().getDisplayName(), responseContact.getUserInfo().getProfilePic());
                        LastMsg lastMsg;
                        if (responseContact.getLastMsg() != null) {
                            lastMsg = new LastMsg(responseContact.getLastMsg().getId(), responseContact.getLastMsg().getCreated(), responseContact.getLastMsg().getContent());
                        } else {
                            lastMsg = null;
                        }
                        Contact contact = new Contact(id, userInfo, lastMsg);
                        contacts.add(contact);
                    }
                    future.complete(contacts);
                } else {
                    future.completeExceptionally(new Error("Invalid token"));
                }
            }

            @Override
            public void onFailure(Call<List<ChatsArray>> call, Throwable t) {
            }
        });
        return future;
    }

    public CompletableFuture<Contact> addNewContact(String token, String username) {
        Username usernameObj = new Username(username);
        Call<ContactNoMsg> call = this.webServiceAPI.addNewContact(token, usernameObj);
        CompletableFuture<Contact> future = new CompletableFuture<>();
        call.enqueue(new Callback<ContactNoMsg>() {
            @Override
            public void onResponse(Call<ContactNoMsg> call, Response<ContactNoMsg> response) {
                if (response.isSuccessful()) {
                    UserInfo userInfo = new UserInfo(response.body().getUserInfo().getUsername(), response.body().getUserInfo().getDisplayName(), response.body().getUserInfo().getProfilePic());
                    Contact newContact = new Contact(response.body().getId(), userInfo, null);
                    future.complete(newContact); // extract the new contact from the server's response
                } else if (response.code() == 403) {
                    future.completeExceptionally(new Error("Wrong username"));
                } else if (response.code() == 404) {
                    future.completeExceptionally(new Error("There is no such user"));
                } else {
                    future.completeExceptionally(new Error("invalid_token"));
                }
            }

            @Override
            public void onFailure(Call<ContactNoMsg> call, Throwable t) {
                t.printStackTrace(); // Complete the CompletableFuture exceptionally with the thrown Throwable
            }
        });
        return future;
    }

    public CompletableFuture<String> deleteContact(String token, int id) {
        Call<Void> call = this.webServiceAPI.deleteContact(id, token);
        CompletableFuture<String> future = new CompletableFuture<>();
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    future.complete("ok");
                } else if (response.code() == 404) {
                    future.completeExceptionally(new Error("There is no chat with this contact"));
                } else {
                    future.completeExceptionally(new Error("invalid token"));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace(); // Complete the CompletableFuture exceptionally with the thrown Throwable
            }
        });
        return future;
    }
}