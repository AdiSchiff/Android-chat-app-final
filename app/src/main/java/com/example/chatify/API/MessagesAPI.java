package com.example.chatify.API;

import com.example.chatify.Entities.Message;
import com.example.chatify.Objects.MessageArray;
import com.example.chatify.Objects.StringMessage;
import com.example.chatify.Objects.UserInfo;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MessagesAPI {
    private static MessagesAPI instance;
    private Retrofit retrofit;
    private WebServiceAPI webServiceAPI;

    private MessagesAPI() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public static MessagesAPI getInstance() {
        if (instance == null) {
            instance = new MessagesAPI();
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

    public CompletableFuture<List<Message>> getMessages(String token, int id) {
        Call<List<MessageArray>> call = this.webServiceAPI.getMessages(id, token);
        CompletableFuture<List<Message>> future = new CompletableFuture<>();

        call.enqueue(new Callback<List<MessageArray>>() {
            @Override
            public void onResponse(Call<List<MessageArray>> call, Response<List<MessageArray>> response) {
                if (response.isSuccessful()) {
                    List<Message> messages = new ArrayList<>();
                    List<MessageArray> responseList = response.body();
                    for (MessageArray responseMessage : responseList) {
                        Date created = responseMessage.getCreated();
                        UserInfo userInfo = new UserInfo(responseMessage.getUserInfo().getUsername(), responseMessage.getUserInfo().getDisplayName(), responseMessage.getUserInfo().getProfilePic());
                        String content = responseMessage.getContent();
                        Message message = new Message(id, content, created.toString(), userInfo);
                        messages.add(message);
                    }
                    future.complete(messages);
                } else {
                    future.completeExceptionally(new Error("Invalid token"));
                }
            }

            @Override
            public void onFailure(Call<List<MessageArray>> call, Throwable t) {
            }
        });
        return future;
    }

    public CompletableFuture<Message> sendMessage(String token, int id, String content) {
        StringMessage stringMessage = new StringMessage(content);
        Call<MessageArray> call = this.webServiceAPI.sendMessage(id, token, stringMessage);
        CompletableFuture<Message> future = new CompletableFuture<>();

        call.enqueue(new Callback<MessageArray>() {
            @Override
            public void onResponse(Call<MessageArray> call, Response<MessageArray> response) {
                if (response.isSuccessful()) {
                    MessageArray messageArray = response.body();
                    Date created = messageArray.getCreated();
                    UserInfo userInfo = new UserInfo(messageArray.getUserInfo().getUsername(), messageArray.getUserInfo().getDisplayName(), messageArray.getUserInfo().getProfilePic());
                    String content = messageArray.getContent();
                    Message message = new Message(id, content, created.toString(), userInfo);
                    future.complete(message);
                } else {
                    future.completeExceptionally(new Error("Invalid token"));
                }
            }

            @Override
            public void onFailure(Call<MessageArray> call, Throwable t) {
            }
        });
        return future;
    }
}
