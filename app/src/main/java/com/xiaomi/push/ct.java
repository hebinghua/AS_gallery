package com.xiaomi.push;

import java.net.InetSocketAddress;

/* loaded from: classes3.dex */
public final class ct {
    public int a;

    /* renamed from: a  reason: collision with other field name */
    public String f173a;

    public ct(String str, int i) {
        this.f173a = str;
        this.a = i;
    }

    public static ct a(String str, int i) {
        int lastIndexOf = str.lastIndexOf(":");
        if (lastIndexOf != -1) {
            String substring = str.substring(0, lastIndexOf);
            try {
                int parseInt = Integer.parseInt(str.substring(lastIndexOf + 1));
                if (parseInt > 0) {
                    i = parseInt;
                }
            } catch (NumberFormatException unused) {
            }
            str = substring;
        }
        return new ct(str, i);
    }

    /* renamed from: a  reason: collision with other method in class */
    public static InetSocketAddress m2023a(String str, int i) {
        ct a = a(str, i);
        return new InetSocketAddress(a.m2024a(), a.a());
    }

    public int a() {
        return this.a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public String m2024a() {
        return this.f173a;
    }

    public String toString() {
        if (this.a > 0) {
            return this.f173a + ":" + this.a;
        }
        return this.f173a;
    }
}
