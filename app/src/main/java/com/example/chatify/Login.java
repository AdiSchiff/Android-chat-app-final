package com.example.chatify;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.chatify.API.TokenAPI;

import java.util.concurrent.CompletableFuture;

public class Login extends Activity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;

    private TextView registerLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // Initialize views
        usernameEditText = findViewById(R.id.etLoginUsername);
        passwordEditText = findViewById(R.id.etLoginPassword);
        loginButton = findViewById(R.id.btnLogin);
        registerLink = findViewById(R.id.LoginLinkToRegister);

        // Set click listener for the login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToRegister();
            }
        });
    }

    private void handleLogin() {
        // Get username and password from the EditText fields
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        TokenAPI tokenAPI = TokenAPI.getInstance();
        CompletableFuture<String> tokenFuture = tokenAPI.createToken(username, password);

        tokenFuture.thenAccept(token -> {
            String bearerToken = "Bearer \"" + token + "\"";
            navigateToContacts(bearerToken);
        }).exceptionally(error -> {
            String errorMessage = error.getCause().getMessage();
            showAlert(errorMessage);
            return null;
        });
    }

    private void navigateToContacts(String token) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Start the Chat activity
                Intent intent = new Intent(Login.this, Contacts.class);
                intent.putExtra("token", token); // Pass the token as an extra
                startActivity(intent);
            }
        });
    }


    private void navigateToRegister() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Start the Register activity
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
                finish(); // Finish the current activity so that the user cannot navigate back to it
            }
        });
    }

    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setTitle("Error!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
