package com.example.chatify.API;

import com.example.chatify.Entities.Contact;
import com.example.chatify.Entities.Message;
import com.example.chatify.Objects.ChatsArray;
import com.example.chatify.Objects.ContactNoMsg;
import com.example.chatify.Objects.MessageArray;
import com.example.chatify.Objects.NameAndPassword;
import com.example.chatify.Objects.StringMessage;
import com.example.chatify.Objects.User;
import com.example.chatify.Objects.UserInfo;
import com.example.chatify.Objects.Username;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WebServiceAPI {
    @GET("Users")
    Call<UserInfo> getUser(@Header("Authorization") String token);

    @POST("Users")
    Call<Void> registerUser(@Body User user);

    @POST("Tokens")
    Call<String> createToken(@Body NameAndPassword nameAndPassword);

    @POST("Chats")
    Call<ContactNoMsg> addNewContact(@Header("Authorization") String token, @Body Username username);

    @GET("Chats")
    Call<List<ChatsArray>> getContacts(@Header("Authorization") String token);

    @DELETE("Chats/{id}")
    Call<Void> deleteContact(@Path("id") int id, @Header("Authorization") String token);

    @GET("Chats/{id}/Messages")
    Call<List<MessageArray>> getMessages(@Path("id") int id, @Header("Authorization") String token);

    @POST("Chats/{id}/Messages")
    Call<MessageArray> sendMessage(@Path("id") int id, @Header("Authorization") String token, @Body StringMessage msg);
}
