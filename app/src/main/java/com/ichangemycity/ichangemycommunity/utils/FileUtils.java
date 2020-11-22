package com.ichangemycity.ichangemycommunity.utils;

import android.content.Context;
import android.os.Environment;

import com.ichangemycity.ichangemycommunity.AppConstant;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class FileUtils {

    public static File createImageFile(Context context) throws IOException {
        //TODO change storage directory and pass product id
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String pictureFile = AppConstant.APP_NAME + "_" + timeStamp;
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(pictureFile, ".jpg", storageDir);
        return image;
    }

    public static File getImageFile(String imageType, String fileExtension) throws IOException {
        //TODO NO NEED OF TIMESTAMP -- THIS WILL PROVIDE OVERRIDE FUNCTIONALITY
        // String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String mobile = SharedPreferenceUtils.getStringValue(AppConstant.PREF_KEY_MOBILE);
        File baseFolder = new File(AppConstant.BASE_FOLDER);
        if (!baseFolder.exists())
            baseFolder.mkdirs();
        String imageName = AppConstant.BASE_FOLDER + mobile + "_" + imageType + "." + fileExtension;
        File image = new File(imageName);
        return image;
    }


//    public static String constructS3ImageUrl(String fileName) {
//        return "https://" + AppConstant.AWS_IMAGES_BUCKET_NAME + ".s3.amazonaws.com/" + fileName;
//    }
}
