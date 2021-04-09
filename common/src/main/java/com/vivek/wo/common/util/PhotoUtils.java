package com.vivek.wo.common.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PhotoUtils {

    /**
     * 拍照
     * <p>
     * 需要在{@link Activity#onActivityResult(int, int, Intent)} 接收返回数据，可用当前返回的URI
     *
     * @param activity    当前上下文
     * @param authority   在Manifest清单中定义的FileProvider权限
     * @param photoFile   要保存的图片文件
     *                    可使用{@link #createImageFile(Context)} 创建文件
     * @param requestCode 在onActivityResult 返回
     * @return URI 图片文件的URI
     */
    public static Uri takePhoto(Activity activity, String authority, File photoFile, int requestCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(activity, authority, photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                takePictureIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                activity.startActivityForResult(takePictureIntent, requestCode);
                return photoURI;
            }
        }
        return null;
    }

    /**
     * 拍照
     * <p>
     * 同{@link #takePhoto(Activity, String, File, int)}，使得能在Fragment中接收回调
     *
     * @param fragment
     * @param authority
     * @param photoFile
     * @param requestCode
     * @return
     */
    public static Uri takePhoto(Fragment fragment, String authority, File photoFile, int requestCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(fragment.getContext().getPackageManager()) != null) {
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(fragment.getContext(), authority, photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                takePictureIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                fragment.startActivityForResult(takePictureIntent, requestCode);
                return photoURI;
            }
        }
        return null;
    }

    /**
     * 创建空的图片文件
     * <p>
     * 默认文件路径{@link Environment#DIRECTORY_PICTURES}，默认文件名称
     *
     * @param context
     * @return
     */
    public static File createImageFile(Context context) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File imageFile;
        try {
            imageFile = createImageFile(context, imageFileName, ".jpg");
            return imageFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 创建文件
     * <p>
     * 默认文件路径{@link Environment#DIRECTORY_PICTURES}
     *
     * @param context
     * @param imageFileName
     * @param suffix
     * @return
     * @throws IOException
     */
    public static File createImageFile(Context context, String imageFileName, String suffix) throws IOException {
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, suffix, storageDir);
        return image;
    }

    /**
     * 保存文件
     *
     * @param bitmap   图片位图对象
     * @param destFile 要保存到的图片文件
     */
    public static void saveBitmap(Bitmap bitmap, File destFile) {
        if (bitmap == null || destFile == null || !destFile.exists()) {
            return;
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(destFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 选择图片
     * <p>
     * 需要在{@link Activity#onActivityResult(int, int, Intent)} 接收返回数据
     * <p>
     * 通过{@link Intent#getData()} 获取到图片的URI
     *
     * @param activity
     * @param requestCode
     */
    public static void choosePhoto(Activity activity, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 选择图片
     * <p>
     * 同{@link #choosePhoto(Activity, int)} ，使得能在Fragment接收到回调
     *
     * @param fragment
     * @param requestCode
     */
    public static void choosePhoto(Fragment fragment, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        fragment.startActivityForResult(intent, requestCode);
    }

    /**
     * 裁剪图片
     * <p>
     * 可在拍照或者选择图片后进行图片的裁剪
     *
     * @param activity
     * @param uri         图片文件URI
     * @param outputX     输出像素大小
     * @param outputY     输出像素大小
     * @param requestCode
     */
    public static void cropImageUri(Activity activity, Uri uri, int outputX, int outputY, int requestCode) {
        Intent intent = getCropImageUriIntent(uri, outputX, outputY);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 裁剪图片
     * <p>
     * 同{@link #cropImageUri(Activity, Uri, int, int, int)} ,使得能在Fragment接收回调
     *
     * @param fragment
     * @param uri
     * @param outputX
     * @param outputY
     * @param requestCode
     */
    public static void cropImageUri(Fragment fragment, Uri uri, int outputX, int outputY, int requestCode) {
        Intent intent = getCropImageUriIntent(uri, outputX, outputY);
        fragment.startActivityForResult(intent, requestCode);
    }

    /*定义裁剪参数*/
    private static Intent getCropImageUriIntent(Uri uri, int outputX, int outputY) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1); //裁剪比例
        intent.putExtra("aspectY", 1); //裁剪比例
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return intent;
    }

    /**
     * 创建图片文件URI
     * <p>
     * 通过选择图片的URI另保存为图片文件的URL
     *
     * @param context
     * @param imgUri    图片URI
     * @param dstFile   要保存的图片文件
     *                  可使用{@link #createImageFile(Context)} 创建文件
     * @param authority 在Manifest清单中定义的FileProvider权限
     * @return
     */
    public static Uri createImageFileBackup(Context context, Uri imgUri, File dstFile, String authority) {
        Bitmap bitmap;
        ParcelFileDescriptor parcelFileDescriptor = null;
        try {
            parcelFileDescriptor = context.getContentResolver().openFileDescriptor(imgUri, "r");
            bitmap = BitmapFactory.decodeFileDescriptor(parcelFileDescriptor.getFileDescriptor());
            saveBitmap(bitmap, dstFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (parcelFileDescriptor != null) {
                try {
                    parcelFileDescriptor.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return FileProvider.getUriForFile(context, authority, dstFile);
    }

}
