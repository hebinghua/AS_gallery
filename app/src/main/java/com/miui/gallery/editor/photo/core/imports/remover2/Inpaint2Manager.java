package com.miui.gallery.editor.photo.core.imports.remover2;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import com.miui.gallery.editor.photo.app.remover2.Inpaint2;
import java.lang.ref.WeakReference;

/* loaded from: classes2.dex */
public class Inpaint2Manager {
    public BoundingBox[] mBoxes;
    public byte[] mByteMask;
    public Inpaint2Callback mCallback;
    public Bitmap mDisplayBitmap;
    public Bitmap mLineMaskBitmap;
    public Bitmap[] mPeopleBitmaps;

    /* loaded from: classes2.dex */
    public interface Inpaint2Callback {
        void initFinished();

        void inpaintFinished(int i, int i2);

        void isInpaintProcessing(boolean z);

        void segmentFinished();

        void tuneLineFinished(int i, Bitmap bitmap);
    }

    public void init(Bitmap bitmap) {
        this.mDisplayBitmap = bitmap;
        this.mByteMask = new byte[bitmap.getWidth() * bitmap.getHeight()];
        new InitSdkTask(this).execute(new Void[0]);
    }

    public void segment() {
        new SegmentTask(this).execute(new Void[0]);
    }

    public void tuneLine(float[] fArr, float[] fArr2) {
        new TuneLineTask(this, fArr, fArr2).execute(new Void[0]);
    }

    public void inpaint(Bitmap bitmap, Remover2NNFData remover2NNFData, int i, int[] iArr) {
        new InpaintTask(this, bitmap, remover2NNFData, i, iArr).execute(new Void[0]);
    }

    public Bitmap[] getPeopleMask() {
        return this.mPeopleBitmaps;
    }

    public BoundingBox[] getBoxes() {
        return this.mBoxes;
    }

    public final void initPeopleBitmap(byte[] bArr, BoundingBox[] boundingBoxArr, int i) {
        this.mPeopleBitmaps = new Bitmap[boundingBoxArr.length];
        for (int i2 = 0; i2 < boundingBoxArr.length; i2++) {
            BoundingBox boundingBox = boundingBoxArr[i2];
            Bitmap createBitmap = Bitmap.createBitmap(boundingBox.width, boundingBox.height, Bitmap.Config.ARGB_8888);
            Inpaint2.getInstance().genPeopleBitmap(createBitmap, Inpaint2Util.getMaskColor(i2), bArr, boundingBox.x, boundingBox.y, i, boundingBox.width, boundingBox.height, boundingBox.idx);
            this.mPeopleBitmaps[i2] = createBitmap;
        }
    }

    /* loaded from: classes2.dex */
    public static class InitSdkTask extends AsyncTask<Void, Void, Integer> {
        public WeakReference<Inpaint2Manager> mWeakManagerReference;

        public InitSdkTask(Inpaint2Manager inpaint2Manager) {
            this.mWeakManagerReference = new WeakReference<>(inpaint2Manager);
        }

        @Override // android.os.AsyncTask
        public void onPreExecute() {
            super.onPreExecute();
            Inpaint2Manager inpaint2Manager = this.mWeakManagerReference.get();
            if (inpaint2Manager == null || inpaint2Manager.mCallback == null) {
                return;
            }
            inpaint2Manager.mCallback.isInpaintProcessing(true);
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(Integer num) {
            super.onPostExecute((InitSdkTask) num);
            Inpaint2Manager inpaint2Manager = this.mWeakManagerReference.get();
            if (inpaint2Manager == null || inpaint2Manager.mCallback == null) {
                return;
            }
            inpaint2Manager.mCallback.isInpaintProcessing(false);
            inpaint2Manager.mCallback.initFinished();
        }

        @Override // android.os.AsyncTask
        public Integer doInBackground(Void... voidArr) {
            Inpaint2.getInstance().init();
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public static class SegmentTask extends AsyncTask<Void, Void, Integer> {
        public WeakReference<Inpaint2Manager> mWeakManagerReference;

        public SegmentTask(Inpaint2Manager inpaint2Manager) {
            this.mWeakManagerReference = new WeakReference<>(inpaint2Manager);
        }

        @Override // android.os.AsyncTask
        public void onPreExecute() {
            super.onPreExecute();
            Inpaint2Manager inpaint2Manager = this.mWeakManagerReference.get();
            if (inpaint2Manager == null || inpaint2Manager.mCallback == null) {
                return;
            }
            inpaint2Manager.mCallback.isInpaintProcessing(true);
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(Integer num) {
            super.onPostExecute((SegmentTask) num);
            Inpaint2Manager inpaint2Manager = this.mWeakManagerReference.get();
            if (inpaint2Manager == null || inpaint2Manager.mCallback == null) {
                return;
            }
            inpaint2Manager.mCallback.isInpaintProcessing(false);
            inpaint2Manager.mCallback.segmentFinished();
        }

        @Override // android.os.AsyncTask
        public Integer doInBackground(Void... voidArr) {
            Inpaint2Manager inpaint2Manager = this.mWeakManagerReference.get();
            if (inpaint2Manager != null) {
                inpaint2Manager.mBoxes = Inpaint2.getInstance().segmentPredict(inpaint2Manager.mDisplayBitmap, inpaint2Manager.mByteMask);
                if (inpaint2Manager.mBoxes != null && inpaint2Manager.mBoxes.length > 0) {
                    inpaint2Manager.initPeopleBitmap(inpaint2Manager.mByteMask, inpaint2Manager.mBoxes, inpaint2Manager.mDisplayBitmap.getWidth());
                }
                return -1;
            }
            return -1;
        }
    }

    /* loaded from: classes2.dex */
    public static class InpaintTask extends AsyncTask<Void, Void, Integer> {
        public int[] mIds;
        public int mIsPeople;
        public Bitmap mMaskBitmap;
        public Remover2NNFData mNNFData;
        public WeakReference<Inpaint2Manager> mWeakManagerReference;

        public InpaintTask(Inpaint2Manager inpaint2Manager, Bitmap bitmap, Remover2NNFData remover2NNFData, int i, int[] iArr) {
            this.mWeakManagerReference = new WeakReference<>(inpaint2Manager);
            this.mMaskBitmap = bitmap;
            this.mNNFData = remover2NNFData;
            this.mIsPeople = i;
            this.mIds = iArr;
        }

        @Override // android.os.AsyncTask
        public void onPreExecute() {
            super.onPreExecute();
            Inpaint2Manager inpaint2Manager = this.mWeakManagerReference.get();
            if (inpaint2Manager == null || inpaint2Manager.mCallback == null) {
                return;
            }
            inpaint2Manager.mCallback.isInpaintProcessing(true);
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(Integer num) {
            super.onPostExecute((InpaintTask) num);
            Inpaint2Manager inpaint2Manager = this.mWeakManagerReference.get();
            if (inpaint2Manager == null || inpaint2Manager.mCallback == null) {
                return;
            }
            inpaint2Manager.mCallback.inpaintFinished(num.intValue(), this.mIsPeople);
            inpaint2Manager.mCallback.isInpaintProcessing(false);
        }

        @Override // android.os.AsyncTask
        public Integer doInBackground(Void... voidArr) {
            Inpaint2Manager inpaint2Manager = this.mWeakManagerReference.get();
            if (inpaint2Manager != null) {
                return Integer.valueOf(Inpaint2.getInstance().inpaint(inpaint2Manager.mDisplayBitmap, this.mMaskBitmap, inpaint2Manager.mDisplayBitmap.getWidth(), inpaint2Manager.mDisplayBitmap.getHeight(), this.mNNFData, this.mIsPeople, this.mIds));
            }
            return -1;
        }
    }

    /* loaded from: classes2.dex */
    public static class TuneLineTask extends AsyncTask<Void, Void, Integer> {
        public WeakReference<Inpaint2Manager> mWeakManagerReference;
        public float[] mX;
        public float[] mY;

        public TuneLineTask(Inpaint2Manager inpaint2Manager, float[] fArr, float[] fArr2) {
            this.mWeakManagerReference = new WeakReference<>(inpaint2Manager);
            this.mX = fArr;
            this.mY = fArr2;
        }

        @Override // android.os.AsyncTask
        public void onPreExecute() {
            super.onPreExecute();
            Inpaint2Manager inpaint2Manager = this.mWeakManagerReference.get();
            if (inpaint2Manager == null || inpaint2Manager.mCallback == null) {
                return;
            }
            inpaint2Manager.mCallback.isInpaintProcessing(true);
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(Integer num) {
            super.onPostExecute((TuneLineTask) num);
            Inpaint2Manager inpaint2Manager = this.mWeakManagerReference.get();
            if (inpaint2Manager == null || inpaint2Manager.mCallback == null) {
                return;
            }
            inpaint2Manager.mCallback.tuneLineFinished(num.intValue(), inpaint2Manager.mLineMaskBitmap);
        }

        @Override // android.os.AsyncTask
        public Integer doInBackground(Void... voidArr) {
            Inpaint2Manager inpaint2Manager = this.mWeakManagerReference.get();
            if (inpaint2Manager != null) {
                inpaint2Manager.mLineMaskBitmap = Bitmap.createBitmap(inpaint2Manager.mDisplayBitmap.getWidth(), inpaint2Manager.mDisplayBitmap.getHeight(), Bitmap.Config.ALPHA_8);
                return Integer.valueOf(Inpaint2.getInstance().tune(this.mX, this.mY, inpaint2Manager.mDisplayBitmap, inpaint2Manager.mDisplayBitmap.getWidth(), inpaint2Manager.mDisplayBitmap.getHeight(), inpaint2Manager.mLineMaskBitmap));
            }
            return -1;
        }
    }

    public void setCallback(Inpaint2Callback inpaint2Callback) {
        this.mCallback = inpaint2Callback;
    }
}
