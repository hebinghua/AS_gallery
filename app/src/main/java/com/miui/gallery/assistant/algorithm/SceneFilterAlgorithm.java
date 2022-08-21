package com.miui.gallery.assistant.algorithm;

import com.miui.gallery.assistant.jni.filter.SceneClassifyJNI;
import com.miui.gallery.assistant.jni.filter.SceneResult;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes.dex */
public class SceneFilterAlgorithm extends Algorithm {
    public long mNativePtr;
    public SceneClassifyJNI mSceneClassifyJNI;

    @Override // com.miui.gallery.assistant.algorithm.Algorithm
    public void clearAlgorithm() {
    }

    public SceneFilterAlgorithm() {
        super(1003L);
    }

    @Override // com.miui.gallery.assistant.algorithm.Algorithm
    public boolean onInitAlgorithm() {
        SceneClassifyJNI sceneClassifyJNI = new SceneClassifyJNI();
        this.mSceneClassifyJNI = sceneClassifyJNI;
        try {
            this.mNativePtr = sceneClassifyJNI.nativeCreate();
            return true;
        } catch (Exception e) {
            DefaultLogger.e(this.TAG, e);
            return false;
        }
    }

    @Override // com.miui.gallery.assistant.algorithm.Algorithm
    public void onDestroyAlgorithm() {
        try {
            this.mSceneClassifyJNI.nativeDestroy(this.mNativePtr);
        } catch (Exception e) {
            DefaultLogger.e(this.TAG, e);
        }
        this.mNativePtr = 0L;
    }

    public synchronized SceneResult getSceneResult(byte[] bArr, int i, int i2) {
        if (this.mIsNativeInitiated && bArr != null) {
            try {
                return this.mSceneClassifyJNI.nativeGetImageSceneCls(this.mNativePtr, bArr, i, i2);
            } catch (Exception e) {
                DefaultLogger.e(this.TAG, e);
            }
        }
        return null;
    }
}
