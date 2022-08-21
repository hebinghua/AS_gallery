package com.xiaomi.sdk;

import android.graphics.Bitmap;
import android.util.Log;
import com.xiaomi.screenutils.BitmapUtils;
import java.util.HashMap;

/* loaded from: classes3.dex */
public class screenClassify {
    private static final int CLASSIFY_IMAGE_SIZE = 720;
    private static String TAG;
    private ScreenTag screenTag = null;

    static {
        System.loadLibrary("screen_scene_tag");
        TAG = "screenClassify";
    }

    public void construct(ScreenTag.InitConfig initConfig) {
        Log.d(TAG, "construct");
        if (this.screenTag == null) {
            ScreenTag screenTag = new ScreenTag();
            this.screenTag = screenTag;
            screenTag.init(initConfig);
        }
    }

    public void destruct() {
        Log.d(TAG, "destruct");
        ScreenTag screenTag = this.screenTag;
        if (screenTag != null) {
            screenTag.destroy();
        }
    }

    public String getVersion() {
        ScreenTag screenTag = this.screenTag;
        return screenTag != null ? screenTag.getVersion() : "";
    }

    public ScreenTag.ScreenTagNode[] classifyImage(Bitmap bitmap, int i) {
        if (this.screenTag != null) {
            long currentTimeMillis = System.currentTimeMillis();
            int rotate = BitmapUtils.getRotate(i);
            if (bitmap != null) {
                ScreenTag.ScreenTagNode[] classify = this.screenTag.classify(bitmap, rotate);
                String str = TAG;
                Log.d(str, "classifyImage time:" + (System.currentTimeMillis() - currentTimeMillis));
                return classify;
            }
            Log.e(TAG, "cannot load bitmap");
            return null;
        }
        return null;
    }

    /* loaded from: classes3.dex */
    public class ScreenTag {
        private static final int DEFAULT_MAX_SZIE = 4;

        private native ScreenTagNode[] classifyBitmapJni(Bitmap bitmap, int i);

        private native void destroyJni();

        private native String getVersionJni();

        private native int initJni(InitConfig initConfig);

        public ScreenTag() {
        }

        /* loaded from: classes3.dex */
        public class ScreenTagNode {
            public float score;
            public int tag;

            public ScreenTagNode(int i, float f) {
                this.tag = i;
                this.score = f;
            }
        }

        /* loaded from: classes3.dex */
        public class InitConfig {
            public String dlc_path;
            public String dsp_so_path;
            public int result_max_size = 4;
            public HashMap<Integer, Float> confidence_threshold = null;

            public InitConfig() {
            }
        }

        public int init(InitConfig initConfig) {
            return initJni(initConfig);
        }

        public ScreenTagNode[] classify(Bitmap bitmap, int i) {
            if (bitmap == null) {
                Log.e(screenClassify.TAG, "get bitmap null ");
                return null;
            }
            return classifyBitmapJni(bitmap, i);
        }

        public String getVersion() {
            return getVersionJni();
        }

        public void destroy() {
            destroyJni();
        }
    }
}
