package com.miui.gallery.ui.renameface;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.BaseRequestOptions;
import com.miui.gallery.R;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.glide.load.model.GalleryModel;
import com.miui.gallery.model.DisplayFolderItem;
import com.miui.gallery.provider.deprecated.NormalPeopleFaceMediaSet;
import com.miui.gallery.ui.renameface.FolderItemsLoader;
import com.miui.gallery.widget.GalleryDialogFragment;
import com.miui.gallery.widget.PagerAdapter;
import com.miui.gallery.widget.PagerGridLayout;
import com.miui.gallery.widget.PagerIndicator;
import com.miui.gallery.widget.ViewPager;
import java.util.ArrayList;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes2.dex */
public class PeopleFaceMergeDialogFragment extends GalleryDialogFragment {
    public PeopleFaceMergeDialog mDialog;
    public ArrayList<DisplayFolderItem> mItems;
    public FolderItemsLoader mLoader;
    public PeoplePagerAdapter mPagerAdapter;
    public PagerIndicator mPagerIndicator;
    public PeopleSelectListener mPeopleSelectListener;
    public ViewPager mViewPager;

    /* loaded from: classes2.dex */
    public interface PeopleSelectListener {
        void onPeopleSelect(FaceDisplayFolderItem faceDisplayFolderItem, boolean z, int i);
    }

    public static /* synthetic */ void $r8$lambda$IVEEgAfif0pXt5TlSFRqEiaRxWI(PeopleFaceMergeDialogFragment peopleFaceMergeDialogFragment, View view) {
        peopleFaceMergeDialogFragment.lambda$onCreateDialog$0(view);
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        NormalPeopleFaceMediaSet normalPeopleFaceMediaSet = (NormalPeopleFaceMediaSet) getArguments().getParcelable("merge_action_from_album");
        ArrayList<DisplayFolderItem> arrayList = new ArrayList<>();
        this.mItems = arrayList;
        arrayList.add(new FaceDisplayFolderItem(true));
        if (getResources().getConfiguration().orientation == 1) {
            this.mPagerAdapter = new PeoplePagerAdapter(2);
        } else {
            this.mPagerAdapter = new PeoplePagerAdapter(1);
        }
        initPagerLoader(normalPeopleFaceMediaSet);
    }

    /* loaded from: classes2.dex */
    public class PeopleFaceMergeDialog extends AlertDialog implements View.OnClickListener {
        public View.OnClickListener mCancelClickListener;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public PeopleFaceMergeDialog(Context context) {
            super(context);
            PeopleFaceMergeDialogFragment.this = r4;
            setTitle(r4.getArguments().getString("people_face_Merge_title"));
            View inflate = ((LayoutInflater) getContext().getSystemService("layout_inflater")).inflate(R.layout.people_face_merge, (ViewGroup) null);
            setView(inflate);
            r4.mPagerIndicator = (PagerIndicator) inflate.findViewById(R.id.pager_index);
            r4.mViewPager = (ViewPager) inflate.findViewById(R.id.pager);
            r4.mViewPager.setAdapter(r4.mPagerAdapter);
            r4.mViewPager.setOnPageChangeListener(new PageChangeListener());
            ((Button) inflate.findViewById(R.id.button1)).setOnClickListener(this);
            updateConfiguration(r4.getResources().getConfiguration());
        }

        public PeopleFaceMergeDialog setCancelButtonOnClickListener(View.OnClickListener onClickListener) {
            this.mCancelClickListener = onClickListener;
            return this;
        }

        public void updateConfiguration(Configuration configuration) {
            ViewGroup.LayoutParams layoutParams = PeopleFaceMergeDialogFragment.this.mViewPager.getLayoutParams();
            if (configuration.orientation == 1) {
                layoutParams.height = PeopleFaceMergeDialogFragment.this.getResources().getDimensionPixelOffset(R.dimen.people_face_merge_dialog_viewpager_portrait_height);
                PeopleFaceMergeDialogFragment.this.mPagerIndicator.setPadding(0, PeopleFaceMergeDialogFragment.this.getResources().getDimensionPixelOffset(R.dimen.people_face_merge_dialog_indicator_portrait_padding_top), 0, PeopleFaceMergeDialogFragment.this.getResources().getDimensionPixelOffset(R.dimen.people_face_merge_dialog_indicator_portrait_padding_bottom));
            } else {
                layoutParams.height = PeopleFaceMergeDialogFragment.this.getResources().getDimensionPixelOffset(R.dimen.people_face_merge_dialog_viewpager_land_height);
                PeopleFaceMergeDialogFragment.this.mPagerIndicator.setPadding(0, PeopleFaceMergeDialogFragment.this.getResources().getDimensionPixelOffset(R.dimen.people_face_indicator_padding_top), 0, PeopleFaceMergeDialogFragment.this.getResources().getDimensionPixelOffset(R.dimen.people_face_merge_dialog_indicator_portrait_padding_bottom));
            }
            PeopleFaceMergeDialogFragment.this.mPagerIndicator.setCount(PeopleFaceMergeDialogFragment.this.mPagerAdapter.getCount());
            PeopleFaceMergeDialogFragment.this.mViewPager.setLayoutParams(layoutParams);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            View.OnClickListener onClickListener;
            if (view.getId() != R.id.button1 || (onClickListener = this.mCancelClickListener) == null) {
                return;
            }
            onClickListener.onClick(view);
        }
    }

    public /* synthetic */ void lambda$onCreateDialog$0(View view) {
        this.mDialog.cancel();
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        PeopleFaceMergeDialog cancelButtonOnClickListener = new PeopleFaceMergeDialog(getActivity()).setCancelButtonOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.renameface.PeopleFaceMergeDialogFragment$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PeopleFaceMergeDialogFragment.$r8$lambda$IVEEgAfif0pXt5TlSFRqEiaRxWI(PeopleFaceMergeDialogFragment.this, view);
            }
        });
        this.mDialog = cancelButtonOnClickListener;
        return cancelButtonOnClickListener;
    }

    @Override // com.miui.gallery.widget.GalleryDialogFragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mPagerAdapter.onConfigurationChanged(configuration);
        this.mPagerAdapter.notifyDataSetChanged();
        this.mDialog.updateConfiguration(configuration);
    }

    public final void initPagerLoader(NormalPeopleFaceMediaSet normalPeopleFaceMediaSet) {
        this.mLoader = new FaceFolderItemsLoader(getActivity(), null, new FolderItemsLoader.LoaderUpdatedItems() { // from class: com.miui.gallery.ui.renameface.PeopleFaceMergeDialogFragment.1
            {
                PeopleFaceMergeDialogFragment.this = this;
            }

            @Override // com.miui.gallery.ui.renameface.FolderItemsLoader.LoaderUpdatedItems
            public void onLoaderUpdatedItems() {
                Dialog dialog = PeopleFaceMergeDialogFragment.this.getDialog();
                if (dialog == null || !dialog.isShowing()) {
                    return;
                }
                PeopleFaceMergeDialogFragment.this.mPagerAdapter.updateItems(PeopleFaceMergeDialogFragment.this.mLoader.unblockGetItems());
            }
        }, normalPeopleFaceMediaSet != null ? new long[]{normalPeopleFaceMediaSet.getBucketId()} : null);
    }

    @Override // androidx.fragment.app.DialogFragment, android.content.DialogInterface.OnCancelListener
    public void onCancel(DialogInterface dialogInterface) {
        super.onCancel(dialogInterface);
        this.mLoader.cancel();
    }

    public void setPeopleSelectListener(PeopleSelectListener peopleSelectListener) {
        this.mPeopleSelectListener = peopleSelectListener;
    }

    /* loaded from: classes2.dex */
    public class PageChangeListener implements ViewPager.OnPageChangeListener {
        @Override // com.miui.gallery.widget.ViewPager.OnPageChangeListener
        public void onPageScrollStateChanged(int i) {
        }

        @Override // com.miui.gallery.widget.ViewPager.OnPageChangeListener
        public void onPageScrolled(int i, float f, int i2) {
        }

        public PageChangeListener() {
            PeopleFaceMergeDialogFragment.this = r1;
        }

        @Override // com.miui.gallery.widget.ViewPager.OnPageChangeListener
        public void onPageSelected(int i) {
            if (PeopleFaceMergeDialogFragment.this.mPagerAdapter.getCount() > 1) {
                PeopleFaceMergeDialogFragment.this.mPagerIndicator.showIndex(i, PeopleFaceMergeDialogFragment.this.mPagerAdapter.getCount());
            }
        }
    }

    /* loaded from: classes2.dex */
    public class PeoplePagerAdapter extends PagerAdapter {
        public PagerGridLayoutAdapter mAdapter;
        public int mRowNum;

        @Override // com.miui.gallery.widget.PagerAdapter
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        public PeoplePagerAdapter(int i) {
            PeopleFaceMergeDialogFragment.this = r3;
            this.mRowNum = 2;
            this.mRowNum = i;
            this.mAdapter = new PagerGridLayoutAdapter(r3.mItems, this.mRowNum);
        }

        @Override // com.miui.gallery.widget.PagerAdapter
        public int getCount() {
            if (this.mRowNum == 1) {
                if (PeopleFaceMergeDialogFragment.this.mItems != null) {
                    return (PeopleFaceMergeDialogFragment.this.mItems.size() + 3) / 4;
                }
                return 0;
            } else if (PeopleFaceMergeDialogFragment.this.mItems != null) {
                return (PeopleFaceMergeDialogFragment.this.mItems.size() + 7) / 8;
            } else {
                return 0;
            }
        }

        @Override // com.miui.gallery.widget.PagerAdapter
        public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
            viewGroup.removeView((View) obj);
        }

        public void onConfigurationChanged(Configuration configuration) {
            if (configuration.orientation == 2) {
                this.mRowNum = 1;
            } else {
                this.mRowNum = 2;
            }
            this.mAdapter.onConfigurationChanged(configuration);
            this.mAdapter.notifyDataSetChanged();
        }

        @Override // com.miui.gallery.widget.PagerAdapter
        public Object instantiateItem(ViewGroup viewGroup, int i) {
            PagerGridLayout pagerLayout = getPagerLayout();
            pagerLayout.setAdapter(this.mAdapter, i);
            viewGroup.addView(pagerLayout);
            return pagerLayout;
        }

        @Override // com.miui.gallery.widget.PagerAdapter
        public int getItemPosition(Object obj, int i) {
            return (i <= 0 || i >= getCount()) ? -2 : -3;
        }

        @Override // com.miui.gallery.widget.PagerAdapter
        public void refreshItem(Object obj, int i) {
            super.refreshItem(obj, i);
            ((PagerGridLayout) obj).requestLayout();
        }

        public void updateItems(ArrayList<DisplayFolderItem> arrayList) {
            PeopleFaceMergeDialogFragment.this.mItems.addAll(arrayList);
            this.mAdapter.updateItems(PeopleFaceMergeDialogFragment.this.mItems);
            notifyDataSetChanged();
        }

        public final PagerGridLayout getPagerLayout() {
            PagerGridLayout pagerGridLayout = new PagerGridLayout(PeopleFaceMergeDialogFragment.this.getActivity());
            int dimensionPixelSize = PeopleFaceMergeDialogFragment.this.getActivity().getResources().getDimensionPixelSize(R.dimen.people_face_merge_dialog_padding);
            pagerGridLayout.setPaddingRelative(dimensionPixelSize, 0, dimensionPixelSize, 0);
            return pagerGridLayout;
        }
    }

    /* loaded from: classes2.dex */
    public class PagerGridLayoutAdapter extends PagerGridLayout.BaseAdapter {
        public ArrayList<DisplayFolderItem> mGridLayoutItems;
        public int mRowNum;

        @Override // com.miui.gallery.widget.PagerGridLayout.BaseAdapter
        public void freshView(View view) {
        }

        @Override // com.miui.gallery.widget.PagerGridLayout.BaseAdapter
        public int getColumnsCount() {
            return 4;
        }

        @Override // com.miui.gallery.widget.PagerGridLayout.BaseAdapter
        public void onPageChanged() {
        }

        public PagerGridLayoutAdapter(ArrayList<DisplayFolderItem> arrayList, int i) {
            PeopleFaceMergeDialogFragment.this = r1;
            this.mRowNum = 2;
            this.mGridLayoutItems = arrayList;
            this.mRowNum = i;
        }

        public void updateItems(ArrayList<DisplayFolderItem> arrayList) {
            this.mGridLayoutItems = arrayList;
            notifyDataSetChanged();
        }

        public void onConfigurationChanged(Configuration configuration) {
            if (configuration.orientation == 1) {
                this.mRowNum = 2;
            } else {
                this.mRowNum = 1;
            }
            notifyDataSetInvalidated();
        }

        @Override // com.miui.gallery.widget.PagerGridLayout.BaseAdapter
        public int getRowsCount() {
            return this.mRowNum;
        }

        @Override // com.miui.gallery.widget.PagerGridLayout.BaseAdapter
        public int getCount() {
            ArrayList<DisplayFolderItem> arrayList = this.mGridLayoutItems;
            if (arrayList == null) {
                return 0;
            }
            return arrayList.size();
        }

        @Override // com.miui.gallery.widget.PagerGridLayout.BaseAdapter
        public View getView(LayoutInflater layoutInflater, ViewGroup viewGroup) {
            View inflate = layoutInflater.inflate(R.layout.people_face_merge_pager_item, viewGroup, false);
            inflate.setTag(new ViewHolder(inflate));
            return inflate;
        }

        @Override // com.miui.gallery.widget.PagerGridLayout.BaseAdapter
        public void bindData(int i, View view) {
            ((ViewHolder) view.getTag()).bindView((FaceDisplayFolderItem) this.mGridLayoutItems.get(i));
        }

        public final void setItemClickListener(View view, final FaceDisplayFolderItem faceDisplayFolderItem) {
            view.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.renameface.PeopleFaceMergeDialogFragment.PagerGridLayoutAdapter.1
                {
                    PagerGridLayoutAdapter.this = this;
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    String str = faceDisplayFolderItem.name;
                    PeopleFaceMergeDialogFragment.this.mPeopleSelectListener.onPeopleSelect(faceDisplayFolderItem, (str != null ? FaceAlbumRenameHandler.getDisplayFolderItem(PeopleFaceMergeDialogFragment.this.mLoader.tryToGetLoadedItems(300L), str.trim()) : null) != null, ((Integer) view2.getTag()).intValue());
                }
            });
        }

        /* loaded from: classes2.dex */
        public class ViewHolder {
            public ImageView cover;
            public TextView name;

            public ViewHolder(View view) {
                PagerGridLayoutAdapter.this = r1;
                this.name = (TextView) view.findViewById(R.id.name);
                this.cover = (ImageView) view.findViewById(R.id.face);
            }

            public void bindView(FaceDisplayFolderItem faceDisplayFolderItem) {
                if (faceDisplayFolderItem.isTip) {
                    this.cover.setBackgroundResource(R.drawable.create_new_face);
                    this.cover.setTag(0);
                    this.name.setText(PeopleFaceMergeDialogFragment.this.getString(R.string.create_new_face));
                } else {
                    this.name.setText(faceDisplayFolderItem.name);
                    this.cover.setTag(1);
                    Glide.with(this.cover).mo985asBitmap().mo962load(GalleryModel.of(faceDisplayFolderItem.thumbnailPath)).mo946apply((BaseRequestOptions<?>) GlideOptions.peopleFaceOf(faceDisplayFolderItem.mFacePosRelative)).into(this.cover);
                }
                PagerGridLayoutAdapter.this.setItemClickListener(this.cover, faceDisplayFolderItem);
            }
        }
    }
}
