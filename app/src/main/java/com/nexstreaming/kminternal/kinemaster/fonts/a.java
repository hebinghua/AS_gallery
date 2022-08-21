package com.nexstreaming.kminternal.kinemaster.fonts;

import android.graphics.Typeface;
import android.os.Build;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* compiled from: BuiltInFonts.java */
/* loaded from: classes3.dex */
class a {
    private static String a(String str) {
        char[] charArray;
        StringBuffer stringBuffer = new StringBuffer();
        boolean z = true;
        for (char c : str.toCharArray()) {
            if (z) {
                if (c >= 'a' && c <= 'z') {
                    c = (char) (c - ' ');
                }
                stringBuffer.append(c);
                z = false;
            } else if (c == '_' || c == '-') {
                stringBuffer.append(' ');
                z = true;
            } else {
                stringBuffer.append(c);
            }
        }
        return stringBuffer.toString();
    }

    private static String b(String str) {
        char[] charArray;
        StringBuffer stringBuffer = new StringBuffer();
        for (char c : str.toLowerCase().toCharArray()) {
            if (c != '_' && c != '-' && c != ' ') {
                stringBuffer.append(c);
            }
        }
        return stringBuffer.toString();
    }

    public static List<Font> a() {
        String str;
        String str2;
        String str3;
        String[] strArr;
        String str4 = "knewave.ttf";
        ArrayList arrayList = new ArrayList();
        String str5 = "leaguegothic.ttf";
        String str6 = "lato-bold.ttf";
        arrayList.addAll(Arrays.asList(new Font("system.robotothin", "android", Typeface.create("sans-serif-thin", 0), "Roboto Thin"), new Font("system.robotolight", "android", Typeface.create("sans-serif-light", 0), "Roboto Light"), new Font("system.droidsans", "android", Typeface.create("sans-serif", 0), "Roboto Regular"), new Font("system.droidsansb", "android", Typeface.create("sans-serif", 1), "Roboto Bold"), new Font("system.robotonthini", "android", Typeface.create("sans-serif-thin", 2), "Roboto Thin Italic"), new Font("system.robotolighti", "android", Typeface.create("sans-serif-light", 2), "Roboto Light Italic"), new Font("system.robotoi", "android", Typeface.create("sans-serif", 2), "Roboto Regular Italic"), new Font("system.robotobi", "android", Typeface.create("sans-serif", 3), "Roboto Bold Italic"), new Font("system.robotocond", "android", Typeface.create("sans-serif-condensed", 0), "Roboto Condensed Regular"), new Font("system.robotocondi", "android", Typeface.create("sans-serif-condensed", 2), "Roboto Condensed Italic"), new Font("system.robotocondb", "android", Typeface.create("sans-serif-condensed", 1), "Roboto Condensed Bold"), new Font("system.robotocondbi", "android", Typeface.create("sans-serif-condensed", 3), "Roboto Condensed Bold Italic"), new Font("system.droidserif", "android", Typeface.create(Typeface.SERIF, 0), "Noto Serif Regular"), new Font("system.droidserifb", "android", Typeface.create(Typeface.SERIF, 1), "Noto Serif Bold"), new Font("system.droidserifi", "android", Typeface.create(Typeface.SERIF, 2), "Noto Serif Italic"), new Font("system.droidserifbi", "android", Typeface.create(Typeface.SERIF, 3), "Noto Serif Bold Italic")));
        try {
            String[] list = com.nexstreaming.kminternal.kinemaster.config.a.a().b().getAssets().list("");
            int length = list.length;
            int i = 0;
            while (i < length) {
                String str7 = list[i];
                if (str7.endsWith(".ttf")) {
                    if (str7.compareTo("bevan.ttf") == 0) {
                        arrayList.add(new Font("builtin.font.bevan", "latin", "bevan.ttf", "Bevan"));
                    } else if (str7.compareTo("creepster-regular.ttf") == 0) {
                        arrayList.add(new Font("builtin.font.creepster", "latin", "creepster-regular.ttf", "Creepster"));
                    } else if (str7.compareTo("goudy_stm_italic.ttf") == 0) {
                        arrayList.add(new Font("builtin.font.sortsmillgoudyital", "latin", "goudy_stm_italic.ttf", "Sorts Mill Goudy Italic"));
                    } else if (str7.compareTo("greatvibes-regular.ttf") == 0) {
                        arrayList.add(new Font("builtin.font.greatvibes", "latin", "greatvibes-regular.ttf", "Great Vibes"));
                    } else if (str7.compareTo("junction.ttf") == 0) {
                        arrayList.add(new Font("builtin.font.junction", "latin", "junction.ttf", "Junction"));
                    } else if (str7.compareTo(str4) == 0) {
                        arrayList.add(new Font("builtin.font.knewave", "latin", str4, "Knewave"));
                    } else {
                        str3 = str6;
                        if (str7.compareTo(str3) == 0) {
                            arrayList.add(new Font("builtin.font.latobold", "latin", str3, "Lato Bold"));
                            str = str4;
                            str2 = str5;
                            strArr = list;
                            i++;
                            str6 = str3;
                            list = strArr;
                            str5 = str2;
                            str4 = str;
                        } else {
                            String str8 = str5;
                            if (str7.compareTo(str8) == 0) {
                                strArr = list;
                                arrayList.add(new Font("builtin.font.leaguegothic", "latin", str8, "League Gothic"));
                                str2 = str8;
                            } else {
                                strArr = list;
                                if (str7.compareTo("leaguescript.ttf") == 0) {
                                    str2 = str8;
                                    arrayList.add(new Font("builtin.font.leaguescriptthin", "latin", "leaguescript.ttf", "League Script"));
                                } else {
                                    str2 = str8;
                                    if (str7.compareTo("lindenhill.ttf") == 0) {
                                        arrayList.add(new Font("builtin.font.lindenhillregular", "latin", "lindenhill.ttf", "Linden Hill"));
                                    } else if (str7.compareTo("orbitron-bold.ttf") == 0) {
                                        arrayList.add(new Font("builtin.font.orbitronbold", "latin", "orbitron-bold.ttf", "Orbitron Bold"));
                                    } else if (str7.compareTo("orbitron-medium.ttf") == 0) {
                                        arrayList.add(new Font("builtin.font.orbitronmedium", "latin", "orbitron-medium.ttf", "Orbitron Medium"));
                                    } else if (str7.compareTo("raleway_thin.ttf") == 0) {
                                        arrayList.add(new Font("builtin.font.ralewaythin", "latin", "raleway_thin.ttf", "Raleway Thin"));
                                    } else if (str7.compareTo("redressed.ttf") == 0) {
                                        arrayList.add(new Font("builtin.font.redressedregular", "latin", "redressed.ttf", "Redressed"));
                                    } else if (str7.compareTo("sniglet.ttf") == 0) {
                                        arrayList.add(new Font("builtin.font.sniglet", "latin", "sniglet.ttf", "Sniglet"));
                                    } else if (!str7.startsWith("_H_")) {
                                        String substring = str7.substring(0, str7.length() - 4);
                                        StringBuilder sb = new StringBuilder();
                                        str = str4;
                                        sb.append("builtin.font.");
                                        sb.append(b(substring));
                                        arrayList.add(new Font(sb.toString(), "latin", str7, a(substring)));
                                        i++;
                                        str6 = str3;
                                        list = strArr;
                                        str5 = str2;
                                        str4 = str;
                                    }
                                }
                            }
                            str = str4;
                            i++;
                            str6 = str3;
                            list = strArr;
                            str5 = str2;
                            str4 = str;
                        }
                    }
                }
                str = str4;
                str2 = str5;
                str3 = str6;
                strArr = list;
                i++;
                str6 = str3;
                list = strArr;
                str5 = str2;
                str4 = str;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 21) {
            arrayList.addAll(Arrays.asList(new Font("system.robotomed", "android", Typeface.create("sans-serif-medium", 0), "Roboto Medium"), new Font("system.robotomedi", "android", Typeface.create("sans-serif-medium", 2), "Roboto Medium Italic"), new Font("system.robotoblk", "android", Typeface.create("sans-serif-black", 0), "Roboto Black"), new Font("system.robotoblki", "android", Typeface.create("sans-serif-black", 2), "Roboto Black Italic")));
            arrayList.addAll(Arrays.asList(new Font("system.cursive", "android", Typeface.create("cursive", 0), "Dancing Script Regular"), new Font("system.cursiveb", "android", Typeface.create("cursive", 1), "Dancing Script Bold"), new Font("system.mono", "android", Typeface.create("monospace", 0), "Droid Sans Mono")));
        }
        return arrayList;
    }
}
