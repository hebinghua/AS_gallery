package miuix.appcompat.internal.view;

import android.content.Context;
import android.graphics.Rect;
import android.view.ActionMode;
import java.lang.ref.WeakReference;
import miuix.appcompat.internal.app.widget.ActionModeView;
import miuix.appcompat.internal.app.widget.SearchActionModeView;
import miuix.view.SearchActionMode;

/* loaded from: classes3.dex */
public class SearchActionModeImpl extends ActionModeImpl implements SearchActionMode {
    public SearchActionModeImpl(Context context, ActionMode.Callback callback) {
        super(context, callback);
    }

    public void setPendingInsets(Rect rect) {
        WeakReference<ActionModeView> weakReference = this.mActionModeView;
        SearchActionModeView searchActionModeView = weakReference != null ? (SearchActionModeView) weakReference.get() : null;
        if (searchActionModeView != null) {
            searchActionModeView.rePaddingAndRelayout(rect);
        }
    }
}
