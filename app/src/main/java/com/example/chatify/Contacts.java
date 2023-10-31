package com.example.chatify;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatify.Entities.Contact;
import com.example.chatify.ViewModels.ContactsViewModel;

import com.example.chatify.Adapters.ContactsListAdapter;
import com.example.chatify.ViewModels.ContactsViewModelFactory;


public class Contacts extends AppCompatActivity implements ContactsListAdapter.OnContactClickListener {
    String bearerToken;
    private boolean deleteFlag = false;
    private ContactsViewModel viewModel;

    ContactsListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts);
        bearerToken = getIntent().getStringExtra("token");

        RecyclerView lstContacts = findViewById(R.id.ContactsList);
        adapter = new ContactsListAdapter(this, this);
        lstContacts.setAdapter(adapter);
        lstContacts.setLayoutManager(new LinearLayoutManager(this));

        ContactsViewModelFactory factory = new ContactsViewModelFactory(bearerToken);
        viewModel = new ViewModelProvider(this, factory).get(ContactsViewModel.class);
        viewModel.reload();
        viewModel.getContacts().observe(this, contacts -> {
            if (contacts != null) {
                adapter.setContacts(contacts);
            }
        });

        // Initialize views
        ImageButton addContactBtn = findViewById(R.id.ContactsAddContact);
        ImageButton deleteContactBtn = findViewById(R.id.ContactsRemoveContact);
        ImageButton settingsBtn = findViewById(R.id.ContactsSettings);
        Button logoutBtn = findViewById(R.id.logoutBtn);

        // Set click listener for the add contact button
        addContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToAddContacts();
            }
        });

        // Set click listener for the delete contact button
        deleteContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFlag = true;
                showAlert("Please select a contact to delete");
            }
        });

        // Set click listener for the settings button
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Contacts.this, Settings.class);
                intent.putExtra("token", bearerToken);
                startActivity(intent);
            }
        });

        // Set click listener for the logout button
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.logout();
                finish();
            }
        });
    }

    @Override
    public void onResume() {

        super.onResume();
        viewModel.reload();
    }

    @Override
    public void onContactClick(Contact contact) {
        adapter.setSelectedContact(contact);
        if (deleteFlag) {
            navigateToDeleteContacts();
        } else {
            Intent intent = new Intent(Contacts.this, ChatPage.class);
            intent.putExtra("contactId", contact.getId());
            intent.putExtra("token", bearerToken);
            intent.putExtra("username", contact.getUsername());
            startActivity(intent);
        }
    }

    private void navigateToAddContacts() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.add_contact_window, null);
        builder.setView(dialogView);
        EditText usernameEditText = dialogView.findViewById(R.id.AddContactUsername);
        Button addContactButton = dialogView.findViewById(R.id.AddContactButton);
        AlertDialog dialog = builder.create();
        dialog.show();
        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                viewModel.addNewContact(username);
                dialog.dismiss(); // Close the dialog
            }
        });
    }


    private void navigateToDeleteContacts() {
        Contact selectedContact = adapter.getSelectedContact();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this contact?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        viewModel.delete(selectedContact.getId());
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
        adapter.setSelectedContact(null);
        deleteFlag = false;
    }

    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setTitle("Hello!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
