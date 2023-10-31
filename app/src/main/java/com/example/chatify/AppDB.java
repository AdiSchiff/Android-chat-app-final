package com.example.chatify;

import static com.example.chatify.ViewModels.MyApplication.context;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.chatify.Daos.MessageDao;
import com.example.chatify.Daos.ContactDao;
import com.example.chatify.Entities.Message;
import com.example.chatify.Entities.Contact;
import com.example.chatify.ViewModels.MyApplication;

@Database(entities = {Contact.class, Message.class}, version = 1)
public abstract class AppDB extends RoomDatabase {
    private static AppDB instance;

    public abstract ContactDao contactDao();

    public abstract MessageDao messageDao();

    public static synchronized AppDB getInstance() {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    MyApplication.context,
                    AppDB.class,
                    "ChatDB"
            ).allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}
