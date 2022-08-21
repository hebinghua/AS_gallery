package com.miui.gallery.share;

import android.text.TextUtils;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.share.AlbumShareUIManager;

/* loaded from: classes2.dex */
public class SharerPublicShareUiHandler extends PublicShareUiHandler {
    public static /* synthetic */ void $r8$lambda$tjIRoAIaSg54z19Oy4PwDVmIsFg(SharerPublicShareUiHandler sharerPublicShareUiHandler, Void r1, String str, int i, boolean z) {
        sharerPublicShareUiHandler.lambda$tryToRequestPublicUrl$0(r1, str, i, z);
    }

    public SharerPublicShareUiHandler(ShareAlbumBaseFragment shareAlbumBaseFragment, String str, CloudSharerMediaSet cloudSharerMediaSet) {
        super(shareAlbumBaseFragment, str, cloudSharerMediaSet);
    }

    @Override // com.miui.gallery.share.PublicShareUiHandler
    public void initPreferences() {
        super.initPreferences();
        tryToRequestPublicUrl();
    }

    public final void tryToRequestPublicUrl() {
        Future<?> future = this.mFuturePublic;
        if (future == null || future.isDone() || this.mFuturePublic.isCancelled()) {
            this.mFuturePublic = requestPublicUrl(this.mAlbumId, new AlbumShareUIManager.OnCompletionListener() { // from class: com.miui.gallery.share.SharerPublicShareUiHandler$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.share.AlbumShareUIManager.OnCompletionListener
                public final void onCompletion(Object obj, Object obj2, int i, boolean z) {
                    SharerPublicShareUiHandler.$r8$lambda$tjIRoAIaSg54z19Oy4PwDVmIsFg(SharerPublicShareUiHandler.this, (Void) obj, (String) obj2, i, z);
                }
            });
        }
    }

    public /* synthetic */ void lambda$tryToRequestPublicUrl$0(Void r1, String str, int i, boolean z) {
        if (!z && i == 0 && str != null) {
            updatePublicPreference(!TextUtils.isEmpty(str), str);
        }
    }

    public final Future<?> requestPublicUrl(String str, AlbumShareUIManager.OnCompletionListener<Void, String> onCompletionListener) {
        return AlbumShareUIManager.requestPublicUrlForSharerAsync(str, onCompletionListener);
    }
}
