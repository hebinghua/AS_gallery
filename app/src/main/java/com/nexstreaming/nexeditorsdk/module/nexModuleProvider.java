package com.nexstreaming.nexeditorsdk.module;

/* loaded from: classes3.dex */
public interface nexModuleProvider {
    String auth();

    String description();

    String format();

    String name();

    UserField[] userFields();

    String uuid();

    int version();
}
