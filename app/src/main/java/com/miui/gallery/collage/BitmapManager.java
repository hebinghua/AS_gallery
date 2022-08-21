package com.miui.gallery.collage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import com.miui.gallery.util.Bitmaps;
import com.miui.gallery.util.ExifUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

/* loaded from: classes.dex */
public class BitmapManager {
    public BitmapLoaderListener mBitmapLoaderListener;
    public HashMap<Bitmap, Uri> mBitmapUriMap = new HashMap<>();
    public Bitmap[] mBitmaps;
    public Context mContext;
    public DecodeBitmapTask mDecodeTask;

    /* loaded from: classes.dex */
    public interface BitmapLoaderListener {
        void onBitmapLoad(Bitmap[] bitmapArr);

        void onBitmapReplace(Bitmap bitmap, Bitmap bitmap2);
    }

    /* loaded from: classes.dex */
    public interface CustomLoadListener {
        void onBitmapLoad(Bitmap[] bitmapArr);
    }

    public BitmapManager(Context context, BitmapLoaderListener bitmapLoaderListener) {
        this.mContext = context.getApplicationContext();
        this.mBitmapLoaderListener = bitmapLoaderListener;
    }

    public void loadBitmapAsync(final Uri[] uriArr) {
        DecodeBitmapTask decodeBitmapTask = new DecodeBitmapTask(new CustomLoadListener() { // from class: com.miui.gallery.collage.BitmapManager.1
            @Override // com.miui.gallery.collage.BitmapManager.CustomLoadListener
            public void onBitmapLoad(Bitmap[] bitmapArr) {
                BitmapManager.this.mBitmapUriMap.clear();
                BitmapManager.this.mBitmaps = bitmapArr;
                for (int i = 0; i < bitmapArr.length; i++) {
                    BitmapManager.this.mBitmapUriMap.put(bitmapArr[i], uriArr[i]);
                }
                if (BitmapManager.this.mBitmapLoaderListener != null) {
                    BitmapManager.this.mBitmapLoaderListener.onBitmapLoad(bitmapArr);
                }
            }
        }, this.mContext);
        this.mDecodeTask = decodeBitmapTask;
        decodeBitmapTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, uriArr);
    }

    public void replaceBitmap(final Bitmap bitmap, final Uri uri) {
        new DecodeBitmapTask(new CustomLoadListener() { // from class: com.miui.gallery.collage.BitmapManager.2
            @Override // com.miui.gallery.collage.BitmapManager.CustomLoadListener
            public void onBitmapLoad(Bitmap[] bitmapArr) {
                int i = 0;
                Bitmap bitmap2 = bitmapArr[0];
                while (true) {
                    if (i >= BitmapManager.this.mBitmaps.length) {
                        break;
                    } else if (BitmapManager.this.mBitmaps[i] == bitmap) {
                        BitmapManager.this.mBitmaps[i] = bitmap2;
                        break;
                    } else {
                        i++;
                    }
                }
                BitmapManager.this.mBitmapUriMap.remove(bitmap);
                BitmapManager.this.mBitmapUriMap.put(bitmap2, uri);
                if (BitmapManager.this.mBitmapLoaderListener != null) {
                    BitmapManager.this.mBitmapLoaderListener.onBitmapReplace(bitmap, bitmap2);
                }
            }
        }, this.mContext).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, uri);
    }

    public boolean isEmpty() {
        Bitmap[] bitmapArr = this.mBitmaps;
        return bitmapArr == null || bitmapArr.length == 0;
    }

    public int size() {
        Bitmap[] bitmapArr = this.mBitmaps;
        if (bitmapArr == null) {
            return 0;
        }
        return bitmapArr.length;
    }

    public Bitmap[] data() {
        return this.mBitmaps;
    }

    public void setBitmapLoaderListener(BitmapLoaderListener bitmapLoaderListener) {
        this.mBitmapLoaderListener = bitmapLoaderListener;
    }

    /* loaded from: classes.dex */
    public static class DecodeBitmapTask extends AsyncTask<Uri, Void, Bitmap[]> {
        public Context mContext;
        public CustomLoadListener mLoadListener;

        public DecodeBitmapTask(CustomLoadListener customLoadListener, Context context) {
            this.mLoadListener = customLoadListener;
            this.mContext = context;
        }

        @Override // android.os.AsyncTask
        public Bitmap[] doInBackground(Uri... uriArr) {
            ArrayList arrayList = new ArrayList();
            for (Uri uri : uriArr) {
                Bitmap loadSuitableBitmapOnScreen = BitmapManager.loadSuitableBitmapOnScreen(this.mContext, uri);
                if (loadSuitableBitmapOnScreen != null) {
                    arrayList.add(loadSuitableBitmapOnScreen);
                }
            }
            return (Bitmap[]) arrayList.toArray(new Bitmap[arrayList.size()]);
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(Bitmap[] bitmapArr) {
            CustomLoadListener customLoadListener;
            if (bitmapArr == null || (customLoadListener = this.mLoadListener) == null) {
                return;
            }
            customLoadListener.onBitmapLoad(bitmapArr);
        }
    }

    public Uri getOriginUriByBitmap(Bitmap bitmap) {
        return this.mBitmapUriMap.get(bitmap);
    }

    public Bitmap loadSuitableBitmapBySize(int i, int i2, Uri uri) {
        return loadSuitableBitmapBySize(this.mContext, i, i2, uri);
    }

    public static Bitmap loadSuitableBitmapBySize(Context context, int i, int i2, Uri uri) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmaps.decodeUri(context, uri, options);
            options.inJustDecodeBounds = false;
            options.inMutable = true;
            options.inSampleSize = calculateInSampleSize(options, i, i2);
            return Bitmaps.joinExif(Bitmaps.decodeUri(context, uri, options), ExifUtil.getRotationDegrees(Bitmaps.readExif(context, uri)), options);
        } catch (FileNotFoundException e) {
            DefaultLogger.e("BitmapManager", e);
            return null;
        }
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int i, int i2) {
        int i3 = options.outHeight;
        int i4 = options.outWidth;
        int i5 = 1;
        if (i3 > i2 || i4 > i) {
            int i6 = i3 / 2;
            int i7 = i4 / 2;
            while (i6 / i5 >= i2 && i7 / i5 >= i) {
                i5 *= 2;
            }
            while ((i4 * i3) / Math.pow(i5, 2.0d) >= i * i2 * 2) {
                i5 *= 2;
            }
        }
        return i5;
    }

    public static Bitmap loadSuitableBitmapOnScreen(Context context, Uri uri) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int i = displayMetrics.widthPixels;
        int i2 = displayMetrics.heightPixels;
        if (i > 1080) {
            i2 = (int) (i2 * (1080.0f / 1080));
            i = 1080;
        }
        return loadSuitableBitmapBySize(context, i, i2, uri);
    }
}
