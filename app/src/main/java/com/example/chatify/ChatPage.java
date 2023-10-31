package com.example.chatify;

import static com.example.chatify.ViewModels.MyApplication.context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatify.API.MessagesAPI;
import com.example.chatify.Adapters.MessagesListAdapter;
import com.example.chatify.Daos.ContactDao;
import com.example.chatify.Entities.Contact;
import com.example.chatify.Entities.Message;
import com.example.chatify.ViewModels.MessagesViewModel;
import com.example.chatify.ViewModels.MessagesViewModelFactory;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.concurrent.CompletableFuture;

public class ChatPage extends AppCompatActivity {
    private int contactId;
    private String token;
    private MessagesViewModel viewModel;
    private MessagesListAdapter adapter;
    private ShapeableImageView profilePic;
    private TextView displayName;
    private ContactDao contactDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_page);
        token = getIntent().getStringExtra("token");
        contactId = getIntent().getIntExtra("contactId", 0);

        RecyclerView listMessages = findViewById(R.id.ChatPageMassages);
        adapter = new MessagesListAdapter(this, contactId);
        listMessages.setAdapter(adapter);
        listMessages.setLayoutManager(new LinearLayoutManager(this));

        MessagesViewModelFactory factory = new MessagesViewModelFactory(token, contactId);
        viewModel = new ViewModelProvider(this, factory).get(MessagesViewModel.class);
        viewModel.reload();
        viewModel.getMessages().observe(this, messages -> {
            if (messages != null && !messages.isEmpty()) {
                adapter.setMessages(messages);
            }
        });

        contactDao = AppDB.getInstance().contactDao();
        profilePic = findViewById(R.id.ChatPageContactProfilePicture);
        displayName = findViewById(R.id.ChatPageContactName);
        Contact temp = contactDao.get(getIntent().getStringExtra("username"));
        byte[] imageInBytes = Base64.decode(temp.getProfilePic(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageInBytes, 0, imageInBytes.length);
        profilePic.setImageBitmap(bitmap);
        displayName.setText(temp.getDisplayName());

        ImageButton sendButton = findViewById(R.id.ChatPageSendButton);
        ImageButton back = findViewById(R.id.ChatPageBackButton);


        // Set click listener for the send button
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSendButtonClicked();
            }
        });
        // Set click listener for the back button
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToContacts();
            }
        });

    }

    public void onSendButtonClicked() {
        EditText typingBox = findViewById(R.id.ChatPageTypingBox);
        String messageContent = typingBox.getText().toString();
        viewModel.sendMessage(messageContent);
        // Clear the typing box after sending the message
        typingBox.setText("");
    }

    public void navigateToContacts() {
        Intent intent = new Intent(ChatPage.this, Contacts.class);
        intent.putExtra("token", token);
        startActivity(intent);
    }

}