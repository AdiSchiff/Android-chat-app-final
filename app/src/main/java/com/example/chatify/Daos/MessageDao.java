package com.example.chatify.Daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.chatify.Entities.Message;

import java.util.List;

@Dao
public interface MessageDao {
    @Query("SELECT * FROM messages WHERE contactId=:contactId")
    List<Message> getAll(int contactId);

    @Insert
    void insert(Message... messages);

    @Query("DELETE FROM messages")
    void clear();
}
