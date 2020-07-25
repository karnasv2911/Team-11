package com.kickstart.woc.wocdriverapp.ui.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.kickstart.woc.wocdriverapp.AppConstant;
import com.kickstart.woc.wocdriverapp.R;
import com.kickstart.woc.wocdriverapp.ui.fragments.DatePickerFragment;
import com.kickstart.woc.wocdriverapp.utils.LogUtils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.kickstart.woc.wocdriverapp.AppConstant.SMS_PERMISSION_CODE;


public class CreateOrEditRiderActivity extends BaseActivity implements View.OnClickListener,DatePickerFragment.DatePickerDialogListener{

    TextView mTVLicenceExpiry;
    Button mUploadPic,mUploadLicence,mUploadRC,mUploadInsurance;
    Button mRemovePic,mRemoveLicence,mRemoveRC,mRemoveInsurance;

    LinearLayout mImageUploadedContainer,mLicenseUploadedContainer,mInsuranceUploadedContainer,mRCUploadedContainer;

   // boolean uploadPic,uploadLicence,uploadRC,uploadInsurance;

    Map<Integer,String>docFileNameMap;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_create_or_edit_rider;
    }

    @Override
    protected void onCreateActivity(Bundle bundle) {
        initUI();
    }

    private void initUI() {
        setStatusBar();
        checkForFilePermission();
//        uploadPic=false;
//        uploadInsurance=false;
//        uploadLicence=false;
//        uploadRC=false;

        mTVLicenceExpiry=findViewById(R.id.etLicenceExpiry);
        mTVLicenceExpiry.setOnClickListener(this);

        mUploadPic=findViewById(R.id.btnUploadPhoto);
        mUploadInsurance=findViewById(R.id.btnInsurance);
        mUploadLicence=findViewById(R.id.btnUploadLicence);
        mUploadRC=findViewById(R.id.btnRC);

        mUploadPic.setOnClickListener(this);
        mUploadInsurance.setOnClickListener(this);
        mUploadLicence.setOnClickListener(this);
        mUploadRC.setOnClickListener(this);
        docFileNameMap=new HashMap<>();

        mRemovePic=findViewById(R.id.btnPhotoRemove);
        mRemoveInsurance=findViewById(R.id.btnInsuranceRemove);
        mRemoveLicence=findViewById(R.id.btnLicenceRemove);
        mRemoveRC=findViewById(R.id.btnRCRemove);

        mRemovePic.setOnClickListener(this);
        mRemoveInsurance.setOnClickListener(this);
        mRemoveLicence.setOnClickListener(this);
        mRemoveRC.setOnClickListener(this);



        mImageUploadedContainer=findViewById(R.id.imageUploadedContainer);
        mLicenseUploadedContainer=findViewById(R.id.licenceUploadedContainer);
        mInsuranceUploadedContainer=findViewById(R.id.insuranceUploadedContainer);
        mRCUploadedContainer=findViewById(R.id.rcUploadedContainer);

    }

    private void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            Drawable background = getResources().getDrawable(R.drawable.bg_blue_gradient);
            window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
            window.setNavigationBarColor(getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }
    }

    public void showDatePicker() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "date picker");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.etLicenceExpiry:
                showDatePicker();
                break;
            case R.id.btnUploadPhoto:
                pickImageUpload(AppConstant.PICK_IMAGE);
                break;
            case R.id.btnUploadLicence:
                pickImageUpload(AppConstant.PICK_LICENCE);
                break;
            case R.id.btnInsurance:
                pickImageUpload(AppConstant.PICK_INSURANCE);
                break;
            case R.id.btnRC:
                pickImageUpload(AppConstant.PICK_RC);
                break;
            case R.id.btnPhotoRemove:
                performImageRemove(AppConstant.PICK_IMAGE);
                break;
            case R.id.btnLicenceRemove:
                performImageRemove(AppConstant.PICK_LICENCE);
                break;
            case R.id.btnInsuranceRemove:
                performImageRemove(AppConstant.PICK_INSURANCE);
                break;
            case R.id.btnRCRemove:
                performImageRemove(AppConstant.PICK_RC);
                break;
        }
    }

    @Override
    public void onDateSelected(DatePicker view, int day, int month, int year) {
        String date=""+day+month+year;
        mTVLicenceExpiry.setText(date);
    }

    private void pickImageUpload(int requestCode) {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto ,requestCode);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK ) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            saveImageInAppFolder(picturePath,requestCode);
            changeImageUploadUI(true,requestCode);

        }
    }

    private void saveImageInAppFolder(String sourcePath,int requestCode) {
        File source = new File(sourcePath);
        String extension= FilenameUtils.getExtension(sourcePath);
        try {
            File imageDestination= null;
            switch (requestCode){
                case AppConstant.PICK_IMAGE:
                    imageDestination= com.kickstart.woc.wocdriverapp.utils.FileUtils.getImageFile(AppConstant.ImageType.PROFILE_PIC.toString(),extension);
                    docFileNameMap.put(AppConstant.PICK_IMAGE,imageDestination.getName());
                    break;

                case AppConstant.PICK_LICENCE:
                    imageDestination= com.kickstart.woc.wocdriverapp.utils.FileUtils.getImageFile(AppConstant.ImageType.DRIVING_LICENCE.toString(),extension);
                    docFileNameMap.put(AppConstant.PICK_LICENCE,imageDestination.getName());
                    break;


                case AppConstant.PICK_INSURANCE:
                    imageDestination= com.kickstart.woc.wocdriverapp.utils.FileUtils.getImageFile(AppConstant.ImageType.VEHICLE_INSURANCE.toString(),extension);
                    docFileNameMap.put(AppConstant.PICK_INSURANCE,imageDestination.getName());
                    break;

                case AppConstant.PICK_RC:
                    imageDestination= com.kickstart.woc.wocdriverapp.utils.FileUtils.getImageFile(AppConstant.ImageType.VEHICLE_RC.toString(),extension);
                    docFileNameMap.put(AppConstant.PICK_RC,imageDestination.getName());
                    break;
            }
            FileUtils.copyFile(source, imageDestination);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void changeImageUploadUI(boolean isUploadedContainer,int requestCode) {
        View uploadedView=mUploadPic;
        View buttonView=mImageUploadedContainer;
        switch (requestCode){
            case AppConstant.PICK_IMAGE:
                buttonView=mUploadPic;
                uploadedView=mImageUploadedContainer;
                break;

            case AppConstant.PICK_LICENCE:
                buttonView=mUploadLicence;
                uploadedView=mLicenseUploadedContainer;
                break;


            case AppConstant.PICK_INSURANCE:
                buttonView=mUploadInsurance;
                uploadedView=mInsuranceUploadedContainer;
                break;

            case AppConstant.PICK_RC:
                buttonView=mUploadRC;
                uploadedView=mRCUploadedContainer;
                break;
        }
        if(isUploadedContainer) {
            showToast("Document added successfully!");
            toogleView(uploadedView, buttonView);
        }else{
            showToast("Document removed successfully!");
            toogleView(buttonView,uploadedView);
            docFileNameMap.remove(requestCode);
        }
    }

    private void performImageRemove(int requestCode) {
        if(docFileNameMap.get(requestCode)!=null){
            File dir = new File(AppConstant.BASE_FOLDER);
            File[] directory = dir.listFiles();
            if (directory != null) {
                for (File file : directory) {
                    if(file.getName().equalsIgnoreCase(docFileNameMap.get(requestCode))){
                        LogUtils.debug("removing file ::"+file.getAbsolutePath());
                        file.delete();
                        break;
                    }
                }
            }

            changeImageUploadUI(false,requestCode);
        }
    }

    private void checkForFilePermission() {
        if (!hasReadFilePermission()) {
            requestReadAndWriteFilePermission();
        }
    }

    private boolean hasReadFilePermission() {
        return ContextCompat.checkSelfPermission(CreateOrEditRiderActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(CreateOrEditRiderActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestReadAndWriteFilePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(CreateOrEditRiderActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Log.d(TAG, "shouldShowRequestPermissionRationale(), no permission requested");
            return;
        }
        ActivityCompat.requestPermissions(CreateOrEditRiderActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                SMS_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        // For the requestCode, check if permission was granted or not.
        switch (requestCode) {
            case SMS_PERMISSION_CODE: {
                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];
                    int grantResult = grantResults[i];

                    if (permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        if (grantResult == PackageManager.PERMISSION_GRANTED) {
                            LogUtils.debug("SMS_PERMISSION_GRANTED");
                        } else {
                            LogUtils.debug("SMS_PERMISSION_NOT_GRANTED");
                        }
                    }
                }
            }
        }
    }
}
