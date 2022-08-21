package com.miui.gallery.ui;

import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class KeyboardShortcutsCopyHelper {
    public ArrayList<Long> mCopyIdList;
    public boolean mDeleteOrigin;

    public KeyboardShortcutsCopyHelper() {
        this.mCopyIdList = new ArrayList<>();
    }

    /* loaded from: classes2.dex */
    public static final class KeyboardShortcutsCopyHelperHolder {
        public static final KeyboardShortcutsCopyHelper S_INSTANCE = new KeyboardShortcutsCopyHelper();
    }

    public static synchronized KeyboardShortcutsCopyHelper getInstance() {
        KeyboardShortcutsCopyHelper keyboardShortcutsCopyHelper;
        synchronized (KeyboardShortcutsCopyHelper.class) {
            keyboardShortcutsCopyHelper = KeyboardShortcutsCopyHelperHolder.S_INSTANCE;
        }
        return keyboardShortcutsCopyHelper;
    }

    public synchronized void setCopyList(List<Long> list, boolean z) {
        this.mCopyIdList.clear();
        this.mCopyIdList.addAll(list);
        this.mDeleteOrigin = z;
    }

    public synchronized ArrayList<Long> getCopyList() {
        return new ArrayList<>(this.mCopyIdList);
    }

    public boolean deleteOrigin() {
        return this.mDeleteOrigin;
    }

    public synchronized boolean isCopyListEmpty() {
        return this.mCopyIdList.isEmpty();
    }
}
