package com.miui.gallery.assistant.recommend;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.miui.gallery.R;
import com.miui.gallery.cloudcontrol.RecommendItem;
import com.miui.gallery.listener.SingleClickListener;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.MiscUtil;
import com.miui.gallery.util.anim.FolmeUtil;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/* loaded from: classes.dex */
public class RecommendFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public Context mContext;
    public ArrayList<RecommendItem> mRecommendItems = new ArrayList<>();

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        return i == 0 ? 1 : 2;
    }

    public RecommendFragmentAdapter(Context context) {
        this.mContext = context;
    }

    public void changeRecommendItems(ArrayList<RecommendItem> arrayList) {
        this.mRecommendItems = arrayList;
        notifyDataSetChanged();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder */
    public RecyclerView.ViewHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == 1) {
            return new BannerViewHolder(viewGroup);
        }
        return new ItemViewHolder(viewGroup);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        ArrayList<RecommendItem> arrayList = this.mRecommendItems;
        if (arrayList != null) {
            return arrayList.size();
        }
        return 0;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        ArrayList<RecommendItem> arrayList = this.mRecommendItems;
        if (arrayList == null || arrayList.size() == 0) {
            return;
        }
        final RecommendItem recommendItem = this.mRecommendItems.get(i);
        if (getItemViewType(i) == 1) {
            Glide.with(viewHolder.itemView).mo990load(constructBannerUrl(recommendItem.getIconUrl())).mo972placeholder((int) R.drawable.recommend_banner_loading).mo954error(R.drawable.recommend_banner_loading).mo950diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(((BannerViewHolder) viewHolder).mIcon);
            return;
        }
        int i2 = i % 7;
        Locale locale = Locale.US;
        Object[] objArr = new Object[1];
        if (i2 == 0) {
            i2 = 7;
        }
        objArr[0] = Integer.valueOf(i2);
        int identifier = this.mContext.getResources().getIdentifier(String.format(locale, "recommend_item%d_loading", objArr), "drawable", "com.miui.gallery");
        ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;
        Glide.with(viewHolder.itemView).mo990load(constructBannerUrl(recommendItem.getIconUrl())).mo972placeholder(identifier).mo954error(identifier).mo950diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).mo970override(Integer.MIN_VALUE).mo951dontTransform().into(itemViewHolder.mIcon);
        itemViewHolder.mTitle.setText(recommendItem.getName());
        itemViewHolder.mSubtitle.setText(recommendItem.getDescription());
        FolmeUtil.setDefaultTouchAnim(viewHolder.itemView, null, false, false, true);
        viewHolder.itemView.setOnClickListener(new SingleClickListener() { // from class: com.miui.gallery.assistant.recommend.RecommendFragmentAdapter.1
            @Override // com.miui.gallery.listener.SingleClickListener
            public void onSingleClick(View view) {
                HashMap hashMap = new HashMap();
                hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, recommendItem.getName());
                SamplingStatHelper.recordCountEvent("assistant", "assistant_discover_item_clicked", hashMap);
                Intent intent = new Intent("com.miui.gallery.action.VIEW_WEB_RECOMMEND");
                intent.putExtra(MapBundleKey.MapObjKey.OBJ_URL, RecommendFragmentAdapter.this.constructLandingUrl(recommendItem.getLandingUrl()));
                intent.putExtra("from_recommend", true);
                RecommendFragmentAdapter.this.mContext.startActivity(intent);
            }
        });
        if (i != this.mRecommendItems.size() - 1) {
            return;
        }
        itemViewHolder.mGuideline.setVisibility(4);
    }

    public final String constructLandingUrl(String str) {
        Uri parse = Uri.parse(str);
        return parse.buildUpon().appendQueryParameter("version", String.valueOf(MiscUtil.getAppVersionCode())).appendQueryParameter("language", String.format("%s_%s", Locale.getDefault().getLanguage(), Locale.getDefault().getCountry())).build().toString();
    }

    public final String constructBannerUrl(String str) {
        String replaceFirst;
        Uri parse = Uri.parse(str);
        String path = parse.getPath();
        if (Locale.getDefault().getLanguage().equals("en")) {
            replaceFirst = path.replaceFirst("x*hdpi", "en/" + this.mContext.getResources().getString(R.string.which_hdpi));
        } else {
            replaceFirst = path.replaceFirst("x*hdpi", this.mContext.getResources().getString(R.string.which_hdpi));
        }
        return new Uri.Builder().scheme(parse.getScheme()).authority(parse.getAuthority()).path(replaceFirst).query(parse.getQuery()).fragment(parse.getFragment()).build().toString();
    }

    /* loaded from: classes.dex */
    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public View mGuideline;
        public ImageView mIcon;
        public TextView mSubtitle;
        public TextView mTitle;

        public ItemViewHolder(ViewGroup viewGroup) {
            super(LayoutInflater.from(RecommendFragmentAdapter.this.mContext).inflate(R.layout.recommend_item_view, viewGroup, false));
            this.mIcon = (ImageView) this.itemView.findViewById(R.id.icon);
            this.mTitle = (TextView) this.itemView.findViewById(R.id.title);
            this.mSubtitle = (TextView) this.itemView.findViewById(R.id.subtitle);
            this.mGuideline = this.itemView.findViewById(R.id.guide_line);
        }
    }

    /* loaded from: classes.dex */
    public class BannerViewHolder extends RecyclerView.ViewHolder {
        public ImageView mIcon;

        public BannerViewHolder(ViewGroup viewGroup) {
            super(LayoutInflater.from(RecommendFragmentAdapter.this.mContext).inflate(R.layout.recommend_banner_view, viewGroup, false));
            this.mIcon = (ImageView) this.itemView.findViewById(R.id.banner);
        }
    }
}
