package com.miui.gallery.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.miui.gallery.R;
import com.miui.gallery.widget.editwrapper.PickAnimationHelper$BackgroundImageViewable;

/* loaded from: classes2.dex */
public class PeoplePageGridItem extends FaceDisplayItemPreferFromThumbnailSource implements Checkable, PickAnimationHelper$BackgroundImageViewable {
    public CheckBox mCheckBox;
    public TextView mName;

    public PeoplePageGridItem(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mCover = (ImageView) findViewById(R.id.face);
        this.mName = (TextView) findViewById(R.id.name);
        this.mCheckBox = (CheckBox) findViewById(16908289);
    }

    public void setName(String str) {
        this.mName.setText(str);
    }

    public void saveIds2Tag(long j, String str) {
        setTag(new IdHolder(j, str));
    }

    public String getName() {
        return this.mName.getText().toString();
    }

    public String getPeopleServerId() {
        return ((IdHolder) getTag()).serverId;
    }

    public long getPeopleLocalId() {
        return ((IdHolder) getTag()).id;
    }

    @Override // com.miui.gallery.ui.Checkable
    public void setCheckable(boolean z) {
        if (z) {
            this.mCheckBox.setVisibility(0);
        } else {
            this.mCheckBox.setVisibility(8);
        }
    }

    @Override // android.widget.Checkable
    public void setChecked(boolean z) {
        this.mCheckBox.setChecked(z);
    }

    @Override // android.widget.Checkable
    public boolean isChecked() {
        return this.mCheckBox.isChecked();
    }

    @Override // android.widget.Checkable
    public void toggle() {
        this.mCheckBox.toggle();
    }

    @Override // com.miui.gallery.widget.editwrapper.PickAnimationHelper$BackgroundImageViewable
    public ImageView getBackgroundImageView() {
        return this.mCover;
    }

    /* loaded from: classes2.dex */
    public static class IdHolder {
        public long id;
        public String serverId;

        public IdHolder(long j, String str) {
            this.id = j;
            this.serverId = str;
        }
    }
}
