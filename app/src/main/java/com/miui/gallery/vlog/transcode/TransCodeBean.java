package com.miui.gallery.vlog.transcode;

import android.os.Build;
import android.text.TextUtils;
import androidx.annotation.Keep;
import com.google.gson.annotations.SerializedName;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.vlog.tools.VlogVideoFileTools;
import java.util.List;

/* loaded from: classes2.dex */
public class TransCodeBean {
    public String mSpecificDeviceName;
    public List<ModelResourceInfoList> modelResourceInfoList;

    public final ModelResourceInfoList getCurrentModelInfo() {
        if (!BaseMiscUtil.isValid(this.modelResourceInfoList)) {
            return null;
        }
        for (ModelResourceInfoList modelResourceInfoList : this.modelResourceInfoList) {
            if (modelResourceInfoList != null) {
                if (!TextUtils.isEmpty(this.mSpecificDeviceName)) {
                    if (modelResourceInfoList.hasDevice(this.mSpecificDeviceName)) {
                        return modelResourceInfoList;
                    }
                } else if (modelResourceInfoList.hasDevice(Build.DEVICE)) {
                    return modelResourceInfoList;
                }
            }
        }
        return null;
    }

    public int isTransCodeAvailable(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        return isTransCodeAvailable(VlogVideoFileTools.getVideoType(str));
    }

    public int isTransCodeAvailable(VideoType videoType) {
        ModelResourceInfoList currentModelInfo = getCurrentModelInfo();
        if (currentModelInfo == null || currentModelInfo.getExtraInfo() == null) {
            return 0;
        }
        ExtraInfo extraInfo = currentModelInfo.getExtraInfo();
        switch (AnonymousClass1.$SwitchMap$com$miui$gallery$vlog$transcode$VideoType[videoType.ordinal()]) {
            case 1:
                return extraInfo.getTypeFor720p120();
            case 2:
                return extraInfo.getTypeFor720p240();
            case 3:
                return extraInfo.getTypeFor1080p240();
            case 4:
                return extraInfo.getTypeFor1080p120();
            case 5:
                return extraInfo.getTypeFor1080p60();
            case 6:
                return extraInfo.getTypeFor1080p30();
            case 7:
                return extraInfo.getTypeFor4k120();
            case 8:
                return extraInfo.getTypeFor4k60();
            case 9:
                return extraInfo.getTypeFor4k30();
            default:
                return 0;
        }
    }

    /* renamed from: com.miui.gallery.vlog.transcode.TransCodeBean$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$vlog$transcode$VideoType;

        static {
            int[] iArr = new int[VideoType.values().length];
            $SwitchMap$com$miui$gallery$vlog$transcode$VideoType = iArr;
            try {
                iArr[VideoType.VIDEO_720P_120FPS.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$vlog$transcode$VideoType[VideoType.VIDEO_720P_240FPS.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$vlog$transcode$VideoType[VideoType.VIDEO_1080P_240FPS.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$miui$gallery$vlog$transcode$VideoType[VideoType.VIDEO_1080P_120FPS.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$miui$gallery$vlog$transcode$VideoType[VideoType.VIDEO_1080P_60FPS.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$miui$gallery$vlog$transcode$VideoType[VideoType.VIDEO_1080P_30FPS.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$miui$gallery$vlog$transcode$VideoType[VideoType.VIDEO_4K_120FPS.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$miui$gallery$vlog$transcode$VideoType[VideoType.VIDEO_4K_60FPS.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$miui$gallery$vlog$transcode$VideoType[VideoType.VIDEO_4K_30FPS.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
        }
    }

    @Keep
    /* loaded from: classes2.dex */
    public static class ModelResourceInfoList {
        private List<String> devices;
        private ExtraInfo extraInfo;
        private List<String> models;

        private ModelResourceInfoList() {
        }

        public List<String> getModels() {
            return this.models;
        }

        public void setModels(List<String> list) {
            this.models = list;
        }

        public List<String> getDevices() {
            return this.devices;
        }

        public void setDevices(List<String> list) {
            this.devices = list;
        }

        public ExtraInfo getExtraInfo() {
            return this.extraInfo;
        }

        public void setExtraInfo(ExtraInfo extraInfo) {
            this.extraInfo = extraInfo;
        }

        public boolean hasDevice(String str) {
            if (BaseMiscUtil.isValid(this.devices)) {
                return this.devices.contains(str);
            }
            return false;
        }
    }

    @Keep
    /* loaded from: classes2.dex */
    public static class ExtraInfo {
        @SerializedName("VIDEO_1080P_120FPS")
        private int typeFor1080p120;
        @SerializedName("VIDEO_1080P_240FPS")
        private int typeFor1080p240;
        @SerializedName("VIDEO_1080P_30FPS")
        private int typeFor1080p30;
        @SerializedName("VIDEO_1080P_60FPS")
        private int typeFor1080p60;
        @SerializedName("VIDEO_4K_120FPS")
        private int typeFor4k120;
        @SerializedName("VIDEO_4K_30FPS")
        private int typeFor4k30;
        @SerializedName("VIDEO_4K_60FPS")
        private int typeFor4k60;
        @SerializedName("VIDEO_720P_120FPS")
        private int typeFor720p120;
        @SerializedName("VIDEO_720P_240FPS")
        private int typeFor720p240;

        private ExtraInfo() {
        }

        public int getTypeFor4k30() {
            return this.typeFor4k30;
        }

        public void setTypeFor4k30(int i) {
            this.typeFor4k30 = i;
        }

        public int getTypeFor4k60() {
            return this.typeFor4k60;
        }

        public void setTypeFor4k60(int i) {
            this.typeFor4k60 = i;
        }

        public int getTypeFor4k120() {
            return this.typeFor4k120;
        }

        public void setTypeFor4k120(int i) {
            this.typeFor4k120 = i;
        }

        public int getTypeFor1080p30() {
            return this.typeFor1080p30;
        }

        public void setTypeFor1080p30(int i) {
            this.typeFor1080p30 = i;
        }

        public int getTypeFor1080p60() {
            return this.typeFor1080p60;
        }

        public void setTypeFor1080p60(int i) {
            this.typeFor1080p60 = i;
        }

        public int getTypeFor1080p120() {
            return this.typeFor1080p120;
        }

        public void setTypeFor1080p120(int i) {
            this.typeFor1080p120 = i;
        }

        public int getTypeFor1080p240() {
            return this.typeFor1080p240;
        }

        public void setTypeFor1080p240(int i) {
            this.typeFor1080p240 = i;
        }

        public int getTypeFor720p240() {
            return this.typeFor720p240;
        }

        public void setTypeFor720p240(int i) {
            this.typeFor720p240 = i;
        }

        public int getTypeFor720p120() {
            return this.typeFor720p120;
        }

        public void setTypeFor720p120(int i) {
            this.typeFor720p120 = i;
        }
    }
}
