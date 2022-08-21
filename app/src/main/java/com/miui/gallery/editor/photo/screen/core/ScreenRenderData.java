package com.miui.gallery.editor.photo.screen.core;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import com.miui.gallery.editor.photo.core.RenderData;
import com.miui.gallery.editor.photo.core.imports.longcrop.LongScreenshotCropEditorView;
import com.miui.gallery.editor.photo.screen.crop.ScreenCropEntry;
import com.miui.gallery.editor.photo.screen.shell.ScreenShellEntry;
import java.util.HashMap;

@SuppressLint({"ParcelCreator"})
/* loaded from: classes2.dex */
public class ScreenRenderData extends RenderData {
    public ScreenDrawEntry mDrawEntry;
    public LongScreenshotCropEditorView.Entry mLongCropEntry;
    public ScreenCropEntry mScreenCropEntry;
    public ScreenShellEntry mScreenShellEntry;

    public ScreenRenderData(ScreenDrawEntry screenDrawEntry, ScreenCropEntry screenCropEntry, ScreenShellEntry screenShellEntry) {
        this.mDrawEntry = screenDrawEntry;
        this.mScreenCropEntry = screenCropEntry;
        this.mScreenShellEntry = screenShellEntry;
    }

    public Bitmap apply(Bitmap bitmap) {
        Bitmap apply = this.mDrawEntry.apply(bitmap);
        ScreenShellEntry screenShellEntry = this.mScreenShellEntry;
        if (screenShellEntry != null && screenShellEntry.getShellInfo() != null) {
            return this.mScreenShellEntry.apply(apply);
        }
        ScreenCropEntry screenCropEntry = this.mScreenCropEntry;
        if (screenCropEntry != null) {
            return screenCropEntry.apply(apply);
        }
        LongScreenshotCropEditorView.Entry entry = this.mLongCropEntry;
        return entry != null ? entry.apply(apply) : apply;
    }

    public void setLongCropEntry(LongScreenshotCropEditorView.Entry entry) {
        this.mLongCropEntry = entry;
    }

    public void putStat(HashMap<String, String> hashMap) {
        String str = "false";
        hashMap.put("cropChanged", this.mScreenCropEntry == null ? str : "true");
        LongScreenshotCropEditorView.Entry entry = this.mLongCropEntry;
        if (entry != null) {
            if (entry.isModified()) {
                str = "true";
            }
            hashMap.put("longCropChanged", str);
        }
        this.mDrawEntry.putStat(hashMap);
    }
}
