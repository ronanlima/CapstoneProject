package com.udacity.ronanlima.capstoneproject.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.udacity.ronanlima.capstoneproject.R;
import com.udacity.ronanlima.capstoneproject.data.Project;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rlima on 27/11/18.
 * <p>
 * This class was built to manage the connection to the firebase database and display the data in
 * the view you requested, without this view knowing how the data is retrieved.
 * </p>
 */

public class FirebaseViewModel extends AndroidViewModel {
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private DatabaseReference projectReference;
    private MutableLiveData<List<Project>> dataProject;

    public FirebaseViewModel(@NonNull Application application) {
        super(application);
        database = FirebaseDatabase.getInstance();
        projectReference = database.getReference(application.getString(R.string.firebase_project_reference));
    }

    public void retrieveProjects() {
        projectReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<Project> list = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        list.add(getValue(snapshot));
                    }

                    getDataProject().postValue(list);
                }
            }

            @NonNull
            private Project getValue(DataSnapshot snapshot) {
                Project value = snapshot.getValue(Project.class);
                value.setId(snapshot.getKey());
                return value;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public MutableLiveData<List<Project>> getDataProject() {
        if (dataProject == null) {
            dataProject = new MutableLiveData<>();
        }
        return dataProject;
    }
}
