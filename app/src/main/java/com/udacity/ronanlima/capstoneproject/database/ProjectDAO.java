package com.udacity.ronanlima.capstoneproject.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.sqlite.SQLiteConstraintException;

import com.udacity.ronanlima.capstoneproject.data.Project;

import java.util.List;

/**
 * Created by rlima on 01/12/18.
 */

@Dao
public interface ProjectDAO {
    @Query("SELECT * FROM project WHERE id = :id")
    Project loadProjectById(String id);

    @Query("SELECT * FROM project")
    List<Project> loadAllProjects();

    @Query("SELECT COUNT(*) FROM project")
    int countProjects();

    @Insert
    void insertProject(Project project) throws SQLiteConstraintException;

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateProject(Project project);

    @Delete
    void deleteProject(Project project);

    @Query("SELECT * FROM project WHERE id = :id")
    Project loadFirstProject(String id);
}
