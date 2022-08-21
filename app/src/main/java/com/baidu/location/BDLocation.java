package com.baidu.location;

import android.location.Location;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.baidu.location.Address;
import com.baidu.location.e.j;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/* loaded from: classes.dex */
public final class BDLocation implements Parcelable {
    public static final String BDLOCATION_BD09LL_TO_GCJ02 = "bd09ll2gcj";
    public static final String BDLOCATION_BD09_TO_GCJ02 = "bd092gcj";
    public static final String BDLOCATION_GCJ02_TO_BD09 = "bd09";
    public static final String BDLOCATION_GCJ02_TO_BD09LL = "bd09ll";
    public static final String BDLOCATION_WGS84_TO_GCJ02 = "gps2gcj";
    public static final Parcelable.Creator<BDLocation> CREATOR = new a();
    public static final int GPS_ACCURACY_BAD = 3;
    public static final int GPS_ACCURACY_GOOD = 1;
    public static final int GPS_ACCURACY_MID = 2;
    public static final int GPS_ACCURACY_UNKNOWN = 0;
    public static final int GPS_RECTIFY_INDOOR = 1;
    public static final int GPS_RECTIFY_NONE = 0;
    public static final int GPS_RECTIFY_OUTDOOR = 2;
    public static final int INDOOR_LOCATION_NEARBY_SURPPORT_TRUE = 2;
    public static final int INDOOR_LOCATION_SOURCE_BLUETOOTH = 4;
    public static final int INDOOR_LOCATION_SOURCE_MAGNETIC = 2;
    public static final int INDOOR_LOCATION_SOURCE_SMALLCELLSTATION = 8;
    public static final int INDOOR_LOCATION_SOURCE_UNKNOWN = 0;
    public static final int INDOOR_LOCATION_SOURCE_WIFI = 1;
    public static final int INDOOR_LOCATION_SURPPORT_FALSE = 0;
    public static final int INDOOR_LOCATION_SURPPORT_TRUE = 1;
    public static final int INDOOR_NETWORK_STATE_HIGH = 2;
    public static final int INDOOR_NETWORK_STATE_LOW = 0;
    public static final int INDOOR_NETWORK_STATE_MIDDLE = 1;
    public static final int LOCATION_WHERE_IN_CN = 1;
    public static final int LOCATION_WHERE_OUT_CN = 0;
    public static final int LOCATION_WHERE_UNKNOW = 2;
    public static final int MOCK_GPS_PROBABILITY_HIGH = 3;
    public static final int MOCK_GPS_PROBABILITY_LOW = 1;
    public static final int MOCK_GPS_PROBABILITY_MIDDLE = 2;
    public static final int MOCK_GPS_PROBABILITY_UNKNOW = -1;
    public static final int MOCK_GPS_PROBABILITY_ZERO = 0;
    public static final int OPERATORS_TYPE_MOBILE = 1;
    public static final int OPERATORS_TYPE_TELECOMU = 3;
    public static final int OPERATORS_TYPE_UNICOM = 2;
    public static final int OPERATORS_TYPE_UNKONW = 0;
    public static final int TypeCacheLocation = 65;
    public static final int TypeCriteriaException = 62;
    public static final int TypeGpsLocation = 61;
    public static final int TypeNetWorkException = 63;
    public static final int TypeNetWorkLocation = 161;
    public static final int TypeNone = 0;
    public static final int TypeOffLineLocation = 66;
    public static final int TypeOffLineLocationFail = 67;
    public static final int TypeOffLineLocationNetworkFail = 68;
    public static final int TypeServerCheckKeyError = 505;
    public static final int TypeServerDecryptError = 162;
    public static final int TypeServerError = 167;
    public static final int USER_INDDOR_TRUE = 1;
    public static final int USER_INDOOR_FALSE = 0;
    public static final int USER_INDOOR_UNKNOW = -1;
    private int A;
    private int B;
    private String C;
    private int D;
    private String E;
    private int F;
    private int G;
    private int H;
    private int I;
    private String J;
    private String K;
    private String L;
    private List<Poi> M;
    private String N;
    private String O;
    private String P;
    private Bundle Q;
    private int R;
    private int S;
    private long T;
    private String U;
    private double V;
    private double W;
    private boolean X;
    private PoiRegion Y;
    private float Z;
    private int a;
    private double aa;
    private int ab;
    private int ac;
    private BDLocation ad;
    private String b;
    private double c;
    private double d;
    private boolean e;
    private double f;
    private boolean g;
    private float h;
    private boolean i;
    private float j;
    private String k;
    private boolean l;
    private int m;
    private float n;
    private String o;
    private boolean p;
    private String q;
    private String r;
    private String s;
    private String t;
    private boolean u;
    private Address v;
    private String w;
    private String x;
    private String y;
    private boolean z;

    public BDLocation() {
        this.a = 0;
        this.b = null;
        this.c = Double.MIN_VALUE;
        this.d = Double.MIN_VALUE;
        this.e = false;
        this.f = Double.MIN_VALUE;
        this.g = false;
        this.h = 0.0f;
        this.i = false;
        this.j = 0.0f;
        this.l = false;
        this.m = -1;
        this.n = -1.0f;
        this.o = null;
        this.p = false;
        this.q = null;
        this.r = null;
        this.s = null;
        this.t = null;
        this.u = false;
        this.v = new Address.Builder().build();
        this.w = null;
        this.x = null;
        this.y = null;
        this.z = false;
        this.A = 0;
        this.B = 1;
        this.C = null;
        this.E = "";
        this.F = -1;
        this.G = 0;
        this.H = 2;
        this.I = 0;
        this.J = null;
        this.K = null;
        this.L = null;
        this.M = null;
        this.N = null;
        this.O = null;
        this.P = null;
        this.Q = new Bundle();
        this.R = 0;
        this.S = 0;
        this.T = 0L;
        this.U = null;
        this.V = Double.MIN_VALUE;
        this.W = Double.MIN_VALUE;
        this.X = false;
        this.Y = null;
        this.Z = -1.0f;
        this.aa = -1.0d;
        this.ab = 0;
        this.ac = -1;
    }

    private BDLocation(Parcel parcel) {
        this.a = 0;
        this.b = null;
        this.c = Double.MIN_VALUE;
        this.d = Double.MIN_VALUE;
        this.e = false;
        this.f = Double.MIN_VALUE;
        this.g = false;
        this.h = 0.0f;
        this.i = false;
        this.j = 0.0f;
        this.l = false;
        this.m = -1;
        this.n = -1.0f;
        this.o = null;
        this.p = false;
        this.q = null;
        this.r = null;
        this.s = null;
        this.t = null;
        this.u = false;
        this.v = new Address.Builder().build();
        this.w = null;
        this.x = null;
        this.y = null;
        this.z = false;
        this.A = 0;
        this.B = 1;
        this.C = null;
        this.E = "";
        this.F = -1;
        this.G = 0;
        this.H = 2;
        this.I = 0;
        this.J = null;
        this.K = null;
        this.L = null;
        this.M = null;
        this.N = null;
        this.O = null;
        this.P = null;
        this.Q = new Bundle();
        this.R = 0;
        this.S = 0;
        this.T = 0L;
        this.U = null;
        this.V = Double.MIN_VALUE;
        this.W = Double.MIN_VALUE;
        this.X = false;
        this.Y = null;
        this.Z = -1.0f;
        this.aa = -1.0d;
        this.ab = 0;
        this.ac = -1;
        this.a = parcel.readInt();
        this.b = parcel.readString();
        this.c = parcel.readDouble();
        this.d = parcel.readDouble();
        this.f = parcel.readDouble();
        this.h = parcel.readFloat();
        this.j = parcel.readFloat();
        this.k = parcel.readString();
        this.m = parcel.readInt();
        this.n = parcel.readFloat();
        this.w = parcel.readString();
        this.A = parcel.readInt();
        this.x = parcel.readString();
        this.y = parcel.readString();
        this.C = parcel.readString();
        String readString = parcel.readString();
        String readString2 = parcel.readString();
        String readString3 = parcel.readString();
        String readString4 = parcel.readString();
        String readString5 = parcel.readString();
        String readString6 = parcel.readString();
        parcel.readString();
        String readString7 = parcel.readString();
        String readString8 = parcel.readString();
        String readString9 = parcel.readString();
        this.v = new Address.Builder().country(readString7).countryCode(readString8).province(readString).city(readString2).cityCode(readString6).district(readString3).street(readString4).streetNumber(readString5).adcode(readString9).town(parcel.readString()).build();
        boolean[] zArr = new boolean[8];
        this.D = parcel.readInt();
        this.E = parcel.readString();
        this.r = parcel.readString();
        this.s = parcel.readString();
        this.t = parcel.readString();
        this.B = parcel.readInt();
        this.N = parcel.readString();
        this.F = parcel.readInt();
        this.G = parcel.readInt();
        this.H = parcel.readInt();
        this.I = parcel.readInt();
        this.J = parcel.readString();
        this.K = parcel.readString();
        this.L = parcel.readString();
        this.R = parcel.readInt();
        this.O = parcel.readString();
        this.S = parcel.readInt();
        this.P = parcel.readString();
        this.U = parcel.readString();
        this.T = parcel.readLong();
        this.V = parcel.readDouble();
        this.W = parcel.readDouble();
        this.Z = parcel.readFloat();
        this.aa = parcel.readDouble();
        this.ab = parcel.readInt();
        this.ac = parcel.readInt();
        this.o = parcel.readString();
        try {
            this.ad = (BDLocation) parcel.readParcelable(BDLocation.class.getClassLoader());
        } catch (Exception e) {
            this.ad = null;
            e.printStackTrace();
        }
        try {
            parcel.readBooleanArray(zArr);
            this.e = zArr[0];
            this.g = zArr[1];
            this.i = zArr[2];
            this.l = zArr[3];
            this.p = zArr[4];
            this.u = zArr[5];
            this.z = zArr[6];
            this.X = zArr[7];
        } catch (Exception unused) {
        }
        ArrayList arrayList = new ArrayList();
        try {
            parcel.readList(arrayList, Poi.class.getClassLoader());
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        if (arrayList.size() == 0) {
            this.M = null;
        } else {
            this.M = arrayList;
        }
        try {
            this.Q = parcel.readBundle();
        } catch (Exception e3) {
            e3.printStackTrace();
            this.Q = new Bundle();
        }
        try {
            this.Y = (PoiRegion) parcel.readParcelable(PoiRegion.class.getClassLoader());
        } catch (Exception e4) {
            this.Y = null;
            e4.printStackTrace();
        }
    }

    public /* synthetic */ BDLocation(Parcel parcel, a aVar) {
        this(parcel);
    }

    public BDLocation(BDLocation bDLocation) {
        this.a = 0;
        ArrayList arrayList = null;
        this.b = null;
        this.c = Double.MIN_VALUE;
        this.d = Double.MIN_VALUE;
        this.e = false;
        this.f = Double.MIN_VALUE;
        this.g = false;
        this.h = 0.0f;
        this.i = false;
        this.j = 0.0f;
        this.l = false;
        this.m = -1;
        this.n = -1.0f;
        this.o = null;
        this.p = false;
        this.q = null;
        this.r = null;
        this.s = null;
        this.t = null;
        this.u = false;
        this.v = new Address.Builder().build();
        this.w = null;
        this.x = null;
        this.y = null;
        this.z = false;
        this.A = 0;
        this.B = 1;
        this.C = null;
        this.E = "";
        this.F = -1;
        this.G = 0;
        this.H = 2;
        this.I = 0;
        this.J = null;
        this.K = null;
        this.L = null;
        this.M = null;
        this.N = null;
        this.O = null;
        this.P = null;
        this.Q = new Bundle();
        this.R = 0;
        this.S = 0;
        this.T = 0L;
        this.U = null;
        this.V = Double.MIN_VALUE;
        this.W = Double.MIN_VALUE;
        this.X = false;
        this.Y = null;
        this.Z = -1.0f;
        this.aa = -1.0d;
        this.ab = 0;
        this.ac = -1;
        this.a = bDLocation.a;
        this.b = bDLocation.b;
        this.c = bDLocation.c;
        this.d = bDLocation.d;
        this.e = bDLocation.e;
        this.f = bDLocation.f;
        this.g = bDLocation.g;
        this.h = bDLocation.h;
        this.i = bDLocation.i;
        this.j = bDLocation.j;
        this.k = bDLocation.k;
        this.l = bDLocation.l;
        this.m = bDLocation.m;
        this.n = bDLocation.n;
        this.o = bDLocation.o;
        this.p = bDLocation.p;
        this.q = bDLocation.q;
        this.u = bDLocation.u;
        this.v = new Address.Builder().country(bDLocation.v.country).countryCode(bDLocation.v.countryCode).province(bDLocation.v.province).city(bDLocation.v.city).cityCode(bDLocation.v.cityCode).district(bDLocation.v.district).street(bDLocation.v.street).streetNumber(bDLocation.v.streetNumber).adcode(bDLocation.v.adcode).town(bDLocation.v.town).build();
        this.w = bDLocation.w;
        this.x = bDLocation.x;
        this.y = bDLocation.y;
        this.B = bDLocation.B;
        this.A = bDLocation.A;
        this.z = bDLocation.z;
        this.C = bDLocation.C;
        this.D = bDLocation.D;
        this.E = bDLocation.E;
        this.r = bDLocation.r;
        this.s = bDLocation.s;
        this.t = bDLocation.t;
        this.F = bDLocation.F;
        this.G = bDLocation.G;
        this.H = bDLocation.G;
        this.I = bDLocation.I;
        this.J = bDLocation.J;
        this.K = bDLocation.K;
        this.L = bDLocation.L;
        this.R = bDLocation.R;
        this.P = bDLocation.P;
        this.U = bDLocation.U;
        this.V = bDLocation.V;
        this.W = bDLocation.W;
        this.T = bDLocation.T;
        this.aa = bDLocation.aa;
        this.ab = bDLocation.ab;
        this.ac = bDLocation.ac;
        this.ad = bDLocation.ad;
        this.O = bDLocation.O;
        if (bDLocation.M != null) {
            arrayList = new ArrayList();
            for (int i = 0; i < bDLocation.M.size(); i++) {
                Poi poi = bDLocation.M.get(i);
                arrayList.add(new Poi(poi.getId(), poi.getName(), poi.getRank(), poi.getTags(), poi.getAddr()));
            }
        }
        this.M = arrayList;
        this.N = bDLocation.N;
        this.Q = bDLocation.Q;
        this.S = bDLocation.S;
        this.X = bDLocation.X;
        this.Y = bDLocation.Y;
        this.Z = bDLocation.Z;
    }

    /* JADX WARN: Removed duplicated region for block: B:205:0x044d A[Catch: Exception -> 0x0175, Error -> 0x0719, TRY_LEAVE, TryCatch #8 {Exception -> 0x0175, blocks: (B:11:0x00dc, B:13:0x0134, B:14:0x013d, B:21:0x0164, B:23:0x0168, B:24:0x0170, B:29:0x017e, B:31:0x01ad, B:32:0x01b4, B:35:0x01bc, B:37:0x01c6, B:39:0x01d0, B:40:0x01d3, B:41:0x01d5, B:43:0x01dd, B:44:0x01ef, B:46:0x01f5, B:48:0x0213, B:50:0x021e, B:52:0x0224, B:54:0x022d, B:55:0x023a, B:56:0x023c, B:58:0x0244, B:60:0x0250, B:61:0x0252, B:63:0x025a, B:65:0x0268, B:67:0x0270, B:69:0x0278, B:71:0x0280, B:73:0x0288, B:75:0x0290, B:76:0x0297, B:78:0x029f, B:80:0x02ab, B:81:0x02ad, B:88:0x02bf, B:90:0x02c7, B:92:0x02cf, B:94:0x02d7, B:96:0x02df, B:98:0x02e7, B:100:0x02ef, B:102:0x02f7, B:104:0x02ff, B:106:0x0307, B:108:0x0313, B:110:0x031b, B:112:0x0326, B:114:0x032e, B:116:0x0339, B:118:0x0341, B:120:0x034c, B:122:0x0354, B:124:0x035c, B:126:0x0364, B:205:0x044d, B:212:0x0496, B:214:0x049e, B:216:0x04aa, B:217:0x04ad, B:219:0x04b5, B:221:0x04c1, B:222:0x04cc, B:224:0x04d4, B:226:0x04e2, B:227:0x04e5, B:229:0x04ed, B:231:0x04fb, B:232:0x04fe, B:234:0x0506, B:236:0x0514, B:237:0x0517, B:239:0x051f, B:246:0x0534, B:249:0x053d, B:250:0x0547, B:285:0x05fb, B:287:0x0603, B:295:0x0627, B:297:0x062b, B:299:0x0635, B:301:0x063d, B:302:0x0645, B:304:0x064d, B:319:0x0683, B:320:0x0686, B:329:0x06bc, B:298:0x0632, B:284:0x05f8, B:203:0x043a, B:211:0x0493, B:339:0x06d4, B:340:0x06d9), top: B:372:0x00da }] */
    /* JADX WARN: Removed duplicated region for block: B:207:0x0486  */
    /* JADX WARN: Removed duplicated region for block: B:214:0x049e A[Catch: Exception -> 0x0175, Error -> 0x0719, TryCatch #8 {Exception -> 0x0175, blocks: (B:11:0x00dc, B:13:0x0134, B:14:0x013d, B:21:0x0164, B:23:0x0168, B:24:0x0170, B:29:0x017e, B:31:0x01ad, B:32:0x01b4, B:35:0x01bc, B:37:0x01c6, B:39:0x01d0, B:40:0x01d3, B:41:0x01d5, B:43:0x01dd, B:44:0x01ef, B:46:0x01f5, B:48:0x0213, B:50:0x021e, B:52:0x0224, B:54:0x022d, B:55:0x023a, B:56:0x023c, B:58:0x0244, B:60:0x0250, B:61:0x0252, B:63:0x025a, B:65:0x0268, B:67:0x0270, B:69:0x0278, B:71:0x0280, B:73:0x0288, B:75:0x0290, B:76:0x0297, B:78:0x029f, B:80:0x02ab, B:81:0x02ad, B:88:0x02bf, B:90:0x02c7, B:92:0x02cf, B:94:0x02d7, B:96:0x02df, B:98:0x02e7, B:100:0x02ef, B:102:0x02f7, B:104:0x02ff, B:106:0x0307, B:108:0x0313, B:110:0x031b, B:112:0x0326, B:114:0x032e, B:116:0x0339, B:118:0x0341, B:120:0x034c, B:122:0x0354, B:124:0x035c, B:126:0x0364, B:205:0x044d, B:212:0x0496, B:214:0x049e, B:216:0x04aa, B:217:0x04ad, B:219:0x04b5, B:221:0x04c1, B:222:0x04cc, B:224:0x04d4, B:226:0x04e2, B:227:0x04e5, B:229:0x04ed, B:231:0x04fb, B:232:0x04fe, B:234:0x0506, B:236:0x0514, B:237:0x0517, B:239:0x051f, B:246:0x0534, B:249:0x053d, B:250:0x0547, B:285:0x05fb, B:287:0x0603, B:295:0x0627, B:297:0x062b, B:299:0x0635, B:301:0x063d, B:302:0x0645, B:304:0x064d, B:319:0x0683, B:320:0x0686, B:329:0x06bc, B:298:0x0632, B:284:0x05f8, B:203:0x043a, B:211:0x0493, B:339:0x06d4, B:340:0x06d9), top: B:372:0x00da }] */
    /* JADX WARN: Removed duplicated region for block: B:219:0x04b5 A[Catch: Exception -> 0x0175, Error -> 0x0719, TryCatch #8 {Exception -> 0x0175, blocks: (B:11:0x00dc, B:13:0x0134, B:14:0x013d, B:21:0x0164, B:23:0x0168, B:24:0x0170, B:29:0x017e, B:31:0x01ad, B:32:0x01b4, B:35:0x01bc, B:37:0x01c6, B:39:0x01d0, B:40:0x01d3, B:41:0x01d5, B:43:0x01dd, B:44:0x01ef, B:46:0x01f5, B:48:0x0213, B:50:0x021e, B:52:0x0224, B:54:0x022d, B:55:0x023a, B:56:0x023c, B:58:0x0244, B:60:0x0250, B:61:0x0252, B:63:0x025a, B:65:0x0268, B:67:0x0270, B:69:0x0278, B:71:0x0280, B:73:0x0288, B:75:0x0290, B:76:0x0297, B:78:0x029f, B:80:0x02ab, B:81:0x02ad, B:88:0x02bf, B:90:0x02c7, B:92:0x02cf, B:94:0x02d7, B:96:0x02df, B:98:0x02e7, B:100:0x02ef, B:102:0x02f7, B:104:0x02ff, B:106:0x0307, B:108:0x0313, B:110:0x031b, B:112:0x0326, B:114:0x032e, B:116:0x0339, B:118:0x0341, B:120:0x034c, B:122:0x0354, B:124:0x035c, B:126:0x0364, B:205:0x044d, B:212:0x0496, B:214:0x049e, B:216:0x04aa, B:217:0x04ad, B:219:0x04b5, B:221:0x04c1, B:222:0x04cc, B:224:0x04d4, B:226:0x04e2, B:227:0x04e5, B:229:0x04ed, B:231:0x04fb, B:232:0x04fe, B:234:0x0506, B:236:0x0514, B:237:0x0517, B:239:0x051f, B:246:0x0534, B:249:0x053d, B:250:0x0547, B:285:0x05fb, B:287:0x0603, B:295:0x0627, B:297:0x062b, B:299:0x0635, B:301:0x063d, B:302:0x0645, B:304:0x064d, B:319:0x0683, B:320:0x0686, B:329:0x06bc, B:298:0x0632, B:284:0x05f8, B:203:0x043a, B:211:0x0493, B:339:0x06d4, B:340:0x06d9), top: B:372:0x00da }] */
    /* JADX WARN: Removed duplicated region for block: B:224:0x04d4 A[Catch: Exception -> 0x0175, Error -> 0x0719, TryCatch #8 {Exception -> 0x0175, blocks: (B:11:0x00dc, B:13:0x0134, B:14:0x013d, B:21:0x0164, B:23:0x0168, B:24:0x0170, B:29:0x017e, B:31:0x01ad, B:32:0x01b4, B:35:0x01bc, B:37:0x01c6, B:39:0x01d0, B:40:0x01d3, B:41:0x01d5, B:43:0x01dd, B:44:0x01ef, B:46:0x01f5, B:48:0x0213, B:50:0x021e, B:52:0x0224, B:54:0x022d, B:55:0x023a, B:56:0x023c, B:58:0x0244, B:60:0x0250, B:61:0x0252, B:63:0x025a, B:65:0x0268, B:67:0x0270, B:69:0x0278, B:71:0x0280, B:73:0x0288, B:75:0x0290, B:76:0x0297, B:78:0x029f, B:80:0x02ab, B:81:0x02ad, B:88:0x02bf, B:90:0x02c7, B:92:0x02cf, B:94:0x02d7, B:96:0x02df, B:98:0x02e7, B:100:0x02ef, B:102:0x02f7, B:104:0x02ff, B:106:0x0307, B:108:0x0313, B:110:0x031b, B:112:0x0326, B:114:0x032e, B:116:0x0339, B:118:0x0341, B:120:0x034c, B:122:0x0354, B:124:0x035c, B:126:0x0364, B:205:0x044d, B:212:0x0496, B:214:0x049e, B:216:0x04aa, B:217:0x04ad, B:219:0x04b5, B:221:0x04c1, B:222:0x04cc, B:224:0x04d4, B:226:0x04e2, B:227:0x04e5, B:229:0x04ed, B:231:0x04fb, B:232:0x04fe, B:234:0x0506, B:236:0x0514, B:237:0x0517, B:239:0x051f, B:246:0x0534, B:249:0x053d, B:250:0x0547, B:285:0x05fb, B:287:0x0603, B:295:0x0627, B:297:0x062b, B:299:0x0635, B:301:0x063d, B:302:0x0645, B:304:0x064d, B:319:0x0683, B:320:0x0686, B:329:0x06bc, B:298:0x0632, B:284:0x05f8, B:203:0x043a, B:211:0x0493, B:339:0x06d4, B:340:0x06d9), top: B:372:0x00da }] */
    /* JADX WARN: Removed duplicated region for block: B:229:0x04ed A[Catch: Exception -> 0x0175, Error -> 0x0719, TryCatch #8 {Exception -> 0x0175, blocks: (B:11:0x00dc, B:13:0x0134, B:14:0x013d, B:21:0x0164, B:23:0x0168, B:24:0x0170, B:29:0x017e, B:31:0x01ad, B:32:0x01b4, B:35:0x01bc, B:37:0x01c6, B:39:0x01d0, B:40:0x01d3, B:41:0x01d5, B:43:0x01dd, B:44:0x01ef, B:46:0x01f5, B:48:0x0213, B:50:0x021e, B:52:0x0224, B:54:0x022d, B:55:0x023a, B:56:0x023c, B:58:0x0244, B:60:0x0250, B:61:0x0252, B:63:0x025a, B:65:0x0268, B:67:0x0270, B:69:0x0278, B:71:0x0280, B:73:0x0288, B:75:0x0290, B:76:0x0297, B:78:0x029f, B:80:0x02ab, B:81:0x02ad, B:88:0x02bf, B:90:0x02c7, B:92:0x02cf, B:94:0x02d7, B:96:0x02df, B:98:0x02e7, B:100:0x02ef, B:102:0x02f7, B:104:0x02ff, B:106:0x0307, B:108:0x0313, B:110:0x031b, B:112:0x0326, B:114:0x032e, B:116:0x0339, B:118:0x0341, B:120:0x034c, B:122:0x0354, B:124:0x035c, B:126:0x0364, B:205:0x044d, B:212:0x0496, B:214:0x049e, B:216:0x04aa, B:217:0x04ad, B:219:0x04b5, B:221:0x04c1, B:222:0x04cc, B:224:0x04d4, B:226:0x04e2, B:227:0x04e5, B:229:0x04ed, B:231:0x04fb, B:232:0x04fe, B:234:0x0506, B:236:0x0514, B:237:0x0517, B:239:0x051f, B:246:0x0534, B:249:0x053d, B:250:0x0547, B:285:0x05fb, B:287:0x0603, B:295:0x0627, B:297:0x062b, B:299:0x0635, B:301:0x063d, B:302:0x0645, B:304:0x064d, B:319:0x0683, B:320:0x0686, B:329:0x06bc, B:298:0x0632, B:284:0x05f8, B:203:0x043a, B:211:0x0493, B:339:0x06d4, B:340:0x06d9), top: B:372:0x00da }] */
    /* JADX WARN: Removed duplicated region for block: B:234:0x0506 A[Catch: Exception -> 0x0175, Error -> 0x0719, TryCatch #8 {Exception -> 0x0175, blocks: (B:11:0x00dc, B:13:0x0134, B:14:0x013d, B:21:0x0164, B:23:0x0168, B:24:0x0170, B:29:0x017e, B:31:0x01ad, B:32:0x01b4, B:35:0x01bc, B:37:0x01c6, B:39:0x01d0, B:40:0x01d3, B:41:0x01d5, B:43:0x01dd, B:44:0x01ef, B:46:0x01f5, B:48:0x0213, B:50:0x021e, B:52:0x0224, B:54:0x022d, B:55:0x023a, B:56:0x023c, B:58:0x0244, B:60:0x0250, B:61:0x0252, B:63:0x025a, B:65:0x0268, B:67:0x0270, B:69:0x0278, B:71:0x0280, B:73:0x0288, B:75:0x0290, B:76:0x0297, B:78:0x029f, B:80:0x02ab, B:81:0x02ad, B:88:0x02bf, B:90:0x02c7, B:92:0x02cf, B:94:0x02d7, B:96:0x02df, B:98:0x02e7, B:100:0x02ef, B:102:0x02f7, B:104:0x02ff, B:106:0x0307, B:108:0x0313, B:110:0x031b, B:112:0x0326, B:114:0x032e, B:116:0x0339, B:118:0x0341, B:120:0x034c, B:122:0x0354, B:124:0x035c, B:126:0x0364, B:205:0x044d, B:212:0x0496, B:214:0x049e, B:216:0x04aa, B:217:0x04ad, B:219:0x04b5, B:221:0x04c1, B:222:0x04cc, B:224:0x04d4, B:226:0x04e2, B:227:0x04e5, B:229:0x04ed, B:231:0x04fb, B:232:0x04fe, B:234:0x0506, B:236:0x0514, B:237:0x0517, B:239:0x051f, B:246:0x0534, B:249:0x053d, B:250:0x0547, B:285:0x05fb, B:287:0x0603, B:295:0x0627, B:297:0x062b, B:299:0x0635, B:301:0x063d, B:302:0x0645, B:304:0x064d, B:319:0x0683, B:320:0x0686, B:329:0x06bc, B:298:0x0632, B:284:0x05f8, B:203:0x043a, B:211:0x0493, B:339:0x06d4, B:340:0x06d9), top: B:372:0x00da }] */
    /* JADX WARN: Removed duplicated region for block: B:239:0x051f A[Catch: Exception -> 0x0175, Error -> 0x0719, TRY_LEAVE, TryCatch #8 {Exception -> 0x0175, blocks: (B:11:0x00dc, B:13:0x0134, B:14:0x013d, B:21:0x0164, B:23:0x0168, B:24:0x0170, B:29:0x017e, B:31:0x01ad, B:32:0x01b4, B:35:0x01bc, B:37:0x01c6, B:39:0x01d0, B:40:0x01d3, B:41:0x01d5, B:43:0x01dd, B:44:0x01ef, B:46:0x01f5, B:48:0x0213, B:50:0x021e, B:52:0x0224, B:54:0x022d, B:55:0x023a, B:56:0x023c, B:58:0x0244, B:60:0x0250, B:61:0x0252, B:63:0x025a, B:65:0x0268, B:67:0x0270, B:69:0x0278, B:71:0x0280, B:73:0x0288, B:75:0x0290, B:76:0x0297, B:78:0x029f, B:80:0x02ab, B:81:0x02ad, B:88:0x02bf, B:90:0x02c7, B:92:0x02cf, B:94:0x02d7, B:96:0x02df, B:98:0x02e7, B:100:0x02ef, B:102:0x02f7, B:104:0x02ff, B:106:0x0307, B:108:0x0313, B:110:0x031b, B:112:0x0326, B:114:0x032e, B:116:0x0339, B:118:0x0341, B:120:0x034c, B:122:0x0354, B:124:0x035c, B:126:0x0364, B:205:0x044d, B:212:0x0496, B:214:0x049e, B:216:0x04aa, B:217:0x04ad, B:219:0x04b5, B:221:0x04c1, B:222:0x04cc, B:224:0x04d4, B:226:0x04e2, B:227:0x04e5, B:229:0x04ed, B:231:0x04fb, B:232:0x04fe, B:234:0x0506, B:236:0x0514, B:237:0x0517, B:239:0x051f, B:246:0x0534, B:249:0x053d, B:250:0x0547, B:285:0x05fb, B:287:0x0603, B:295:0x0627, B:297:0x062b, B:299:0x0635, B:301:0x063d, B:302:0x0645, B:304:0x064d, B:319:0x0683, B:320:0x0686, B:329:0x06bc, B:298:0x0632, B:284:0x05f8, B:203:0x043a, B:211:0x0493, B:339:0x06d4, B:340:0x06d9), top: B:372:0x00da }] */
    /* JADX WARN: Removed duplicated region for block: B:287:0x0603 A[Catch: Exception -> 0x0175, Error -> 0x0719, TRY_LEAVE, TryCatch #8 {Exception -> 0x0175, blocks: (B:11:0x00dc, B:13:0x0134, B:14:0x013d, B:21:0x0164, B:23:0x0168, B:24:0x0170, B:29:0x017e, B:31:0x01ad, B:32:0x01b4, B:35:0x01bc, B:37:0x01c6, B:39:0x01d0, B:40:0x01d3, B:41:0x01d5, B:43:0x01dd, B:44:0x01ef, B:46:0x01f5, B:48:0x0213, B:50:0x021e, B:52:0x0224, B:54:0x022d, B:55:0x023a, B:56:0x023c, B:58:0x0244, B:60:0x0250, B:61:0x0252, B:63:0x025a, B:65:0x0268, B:67:0x0270, B:69:0x0278, B:71:0x0280, B:73:0x0288, B:75:0x0290, B:76:0x0297, B:78:0x029f, B:80:0x02ab, B:81:0x02ad, B:88:0x02bf, B:90:0x02c7, B:92:0x02cf, B:94:0x02d7, B:96:0x02df, B:98:0x02e7, B:100:0x02ef, B:102:0x02f7, B:104:0x02ff, B:106:0x0307, B:108:0x0313, B:110:0x031b, B:112:0x0326, B:114:0x032e, B:116:0x0339, B:118:0x0341, B:120:0x034c, B:122:0x0354, B:124:0x035c, B:126:0x0364, B:205:0x044d, B:212:0x0496, B:214:0x049e, B:216:0x04aa, B:217:0x04ad, B:219:0x04b5, B:221:0x04c1, B:222:0x04cc, B:224:0x04d4, B:226:0x04e2, B:227:0x04e5, B:229:0x04ed, B:231:0x04fb, B:232:0x04fe, B:234:0x0506, B:236:0x0514, B:237:0x0517, B:239:0x051f, B:246:0x0534, B:249:0x053d, B:250:0x0547, B:285:0x05fb, B:287:0x0603, B:295:0x0627, B:297:0x062b, B:299:0x0635, B:301:0x063d, B:302:0x0645, B:304:0x064d, B:319:0x0683, B:320:0x0686, B:329:0x06bc, B:298:0x0632, B:284:0x05f8, B:203:0x043a, B:211:0x0493, B:339:0x06d4, B:340:0x06d9), top: B:372:0x00da }] */
    /* JADX WARN: Removed duplicated region for block: B:289:0x060d  */
    /* JADX WARN: Removed duplicated region for block: B:293:0x0617 A[Catch: Exception -> 0x0627, Error -> 0x0719, TryCatch #18 {Exception -> 0x0627, blocks: (B:291:0x0611, B:293:0x0617, B:294:0x0623), top: B:391:0x0611 }] */
    /* JADX WARN: Removed duplicated region for block: B:294:0x0623 A[Catch: Exception -> 0x0627, Error -> 0x0719, TRY_LEAVE, TryCatch #18 {Exception -> 0x0627, blocks: (B:291:0x0611, B:293:0x0617, B:294:0x0623), top: B:391:0x0611 }] */
    /* JADX WARN: Removed duplicated region for block: B:297:0x062b A[Catch: Exception -> 0x0175, Error -> 0x0719, TryCatch #8 {Exception -> 0x0175, blocks: (B:11:0x00dc, B:13:0x0134, B:14:0x013d, B:21:0x0164, B:23:0x0168, B:24:0x0170, B:29:0x017e, B:31:0x01ad, B:32:0x01b4, B:35:0x01bc, B:37:0x01c6, B:39:0x01d0, B:40:0x01d3, B:41:0x01d5, B:43:0x01dd, B:44:0x01ef, B:46:0x01f5, B:48:0x0213, B:50:0x021e, B:52:0x0224, B:54:0x022d, B:55:0x023a, B:56:0x023c, B:58:0x0244, B:60:0x0250, B:61:0x0252, B:63:0x025a, B:65:0x0268, B:67:0x0270, B:69:0x0278, B:71:0x0280, B:73:0x0288, B:75:0x0290, B:76:0x0297, B:78:0x029f, B:80:0x02ab, B:81:0x02ad, B:88:0x02bf, B:90:0x02c7, B:92:0x02cf, B:94:0x02d7, B:96:0x02df, B:98:0x02e7, B:100:0x02ef, B:102:0x02f7, B:104:0x02ff, B:106:0x0307, B:108:0x0313, B:110:0x031b, B:112:0x0326, B:114:0x032e, B:116:0x0339, B:118:0x0341, B:120:0x034c, B:122:0x0354, B:124:0x035c, B:126:0x0364, B:205:0x044d, B:212:0x0496, B:214:0x049e, B:216:0x04aa, B:217:0x04ad, B:219:0x04b5, B:221:0x04c1, B:222:0x04cc, B:224:0x04d4, B:226:0x04e2, B:227:0x04e5, B:229:0x04ed, B:231:0x04fb, B:232:0x04fe, B:234:0x0506, B:236:0x0514, B:237:0x0517, B:239:0x051f, B:246:0x0534, B:249:0x053d, B:250:0x0547, B:285:0x05fb, B:287:0x0603, B:295:0x0627, B:297:0x062b, B:299:0x0635, B:301:0x063d, B:302:0x0645, B:304:0x064d, B:319:0x0683, B:320:0x0686, B:329:0x06bc, B:298:0x0632, B:284:0x05f8, B:203:0x043a, B:211:0x0493, B:339:0x06d4, B:340:0x06d9), top: B:372:0x00da }] */
    /* JADX WARN: Removed duplicated region for block: B:298:0x0632 A[Catch: Exception -> 0x0175, Error -> 0x0719, TryCatch #8 {Exception -> 0x0175, blocks: (B:11:0x00dc, B:13:0x0134, B:14:0x013d, B:21:0x0164, B:23:0x0168, B:24:0x0170, B:29:0x017e, B:31:0x01ad, B:32:0x01b4, B:35:0x01bc, B:37:0x01c6, B:39:0x01d0, B:40:0x01d3, B:41:0x01d5, B:43:0x01dd, B:44:0x01ef, B:46:0x01f5, B:48:0x0213, B:50:0x021e, B:52:0x0224, B:54:0x022d, B:55:0x023a, B:56:0x023c, B:58:0x0244, B:60:0x0250, B:61:0x0252, B:63:0x025a, B:65:0x0268, B:67:0x0270, B:69:0x0278, B:71:0x0280, B:73:0x0288, B:75:0x0290, B:76:0x0297, B:78:0x029f, B:80:0x02ab, B:81:0x02ad, B:88:0x02bf, B:90:0x02c7, B:92:0x02cf, B:94:0x02d7, B:96:0x02df, B:98:0x02e7, B:100:0x02ef, B:102:0x02f7, B:104:0x02ff, B:106:0x0307, B:108:0x0313, B:110:0x031b, B:112:0x0326, B:114:0x032e, B:116:0x0339, B:118:0x0341, B:120:0x034c, B:122:0x0354, B:124:0x035c, B:126:0x0364, B:205:0x044d, B:212:0x0496, B:214:0x049e, B:216:0x04aa, B:217:0x04ad, B:219:0x04b5, B:221:0x04c1, B:222:0x04cc, B:224:0x04d4, B:226:0x04e2, B:227:0x04e5, B:229:0x04ed, B:231:0x04fb, B:232:0x04fe, B:234:0x0506, B:236:0x0514, B:237:0x0517, B:239:0x051f, B:246:0x0534, B:249:0x053d, B:250:0x0547, B:285:0x05fb, B:287:0x0603, B:295:0x0627, B:297:0x062b, B:299:0x0635, B:301:0x063d, B:302:0x0645, B:304:0x064d, B:319:0x0683, B:320:0x0686, B:329:0x06bc, B:298:0x0632, B:284:0x05f8, B:203:0x043a, B:211:0x0493, B:339:0x06d4, B:340:0x06d9), top: B:372:0x00da }] */
    /* JADX WARN: Removed duplicated region for block: B:301:0x063d A[Catch: Exception -> 0x0175, Error -> 0x0719, TryCatch #8 {Exception -> 0x0175, blocks: (B:11:0x00dc, B:13:0x0134, B:14:0x013d, B:21:0x0164, B:23:0x0168, B:24:0x0170, B:29:0x017e, B:31:0x01ad, B:32:0x01b4, B:35:0x01bc, B:37:0x01c6, B:39:0x01d0, B:40:0x01d3, B:41:0x01d5, B:43:0x01dd, B:44:0x01ef, B:46:0x01f5, B:48:0x0213, B:50:0x021e, B:52:0x0224, B:54:0x022d, B:55:0x023a, B:56:0x023c, B:58:0x0244, B:60:0x0250, B:61:0x0252, B:63:0x025a, B:65:0x0268, B:67:0x0270, B:69:0x0278, B:71:0x0280, B:73:0x0288, B:75:0x0290, B:76:0x0297, B:78:0x029f, B:80:0x02ab, B:81:0x02ad, B:88:0x02bf, B:90:0x02c7, B:92:0x02cf, B:94:0x02d7, B:96:0x02df, B:98:0x02e7, B:100:0x02ef, B:102:0x02f7, B:104:0x02ff, B:106:0x0307, B:108:0x0313, B:110:0x031b, B:112:0x0326, B:114:0x032e, B:116:0x0339, B:118:0x0341, B:120:0x034c, B:122:0x0354, B:124:0x035c, B:126:0x0364, B:205:0x044d, B:212:0x0496, B:214:0x049e, B:216:0x04aa, B:217:0x04ad, B:219:0x04b5, B:221:0x04c1, B:222:0x04cc, B:224:0x04d4, B:226:0x04e2, B:227:0x04e5, B:229:0x04ed, B:231:0x04fb, B:232:0x04fe, B:234:0x0506, B:236:0x0514, B:237:0x0517, B:239:0x051f, B:246:0x0534, B:249:0x053d, B:250:0x0547, B:285:0x05fb, B:287:0x0603, B:295:0x0627, B:297:0x062b, B:299:0x0635, B:301:0x063d, B:302:0x0645, B:304:0x064d, B:319:0x0683, B:320:0x0686, B:329:0x06bc, B:298:0x0632, B:284:0x05f8, B:203:0x043a, B:211:0x0493, B:339:0x06d4, B:340:0x06d9), top: B:372:0x00da }] */
    /* JADX WARN: Removed duplicated region for block: B:304:0x064d A[Catch: Exception -> 0x0175, Error -> 0x0719, TRY_LEAVE, TryCatch #8 {Exception -> 0x0175, blocks: (B:11:0x00dc, B:13:0x0134, B:14:0x013d, B:21:0x0164, B:23:0x0168, B:24:0x0170, B:29:0x017e, B:31:0x01ad, B:32:0x01b4, B:35:0x01bc, B:37:0x01c6, B:39:0x01d0, B:40:0x01d3, B:41:0x01d5, B:43:0x01dd, B:44:0x01ef, B:46:0x01f5, B:48:0x0213, B:50:0x021e, B:52:0x0224, B:54:0x022d, B:55:0x023a, B:56:0x023c, B:58:0x0244, B:60:0x0250, B:61:0x0252, B:63:0x025a, B:65:0x0268, B:67:0x0270, B:69:0x0278, B:71:0x0280, B:73:0x0288, B:75:0x0290, B:76:0x0297, B:78:0x029f, B:80:0x02ab, B:81:0x02ad, B:88:0x02bf, B:90:0x02c7, B:92:0x02cf, B:94:0x02d7, B:96:0x02df, B:98:0x02e7, B:100:0x02ef, B:102:0x02f7, B:104:0x02ff, B:106:0x0307, B:108:0x0313, B:110:0x031b, B:112:0x0326, B:114:0x032e, B:116:0x0339, B:118:0x0341, B:120:0x034c, B:122:0x0354, B:124:0x035c, B:126:0x0364, B:205:0x044d, B:212:0x0496, B:214:0x049e, B:216:0x04aa, B:217:0x04ad, B:219:0x04b5, B:221:0x04c1, B:222:0x04cc, B:224:0x04d4, B:226:0x04e2, B:227:0x04e5, B:229:0x04ed, B:231:0x04fb, B:232:0x04fe, B:234:0x0506, B:236:0x0514, B:237:0x0517, B:239:0x051f, B:246:0x0534, B:249:0x053d, B:250:0x0547, B:285:0x05fb, B:287:0x0603, B:295:0x0627, B:297:0x062b, B:299:0x0635, B:301:0x063d, B:302:0x0645, B:304:0x064d, B:319:0x0683, B:320:0x0686, B:329:0x06bc, B:298:0x0632, B:284:0x05f8, B:203:0x043a, B:211:0x0493, B:339:0x06d4, B:340:0x06d9), top: B:372:0x00da }] */
    /* JADX WARN: Removed duplicated region for block: B:315:0x067e A[Catch: all -> 0x0681, TRY_LEAVE, TryCatch #0 {all -> 0x0681, blocks: (B:307:0x0657, B:309:0x065d, B:311:0x0663, B:313:0x0667, B:315:0x067e), top: B:356:0x0657 }] */
    /* JADX WARN: Removed duplicated region for block: B:370:0x054f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:392:0x068e A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:407:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public BDLocation(java.lang.String r28) {
        /*
            Method dump skipped, instructions count: 1835
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.location.BDLocation.<init>(java.lang.String):void");
    }

    private void a(Boolean bool) {
        this.u = bool.booleanValue();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String getAdCode() {
        return this.v.adcode;
    }

    public String getAddrStr() {
        return this.v.address;
    }

    public Address getAddress() {
        return this.v;
    }

    public double getAltitude() {
        return this.f;
    }

    public String getBuildingID() {
        return this.x;
    }

    public String getBuildingName() {
        return this.y;
    }

    public String getCity() {
        return this.v.city;
    }

    public String getCityCode() {
        return this.v.cityCode;
    }

    public String getCoorType() {
        return this.o;
    }

    public String getCountry() {
        return this.v.country;
    }

    public String getCountryCode() {
        return this.v.countryCode;
    }

    public long getDelayTime() {
        return this.T;
    }

    @Deprecated
    public float getDerect() {
        return this.n;
    }

    public float getDirection() {
        return this.n;
    }

    public double getDisToRealLocation() {
        return this.aa;
    }

    public String getDistrict() {
        return this.v.district;
    }

    public Location getExtraLocation(String str) {
        Bundle bundle = this.Q;
        if (bundle != null) {
            Parcelable parcelable = bundle.getParcelable(str);
            if (!(parcelable instanceof Location)) {
                return null;
            }
            return (Location) parcelable;
        }
        return null;
    }

    public String getFloor() {
        return this.w;
    }

    public double[] getFusionLocInfo(String str) {
        return this.Q.getDoubleArray(str);
    }

    public int getGpsAccuracyStatus() {
        return this.R;
    }

    public float getGpsBiasProb() {
        return this.Z;
    }

    public int getGpsCheckStatus() {
        return this.S;
    }

    public int getIndoorLocationSource() {
        return this.I;
    }

    public int getIndoorLocationSurpport() {
        return this.G;
    }

    public String getIndoorLocationSurpportBuidlingID() {
        return this.K;
    }

    public String getIndoorLocationSurpportBuidlingName() {
        return this.J;
    }

    public int getIndoorNetworkState() {
        return this.H;
    }

    public String getIndoorSurpportPolygon() {
        return this.L;
    }

    public double getLatitude() {
        return this.c;
    }

    public int getLocType() {
        return this.a;
    }

    public String getLocTypeDescription() {
        return this.N;
    }

    public String getLocationDescribe() {
        return this.r;
    }

    public String getLocationID() {
        return this.O;
    }

    public int getLocationWhere() {
        return this.B;
    }

    public double getLongitude() {
        return this.d;
    }

    public int getMockGpsProbability() {
        return this.ac;
    }

    public int getMockGpsStrategy() {
        return this.ab;
    }

    public String getNetworkLocationType() {
        return this.C;
    }

    public double getNrlLat() {
        return this.V;
    }

    public double getNrlLon() {
        return this.W;
    }

    public String getNrlResult() {
        return this.U;
    }

    public int getOperators() {
        return this.D;
    }

    public List<Poi> getPoiList() {
        return this.M;
    }

    public PoiRegion getPoiRegion() {
        return this.Y;
    }

    public String getProvince() {
        return this.v.province;
    }

    public float getRadius() {
        return this.j;
    }

    public BDLocation getReallLocation() {
        if (getMockGpsStrategy() > 0) {
            return this.ad;
        }
        return null;
    }

    public String getRetFields(String str) {
        return this.Q.getString(str);
    }

    public String getRoadLocString() {
        return this.P;
    }

    public int getSatelliteNumber() {
        this.l = true;
        return this.m;
    }

    @Deprecated
    public String getSemaAptag() {
        return this.r;
    }

    public float getSpeed() {
        return this.h;
    }

    public String getStreet() {
        return this.v.street;
    }

    public String getStreetNumber() {
        return this.v.streetNumber;
    }

    public String getTime() {
        return this.b;
    }

    public String getTown() {
        return this.v.town;
    }

    public String getTraffic() {
        return this.k;
    }

    public int getUserIndoorState() {
        return this.F;
    }

    public String getVdrJsonString() {
        Bundle bundle = this.Q;
        if (bundle == null || !bundle.containsKey("vdr")) {
            return null;
        }
        return this.Q.getString("vdr");
    }

    public boolean hasAddr() {
        return this.p;
    }

    public boolean hasAltitude() {
        return this.e;
    }

    public boolean hasRadius() {
        return this.i;
    }

    public boolean hasSateNumber() {
        return this.l;
    }

    public boolean hasSpeed() {
        return this.g;
    }

    public boolean isCellChangeFlag() {
        return this.u;
    }

    public boolean isInIndoorPark() {
        return this.X;
    }

    public boolean isIndoorLocMode() {
        return this.z;
    }

    public boolean isNrlAvailable() {
        return (this.W == Double.MIN_VALUE || this.V == Double.MIN_VALUE) ? false : true;
    }

    public int isParkAvailable() {
        return this.A;
    }

    public void setAddr(Address address) {
        if (address != null) {
            this.v = address;
            this.p = true;
        }
    }

    public void setAddrStr(String str) {
        this.q = str;
        this.p = str != null;
    }

    public void setAltitude(double d) {
        if (d < 9999.0d) {
            this.f = d;
            this.e = true;
        }
    }

    public void setBuildingID(String str) {
        this.x = str;
    }

    public void setBuildingName(String str) {
        this.y = str;
    }

    public void setCoorType(String str) {
        this.o = str;
    }

    public void setDelayTime(long j) {
        this.T = j;
    }

    public void setDirection(float f) {
        this.n = f;
    }

    public void setDisToRealLocation(double d) {
        this.aa = d;
    }

    public void setExtraLocation(String str, Location location) {
        if (this.Q == null) {
            this.Q = new Bundle();
        }
        this.Q.putParcelable(str, location);
    }

    public void setFloor(String str) {
        this.w = str;
    }

    public void setFusionLocInfo(String str, double[] dArr) {
        if (this.Q == null) {
            this.Q = new Bundle();
        }
        this.Q.putDoubleArray(str, dArr);
    }

    public void setGpsAccuracyStatus(int i) {
        this.R = i;
    }

    public void setGpsBiasProb(float f) {
        this.Z = f;
    }

    public void setGpsCheckStatus(int i) {
        this.S = i;
    }

    public void setIndoorLocMode(boolean z) {
        this.z = z;
    }

    public void setIndoorLocationSource(int i) {
        this.I = i;
    }

    public void setIndoorLocationSurpport(int i) {
        this.G = i;
    }

    public void setIndoorNetworkState(int i) {
        this.H = i;
    }

    public void setIndoorSurpportPolygon(String str) {
        this.L = str;
    }

    public void setIsInIndoorPark(boolean z) {
        this.X = z;
    }

    public void setLatitude(double d) {
        this.c = d;
    }

    public void setLocType(int i) {
        String str;
        this.a = i;
        if (i != 66) {
            if (i != 67) {
                if (i == 161) {
                    str = "NetWork location successful!";
                } else if (i == 162) {
                    str = "NetWork location failed because baidu location service can not decrypt the request query, please check the so file !";
                } else if (i == 167) {
                    str = "NetWork location failed because baidu location service can not caculate the location!";
                } else if (i != 505) {
                    switch (i) {
                        case 61:
                            setLocTypeDescription("GPS location successful!");
                            setUserIndoorState(0);
                            return;
                        case 62:
                            str = "Location failed beacuse we can not get any loc information!";
                            break;
                        case 63:
                            break;
                        default:
                            str = "UnKnown!";
                            break;
                    }
                } else {
                    str = "NetWork location failed because baidu location service check the key is unlegal, please check the key in AndroidManifest.xml !";
                }
            }
            str = "Offline location failed, please check the net (wifi/cell)!";
        } else {
            str = "Offline location successful!";
        }
        setLocTypeDescription(str);
    }

    public void setLocTypeDescription(String str) {
        this.N = str;
    }

    public void setLocationDescribe(String str) {
        this.r = str;
    }

    public void setLocationID(String str) {
        this.O = str;
    }

    public void setLocationWhere(int i) {
        this.B = i;
    }

    public void setLongitude(double d) {
        this.d = d;
    }

    public void setMockGpsProbability(int i) {
        this.ac = i;
    }

    public void setMockGpsStrategy(int i) {
        this.ab = i;
    }

    public void setNetworkLocationType(String str) {
        this.C = str;
    }

    public void setNrlData(String str) {
        this.U = str;
    }

    public void setOperators(int i) {
        this.D = i;
    }

    public void setParkAvailable(int i) {
        this.A = i;
    }

    public void setPoiList(List<Poi> list) {
        this.M = list;
    }

    public void setPoiRegion(PoiRegion poiRegion) {
        this.Y = poiRegion;
    }

    public void setRadius(float f) {
        this.j = f;
        this.i = true;
    }

    public void setReallLocation(BDLocation bDLocation) {
        if (getMockGpsStrategy() > 0) {
            this.ad = bDLocation;
        }
    }

    public void setRetFields(String str, String str2) {
        if (this.Q == null) {
            this.Q = new Bundle();
        }
        this.Q.putString(str, str2);
    }

    public void setRoadLocString(float f, float f2) {
        String str = "";
        String format = ((double) f) > 0.001d ? String.format("%.2f", Float.valueOf(f)) : str;
        if (f2 > 0.001d) {
            str = String.format("%.2f", Float.valueOf(f2));
        }
        String str2 = this.U;
        if (str2 != null) {
            this.P = String.format(Locale.US, "%s|%s,%s", str2, format, str);
        }
    }

    public void setSatelliteNumber(int i) {
        this.m = i;
    }

    public void setSpeed(float f) {
        this.h = f;
        this.g = true;
    }

    public void setTime(String str) {
        this.b = str;
        setLocationID(j.a(str));
    }

    public void setTraffic(String str) {
        this.k = str;
    }

    public void setUserIndoorState(int i) {
        this.F = i;
    }

    public void setVdrJsonValue(String str) {
        if (this.Q == null) {
            this.Q = new Bundle();
        }
        this.Q.putString("vdr", str);
    }

    public String toString() {
        return "&loctype=" + getLocType() + "&lat=" + getLatitude() + "&lon=" + getLongitude() + "&radius=" + getRadius() + "&biasprob=" + getGpsBiasProb();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.a);
        parcel.writeString(this.b);
        parcel.writeDouble(this.c);
        parcel.writeDouble(this.d);
        parcel.writeDouble(this.f);
        parcel.writeFloat(this.h);
        parcel.writeFloat(this.j);
        parcel.writeString(this.k);
        parcel.writeInt(this.m);
        parcel.writeFloat(this.n);
        parcel.writeString(this.w);
        parcel.writeInt(this.A);
        parcel.writeString(this.x);
        parcel.writeString(this.y);
        parcel.writeString(this.C);
        parcel.writeString(this.v.province);
        parcel.writeString(this.v.city);
        parcel.writeString(this.v.district);
        parcel.writeString(this.v.street);
        parcel.writeString(this.v.streetNumber);
        parcel.writeString(this.v.cityCode);
        parcel.writeString(this.v.address);
        parcel.writeString(this.v.country);
        parcel.writeString(this.v.countryCode);
        parcel.writeString(this.v.adcode);
        parcel.writeString(this.v.town);
        parcel.writeInt(this.D);
        parcel.writeString(this.E);
        parcel.writeString(this.r);
        parcel.writeString(this.s);
        parcel.writeString(this.t);
        parcel.writeInt(this.B);
        parcel.writeString(this.N);
        parcel.writeInt(this.F);
        parcel.writeInt(this.G);
        parcel.writeInt(this.H);
        parcel.writeInt(this.I);
        parcel.writeString(this.J);
        parcel.writeString(this.K);
        parcel.writeString(this.L);
        parcel.writeInt(this.R);
        parcel.writeString(this.O);
        parcel.writeInt(this.S);
        parcel.writeString(this.P);
        parcel.writeString(this.U);
        parcel.writeLong(this.T);
        parcel.writeDouble(this.V);
        parcel.writeDouble(this.W);
        parcel.writeFloat(this.Z);
        parcel.writeDouble(this.aa);
        parcel.writeInt(this.ab);
        parcel.writeInt(this.ac);
        parcel.writeString(this.o);
        parcel.writeParcelable(this.ad, i);
        parcel.writeBooleanArray(new boolean[]{this.e, this.g, this.i, this.l, this.p, this.u, this.z, this.X});
        parcel.writeList(this.M);
        parcel.writeBundle(this.Q);
        parcel.writeParcelable(this.Y, i);
    }
}
