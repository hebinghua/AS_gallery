package com.miui.gallery.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.request.RequestOptions;
import com.miui.gallery.R;
import com.miui.gallery.activity.HomePageActivity;
import com.miui.gallery.discovery.RecentDiscoveryMessageOperator;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.model.DiscoveryMessage;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes2.dex */
public class ActionBarDiscoveryWidget extends DiscoveryWidget {
    public HomePageActivity.HomeTabActionBarHelper mActionBarHelper;
    public View mDiscoveryView;
    public Pattern mPatter;
    public TextView mPhotoCount;
    public ImageView mThumbView;
    public boolean mValidMessage;
    public boolean mVisibleEnable;

    @Override // com.miui.gallery.widget.PanelItem
    public int getPriority() {
        return 0;
    }

    public ActionBarDiscoveryWidget(Context context, HomePageActivity.HomeTabActionBarHelper homeTabActionBarHelper) {
        super(context);
        this.mActionBarHelper = homeTabActionBarHelper;
        this.mPatter = Pattern.compile("\\d+");
        ensureView();
    }

    public void ensureView() {
        View inflate = ((ViewStub) this.mActionBarHelper.getStartView().findViewById(R.id.discovery_view)).inflate();
        this.mDiscoveryView = inflate;
        this.mThumbView = (ImageView) inflate.findViewById(R.id.photo);
        this.mPhotoCount = (TextView) this.mDiscoveryView.findViewById(R.id.photo_count);
        this.mDiscoveryView.setOnClickListener(this);
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
            this.mValidMessage = true;
            bindView(message, strArr, GlideOptions.microThumbOf());
        } else {
            this.mValidMessage = false;
            DefaultLogger.i("ActionBarDiscoveryWidget", "ActionBarDiscoveryWidget:Massages is invalid");
        }
        invalidateIcon();
    }

    public void bindView(String str, String[] strArr, RequestOptions requestOptions) {
        Matcher matcher = this.mPatter.matcher(str);
        int length = strArr != null ? strArr.length : 0;
        if (matcher.find()) {
            length = Math.max(Integer.parseInt(matcher.group()), length);
        }
        int min = Math.min(length, 99);
        setPhotoCount(min);
        if (min > 0 && strArr != null && strArr.length > 0) {
            this.mDiscoveryView.setContentDescription(str);
            bindImage(this.mThumbView, strArr[0], requestOptions);
        }
        this.mDiscoveryView.requestLayout();
        DefaultLogger.i("ActionBarDiscoveryWidget", "ActionBarDiscoveryWidget:Massages is valid and photoCount=%s", Integer.valueOf(min));
    }

    public void setPhotoCount(int i) {
        if (i > 0) {
            this.mPhotoCount.setVisibility(0);
            this.mPhotoCount.setText(String.valueOf(i));
            return;
        }
        this.mPhotoCount.setVisibility(8);
    }

    public void refreshIconVisibility(boolean z) {
        this.mVisibleEnable = z;
        invalidateIcon();
    }

    public final void invalidateIcon() {
        if (this.mVisibleEnable && this.mValidMessage) {
            this.mDiscoveryView.setVisibility(0);
        } else {
            this.mDiscoveryView.setVisibility(8);
        }
    }

    @Override // com.miui.gallery.widget.PanelItem
    public View getView() {
        ensureView();
        return this.mDiscoveryView;
    }
}
