package com.udacity.ronanlima.capstoneproject.util;

import android.content.Context;

import com.udacity.ronanlima.capstoneproject.data.Image;
import com.udacity.ronanlima.capstoneproject.database.AppDatabase;

import java.util.List;
import java.util.Random;

/**
 * Created by rlima on 01/02/19.
 */

public class Util {

    /**
     * Return index of image.
     *
     * @param context
     * @return
     */
    public static Integer indexFromImage(Context context) {
        List<Image> images = AppDatabase.getInstance(context).imageDAO().loadAllImages();
        return getNextRandom(images);
    }

    /**
     * Return a random index for the given image list.
     *
     * @param images
     * @return
     */
    private static int getNextRandom(List<Image> images) {
        int countImage = images.size();
        Random r = new Random();
        if (countImage <= 0) {
            countImage = 1;
        }
        return r.nextInt(countImage - 1);
    }
}
