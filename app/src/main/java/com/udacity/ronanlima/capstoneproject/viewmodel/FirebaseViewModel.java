package com.udacity.ronanlima.capstoneproject.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.udacity.ronanlima.capstoneproject.AppExecutors;
import com.udacity.ronanlima.capstoneproject.R;
import com.udacity.ronanlima.capstoneproject.VisivaArqService;
import com.udacity.ronanlima.capstoneproject.data.Image;
import com.udacity.ronanlima.capstoneproject.data.Project;
import com.udacity.ronanlima.capstoneproject.database.AppDatabase;

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
    private MutableLiveData<List<Project>> dataProject;
    private MutableLiveData<List<Image>> dataImage;

    public FirebaseViewModel(@NonNull Application application) {
        super(application);
        database = FirebaseDatabase.getInstance();
    }

    /**
     * Retrieve projects of Firebase Database for the first time the user open's the app and/or the
     * user make swipe gest.
     */
    public void retrieveProjects() {
        database.getReference(getApplication().getString(R.string.firebase_project_reference))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            List<Project> list = new ArrayList<>();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                list.add(getValue(snapshot));
                            }

                            getDataProject().setValue(list);
                            insertProjectData();
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

    /**
     * After obtain the data project, save it locally.
     */
    private void insertProjectData() {
        final List<Project> value = getDataProject().getValue();
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                for (Project p : value) {
                    AppDatabase.getInstance(getApplication()).projectDAO().insertProject(p);
                }
                Bundle bundle = new Bundle();
                bundle.putInt(VisivaArqService.BUNDLE_QUANT_PROJ, value.size());
                VisivaArqService.updateQuantProj(getApplication(), bundle);
            }
        });
    }

    /**
     * Retrive the image collection of selected project for the first time the user click on that
     * project.
     *
     * @param idProjeto
     */
    public void retrieveImages(final String idProjeto) {
        database.getReference(getApplication().getString(R.string.firebase_image_reference))
                .child(idProjeto)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            List<Image> list = new ArrayList<>();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Image value = getValue(snapshot, idProjeto);
                                list.add(value);
                            }

                            getDataImage().postValue(list);
                        } else {
                            getDataImage().postValue(null);
                        }
                    }

                    @NonNull
                    private Image getValue(DataSnapshot snapshot, String idProjeto) {
                        Image value = snapshot.getValue(Image.class);
                        value.setId(snapshot.getKey());
                        value.setIdProjeto(idProjeto);
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

    public MutableLiveData<List<Image>> getDataImage() {
        if (dataImage == null) {
            dataImage = new MutableLiveData<>();
        }
        return dataImage;
    }
}
