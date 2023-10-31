package com.example.chatify.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatify.Entities.Message;
import com.example.chatify.R;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MessagesListAdapter extends RecyclerView.Adapter<MessagesListAdapter.MessageViewHolder> {

    private static final int VIEW_TYPE_SENDER = 1;
    private static final int VIEW_TYPE_RECEIVER = 2;
    private final LayoutInflater mInflater;
    private List<Message> messages;
    private int contactId;

    class MessageViewHolder extends RecyclerView.ViewHolder {
        private final TextView sendContent;
        private final TextView sendCreated;
        private final TextView receivedContent;
        private final TextView receivedCreated;

        private MessageViewHolder(View itemView) {
            super(itemView);
            sendContent = itemView.findViewById(R.id.SentMessageContent);
            sendCreated = itemView.findViewById(R.id.SentMessageDate);
            receivedContent = itemView.findViewById(R.id.ReceivedMessageContent);
            receivedCreated = itemView.findViewById(R.id.ReceivedMessageDate);
        }
    }

    public MessagesListAdapter(Context context, int contactId) {
        mInflater = LayoutInflater.from(context);
        this.contactId = contactId;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        // Determine the layout based on the view type
        if (viewType == VIEW_TYPE_SENDER) {
            itemView = mInflater.inflate(R.layout.sent_message, parent, false);
        } else {
            itemView = mInflater.inflate(R.layout.recieved_message, parent, false);
        }

        return new MessageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        if (messages != null) {
            final Message current = messages.get(position);
            if (current.getContent() != null) {
                if (getItemViewType(position) == VIEW_TYPE_SENDER) {
                    holder.sendContent.setText(current.getContent());
                    holder.sendCreated.setText(current.getTimeSent());
                } else {
                    holder.receivedContent.setText(current.getContent());
                    holder.receivedCreated.setText(current.getTimeSent());
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setMessages(List<Message> s) {
        if (messages == null) {
            messages = new LinkedList<>();
        } else {
            messages.clear();
        }
        if (s != null) {
            Collections.reverse(s);
            messages.addAll(s);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (messages != null) {
            return messages.size();
        } else return 0;
    }

    public List<Message> getChats() {
        return messages;
    }

    @Override
    public int getItemViewType(int position) {
        final Message current = messages.get(position);
        if (current.getContactId() ==contactId ) {
            return VIEW_TYPE_SENDER;
        } else {
            return VIEW_TYPE_RECEIVER;
        }
    }
}
