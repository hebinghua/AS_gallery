package miuix.animation.utils;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import ch.qos.logback.core.util.FileSize;
import com.xiaomi.mirror.synergy.CallMethod;
import dalvik.system.PathClassLoader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes3.dex */
public class DeviceUtils {
    public static int HIGH;
    public static int LOW;
    public static int MIDDLE;
    public static final String[] STOCK_DEVICE;
    public static int TYPE_CPU;
    public static int TYPE_GPU;
    public static int TYPE_RAM;
    public static int UNKNOWN;
    public static Application application;
    public static Context applicationContext;
    public static Constructor<Class> mConstructor;
    public static Method mGetDeviceLevel;
    public static Method mGetDeviceLevelForWhole;
    public static Method mIsSupportPrune;
    public static Object mPerf;
    public static Class perfClass;
    public static PathClassLoader perfClassLoader;
    public static final Pattern SM_PATTERN = Pattern.compile("Inc ([A-Z]+)([\\d]+)");
    public static final Pattern MT_PATTERN = Pattern.compile("MT([\\d]{2})([\\d]+)");
    public static int mLevel = -1;
    public static int mCpuLevel = -1;
    public static int mGpuLevel = -1;
    public static int mRamLevel = -1;
    public static int mTotalRam = Integer.MAX_VALUE;
    public static int DEV_STANDARD_VERSION = 1;
    public static int mLastVersion = 1;

    static {
        mConstructor = null;
        mPerf = null;
        mGetDeviceLevel = null;
        mGetDeviceLevelForWhole = null;
        mIsSupportPrune = null;
        TYPE_RAM = 1;
        TYPE_CPU = 2;
        TYPE_GPU = 3;
        try {
            PathClassLoader pathClassLoader = new PathClassLoader("/system/framework/MiuiBooster.jar", ClassLoader.getSystemClassLoader());
            perfClassLoader = pathClassLoader;
            Class loadClass = pathClassLoader.loadClass("com.miui.performance.DeviceLevelUtils");
            perfClass = loadClass;
            mConstructor = loadClass.getConstructor(Context.class);
            Class<?> cls = Integer.TYPE;
            mGetDeviceLevel = perfClass.getDeclaredMethod("getDeviceLevel", cls, cls);
            mGetDeviceLevelForWhole = perfClass.getDeclaredMethod("getDeviceLevel", cls);
            mIsSupportPrune = perfClass.getDeclaredMethod("isSupportPrune", new Class[0]);
            TYPE_RAM = ((Integer) getStaticObjectField(perfClass, "DEVICE_LEVEL_FOR_RAM", cls)).intValue();
            TYPE_CPU = ((Integer) getStaticObjectField(perfClass, "DEVICE_LEVEL_FOR_CPU", cls)).intValue();
            TYPE_GPU = ((Integer) getStaticObjectField(perfClass, "DEVICE_LEVEL_FOR_GPU", cls)).intValue();
            LOW = ((Integer) getStaticObjectField(perfClass, "LOW_DEVICE", cls)).intValue();
            MIDDLE = ((Integer) getStaticObjectField(perfClass, "MIDDLE_DEVICE", cls)).intValue();
            HIGH = ((Integer) getStaticObjectField(perfClass, "HIGH_DEVICE", cls)).intValue();
            UNKNOWN = ((Integer) getStaticObjectField(perfClass, "DEVICE_LEVEL_UNKNOWN", cls)).intValue();
        } catch (Exception e) {
            Log.e("DeviceUtils", "DeviceLevel(): Load Class Exception:" + e);
        }
        if (applicationContext == null) {
            try {
                Application application2 = (Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication", new Class[0]).invoke(null, null);
                application = application2;
                if (application2 != null) {
                    applicationContext = application2.getApplicationContext();
                }
            } catch (Exception e2) {
                Log.e("DeviceUtils", "android.app.ActivityThread Exception:" + e2);
            }
        }
        if (applicationContext == null) {
            try {
                Application application3 = (Application) Class.forName("android.app.AppGlobals").getMethod("getInitialApplication", new Class[0]).invoke(null, null);
                application = application3;
                if (application3 != null) {
                    applicationContext = application3.getApplicationContext();
                }
            } catch (Exception e3) {
                Log.e("DeviceUtils", "android.app.AppGlobals Exception:" + e3);
            }
        }
        try {
            Constructor<Class> constructor = mConstructor;
            if (constructor != null) {
                mPerf = constructor.newInstance(applicationContext);
            }
        } catch (Exception e4) {
            Log.e("DeviceUtils", "DeviceLevelUtils(): newInstance Exception:" + e4);
            e4.printStackTrace();
        }
        STOCK_DEVICE = new String[]{"cactus", "cereus", "pine", "olive", "ginkgo", "olivelite", "olivewood", "willow", "wayne", "dandelion", "angelica", "angelicain", "whyred", "tulip", "onc", "onclite", "lavender", "lotus", "laurus", "merlinnfc", "merlin", "lancelot", "citrus", "pomelo", "lemon", "shiva", "lime", "cannon", "curtana", "durandal", "excalibur", "joyeuse", "gram", "sunny", "mojito", "rainbow", "cattail", "angelican", "camellia"};
    }

    /* loaded from: classes3.dex */
    public static class CpuInfo {
        public int architecture;
        public int id;
        public int implementor;
        public int maxFreq;
        public int part;

        public String toString() {
            return "CpuInfo{id=" + this.id + ", implementor=" + Integer.toHexString(this.implementor) + ", architecture=" + this.architecture + ", part=" + Integer.toHexString(this.part) + ", maxFreq=" + this.maxFreq + '}';
        }
    }

    /* loaded from: classes3.dex */
    public static class CpuStats {
        public int bigCoreCount;
        public int level = -1;
        public int maxFreq;
        public int smallCoreCount;

        public String toString() {
            return "CpuStats{level=" + this.level + ", maxFreq=" + this.maxFreq + ", bigCoreCount=" + this.bigCoreCount + ", smallCoreCount=" + this.smallCoreCount + '}';
        }
    }

    public static int getTotalRam() {
        if (mTotalRam == Integer.MAX_VALUE) {
            try {
                mTotalRam = (int) (((((Long) Class.forName("miui.util.HardwareInfo").getMethod("getTotalPhysicalMemory", new Class[0]).invoke(null, new Object[0])).longValue() / FileSize.KB_COEFFICIENT) / FileSize.KB_COEFFICIENT) / FileSize.KB_COEFFICIENT);
            } catch (Throwable th) {
                Log.e("DeviceUtils", th.getMessage());
                mTotalRam = 0;
            }
        }
        return mTotalRam;
    }

    public static boolean isMiuiLite() {
        try {
            return ((Boolean) Class.forName("miui.os.Build").getDeclaredField("IS_MIUI_LITE_VERSION").get(null)).booleanValue();
        } catch (Throwable th) {
            Log.i("DeviceUtils", "getDeviceLevel failed", th);
            return false;
        }
    }

    public static int getDeviceLevel() {
        return getDeviceLevel(DEV_STANDARD_VERSION);
    }

    public static int getDeviceLevel(int i) {
        int i2;
        if (mLastVersion != i || (i2 = mLevel) == -1) {
            mLastVersion = i;
            int deviceLevel2 = getDeviceLevel2(i);
            mLevel = deviceLevel2;
            return deviceLevel2 != -1 ? deviceLevel2 : getDeviceLevel1();
        }
        return i2;
    }

    public static int getDeviceLevel(int i, int i2) {
        int i3;
        int i4;
        int i5;
        if (i2 == TYPE_CPU) {
            if (mLastVersion == i && (i5 = mCpuLevel) != -1) {
                return i5;
            }
        } else if (i2 == TYPE_GPU) {
            if (mLastVersion == i && (i4 = mGpuLevel) != -1) {
                return i4;
            }
        } else if (i2 == TYPE_RAM && mLastVersion == i && (i3 = mRamLevel) != -1) {
            return i3;
        }
        int deviceLevel2 = getDeviceLevel2(i, i2);
        if (deviceLevel2 != -1) {
            return setDeviceLevel(i, deviceLevel2, i2);
        }
        return setDeviceLevel(i, getDeviceLevel1(i2), i2);
    }

    public static int setDeviceLevel(int i, int i2, int i3) {
        mLastVersion = i;
        if (i3 == TYPE_CPU) {
            mCpuLevel = i2;
            return i2;
        } else if (i3 == TYPE_GPU) {
            mGpuLevel = i2;
            return i2;
        } else if (i3 != TYPE_RAM) {
            return -1;
        } else {
            mRamLevel = i2;
            return i2;
        }
    }

    public static int getDeviceLevel1(int i) {
        if (i == TYPE_RAM) {
            int totalRam = getTotalRam();
            if (totalRam > 6) {
                return 2;
            }
            if (totalRam > 4) {
                return 1;
            }
            return totalRam > 0 ? 0 : -1;
        } else if (i != TYPE_CPU) {
            return -1;
        } else {
            return getCpuLevel();
        }
    }

    public static int getDeviceLevel1() {
        int i = mLevel;
        if (i != -1) {
            return i;
        }
        if (isMiuiLite()) {
            mLevel = 0;
        } else {
            mLevel = getMinLevel(getDeviceLevel1(TYPE_CPU), getDeviceLevel1(TYPE_RAM), getDeviceLevel(DEV_STANDARD_VERSION, TYPE_GPU));
        }
        return mLevel;
    }

    public static int getMinLevel(int... iArr) {
        if (iArr.length == 0) {
            return -1;
        }
        int i = iArr[0];
        for (int i2 : iArr) {
            if (i2 > -1 && i2 < i) {
                i = i2;
            }
        }
        return i;
    }

    public static int getCpuLevel() {
        int i;
        String hardwareInfo = getHardwareInfo();
        if (hardwareInfo.length() <= 0) {
            i = -1;
        } else if (hardwareInfo.contains("Qualcomm")) {
            i = getQualcommCpuLevel(hardwareInfo);
        } else {
            i = getMtkCpuLevel(hardwareInfo);
        }
        return i == -1 ? getCpuStats().level : i;
    }

    public static String getHardwareInfo() {
        try {
            Scanner scanner = new Scanner(new File("/proc/cpuinfo"));
            while (scanner.hasNextLine()) {
                String nextLine = scanner.nextLine();
                if (!scanner.hasNextLine()) {
                    String[] split = nextLine.split(": ");
                    if (split.length > 1) {
                        return split[1];
                    }
                }
            }
            return "";
        } catch (Exception e) {
            Log.e("DeviceUtils", "getChipSetFromCpuInfo failed", e);
            return "";
        }
    }

    public static int getQualcommCpuLevel(String str) {
        String group;
        String group2;
        Matcher matcher = SM_PATTERN.matcher(str);
        if (!matcher.find() || (group = matcher.group(1)) == null || (group2 = matcher.group(2)) == null) {
            return -1;
        }
        String lowerCase = group.toLowerCase(Locale.ENGLISH);
        if (!lowerCase.equals("sm")) {
            return lowerCase.equals("sdm") ? Integer.parseInt(group2.substring(0, 1)) >= 7 ? 1 : 0 : lowerCase.equals("msm") ? 0 : -1;
        }
        int parseInt = Integer.parseInt(group2.substring(0, 1));
        if (parseInt >= 8) {
            return 2;
        }
        return parseInt >= 7 ? 1 : 0;
    }

    public static int getMtkCpuLevel(String str) {
        String group;
        String group2;
        Matcher matcher = MT_PATTERN.matcher(str);
        if (!matcher.find() || (group = matcher.group(1)) == null || (group2 = matcher.group(2)) == null) {
            return -1;
        }
        return (Integer.parseInt(group) != 68 || Integer.parseInt(group2) < 73) ? 0 : 1;
    }

    public static CpuStats getCpuStats() {
        List<CpuInfo> cpuInfoList = getCpuInfoList();
        CpuStats cpuStats = new CpuStats();
        if (cpuInfoList.size() < 8) {
            cpuStats.level = 0;
        }
        doCpuStats(cpuStats, cpuInfoList);
        return cpuStats;
    }

    public static void doCpuStats(CpuStats cpuStats, List<CpuInfo> list) {
        for (CpuInfo cpuInfo : list) {
            if (cpuInfo.architecture < 8) {
                cpuStats.level = 0;
            }
            int i = cpuInfo.maxFreq;
            if (i > cpuStats.maxFreq) {
                cpuStats.maxFreq = i;
            }
            if (i >= 2000000) {
                cpuStats.bigCoreCount++;
            } else {
                cpuStats.smallCoreCount++;
            }
        }
        decideLevel(cpuStats);
    }

    public static void decideLevel(CpuStats cpuStats) {
        if (cpuStats.level != -1) {
            return;
        }
        if (cpuStats.bigCoreCount >= 4) {
            int i = cpuStats.maxFreq;
            if (i > 2700000) {
                cpuStats.level = 2;
            } else if (i > 2300000) {
                cpuStats.level = 1;
            } else {
                cpuStats.level = 0;
            }
        } else if (cpuStats.maxFreq > 2300000) {
            cpuStats.level = 1;
        } else {
            cpuStats.level = 0;
        }
    }

    public static List<CpuInfo> getCpuInfoList() {
        ArrayList arrayList = new ArrayList();
        try {
            Scanner scanner = new Scanner(new File("/proc/cpuinfo"));
            CpuInfo cpuInfo = null;
            while (scanner.hasNextLine()) {
                String[] split = scanner.nextLine().split(": ");
                if (split.length > 1) {
                    cpuInfo = parseLine(split, arrayList, cpuInfo);
                }
            }
        } catch (Exception e) {
            Log.e("DeviceUtils", "getChipSetFromCpuInfo failed", e);
        }
        return arrayList;
    }

    public static CpuInfo parseLine(String[] strArr, List<CpuInfo> list, CpuInfo cpuInfo) {
        String trim = strArr[1].trim();
        if (strArr[0].contains("processor") && TextUtils.isDigitsOnly(trim)) {
            CpuInfo createCpuInfo = createCpuInfo(trim);
            list.add(createCpuInfo);
            return createCpuInfo;
        } else if (cpuInfo == null) {
            return cpuInfo;
        } else {
            getCpuInfo(strArr[0], trim, cpuInfo);
            return cpuInfo;
        }
    }

    public static CpuInfo createCpuInfo(String str) {
        CpuInfo cpuInfo = new CpuInfo();
        int parseInt = Integer.parseInt(str);
        cpuInfo.id = parseInt;
        String contentFromFileInfo = getContentFromFileInfo(String.format(Locale.ENGLISH, "/sys/devices/system/cpu/cpu%d/cpufreq/cpuinfo_max_freq", Integer.valueOf(parseInt)));
        if (contentFromFileInfo != null) {
            cpuInfo.maxFreq = Integer.parseInt(contentFromFileInfo);
        }
        return cpuInfo;
    }

    public static void getCpuInfo(String str, String str2, CpuInfo cpuInfo) {
        if (str.contains("CPU implementer")) {
            cpuInfo.implementor = toInt(str2);
        } else if (str.contains("CPU architecture")) {
            cpuInfo.architecture = toInt(str2);
        } else if (!str.contains("CPU part")) {
        } else {
            cpuInfo.part = toInt(str2);
        }
    }

    public static int toInt(String str) {
        if (str.startsWith("0x")) {
            return Integer.parseInt(str.substring(2), 16);
        }
        return Integer.parseInt(str);
    }

    public static String getContentFromFileInfo(String str) {
        FileInputStream fileInputStream;
        FileInputStream fileInputStream2 = null;
        try {
            fileInputStream = new FileInputStream(str);
        } catch (IOException unused) {
            fileInputStream = null;
        } catch (Throwable th) {
            th = th;
        }
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            String readLine = bufferedReader.readLine();
            bufferedReader.close();
            try {
                fileInputStream.close();
            } catch (IOException unused2) {
            }
            return readLine;
        } catch (IOException unused3) {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException unused4) {
                }
            }
            return null;
        } catch (Throwable th2) {
            th = th2;
            fileInputStream2 = fileInputStream;
            if (fileInputStream2 != null) {
                try {
                    fileInputStream2.close();
                } catch (IOException unused5) {
                }
            }
            throw th;
        }
    }

    public static int getDeviceLevel2(int i) {
        int i2;
        try {
            i2 = ((Integer) mGetDeviceLevelForWhole.invoke(mPerf, Integer.valueOf(i))).intValue();
        } catch (Exception e) {
            Log.e("DeviceUtils", "getDeviceLevel failed , e:" + e.toString());
            i2 = -1;
        }
        return transDeviceLevel(i2);
    }

    public static int getDeviceLevel2(int i, int i2) {
        int i3;
        try {
            i3 = ((Integer) mGetDeviceLevel.invoke(mPerf, Integer.valueOf(i), Integer.valueOf(i2))).intValue();
        } catch (Exception e) {
            Log.e("DeviceUtils", "getDeviceLevel failed , e:" + e.toString());
            i3 = -1;
        }
        return transDeviceLevel(i3);
    }

    public static int transDeviceLevel(int i) {
        if (i == LOW) {
            return 0;
        }
        if (i == MIDDLE) {
            return 1;
        }
        return i == HIGH ? 2 : -1;
    }

    public static <T> T getStaticObjectField(Class<?> cls, String str, Class<T> cls2) throws Exception {
        Field declaredField = cls.getDeclaredField(str);
        declaredField.setAccessible(true);
        return (T) declaredField.get(null);
    }

    public static String getProductDevice() {
        try {
            return (String) Class.forName("android.os.SystemProperties").getMethod(CallMethod.METHOD_GET, String.class, String.class).invoke(null, "ro.product.device", "");
        } catch (Exception e) {
            Log.e("DeviceUtils", "getProductDevice failed , e:" + e.toString());
            return "";
        }
    }

    public static boolean isStockDevice() {
        String productDevice = getProductDevice();
        if (productDevice != null && productDevice.length() != 0) {
            for (String str : STOCK_DEVICE) {
                if (str.equalsIgnoreCase(productDevice)) {
                    return true;
                }
            }
        }
        return false;
    }
}
