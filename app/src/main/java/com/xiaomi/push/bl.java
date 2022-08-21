package com.xiaomi.push;

import java.util.LinkedList;

/* loaded from: classes3.dex */
public class bl {
    public LinkedList<a> a = new LinkedList<>();

    /* loaded from: classes3.dex */
    public static class a {
        public static final bl a = new bl();

        /* renamed from: a  reason: collision with other field name */
        public int f132a;

        /* renamed from: a  reason: collision with other field name */
        public Object f133a;

        /* renamed from: a  reason: collision with other field name */
        public String f134a;

        public a(int i, Object obj) {
            this.f132a = i;
            this.f133a = obj;
        }
    }

    public static bl a() {
        return a.a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public synchronized int m1974a() {
        return this.a.size();
    }

    /* renamed from: a  reason: collision with other method in class */
    public synchronized LinkedList<a> m1975a() {
        LinkedList<a> linkedList;
        linkedList = this.a;
        this.a = new LinkedList<>();
        return linkedList;
    }

    /* renamed from: a  reason: collision with other method in class */
    public final void m1976a() {
        if (this.a.size() > 100) {
            this.a.removeFirst();
        }
    }

    public synchronized void a(Object obj) {
        this.a.add(new a(0, obj));
        m1976a();
    }
}
