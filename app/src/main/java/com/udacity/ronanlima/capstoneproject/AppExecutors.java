package com.udacity.ronanlima.capstoneproject;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import lombok.Getter;

/**
 * Created by rlima on 08/12/18.
 */

public class AppExecutors {
    private static final Object LOCK = new Object();
    private static final int N_THREADS = 3;
    private static AppExecutors sInstance;
    @Getter
    private final Executor diskIO;
    @Getter
    private final Executor mainThread;
    @Getter
    private final Executor networkIO;

    private AppExecutors(Executor diskIO, Executor mainThread, Executor networkIO) {
        this.diskIO = diskIO;
        this.mainThread = mainThread;
        this.networkIO = networkIO;
    }

    public static AppExecutors getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new AppExecutors(Executors.newSingleThreadExecutor()
                        , new MainThreadExecutor()
                        , Executors.newFixedThreadPool(N_THREADS));
            }
        }

        return sInstance;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
