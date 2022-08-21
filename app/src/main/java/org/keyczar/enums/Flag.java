package org.keyczar.enums;

import com.baidu.platform.comapi.map.MapBundleKey;
import com.xiaomi.stat.MiStat;
import java.util.Objects;
import org.keyczar.i18n.Messages;

/* loaded from: classes3.dex */
public enum Flag {
    LOCATION("location"),
    NAME("name"),
    SIZE(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE),
    STATUS("status"),
    PURPOSE("purpose"),
    PADDING("padding"),
    DESTINATION(MiStat.Param.DESTINATION),
    VERSION("version"),
    ASYMMETRIC("asymmetric"),
    CRYPTER("crypter"),
    PEMFILE("pemfile"),
    PASSPHRASE("passphrase");
    
    private final String name;

    Flag(String str) {
        this.name = str;
    }

    @Override // java.lang.Enum
    public String toString() {
        return this.name;
    }

    public static Flag getFlag(String str) {
        Objects.requireNonNull(str);
        Flag flag = LOCATION;
        if (str.equalsIgnoreCase(flag.toString())) {
            return flag;
        }
        Flag flag2 = NAME;
        if (str.equalsIgnoreCase(flag2.toString())) {
            return flag2;
        }
        Flag flag3 = SIZE;
        if (str.equalsIgnoreCase(flag3.toString())) {
            return flag3;
        }
        Flag flag4 = STATUS;
        if (str.equalsIgnoreCase(flag4.toString())) {
            return flag4;
        }
        Flag flag5 = PURPOSE;
        if (str.equalsIgnoreCase(flag5.toString())) {
            return flag5;
        }
        Flag flag6 = DESTINATION;
        if (str.equalsIgnoreCase(flag6.toString())) {
            return flag6;
        }
        Flag flag7 = VERSION;
        if (str.equalsIgnoreCase(flag7.toString())) {
            return flag7;
        }
        Flag flag8 = ASYMMETRIC;
        if (str.equalsIgnoreCase(flag8.toString())) {
            return flag8;
        }
        Flag flag9 = CRYPTER;
        if (str.equalsIgnoreCase(flag9.toString())) {
            return flag9;
        }
        Flag flag10 = PEMFILE;
        if (str.equalsIgnoreCase(flag10.toString())) {
            return flag10;
        }
        Flag flag11 = PASSPHRASE;
        if (str.equalsIgnoreCase(flag11.toString())) {
            return flag11;
        }
        Flag flag12 = PADDING;
        if (!str.equalsIgnoreCase(flag12.toString())) {
            throw new IllegalArgumentException(Messages.getString("Flag.UnknownFlag", str));
        }
        return flag12;
    }
}
