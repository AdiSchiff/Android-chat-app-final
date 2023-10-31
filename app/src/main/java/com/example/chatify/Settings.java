package com.example.chatify;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.chatify.API.ContactsAPI;
import com.example.chatify.API.MessagesAPI;
import com.example.chatify.API.TokenAPI;
import com.example.chatify.API.UserAPI;
import com.example.chatify.ViewModels.MyApplication;


public class Settings extends AppCompatActivity {

    Switch switcher;
    boolean darkMode;
    String token;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private final String defaultApi = "http://10.0.2.2:5000/api/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        token = getIntent().getStringExtra("token");

        getSupportActionBar().hide();
        switcher = findViewById(R.id.theme_switch);

        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        darkMode = sharedPreferences.getBoolean("dark", false);

        if(darkMode){
            switcher.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        switcher.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(darkMode){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("dark", false);
                    darkMode = false;
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("dark", true);
                    darkMode = true;
                }
                editor.apply();
            }
        });

        TextView serverAddress = findViewById(R.id.serverInput);

        Button changeButton = findViewById(R.id.changeBTN);
        changeButton.setOnClickListener(view -> {
            String address = serverAddress.getText().toString();
            if (address.equals("")){
                address = defaultApi;
            } else {
                String url = "http://" + address + "/api/";
                TokenAPI.getInstance().setRetrofit(url);
                UserAPI.getInstance().setRetrofit(url);
                MessagesAPI.getInstance().setRetrofit(url);
                ContactsAPI.getInstance().setRetrofit(url);
            }
        });

        // Initialize view
        ImageButton closeImageButton = findViewById(R.id.closeSettings);
        closeImageButton.setOnClickListener(view -> {
            Intent intent = new Intent(Settings.this, Contacts.class);
            intent.putExtra("token", token);
            startActivity(intent);
        });
    }
}
