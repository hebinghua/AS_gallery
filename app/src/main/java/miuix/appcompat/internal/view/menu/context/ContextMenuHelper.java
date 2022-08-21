package miuix.appcompat.internal.view.menu.context;

import android.view.ContextMenu;
import android.view.View;
import android.view.ViewParent;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import miuix.reflect.Reflects;

/* loaded from: classes3.dex */
public class ContextMenuHelper {
    public static final Field mOnCreateContextMenuListener = Reflects.getDeclaredField("android.view.View$ListenerInfo", "mOnCreateContextMenuListener");
    public static final Method getContextMenuInfo = Reflects.getDeclaredMethod(View.class, "getContextMenuInfo", new Class[0]);
    public static final Method onCreateContextMenu = Reflects.getDeclaredMethod(View.class, "onCreateContextMenu", ContextMenu.class);
    public static final Field mListenerInfo = Reflects.getDeclaredField(View.class, "mListenerInfo");

    public static void createContextMenu(View view, ContextMenuBuilder contextMenuBuilder) {
        View.OnCreateContextMenuListener onCreateContextMenuListener;
        ContextMenu.ContextMenuInfo contextMenuInfo = (ContextMenu.ContextMenuInfo) Reflects.invoke(view, getContextMenuInfo, new Object[0]);
        contextMenuBuilder.setCurrentMenuInfo(contextMenuInfo);
        Reflects.invoke(view, onCreateContextMenu, contextMenuBuilder);
        Object obj = Reflects.get(view, mListenerInfo);
        if (obj != null && (onCreateContextMenuListener = (View.OnCreateContextMenuListener) Reflects.get(obj, mOnCreateContextMenuListener)) != null) {
            onCreateContextMenuListener.onCreateContextMenu(contextMenuBuilder, view, contextMenuInfo);
        }
        contextMenuBuilder.setCurrentMenuInfo(null);
        ViewParent parent = view.getParent();
        if (parent instanceof View) {
            createContextMenu((View) parent, contextMenuBuilder);
        }
    }
}
