package com.xiaomi.micloudsdk.sync;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Time;
import com.xiaomi.micloudsdk.utils.MiCloudConstants;

/* loaded from: classes3.dex */
public abstract class SyncLogInfo {
    public final String authority;

    public abstract String generateLogInfoString();

    public SyncLogInfo(String str) {
        this.authority = str;
    }

    public StringBuilder appendSyncExtras(StringBuilder sb, Bundle bundle) {
        sb.append("extras:");
        sb.append("{");
        StringBuilder sb2 = new StringBuilder();
        String str = MiCloudConstants.SYNC.SYNC_EXTRAS_FORCE;
        sb2.append(str);
        sb2.append("=");
        sb2.append(bundle.getBoolean(str, false));
        sb.append(sb2.toString());
        sb.append(",");
        sb.append("force=" + bundle.getBoolean("force", false));
        sb.append(",");
        sb.append("ignore_backoff=" + bundle.getBoolean("ignore_backoff", false));
        sb.append(",");
        sb.append("upload=" + bundle.getBoolean("upload", false));
        String string = bundle.getString("xiaomi_request");
        if (!TextUtils.isEmpty(string)) {
            sb.append(",");
            sb.append("xiaomi_request=" + string);
        }
        sb.append("}");
        return sb;
    }

    public static String getDeltaTime(long j, long j2) {
        long j3 = (j2 - j) / 1000;
        if (j3 < 60) {
            return String.valueOf(j3);
        }
        if (j3 < 3600) {
            return String.format("%02d:%02d", Long.valueOf(j3 / 60), Long.valueOf(j3 % 60));
        }
        long j4 = j3 % 3600;
        return String.format("%02d:%02d:%02d", Long.valueOf(j3 / 3600), Long.valueOf(j4 / 60), Long.valueOf(j4 % 60));
    }

    public static String formatTime(long j) {
        Time time = new Time();
        time.set(j);
        return time.format("%Y-%m-%d %H:%M:%S");
    }

    /* loaded from: classes3.dex */
    public static class SyncStartLogInfo extends SyncLogInfo {
        public final Bundle mExtras;
        public final long mStartTime;

        public SyncStartLogInfo(String str, long j, Bundle bundle) {
            super(str);
            this.mStartTime = j;
            this.mExtras = bundle;
        }

        @Override // com.xiaomi.micloudsdk.sync.SyncLogInfo
        public String generateLogInfoString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Start Sync");
            sb.append(":{");
            sb.append("authority:");
            sb.append(this.authority);
            sb.append(", ");
            sb.append("start:");
            sb.append(SyncLogInfo.formatTime(this.mStartTime));
            sb.append(", ");
            appendSyncExtras(sb, this.mExtras);
            sb.append("}");
            return sb.toString();
        }
    }

    /* loaded from: classes3.dex */
    public static class SyncEndLogInfo extends SyncLogInfo {
        public final long mEndTime;
        public final String mErrorMsg;
        public final boolean mIsSuccess;
        public final long mStartTime;

        public SyncEndLogInfo(String str, boolean z, long j, long j2, String str2) {
            super(str);
            this.mIsSuccess = z;
            this.mStartTime = j;
            this.mEndTime = j2;
            if (str2 == null) {
                this.mErrorMsg = "";
            } else {
                this.mErrorMsg = str2;
            }
        }

        @Override // com.xiaomi.micloudsdk.sync.SyncLogInfo
        public String generateLogInfoString() {
            StringBuilder sb = new StringBuilder();
            sb.append(this.mIsSuccess ? "Success" : "Error");
            sb.append(":{");
            sb.append("authority:");
            sb.append(this.authority);
            sb.append(", ");
            sb.append("start:");
            sb.append(SyncLogInfo.formatTime(this.mStartTime));
            sb.append(", ");
            sb.append("end:");
            sb.append(SyncLogInfo.formatTime(this.mEndTime));
            sb.append(", ");
            sb.append("delta:");
            sb.append(SyncLogInfo.getDeltaTime(this.mStartTime, this.mEndTime));
            sb.append(", ");
            sb.append("error:");
            sb.append(this.mErrorMsg);
            sb.append("}");
            return sb.toString();
        }
    }
}
