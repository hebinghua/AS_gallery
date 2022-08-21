package com.miui.gallery.assistant.algorithm;

import com.miui.gallery.assistant.jni.score.QualityScore;
import com.miui.gallery.assistant.jni.score.QualityScoreJNI;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes.dex */
public class QualityScoreAlgorithm extends Algorithm {
    public long mNativePtr;
    public QualityScoreJNI mQualityScoreJNI;

    @Override // com.miui.gallery.assistant.algorithm.Algorithm
    public void clearAlgorithm() {
    }

    public QualityScoreAlgorithm() {
        super(1002002L);
    }

    @Override // com.miui.gallery.assistant.algorithm.Algorithm
    public boolean onInitAlgorithm() {
        QualityScoreJNI qualityScoreJNI = new QualityScoreJNI();
        this.mQualityScoreJNI = qualityScoreJNI;
        try {
            this.mNativePtr = qualityScoreJNI.nativeCreate();
            return true;
        } catch (Exception e) {
            DefaultLogger.e(this.TAG, e);
            return false;
        }
    }

    @Override // com.miui.gallery.assistant.algorithm.Algorithm
    public void onDestroyAlgorithm() {
        try {
            this.mQualityScoreJNI.nativeDestroy(this.mNativePtr);
        } catch (Exception e) {
            DefaultLogger.e(this.TAG, e);
        }
        this.mNativePtr = 0L;
    }

    public synchronized QualityScore getBaseQualityScore(byte[] bArr, int i, int i2) {
        if (this.mIsNativeInitiated && bArr != null && i > 224 && i2 > 224) {
            try {
                return this.mQualityScoreJNI.nativeGetImageIqa(this.mNativePtr, bArr, i, i2);
            } catch (Exception e) {
                DefaultLogger.e(this.TAG, e);
            }
        }
        return null;
    }
}
