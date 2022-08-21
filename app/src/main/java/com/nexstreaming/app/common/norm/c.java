package com.nexstreaming.app.common.norm;

import ch.qos.logback.core.CoreConstants;
import com.nexstreaming.app.common.norm.NormColumnInfo;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* compiled from: NormTableInfo.java */
/* loaded from: classes3.dex */
public class c {
    private static Map<Class<? extends b>, c> g = new HashMap();
    private final String[] a;
    private final String[] b;
    private final NormColumnInfo[] c;
    private final String d;
    private final String[] e;
    private final NormColumnInfo f;

    private boolean a(Field field) {
        if (field.isSynthetic()) {
            return true;
        }
        String name = field.getName();
        return name.startsWith("$") || name.equals("serialVersionUID");
    }

    private c(Class<? extends b> cls) {
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        String camelCaseToLCUnderscore = a.camelCaseToLCUnderscore(cls.getSimpleName());
        this.d = camelCaseToLCUnderscore;
        sb.append("CREATE TABLE ");
        sb.append(camelCaseToLCUnderscore);
        sb.append(" (\n    ");
        sb2.append("DROP TABLE IF EXISTS ");
        sb2.append(camelCaseToLCUnderscore);
        Field[] declaredFields = cls.getDeclaredFields();
        int i = 0;
        for (Field field : declaredFields) {
            if (a(field)) {
                i++;
            }
        }
        this.c = new NormColumnInfo[declaredFields.length - i];
        this.e = new String[declaredFields.length - i];
        int i2 = -1;
        NormColumnInfo normColumnInfo = null;
        int i3 = 0;
        String[] strArr = null;
        String[] strArr2 = null;
        for (Field field2 : declaredFields) {
            if (!a(field2)) {
                i2++;
                NormColumnInfo normColumnInfo2 = new NormColumnInfo(field2);
                this.c[i2] = normColumnInfo2;
                this.e[i2] = normColumnInfo2.a;
                if (normColumnInfo2.h) {
                    if (strArr == null) {
                        strArr = new String[declaredFields.length];
                        strArr2 = new String[declaredFields.length];
                    }
                    strArr[i3] = "CREATE INDEX idx_" + normColumnInfo2.a + " ON " + this.d + "( " + normColumnInfo2.a + ")";
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("DROP INDEX IF EXISTS idx_");
                    sb3.append(normColumnInfo2.a);
                    strArr2[i3] = sb3.toString();
                    i3++;
                }
                normColumnInfo = normColumnInfo2.g ? normColumnInfo2 : normColumnInfo;
                if (i2 > 0) {
                    sb.append(",\n    ");
                }
                sb.append(normColumnInfo2.a);
                sb.append(' ');
                switch (AnonymousClass1.a[normColumnInfo2.c.ordinal()]) {
                    case 1:
                    case 2:
                    case 3:
                        if (normColumnInfo2.g) {
                            sb.append("INTEGER PRIMARY KEY");
                            break;
                        } else {
                            sb.append("INTEGER");
                            break;
                        }
                    case 4:
                    case 5:
                        sb.append("REAL");
                        break;
                    case 6:
                    case 7:
                    case 8:
                        sb.append("TEXT");
                        break;
                    case 9:
                        sb.append("BLOB");
                        break;
                }
                if (normColumnInfo2.d) {
                    sb.append(" UNIQUE");
                }
                if (normColumnInfo2.e) {
                    sb.append(" NOT NULL");
                }
            }
        }
        this.f = normColumnInfo;
        sb.append(CoreConstants.RIGHT_PARENTHESIS_CHAR);
        int i4 = i3 + 1;
        String[] strArr3 = new String[i4];
        this.a = strArr3;
        strArr3[0] = sb.toString();
        if (i3 > 0) {
            System.arraycopy(strArr, 0, strArr3, 1, i3);
        }
        String[] strArr4 = new String[i4];
        this.b = strArr4;
        strArr4[i3] = sb2.toString();
        if (i3 > 0) {
            System.arraycopy(strArr2, 0, strArr4, 0, i3);
        }
    }

    /* compiled from: NormTableInfo.java */
    /* renamed from: com.nexstreaming.app.common.norm.c$1  reason: invalid class name */
    /* loaded from: classes3.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] a;

        static {
            int[] iArr = new int[NormColumnInfo.ColumnType.values().length];
            a = iArr;
            try {
                iArr[NormColumnInfo.ColumnType.INT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                a[NormColumnInfo.ColumnType.LONG.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                a[NormColumnInfo.ColumnType.BOOL.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                a[NormColumnInfo.ColumnType.DOUBLE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                a[NormColumnInfo.ColumnType.FLOAT.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                a[NormColumnInfo.ColumnType.ENUM.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                a[NormColumnInfo.ColumnType.TEXT.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                a[NormColumnInfo.ColumnType.JSON.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                a[NormColumnInfo.ColumnType.BLOB.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
        }
    }

    public static c a(Class<? extends b> cls) {
        c cVar = g.get(cls);
        if (cVar == null) {
            c cVar2 = new c(cls);
            g.put(cls, cVar2);
            return cVar2;
        }
        return cVar;
    }

    public String[] a() {
        return this.a;
    }

    public String[] b() {
        return this.b;
    }

    public String c() {
        return this.d;
    }

    public String[] d() {
        return this.e;
    }

    public NormColumnInfo[] e() {
        return this.c;
    }

    public NormColumnInfo a(String str) {
        NormColumnInfo[] normColumnInfoArr;
        for (NormColumnInfo normColumnInfo : this.c) {
            if (normColumnInfo.a.equals(str)) {
                return normColumnInfo;
            }
        }
        return null;
    }

    public NormColumnInfo f() {
        return this.f;
    }

    public String[] a(int i, int i2) {
        ArrayList arrayList = new ArrayList();
        int i3 = 0;
        int i4 = 0;
        while (true) {
            NormColumnInfo[] normColumnInfoArr = this.c;
            if (i4 >= normColumnInfoArr.length) {
                break;
            }
            if (normColumnInfoArr[i4].i > i) {
                arrayList.add(Integer.valueOf(i4));
            }
            i4++;
        }
        if (arrayList.size() == 0) {
            return new String[0];
        }
        String[] strArr = new String[arrayList.size()];
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            NormColumnInfo normColumnInfo = this.c[((Integer) it.next()).intValue()];
            StringBuilder sb = new StringBuilder();
            sb.append("ALTER TABLE ");
            sb.append(this.d);
            sb.append(" ADD COLUMN ");
            sb.append(normColumnInfo.a);
            sb.append(" ");
            switch (AnonymousClass1.a[normColumnInfo.c.ordinal()]) {
                case 1:
                case 2:
                case 3:
                    if (normColumnInfo.g) {
                        sb.append("INTEGER PRIMARY KEY");
                        break;
                    } else {
                        sb.append("INTEGER");
                        break;
                    }
                case 4:
                case 5:
                    sb.append("REAL");
                    break;
                case 6:
                case 7:
                case 8:
                    sb.append("TEXT");
                    break;
                case 9:
                    sb.append("BLOB");
                    break;
            }
            strArr[i3] = sb.toString();
            sb.reverse();
            i3++;
        }
        return strArr;
    }
}
