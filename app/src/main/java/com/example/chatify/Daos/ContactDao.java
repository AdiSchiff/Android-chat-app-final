package com.example.chatify.Daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.chatify.Entities.Contact;

import java.util.List;

import retrofit2.http.DELETE;

@Dao
public interface ContactDao {
    @Query("SELECT * FROM contacts WHERE username=:username")
    Contact get(String username);

    @Insert
    void insert(Contact... contacts);

    @Query("SELECT * FROM contacts")
    List<Contact> getAll();

    @Query("DELETE FROM contacts")
    void clear();

    @Query("DELETE FROM contacts WHERE id=:id")
    void delete(int id);
}
