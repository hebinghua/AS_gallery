package com.miui.gallery.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.miui.gallery.R;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.AIAlbumStatusHelper;
import com.miui.gallery.util.GalleryIntent$CloudGuideSource;
import com.miui.gallery.util.IntentUtil;
import com.xiaomi.stat.MiStat;

/* loaded from: classes2.dex */
public class StatusHandleHelper {
    public ErrorViewAdapter mErrorViewAdapter;
    public View[] mFullScreenViews;
    public View mInfoFooterView;
    public View mInfoHeaderView;
    public boolean mInitiated;
    public int mBaseStatus = -1;
    public int mResultStatus = -1;
    public int mTopVisibleView = -1;

    /* loaded from: classes2.dex */
    public interface ErrorViewAdapter {
        void addFooterView(View view);

        void addHeaderView(View view);

        void bindInfoView(View view, int i, InfoViewPosition infoViewPosition);

        View bindLoadingView(View view, int i, InfoViewPosition infoViewPosition);

        View getInfoView(View view, int i, InfoViewPosition infoViewPosition);

        View getLoadingView(View view, int i, InfoViewPosition infoViewPosition);

        boolean isEmptyDataView();

        boolean isLoading();

        InfoViewPosition positionForBaseStatus(int i);

        InfoViewPosition positionForResultStatus(int i);

        void removeFooterView(View view);

        void removeHeaderView(View view);

        void requestRetry();
    }

    /* loaded from: classes2.dex */
    public enum InfoViewPosition {
        NONE,
        FULL_SCREEN,
        HEADER,
        FOOTER
    }

    public void init(View view, View view2, View view3, View view4, ErrorViewAdapter errorViewAdapter) {
        if (errorViewAdapter == null) {
            throw new IllegalArgumentException("The ErrorViewAdapter cannot be null!");
        }
        this.mErrorViewAdapter = errorViewAdapter;
        this.mFullScreenViews = new View[]{view, view3, view4, view2};
        this.mInitiated = true;
    }

    public void updateBaseStatus(int i) {
        int i2;
        if (!this.mInitiated || (i2 = this.mBaseStatus) == i) {
            return;
        }
        this.mBaseStatus = i;
        if (SearchConstants.isErrorStatus(i2) && !SearchConstants.isErrorStatus(this.mBaseStatus)) {
            this.mErrorViewAdapter.requestRetry();
        }
        refreshInfoViews();
    }

    public int getResultStatus() {
        return this.mResultStatus;
    }

    public void updateResultStatus(int i) {
        this.mResultStatus = i;
        refreshInfoViews();
    }

    public void refreshInfoViews() {
        int i;
        if (!this.mInitiated) {
            return;
        }
        if (this.mErrorViewAdapter.isEmptyDataView()) {
            if (this.mErrorViewAdapter.isLoading()) {
                View[] viewArr = this.mFullScreenViews;
                ErrorViewAdapter errorViewAdapter = this.mErrorViewAdapter;
                View view = viewArr[1];
                int i2 = this.mResultStatus;
                InfoViewPosition infoViewPosition = InfoViewPosition.FULL_SCREEN;
                viewArr[1] = errorViewAdapter.getLoadingView(view, i2, infoViewPosition);
                setOnlyVisibleView(1);
                this.mErrorViewAdapter.bindLoadingView(this.mFullScreenViews[1], this.mResultStatus, infoViewPosition);
                return;
            } else if (SearchConstants.isErrorStatus(this.mResultStatus)) {
                View[] viewArr2 = this.mFullScreenViews;
                ErrorViewAdapter errorViewAdapter2 = this.mErrorViewAdapter;
                View view2 = viewArr2[3];
                int i3 = this.mResultStatus;
                InfoViewPosition infoViewPosition2 = InfoViewPosition.FULL_SCREEN;
                viewArr2[3] = errorViewAdapter2.getInfoView(view2, i3, infoViewPosition2);
                setOnlyVisibleView(3);
                this.mErrorViewAdapter.bindInfoView(this.mFullScreenViews[3], this.mResultStatus, infoViewPosition2);
                return;
            } else if (SearchConstants.isErrorStatus(this.mBaseStatus)) {
                View[] viewArr3 = this.mFullScreenViews;
                ErrorViewAdapter errorViewAdapter3 = this.mErrorViewAdapter;
                View view3 = viewArr3[3];
                int i4 = this.mBaseStatus;
                InfoViewPosition infoViewPosition3 = InfoViewPosition.FULL_SCREEN;
                viewArr3[3] = errorViewAdapter3.getInfoView(view3, i4, infoViewPosition3);
                setOnlyVisibleView(3);
                this.mErrorViewAdapter.bindInfoView(this.mFullScreenViews[3], this.mBaseStatus, infoViewPosition3);
                return;
            } else {
                setOnlyVisibleView(2);
                return;
            }
        }
        setOnlyVisibleView(0);
        View view4 = this.mInfoHeaderView;
        View view5 = this.mInfoFooterView;
        if (view4 != null) {
            this.mErrorViewAdapter.removeHeaderView(view4);
            this.mInfoHeaderView = null;
        }
        View view6 = this.mInfoFooterView;
        if (view6 != null) {
            this.mErrorViewAdapter.removeFooterView(view6);
            this.mInfoFooterView = null;
        }
        InfoViewPosition positionForResultStatus = this.mErrorViewAdapter.positionForResultStatus(this.mResultStatus);
        InfoViewPosition infoViewPosition4 = InfoViewPosition.HEADER;
        int i5 = -1;
        if (positionForResultStatus == infoViewPosition4) {
            i = this.mResultStatus;
        } else {
            i = this.mErrorViewAdapter.positionForBaseStatus(this.mBaseStatus) == infoViewPosition4 ? this.mBaseStatus : -1;
        }
        View infoView = this.mErrorViewAdapter.getInfoView(view4, i, infoViewPosition4);
        this.mInfoHeaderView = infoView;
        if (infoView != null) {
            this.mErrorViewAdapter.bindInfoView(infoView, i, infoViewPosition4);
            this.mErrorViewAdapter.addHeaderView(this.mInfoHeaderView);
        }
        if (this.mErrorViewAdapter.isLoading()) {
            View loadingView = this.mErrorViewAdapter.getLoadingView(view5, this.mResultStatus, InfoViewPosition.FOOTER);
            this.mInfoFooterView = loadingView;
            if (loadingView == null) {
                return;
            }
            this.mErrorViewAdapter.addFooterView(loadingView);
            return;
        }
        InfoViewPosition positionForResultStatus2 = this.mErrorViewAdapter.positionForResultStatus(this.mResultStatus);
        InfoViewPosition infoViewPosition5 = InfoViewPosition.FOOTER;
        if (positionForResultStatus2 == infoViewPosition5) {
            i5 = this.mResultStatus;
        } else if (this.mErrorViewAdapter.positionForBaseStatus(this.mBaseStatus) == infoViewPosition5) {
            i5 = this.mBaseStatus;
        }
        View infoView2 = this.mErrorViewAdapter.getInfoView(view5, i5, infoViewPosition5);
        this.mInfoFooterView = infoView2;
        if (infoView2 == null) {
            return;
        }
        this.mErrorViewAdapter.bindInfoView(infoView2, i5, infoViewPosition5);
        this.mErrorViewAdapter.addFooterView(this.mInfoFooterView);
    }

    public final void setVisibility(int i, int i2) {
        View view = this.mFullScreenViews[i];
        boolean z = view instanceof ViewStub;
        if (i2 == 8) {
            if (z) {
                return;
            }
            view.setVisibility(i2);
            return;
        }
        if (z) {
            view = ((ViewStub) view).inflate();
            this.mFullScreenViews[i] = view;
        }
        view.setVisibility(i2);
    }

    public final void setOnlyVisibleView(int i) {
        int i2 = 0;
        while (i2 < this.mFullScreenViews.length) {
            setVisibility(i2, i2 == i ? 0 : 8);
            i2++;
        }
        if (this.mTopVisibleView != i) {
            this.mTopVisibleView = i;
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class AbstractErrorViewAdapter implements ErrorViewAdapter {
        public final Context mContext;
        public final LayoutInflater mInflater;

        public boolean shouldShowFixButtonForStatus(int i, InfoViewPosition infoViewPosition) {
            return (i == 13 || i == 14) ? false : true;
        }

        public AbstractErrorViewAdapter(Context context) {
            this.mContext = context;
            this.mInflater = LayoutInflater.from(context);
        }

        @Override // com.miui.gallery.search.StatusHandleHelper.ErrorViewAdapter
        public View getInfoView(View view, int i, InfoViewPosition infoViewPosition) {
            if (!SearchConstants.isErrorStatus(i)) {
                return null;
            }
            return infoViewPosition == InfoViewPosition.FULL_SCREEN ? view : getInfoItemView(view, i, infoViewPosition);
        }

        @Override // com.miui.gallery.search.StatusHandleHelper.ErrorViewAdapter
        public void bindInfoView(View view, int i, InfoViewPosition infoViewPosition) {
            if (view == null) {
                return;
            }
            setTextIfExist(view, R.id.title, getInfoTitleForStatus(i, infoViewPosition));
            setTextIfExist(view, R.id.sub_title, getInfoSubTitleForStatus(i));
            if (shouldShowFixButtonForStatus(i, infoViewPosition)) {
                setVisibilityIfExist(view, R.id.fix_button, 0);
                setTextIfExist(view, R.id.fix_button, getFixButtonTextForStatus(i, infoViewPosition));
            } else {
                setVisibilityIfExist(view, R.id.fix_button, 4);
            }
            setIconIfExist(view, R.id.icon, getIconResForStatus(i, infoViewPosition));
            if (view.findViewById(R.id.fix_button) == null) {
                return;
            }
            bindFixActionForStatus(i, view.findViewById(R.id.fix_button), infoViewPosition);
        }

        public void setIconIfExist(View view, int i, int i2) {
            View findViewById;
            if (i2 <= 0 || (findViewById = view.findViewById(i)) == null || !(findViewById instanceof ImageView)) {
                return;
            }
            ((ImageView) findViewById).setImageResource(i2);
        }

        public void setTextIfExist(View view, int i, String str) {
            View findViewById = view.findViewById(i);
            if (findViewById != null) {
                if (findViewById instanceof TextView) {
                    ((TextView) findViewById).setText(str);
                } else if (!(findViewById instanceof Button)) {
                } else {
                    ((Button) findViewById).setText(str);
                }
            }
        }

        public void setVisibilityIfExist(View view, int i, int i2) {
            View findViewById = view.findViewById(i);
            if (findViewById != null) {
                findViewById.setVisibility(i2);
            }
        }

        @Override // com.miui.gallery.search.StatusHandleHelper.ErrorViewAdapter
        public View getLoadingView(View view, int i, InfoViewPosition infoViewPosition) {
            return infoViewPosition == InfoViewPosition.FULL_SCREEN ? view : getLoadingItemView(view);
        }

        @Override // com.miui.gallery.search.StatusHandleHelper.ErrorViewAdapter
        public View bindLoadingView(View view, int i, InfoViewPosition infoViewPosition) {
            if (SearchConstants.isErrorStatus(i)) {
                setIconIfExist(view, R.id.icon, getIconResForStatus(i, infoViewPosition));
                if (view.findViewById(R.id.icon) != null) {
                    view.findViewById(R.id.icon).setVisibility(0);
                }
                if (view.findViewById(R.id.progress) != null) {
                    view.findViewById(R.id.progress).setVisibility(8);
                }
            } else {
                if (view.findViewById(R.id.icon) != null) {
                    view.findViewById(R.id.icon).setVisibility(8);
                }
                if (view.findViewById(R.id.progress) != null) {
                    view.findViewById(R.id.progress).setVisibility(0);
                }
            }
            return view;
        }

        @Override // com.miui.gallery.search.StatusHandleHelper.ErrorViewAdapter
        public InfoViewPosition positionForBaseStatus(int i) {
            if (!SearchConstants.isErrorStatus(i)) {
                return InfoViewPosition.NONE;
            }
            return InfoViewPosition.HEADER;
        }

        @Override // com.miui.gallery.search.StatusHandleHelper.ErrorViewAdapter
        public InfoViewPosition positionForResultStatus(int i) {
            if (!SearchConstants.isErrorStatus(i)) {
                return InfoViewPosition.NONE;
            }
            return InfoViewPosition.FOOTER;
        }

        public void bindFixActionForStatus(int i, View view, InfoViewPosition infoViewPosition) {
            final String str;
            if (i == 1) {
                str = "android.settings.SETTINGS";
            } else if (i == 10) {
                view.setVisibility(4);
                return;
            } else if (i == 3) {
                view.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.search.StatusHandleHelper.AbstractErrorViewAdapter.2
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("cloud_guide_source", GalleryIntent$CloudGuideSource.SEARCH);
                        IntentUtil.guideToLoginXiaomiAccount(AbstractErrorViewAdapter.this.mContext, bundle);
                    }
                });
                return;
            } else if (i == 4) {
                str = "com.miui.gallery.cloud.provider.SYNC_SETTINGS";
            } else if (i != 5) {
                switch (i) {
                    case 12:
                        view.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.search.StatusHandleHelper.AbstractErrorViewAdapter.1
                            @Override // android.view.View.OnClickListener
                            public void onClick(View view2) {
                                AIAlbumStatusHelper.setLocalSearchStatus(AbstractErrorViewAdapter.this.mContext, true);
                                AbstractErrorViewAdapter.this.requestRetry();
                                SamplingStatHelper.recordCountEvent(MiStat.Event.SEARCH, "search_open_switch");
                            }
                        });
                        return;
                    case 13:
                    case 14:
                        view.setOnClickListener(null);
                        return;
                    default:
                        view.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.search.StatusHandleHelper.AbstractErrorViewAdapter.3
                            @Override // android.view.View.OnClickListener
                            public void onClick(View view2) {
                                AbstractErrorViewAdapter.this.requestRetry();
                            }
                        });
                        return;
                }
            } else {
                str = "android.settings.WIFI_SETTINGS";
            }
            view.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.search.StatusHandleHelper.AbstractErrorViewAdapter.4
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    Intent intent = new Intent(str);
                    intent.putExtra("use_dialog", true);
                    AbstractErrorViewAdapter.this.mContext.startActivity(intent);
                }
            });
        }

        public View getInfoItemView(View view, int i, InfoViewPosition infoViewPosition) {
            if (i == 3) {
                View inflate = this.mInflater.inflate(R.layout.search_set_cloud_back_up_item, (ViewGroup) null);
                inflate.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.search.StatusHandleHelper.AbstractErrorViewAdapter.5
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("cloud_guide_source", GalleryIntent$CloudGuideSource.SEARCH);
                        IntentUtil.guideToLoginXiaomiAccount(AbstractErrorViewAdapter.this.mContext, bundle);
                    }
                });
                return inflate;
            } else if (i == 4) {
                View inflate2 = this.mInflater.inflate(R.layout.search_set_cloud_back_up_item, (ViewGroup) null);
                inflate2.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.search.StatusHandleHelper.AbstractErrorViewAdapter.6
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        AbstractErrorViewAdapter.this.mContext.startActivity(new Intent("com.miui.gallery.cloud.provider.SYNC_SETTINGS"));
                    }
                });
                return inflate2;
            } else {
                return this.mInflater.inflate(R.layout.search_item_error_layout, (ViewGroup) null);
            }
        }

        public View getLoadingItemView(View view) {
            return this.mInflater.inflate(R.layout.search_item_loading_layout, (ViewGroup) null);
        }

        public String getInfoTitleForStatus(int i, InfoViewPosition infoViewPosition) {
            int i2;
            boolean z = infoViewPosition == InfoViewPosition.FULL_SCREEN;
            if (i == 1) {
                i2 = z ? R.string.search_connection_error : R.string.search_connection_error_and_set;
            } else if (i == 10) {
                i2 = R.string.search_syncing;
            } else if (i == 3) {
                i2 = z ? R.string.search_login_title_full_screen : R.string.search_login_title;
            } else if (i == 4) {
                i2 = z ? R.string.search_backup_full_screen_new : R.string.search_backup_title;
            } else if (i != 5) {
                switch (i) {
                    case 12:
                        i2 = R.string.search_empty_title;
                        break;
                    case 13:
                    case 14:
                        i2 = R.string.ai_album_requesting_title;
                        break;
                    default:
                        if (!z) {
                            i2 = R.string.search_error_and_retry;
                            break;
                        } else {
                            i2 = R.string.search_error;
                            break;
                        }
                }
            } else {
                i2 = R.string.search_net_error;
            }
            return this.mContext.getString(i2);
        }

        public int getIconResForStatus(int i, InfoViewPosition infoViewPosition) {
            if (infoViewPosition == InfoViewPosition.FULL_SCREEN) {
                return (i == 1 || i == 5) ? R.drawable.ic_pic_empty_internet : R.drawable.ic_pic_empty_search;
            } else if (infoViewPosition != InfoViewPosition.FOOTER) {
                return 0;
            } else {
                return R.drawable.search_connection_error_icon;
            }
        }

        public String getFixButtonTextForStatus(int i, InfoViewPosition infoViewPosition) {
            return this.mContext.getString(i != 1 ? i != 12 ? i != 3 ? i != 4 ? i != 5 ? R.string.search_retry : R.string.search_select_network : R.string.search_backup_now : R.string.search_login_now : R.string.ai_album_open_switch_button_title : R.string.search_set_network_connection);
        }

        public final String getInfoSubTitleForStatus(int i) {
            int i2;
            if (i == 3) {
                i2 = R.string.search_login_sub_title;
            } else if (i != 4) {
                return null;
            } else {
                i2 = R.string.search_backup_sub_title;
            }
            return this.mContext.getString(i2);
        }
    }
}
