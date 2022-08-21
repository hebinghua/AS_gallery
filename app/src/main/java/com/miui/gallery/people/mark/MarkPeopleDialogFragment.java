package com.miui.gallery.people.mark;

import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.people.PeopleDisplayHelper;
import com.miui.gallery.people.mark.MarkPeopleDialogFragment;
import com.miui.gallery.people.model.People;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.DividerItemDecoration;
import com.miui.gallery.widget.GalleryDialogFragment;
import com.miui.gallery.widget.recyclerview.GridItemSpacingDecoration;
import java.util.ArrayList;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes2.dex */
public class MarkPeopleDialogFragment extends GalleryDialogFragment implements View.OnClickListener {
    public LinearLayout mButtonGroup;
    public DialogInterface.OnCancelListener mCancelButtonListener;
    public View mCancelView;
    public RecyclerView mDataView;
    public MarkPeopleDialog mDialog;
    public GridLayoutManager mGridLayoutManager;
    public RecyclerView.ItemDecoration mItemDecoration;
    public DialogInterface.OnClickListener mLoadMoreButtonListener;
    public String mLoadMoreButtonText;
    public Button mLoadMoreView;
    public ArrayList<People> mPeopleList;
    public OnPeopleSelectListener mPeopleSelectListener;
    public View mSingleView;
    public ViewStub mSingleViewStub;
    public String mSubTitle;
    public TextView mSubTitleView;
    public String mTitle;
    public TextView mTitleView;
    public boolean mShowCancelButton = false;
    public RecyclerView.Adapter<PeopleItemViewHolder> mAdapter = new RecyclerView.Adapter<PeopleItemViewHolder>() { // from class: com.miui.gallery.people.mark.MarkPeopleDialogFragment.2
        {
            MarkPeopleDialogFragment.this = this;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* renamed from: onCreateViewHolder */
        public PeopleItemViewHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
            MarkPeopleDialogFragment markPeopleDialogFragment = MarkPeopleDialogFragment.this;
            return new PeopleItemViewHolder(LayoutInflater.from(markPeopleDialogFragment.getActivity()).inflate(R.layout.mark_people_item, viewGroup, false));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(PeopleItemViewHolder peopleItemViewHolder, int i) {
            peopleItemViewHolder.bindView((People) MarkPeopleDialogFragment.this.mPeopleList.get(i));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return MarkPeopleDialogFragment.this.mPeopleList.size();
        }
    };

    /* loaded from: classes2.dex */
    public interface OnPeopleSelectListener {
        void onPeopleSelected(int i, People people);
    }

    /* renamed from: $r8$lambda$_Q90Wasy-QfBiZnMODxJCmbpliY */
    public static /* synthetic */ void m1168$r8$lambda$_Q90WasyQfBiZnMODxJCmbpliY(MarkPeopleDialogFragment markPeopleDialogFragment, View view) {
        markPeopleDialogFragment.lambda$configDataView$0(view);
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        if (bundle != null) {
            this.mTitle = bundle.getString("MarkPeopleDialogFragment_Title");
            this.mSubTitle = bundle.getString("MarkPeopleDialogFragment_SubTitle");
            this.mLoadMoreButtonText = bundle.getString("MarkPeopleDialogFragment_LoadMoreButtonText");
            this.mShowCancelButton = bundle.getBoolean("MarkPeopleDialogFragment_ShowCancelButton", false);
            if (bundle.getSerializable("MarkPeopleDialogFragment_PeopleList") != null) {
                this.mPeopleList = (ArrayList) bundle.getSerializable("MarkPeopleDialogFragment_PeopleList");
            }
        }
        MarkPeopleDialog markPeopleDialog = new MarkPeopleDialog(getActivity());
        markPeopleDialog.setCancelable(false);
        this.mDialog = markPeopleDialog;
        return markPeopleDialog;
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (!TextUtils.isEmpty(this.mTitle)) {
            bundle.putString("MarkPeopleDialogFragment_Title", this.mTitle);
        }
        if (!TextUtils.isEmpty(this.mSubTitle)) {
            bundle.putString("MarkPeopleDialogFragment_SubTitle", this.mSubTitle);
        }
        if (!TextUtils.isEmpty(this.mLoadMoreButtonText)) {
            bundle.putString("MarkPeopleDialogFragment_LoadMoreButtonText", this.mLoadMoreButtonText);
        }
        bundle.putBoolean("MarkPeopleDialogFragment_ShowCancelButton", this.mShowCancelButton);
        ArrayList<People> arrayList = this.mPeopleList;
        if (arrayList != null) {
            bundle.putParcelableArrayList("MarkPeopleDialogFragment_PeopleList", arrayList);
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        DialogInterface.OnClickListener onClickListener;
        if (view != null) {
            int id = view.getId();
            if (id != R.id.close_button) {
                if (id != R.id.load_more_button || (onClickListener = this.mLoadMoreButtonListener) == null) {
                    return;
                }
                onClickListener.onClick(this.mDialog, -10);
                return;
            }
            DialogInterface.OnCancelListener onCancelListener = this.mCancelButtonListener;
            if (onCancelListener == null) {
                return;
            }
            onCancelListener.onCancel(this.mDialog);
        }
    }

    @Override // androidx.fragment.app.DialogFragment, android.content.DialogInterface.OnCancelListener
    public void onCancel(DialogInterface dialogInterface) {
        DialogInterface.OnCancelListener onCancelListener = this.mCancelButtonListener;
        if (onCancelListener != null) {
            onCancelListener.onCancel(dialogInterface);
        }
    }

    public void setTitle(String str) {
        if (str.split("\\\n").length == 2) {
            this.mTitle = str.split("\\\n")[0];
            this.mSubTitle = str.split("\\\n")[1];
        } else {
            this.mTitle = str;
            this.mSubTitle = "";
        }
        configTitle();
    }

    public void setCancelButton(boolean z, DialogInterface.OnCancelListener onCancelListener) {
        this.mShowCancelButton = z;
        this.mCancelButtonListener = onCancelListener;
        configCancelButton();
    }

    public void setLoadMoreButton(String str, DialogInterface.OnClickListener onClickListener) {
        this.mLoadMoreButtonText = str;
        this.mLoadMoreButtonListener = onClickListener;
        configLoadMoreButton();
    }

    public void setPeopleList(ArrayList<People> arrayList, OnPeopleSelectListener onPeopleSelectListener) {
        this.mPeopleList = arrayList;
        this.mPeopleSelectListener = onPeopleSelectListener;
        configDataView();
        if (isAdded()) {
            this.mAdapter.notifyDataSetChanged();
        }
    }

    public void configTitle() {
        TextView textView = this.mTitleView;
        if (textView != null) {
            textView.setText(this.mTitle);
        }
        TextView textView2 = this.mSubTitleView;
        if (textView2 != null) {
            textView2.setText(this.mSubTitle);
        }
    }

    public final void configCancelButton() {
        View view = this.mCancelView;
        if (view != null) {
            if (this.mShowCancelButton) {
                view.setVisibility(0);
                this.mCancelView.setOnClickListener(this);
                return;
            }
            view.setVisibility(8);
            this.mCancelView.setOnClickListener(null);
        }
    }

    public final void configLoadMoreButton() {
        if (this.mLoadMoreView != null) {
            if (!TextUtils.isEmpty(this.mLoadMoreButtonText)) {
                new SpannableString(this.mLoadMoreButtonText).setSpan(new CharacterStyle() { // from class: com.miui.gallery.people.mark.MarkPeopleDialogFragment.1
                    {
                        MarkPeopleDialogFragment.this = this;
                    }

                    @Override // android.text.style.CharacterStyle
                    public void updateDrawState(TextPaint textPaint) {
                        textPaint.setColor(MarkPeopleDialogFragment.this.getResources().getColor(R.color.text_blue));
                        textPaint.setUnderlineText(true);
                    }
                }, 0, this.mLoadMoreButtonText.length(), 33);
                this.mLoadMoreView.setText(this.mLoadMoreButtonText);
                this.mLoadMoreView.setVisibility(0);
                this.mLoadMoreView.setOnClickListener(this);
                return;
            }
            this.mLoadMoreView.setVisibility(8);
            this.mLoadMoreView.setOnClickListener(null);
        }
    }

    public final void configDataView() {
        ViewStub viewStub;
        ArrayList<People> arrayList = this.mPeopleList;
        if (arrayList == null) {
            RecyclerView recyclerView = this.mDataView;
            if (recyclerView != null) {
                recyclerView.setVisibility(8);
            }
            View view = this.mSingleView;
            if (view == null) {
                return;
            }
            view.setVisibility(8);
        } else if (arrayList.size() == 1) {
            if (this.mSingleView == null && (viewStub = this.mSingleViewStub) != null) {
                this.mSingleView = viewStub.inflate();
            }
            View view2 = this.mSingleView;
            if (view2 != null) {
                bindPeopleCover((ImageView) view2.findViewById(R.id.icon), this.mPeopleList.get(0));
                this.mSingleView.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.people.mark.MarkPeopleDialogFragment$$ExternalSyntheticLambda0
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view3) {
                        MarkPeopleDialogFragment.m1168$r8$lambda$_Q90WasyQfBiZnMODxJCmbpliY(MarkPeopleDialogFragment.this, view3);
                    }
                });
                this.mSingleView.setVisibility(0);
            }
            RecyclerView recyclerView2 = this.mDataView;
            if (recyclerView2 == null) {
                return;
            }
            recyclerView2.setVisibility(8);
        } else {
            RecyclerView recyclerView3 = this.mDataView;
            if (recyclerView3 != null) {
                recyclerView3.setAdapter(this.mAdapter);
                this.mDataView.setVisibility(0);
            }
            View view3 = this.mSingleView;
            if (view3 == null) {
                return;
            }
            view3.setVisibility(8);
        }
    }

    public /* synthetic */ void lambda$configDataView$0(View view) {
        onClickPeople(0);
    }

    /* loaded from: classes2.dex */
    public class PeopleItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView mCover;

        public static /* synthetic */ void $r8$lambda$Y4l3u7vk0l8ZnJ8QvUQgQhdPmcs(PeopleItemViewHolder peopleItemViewHolder, View view) {
            peopleItemViewHolder.lambda$bindView$0(view);
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public PeopleItemViewHolder(View view) {
            super(view);
            MarkPeopleDialogFragment.this = r1;
            this.mCover = (ImageView) view.findViewById(R.id.icon);
        }

        public void bindView(People people) {
            MarkPeopleDialogFragment.this.bindPeopleCover(this.mCover, people);
            this.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.people.mark.MarkPeopleDialogFragment$PeopleItemViewHolder$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    MarkPeopleDialogFragment.PeopleItemViewHolder.$r8$lambda$Y4l3u7vk0l8ZnJ8QvUQgQhdPmcs(MarkPeopleDialogFragment.PeopleItemViewHolder.this, view);
                }
            });
        }

        public /* synthetic */ void lambda$bindView$0(View view) {
            MarkPeopleDialogFragment.this.onClickPeople(getAdapterPosition());
        }
    }

    public final void bindPeopleCover(ImageView imageView, People people) {
        PeopleDisplayHelper.bindImage(imageView, people.getCoverPath(), ContentUris.withAppendedId(GalleryContract.Cloud.CLOUD_URI, people.getCoverImageId()), GlideOptions.peopleFaceOf(people.getCoverFaceRect()), people.getCoverType());
    }

    public final void onClickPeople(int i) {
        OnPeopleSelectListener onPeopleSelectListener;
        if (i < 0 || i >= this.mPeopleList.size() || (onPeopleSelectListener = this.mPeopleSelectListener) == null) {
            return;
        }
        onPeopleSelectListener.onPeopleSelected(i, this.mPeopleList.get(i));
    }

    @Override // com.miui.gallery.widget.GalleryDialogFragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        RecyclerView recyclerView;
        this.mDialog.updateConfiguration(configuration);
        super.onConfigurationChanged(configuration);
        if (!isAdded() || this.mGridLayoutManager == null || (recyclerView = this.mDataView) == null) {
            return;
        }
        BaseMiscUtil.invokeSafely(recyclerView, "markItemDecorInsetsDirty", null, new Object[0]);
    }

    /* loaded from: classes2.dex */
    public class MarkPeopleDialog extends AlertDialog {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public MarkPeopleDialog(Context context) {
            super(context);
            MarkPeopleDialogFragment.this = r3;
            View inflate = ((LayoutInflater) getContext().getSystemService("layout_inflater")).inflate(R.layout.mark_people_dialog_layout, (ViewGroup) null);
            setView(inflate);
            r3.mTitleView = (TextView) inflate.findViewById(R.id.dialog_title);
            r3.mSubTitleView = (TextView) inflate.findViewById(R.id.dialog_subtitle);
            r3.mCancelView = inflate.findViewById(R.id.close_button);
            r3.mButtonGroup = (LinearLayout) inflate.findViewById(R.id.buttonGroup);
            r3.mLoadMoreView = (Button) inflate.findViewById(R.id.load_more_button);
            r3.mDataView = (RecyclerView) inflate.findViewById(R.id.data_view);
            r3.mSingleViewStub = (ViewStub) inflate.findViewById(R.id.single_people_view);
            r3.configTitle();
            r3.configCancelButton();
            r3.configLoadMoreButton();
            r3.configDataView();
            updateConfiguration(r3.getResources().getConfiguration());
        }

        public void updateConfiguration(Configuration configuration) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) MarkPeopleDialogFragment.this.mButtonGroup.getLayoutParams();
            ViewGroup.LayoutParams layoutParams2 = MarkPeopleDialogFragment.this.mDataView.getLayoutParams();
            if (MarkPeopleDialogFragment.this.mItemDecoration != null) {
                MarkPeopleDialogFragment.this.mDataView.removeItemDecoration(MarkPeopleDialogFragment.this.mItemDecoration);
            }
            if (configuration.orientation == 2) {
                layoutParams.topMargin = getContext().getResources().getDimensionPixelOffset(R.dimen.people_dialog_land_button_margin_top);
                MarkPeopleDialogFragment.this.mSubTitleView.setVisibility(8);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                linearLayoutManager.setOrientation(0);
                Drawable drawable = MarkPeopleDialogFragment.this.getActivity().getResources().getDrawable(R.drawable.mark_people_divider);
                MarkPeopleDialogFragment.this.mItemDecoration = new DividerItemDecoration(drawable);
                ((DividerItemDecoration) MarkPeopleDialogFragment.this.mItemDecoration).setOrientation(0);
                MarkPeopleDialogFragment.this.mDataView.setLayoutManager(linearLayoutManager);
                layoutParams2.height = -2;
            } else {
                layoutParams2.height = MarkPeopleDialogFragment.this.getResources().getDimensionPixelSize(R.dimen.people_dialog_recycler_height);
                MarkPeopleDialogFragment.this.mDataView.setLayoutParams(layoutParams2);
                MarkPeopleDialogFragment.this.mSubTitleView.setVisibility(0);
                MarkPeopleDialogFragment.this.mGridLayoutManager = new GridLayoutManager(getContext(), MarkPeopleDialogFragment.this.getResources().getInteger(R.integer.mark_people_column_count));
                MarkPeopleDialogFragment.this.mDataView.removeItemDecoration(MarkPeopleDialogFragment.this.mItemDecoration);
                int dimensionPixelSize = MarkPeopleDialogFragment.this.getResources().getDimensionPixelSize(R.dimen.people_dialog_people_item_vertical_padding);
                MarkPeopleDialogFragment markPeopleDialogFragment = MarkPeopleDialogFragment.this;
                markPeopleDialogFragment.mItemDecoration = new GridItemSpacingDecoration(markPeopleDialogFragment.mDataView, false, 100, dimensionPixelSize);
                MarkPeopleDialogFragment.this.mDataView.setLayoutManager(MarkPeopleDialogFragment.this.mGridLayoutManager);
                layoutParams.topMargin = getContext().getResources().getDimensionPixelOffset(R.dimen.people_dialog_button_margin_top);
            }
            MarkPeopleDialogFragment.this.mDataView.addItemDecoration(MarkPeopleDialogFragment.this.mItemDecoration);
            MarkPeopleDialogFragment.this.mDataView.setLayoutParams(layoutParams2);
            MarkPeopleDialogFragment.this.mButtonGroup.setLayoutParams(layoutParams);
        }
    }
}
