package com.miui.gallery.model.datalayer.repository.album.common;

import com.miui.gallery.model.datalayer.repository.album.IBaseDataSource;
import io.reactivex.functions.Consumer;

/* loaded from: classes2.dex */
public class OnDataSourceFinishEventConsumer<T> implements Consumer<T> {
    public final IBaseDataSource[] mDataSources;
    public final int methodFlag;

    public OnDataSourceFinishEventConsumer(int i, IBaseDataSource[] iBaseDataSourceArr) {
        this.mDataSources = iBaseDataSourceArr;
        this.methodFlag = i;
    }

    @Override // io.reactivex.functions.Consumer
    public void accept(T t) throws Exception {
        IBaseDataSource[] iBaseDataSourceArr = this.mDataSources;
        if (iBaseDataSourceArr != null) {
            for (IBaseDataSource iBaseDataSource : iBaseDataSourceArr) {
                iBaseDataSource.onFinish(this.methodFlag, t);
            }
        }
    }
}
