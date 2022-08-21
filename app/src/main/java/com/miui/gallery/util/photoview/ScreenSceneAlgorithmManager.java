package com.miui.gallery.util.photoview;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import com.miui.gallery.assistant.library.LibraryConstantsHelper;
import com.miui.gallery.assistant.library.LibraryManager;
import com.miui.gallery.util.LazyValue;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.sdk.screenClassify;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Objects;

/* loaded from: classes2.dex */
public class ScreenSceneAlgorithmManager {
    public static String TAG = "ScreenSceneAlgorithmManager";
    public static screenClassify sScreenClassify;
    public static volatile State sCurrentState = State.STATE_NOTDOWNLOADED;
    public static String DLC_NAME = "screenscene.dlc";
    public static LazyValue<Void, String> DLC_PATH = new LazyValue<Void, String>() { // from class: com.miui.gallery.util.photoview.ScreenSceneAlgorithmManager.1
        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit  reason: avoid collision after fix types in other method */
        public String mo1272onInit(Void r6) {
            File[] listFiles;
            File specificDirForLibrary = LibraryConstantsHelper.getSpecificDirForLibrary(10286L);
            if (specificDirForLibrary.exists() && specificDirForLibrary.isDirectory() && (listFiles = specificDirForLibrary.listFiles()) != null && listFiles.length > 0) {
                for (File file : listFiles) {
                    if (file.getName().equals(ScreenSceneAlgorithmManager.DLC_NAME)) {
                        DefaultLogger.d(ScreenSceneAlgorithmManager.TAG, "DLC_PATH is %s", file.getAbsolutePath());
                        return file.getAbsolutePath();
                    }
                }
            }
            DefaultLogger.d(ScreenSceneAlgorithmManager.TAG, "DLC_PATH is null");
            return null;
        }
    };
    public static LazyValue<Void, String> DSP_PATH = new LazyValue<Void, String>() { // from class: com.miui.gallery.util.photoview.ScreenSceneAlgorithmManager.2
        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit  reason: avoid collision after fix types in other method */
        public String mo1272onInit(Void r5) {
            if (ScreenSceneAlgorithmManager.DLC_PATH.get(null) != null) {
                DefaultLogger.d(ScreenSceneAlgorithmManager.TAG, "DLC_PATH is %s", LibraryConstantsHelper.getSpecificDirForLibrary(10286L).getAbsolutePath());
                return LibraryConstantsHelper.getSpecificDirForLibrary(10286L).getAbsolutePath();
            }
            return null;
        }
    };
    public static final LazyValue<Void, Handler> sCommandProcessor = new LazyValue<Void, Handler>() { // from class: com.miui.gallery.util.photoview.ScreenSceneAlgorithmManager.3
        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit  reason: avoid collision after fix types in other method */
        public Handler mo1272onInit(Void r3) {
            HandlerThread handlerThread = new HandlerThread("screen_scene_algorithm_manager", 10);
            handlerThread.start();
            return new Handler(handlerThread.getLooper(), ScreenSceneAlgorithmManager.sHandlerCallback);
        }
    };
    public static Handler.Callback sHandlerCallback = new Handler.Callback() { // from class: com.miui.gallery.util.photoview.ScreenSceneAlgorithmManager.4
        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            final ClassifyTarget classifyTarget;
            WeakReference<Bitmap> weakReference;
            int i = message.what;
            if (i == 1) {
                ScreenSceneAlgorithmManager.initAlgorithm();
            } else if (i != 2 || (classifyTarget = (ClassifyTarget) message.obj) == null || (weakReference = classifyTarget.mBitmapRef) == null || weakReference.get() == null || classifyTarget.mBitmapRef.get().isRecycled()) {
                return true;
            } else {
                final int classify = ScreenSceneAlgorithmManager.classify(classifyTarget.mBitmapRef.get(), classifyTarget.mOrientation);
                WeakReference<ClassifyResultListener> weakReference2 = classifyTarget.mClassifyListener;
                if (weakReference2 != null && weakReference2.get() != null) {
                    ScreenSceneAlgorithmManager.sMainHandler.post(new Runnable() { // from class: com.miui.gallery.util.photoview.ScreenSceneAlgorithmManager.4.1
                        @Override // java.lang.Runnable
                        public void run() {
                            DefaultLogger.d(ScreenSceneAlgorithmManager.TAG, "classify result is %s for image %s", Integer.valueOf(classify), Long.valueOf(classifyTarget.mId));
                            classifyTarget.mClassifyListener.get().onClassifyResult(classify, classifyTarget.mId);
                        }
                    });
                }
            }
            return true;
        }
    };
    public static final Handler sMainHandler = ThreadManager.getMainHandler();

    /* loaded from: classes2.dex */
    public interface ClassifyResultListener {
        void onClassifyResult(int i, long j);
    }

    /* loaded from: classes2.dex */
    public enum State {
        STATE_NOTDOWNLOADED,
        STATE_DOWNLOADING,
        STATE_DOWNLOADED,
        STATE_INITED,
        STATE_DESTROYED
    }

    /* loaded from: classes2.dex */
    public static class ClassifyTarget {
        public WeakReference<Bitmap> mBitmapRef;
        public WeakReference<ClassifyResultListener> mClassifyListener;
        public long mId;
        public int mOrientation;

        public ClassifyTarget(Bitmap bitmap, long j, int i, ClassifyResultListener classifyResultListener) {
            this.mBitmapRef = new WeakReference<>(bitmap);
            this.mId = j;
            this.mOrientation = i;
            this.mClassifyListener = new WeakReference<>(classifyResultListener);
        }
    }

    public static void classify(Bitmap bitmap, long j, int i, ClassifyResultListener classifyResultListener) {
        if (!ScreenSceneClassificationUtil.isScreenSceneClassifyEnable()) {
            return;
        }
        if (sCurrentState != State.STATE_INITED) {
            LazyValue<Void, Handler> lazyValue = sCommandProcessor;
            lazyValue.get(null).removeMessages(1);
            lazyValue.get(null).removeMessages(2);
            lazyValue.get(null).obtainMessage(1).sendToTarget();
            return;
        }
        sCommandProcessor.get(null).obtainMessage(2, new ClassifyTarget(bitmap, j, i, classifyResultListener)).sendToTarget();
    }

    public static int classify(Bitmap bitmap, int i) {
        if (!ScreenSceneClassificationUtil.isScreenSceneClassifyEnable()) {
            return 0;
        }
        if (sScreenClassify == null) {
            initAlgorithm();
            DefaultLogger.e(TAG, "classification is null and not inited, return null");
            return 0;
        }
        long currentTimeMillis = System.currentTimeMillis();
        screenClassify.ScreenTag.ScreenTagNode[] classifyImage = sScreenClassify.classifyImage(bitmap, i);
        if (classifyImage != null && classifyImage.length > 0) {
            DefaultLogger.d(TAG, "classification is %s cost %s", Integer.valueOf(classifyImage[0].tag), Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
            return classifyImage[0].tag;
        }
        DefaultLogger.d(TAG, "classification returns null");
        return -2;
    }

    public static boolean isInitAlgorithm() {
        return sCurrentState == State.STATE_INITED;
    }

    public static screenClassify initScreenClassify() {
        if (sScreenClassify == null) {
            DefaultLogger.d(TAG, "begin to init classify");
            screenClassify screenclassify = new screenClassify();
            sScreenClassify = screenclassify;
            Objects.requireNonNull(screenclassify);
            screenClassify.ScreenTag.InitConfig initConfig = new screenClassify.ScreenTag.InitConfig();
            if (DLC_PATH.get(null) == null || DSP_PATH.get(null) == null) {
                DefaultLogger.d(TAG, "classification libs path is null, return null");
                return null;
            }
            initConfig.dlc_path = DLC_PATH.get(null);
            initConfig.dsp_so_path = DSP_PATH.get(null);
            sScreenClassify.construct(initConfig);
        }
        return sScreenClassify;
    }

    /* renamed from: com.miui.gallery.util.photoview.ScreenSceneAlgorithmManager$5  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass5 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$util$photoview$ScreenSceneAlgorithmManager$State;

        static {
            int[] iArr = new int[State.values().length];
            $SwitchMap$com$miui$gallery$util$photoview$ScreenSceneAlgorithmManager$State = iArr;
            try {
                iArr[State.STATE_NOTDOWNLOADED.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$util$photoview$ScreenSceneAlgorithmManager$State[State.STATE_DOWNLOADING.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$util$photoview$ScreenSceneAlgorithmManager$State[State.STATE_DOWNLOADED.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$miui$gallery$util$photoview$ScreenSceneAlgorithmManager$State[State.STATE_INITED.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$miui$gallery$util$photoview$ScreenSceneAlgorithmManager$State[State.STATE_DESTROYED.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
        }
    }

    public static synchronized boolean initAlgorithm() {
        synchronized (ScreenSceneAlgorithmManager.class) {
            DefaultLogger.d(TAG, "initAlgorithm");
            int i = AnonymousClass5.$SwitchMap$com$miui$gallery$util$photoview$ScreenSceneAlgorithmManager$State[sCurrentState.ordinal()];
            if (i == 1) {
                DefaultLogger.d(TAG, "initAlgorithm STATE_NOTDOWNLOADED");
                if (LibraryManager.getInstance().loadLibrary(10286L)) {
                    sCurrentState = State.STATE_DOWNLOADED;
                    initAlgorithm();
                }
            } else if (i == 2) {
                if (LibraryManager.getInstance().loadLibrary(10286L)) {
                    sCurrentState = State.STATE_DOWNLOADED;
                    initAlgorithm();
                }
                DefaultLogger.d(TAG, "initAlgorithm STATE_DOWNLOADING");
            } else if (i == 3) {
                DefaultLogger.d(TAG, "initAlgorithm STATE_DOWNLOADED");
                initScreenClassify();
                sCurrentState = State.STATE_INITED;
            } else if (i == 4) {
                DefaultLogger.d(TAG, "initAlgorithm STATE_INITED");
                return true;
            } else if (i == 5) {
                sCurrentState = State.STATE_DOWNLOADED;
                initAlgorithm();
                DefaultLogger.d(TAG, "initAlgorithm STATE_DESTROYED");
            }
            return false;
        }
    }
}
