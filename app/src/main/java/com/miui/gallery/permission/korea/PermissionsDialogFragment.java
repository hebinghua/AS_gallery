package com.miui.gallery.permission.korea;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.BaseTermsDialogFragment;
import com.miui.gallery.agreement.core.OnAgreementInvokedListener;
import com.miui.gallery.baseui.R$drawable;
import com.miui.gallery.permission.R$dimen;
import com.miui.gallery.permission.R$id;
import com.miui.gallery.permission.R$layout;
import com.miui.gallery.permission.R$string;
import com.miui.gallery.permission.core.OnPermissionIntroduced;
import com.miui.gallery.permission.core.Permission;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes2.dex */
public class PermissionsDialogFragment extends BaseTermsDialogFragment implements OnAgreementInvokedListener {
    public PermissionAdapter mAdapter;
    public OnPermissionIntroduced mOnIntroducedListener;

    @Override // com.miui.gallery.BaseTermsDialogFragment
    public CharSequence getNegativeButtonText() {
        return null;
    }

    public static PermissionsDialogFragment newInstance(ArrayList<Permission> arrayList, OnPermissionIntroduced onPermissionIntroduced) {
        PermissionsDialogFragment permissionsDialogFragment = new PermissionsDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("extra_permissions", arrayList);
        permissionsDialogFragment.setArguments(bundle);
        permissionsDialogFragment.setOnIntroducedListener(onPermissionIntroduced);
        permissionsDialogFragment.setCancelable(false);
        return permissionsDialogFragment;
    }

    @Override // com.miui.gallery.BaseTermsDialogFragment, androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setOnAgreementListener(this);
    }

    @Override // com.miui.gallery.BaseTermsDialogFragment
    public void configList(View view) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R$id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        PermissionAdapter permissionAdapter = new PermissionAdapter();
        this.mAdapter = permissionAdapter;
        recyclerView.setAdapter(permissionAdapter);
        recyclerView.addItemDecoration(new DividerDecoration());
        this.mAdapter.setPermissions(getArguments().getParcelableArrayList("extra_permissions"));
    }

    public void setOnAgreementListener(OnAgreementInvokedListener onAgreementInvokedListener) {
        this.mAgreementListener = onAgreementInvokedListener;
    }

    @Override // com.miui.gallery.BaseTermsDialogFragment
    public CharSequence getSummary() {
        return getContext().getResources().getString(R$string.permission_use_desc);
    }

    @Override // com.miui.gallery.BaseTermsDialogFragment
    public CharSequence getPositiveButtonText() {
        return getContext().getResources().getString(com.miui.gallery.baseui.R$string.have_known);
    }

    public void invoke(FragmentActivity fragmentActivity) {
        Fragment findFragmentByTag = fragmentActivity.getSupportFragmentManager().findFragmentByTag("PermissionsDialogFragment");
        if (findFragmentByTag != this && (findFragmentByTag instanceof PermissionsDialogFragment)) {
            ((DialogFragment) findFragmentByTag).dismiss();
        }
        show(fragmentActivity.getSupportFragmentManager(), "PermissionsDialogFragment");
    }

    @Override // com.miui.gallery.agreement.core.OnAgreementInvokedListener
    public void onAgreementInvoked(boolean z) {
        OnPermissionIntroduced onPermissionIntroduced = this.mOnIntroducedListener;
        if (onPermissionIntroduced != null) {
            onPermissionIntroduced.onPermissionIntroduced(z);
        }
    }

    public void setOnIntroducedListener(OnPermissionIntroduced onPermissionIntroduced) {
        this.mOnIntroducedListener = onPermissionIntroduced;
    }

    /* loaded from: classes2.dex */
    public static class PermissionAdapter extends RecyclerView.Adapter<BaseViewHolder> {
        public ArrayList<PermissionWrapper> mPermissions;

        public PermissionAdapter() {
            this.mPermissions = new ArrayList<>();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* renamed from: onCreateViewHolder  reason: collision with other method in class */
        public BaseViewHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
            if (i == 1) {
                return new CategoryViewHolder(BaseViewHolder.getView(viewGroup, R$layout.user_permission_category));
            }
            return new PermissionViewHolder(BaseViewHolder.getView(viewGroup, R$layout.user_permission_item));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(BaseViewHolder baseViewHolder, int i) {
            baseViewHolder.bindPermission(this.mPermissions.get(i));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return this.mPermissions.size();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            return this.mPermissions.get(i).mIsCategory ? 1 : 0;
        }

        public void setPermissions(List<Permission> list) {
            this.mPermissions.clear();
            if (list != null) {
                LinkedList linkedList = new LinkedList();
                for (Permission permission : list) {
                    if (!TextUtils.isEmpty(permission.mName)) {
                        if (permission.mRequired) {
                            this.mPermissions.add(new PermissionWrapper(permission, false));
                        } else {
                            linkedList.add(new PermissionWrapper(permission, false));
                        }
                    }
                }
                if (!this.mPermissions.isEmpty()) {
                    this.mPermissions.add(0, new PermissionWrapper(new Permission("", "", "", true), true));
                }
                if (!linkedList.isEmpty()) {
                    this.mPermissions.add(new PermissionWrapper(new Permission("", "", "", false), true));
                    this.mPermissions.addAll(linkedList);
                }
            }
            notifyDataSetChanged();
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class BaseViewHolder extends RecyclerView.ViewHolder {
        public abstract void bindPermission(PermissionWrapper permissionWrapper);

        public BaseViewHolder(View view) {
            super(view);
        }

        public static View getView(ViewGroup viewGroup, int i) {
            return LayoutInflater.from(viewGroup.getContext()).inflate(i, viewGroup, false);
        }
    }

    /* loaded from: classes2.dex */
    public static class PermissionViewHolder extends BaseViewHolder {
        public TextView mDesc;
        public TextView mName;

        public PermissionViewHolder(View view) {
            super(view);
            this.mName = (TextView) view.findViewById(R$id.title);
            this.mDesc = (TextView) view.findViewById(R$id.desc);
        }

        @Override // com.miui.gallery.permission.korea.PermissionsDialogFragment.BaseViewHolder
        public void bindPermission(PermissionWrapper permissionWrapper) {
            this.mName.setText(permissionWrapper.mName);
            this.mDesc.setText(permissionWrapper.mDesc);
        }
    }

    /* loaded from: classes2.dex */
    public static class CategoryViewHolder extends BaseViewHolder {
        public TextView mCategory;

        public CategoryViewHolder(View view) {
            super(view);
            this.mCategory = (TextView) view.findViewById(R$id.category);
        }

        @Override // com.miui.gallery.permission.korea.PermissionsDialogFragment.BaseViewHolder
        public void bindPermission(PermissionWrapper permissionWrapper) {
            this.mCategory.setText(this.itemView.getContext().getResources().getString(permissionWrapper.mRequired ? R$string.permission_require_category : R$string.permission_optional_category));
        }
    }

    /* loaded from: classes2.dex */
    public static class PermissionWrapper extends Permission {
        public final boolean mIsCategory;

        public PermissionWrapper(Permission permission, boolean z) {
            super(permission.mPermissionGroup, permission.mName, permission.mDesc, permission.mRequired);
            this.mIsCategory = z;
        }
    }

    /* loaded from: classes2.dex */
    public class DividerDecoration extends BaseTermsDialogFragment.BaseDividerDecoration {
        public int mCategoryPaddingTop;
        public Drawable mSectionDividerDrawable;
        public int mSectionDividerHeight;

        public DividerDecoration() {
        }

        private void init(Context context) {
            if (this.mSectionDividerDrawable == null) {
                this.mSectionDividerDrawable = context.getResources().getDrawable(R$drawable.section_divider_bg);
                this.mSectionDividerHeight = context.getResources().getDimensionPixelSize(R$dimen.agreement_section_divider_height);
                this.mCategoryPaddingTop = context.getResources().getDimensionPixelSize(R$dimen.permission_category_item_padding_top);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
        public void onDrawOver(Canvas canvas, RecyclerView recyclerView, RecyclerView.State state) {
            int childCount = recyclerView.getChildCount();
            int paddingStart = recyclerView.getPaddingStart();
            int right = recyclerView.getRight() - recyclerView.getPaddingEnd();
            PermissionAdapter permissionAdapter = (PermissionAdapter) recyclerView.getAdapter();
            for (int i = 0; i < childCount; i++) {
                View childAt = recyclerView.getChildAt(i);
                int childAdapterPosition = recyclerView.getChildAdapterPosition(childAt);
                if (childAdapterPosition != -1) {
                    if (childAdapterPosition > 0 && ((PermissionWrapper) permissionAdapter.mPermissions.get(childAdapterPosition)).mIsCategory) {
                        drawSection(canvas, childAt, paddingStart, right);
                    } else {
                        drawTop(canvas, childAt, childAdapterPosition == 0 ? paddingStart : this.mPaddingStart + paddingStart, right);
                        if (childAdapterPosition == recyclerView.getAdapter().getItemCount() - 1) {
                            drawBottom(canvas, childAt, paddingStart, right);
                        }
                    }
                }
            }
        }

        public void drawSection(Canvas canvas, View view, int i, int i2) {
            float top = (view.getTop() - ((ViewGroup.MarginLayoutParams) ((RecyclerView.LayoutParams) view.getLayoutParams())).topMargin) + view.getTranslationY();
            int i3 = this.mSectionDividerHeight;
            int i4 = (int) ((top - i3) - (this.mCategoryPaddingTop / 2));
            this.mSectionDividerDrawable.setBounds(i, i4, i2, i3 + i4);
            this.mSectionDividerDrawable.draw(canvas);
        }

        @Override // com.miui.gallery.BaseTermsDialogFragment.BaseDividerDecoration, androidx.recyclerview.widget.RecyclerView.ItemDecoration
        public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
            int childAdapterPosition = recyclerView.getChildAdapterPosition(view);
            if (childAdapterPosition > 0) {
                init(view.getContext());
                if (((PermissionWrapper) ((PermissionAdapter) recyclerView.getAdapter()).mPermissions.get(childAdapterPosition)).mIsCategory) {
                    rect.set(0, this.mCategoryPaddingTop, 0, 0);
                    return;
                }
            }
            super.getItemOffsets(rect, view, recyclerView, state);
        }
    }
}
