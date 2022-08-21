package com.miui.gallery.picker.decor;

import android.database.DataSetObserver;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.miui.gallery.R;
import com.miui.gallery.picker.PickerActivity;
import com.miui.gallery.picker.helper.Picker;
import miui.gallery.support.MiuiSdkCompat;
import miuix.appcompat.app.ActionBar;

/* loaded from: classes2.dex */
public class DefaultDecor extends PickerActivity.Decor {
    public boolean isVisibleBackButton;
    public View mActionBack;
    public Button mCancelButton;
    public Button mDoneButton;
    public View.OnClickListener mMoreButtonClickListener;
    public TextView mPickerTitle;
    public TextView mTitle;

    public DefaultDecor(PickerActivity pickerActivity) {
        super(pickerActivity);
        this.isVisibleBackButton = true;
    }

    @Override // com.miui.gallery.picker.PickerActivity.Decor
    public void applyActionBar() {
        ActionBar appCompatActionBar = this.mActivity.getAppCompatActionBar();
        appCompatActionBar.setDisplayShowCustomEnabled(true);
        int i = 0;
        appCompatActionBar.setTabsMode(false);
        appCompatActionBar.setCustomView(R.layout.picker_album_custom_title);
        appCompatActionBar.setDisplayOptions(appCompatActionBar.getDisplayOptions() & (-9));
        View customView = appCompatActionBar.getCustomView();
        this.mTitle = (TextView) customView.findViewById(16908310);
        this.mPickerTitle = (TextView) customView.findViewById(R.id.picker_title);
        this.mActionBack = customView.findViewById(R.id.action_back);
        this.mCancelButton = (Button) customView.findViewById(16908313);
        this.mDoneButton = (Button) customView.findViewById(16908314);
        updateDoneButtonVisibility();
        Button button = (Button) customView.findViewById(16908315);
        button.setVisibility(4);
        MiuiSdkCompat.setEditActionModeButton(this.mActivity, button, 0);
        View view = this.mActionBack;
        if (!this.isVisibleBackButton) {
            i = 8;
        }
        view.setVisibility(i);
        setup(customView);
    }

    public void setup(View view) {
        MiuiSdkCompat.setEditActionModeButton(this.mActivity, this.mDoneButton, 2);
        MiuiSdkCompat.setEditActionModeButton(this.mActivity, this.mCancelButton, 3);
        Picker picker = this.mActivity.getPicker();
        if (this.mMoreButtonClickListener != null) {
            Button button = (Button) view.findViewById(R.id.more);
            button.setVisibility(0);
            button.setOnClickListener(this.mMoreButtonClickListener);
        }
        this.mActionBack.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.picker.decor.DefaultDecor.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                DefaultDecor.this.mActivity.getPicker().done(3);
            }
        });
        this.mCancelButton.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.picker.decor.DefaultDecor.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                DefaultDecor.this.mActivity.getPicker().cancel();
            }
        });
        this.mDoneButton.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.picker.decor.DefaultDecor.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                DefaultDecor.this.mActivity.getPicker().done(-1);
            }
        });
        picker.registerObserver(new DataSetObserver() { // from class: com.miui.gallery.picker.decor.DefaultDecor.4
            @Override // android.database.DataSetObserver
            public void onChanged() {
                super.onChanged();
                DefaultDecor.this.mActivity.updateTitle();
                DefaultDecor.this.updateDoneButtonVisibility();
            }

            @Override // android.database.DataSetObserver
            public void onInvalidated() {
                super.onInvalidated();
                DefaultDecor.this.mActivity.updateTitle();
                DefaultDecor.this.updateDoneButtonVisibility();
            }
        });
    }

    public final void updateDoneButtonVisibility() {
        this.mDoneButton.setEnabled(this.mActivity.getPicker().count() >= this.mActivity.getPicker().baseline());
    }

    public void setMoreButtonClickListener(View.OnClickListener onClickListener) {
        this.mMoreButtonClickListener = onClickListener;
    }

    @Override // com.miui.gallery.picker.PickerActivity.Decor
    public void applyTheme() {
        this.mActivity.setTheme(2131951999);
    }

    @Override // com.miui.gallery.picker.PickerActivity.Decor
    public void setTitle(CharSequence charSequence) {
        this.mTitle.setText(charSequence);
    }

    @Override // com.miui.gallery.picker.PickerActivity.Decor
    public void setPickerTitle(CharSequence charSequence) {
        this.mPickerTitle.setText(charSequence);
    }
}
