package com.udacity.ronanlima.capstoneproject.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import com.udacity.ronanlima.capstoneproject.data.Image;
import com.udacity.ronanlima.capstoneproject.data.Project;

/**
 * Created by rlima on 01/12/18.
 */

@Database(entities = {Project.class, Image.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final String TAG = AppDatabase.class.getSimpleName().toUpperCase();
    public static final Object LOCK = new Object();
    public static final String DATABASE_NAME = "capstone";
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract ProjectDAO projectDAO();

    public abstract ImageDAO imageDAO();
}
