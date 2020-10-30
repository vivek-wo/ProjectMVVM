package com.vivek.wo.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.vivek.wo.entity.User;
import com.vivek.wo.local.dao.UserDao;

@Database(entities = {User.class}, version = LocalDatabaseHolder.VERSION)
public abstract class LocalRoomDatabase extends RoomDatabase {

    //TODO 展示示例
    abstract UserDao userDao();
}
