package com.miui.gallery.util;

import ch.qos.logback.core.joran.action.Action;
import com.xiaomi.stat.MiStat;
import java.util.Locale;

/* loaded from: classes2.dex */
public enum Scheme {
    HTTP("http"),
    HTTPS("https"),
    FILE(Action.FILE_ATTRIBUTE),
    CONTENT(MiStat.Param.CONTENT),
    ASSETS("assets"),
    DRAWABLE("drawable"),
    UNKNOWN("");
    
    private String scheme;
    private String uriPrefix;

    Scheme(String str) {
        this.scheme = str;
        this.uriPrefix = str + "://";
    }

    public static Scheme ofUri(String str) {
        Scheme[] values;
        if (str != null) {
            for (Scheme scheme : values()) {
                if (scheme.belongsTo(str)) {
                    return scheme;
                }
            }
        }
        return UNKNOWN;
    }

    public final boolean belongsTo(String str) {
        return str.toLowerCase(Locale.US).startsWith(this.uriPrefix);
    }

    public String wrap(String str) {
        return this.uriPrefix + str;
    }

    public String crop(String str) {
        if (!belongsTo(str)) {
            throw new IllegalArgumentException(String.format("URI [%1$s] doesn't have expected scheme [%2$s]", str, this.scheme));
        }
        return str.substring(this.uriPrefix.length());
    }

    public String getScheme() {
        return this.scheme;
    }
}
