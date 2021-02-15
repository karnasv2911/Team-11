package com.ichangemycity.ichangemycommunity.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import com.ichangemycity.ichangemycommunity.AppConstant;
import com.ichangemycity.ichangemycommunity.utils.LogUtils;

import java.io.File;

public class ImageUploadService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        performS3ImageSync();
        return Service.START_NOT_STICKY;
    }

//    private void performS3ImageSync() {
//        LogUtils.debug("Syncing images on s3");
//        AWSCredentials credentials = new AWSCredentials() {
//            @Override
//            public String getAWSAccessKeyId() {
//                return AppConstant.AWS_ACCESS_KEY;
//            }
//
//            @Override
//            public String getAWSSecretKey() {
//                return AppConstant.AWS_SECRET_KEY;
//            }
//
//
//        };
//        TransferManager transferManager = new TransferManager(credentials);
//        File dir = new File(AppConstant.BASE_FOLDER);
//        File[] directory = dir.listFiles();
//        if (directory != null) {
//            for (File file : directory) {
//                String key = file.getName();
//                LogUtils.debug("Syncing images on s3 key:" + key);
//                awsUpload(transferManager, AppConstant.AWS_IMAGES_BUCKET_NAME, key, file);
//                file.delete();
//            }
//        }
//
//        transferManager.shutdownNow();
//        LogUtils.debug("Images Sync campaign finished");
//    }
//
//
//    private void awsUpload(TransferManager transferManager, String bucketName, String key, File file) {
//        Upload myUpload = transferManager.upload(bucketName, key, file);
//        if (myUpload.isDone() == false) {
//            LogUtils.debug("Transfer: " + myUpload.getDescription());
//            LogUtils.debug("  - State: " + myUpload.getState());
//            LogUtils.debug("  - Progress: "
//                    + myUpload.getProgress().getBytesTransferred());
//        }
//        try {
//            myUpload.waitForCompletion();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
}
