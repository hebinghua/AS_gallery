package com.miui.gallery.ui.album.picker;

import android.content.Context;
import com.miui.gallery.model.datalayer.config.ModelManager;
import com.miui.gallery.model.datalayer.repository.AbstractAlbumRepository;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/* loaded from: classes2.dex */
public class CrossUserAlbumRepositoryModelInstance implements ModelManager.ModelInstanceCallback<AbstractAlbumRepository> {
    public Context mContext;

    public CrossUserAlbumRepositoryModelInstance(Context context) {
        this.mContext = context;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.miui.gallery.model.datalayer.config.ModelManager.ModelInstanceCallback
    /* renamed from: newModel */
    public AbstractAlbumRepository mo1602newModel(Class cls) {
        try {
            Constructor constructor = cls.getConstructor(Context.class);
            constructor.setAccessible(true);
            return (AbstractAlbumRepository) constructor.newInstance(this.mContext);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (InstantiationException e2) {
            e2.printStackTrace();
            return null;
        } catch (NoSuchMethodException e3) {
            e3.printStackTrace();
            return null;
        } catch (InvocationTargetException e4) {
            e4.printStackTrace();
            return null;
        }
    }
}
