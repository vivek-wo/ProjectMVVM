package com.vivek.wo.local;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.vivek.wo.local.dao.UserDao;

public class LocalDatabaseHolder {
    private static final String DBNAME = "apartmentLock_database.db";
    // 数据库版本,默认2开始
    // 每增加数据库版本号需添加版本升级内容
    static final int VERSION = 2;

    private LocalRoomDatabase mDatabase;

    public static LocalDatabaseHolder get() {
        return Holder.INSTANCE;
    }

    //数据库初始化
    public void init(Context context) {
        mDatabase = Room.databaseBuilder(context.getApplicationContext(),
                LocalRoomDatabase.class, DBNAME)
                //重要：每增加数据库版本号需添加版本升级内容
//                .addMigrations(MIGRATION_2_3)
                .build();
    }

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
        }
    };

    public UserDao getUserDao() {
        return mDatabase.userDao();
    }

    private static class Holder {
        private static final LocalDatabaseHolder INSTANCE = new LocalDatabaseHolder();
    }
}
