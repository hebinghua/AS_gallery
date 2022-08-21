package com.nexstreaming.app.common.norm;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* compiled from: NormTable.java */
/* loaded from: classes3.dex */
public abstract class b {
    public boolean addedOrUpdatedToDb = false;
    private final com.nexstreaming.app.common.norm.c tableInfo = com.nexstreaming.app.common.norm.c.a((Class<? extends b>) getClass());

    /* compiled from: NormTable.java */
    @Retention(RetentionPolicy.RUNTIME)
    /* loaded from: classes3.dex */
    public @interface a {
        int a() default 11;
    }

    /* compiled from: NormTable.java */
    @Retention(RetentionPolicy.RUNTIME)
    /* renamed from: com.nexstreaming.app.common.norm.b$b  reason: collision with other inner class name */
    /* loaded from: classes3.dex */
    public @interface InterfaceC0101b {
    }

    /* compiled from: NormTable.java */
    @Retention(RetentionPolicy.RUNTIME)
    /* loaded from: classes3.dex */
    public @interface c {
    }

    /* compiled from: NormTable.java */
    @Retention(RetentionPolicy.RUNTIME)
    /* loaded from: classes3.dex */
    public @interface d {
    }

    /* compiled from: NormTable.java */
    @Retention(RetentionPolicy.RUNTIME)
    /* loaded from: classes3.dex */
    public @interface e {
    }

    /* compiled from: NormTable.java */
    @Retention(RetentionPolicy.RUNTIME)
    /* loaded from: classes3.dex */
    public @interface f {
    }

    /* compiled from: NormTable.java */
    @Retention(RetentionPolicy.RUNTIME)
    /* loaded from: classes3.dex */
    public @interface g {
    }

    public com.nexstreaming.app.common.norm.c getTableInfo() {
        return this.tableInfo;
    }

    public boolean wasAddedToDb() {
        return this.addedOrUpdatedToDb;
    }

    public long getDbRowID() {
        try {
            return getTableInfo().f().b.getLong(this);
        } catch (IllegalAccessException e2) {
            throw new IllegalStateException(e2);
        }
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof b)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        b bVar = (b) obj;
        return bVar.getTableInfo().c().equals(getTableInfo().c()) && getDbRowID() == bVar.getDbRowID();
    }

    public int hashCode() {
        return (int) (getDbRowID() * getTableInfo().c().hashCode());
    }
}
