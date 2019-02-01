package com.udacity.ronanlima.capstoneproject.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.udacity.ronanlima.capstoneproject.data.Image;

import java.util.List;

/**
 * Created by rlima on 01/12/18.
 */

@Dao
public interface ImageDAO {
    @Query("SELECT * FROM image WHERE idProjeto = :idProjeto")
    List<Image> loadAllImagesFromProject(String idProjeto);

    @Query("SELECT id, idProjeto, uriImagem FROM image")
    List<Image> loadAllImages();

    @Insert
    void insertImage(Image image);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateImage(Image image);

    @Delete
    void deleteImage(Image image);
}
