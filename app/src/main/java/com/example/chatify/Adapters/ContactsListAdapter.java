package com.example.chatify.Adapters;

import static com.example.chatify.ViewModels.MyApplication.context;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatify.Entities.Contact;
import com.example.chatify.R;

import java.io.File;
import java.util.List;

public class ContactsListAdapter extends RecyclerView.Adapter<ContactsListAdapter.ContactViewHolder> {

    public interface OnContactClickListener {
        void onContactClick(Contact contact);
    }

    class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView displayName;
        private final ImageView profilePic;
        private final TextView lastMsgContent;
        private final TextView lastMsgCreated;

        private ContactViewHolder(View itemView, final OnContactClickListener contactClickListener) {
            super(itemView);
            displayName = itemView.findViewById(R.id.Contact_item_display_name);
            profilePic = itemView.findViewById(R.id.Contact_item_contact_profile_picture);
            lastMsgContent = itemView.findViewById(R.id.Contact_item_last_message);
            lastMsgCreated = itemView.findViewById(R.id.Contact_item_timestamp);
            itemView.setOnClickListener(this);

            // Set click listener on item view
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Contact clickedContact = contacts.get(position);
                        contactClickListener.onContactClick(clickedContact); // Notify the listener
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            // Retrieve the clicked contact from the list based on the clicked position
            selectedContact = contacts.get(getAdapterPosition());
            notifyDataSetChanged(); // Notify the adapter that the data has changed
        }

    }

    private final LayoutInflater mInflater;
    private List<Contact> contacts;

    private Contact selectedContact;

    private OnContactClickListener contactClickListener;


    public ContactsListAdapter(Context context, OnContactClickListener contactClickListener) {
        this.contactClickListener = contactClickListener;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.contact_item, parent, false);
        return new ContactViewHolder(itemView, contactClickListener);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        if (contacts != null) {
            final Contact current = contacts.get(position);
            if (current.getUsername() != null) {
                holder.displayName.setText(current.getDisplayName());
                byte[] imageInBytes = Base64.decode(current.getProfilePic(), Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageInBytes, 0, imageInBytes.length);
                holder.profilePic.setImageBitmap(bitmap);
            } else {
                holder.displayName.setText("");
                holder.profilePic.setImageBitmap(null);
            }
            if (current.getContent() == null) {
                holder.lastMsgContent.setText("");
                holder.lastMsgCreated.setText("");
            } else {
                holder.lastMsgContent.setText(current.getContent());
                holder.lastMsgCreated.setText(current.getCreated());
            }
            if (current.equals(selectedContact)) {
                // Apply selected state styling
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, androidx.cardview.R.color.cardview_shadow_start_color));
            } else {
                holder.itemView.setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }

    public void setContacts(List<Contact> s) {
        contacts = s;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (contacts != null) {
            return contacts.size();
        } else return 0;
    }

    public Contact getSelectedContact() {
        return selectedContact;
    }

    public void setSelectedContact(Contact s) {
        selectedContact = s;
    }

    public List<Contact> getContacts() {
        return contacts;
    }
}