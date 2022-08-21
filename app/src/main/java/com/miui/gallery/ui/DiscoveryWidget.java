package com.miui.gallery.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.miui.gallery.discovery.DiscoveryMessageManager;
import com.miui.gallery.glide.load.model.GalleryModel;
import com.miui.gallery.model.DiscoveryMessage;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.SyncDownloadManager;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class DiscoveryWidget extends SyncDownloadManager.HomePageWidget implements View.OnClickListener {
    public boolean mDiscoveryMessageClicked;
    public List<DiscoveryMessage> mMessages;
    public boolean mShowEnable;

    @Override // com.miui.gallery.widget.PanelItem
    public void setAlpha(float f) {
    }

    @Override // com.miui.gallery.widget.PanelItem
    public void setClickable(boolean z) {
    }

    @Override // com.miui.gallery.widget.PanelItem
    public void setEnable(boolean z) {
    }

    public abstract void setMessages(List<DiscoveryMessage> list);

    public DiscoveryWidget(Context context) {
        super(context);
    }

    public void setShowEnable(boolean z) {
        this.mShowEnable = z;
    }

    public void bindImage(ImageView imageView, String str, RequestOptions requestOptions) {
        Glide.with(imageView).mo985asBitmap().mo962load(GalleryModel.of(str)).mo946apply((BaseRequestOptions<?>) requestOptions).into((RequestBuilder<Bitmap>) new BitmapImageViewTarget(imageView).clearOnDetach());
    }

    public DiscoveryMessage getFirstMessage() {
        if (BaseMiscUtil.isValid(this.mMessages)) {
            return this.mMessages.get(0);
        }
        return null;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        DiscoveryMessage firstMessage = getFirstMessage();
        if (firstMessage == null) {
            return;
        }
        try {
            Intent intent = new Intent("android.intent.action.VIEW");
            Uri parse = Uri.parse(firstMessage.getActionUri());
            if (firstMessage.getType() == 1) {
                intent.setPackage(getContext().getPackageName());
                parse = parse.buildUpon().appendQueryParameter("source", "home_page").build();
            }
            intent.setData(parse);
            getContext().startActivity(intent);
        } catch (Exception e) {
            DefaultLogger.e("DiscoveryBar", "Wrong intent action uri: %s", firstMessage.getActionUri());
            e.printStackTrace();
        }
        markAsRead(firstMessage);
    }

    public void markAsRead(DiscoveryMessage discoveryMessage) {
        if (discoveryMessage == null) {
            return;
        }
        DiscoveryMessageManager.getInstance().markAsReadAsync(getContext(), discoveryMessage);
        recordMessageClickedTime(discoveryMessage);
        this.mDiscoveryMessageClicked = true;
    }

    public final String formatMessageClickTime(long j) {
        long currentTimeMillis = (System.currentTimeMillis() - j) / 60000;
        return currentTimeMillis < 1 ? "< 1 minute" : currentTimeMillis <= 3 ? "1-3 minutes" : currentTimeMillis <= 10 ? "4-10 minutes" : "> 10 minutes";
    }

    public final void recordMessageClickedTime(DiscoveryMessage discoveryMessage) {
        HashMap hashMap = new HashMap();
        hashMap.put("elapse_time", formatMessageClickTime(discoveryMessage.getUpdateTime()));
        SamplingStatHelper.recordCountEvent("quickly_discovery", "quickly_discovery_message_click_time", hashMap);
    }
}
