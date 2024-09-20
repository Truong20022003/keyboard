package com.sticker.nicekeyboard.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.sticker.nicekeyboard.ui.fragment_theme.theme.ThemeModel;
import com.sticker.nicekeyboard.util.Constants;
import com.sticker.nicekeyboard.util.SPUtils;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MethodUltilts {
    private Context mContext;

    public MethodUltilts(Context context) {
        this.mContext = context;
    }

    public static Bitmap scaleToFitWidth(Bitmap bitmap, int i) {
        return Bitmap.createScaledBitmap(bitmap, i, (int) (((float) bitmap.getHeight()) * (((float) i) / ((float) bitmap.getWidth()))), true);
    }

    public List<String> getAllShownImagesPath(Context context) {
        ArrayList<String> arrayList = new ArrayList<>();
        if (SPUtils.checkStoragePermission(context)) {
            @SuppressLint("Recycle") Cursor query = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"_data", "bucket_display_name"}, null, null, null);
            if (query != null) {
                int columnIndexOrThrow = query.getColumnIndexOrThrow("_data");
                query.getColumnIndexOrThrow("bucket_display_name");
                while (query.moveToNext()) {
                    arrayList.add(query.getString(columnIndexOrThrow));
                }
            }
        }
        return arrayList;
    }

    public Bitmap getBitmapFromAsset(Context context, String str) {
        try {
            return BitmapFactory.decodeStream(context.getAssets().open(str));
        } catch (IOException unused) {
            return null;
        }
    }

    public ArrayList<EmojiObject> getAllNameImageByType(Context context, String str) {
        String[] strArr;
        ArrayList<EmojiObject> arrayList = new ArrayList<>();
        String[] strArr2 = new String[0];
        try {
            strArr = context.getAssets().list(str);
        } catch (IOException e) {
            e.printStackTrace();
            strArr = strArr2;
        }
        for (int i = 0; i < strArr.length; i++) {
            strArr[i] = str + "/" + ((Object) strArr[i]);
        }
        for (int i2 = 0; i2 < Arrays.asList(strArr).size(); i2++) {
            arrayList.add(new EmojiObject(((String) Arrays.asList(strArr).get(i2)).split("/")[2], (String) Arrays.asList(strArr).get(i2)));
        }
        return arrayList;
    }

    public ArrayList<String> getAllBackground(Context context, String str) {
        ArrayList<String> arrayList = new ArrayList<>();
        String[] strArr = new String[0];
        try {
            strArr = context.getAssets().list(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < Arrays.asList(strArr).size(); i++) {
            arrayList.add(str + "/" + ((String) Arrays.asList(strArr).get(i)));
        }
        return arrayList;
    }

    public ArrayList<ThemeModel> getAllTheme(Context context, String str, String strBackground) {
        ArrayList<ThemeModel> arrayList = new ArrayList<>();
        String[] strArr = new String[0];
        String[] strArrBackground = new String[0];
        try {
            strArr = context.getAssets().list(str);
            strArrBackground = context.getAssets().list(strBackground);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < Arrays.asList(strArr).size(); i++) {
            String nameConstance = Constants.THEME_15;
            switch (Arrays.asList(strArr).get(i)) {
                case "style_ios111.png":
                    nameConstance = Constants.THEME_1;
                    break;
                case "style_ios112.png":
                    nameConstance = Constants.THEME_2;
                    break;
                case "style_ios113.png":
                    nameConstance = Constants.THEME_3;
                    break;
                case "style_ios114.png":
                    nameConstance = Constants.THEME_4;
                    break;
                case "style_ios115.png":
                    nameConstance = Constants.THEME_5;
                    break;
                case "style_ios116.png":
                    nameConstance = Constants.THEME_6;
                    break;
                case "style_ios117.png":
                    nameConstance = Constants.THEME_7;
                    break;
                case "style_ios118.png":
                    nameConstance = Constants.THEME_8;
                    break;
                case "style_ios119.png":
                    nameConstance = Constants.THEME_9;
                    break;
                case "style_ios120.png":
                    nameConstance = Constants.THEME_10;
                    break;
                case "style_ios121.png":
                    nameConstance = Constants.THEME_11;
                    break;
                case "style_ios122.png":
                    nameConstance = Constants.THEME_12;
                    break;
                case "style_ios123.png":
                    nameConstance = Constants.THEME_13;
                    break;
                case "style_ios124.png":
                    nameConstance = Constants.THEME_14;
                    break;
                case "style_ios125.png":
                    nameConstance = Constants.THEME_15;
                    break;
                case "style_ios126.png":
                    nameConstance = Constants.THEME_16;
                    break;
                case "style_ios127.png":
                    nameConstance = Constants.THEME_17;
                    break;
                case "style_ios128.png":
                    nameConstance = Constants.THEME_18;
                    break;
                case "style_ios129.png":
                    nameConstance = Constants.THEME_19;
                    break;
                case "style_ios130.png":
                    nameConstance = Constants.THEME_20;
                    break;
                case "style_ios131.png":
                    nameConstance = Constants.THEME_21;
                    break;
                case "style_ios132.png":
                    nameConstance = Constants.THEME_22;
                    break;
                case "style_ios133.png":
                    nameConstance = Constants.THEME_23;
                    break;
                case "style_ios134.png":
                    nameConstance = Constants.THEME_24;
                    break;
                case "style_ios135.png":
                    nameConstance = Constants.THEME_25;
                    break;
                case "style_ios136.png":
                    nameConstance = Constants.THEME_26;
                    break;
                case "style_ios137.png":
                    nameConstance = Constants.THEME_27;
                    break;
                case "style_ios138.png":
                    nameConstance = Constants.THEME_28;
                    break;
                case "style_ios139.png":
                    nameConstance = Constants.THEME_29;
                    break;
                case "style_ios140.png":
                    nameConstance = Constants.THEME_30;
                    break;
                case "style_ios141.png":
                    nameConstance = Constants.THEME_31;
                    break;
                case "style_ios150.png":
                    nameConstance = Constants.THEME_32;
                    break;
                case "style_ios151.png":
                    nameConstance = Constants.THEME_33;
                    break;
                case "style_ios152.png":
                    nameConstance = Constants.THEME_34;
                    break;
                case "style_ios153.png":
                    nameConstance = Constants.THEME_35;
                    break;
                case "style_ios154.png":
                    nameConstance = Constants.THEME_36;
                    break;
                case "style_ios155.png":
                    nameConstance = Constants.THEME_37;
                    break;
                case "style_ios161.png":
                    nameConstance = Constants.THEME_38;
                    break;
                case "style_ios162.png":
                    nameConstance = Constants.THEME_39;
                    break;
                case "style_ios163.png":
                    nameConstance = Constants.THEME_40;
                    break;
                case "style_ios164.png":
                    nameConstance = Constants.THEME_41;
                    break;
                case "style_ios165.png":
                    nameConstance = Constants.THEME_42;
                    break;
                case "style_ios166.png":
                    nameConstance = Constants.THEME_43;
                    break;
                default:
                    break;
            }

            Log.d("TAG", "doInBackground: " + str + "/" + Arrays.asList(strArr).get(i));
//            arrayList.add(new ThemeModel(i, Arrays.asList(strArr).get(i), nameConstance, str + "/" + Arrays.asList(strArr).get(i), strBackground + "/" + Arrays.asList(strArrBackground).get(i)));
        }
        return arrayList;
    }

    public List<String> getFont(Context context, String str) {
        ArrayList arrayList = new ArrayList();
        try {
            String[] list = context.getAssets().list(str);
            if (list.length <= 0) {
                return arrayList;
            }
            for (String str2 : list) {
                arrayList.add(new String(str2));
            }
            return arrayList;
        } catch (IOException unused) {
            return null;
        }
    }

    public int dip2px(Context context, float f) {
        return (int) ((context.getResources().getDisplayMetrics().density * f) + 0.5f);
    }

    @SuppressLint({"WrongConstant"})
    public DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    public Uri getImageUri(Context context, Bitmap bitmap) {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new ByteArrayOutputStream());
        return Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", (String) null));
    }

    public File copyFileFromAssets(Context context, String str, String str2) {
        InputStream inputStream;
        try {
            inputStream = context.getAssets().open(str);
        } catch (IOException e) {
            e.printStackTrace();
            inputStream = null;
        }
        File file = new File(str2);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] bArr = new byte[1024];
            while (true) {
                int read = inputStream.read(bArr);
                if (read <= 0) {
                    break;
                }
                fileOutputStream.write(bArr, 0, read);
            }
            fileOutputStream.close();
            inputStream.close();
        } catch (IOException unused) {
        }
        return file;
    }


    public List<String> loadImgFromSdCard() {
        ArrayList arrayList = new ArrayList();
        File[] listFiles = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/MoijiKeyboard").listFiles();
        if (listFiles == null) {
            return null;
        }
        for (File file : listFiles) {
            if (!file.isDirectory() && file.getName().toLowerCase().contains(".jpg")) {
                arrayList.add(file.getPath());
            }
        }
        return arrayList;
    }
}
