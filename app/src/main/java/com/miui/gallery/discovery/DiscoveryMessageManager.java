package com.miui.gallery.discovery;

import android.content.Context;
import android.database.Cursor;
import android.util.SparseArray;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.loader.DiscoveryMessageLoader;
import com.miui.gallery.model.DiscoveryMessage;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.stat.a.j;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/* loaded from: classes.dex */
public class DiscoveryMessageManager {
    public static final String[] PROJECTION = {j.c, nexExportFormat.TAG_FORMAT_TYPE, "actionUri", "messageSource", "message", "title", "subTitle", j.k, "expireTime", "receiveTime", "updateTime", "triggerTime", "isConsumed", "extraData"};
    public final LoaderFactory mFactory;
    public final SparseArray<BaseMessageOperator> mMessageOperators;

    /* loaded from: classes.dex */
    public static class Holder {
        public static final DiscoveryMessageManager INSTANCE = new DiscoveryMessageManager();
    }

    /* loaded from: classes.dex */
    public interface MessageFilter {
        boolean accept(DiscoveryMessage discoveryMessage);
    }

    public DiscoveryMessageManager() {
        this.mMessageOperators = new SparseArray<>();
        this.mFactory = new LoaderFactory();
        registerMessageOperator(1, new RecentDiscoveryMessageOperator());
    }

    public static DiscoveryMessageManager getInstance() {
        return Holder.INSTANCE;
    }

    public final void registerMessageOperator(int i, BaseMessageOperator baseMessageOperator) {
        this.mMessageOperators.put(i, baseMessageOperator);
    }

    public final BaseMessageOperator findMessageOperatorByType(int i) {
        SparseArray<BaseMessageOperator> sparseArray = this.mMessageOperators;
        if (sparseArray != null) {
            return sparseArray.get(i);
        }
        return null;
    }

    public DiscoveryMessageLoader createLoader(Context context, int i) {
        return this.mFactory.createLoader(context, i);
    }

    public final Comparator<DiscoveryMessage> createComparatorByType(int i) {
        return new Comparator<DiscoveryMessage>() { // from class: com.miui.gallery.discovery.DiscoveryMessageManager.1
            @Override // java.util.Comparator
            public int compare(DiscoveryMessage discoveryMessage, DiscoveryMessage discoveryMessage2) {
                if (discoveryMessage.getPriority() != discoveryMessage2.getPriority()) {
                    return discoveryMessage.getPriority() > discoveryMessage2.getPriority() ? 1 : -1;
                } else if (discoveryMessage.getUpdateTime() == discoveryMessage2.getUpdateTime()) {
                    return 0;
                } else {
                    return discoveryMessage.getUpdateTime() > discoveryMessage2.getUpdateTime() ? -1 : 1;
                }
            }
        };
    }

    public final MessageFilter createFilterByType(int i) {
        return new MessageFilter() { // from class: com.miui.gallery.discovery.DiscoveryMessageManager.2
            @Override // com.miui.gallery.discovery.DiscoveryMessageManager.MessageFilter
            public boolean accept(DiscoveryMessage discoveryMessage) {
                return !discoveryMessage.isConsumed();
            }
        };
    }

    public ArrayList<DiscoveryMessage> loadMessage(Context context, int i) {
        if (context == null) {
            return null;
        }
        Cursor query = context.getContentResolver().query(GalleryContract.DiscoveryMessage.URI, PROJECTION, "(type & " + i + ") != 0", null, null);
        ArrayList<DiscoveryMessage> arrayList = new ArrayList<>();
        if (query != null) {
            try {
                try {
                    Comparator<DiscoveryMessage> createComparatorByType = createComparatorByType(i);
                    MessageFilter createFilterByType = createFilterByType(i);
                    while (query.moveToNext()) {
                        DiscoveryMessage.Builder builder = new DiscoveryMessage.Builder();
                        builder.consumed(query.getInt(12) > 0).type(query.getInt(1)).actionUri(query.getString(2)).message(query.getString(4)).expireTime(query.getLong(8)).triggerTime(query.getLong(11)).updateTime(query.getLong(10)).title(query.getString(5)).subTitle(query.getString(6)).priority(query.getInt(7)).receiveTime(query.getLong(9)).messageSource(query.getString(3)).messageId(query.getLong(0));
                        DiscoveryMessage build = builder.build();
                        wrapMessage(build, query.getString(13));
                        if (createFilterByType != null && createFilterByType.accept(build) && build.getMessage() != null) {
                            arrayList.add(build);
                        }
                    }
                    if (createComparatorByType != null) {
                        Collections.sort(arrayList, createComparatorByType);
                    }
                } catch (Exception e) {
                    DefaultLogger.e("DiscoveryMessageManager", "encounter error when load messages:\n%s", e.getMessage());
                    e.printStackTrace();
                }
            } finally {
                query.close();
            }
        }
        return arrayList;
    }

    public <T> void saveMessage(Context context, int i, T t) {
        BaseMessageOperator findMessageOperatorByType;
        if (context == null || t == null || (findMessageOperatorByType = findMessageOperatorByType(i)) == null) {
            return;
        }
        try {
            findMessageOperatorByType.saveMessage(context, t);
        } catch (ClassCastException e) {
            DefaultLogger.e("DiscoveryMessageManager", "Generic type saveParams doesn't match the generic type defined in concrete implementation of BaseMessageOperator");
            e.printStackTrace();
        }
    }

    public void markAsRead(Context context, DiscoveryMessage discoveryMessage) {
        BaseMessageOperator findMessageOperatorByType;
        if (context == null || discoveryMessage == null || discoveryMessage.getMessageId() <= 0 || (findMessageOperatorByType = findMessageOperatorByType(discoveryMessage.getType())) == null) {
            return;
        }
        findMessageOperatorByType.markMessageAsRead(context, discoveryMessage);
    }

    public void markAsReadAsync(Context context, DiscoveryMessage discoveryMessage) {
        if (context == null || discoveryMessage == null) {
            return;
        }
        ThreadManager.getMiscPool().submit(new MarkMsgAsReadJob(context, discoveryMessage));
    }

    /* loaded from: classes.dex */
    public static class LoaderFactory {
        public LoaderFactory() {
        }

        public DiscoveryMessageLoader createLoader(Context context, int i) {
            if (i != 1) {
                return null;
            }
            return new DiscoveryMessageLoader(context, 3);
        }
    }

    public final void wrapMessage(DiscoveryMessage discoveryMessage, String str) {
        BaseMessageOperator findMessageOperatorByType = findMessageOperatorByType(discoveryMessage.getType());
        if (findMessageOperatorByType != null) {
            findMessageOperatorByType.wrapMessage(discoveryMessage, str);
        }
    }

    /* loaded from: classes.dex */
    public class MarkMsgAsReadJob implements ThreadPool.Job<Void> {
        public WeakReference<Context> mContextRef;
        public List<DiscoveryMessage> mMessages;

        public MarkMsgAsReadJob(Context context, DiscoveryMessage... discoveryMessageArr) {
            this.mContextRef = new WeakReference<>(context);
            if (discoveryMessageArr == null || discoveryMessageArr.length <= 0) {
                return;
            }
            this.mMessages = Arrays.asList(discoveryMessageArr);
        }

        public List<DiscoveryMessage> getMessages() {
            return this.mMessages;
        }

        @Override // com.miui.gallery.concurrent.ThreadPool.Job
        /* renamed from: run  reason: collision with other method in class */
        public Void mo1807run(ThreadPool.JobContext jobContext) {
            Context context;
            if (jobContext == null || jobContext.isCancelled() || (context = this.mContextRef.get()) == null) {
                return null;
            }
            List<DiscoveryMessage> messages = getMessages();
            if (!BaseMiscUtil.isValid(messages)) {
                return null;
            }
            for (DiscoveryMessage discoveryMessage : messages) {
                DiscoveryMessageManager.this.markAsRead(context, discoveryMessage);
            }
            return null;
        }
    }
}
