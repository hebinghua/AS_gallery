package cn.kuaipan.android.http;

import android.os.SystemClock;
import android.util.SparseArray;
import com.baidu.platform.comapi.UIMsg;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/* loaded from: classes.dex */
public class KscSpeedManager {
    public final int mRecodeDuration;
    public long mLatestEraseTime = 0;
    public final HashMap<String, SparseArray<Float>> mRecordMap = new HashMap<>();

    public KscSpeedManager(int i) {
        this.mRecodeDuration = Math.min(3600, Math.max((int) UIMsg.MsgDefine.RENDER_STATE_FIRST_FRAME, i < 0 ? UIMsg.MSG_MAP_PANO_DATA : i));
    }

    public KscSpeedMonitor getMoniter(String str) {
        return new KscSpeedMonitor(this, str);
    }

    public synchronized void recoder(String str, long j, long j2, float f) {
        if (j2 < j || f < 0.0f) {
            return;
        }
        long j3 = j / 1000;
        long j4 = j2 / 1000;
        if (j4 == j3) {
            appendRecoder(str, computeKey(j), f);
        } else {
            long j5 = j4 - j3;
            if (j5 <= 1) {
                float f2 = (float) (j2 - j);
                int computeKey = computeKey(j);
                int computeKey2 = computeKey(j2);
                appendRecoder(str, computeKey, (((float) (1000 - (j % 1000))) * f) / f2);
                appendRecoder(str, computeKey2, (f * ((float) (j2 % 1000))) / f2);
            } else {
                float f3 = (float) (j2 - j);
                float f4 = (((float) (1000 - (j % 1000))) * f) / f3;
                float f5 = (((float) (j2 % 1000)) * f) / f3;
                int computeKey3 = computeKey(j);
                int computeKey4 = computeKey(j2);
                appendRecoder(str, computeKey3, f4);
                appendRecoder(str, computeKey4, f5);
                appendRecoders(str, computeKey3 + 1, computeKey4 - 1, ((f - f4) - f5) / ((float) (j5 - 1)));
            }
        }
        eraseExpired();
    }

    public final void eraseExpired() {
        long current = current();
        if (current - this.mLatestEraseTime <= 300000) {
            return;
        }
        int computeKey = computeKey(current);
        int i = computeKey - this.mRecodeDuration;
        boolean z = computeKey < i;
        LinkedList linkedList = new LinkedList();
        for (Map.Entry<String, SparseArray<Float>> entry : this.mRecordMap.entrySet()) {
            String key = entry.getKey();
            SparseArray<Float> value = entry.getValue();
            if (z) {
                int i2 = 0;
                while (i2 < value.size()) {
                    int keyAt = value.keyAt(i2);
                    if (keyAt > computeKey && keyAt < i) {
                        value.delete(keyAt);
                    } else if (keyAt >= i) {
                        break;
                    } else {
                        i2++;
                    }
                }
            } else {
                int i3 = 0;
                while (i3 < value.size()) {
                    int keyAt2 = value.keyAt(i3);
                    if (keyAt2 > computeKey || keyAt2 < i) {
                        value.delete(keyAt2);
                    } else {
                        i3++;
                    }
                }
            }
            if (value.size() <= 0) {
                linkedList.add(key);
            }
        }
        Iterator it = linkedList.iterator();
        while (it.hasNext()) {
            this.mRecordMap.remove((String) it.next());
        }
        this.mLatestEraseTime = current;
    }

    public static int computeKey(long j) {
        return (int) ((j / 1000) % 3600000);
    }

    public final void appendRecoders(String str, int i, int i2, float f) {
        if (i2 >= i) {
            while (i <= i2) {
                appendRecoder(str, i, f);
                i++;
            }
            return;
        }
        while (i < 3600000) {
            appendRecoder(str, i, f);
            i++;
        }
        for (int i3 = 0; i3 <= i2; i3++) {
            appendRecoder(str, i3, f);
        }
    }

    public final void appendRecoder(String str, int i, float f) {
        SparseArray<Float> sparseArray = this.mRecordMap.get(str);
        if (sparseArray == null) {
            sparseArray = new SparseArray<>();
            this.mRecordMap.put(str, sparseArray);
        }
        sparseArray.put(i, Float.valueOf(sparseArray.get(i, Float.valueOf(0.0f)).floatValue() + f));
        if (str != null) {
            appendRecoder(null, i, f);
        }
    }

    public static long current() {
        return SystemClock.elapsedRealtime();
    }
}
