package com.miui.os;

import android.os.Build;
import com.android.internal.SystemPropertiesCompat;

/* loaded from: classes3.dex */
public class Device {
    public static final boolean IS_HONGMI;
    public static final boolean IS_HONGMI_THREE;
    public static final boolean IS_HONGMI_THREEX;
    public static final boolean IS_HONGMI_THREEX_CM;
    public static final boolean IS_HONGMI_THREEX_CT;
    public static final boolean IS_HONGMI_THREEX_CU;
    public static final boolean IS_HONGMI_THREE_LTE;
    public static final boolean IS_HONGMI_THREE_LTE_CM;
    public static final boolean IS_HONGMI_THREE_LTE_CU;
    public static final boolean IS_HONGMI_TWO;
    public static final boolean IS_HONGMI_TWOS_LTE_MTK;
    public static final boolean IS_HONGMI_TWOX;
    public static final boolean IS_HONGMI_TWOX_BR;
    public static final boolean IS_HONGMI_TWOX_CM;
    public static final boolean IS_HONGMI_TWOX_CT;
    public static final boolean IS_HONGMI_TWOX_CU;
    public static final boolean IS_HONGMI_TWOX_IN;
    public static final boolean IS_HONGMI_TWOX_LC;
    public static final boolean IS_HONGMI_TWOX_SA;
    public static final boolean IS_HONGMI_TWO_A;
    public static final boolean IS_HONGMI_TWO_S;

    static {
        String str = Build.DEVICE;
        boolean equals = "armani".equals(str);
        IS_HONGMI_TWO_A = equals;
        boolean z = false;
        boolean z2 = "HM2014011".equals(str) || "HM2014012".equals(str);
        IS_HONGMI_TWO_S = z2;
        boolean equals2 = "HM2014501".equals(str);
        IS_HONGMI_TWOS_LTE_MTK = equals2;
        boolean z3 = "HM2013022".equals(str) || "HM2013023".equals(str) || equals || z2;
        IS_HONGMI_TWO = z3;
        boolean z4 = "lcsh92_wet_jb9".equals(str) || "lcsh92_wet_tdd".equals(str);
        IS_HONGMI_THREE = z4;
        boolean equals3 = "dior".equals(str);
        IS_HONGMI_THREE_LTE = equals3;
        IS_HONGMI_THREE_LTE_CM = equals3 && "LTETD".equals(SystemPropertiesCompat.get("ro.boot.modem", ""));
        IS_HONGMI_THREE_LTE_CU = equals3 && "LTEW".equals(SystemPropertiesCompat.get("ro.boot.modem", ""));
        boolean equals4 = "HM2014811".equals(str);
        IS_HONGMI_TWOX_CU = equals4;
        boolean z5 = "HM2014812".equals(str) || "HM2014821".equals(str);
        IS_HONGMI_TWOX_CT = z5;
        boolean z6 = "HM2014813".equals(str) || "HM2014112".equals(str);
        IS_HONGMI_TWOX_CM = z6;
        boolean equals5 = "HM2014818".equals(str);
        IS_HONGMI_TWOX_IN = equals5;
        boolean equals6 = "HM2014817".equals(str);
        IS_HONGMI_TWOX_SA = equals6;
        boolean equals7 = "HM2014819".equals(str);
        IS_HONGMI_TWOX_BR = equals7;
        boolean z7 = equals4 || z5 || z6 || equals5 || equals6 || equals7;
        IS_HONGMI_TWOX = z7;
        boolean equals8 = "lte26007".equals(str);
        IS_HONGMI_TWOX_LC = equals8;
        boolean equals9 = "gucci".equals(str);
        IS_HONGMI_THREEX = equals9;
        IS_HONGMI_THREEX_CM = equals9 && "cm".equals(SystemPropertiesCompat.get("persist.sys.modem", ""));
        IS_HONGMI_THREEX_CU = equals9 && "cu".equals(SystemPropertiesCompat.get("persist.sys.modem", ""));
        IS_HONGMI_THREEX_CT = equals9 && "ct".equals(SystemPropertiesCompat.get("persist.sys.modem", ""));
        if (z3 || z4 || z7 || equals3 || equals8 || equals2 || equals9) {
            z = true;
        }
        IS_HONGMI = z;
    }
}
