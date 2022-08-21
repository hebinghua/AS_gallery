package com.miui.gallery.editor.photo.app;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.miui.gallery.R;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.GalleryDialogFragment;
import java.util.List;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes2.dex */
public class SimpleSelectDialog extends GalleryDialogFragment {
    public AlertDialog mDialog;
    public OnOperationSelectedListener mOnOperationSelectedListener;
    public List<SelectModel> mSelectModels;

    /* loaded from: classes2.dex */
    public interface OnOperationSelectedListener {
        boolean onOperationSelected(int i);
    }

    /* loaded from: classes2.dex */
    public static class SelectModel {
        public int creationId;
        public int iconResourceId;
        public String informationString;
        public boolean isRemainWhenClick;
        public boolean showIcon;
        public int titleResourceId;
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setAdapter(new SimpleOptionAdapter(this.mSelectModels), null);
        AlertDialog create = builder.create();
        this.mDialog = create;
        return create;
    }

    /* loaded from: classes2.dex */
    public class SimpleOptionAdapter extends BaseAdapter implements View.OnClickListener {
        public List<SelectModel> mCreationModelList;

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return i;
        }

        public SimpleOptionAdapter(List<SelectModel> list) {
            this.mCreationModelList = list;
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return this.mCreationModelList.size();
        }

        @Override // android.widget.Adapter
        /* renamed from: getItem */
        public SelectModel mo744getItem(int i) {
            if (i < 0 || i >= this.mCreationModelList.size()) {
                return null;
            }
            return this.mCreationModelList.get(i);
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.simple_select_dialog_item, viewGroup, false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.icon = (ImageView) view.findViewById(R.id.creation_icon);
                viewHolder.title = (TextView) view.findViewById(R.id.creation_text);
                viewHolder.information = (TextView) view.findViewById(R.id.information);
                view.setTag(viewHolder);
                view.setOnClickListener(this);
            }
            ((ViewHolder) view.getTag()).bindView(mo744getItem(i), i);
            return view;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            int i = ((ViewHolder) view.getTag()).position;
            if (i >= SimpleSelectDialog.this.mSelectModels.size() || SimpleSelectDialog.this.mOnOperationSelectedListener == null) {
                return;
            }
            DefaultLogger.d("SimpleSelectDialog", "Creation select : %d", Integer.valueOf(i));
            SelectModel selectModel = (SelectModel) SimpleSelectDialog.this.mSelectModels.get(i);
            if (selectModel.isRemainWhenClick || !SimpleSelectDialog.this.mOnOperationSelectedListener.onOperationSelected(selectModel.creationId)) {
                return;
            }
            SimpleSelectDialog.this.dismissSafely();
        }

        /* loaded from: classes2.dex */
        public class ViewHolder {
            public ImageView icon;
            public TextView information;
            public int position;
            public TextView title;

            public ViewHolder() {
            }

            public void bindView(SelectModel selectModel, int i) {
                if (selectModel.showIcon) {
                    this.icon.setImageResource(selectModel.iconResourceId);
                }
                this.title.setText(selectModel.titleResourceId);
                if (!TextUtils.isEmpty(selectModel.informationString)) {
                    this.information.setVisibility(0);
                    this.information.setText(selectModel.informationString);
                } else {
                    this.information.setVisibility(8);
                    this.information.setText("");
                }
                this.position = i;
            }
        }
    }
}
