package miuix.appcompat.internal.view;

import android.content.Context;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import java.lang.ref.WeakReference;
import miuix.appcompat.internal.app.widget.ActionModeView;
import miuix.appcompat.internal.view.menu.MenuBuilder;
import miuix.view.ActionModeAnimationListener;

/* loaded from: classes3.dex */
public class ActionModeImpl extends ActionMode implements MenuBuilder.Callback, ActionModeAnimationListener {
    public ActionModeCallback mActionModeCallback;
    public WeakReference<ActionModeView> mActionModeView;
    public ActionMode.Callback mCallback;
    public Context mContext;
    public boolean mFinishing = false;
    public MenuBuilder mMenu;

    /* loaded from: classes3.dex */
    public interface ActionModeCallback {
        void onActionModeFinish(ActionMode actionMode);
    }

    @Override // miuix.view.ActionModeAnimationListener
    public void onStart(boolean z) {
    }

    @Override // miuix.view.ActionModeAnimationListener
    public void onUpdate(boolean z, float f) {
    }

    public ActionModeImpl(Context context, ActionMode.Callback callback) {
        this.mContext = context;
        this.mCallback = callback;
        MenuBuilder defaultShowAsAction = new MenuBuilder(context).setDefaultShowAsAction(1);
        this.mMenu = defaultShowAsAction;
        defaultShowAsAction.setCallback(this);
    }

    public void setActionModeCallback(ActionModeCallback actionModeCallback) {
        this.mActionModeCallback = actionModeCallback;
    }

    public void setActionModeView(ActionModeView actionModeView) {
        actionModeView.addAnimationListener(this);
        this.mActionModeView = new WeakReference<>(actionModeView);
    }

    public boolean dispatchOnCreate() {
        this.mMenu.stopDispatchingItemsChanged();
        try {
            return this.mCallback.onCreateActionMode(this, this.mMenu);
        } finally {
            this.mMenu.startDispatchingItemsChanged();
        }
    }

    @Override // android.view.ActionMode
    public void setTitle(CharSequence charSequence) {
        throw new UnsupportedOperationException("setTitle not supported");
    }

    @Override // android.view.ActionMode
    public void setSubtitle(CharSequence charSequence) {
        throw new UnsupportedOperationException("setSubTitle not supported");
    }

    @Override // android.view.ActionMode
    public void invalidate() {
        this.mMenu.stopDispatchingItemsChanged();
        try {
            this.mCallback.onPrepareActionMode(this, this.mMenu);
        } finally {
            this.mMenu.startDispatchingItemsChanged();
        }
    }

    @Override // android.view.ActionMode
    public void finish() {
        if (this.mFinishing) {
            return;
        }
        this.mFinishing = true;
        this.mActionModeView.get().closeMode();
        ActionModeCallback actionModeCallback = this.mActionModeCallback;
        if (actionModeCallback != null) {
            actionModeCallback.onActionModeFinish(this);
        }
        ActionMode.Callback callback = this.mCallback;
        if (callback == null) {
            return;
        }
        callback.onDestroyActionMode(this);
        this.mCallback = null;
    }

    @Override // android.view.ActionMode
    public Menu getMenu() {
        return this.mMenu;
    }

    @Override // android.view.ActionMode
    public CharSequence getTitle() {
        throw new UnsupportedOperationException("getTitle not supported");
    }

    @Override // android.view.ActionMode
    public void setTitle(int i) {
        throw new UnsupportedOperationException("setTitle not supported");
    }

    @Override // android.view.ActionMode
    public CharSequence getSubtitle() {
        throw new UnsupportedOperationException("getSubtitle not supported");
    }

    @Override // android.view.ActionMode
    public void setSubtitle(int i) {
        throw new UnsupportedOperationException("setSubTitle not supported");
    }

    @Override // android.view.ActionMode
    public View getCustomView() {
        throw new UnsupportedOperationException("getCustomView not supported");
    }

    @Override // android.view.ActionMode
    public void setCustomView(View view) {
        throw new UnsupportedOperationException("setCustomView not supported");
    }

    @Override // android.view.ActionMode
    public MenuInflater getMenuInflater() {
        return new MenuInflater(this.mContext);
    }

    @Override // miuix.appcompat.internal.view.menu.MenuBuilder.Callback
    public boolean onMenuItemSelected(MenuBuilder menuBuilder, MenuItem menuItem) {
        ActionMode.Callback callback = this.mCallback;
        return callback != null && callback.onActionItemClicked(this, menuItem);
    }

    @Override // miuix.appcompat.internal.view.menu.MenuBuilder.Callback
    public void onMenuModeChange(MenuBuilder menuBuilder) {
        if (this.mCallback == null) {
            return;
        }
        invalidate();
    }

    @Override // miuix.view.ActionModeAnimationListener
    public void onStop(boolean z) {
        ActionMode.Callback callback;
        if (z || (callback = this.mCallback) == null) {
            return;
        }
        callback.onDestroyActionMode(this);
        this.mCallback = null;
    }
}
