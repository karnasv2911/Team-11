package com.kickstart.woc.wocdriverapp.utils;

import android.content.Context;
import android.os.Environment;

import com.kickstart.woc.wocdriverapp.AppConstant;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;



public class FileUtils {

    public static File createImageFile(Context context) throws IOException {
        //TODO change storage directory and pass product id
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String pictureFile = AppConstant.APP_NAME+"_" + timeStamp;
        File storageDir =context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(pictureFile,  ".jpg", storageDir);
        return image;
    }
}
