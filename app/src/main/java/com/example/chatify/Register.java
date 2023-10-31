package com.example.chatify;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.chatify.API.UserAPI;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends Activity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText password2EditText;
    private EditText displayName;
    private Button uploadProfilePicture;
    private Button registerButton;
    private TextView loginLink;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ShapeableImageView profileImageView;
    private Uri selectedImageUri;
    private UserAPI userAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);


        // Initialize views
        usernameEditText = findViewById(R.id.etRegisterUsername);
        passwordEditText = findViewById(R.id.etRegisterPassword);
        password2EditText = findViewById(R.id.etRegisterPassword2);
        displayName = findViewById(R.id.etRegisterDisplayName);
        uploadProfilePicture = findViewById(R.id.RegisterChooseImageButton);
        registerButton = findViewById(R.id.btnRegister);
        loginLink = findViewById(R.id.RegisterLinkToLogIn);
        profileImageView = findViewById(R.id.RegisterProfilePicture);
        // Set click listener for the login button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRegister();
            }
        });
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToLogin();
            }
        });
        uploadProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    private void handleRegister() {
        // Get values from the EditText fields
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String repeatPassword = password2EditText.getText().toString();
        String nickname = displayName.getText().toString();
        String profilePicture = convertProfilePictureToString();

        if (username.isEmpty() || password.isEmpty() || repeatPassword.isEmpty() || nickname.isEmpty()) {
            showAlert("All fields are required");
        } else if (!password.equals(repeatPassword)) {
            showAlert("Passwords do not match");
        } else if (!isValidUsername(username)) {
            showAlert("Username not in format");
        } else if (!isValidPassword(password)) {
            showAlert("Password not in format");
        } else {
            userAPI = UserAPI.getInstance();
            CompletableFuture<String> future = userAPI.registerUser(username, password, nickname, profilePicture);
            future.thenAccept(status -> {
                if (status.equals("ok")) {
                    navigateToLogin();
                } else {
                    showAlert(status);
                }
            }).exceptionally(ex -> {
                showAlert("An error occurred: " + ex.getMessage());
                return null;
            });
        }
    }

    private boolean isValidUsername(String username) {
        String usernameRegex = "^(?=.*[A-Z])[A-Za-z0-9]{8,16}$";
        Pattern pattern = Pattern.compile(usernameRegex);
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }

    private boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[a-zA-Z])(?=.*[0-9]).{8,16}$";
        Pattern pattern = Pattern.compile(passwordRegex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    private void navigateToLogin() {
        // Start the Register activity
        Intent intent = new Intent(Register.this, Login.class);
        startActivity(intent);
        finish(); // Finish the current activity so that the user cannot navigate back to it
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                // Set the Bitmap to the profile image view
                profileImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
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

    private String convertProfilePictureToString() {
        Bitmap bitmap = ((BitmapDrawable) profileImageView.getDrawable()).getBitmap();
        ByteArrayOutputStream bas = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bas);
        byte[] imageBytes = bas.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }


}
