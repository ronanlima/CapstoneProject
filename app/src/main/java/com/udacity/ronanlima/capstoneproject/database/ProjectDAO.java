package com.udacity.ronanlima.capstoneproject.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.udacity.ronanlima.capstoneproject.data.Project;

import java.util.List;

/**
 * Created by rlima on 01/12/18.
 */

@Dao
public interface ProjectDAO {
    @Query("SELECT * FROM project")
    List<Project> loadAllProjects();

    @Insert
    void insertProject(Project project);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateProject(Project project);

    @Delete
    void deleteProject(Project project);
}
