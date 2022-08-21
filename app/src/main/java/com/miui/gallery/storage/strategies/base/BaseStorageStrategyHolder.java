package com.miui.gallery.storage.strategies.base;

import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.documentfile.provider.DocumentFile;
import androidx.documentfile.provider.DocumentFileHandleRecordWrapper;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.storage.exceptions.StorageUnsupportedOperationException;
import com.miui.gallery.storage.strategies.IOrderedStorageStrategyHolder;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.storage.strategies.IStorageStrategy;
import com.miui.gallery.storage.utils.StrategyOrder;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: BaseStorageStrategyHolder.kt */
/* loaded from: classes2.dex */
public final class BaseStorageStrategyHolder implements IOrderedStorageStrategyHolder {
    public static final Companion Companion = new Companion(null);
    public final List<IStorageStrategy> strategies = new LinkedList();

    @Override // com.miui.gallery.storage.strategies.IStorageStrategyHolder
    public void addAt(IStorageStrategy strategy, int i) {
        Intrinsics.checkNotNullParameter(strategy, "strategy");
        this.strategies.add(i, strategy);
    }

    @Override // com.miui.gallery.storage.strategies.IStorageStrategyHolder
    public void append(IStorageStrategy strategy) {
        Intrinsics.checkNotNullParameter(strategy, "strategy");
        this.strategies.add(strategy);
    }

    @Override // com.miui.gallery.storage.strategies.IOrderedStorageStrategyHolder
    public DocumentFile getDocumentFile(String strategyOrder, String str, IStoragePermissionStrategy.Permission permission, Bundle bundle) {
        Intrinsics.checkNotNullParameter(strategyOrder, "strategyOrder");
        Intrinsics.checkNotNullParameter(permission, "permission");
        if (str == null) {
            if (IStorageStrategy.DEBUG) {
                DefaultLogger.w("StorageStrategy[version=1-0-0]", "[getDocumentFile] Path is null");
            }
            return null;
        }
        List<IStorageStrategy> order = Companion.order(this.strategies, strategyOrder);
        String str2 = "path: [" + ((Object) str) + "], permission: [" + permission + "], extras: [" + bundle + ']';
        if (IStorageStrategy.DEBUG) {
            DefaultLogger.v("StorageStrategy[version=1-0-0]", "[getDocumentFile] description:[" + str2 + ']');
            DefaultLogger.verbosePrintStackMsg("StorageStrategy[version=1-0-0]");
        }
        LinkedList linkedList = new LinkedList();
        DocumentFile documentFile = null;
        for (IStorageStrategy iStorageStrategy : order) {
            if (IStorageStrategy.DEBUG) {
                DefaultLogger.v("StorageStrategy[version=1-0-0]", "try [getDocumentFile] with [" + ((Object) iStorageStrategy.getClass().getSimpleName()) + "] start");
            }
            try {
                documentFile = iStorageStrategy.getDocumentFile(str, permission, bundle);
            } catch (StorageUnsupportedOperationException unused) {
                if (IStorageStrategy.DEBUG) {
                    DefaultLogger.v("StorageStrategy[version=1-0-0]", "try [getDocumentFile] with [" + ((Object) iStorageStrategy.getClass().getSimpleName()) + "] failed, [StorageUnsupportedOperationException]");
                }
            } catch (Throwable th) {
                linkedList.add(th);
            }
            if (Objects.nonNull(documentFile)) {
                break;
            }
        }
        if (IStorageStrategy.DEBUG && !Objects.nonNull(documentFile)) {
            Iterator it = linkedList.iterator();
            while (it.hasNext()) {
                DefaultLogger.e("StorageStrategy[version=1-0-0]", (Throwable) it.next());
            }
        }
        if (IStorageStrategy.DEBUG) {
            DefaultLogger.v("StorageStrategy[version=1-0-0]", String.valueOf(documentFile));
        }
        if (documentFile != null) {
            return DocumentFileHandleRecordWrapper.wrap(documentFile, bundle, str);
        }
        return null;
    }

    @Override // com.miui.gallery.storage.strategies.IStorageStrategyHolder, com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    public IStoragePermissionStrategy.PermissionResult checkPermission(String str, IStoragePermissionStrategy.Permission type) {
        boolean z;
        Intrinsics.checkNotNullParameter(type, "type");
        List<IStorageStrategy> list = this.strategies;
        String str2 = "path: [" + ((Object) str) + "], type: [" + type + ']';
        if (IStorageStrategy.DEBUG) {
            DefaultLogger.v("StorageStrategy[version=1-0-0]", "[checkPermission] description:[" + str2 + ']');
            DefaultLogger.verbosePrintStackMsg("StorageStrategy[version=1-0-0]");
        }
        LinkedList linkedList = new LinkedList();
        Iterator<IStorageStrategy> it = list.iterator();
        IStoragePermissionStrategy.PermissionResult permissionResult = null;
        while (true) {
            z = false;
            if (!it.hasNext()) {
                break;
            }
            IStorageStrategy next = it.next();
            if (IStorageStrategy.DEBUG) {
                DefaultLogger.v("StorageStrategy[version=1-0-0]", "try [checkPermission] with [" + ((Object) next.getClass().getSimpleName()) + "] start");
            }
            try {
                IStoragePermissionStrategy.PermissionResult checkPermission = next.checkPermission(str, type);
                if (permissionResult == null) {
                    permissionResult = checkPermission;
                } else if (checkPermission != null) {
                    permissionResult.granted |= checkPermission.granted;
                    permissionResult.applicable = checkPermission.applicable | permissionResult.applicable;
                }
            } catch (StorageUnsupportedOperationException unused) {
                if (IStorageStrategy.DEBUG) {
                    DefaultLogger.v("StorageStrategy[version=1-0-0]", "try [checkPermission] with [" + ((Object) next.getClass().getSimpleName()) + "] failed, [StorageUnsupportedOperationException]");
                }
            } catch (Throwable th) {
                linkedList.add(th);
            }
            if (permissionResult == null ? false : permissionResult.granted) {
                break;
            }
        }
        if (IStorageStrategy.DEBUG) {
            if (permissionResult != null) {
                z = permissionResult.granted;
            }
            if (!z) {
                Iterator it2 = linkedList.iterator();
                while (it2.hasNext()) {
                    DefaultLogger.e("StorageStrategy[version=1-0-0]", (Throwable) it2.next());
                }
            }
        }
        if (IStorageStrategy.DEBUG) {
            DefaultLogger.v("StorageStrategy[version=1-0-0]", String.valueOf(permissionResult));
        }
        Intrinsics.checkNotNull(permissionResult);
        Intrinsics.checkNotNullExpressionValue(permissionResult, "handle(\n            stra…ed ?: false }\n        )!!");
        return permissionResult;
    }

    /* JADX WARN: Code restructure failed: missing block: B:71:0x00d6, code lost:
        continue;
     */
    @Override // com.miui.gallery.storage.strategies.IStorageStrategyHolder, com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.util.List<com.miui.gallery.storage.strategies.IStoragePermissionStrategy.PermissionResult> checkPermission(java.util.List<java.lang.String> r19, com.miui.gallery.storage.strategies.IStoragePermissionStrategy.Permission r20) {
        /*
            Method dump skipped, instructions count: 378
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.storage.strategies.base.BaseStorageStrategyHolder.checkPermission(java.util.List, com.miui.gallery.storage.strategies.IStoragePermissionStrategy$Permission):java.util.List");
    }

    @Override // com.miui.gallery.storage.strategies.IStorageStrategyHolder, com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    public void requestPermission(FragmentActivity host, String path, IStoragePermissionStrategy.Permission... types) {
        Intrinsics.checkNotNullParameter(host, "host");
        Intrinsics.checkNotNullParameter(path, "path");
        Intrinsics.checkNotNullParameter(types, "types");
        List<IStorageStrategy> list = this.strategies;
        String str = "activity: [" + ((Object) host.getClass().getSimpleName()) + "], path: [" + path + ']';
        Unit unit = Unit.INSTANCE;
        if (IStorageStrategy.DEBUG) {
            DefaultLogger.v("StorageStrategy[version=1-0-0]", "[requestPermission] description:[" + str + ']');
            DefaultLogger.verbosePrintStackMsg("StorageStrategy[version=1-0-0]");
        }
        LinkedList linkedList = new LinkedList();
        for (IStorageStrategy iStorageStrategy : list) {
            if (IStorageStrategy.DEBUG) {
                DefaultLogger.v("StorageStrategy[version=1-0-0]", "try [requestPermission] with [" + ((Object) iStorageStrategy.getClass().getSimpleName()) + "] start");
            }
            try {
                iStorageStrategy.requestPermission(host, path, (IStoragePermissionStrategy.Permission[]) Arrays.copyOf(types, types.length));
                unit = Unit.INSTANCE;
            } catch (StorageUnsupportedOperationException unused) {
                if (IStorageStrategy.DEBUG) {
                    DefaultLogger.v("StorageStrategy[version=1-0-0]", "try [requestPermission] with [" + ((Object) iStorageStrategy.getClass().getSimpleName()) + "] failed, [StorageUnsupportedOperationException]");
                }
            } catch (Throwable th) {
                linkedList.add(th);
            }
        }
        if (IStorageStrategy.DEBUG) {
            Iterator it = linkedList.iterator();
            while (it.hasNext()) {
                DefaultLogger.e("StorageStrategy[version=1-0-0]", (Throwable) it.next());
            }
        }
        if (IStorageStrategy.DEBUG) {
            DefaultLogger.v("StorageStrategy[version=1-0-0]", String.valueOf(unit));
        }
    }

    @Override // com.miui.gallery.storage.strategies.IStorageStrategyHolder, com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    public void requestPermission(FragmentActivity activity, String path, Map<String, ? extends Object> params, IStoragePermissionStrategy.Permission... types) {
        Intrinsics.checkNotNullParameter(activity, "activity");
        Intrinsics.checkNotNullParameter(path, "path");
        Intrinsics.checkNotNullParameter(params, "params");
        Intrinsics.checkNotNullParameter(types, "types");
        List<IStorageStrategy> list = this.strategies;
        String str = "activity: [" + ((Object) activity.getClass().getSimpleName()) + "], path: [" + path + "], params: [" + ((Object) TextUtils.join(", ", params.entrySet())) + ']';
        Unit unit = Unit.INSTANCE;
        if (IStorageStrategy.DEBUG) {
            DefaultLogger.v("StorageStrategy[version=1-0-0]", "[requestPermission] description:[" + str + ']');
            DefaultLogger.verbosePrintStackMsg("StorageStrategy[version=1-0-0]");
        }
        LinkedList linkedList = new LinkedList();
        for (IStorageStrategy iStorageStrategy : list) {
            if (IStorageStrategy.DEBUG) {
                DefaultLogger.v("StorageStrategy[version=1-0-0]", "try [requestPermission] with [" + ((Object) iStorageStrategy.getClass().getSimpleName()) + "] start");
            }
            try {
                iStorageStrategy.requestPermission(activity, path, params, (IStoragePermissionStrategy.Permission[]) Arrays.copyOf(types, types.length));
                unit = Unit.INSTANCE;
            } catch (StorageUnsupportedOperationException unused) {
                if (IStorageStrategy.DEBUG) {
                    DefaultLogger.v("StorageStrategy[version=1-0-0]", "try [requestPermission] with [" + ((Object) iStorageStrategy.getClass().getSimpleName()) + "] failed, [StorageUnsupportedOperationException]");
                }
            } catch (Throwable th) {
                linkedList.add(th);
            }
        }
        if (IStorageStrategy.DEBUG) {
            Iterator it = linkedList.iterator();
            while (it.hasNext()) {
                DefaultLogger.e("StorageStrategy[version=1-0-0]", (Throwable) it.next());
            }
        }
        if (IStorageStrategy.DEBUG) {
            DefaultLogger.v("StorageStrategy[version=1-0-0]", String.valueOf(unit));
        }
    }

    @Override // com.miui.gallery.storage.strategies.IStorageStrategyHolder, com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    public void onHandleRequestPermissionResult(FragmentActivity activity, int i, int i2, Intent intent) {
        Intrinsics.checkNotNullParameter(activity, "activity");
        List<IStorageStrategy> list = this.strategies;
        String str = "activity:[" + ((Object) activity.getClass().getSimpleName()) + "], requestCode:[" + i + "], resultCode:[" + i2 + "], data:[" + intent + ']';
        Unit unit = Unit.INSTANCE;
        if (IStorageStrategy.DEBUG) {
            DefaultLogger.v("StorageStrategy[version=1-0-0]", "[onHandleRequestPermissionResult] description:[" + str + ']');
            DefaultLogger.verbosePrintStackMsg("StorageStrategy[version=1-0-0]");
        }
        LinkedList linkedList = new LinkedList();
        for (IStorageStrategy iStorageStrategy : list) {
            if (IStorageStrategy.DEBUG) {
                DefaultLogger.v("StorageStrategy[version=1-0-0]", "try [onHandleRequestPermissionResult] with [" + ((Object) iStorageStrategy.getClass().getSimpleName()) + "] start");
            }
            try {
                iStorageStrategy.onHandleRequestPermissionResult(activity, i, i2, intent);
                unit = Unit.INSTANCE;
            } catch (StorageUnsupportedOperationException unused) {
                if (IStorageStrategy.DEBUG) {
                    DefaultLogger.v("StorageStrategy[version=1-0-0]", "try [onHandleRequestPermissionResult] with [" + ((Object) iStorageStrategy.getClass().getSimpleName()) + "] failed, [StorageUnsupportedOperationException]");
                }
            } catch (Throwable th) {
                linkedList.add(th);
            }
        }
        if (IStorageStrategy.DEBUG) {
            Iterator it = linkedList.iterator();
            while (it.hasNext()) {
                DefaultLogger.e("StorageStrategy[version=1-0-0]", (Throwable) it.next());
            }
        }
        if (IStorageStrategy.DEBUG) {
            DefaultLogger.v("StorageStrategy[version=1-0-0]", String.valueOf(unit));
        }
    }

    @Override // com.miui.gallery.storage.strategies.IStorageStrategyHolder, com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    public void onHandleRequestPermissionResult(FragmentActivity activity, Uri uri) {
        Intrinsics.checkNotNullParameter(activity, "activity");
        List<IStorageStrategy> list = this.strategies;
        String str = "activity:[" + ((Object) activity.getClass().getSimpleName()) + "], uri:[" + uri + ']';
        Unit unit = Unit.INSTANCE;
        if (IStorageStrategy.DEBUG) {
            DefaultLogger.v("StorageStrategy[version=1-0-0]", "[onHandleRequestPermissionResult] description:[" + str + ']');
            DefaultLogger.verbosePrintStackMsg("StorageStrategy[version=1-0-0]");
        }
        LinkedList linkedList = new LinkedList();
        for (IStorageStrategy iStorageStrategy : list) {
            if (IStorageStrategy.DEBUG) {
                DefaultLogger.v("StorageStrategy[version=1-0-0]", "try [onHandleRequestPermissionResult] with [" + ((Object) iStorageStrategy.getClass().getSimpleName()) + "] start");
            }
            try {
                iStorageStrategy.onHandleRequestPermissionResult(activity, uri);
                unit = Unit.INSTANCE;
            } catch (StorageUnsupportedOperationException unused) {
                if (IStorageStrategy.DEBUG) {
                    DefaultLogger.v("StorageStrategy[version=1-0-0]", "try [onHandleRequestPermissionResult] with [" + ((Object) iStorageStrategy.getClass().getSimpleName()) + "] failed, [StorageUnsupportedOperationException]");
                }
            } catch (Throwable th) {
                linkedList.add(th);
            }
        }
        if (IStorageStrategy.DEBUG) {
            Iterator it = linkedList.iterator();
            while (it.hasNext()) {
                DefaultLogger.e("StorageStrategy[version=1-0-0]", (Throwable) it.next());
            }
        }
        if (IStorageStrategy.DEBUG) {
            DefaultLogger.v("StorageStrategy[version=1-0-0]", String.valueOf(unit));
        }
    }

    @Override // com.miui.gallery.storage.strategies.IStorageStrategyHolder, com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    public void onHandleRequestPermissionResult(Fragment fragment, Uri uri) {
        Intrinsics.checkNotNullParameter(fragment, "fragment");
        List<IStorageStrategy> list = this.strategies;
        String str = "activity:[" + ((Object) fragment.getClass().getSimpleName()) + "], uri:[" + uri + ']';
        Unit unit = Unit.INSTANCE;
        if (IStorageStrategy.DEBUG) {
            DefaultLogger.v("StorageStrategy[version=1-0-0]", "[onHandleRequestPermissionResult] description:[" + str + ']');
            DefaultLogger.verbosePrintStackMsg("StorageStrategy[version=1-0-0]");
        }
        LinkedList linkedList = new LinkedList();
        for (IStorageStrategy iStorageStrategy : list) {
            if (IStorageStrategy.DEBUG) {
                DefaultLogger.v("StorageStrategy[version=1-0-0]", "try [onHandleRequestPermissionResult] with [" + ((Object) iStorageStrategy.getClass().getSimpleName()) + "] start");
            }
            try {
                iStorageStrategy.onHandleRequestPermissionResult(fragment, uri);
                unit = Unit.INSTANCE;
            } catch (StorageUnsupportedOperationException unused) {
                if (IStorageStrategy.DEBUG) {
                    DefaultLogger.v("StorageStrategy[version=1-0-0]", "try [onHandleRequestPermissionResult] with [" + ((Object) iStorageStrategy.getClass().getSimpleName()) + "] failed, [StorageUnsupportedOperationException]");
                }
            } catch (Throwable th) {
                linkedList.add(th);
            }
        }
        if (IStorageStrategy.DEBUG) {
            Iterator it = linkedList.iterator();
            while (it.hasNext()) {
                DefaultLogger.e("StorageStrategy[version=1-0-0]", (Throwable) it.next());
            }
        }
        if (IStorageStrategy.DEBUG) {
            DefaultLogger.v("StorageStrategy[version=1-0-0]", String.valueOf(unit));
        }
    }

    @Override // com.miui.gallery.storage.strategies.IStorageStrategyHolder, com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    public void registerPermissionObserver(ContentObserver observer) {
        Intrinsics.checkNotNullParameter(observer, "observer");
        List<IStorageStrategy> list = this.strategies;
        String str = "observer:[" + observer + ']';
        Unit unit = Unit.INSTANCE;
        if (IStorageStrategy.DEBUG) {
            DefaultLogger.v("StorageStrategy[version=1-0-0]", "[registerPermissionObserver] description:[" + str + ']');
            DefaultLogger.verbosePrintStackMsg("StorageStrategy[version=1-0-0]");
        }
        LinkedList linkedList = new LinkedList();
        for (IStorageStrategy iStorageStrategy : list) {
            if (IStorageStrategy.DEBUG) {
                DefaultLogger.v("StorageStrategy[version=1-0-0]", "try [registerPermissionObserver] with [" + ((Object) iStorageStrategy.getClass().getSimpleName()) + "] start");
            }
            try {
                iStorageStrategy.registerPermissionObserver(observer);
                unit = Unit.INSTANCE;
            } catch (StorageUnsupportedOperationException unused) {
                if (IStorageStrategy.DEBUG) {
                    DefaultLogger.v("StorageStrategy[version=1-0-0]", "try [registerPermissionObserver] with [" + ((Object) iStorageStrategy.getClass().getSimpleName()) + "] failed, [StorageUnsupportedOperationException]");
                }
            } catch (Throwable th) {
                linkedList.add(th);
            }
        }
        if (IStorageStrategy.DEBUG) {
            Iterator it = linkedList.iterator();
            while (it.hasNext()) {
                DefaultLogger.e("StorageStrategy[version=1-0-0]", (Throwable) it.next());
            }
        }
        if (IStorageStrategy.DEBUG) {
            DefaultLogger.v("StorageStrategy[version=1-0-0]", String.valueOf(unit));
        }
    }

    @Override // com.miui.gallery.storage.strategies.IStorageStrategyHolder
    public void unregisterPermissionObserver(ContentObserver observer) {
        Intrinsics.checkNotNullParameter(observer, "observer");
        List<IStorageStrategy> list = this.strategies;
        String str = "observer:[" + observer + ']';
        Unit unit = Unit.INSTANCE;
        if (IStorageStrategy.DEBUG) {
            DefaultLogger.v("StorageStrategy[version=1-0-0]", "[unregisterPermissionObserver] description:[" + str + ']');
            DefaultLogger.verbosePrintStackMsg("StorageStrategy[version=1-0-0]");
        }
        LinkedList linkedList = new LinkedList();
        for (IStorageStrategy iStorageStrategy : list) {
            if (IStorageStrategy.DEBUG) {
                DefaultLogger.v("StorageStrategy[version=1-0-0]", "try [unregisterPermissionObserver] with [" + ((Object) iStorageStrategy.getClass().getSimpleName()) + "] start");
            }
            try {
                iStorageStrategy.registerPermissionObserver(observer);
                unit = Unit.INSTANCE;
            } catch (StorageUnsupportedOperationException unused) {
                if (IStorageStrategy.DEBUG) {
                    DefaultLogger.v("StorageStrategy[version=1-0-0]", "try [unregisterPermissionObserver] with [" + ((Object) iStorageStrategy.getClass().getSimpleName()) + "] failed, [StorageUnsupportedOperationException]");
                }
            } catch (Throwable th) {
                linkedList.add(th);
            }
        }
        if (IStorageStrategy.DEBUG) {
            Iterator it = linkedList.iterator();
            while (it.hasNext()) {
                DefaultLogger.e("StorageStrategy[version=1-0-0]", (Throwable) it.next());
            }
        }
        if (IStorageStrategy.DEBUG) {
            DefaultLogger.v("StorageStrategy[version=1-0-0]", String.valueOf(unit));
        }
    }

    /* compiled from: BaseStorageStrategyHolder.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }

        /* JADX WARN: Multi-variable type inference failed */
        public final List<IStorageStrategy> order(List<? extends IStorageStrategy> list, String str) {
            Object obj;
            StrategyOrder strategyOrder = StrategyOrder.get(str);
            if (strategyOrder == null) {
                return list;
            }
            LinkedList linkedList = new LinkedList();
            Iterator<Integer> it = strategyOrder.iterator();
            while (it.hasNext()) {
                Integer next = it.next();
                Iterator it2 = list.iterator();
                while (true) {
                    obj = null;
                    if (!it2.hasNext()) {
                        break;
                    }
                    Object next2 = it2.next();
                    StrategyType strategyType = (StrategyType) ((IStorageStrategy) next2).getClass().getAnnotation(StrategyType.class);
                    if (strategyType != null) {
                        obj = Integer.valueOf(strategyType.type());
                    }
                    if (Intrinsics.areEqual(obj, next)) {
                        obj = next2;
                        break;
                    }
                }
                IStorageStrategy iStorageStrategy = (IStorageStrategy) obj;
                if (iStorageStrategy != null) {
                    linkedList.add(iStorageStrategy);
                }
            }
            for (Object obj2 : list) {
                if (!linkedList.contains((IStorageStrategy) obj2)) {
                    linkedList.add(obj2);
                }
            }
            return linkedList;
        }
    }
}
