package okhttp3.internal.http2;

import com.meicam.sdk.NvsMediaFileConvertor;
import java.util.Arrays;

/* loaded from: classes3.dex */
public final class Settings {
    public int set;
    public final int[] values = new int[10];

    public void clear() {
        this.set = 0;
        Arrays.fill(this.values, 0);
    }

    public Settings set(int i, int i2) {
        if (i >= 0) {
            int[] iArr = this.values;
            if (i < iArr.length) {
                this.set = (1 << i) | this.set;
                iArr[i] = i2;
            }
        }
        return this;
    }

    public boolean isSet(int i) {
        return ((1 << i) & this.set) != 0;
    }

    public int get(int i) {
        return this.values[i];
    }

    public int size() {
        return Integer.bitCount(this.set);
    }

    public int getHeaderTableSize() {
        if ((this.set & 2) != 0) {
            return this.values[1];
        }
        return -1;
    }

    public int getMaxConcurrentStreams(int i) {
        return (this.set & 16) != 0 ? this.values[4] : i;
    }

    public int getMaxFrameSize(int i) {
        return (this.set & 32) != 0 ? this.values[5] : i;
    }

    public int getInitialWindowSize() {
        return (this.set & 128) != 0 ? this.values[7] : NvsMediaFileConvertor.CONVERTOR_ERROR_UNKNOWN;
    }

    public void merge(Settings settings) {
        for (int i = 0; i < 10; i++) {
            if (settings.isSet(i)) {
                set(i, settings.get(i));
            }
        }
    }
}
