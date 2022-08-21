package com.nexstreaming.nexeditorsdk;

import ch.qos.logback.core.net.SyslogConstants;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes3.dex */
public class nexAspectProfile {
    public static final int ExportProfileLevel_HIGH = 1;
    public static final int ExportProfileLevel_LOW = 3;
    public static final int ExportProfileLevel_MID = 2;
    public static final int ExportProfileLevel_NONE = 0;
    private static final int export_max = 2073600;
    private int aspectMode;
    private float aspectRatio;
    private int height;
    private int width;
    private static final int[] export_land_heights = {1080, 720, 480, 360, 240, SyslogConstants.LOG_LOCAL4};
    private static final int[] export_port_heights = {1920, 1280, 640, 640, 320, 240};
    private static final float[] aspects = {1.7777778f, 1.0f, 0.5625f, 2.0f, 0.5f, 1.3333334f, 0.75f};
    public static final nexAspectProfile ardef = new nexAspectProfile(1280, 720, 0);
    public static final nexAspectProfile ar16v9 = new nexAspectProfile(1280, 720, 1);
    public static final nexAspectProfile ar1v1 = new nexAspectProfile(720, 720, 2);
    public static final nexAspectProfile ar9v16 = new nexAspectProfile(720, 1280, 3);
    public static final nexAspectProfile ar2v1 = new nexAspectProfile(1440, 720, 4);
    public static final nexAspectProfile ar1v2 = new nexAspectProfile(720, 1440, 5);
    public static final nexAspectProfile ar4v3 = new nexAspectProfile(960, 720, 6);
    public static final nexAspectProfile ar3v4 = new nexAspectProfile(720, 960, 7);

    /* loaded from: classes3.dex */
    public static class ExportProfile {
        private int bitrate;
        private int height;
        private int level;
        private int width;

        private ExportProfile(int i, int i2, int i3, int i4) {
            this.bitrate = i4;
            this.width = i2;
            this.level = i;
            this.height = i3;
        }

        public int getBitrate() {
            return this.bitrate;
        }

        public int getWidth() {
            return this.width;
        }

        public int getLevel() {
            return this.level;
        }

        public int getHeight() {
            return this.height;
        }
    }

    public static List<nexAspectProfile> getPresetList() {
        return Arrays.asList(ardef, ar16v9, ar1v1, ar9v16, ar2v1, ar1v2, ar4v3, ar3v4);
    }

    public static nexAspectProfile getAspectProfile(int i) {
        if (i <= 0) {
            return null;
        }
        List<nexAspectProfile> presetList = getPresetList();
        if (presetList.size() > i) {
            return presetList.get(i);
        }
        return null;
    }

    private nexAspectProfile(int i, int i2, int i3) {
        this.aspectMode = 0;
        this.width = i;
        this.height = i2;
        this.aspectRatio = i / i2;
        this.aspectMode = i3;
    }

    public nexAspectProfile(int i, int i2) {
        int round;
        this.aspectMode = 0;
        int i3 = 720;
        if (i > i2) {
            i3 = Math.round((i * 720.0f) / i2);
            round = 720;
        } else {
            round = Math.round((i2 * 720.0f) / i);
        }
        this.width = i3;
        this.height = round;
        this.aspectRatio = i3 / round;
        this.aspectMode = getAspectMode(true);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public float getAspectRatio() {
        return this.aspectRatio;
    }

    public int getAspectMode(boolean z) {
        float aspectRatio = getAspectRatio();
        if (aspectRatio == 1.7777778f) {
            return 1;
        }
        if (aspectRatio == 0.5625f) {
            return 3;
        }
        if (aspectRatio == 2.0f) {
            return 4;
        }
        if (aspectRatio == 1.0f) {
            return 2;
        }
        if (aspectRatio == 0.5f) {
            return 5;
        }
        if (aspectRatio == 0.75f) {
            return 7;
        }
        return aspectRatio == 1.3333334f ? 6 : 0;
    }

    public int getAspectMode() {
        return this.aspectMode;
    }

    public ExportProfile[] getExportProfiles() {
        ExportProfile[] exportProfileArr = new ExportProfile[3];
        int[] iArr = export_land_heights;
        if (!isLandscapeMode()) {
            iArr = export_port_heights;
        }
        int i = 0;
        for (int i2 : iArr) {
            if (2073600.0f >= i2 * i2 * getAspectRatio()) {
                float f = i2;
                if ((getAspectRatio() * f) % 2.0f == 0.0f) {
                    int i3 = i + 1;
                    exportProfileArr[i] = new ExportProfile(i3, (int) (f * getAspectRatio()), i2, 6291456);
                    i = i3;
                    if (i3 == 3) {
                        break;
                    }
                } else {
                    continue;
                }
            }
        }
        if (i < 3) {
            for (int i4 = i; i4 < 3; i4++) {
                exportProfileArr[i] = new ExportProfile(0, getWidth(), getHeight(), 6291456);
            }
        }
        return exportProfileArr;
    }

    public boolean isLandscapeMode() {
        return this.width >= this.height;
    }

    public int getSimilarAspectMode() {
        return getSimilarAspectMode(getAspectRatio());
    }

    public int[] aspect2ReduceFraction() {
        return aspect2ReduceFraction(this.width, this.height, 0);
    }

    public static int getSimilarAspectMode(float f) {
        float f2 = 3.0f;
        int i = 0;
        int i2 = 1;
        while (true) {
            float[] fArr = aspects;
            if (i < fArr.length) {
                float f3 = f - fArr[i];
                if (f3 < 0.0f) {
                    f3 *= -1.0f;
                }
                if (f2 > f3) {
                    i2 = i;
                    f2 = f3;
                }
                i++;
            } else {
                return i2 + 1;
            }
        }
    }

    public static int getSimilarAspectMode(int i, int i2, int i3) {
        float f = i;
        float f2 = i2;
        float f3 = f / f2;
        if (i3 == 90 || i3 == 270) {
            f3 = f2 / f;
        }
        float f4 = 3.0f;
        int i4 = 0;
        int i5 = 1;
        while (true) {
            float[] fArr = aspects;
            if (i4 < fArr.length) {
                float f5 = f3 - fArr[i4];
                if (f5 < 0.0f) {
                    f5 *= -1.0f;
                }
                if (f4 > f5) {
                    i5 = i4;
                    f4 = f5;
                }
                i4++;
            } else {
                return i5 + 1;
            }
        }
    }

    public static int[] aspect2ReduceFraction(int i, int i2, int i3) {
        if (i3 == 90 || i3 == 270) {
            return reduceFraction(i2, i);
        }
        return reduceFraction(i, i2);
    }

    private static int[] reduceFraction(int i, int i2) {
        int[] iArr = {i, i2};
        if (iArr[1] == 0) {
            iArr[0] = 0;
            iArr[1] = 0;
            return iArr;
        }
        int gcd = gcd(iArr[0], iArr[1]);
        iArr[0] = iArr[0] / gcd;
        iArr[1] = iArr[1] / gcd;
        return iArr;
    }

    private static int gcd(int i, int i2) {
        while (true) {
            int i3 = i2;
            int i4 = i;
            i = i3;
            if (i != 0) {
                i2 = i4 % i;
            } else {
                return Math.abs(i4);
            }
        }
    }
}
