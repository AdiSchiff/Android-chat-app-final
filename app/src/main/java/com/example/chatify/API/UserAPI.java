package com.example.chatify.API;

import com.example.chatify.Objects.User;
import com.example.chatify.Objects.UserInfo;
import com.example.chatify.R;
import com.example.chatify.ViewModels.MyApplication;

import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserAPI {
    private static UserAPI instance;
    private Retrofit retrofit;
    private WebServiceAPI webServiceAPI;

    private UserAPI() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public static UserAPI getInstance() {
        if (instance == null) {
            instance = new UserAPI();
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

    public CompletableFuture<String> registerUser(String username, String password, String nickname, String profilePicture) {
        User user = new User(username, password, nickname, profilePicture);
        Call<Void> call = this.webServiceAPI.registerUser(user);
        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 409) {
                    completableFuture.complete("This user is already registered");
                } else if (!response.isSuccessful()) {
                    completableFuture.complete("Something went wrong, try again");
                } else {
                    completableFuture.complete("ok");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
                completableFuture.completeExceptionally(t);
            }
        });

        return completableFuture;
    }

    public CompletableFuture<UserInfo> getUser(String token) {
        Call<UserInfo> call = this.webServiceAPI.getUser(token);
        CompletableFuture<UserInfo> future = new CompletableFuture<>();
        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if (response.isSuccessful()) {
                    UserInfo userInfo = new UserInfo(response.body().getUsername(), response.body().getDisplayName(), response.body().getProfilePic());
                    future.complete(userInfo);
                } else if (response.code() == 404) {
                    future.completeExceptionally(new Error("User was not found"));
                } else {
                    future.completeExceptionally(new Error("Invalid token"));
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {}
        });
        return future;
    }
}