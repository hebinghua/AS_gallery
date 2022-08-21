package miuix.appcompat.internal.view.menu;

import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

/* loaded from: classes3.dex */
public interface ImmersionMenuPopupWindow {
    void dismiss(boolean z);

    boolean isShowing();

    void show(View view, ViewGroup viewGroup);

    void update(Menu menu);
}
