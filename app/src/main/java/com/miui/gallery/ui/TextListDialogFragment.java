package com.miui.gallery.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import androidx.fragment.app.DialogFragment;
import com.miui.gallery.R;
import com.miui.gallery.util.BaseMiscUtil;
import java.util.ArrayList;
import java.util.List;
import miuix.appcompat.app.AlertDialog;
import miuix.internal.util.AnimHelper;

/* loaded from: classes2.dex */
public class TextListDialogFragment extends DialogFragment {
    public AlertDialog mDialog;
    public DialogInterface.OnClickListener mItemClickListener = new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.TextListDialogFragment.1
        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            TextListDialogFragment.this.dismissAllowingStateLoss();
            if (TextListDialogFragment.this.mListener != null) {
                TextListDialogFragment.this.mListener.onItemSelected(TextListDialogFragment.this.mListAdapter.getItem(i), i);
            }
        }
    };
    public TextAdapter mListAdapter;
    public OnItemSelectedListener mListener;
    public boolean mShowCancelBtn;
    public String mTitle;

    /* loaded from: classes2.dex */
    public interface OnItemSelectedListener {
        void onItemSelected(String str, int i);
    }

    public static TextListDialogFragment newInstance(ArrayList<String> arrayList, String str) {
        TextListDialogFragment textListDialogFragment = new TextListDialogFragment();
        Bundle bundle = new Bundle();
        if (BaseMiscUtil.isValid(arrayList)) {
            bundle.putStringArrayList("key_texts", arrayList);
        }
        if (!TextUtils.isEmpty(str)) {
            bundle.putString("key_title", str);
        }
        textListDialogFragment.setArguments(bundle);
        return textListDialogFragment;
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ArrayList<String> stringArrayList = getArguments().getStringArrayList("key_texts");
        this.mTitle = getArguments().getString("key_title");
        this.mShowCancelBtn = getArguments().getBoolean("key_show_cancel", true);
        this.mListAdapter = new TextAdapter(stringArrayList);
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(this.mTitle);
        builder.setAdapter(this.mListAdapter, this.mItemClickListener);
        if (this.mShowCancelBtn) {
            builder.setNegativeButton(17039360, (DialogInterface.OnClickListener) null);
        }
        AlertDialog create = builder.create();
        this.mDialog = create;
        return create;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.mListener = onItemSelectedListener;
    }

    /* loaded from: classes2.dex */
    public static class TextAdapter extends BaseAdapter {
        public List<String> mList;

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return i;
        }

        public TextAdapter(List<String> list) {
            this.mList = null;
            if (list == null) {
                this.mList = new ArrayList();
            } else {
                this.mList = new ArrayList(list);
            }
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return this.mList.size();
        }

        @Override // android.widget.Adapter
        public String getItem(int i) {
            if (i <= -1 || i >= getCount()) {
                return null;
            }
            return this.mList.get(i);
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.text_list_item, viewGroup, false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.title = (TextView) view.findViewById(R.id.title);
                view.setTag(viewHolder);
                AnimHelper.addPressAnimWithBg(view);
            }
            ((ViewHolder) view.getTag()).title.setText(getItem(i));
            return view;
        }

        /* loaded from: classes2.dex */
        public static class ViewHolder {
            public TextView title;

            public ViewHolder() {
            }
        }
    }
}
