package com.miui.gallery.adapter.itemmodel.trans;

import com.miui.epoxy.EpoxyModel;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/* loaded from: classes.dex */
public class ItemModelTransManager {
    public static final Map<Class<?>, TransDataToModelSolver> mSolver = new HashMap(24);

    public static ItemModelTransManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /* loaded from: classes.dex */
    public static class SingletonHolder {
        public static final ItemModelTransManager INSTANCE = new ItemModelTransManager();
    }

    public ItemModelTransManager() {
        register();
    }

    public <T> EpoxyModel<?> transDataToModel(T t) {
        TransDataToModelSolver transDataToModelSolver;
        if (t == null || (transDataToModelSolver = mSolver.get(t.getClass())) == null) {
            return null;
        }
        return transDataToModelSolver.transDataToModel(t);
    }

    public final void register() {
        final CommonItemModelTransSolver commonItemModelTransSolver = new CommonItemModelTransSolver();
        final OtherItemModelTransSolver otherItemModelTransSolver = new OtherItemModelTransSolver();
        Arrays.stream(otherItemModelTransSolver.supportTypes()).forEach(new Consumer<Class>() { // from class: com.miui.gallery.adapter.itemmodel.trans.ItemModelTransManager.1
            @Override // java.util.function.Consumer
            public void accept(Class cls) {
                ItemModelTransManager.mSolver.put(cls, otherItemModelTransSolver);
            }
        });
        Arrays.stream(commonItemModelTransSolver.supportTypes()).forEach(new Consumer<Class>() { // from class: com.miui.gallery.adapter.itemmodel.trans.ItemModelTransManager.2
            @Override // java.util.function.Consumer
            public void accept(Class cls) {
                ItemModelTransManager.mSolver.put(cls, commonItemModelTransSolver);
            }
        });
        DefaultLogger.d("ItemModelTransManager", "register success,count = %d", Integer.valueOf(mSolver.size()));
    }
}
