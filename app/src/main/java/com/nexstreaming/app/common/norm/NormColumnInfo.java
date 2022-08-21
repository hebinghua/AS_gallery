package com.nexstreaming.app.common.norm;

import android.graphics.Bitmap;
import com.nexstreaming.app.common.norm.b;
import com.xiaomi.stat.a.j;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

/* loaded from: classes3.dex */
public class NormColumnInfo {
    public final String a;
    public final Field b;
    public final ColumnType c;
    public final boolean d;
    public final boolean e;
    public final boolean f;
    public final boolean g;
    public final boolean h;
    public final int i;
    public final Class<? extends b> j;

    /* loaded from: classes3.dex */
    public enum ColumnType {
        INT,
        LONG,
        FLOAT,
        DOUBLE,
        TEXT,
        BOOL,
        JSON,
        ENUM,
        BLOB,
        JPEG,
        PNG
    }

    public NormColumnInfo(Field field) {
        ColumnType columnType;
        Class type = field.getType();
        boolean z = false;
        boolean z2 = field.isAnnotationPresent(b.c.class) || b.class.isAssignableFrom(type);
        this.f = z2;
        if (z2 && b.class.isAssignableFrom(type)) {
            this.j = type;
        } else {
            this.j = null;
        }
        z = (field.getName().equals(j.c) || field.isAnnotationPresent(b.f.class)) ? true : z;
        this.g = z;
        if (z2) {
            columnType = ColumnType.LONG;
        } else if (type == Integer.class || type == Integer.TYPE) {
            columnType = ColumnType.INT;
        } else if (type == Long.class || type == Long.TYPE) {
            columnType = ColumnType.LONG;
        } else if (type == Float.class || type == Float.TYPE) {
            columnType = ColumnType.FLOAT;
        } else if (type == Double.class || type == Double.TYPE) {
            columnType = ColumnType.DOUBLE;
        } else if (type == String.class) {
            columnType = ColumnType.TEXT;
        } else if (type == Boolean.class || type == Boolean.TYPE) {
            columnType = ColumnType.BOOL;
        } else if (Collection.class.isAssignableFrom(type)) {
            columnType = ColumnType.JSON;
        } else if (Map.class.isAssignableFrom(type)) {
            columnType = ColumnType.JSON;
        } else if (type == byte[].class) {
            columnType = ColumnType.BLOB;
        } else if (type == Bitmap.class) {
            if (field.isAnnotationPresent(b.InterfaceC0101b.class)) {
                columnType = ColumnType.JPEG;
            } else {
                columnType = ColumnType.PNG;
            }
        } else if (Enum.class.isAssignableFrom(type)) {
            columnType = ColumnType.ENUM;
        } else {
            throw new UnsupportedOperationException("Unrecognized column type: " + type.getSimpleName() + " (for column '" + field.getName() + "')");
        }
        if (z && columnType != ColumnType.LONG) {
            throw new IllegalArgumentException("Primary key must be 'long' type.");
        }
        this.d = field.isAnnotationPresent(b.g.class);
        this.h = field.isAnnotationPresent(b.d.class);
        this.e = field.isAnnotationPresent(b.e.class);
        this.a = a.camelCaseToLCUnderscore(field.getName());
        this.b = field;
        this.c = columnType;
        if (field.isAnnotationPresent(b.a.class)) {
            this.i = ((b.a) field.getAnnotation(b.a.class)).a();
        } else {
            this.i = 11;
        }
    }

    public String toString() {
        return this.a;
    }
}
