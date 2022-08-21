package com.miui.gallery.share;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.common.collect.Lists;
import com.miui.gallery.R;
import com.miui.gallery.cloud.GalleryCloudUtils;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.util.StaticContext;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class ShareUserAdapterBase extends BaseAdapter {
    public static int sIconEffectSize;
    public final String mCreatorId;
    public RequestOptions mDefaultRequestOptions;
    public final LayoutInflater mInflater;
    public final int mItemLayoutId;
    public final List<CloudUserCacheEntry> mShareUsers = Lists.newArrayList();

    public abstract int getAbsentSharerIcon(CloudUserCacheEntry cloudUserCacheEntry);

    public abstract int getDefaultIcon(CloudUserCacheEntry cloudUserCacheEntry);

    public abstract int getIconEffect();

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return 0L;
    }

    public ShareUserAdapterBase(Context context, String str, int i) {
        this.mInflater = LayoutInflater.from(context);
        this.mCreatorId = str;
        this.mItemLayoutId = i;
        if (sIconEffectSize == 0) {
            sIconEffectSize = (int) context.getResources().getDimension(R.dimen.baby_album_sharer_icon_size);
        }
        this.mDefaultRequestOptions = GlideOptions.peopleFaceOf().mo972placeholder(R.drawable.album_sharer_default).mo954error(R.drawable.album_sharer_default).mo956fallback(R.drawable.album_sharer_default).circleCrop().autoClone();
    }

    public void setShareUsers(List<CloudUserCacheEntry> list) {
        this.mShareUsers.clear();
        this.mShareUsers.addAll(list);
        notifyDataSetChanged();
    }

    @Override // android.widget.Adapter
    /* renamed from: getItem */
    public CloudUserCacheEntry mo1387getItem(int i) {
        if (i < this.mShareUsers.size()) {
            return this.mShareUsers.get(i);
        }
        return null;
    }

    /* loaded from: classes2.dex */
    public class ViewHolder {
        public final ImageView mIcon;
        public final ImageView mIconEffect;
        public final ImageView mMark;
        public final TextView mText;

        public ViewHolder(CloudUserCacheEntry cloudUserCacheEntry, View view) {
            ImageView imageView = (ImageView) view.findViewById(R.id.icon);
            this.mIcon = imageView;
            ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
            ShareUserAdapterBase.this.setIconSize(cloudUserCacheEntry, layoutParams);
            view.getLayoutParams().width = layoutParams.width;
            this.mMark = (ImageView) view.findViewById(R.id.mark);
            this.mText = (TextView) view.findViewById(R.id.text);
            this.mIconEffect = (ImageView) view.findViewById(R.id.ic_effect);
        }
    }

    public String getDisplayName(Resources resources, UserInfo userInfo, CloudUserCacheEntry cloudUserCacheEntry) {
        if (userInfo != null) {
            if (TextUtils.equals(userInfo.getUserId(), GalleryCloudUtils.getAccountName())) {
                return resources.getString(R.string.user_alias_self);
            }
            return userInfo.getDisplayName();
        } else if (cloudUserCacheEntry != null) {
            return cloudUserCacheEntry.mUserId;
        } else {
            return null;
        }
    }

    public void setIconSize(CloudUserCacheEntry cloudUserCacheEntry, ViewGroup.LayoutParams layoutParams) {
        int i = sIconEffectSize;
        layoutParams.width = i;
        layoutParams.height = i;
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        CloudUserCacheEntry mo1387getItem = mo1387getItem(i);
        if (view == null) {
            view = this.mInflater.inflate(this.mItemLayoutId, viewGroup, false);
            view.setTag(new ViewHolder(mo1387getItem, view));
        }
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        if (mo1387getItem != null && !TextUtils.isEmpty(mo1387getItem.mUserId)) {
            UserInfo value = UserInfoCache.getInstance().getValue(mo1387getItem.mUserId);
            viewHolder.mText.setText(getDisplayName(view.getResources(), value, mo1387getItem));
            if (value != null) {
                String miliaoIconUrl150 = value.getMiliaoIconUrl150();
                if (TextUtils.isEmpty(miliaoIconUrl150)) {
                    viewHolder.mIcon.setImageResource(getDefaultIcon(mo1387getItem));
                } else {
                    Glide.with(viewHolder.mIcon).mo985asBitmap().mo963load(miliaoIconUrl150).mo946apply((BaseRequestOptions<?>) this.mDefaultRequestOptions).into(viewHolder.mIcon);
                }
            } else {
                viewHolder.mIcon.setImageResource(getDefaultIcon(mo1387getItem));
            }
            if (TextUtils.equals(mo1387getItem.mUserId, this.mCreatorId)) {
                viewHolder.mMark.setImageResource(R.drawable.cloud_album_creator_mark);
                view.setClickable(false);
            }
        } else {
            viewHolder.mText.setText(getDisplayName(view.getResources(), null, mo1387getItem));
            viewHolder.mIcon.setImageResource(i == 0 ? getDefaultIcon(mo1387getItem) : getAbsentSharerIcon(mo1387getItem));
        }
        if (TextUtils.isEmpty(viewHolder.mText.getText())) {
            viewHolder.mIcon.setFocusable(true);
            viewHolder.mIcon.setFocusableInTouchMode(true);
            viewHolder.mIcon.setContentDescription(StaticContext.sGetAndroidContext().getResources().getString(R.string.add_sharer));
        }
        viewHolder.mIconEffect.setImageResource(getIconEffect());
        return view;
    }
}
