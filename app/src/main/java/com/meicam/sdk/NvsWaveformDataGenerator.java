package com.meicam.sdk;

import android.os.Handler;
import android.os.Looper;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* loaded from: classes.dex */
public class NvsWaveformDataGenerator {
    private float[] m_tmpLeftWaveformData;
    private float[] m_tmpRightWaveformData;
    private WaveformDataCallback m_waveformDataCallback;
    private long m_waveformDataGenerator;
    private final String TAG = "Meicam";
    private long m_nextTaskId = 1;
    private boolean m_fetchingWaveformData = false;
    private long m_tmpSamplesPerGroup = 0;
    private HashMap<Long, Task> m_taskMap = new HashMap<>();

    /* loaded from: classes.dex */
    public interface WaveformDataCallback {
        void onWaveformDataGenerationFailed(long j, String str, long j2);

        void onWaveformDataReady(long j, String str, long j2, long j3, float[] fArr, float[] fArr2);
    }

    private native void nativeCancelTask(long j);

    private native void nativeClose(long j);

    private native long nativeFetchWaveformData(long j, String str, long j2, long j3, long j4);

    private native long nativeGetAudioFileDuration(String str);

    private native long nativeGetAudioFileSampleCount(String str);

    private native long nativeInit();

    /* loaded from: classes.dex */
    public static class Task {
        public String m_audioFilePath;
        public long m_audioFileSampleCount;
        private float[] m_leftWaveformData;
        private float[] m_rightWaveformData;
        private long m_samplesPerGroup;
        public long taskId;
        public long waveformTaskId;

        private Task() {
        }
    }

    public NvsWaveformDataGenerator() {
        this.m_waveformDataGenerator = 0L;
        NvsUtils.checkFunctionInMainThread();
        this.m_waveformDataGenerator = nativeInit();
    }

    public void release() {
        NvsUtils.checkFunctionInMainThread();
        if (isReleased()) {
            return;
        }
        for (Map.Entry<Long, Task> entry : this.m_taskMap.entrySet()) {
            nativeCancelTask(entry.getValue().waveformTaskId);
        }
        this.m_taskMap.clear();
        this.m_waveformDataCallback = null;
        nativeClose(this.m_waveformDataGenerator);
        this.m_waveformDataGenerator = 0L;
    }

    public boolean isReleased() {
        NvsUtils.checkFunctionInMainThread();
        return this.m_waveformDataGenerator == 0;
    }

    public void setWaveformDataCallback(WaveformDataCallback waveformDataCallback) {
        NvsUtils.checkFunctionInMainThread();
        this.m_waveformDataCallback = waveformDataCallback;
    }

    public long getAudioFileDuration(String str) {
        NvsUtils.checkFunctionInMainThread();
        if (str == null) {
            return 0L;
        }
        return nativeGetAudioFileDuration(str);
    }

    public long getAudioFileSampleCount(String str) {
        NvsUtils.checkFunctionInMainThread();
        if (str == null) {
            return 0L;
        }
        return nativeGetAudioFileSampleCount(str);
    }

    public long generateWaveformData(String str, long j, long j2, long j3, int i) {
        NvsUtils.checkFunctionInMainThread();
        if (!isReleased() && str != null && j > 0) {
            long nativeGetAudioFileSampleCount = nativeGetAudioFileSampleCount(str);
            if (nativeGetAudioFileSampleCount <= 0) {
                return 0L;
            }
            this.m_fetchingWaveformData = true;
            long nativeFetchWaveformData = nativeFetchWaveformData(this.m_waveformDataGenerator, str, j, j2, j3);
            this.m_fetchingWaveformData = false;
            if (nativeFetchWaveformData == 0) {
                return 0L;
            }
            Task task = new Task();
            long j4 = this.m_nextTaskId;
            this.m_nextTaskId = 1 + j4;
            task.taskId = j4;
            task.waveformTaskId = nativeFetchWaveformData;
            task.m_audioFilePath = str;
            task.m_audioFileSampleCount = nativeGetAudioFileSampleCount;
            task.m_samplesPerGroup = j;
            if (this.m_tmpSamplesPerGroup > 0) {
                task.m_leftWaveformData = this.m_tmpLeftWaveformData;
                task.m_rightWaveformData = this.m_tmpRightWaveformData;
                task.m_samplesPerGroup = this.m_tmpSamplesPerGroup;
                this.m_tmpLeftWaveformData = null;
                this.m_tmpRightWaveformData = null;
                this.m_tmpSamplesPerGroup = 0L;
            }
            this.m_taskMap.put(Long.valueOf(task.taskId), task);
            if (task.m_leftWaveformData != null) {
                finishWaveformDataFetchingTask(task.taskId, true);
            }
            return task.taskId;
        }
        return 0L;
    }

    public void cancelTask(long j) {
        NvsUtils.checkFunctionInMainThread();
        Task task = this.m_taskMap.get(Long.valueOf(j));
        if (task != null) {
            if (!isReleased()) {
                nativeCancelTask(task.waveformTaskId);
            }
            this.m_taskMap.remove(Long.valueOf(j));
        }
    }

    public void notifyWaveformDataReady(long j, long j2, long j3, float[] fArr, float[] fArr2) {
        long j4;
        if (!this.m_fetchingWaveformData) {
            Iterator<Map.Entry<Long, Task>> it = this.m_taskMap.entrySet().iterator();
            while (true) {
                if (!it.hasNext()) {
                    j4 = 0;
                    break;
                }
                Task value = it.next().getValue();
                if (value.waveformTaskId == j) {
                    value.m_samplesPerGroup = j2;
                    value.m_leftWaveformData = fArr;
                    value.m_rightWaveformData = fArr2;
                    j4 = value.taskId;
                    break;
                }
            }
            if (j4 == 0) {
                return;
            }
            finishWaveformDataFetchingTask(j4, false);
            return;
        }
        this.m_tmpSamplesPerGroup = j2;
        this.m_tmpLeftWaveformData = fArr;
        this.m_tmpRightWaveformData = fArr2;
    }

    private void finishWaveformDataFetchingTask(final long j, boolean z) {
        final WaveformDataCallback waveformDataCallback;
        Task task = this.m_taskMap.get(Long.valueOf(j));
        this.m_taskMap.remove(Long.valueOf(j));
        if (task == null || (waveformDataCallback = this.m_waveformDataCallback) == null) {
            return;
        }
        final String str = task.m_audioFilePath;
        final long j2 = task.m_audioFileSampleCount;
        final long j3 = task.m_samplesPerGroup;
        final float[] fArr = task.m_leftWaveformData;
        final float[] fArr2 = task.m_rightWaveformData;
        if (!z) {
            waveformDataCallback.onWaveformDataReady(j, str, j2, j3, fArr, fArr2);
        } else {
            new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.meicam.sdk.NvsWaveformDataGenerator.1
                @Override // java.lang.Runnable
                public void run() {
                    waveformDataCallback.onWaveformDataReady(j, str, j2, j3, fArr, fArr2);
                }
            });
        }
    }
}
