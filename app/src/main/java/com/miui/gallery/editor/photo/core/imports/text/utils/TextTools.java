package com.miui.gallery.editor.photo.core.imports.text.utils;

import android.graphics.Typeface;
import androidx.documentfile.provider.DocumentFile;
import com.miui.gallery.editor.photo.core.imports.text.typeface.TextStyle;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.FileHandleRecordHelper;
import java.util.List;
import java.util.Locale;

/* loaded from: classes2.dex */
public class TextTools {
    public static void checkResourceExist(List<TextStyle> list) {
        DocumentFile documentFile;
        if (!BaseMiscUtil.isValid(list)) {
            return;
        }
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("TextTools", "checkResourceExist");
        for (TextStyle textStyle : list) {
            if (textStyle.isLocal()) {
                textStyle.setTypeFace(Typeface.DEFAULT);
                textStyle.setState(17);
            } else if (textStyle.isExtra() && (documentFile = StorageSolutionProvider.get().getDocumentFile(textStyle.getFilePath(), IStoragePermissionStrategy.Permission.QUERY, appendInvokerTag)) != null && documentFile.exists()) {
                textStyle.setTypeFace(Typeface.createFromFile(textStyle.getFilePath()));
                textStyle.setState(17);
            }
        }
    }

    public static boolean isZhCNLanguage() {
        return Locale.getDefault().toString().equals("zh_CN");
    }
}
