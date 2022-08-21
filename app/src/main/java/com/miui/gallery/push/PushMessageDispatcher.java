package com.miui.gallery.push;

import android.content.Context;
import android.text.TextUtils;
import ch.qos.logback.core.joran.action.Action;
import com.miui.gallery.cloud.AccountCache;
import com.miui.gallery.push.messagehandler.MessageHandler;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.Encode;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.mipush.sdk.MiPushMessage;
import java.util.HashMap;

/* loaded from: classes2.dex */
public class PushMessageDispatcher {
    public static void dispatch(Context context, MiPushMessage miPushMessage) {
        String content = miPushMessage.getContent();
        if (TextUtils.isEmpty(content)) {
            DefaultLogger.e("PushMessageDispatcher", "Message content is empty");
            SamplingStatHelper.recordCountEvent("mipush", "message_content_is_empty");
            return;
        }
        GalleryPushMessage fromJson = GalleryPushMessage.fromJson(content);
        if (fromJson == null) {
            DefaultLogger.e("PushMessageDispatcher", "Parse message content failed: %s", content);
            SamplingStatHelper.recordCountEvent("mipush", "message_content_parse_failed");
            return;
        }
        PushConstants$MessageScope scope = PushConstants$MessageScope.getScope(fromJson.getMessageScope());
        if (scope != PushConstants$MessageScope.RELEASE && (scope != PushConstants$MessageScope.DEBUG || !BaseBuildUtil.IS_DEBUG_BUILD)) {
            DefaultLogger.e("PushMessageDispatcher", "Message scope does not match: %s", fromJson.getMessageScope());
            HashMap hashMap = new HashMap();
            hashMap.put(Action.SCOPE_ATTRIBUTE, fromJson.getMessageScope());
            SamplingStatHelper.recordCountEvent("mipush", "unknown_message_scope_doesnt_match", hashMap);
            return;
        }
        PushConstants$MessageType type = PushConstants$MessageType.getType(fromJson.getType());
        if (type == null) {
            DefaultLogger.e("PushMessageDispatcher", "Unknown message type: %s", fromJson.getType());
            HashMap hashMap2 = new HashMap();
            hashMap2.put(nexExportFormat.TAG_FORMAT_TYPE, fromJson.getType());
            SamplingStatHelper.recordCountEvent("mipush", "unknown_message_type", hashMap2);
            return;
        }
        String str = null;
        String userAccount = TextUtils.isEmpty(miPushMessage.getUserAccount()) ? null : miPushMessage.getUserAccount();
        if (AccountCache.getAccount() != null) {
            str = Encode.SHA1Encode(AccountCache.getAccount().name.getBytes());
        }
        if (userAccount != null && !TextUtils.equals(userAccount, str)) {
            DefaultLogger.e("PushMessageDispatcher", "UserAccount doesn't match, skip handle");
            SamplingStatHelper.recordCountEvent("mipush", "push_user_account_doesnt_match");
            return;
        }
        String businessModule = fromJson.getBusinessModule();
        MessageHandler create = MessageHandlerFactory.create(businessModule);
        if (create == null) {
            DefaultLogger.e("PushMessageDispatcher", "MessageHandler is undefined: %s", businessModule);
            return;
        }
        int i = AnonymousClass1.$SwitchMap$com$miui$gallery$push$PushConstants$MessageType[type.ordinal()];
        if (i == 1) {
            create.handlePull(context, fromJson);
        } else if (i != 2) {
        } else {
            create.handleDirect(context, fromJson);
        }
    }

    /* renamed from: com.miui.gallery.push.PushMessageDispatcher$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$push$PushConstants$MessageType;

        static {
            int[] iArr = new int[PushConstants$MessageType.values().length];
            $SwitchMap$com$miui$gallery$push$PushConstants$MessageType = iArr;
            try {
                iArr[PushConstants$MessageType.SYNC.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$push$PushConstants$MessageType[PushConstants$MessageType.DIRECT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }
}
