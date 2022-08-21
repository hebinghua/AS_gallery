package com.miui.gallery.ui;

import android.content.Context;
import android.view.View;
import com.bumptech.glide.request.RequestOptions;
import com.miui.gallery.discovery.RecentDiscoveryMessageOperator;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.model.DiscoveryMessage;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.SwitchView;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Marker;

/* loaded from: classes2.dex */
public class DiscoveryDot extends DiscoveryWidget {
    public Pattern mPatter;
    public int mPhotoCount;
    public SwitchView mSwitchView;

    @Override // com.miui.gallery.widget.PanelItem
    public int getPriority() {
        return 0;
    }

    @Override // com.miui.gallery.widget.PanelItem
    public View getView() {
        return null;
    }

    public DiscoveryDot(Context context, SwitchView switchView) {
        super(context);
        this.mSwitchView = switchView;
        this.mPatter = Pattern.compile("\\d+");
        ensureView();
    }

    public void ensureView() {
        this.mSwitchView.initDotView();
    }

    @Override // com.miui.gallery.ui.DiscoveryWidget
    public void setMessages(List<DiscoveryMessage> list) {
        this.mMessages = list;
        if (BaseMiscUtil.isValid(list)) {
            DiscoveryMessage discoveryMessage = list.get(0);
            String message = discoveryMessage.getMessage();
            String[] strArr = null;
            DiscoveryMessage.BaseMessageDetail messageDetail = discoveryMessage.getMessageDetail();
            if (messageDetail instanceof RecentDiscoveryMessageOperator.RecentMessageDetail) {
                strArr = ((RecentDiscoveryMessageOperator.RecentMessageDetail) messageDetail).getThumbUrls();
            }
            bindView(message, strArr, GlideOptions.microThumbOf());
            return;
        }
        this.mSwitchView.showOrHideDotView(false);
        DefaultLogger.i("ActionBarDiscoveryWidget", "ActionBarDiscoveryWidget:Massages is invalid");
    }

    public void bindView(String str, String[] strArr, RequestOptions requestOptions) {
        Matcher matcher = this.mPatter.matcher(str);
        this.mPhotoCount = strArr != null ? strArr.length : 0;
        if (matcher.find()) {
            this.mPhotoCount = Math.max(Integer.parseInt(matcher.group()), this.mPhotoCount);
        }
        setPhotoCount();
    }

    public void setPhotoCount() {
        boolean z = false;
        if (this.mPhotoCount > 0) {
            if (!this.mShowEnable) {
                this.mSwitchView.showOrHideDotView(false);
                markAsRead(getFirstMessage());
                this.mPhotoCount = 0;
            } else {
                StringBuilder sb = new StringBuilder();
                int i = this.mPhotoCount;
                if (i > 99) {
                    sb.append(99);
                    sb.append(Marker.ANY_NON_NULL_MARKER);
                } else if (i >= 10) {
                    sb.append(i);
                } else {
                    sb.append(i);
                    this.mSwitchView.showOrHideDotView(true);
                    this.mSwitchView.setDotContent(sb.toString(), z);
                }
                z = true;
                this.mSwitchView.showOrHideDotView(true);
                this.mSwitchView.setDotContent(sb.toString(), z);
            }
        } else {
            this.mPhotoCount = 0;
            this.mSwitchView.showOrHideDotView(false);
        }
        GalleryPreferences.HomePage.setHomePageDiscoverPhotos(this.mPhotoCount);
    }

    public int getPhotoCount() {
        return this.mPhotoCount;
    }
}
