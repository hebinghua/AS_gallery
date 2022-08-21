package com.miui.gallery.util.recorder;

/* loaded from: classes2.dex */
public interface IRecorder {
    boolean clearExpiredRecords();

    boolean isObserveAccountChanged();

    void onAddAccount(String str);

    void onDeleteAccount(String str);
}
