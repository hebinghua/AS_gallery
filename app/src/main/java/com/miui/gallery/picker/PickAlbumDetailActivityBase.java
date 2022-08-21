package com.miui.gallery.picker;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import com.miui.gallery.analytics.TimeMonitor;
import com.miui.gallery.picker.PickerActivity;
import com.miui.gallery.picker.albumdetail.IPickAlbumDetail;
import com.miui.gallery.picker.albumdetail.ISelectAllDecor;
import com.miui.gallery.picker.albumdetail.ItemStateListener;
import com.miui.gallery.picker.albumdetail.ShowSortImmersionMenuListener;
import com.miui.gallery.picker.decor.DefaultDecor;
import com.miui.gallery.picker.helper.Picker;
import com.miui.gallery.ui.KeyboardShortcutGroupManager;
import com.miui.gallery.util.LinearMotorHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import miui.gallery.support.MiuiSdkCompat;

/* loaded from: classes2.dex */
public abstract class PickAlbumDetailActivityBase extends PickerActivity {
    public IPickAlbumDetail mAlbumDetailImpl;
    public ISelectAllDecor mISelectAllDecor;
    public boolean mIsAiAlbum;
    public boolean mIsBabyAlbum;
    public ItemStateListener mItemStateListener;
    public PickAlbumDetailKeyboardShortcutCallback mShortcutCallback = new PickAlbumDetailKeyboardShortcutCallback();
    public ShowSortImmersionMenuListener mShowSortMenuListener;

    @Override // com.miui.gallery.picker.PickerActivity, com.miui.gallery.picker.PickerCompatActivity, com.miui.gallery.picker.PickerBaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        Intent intent = getIntent();
        this.mIsBabyAlbum = intent.getBooleanExtra("baby_album", false);
        this.mIsAiAlbum = intent.getBooleanExtra("ai_album", false);
        super.onCreate(bundle);
    }

    @Override // com.miui.gallery.picker.PickerActivity
    public Picker onCreatePicker() {
        Intent intent = getIntent();
        int intExtra = intent.getIntExtra("pick-upper-bound", 1);
        int intExtra2 = intent.getIntExtra("pick-lower-bound", 1);
        Picker.MediaType mediaType = Picker.MediaType.values()[intent.getIntExtra("picker_media_type", 0)];
        Picker.ResultType resultType = Picker.ResultType.values()[intent.getIntExtra("picker_result_type", 0)];
        PickerActivity.SimplePicker simplePicker = new PickerActivity.SimplePicker(this, intExtra, intExtra2, (Set) intent.getSerializableExtra("picker_result_set"));
        simplePicker.setMediaType(mediaType);
        simplePicker.setResultType(resultType);
        if (intent.hasExtra("extra_filter_media_type")) {
            simplePicker.setFilterMimeTypes(intent.getStringArrayExtra("extra_filter_media_type"));
        }
        return simplePicker;
    }

    @Override // com.miui.gallery.picker.PickerActivity
    public PickerActivity.Decor onCreateDecor() {
        if (getPicker().getFromType() == 1014) {
            DefaultDecor defaultDecor = new DefaultDecor(this);
            initMoreButtonClickListenerIfNeed(defaultDecor);
            return defaultDecor;
        } else if (getPicker().getMode() == Picker.Mode.SINGLE) {
            return super.onCreateDecor();
        } else {
            return new SelectAllDecor(this);
        }
    }

    @Override // com.miui.gallery.picker.PickerActivity
    public void onDone(int i) {
        if (getPicker() != null && getPicker().getFromType() == 1014) {
            TimeMonitor.createNewTimeMonitor("403.7.0.1.13792");
        }
        Intent intent = new Intent();
        intent.putExtra("internal_key_updated_selection", PickerActivity.copyPicker(getPicker()));
        setResult(i, intent);
        finish();
    }

    @Override // miuix.appcompat.app.AppCompatActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        getPicker().done(4);
    }

    public final void initMoreButtonClickListenerIfNeed(DefaultDecor defaultDecor) {
        if (this.mIsBabyAlbum || this.mIsAiAlbum) {
            return;
        }
        defaultDecor.setMoreButtonClickListener(new View.OnClickListener() { // from class: com.miui.gallery.picker.PickAlbumDetailActivityBase.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ShowSortImmersionMenuListener showSortImmersionMenuListener = PickAlbumDetailActivityBase.this.mShowSortMenuListener;
                if (showSortImmersionMenuListener != null) {
                    showSortImmersionMenuListener.onShowSortImmersionMenu(view);
                }
            }
        });
    }

    /* loaded from: classes2.dex */
    public static class SelectAllDecor extends DefaultDecor {
        public PickAlbumDetailActivityBase mActivity;
        public boolean mAllSelected;
        public Button mSelectAllButton;

        public SelectAllDecor(PickAlbumDetailActivityBase pickAlbumDetailActivityBase) {
            super(pickAlbumDetailActivityBase);
            this.mActivity = pickAlbumDetailActivityBase;
            pickAlbumDetailActivityBase.initMoreButtonClickListenerIfNeed(this);
        }

        @Override // com.miui.gallery.picker.decor.DefaultDecor
        public void setup(View view) {
            super.setup(view);
            Button button = (Button) view.findViewById(16908315);
            this.mSelectAllButton = button;
            button.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.picker.PickAlbumDetailActivityBase.SelectAllDecor.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    if (Objects.isNull(SelectAllDecor.this.mActivity.mISelectAllDecor)) {
                        return;
                    }
                    LinearMotorHelper.performHapticFeedback(view2, LinearMotorHelper.HAPTIC_MESH_NORMAL);
                    if (SelectAllDecor.this.mAllSelected) {
                        SelectAllDecor.this.mActivity.mISelectAllDecor.deselectAll();
                    } else {
                        SelectAllDecor.this.mActivity.mISelectAllDecor.selectAll();
                    }
                }
            });
            this.mSelectAllButton.setVisibility(0);
            updateSelectAll();
            this.mActivity.mItemStateListener = new ItemStateListener() { // from class: com.miui.gallery.picker.PickAlbumDetailActivityBase.SelectAllDecor.2
                @Override // com.miui.gallery.picker.albumdetail.ItemStateListener
                public void onStateChanged() {
                    SelectAllDecor.this.updateSelectAll();
                }
            };
        }

        public final void updateSelectAll() {
            PickAlbumDetailActivityBase pickAlbumDetailActivityBase = this.mActivity;
            boolean z = pickAlbumDetailActivityBase.mAlbumDetailImpl != null && (pickAlbumDetailActivityBase.mISelectAllDecor.isAllSelected() || (this.mActivity.getPicker().isFull() && !this.mActivity.mISelectAllDecor.isNoneSelected()));
            this.mAllSelected = z;
            PickAlbumDetailActivityBase pickAlbumDetailActivityBase2 = this.mActivity;
            Button button = this.mSelectAllButton;
            int i = z ? 1 : 0;
            int i2 = z ? 1 : 0;
            MiuiSdkCompat.setEditActionModeButton(pickAlbumDetailActivityBase2, button, i);
        }
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> list, Menu menu, int i) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(KeyboardShortcutGroupManager.getInstance().getSelectAllShortcutInfo());
        list.add(new KeyboardShortcutGroup("picker", arrayList));
        super.onProvideKeyboardShortcuts(list, menu, i);
    }

    @Override // android.app.Activity
    public boolean onKeyShortcut(int i, KeyEvent keyEvent) {
        if (KeyboardShortcutGroupManager.getInstance().onKeyShortcut(i, keyEvent, this.mShortcutCallback)) {
            return true;
        }
        return super.onKeyShortcut(i, keyEvent);
    }

    /* loaded from: classes2.dex */
    public class PickAlbumDetailKeyboardShortcutCallback implements KeyboardShortcutGroupManager.OnKeyShortcutCallback {
        public PickAlbumDetailKeyboardShortcutCallback() {
        }

        @Override // com.miui.gallery.ui.KeyboardShortcutGroupManager.OnKeyShortcutCallback
        public boolean onSelectAllPressed() {
            PickAlbumDetailActivityBase.this.mISelectAllDecor.selectAll();
            return true;
        }
    }
}
