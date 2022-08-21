package com.xiaomi.onetrack;

import android.text.TextUtils;
import ch.qos.logback.core.CoreConstants;
import com.xiaomi.onetrack.OneTrack;
import org.slf4j.Marker;

/* loaded from: classes3.dex */
public class Configuration {
    public String a;
    public String b;
    public String c;
    public boolean d;
    public String e;
    public OneTrack.Mode f;
    public boolean g;
    public boolean h;
    public boolean i;
    public boolean j;
    public boolean k;
    public boolean l;
    public String m;
    public boolean n;

    public Configuration(Builder builder) {
        this.f = OneTrack.Mode.APP;
        this.g = true;
        this.h = true;
        this.i = true;
        this.k = true;
        this.l = false;
        this.n = false;
        this.a = builder.a;
        this.b = builder.b;
        this.c = builder.c;
        this.d = builder.d;
        this.e = builder.e;
        this.f = builder.f;
        this.g = builder.g;
        this.i = builder.i;
        this.h = builder.h;
        this.j = builder.j;
        this.k = builder.k;
        this.l = builder.l;
        this.m = builder.m;
        this.n = builder.n;
    }

    public String getAppId() {
        return this.a;
    }

    public String getPluginId() {
        return this.b;
    }

    public String getChannel() {
        return this.c;
    }

    public boolean isInternational() {
        return this.d;
    }

    public String getRegion() {
        return this.e;
    }

    public OneTrack.Mode getMode() {
        return this.f;
    }

    @Deprecated
    public boolean isGAIDEnable() {
        return this.g;
    }

    public boolean isExceptionCatcherEnable() {
        return this.j;
    }

    public boolean isIMSIEnable() {
        return this.h;
    }

    public boolean isIMEIEnable() {
        return this.i;
    }

    public boolean isAutoTrackActivityAction() {
        return this.k;
    }

    public boolean isOverrideMiuiRegionSetting() {
        return this.l;
    }

    public String getInstanceId() {
        return this.m;
    }

    public boolean isUseCustomPrivacyPolicy() {
        return this.n;
    }

    /* loaded from: classes3.dex */
    public static class Builder {
        public String a;
        public String b;
        public String c;
        public boolean d;
        public String e;
        public String m;
        public OneTrack.Mode f = OneTrack.Mode.APP;
        public boolean g = true;
        public boolean h = true;
        public boolean i = true;
        public boolean j = false;
        public boolean k = true;
        public boolean l = false;
        public boolean n = false;

        public Builder setAppId(String str) {
            this.a = str;
            return this;
        }

        public Builder setChannel(String str) {
            this.c = str;
            return this;
        }

        public Builder setMode(OneTrack.Mode mode) {
            this.f = mode;
            return this;
        }

        public Builder setExceptionCatcherEnable(boolean z) {
            this.j = z;
            return this;
        }

        public Builder setUseCustomPrivacyPolicy(boolean z) {
            this.n = z;
            return this;
        }

        public Configuration build() {
            return new Configuration(this);
        }
    }

    public String toString() {
        try {
            return "Configuration{appId='" + a(this.a) + CoreConstants.SINGLE_QUOTE_CHAR + ", pluginId='" + a(this.b) + CoreConstants.SINGLE_QUOTE_CHAR + ", channel='" + this.c + CoreConstants.SINGLE_QUOTE_CHAR + ", international=" + this.d + ", region='" + this.e + CoreConstants.SINGLE_QUOTE_CHAR + ", overrideMiuiRegionSetting=" + this.l + ", mode=" + this.f + ", GAIDEnable=" + this.g + ", IMSIEnable=" + this.h + ", IMEIEnable=" + this.i + ", ExceptionCatcherEnable=" + this.j + ", instanceId=" + a(this.m) + '}';
        } catch (Exception unused) {
            return "";
        }
    }

    public final String a(String str) {
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(str) && str.length() > 4) {
            for (int i = 0; i < str.length(); i++) {
                if (i == 0 || i == 1 || i == str.length() - 2 || i == str.length() - 1) {
                    sb.append(str.charAt(i));
                } else {
                    sb.append(Marker.ANY_MARKER);
                }
            }
        } else {
            sb.append(str);
        }
        return sb.toString();
    }
}
