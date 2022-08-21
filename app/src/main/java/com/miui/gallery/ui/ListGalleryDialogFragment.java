package com.miui.gallery.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.miui.gallery.R;
import com.miui.gallery.widget.GalleryDialogFragment;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import miuix.appcompat.app.AlertDialog;
import miuix.internal.util.AnimHelper;

/* loaded from: classes2.dex */
public class ListGalleryDialogFragment extends GalleryDialogFragment {
    public InternalListAdapter mAdapter;
    public ArrayList<ItemData> mDatas;
    public OnOperationSelectedListener mOnOperationSelectedListener;
    public int mTitleRes;

    /* loaded from: classes2.dex */
    public interface OnOperationSelectedListener {
        boolean onOperationSelected(int i);
    }

    /* loaded from: classes2.dex */
    public static class ItemData implements Parcelable {
        public static final Parcelable.Creator<ItemData> CREATOR = new Parcelable.Creator<ItemData>() { // from class: com.miui.gallery.ui.ListGalleryDialogFragment.ItemData.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: createFromParcel */
            public ItemData mo1506createFromParcel(Parcel parcel) {
                return new ItemData(parcel);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: newArray */
            public ItemData[] mo1507newArray(int i) {
                return new ItemData[i];
            }
        };
        public boolean enable;
        public int titleId;
        public int titleResourceId;

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public ItemData(int i, int i2) {
            this.enable = true;
            this.titleId = i;
            this.titleResourceId = i2;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(this.titleId);
            parcel.writeInt(this.titleResourceId);
            parcel.writeByte(this.enable ? (byte) 1 : (byte) 0);
        }

        public ItemData(Parcel parcel) {
            boolean z = true;
            this.enable = true;
            this.titleId = parcel.readInt();
            this.titleResourceId = parcel.readInt();
            this.enable = parcel.readByte() == 0 ? false : z;
        }
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null && bundle.containsKey("datas")) {
            this.mDatas = bundle.getParcelableArrayList("datas");
        }
        this.mAdapter = new InternalListAdapter(this.mDatas);
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        int i = this.mTitleRes;
        if (-1 != i && i != 0) {
            builder.setTitle(i);
        }
        builder.setAdapter(this.mAdapter, null);
        return builder.create();
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putParcelableArrayList("datas", this.mDatas);
    }

    public void setDatas(ArrayList<ItemData> arrayList) {
        this.mDatas = arrayList;
    }

    /* loaded from: classes2.dex */
    public class InternalListAdapter extends BaseAdapter implements View.OnClickListener {
        public int mDataSize;
        public List<ItemData> mList;

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return i;
        }

        public InternalListAdapter(List<ItemData> list) {
            this.mList = list;
            this.mDataSize = list.size();
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return this.mList.size();
        }

        @Override // android.widget.Adapter
        /* renamed from: getItem */
        public ItemData mo1505getItem(int i) {
            if (i < 0 || i >= this.mList.size()) {
                return null;
            }
            return this.mList.get(i);
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.simple_list_textview_item, viewGroup, false);
                int paddingStart = view.getPaddingStart();
                int paddingEnd = view.getPaddingEnd();
                int dimensionPixelOffset = viewGroup.getContext().getResources().getDimensionPixelOffset(R.dimen.list_item_dialog_padding_small);
                int dimensionPixelOffset2 = viewGroup.getContext().getResources().getDimensionPixelOffset(R.dimen.list_item_dialog_padding_large);
                if (i != this.mDataSize - 1) {
                    dimensionPixelOffset2 = dimensionPixelOffset;
                }
                view.setPaddingRelative(paddingStart, dimensionPixelOffset, paddingEnd, dimensionPixelOffset2);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.title = (TextView) view.findViewById(16908308);
                view.setTag(viewHolder);
                view.setOnClickListener(this);
                AnimHelper.addPressAnimWithBg(view);
            }
            ((ViewHolder) view.getTag()).bindView(mo1505getItem(i), i);
            return view;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            int i = ((ViewHolder) view.getTag()).position;
            if (i >= ListGalleryDialogFragment.this.mDatas.size() || ListGalleryDialogFragment.this.mOnOperationSelectedListener == null || !ListGalleryDialogFragment.this.mOnOperationSelectedListener.onOperationSelected(((ItemData) ListGalleryDialogFragment.this.mDatas.get(i)).titleId)) {
                return;
            }
            ListGalleryDialogFragment.this.dismissSafely();
        }

        /* loaded from: classes2.dex */
        public class ViewHolder {
            public int position;
            public TextView title;

            public ViewHolder() {
            }

            public void bindView(ItemData itemData, int i) {
                Context context;
                int i2;
                this.title.setText(itemData.titleResourceId);
                this.position = i;
                TextView textView = this.title;
                if (itemData.enable) {
                    context = textView.getContext();
                    i2 = R.color.action_button_text_color_normal_light;
                } else {
                    context = textView.getContext();
                    i2 = R.color.action_button_text_color_disable_light;
                }
                textView.setTextColor(context.getColor(i2));
                this.title.setEnabled(itemData.enable);
            }
        }
    }

    public void setOnOperationSelectedListener(OnOperationSelectedListener onOperationSelectedListener) {
        this.mOnOperationSelectedListener = onOperationSelectedListener;
    }

    public void setItemEnable(int i, boolean z) {
        Iterator<ItemData> it = this.mDatas.iterator();
        while (it.hasNext()) {
            ItemData next = it.next();
            if (next.titleId == i && next.enable != z) {
                next.enable = z;
            }
        }
    }
}
