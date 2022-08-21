package com.miui.gallery.push;

import com.miui.gallery.GalleryApp;
import com.miui.gallery.agreement.AgreementsUtils;
import com.miui.gallery.pendingtask.base.PendingTask;
import com.miui.gallery.push.messagehandler.MessageHandler;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.ParcelableUtil;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class PendingPullTask extends PendingTask<GalleryPushMessage> {
    @Override // com.miui.gallery.pendingtask.base.PendingTask
    public int getNetworkType() {
        return 1;
    }

    public PendingPullTask(int i) {
        super(i);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.miui.gallery.pendingtask.base.PendingTask
    /* renamed from: parseData */
    public GalleryPushMessage mo1252parseData(byte[] bArr) throws Exception {
        return (GalleryPushMessage) ParcelableUtil.unmarshall(bArr, GalleryPushMessage.CREATOR);
    }

    @Override // com.miui.gallery.pendingtask.base.PendingTask
    public byte[] wrapData(GalleryPushMessage galleryPushMessage) throws Exception {
        return ParcelableUtil.marshall(galleryPushMessage);
    }

    @Override // com.miui.gallery.pendingtask.base.PendingTask
    public boolean process(GalleryPushMessage galleryPushMessage) throws Exception {
        if (!AgreementsUtils.isNetworkingAgreementAccepted()) {
            DefaultLogger.w("PendingPullTask", "CTA not confirmed, retry later");
            return true;
        } else if (!BaseNetworkUtils.isNetworkConnected()) {
            DefaultLogger.w("PendingPullTask", "Network is disconnected, retry later");
            return true;
        } else {
            String businessModule = galleryPushMessage.getBusinessModule();
            MessageHandler create = MessageHandlerFactory.create(businessModule);
            if (create != null) {
                create.handlePull(GalleryApp.sGetAndroidContext(), galleryPushMessage);
                return false;
            }
            DefaultLogger.e("PendingPullTask", "MessageHandler is undefined: %s", businessModule);
            return false;
        }
    }
}
