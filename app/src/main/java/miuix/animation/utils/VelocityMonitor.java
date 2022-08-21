package miuix.animation.utils;

import android.os.SystemClock;
import com.miui.gallery.search.statistics.SearchStatUtils;
import java.util.Arrays;
import java.util.LinkedList;

/* loaded from: classes3.dex */
public class VelocityMonitor {
    public float[] mVelocity;
    public Long mMinDeltaTime = 30L;
    public Long mMaxDeltaTime = 100L;
    public LinkedList<MoveRecord> mHistory = new LinkedList<>();

    public final float getVelocity(double d, double d2, long j) {
        return (float) (j == 0 ? SearchStatUtils.POW : (d - d2) / (((float) j) / 1000.0f));
    }

    /* loaded from: classes3.dex */
    public static class MoveRecord {
        public long timeStamp;
        public double[] values;

        public MoveRecord() {
        }
    }

    public void update(float... fArr) {
        if (fArr == null || fArr.length == 0) {
            return;
        }
        MoveRecord moveRecord = getMoveRecord();
        moveRecord.values = new double[fArr.length];
        for (int i = 0; i < fArr.length; i++) {
            moveRecord.values[i] = fArr[i];
        }
        addAndUpdate(moveRecord);
    }

    public void update(double... dArr) {
        if (dArr == null || dArr.length == 0) {
            return;
        }
        MoveRecord moveRecord = getMoveRecord();
        moveRecord.values = dArr;
        addAndUpdate(moveRecord);
    }

    public final MoveRecord getMoveRecord() {
        MoveRecord moveRecord = new MoveRecord();
        moveRecord.timeStamp = SystemClock.uptimeMillis();
        return moveRecord;
    }

    public final void addAndUpdate(MoveRecord moveRecord) {
        this.mHistory.add(moveRecord);
        if (this.mHistory.size() > 10) {
            this.mHistory.remove(0);
        }
        updateVelocity();
    }

    public float getVelocity(int i) {
        float[] fArr;
        long uptimeMillis = SystemClock.uptimeMillis();
        if ((this.mHistory.size() <= 0 || Math.abs(uptimeMillis - this.mHistory.getLast().timeStamp) <= 50) && (fArr = this.mVelocity) != null && fArr.length > i) {
            return fArr[i];
        }
        return 0.0f;
    }

    public void clear() {
        this.mHistory.clear();
        clearVelocity();
    }

    public final void clearVelocity() {
        float[] fArr = this.mVelocity;
        if (fArr != null) {
            Arrays.fill(fArr, 0.0f);
        }
    }

    public final void updateVelocity() {
        int size = this.mHistory.size();
        if (size >= 2) {
            MoveRecord last = this.mHistory.getLast();
            MoveRecord moveRecord = this.mHistory.get(size - 2);
            float[] fArr = this.mVelocity;
            if (fArr == null || fArr.length < last.values.length) {
                this.mVelocity = new float[last.values.length];
            }
            for (int i = 0; i < last.values.length; i++) {
                this.mVelocity[i] = calVelocity(i, last, moveRecord);
            }
            return;
        }
        clearVelocity();
    }

    public final float calVelocity(int i, MoveRecord moveRecord, MoveRecord moveRecord2) {
        float f;
        double d = moveRecord.values[i];
        long j = moveRecord.timeStamp;
        double velocity = getVelocity(d, moveRecord2.values[i], j - moveRecord2.timeStamp);
        int size = this.mHistory.size() - 2;
        MoveRecord moveRecord3 = null;
        while (true) {
            if (size < 0) {
                f = Float.MAX_VALUE;
                break;
            }
            MoveRecord moveRecord4 = this.mHistory.get(size);
            long j2 = j - moveRecord4.timeStamp;
            if (j2 <= this.mMinDeltaTime.longValue() || j2 >= this.mMaxDeltaTime.longValue()) {
                size--;
                moveRecord3 = moveRecord4;
            } else {
                f = getVelocity(d, moveRecord4.values[i], j2);
                double d2 = f;
                if (velocity * d2 > SearchStatUtils.POW) {
                    f = (float) (f > 0.0f ? Math.max(velocity, d2) : Math.min(velocity, d2));
                }
                moveRecord3 = moveRecord4;
            }
        }
        if (f == Float.MAX_VALUE && moveRecord3 != null) {
            long j3 = j - moveRecord3.timeStamp;
            if (j3 > this.mMinDeltaTime.longValue() && j3 < this.mMaxDeltaTime.longValue()) {
                f = getVelocity(d, moveRecord3.values[i], j3);
            }
        }
        if (f == Float.MAX_VALUE) {
            return 0.0f;
        }
        return f;
    }
}
