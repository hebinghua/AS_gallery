package com.miui.gallery.ui;

import android.content.Context;
import android.view.KeyEvent;
import android.view.KeyboardShortcutInfo;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class KeyboardShortcutGroupManager {
    public KeyboardShortcutInfo mCopyInfo;
    public KeyboardShortcutInfo mCutInfo;
    public KeyboardShortcutInfo mDay;
    public List<KeyboardShortcutInfo> mDeleteInfo;
    public KeyboardShortcutInfo mMonthCompact;
    public KeyboardShortcutInfo mMonthLoose;
    public KeyboardShortcutInfo mPasteInfo;
    public KeyboardShortcutInfo mSearchInfo;
    public KeyboardShortcutInfo mSelectAllInfo;
    public KeyboardShortcutInfo mYear;

    /* loaded from: classes2.dex */
    public interface OnKeyShortcutCallback {
        default boolean onCopyPressed() {
            return false;
        }

        default boolean onCutPressed() {
            return false;
        }

        default boolean onDayModePressed() {
            return false;
        }

        default boolean onDeletePressed() {
            return false;
        }

        default boolean onMonthCompactModePressed() {
            return false;
        }

        default boolean onMonthLooseModePressed() {
            return false;
        }

        default boolean onPastePressed() {
            return false;
        }

        default boolean onSearchPressed() {
            return false;
        }

        default boolean onSelectAllPressed() {
            return false;
        }

        default boolean onYearModePressed() {
            return false;
        }
    }

    public KeyboardShortcutGroupManager() {
        Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
        this.mCopyInfo = new KeyboardShortcutInfo(sGetAndroidContext.getResources().getString(R.string.keyboard_shortcut_copy), 31, 4096);
        this.mCutInfo = new KeyboardShortcutInfo(sGetAndroidContext.getResources().getString(R.string.keyboard_shortcut_cut), 52, 4096);
        this.mSelectAllInfo = new KeyboardShortcutInfo(sGetAndroidContext.getResources().getString(R.string.keyboard_shortcut_select_all), 29, 4096);
        this.mDeleteInfo = new ArrayList(2);
        String string = sGetAndroidContext.getResources().getString(R.string.keyboard_shortcut_delete);
        this.mDeleteInfo.add(new KeyboardShortcutInfo(string, 32, 4096));
        this.mDeleteInfo.add(new KeyboardShortcutInfo(string, 67, 4096));
        this.mPasteInfo = new KeyboardShortcutInfo(sGetAndroidContext.getResources().getString(R.string.keyboard_shortcut_paste), 50, 4096);
        this.mSearchInfo = new KeyboardShortcutInfo(sGetAndroidContext.getResources().getString(R.string.keyboard_shortcut_search), 34, 4096);
        this.mYear = new KeyboardShortcutInfo(sGetAndroidContext.getResources().getString(R.string.keyboard_shortcut_year), 8, 4096);
        this.mMonthLoose = new KeyboardShortcutInfo(sGetAndroidContext.getResources().getString(R.string.keyboard_shortcut_month), 10, 4096);
        this.mMonthCompact = new KeyboardShortcutInfo(sGetAndroidContext.getResources().getString(R.string.keyboard_shortcut_month), 9, 4096);
        this.mDay = new KeyboardShortcutInfo(sGetAndroidContext.getResources().getString(R.string.keyboard_shortcut_day), 11, 4096);
    }

    /* loaded from: classes2.dex */
    public static final class KeyboardShortcutGroupManagerHolder {
        public static final KeyboardShortcutGroupManager S_INSTANCE = new KeyboardShortcutGroupManager();
    }

    public static synchronized KeyboardShortcutGroupManager getInstance() {
        KeyboardShortcutGroupManager keyboardShortcutGroupManager;
        synchronized (KeyboardShortcutGroupManager.class) {
            keyboardShortcutGroupManager = KeyboardShortcutGroupManagerHolder.S_INSTANCE;
        }
        return keyboardShortcutGroupManager;
    }

    public KeyboardShortcutInfo getCopyShortcutInfo() {
        return this.mCopyInfo;
    }

    public KeyboardShortcutInfo getCutShortcutInfo() {
        return this.mCutInfo;
    }

    public KeyboardShortcutInfo getSelectAllShortcutInfo() {
        return this.mSelectAllInfo;
    }

    public List<KeyboardShortcutInfo> getDeleteShortcutInfo() {
        return this.mDeleteInfo;
    }

    public KeyboardShortcutInfo getPasteShortcutInfo() {
        return this.mPasteInfo;
    }

    public KeyboardShortcutInfo getSearchShortcutInfo() {
        return this.mSearchInfo;
    }

    public KeyboardShortcutInfo getYearShortcutInfo() {
        return this.mYear;
    }

    public KeyboardShortcutInfo getMonthCompactShortcutInfo() {
        return this.mMonthCompact;
    }

    public KeyboardShortcutInfo getMonthLooseShortcutInfo() {
        return this.mMonthLoose;
    }

    public KeyboardShortcutInfo getDayShortcutInfo() {
        return this.mDay;
    }

    public boolean onKeyShortcut(int i, KeyEvent keyEvent, OnKeyShortcutCallback onKeyShortcutCallback) {
        if (keyEvent.hasModifiers(4096)) {
            if (i == 29) {
                return onKeyShortcutCallback.onSelectAllPressed();
            }
            if (i == 34) {
                return onKeyShortcutCallback.onSearchPressed();
            }
            if (i == 50) {
                return onKeyShortcutCallback.onPastePressed();
            }
            if (i == 52) {
                return onKeyShortcutCallback.onCutPressed();
            }
            if (i != 67) {
                if (i == 31) {
                    return onKeyShortcutCallback.onCopyPressed();
                }
                if (i != 32) {
                    switch (i) {
                        case 8:
                            return onKeyShortcutCallback.onYearModePressed();
                        case 9:
                            return onKeyShortcutCallback.onMonthCompactModePressed();
                        case 10:
                            return onKeyShortcutCallback.onMonthLooseModePressed();
                        case 11:
                            return onKeyShortcutCallback.onDayModePressed();
                        default:
                            return false;
                    }
                }
            }
            return onKeyShortcutCallback.onDeletePressed();
        }
        return false;
    }
}
