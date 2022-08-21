package androidx.documentfile.provider;

import android.os.Bundle;
import android.text.TextUtils;
import com.miui.gallery.util.FileHandleRecordHelper;

/* loaded from: classes.dex */
public class DocumentFileHandleRecordWrapper extends DocumentFileWrapper {
    public final String mFilePath;
    public String mInvoker;

    public static DocumentFileHandleRecordWrapper wrap(DocumentFile documentFile, Bundle bundle, String str) {
        return new DocumentFileHandleRecordWrapper(documentFile, bundle, str);
    }

    public DocumentFileHandleRecordWrapper(DocumentFile documentFile, Bundle bundle, String str) {
        super(documentFile);
        if (bundle != null) {
            this.mInvoker = bundle.getString("invoker");
        }
        this.mFilePath = str;
    }

    @Override // androidx.documentfile.provider.DocumentFileWrapper, androidx.documentfile.provider.DocumentFile
    public boolean delete() {
        boolean delete = super.delete();
        if (delete && !TextUtils.isEmpty(this.mInvoker)) {
            try {
                FileHandleRecordHelper.recordFileHandle(this.mFilePath, "", 3, this.mInvoker);
            } catch (Exception unused) {
            }
        }
        return delete;
    }
}
