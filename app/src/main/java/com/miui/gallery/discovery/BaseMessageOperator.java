package com.miui.gallery.discovery;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.miui.gallery.model.DiscoveryMessage;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.stat.a.j;

/* loaded from: classes.dex */
public abstract class BaseMessageOperator<SaveParams> {
    public static Uri sDiscoveryMessageUri = GalleryContract.DiscoveryMessage.URI;

    public abstract boolean doMarkMessageAsRead(Context context, DiscoveryMessage discoveryMessage);

    public abstract boolean doSaveMessage(Context context, SaveParams saveparams);

    public abstract void doWrapMessage(DiscoveryMessage discoveryMessage, String str);

    public abstract int getMessageType();

    public void wrapMessage(DiscoveryMessage discoveryMessage, String str) {
        if (discoveryMessage == null || str == null) {
            return;
        }
        doWrapMessage(discoveryMessage, str);
    }

    public boolean markMessageAsRead(Context context, DiscoveryMessage discoveryMessage) {
        return doMarkMessageAsRead(context, discoveryMessage);
    }

    public Cursor queryMessageByType(Context context) {
        return context.getContentResolver().query(sDiscoveryMessageUri, null, "type = ?", new String[]{String.valueOf(getMessageType())}, null);
    }

    public boolean saveMessage(Context context, SaveParams saveparams) {
        return doSaveMessage(context, saveparams);
    }

    public boolean doUpdateMessage(Context context, DiscoveryMessage discoveryMessage) {
        return new UpdateTask(context, discoveryMessage, null).run();
    }

    /* loaded from: classes.dex */
    public static abstract class CRUDTask<T> {
        public Context mContext;

        public abstract boolean execute(T t);

        /* renamed from: prepareData */
        public abstract T mo735prepareData();

        public CRUDTask(Context context) {
            this.mContext = context;
        }

        public boolean run() {
            T mo735prepareData = mo735prepareData();
            if (mo735prepareData == null) {
                DefaultLogger.e("BaseMessageOperator", "Can't execute task when prepared data was null");
                return false;
            }
            return execute(mo735prepareData);
        }
    }

    /* loaded from: classes.dex */
    public static class InsertTask extends CRUDTask<ContentValues> {
        public ContentValues mContentValues;

        public InsertTask(Context context, ContentValues contentValues) {
            super(context);
            this.mContentValues = contentValues;
        }

        @Override // com.miui.gallery.discovery.BaseMessageOperator.CRUDTask
        public boolean execute(ContentValues contentValues) {
            return doInsert(contentValues);
        }

        public final boolean doInsert(ContentValues contentValues) {
            return this.mContext.getContentResolver().insert(BaseMessageOperator.sDiscoveryMessageUri, contentValues) != null;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.miui.gallery.discovery.BaseMessageOperator.CRUDTask
        /* renamed from: prepareData */
        public ContentValues mo735prepareData() {
            return this.mContentValues;
        }
    }

    /* loaded from: classes.dex */
    public static class UpdateTask extends CRUDTask<ContentValues> {
        public ContentValues mContentValues;
        public DiscoveryMessage mMessage;

        public String getWhereClause() {
            return "_id = ?";
        }

        public UpdateTask(Context context, DiscoveryMessage discoveryMessage, ContentValues contentValues) {
            super(context);
            this.mMessage = discoveryMessage;
            this.mContentValues = contentValues;
        }

        public String[] getWhereArgs() {
            DiscoveryMessage discoveryMessage = this.mMessage;
            return discoveryMessage == null ? new String[]{String.valueOf(this.mContentValues.get(j.c))} : new String[]{String.valueOf(discoveryMessage.getMessageId())};
        }

        @Override // com.miui.gallery.discovery.BaseMessageOperator.CRUDTask
        public boolean execute(ContentValues contentValues) {
            return this.mContext.getContentResolver().update(BaseMessageOperator.sDiscoveryMessageUri, contentValues, getWhereClause(), getWhereArgs()) > 0;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.miui.gallery.discovery.BaseMessageOperator.CRUDTask
        /* renamed from: prepareData */
        public ContentValues mo735prepareData() {
            if (this.mContentValues == null) {
                ContentValues contentValues = new ContentValues();
                this.mContentValues = contentValues;
                contentValues.put(nexExportFormat.TAG_FORMAT_TYPE, Integer.valueOf(this.mMessage.getType()));
                this.mContentValues.put("receiveTime", Long.valueOf(this.mMessage.getReceiveTime()));
                this.mContentValues.put("updateTime", Long.valueOf(this.mMessage.getUpdateTime()));
                this.mContentValues.put("extraData", this.mMessage.getMessageDetail().toJson());
                this.mContentValues.put("triggerTime", Long.valueOf(this.mMessage.getTriggerTime()));
                this.mContentValues.put("isConsumed", Boolean.valueOf(this.mMessage.isConsumed()));
                this.mContentValues.put("expireTime", Long.valueOf(this.mMessage.getExpireTime()));
                this.mContentValues.put("actionUri", this.mMessage.getActionUri());
                this.mContentValues.put("messageSource", this.mMessage.getMessageSource());
                this.mContentValues.put(j.k, Integer.valueOf(this.mMessage.getPriority()));
                this.mContentValues.put("subTitle", this.mMessage.getSubTitle());
                this.mContentValues.put("title", this.mMessage.getTitle());
                this.mContentValues.put("message", this.mMessage.getMessage());
            }
            return this.mContentValues;
        }
    }
}
