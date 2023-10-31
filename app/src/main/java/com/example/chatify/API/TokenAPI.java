package com.example.chatify.API;

import com.example.chatify.Objects.NameAndPassword;
import com.example.chatify.R;
import com.example.chatify.ViewModels.MyApplication;

import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TokenAPI {
    private static TokenAPI instance;
    private Retrofit retrofit;
    private WebServiceAPI webServiceAPI;

    private TokenAPI() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public void setRetrofit(String url){
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }
    public static TokenAPI getInstance() {
        if (instance == null) {
            instance = new TokenAPI();
        }
        return instance;
    }

    public CompletableFuture<String> createToken(String username, String password) {
        NameAndPassword user = new NameAndPassword(username, password);
        Call<String> call = this.webServiceAPI.createToken(user);
        CompletableFuture<String> future = new CompletableFuture<>();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    future.complete(response.body()); // extract the token from the server's response
                } else {
                    future.completeExceptionally(new Error("Incorrect username or password"));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace(); // Complete the CompletableFuture exceptionally with the thrown Throwable
            }
        });
        return future;
    }
}
