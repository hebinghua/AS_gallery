package com.miui.epoxy.diff;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import androidx.recyclerview.widget.AsyncListDiffer$ListListener;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListUpdateCallback;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;

/* loaded from: classes.dex */
public class AsyncListDiffer<T> {
    public static final Executor sMainThreadExecutor = new MainThreadExecutor();
    public final AsyncDifferConfig<T> mConfig;
    public List<T> mLastDiffList;
    public List<T> mList;
    public ListGenerator<T> mListGenerator;
    public Executor mMainThreadExecutor;
    public int mMaxScheduledGeneration;
    public final ListUpdateCallback mUpdateCallback;
    public final List<AsyncListDiffer$ListListener<T>> mListeners = new CopyOnWriteArrayList();
    public List<T> mReadOnlyList = Collections.emptyList();

    /* loaded from: classes.dex */
    public interface ListGenerator<T> {
        List<T> generate(List<T> list);
    }

    /* loaded from: classes.dex */
    public static class MainThreadExecutor implements Executor {
        public final Handler mHandler = new Handler(Looper.getMainLooper());

        @Override // java.util.concurrent.Executor
        public void execute(Runnable runnable) {
            this.mHandler.post(runnable);
        }
    }

    public AsyncListDiffer(ListUpdateCallback listUpdateCallback, AsyncDifferConfig<T> asyncDifferConfig) {
        this.mUpdateCallback = listUpdateCallback;
        this.mConfig = asyncDifferConfig;
        if (asyncDifferConfig.getMainThreadExecutor() != null) {
            this.mMainThreadExecutor = asyncDifferConfig.getMainThreadExecutor();
        } else {
            this.mMainThreadExecutor = sMainThreadExecutor;
        }
        this.mListGenerator = asyncDifferConfig.getListGenerator();
    }

    public List<T> getCurrentDiffingList() {
        return this.mLastDiffList;
    }

    public boolean submitListByClear() {
        return submitListByClear(null);
    }

    public boolean submitListByClear(Runnable runnable) {
        List<T> generate;
        if (this.mLastDiffList != null) {
            ListGenerator<T> listGenerator = this.mListGenerator;
            if (listGenerator == null || (generate = listGenerator.generate(null)) == null) {
                Log.e("AsyncListDiffer", "generate list failed" + TextUtils.join("\n", Thread.currentThread().getStackTrace()));
                return false;
            }
            generate.clear();
            submitList(generate, runnable);
            return true;
        }
        int size = this.mList.size();
        this.mList.clear();
        this.mUpdateCallback.onRemoved(0, size);
        onCurrentListChanged(this.mReadOnlyList, runnable);
        return this.mList.size() == 0;
    }

    public boolean submitListByRemove(int i) {
        return submitListByRemove(i, (Runnable) null);
    }

    public boolean submitListByRemove(int i, Runnable runnable) {
        if (i >= 0) {
            List<T> list = this.mLastDiffList;
            if (list != null && i < list.size()) {
                return submitListByRemove((AsyncListDiffer<T>) this.mLastDiffList.get(i));
            }
            if (i >= this.mList.size()) {
                return false;
            }
            T remove = this.mList.remove(i);
            this.mUpdateCallback.onRemoved(i, 1);
            onCurrentListChanged(this.mReadOnlyList, runnable);
            return remove != null;
        }
        return false;
    }

    public boolean submitListByRemove(T t) {
        return submitListByRemove((List) Collections.singletonList(t));
    }

    public boolean submitListByRemove(List<T> list) {
        return submitListByRemove(list, (Runnable) null);
    }

    public boolean submitListByRemove(List<T> list, Runnable runnable) {
        List<T> generate;
        List<T> list2 = this.mLastDiffList;
        if (list2 != null) {
            ListGenerator<T> listGenerator = this.mListGenerator;
            if (listGenerator == null || (generate = listGenerator.generate(list2)) == null) {
                Log.e("AsyncListDiffer", "generate list failed" + TextUtils.join("\n", Thread.currentThread().getStackTrace()));
                return false;
            } else if (!generate.removeAll(list)) {
                return false;
            } else {
                submitList(generate, runnable);
                return true;
            }
        }
        LinkedList<Integer> linkedList = new LinkedList();
        for (int i = 0; i < list.size(); i++) {
            int indexOf = this.mList.indexOf(list.get(i));
            if (-1 != indexOf) {
                this.mList.remove(indexOf);
                linkedList.add(Integer.valueOf(indexOf));
            }
        }
        if (linkedList.isEmpty()) {
            return false;
        }
        for (Integer num : linkedList) {
            this.mUpdateCallback.onRemoved(num.intValue(), 1);
        }
        onCurrentListChanged(this.mReadOnlyList, runnable);
        return true;
    }

    public boolean submitListByUpdate(int i, T t) {
        return submitListByUpdate(i, t, null);
    }

    public boolean submitListByUpdate(int i, T t, Runnable runnable) {
        List<T> generate;
        List<T> list = this.mLastDiffList;
        if (list != null && this.mList != list) {
            int indexOf = list.indexOf(t);
            if (-1 != indexOf) {
                ListGenerator<T> listGenerator = this.mListGenerator;
                if (listGenerator == null || (generate = listGenerator.generate(this.mLastDiffList)) == null) {
                    Log.e("AsyncListDiffer", "generate list failed" + TextUtils.join("\n", Thread.currentThread().getStackTrace()));
                } else {
                    generate.set(indexOf, t);
                    submitList(generate, runnable);
                    return true;
                }
            }
            return false;
        } else if (i < 0 || i >= this.mList.size()) {
            return false;
        } else {
            T t2 = this.mList.get(i);
            this.mList.set(i, t);
            this.mUpdateCallback.onChanged(i, 1, this.mConfig.getDiffCallback().getChangePayload(t2, t));
            onCurrentListChanged(this.mReadOnlyList, runnable);
            return true;
        }
    }

    public boolean submitListByAdd(int i, Collection<T> collection) {
        return submitListByAdd(i, collection, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public boolean submitListByAdd(int i, Collection<T> collection, Runnable runnable) {
        List<T> generate;
        List<T> list = this.mLastDiffList;
        if (list != null) {
            if (i < list.size()) {
                ListGenerator<T> listGenerator = this.mListGenerator;
                if (listGenerator == null || (generate = listGenerator.generate(this.mLastDiffList)) == null) {
                    Log.e("AsyncListDiffer", "generate list failed" + TextUtils.join("\n", Thread.currentThread().getStackTrace()));
                } else {
                    generate.addAll(i, collection);
                    submitList(generate, runnable);
                    return true;
                }
            }
            return false;
        }
        List<T> list2 = this.mList;
        if (list2 == null) {
            List<T> list3 = (List) collection;
            this.mList = list3;
            this.mUpdateCallback.onInserted(0, list3.size());
            onCurrentListChanged(this.mReadOnlyList, runnable);
        } else {
            int size = list2.size();
            this.mList.addAll(i, collection);
            this.mUpdateCallback.onInserted(size, collection.size());
            onCurrentListChanged(this.mReadOnlyList, runnable);
        }
        return true;
    }

    public void submitList(final List<T> list, final Runnable runnable) {
        final int i = this.mMaxScheduledGeneration + 1;
        this.mMaxScheduledGeneration = i;
        final List<T> list2 = this.mList;
        if (list == list2) {
            if (runnable == null) {
                return;
            }
            runnable.run();
            return;
        }
        List<T> list3 = this.mReadOnlyList;
        if (list == null) {
            int size = list2.size();
            this.mList = null;
            this.mReadOnlyList = Collections.emptyList();
            this.mUpdateCallback.onRemoved(0, size);
            onCurrentListChanged(list3, runnable);
        } else if (list2 == null) {
            this.mList = list;
            this.mReadOnlyList = Collections.unmodifiableList(list);
            this.mUpdateCallback.onInserted(0, list.size());
            onCurrentListChanged(list3, runnable);
        } else {
            this.mConfig.getBackgroundThreadExecutor().execute(new Runnable() { // from class: com.miui.epoxy.diff.AsyncListDiffer.1
                @Override // java.lang.Runnable
                public void run() {
                    AsyncListDiffer.this.mLastDiffList = list;
                    try {
                        final DiffUtil.DiffResult calculateDiff = DiffUtil.calculateDiff(new DiffUtil.Callback() { // from class: com.miui.epoxy.diff.AsyncListDiffer.1.1
                            @Override // androidx.recyclerview.widget.DiffUtil.Callback
                            public int getOldListSize() {
                                return list2.size();
                            }

                            @Override // androidx.recyclerview.widget.DiffUtil.Callback
                            public int getNewListSize() {
                                return list.size();
                            }

                            @Override // androidx.recyclerview.widget.DiffUtil.Callback
                            public boolean areItemsTheSame(int i2, int i3) {
                                Object obj = list2.get(i2);
                                Object obj2 = list.get(i3);
                                if (obj == null || obj2 == null) {
                                    return obj == null && obj2 == null;
                                }
                                return AsyncListDiffer.this.mConfig.getDiffCallback().areItemsTheSame(obj, obj2);
                            }

                            @Override // androidx.recyclerview.widget.DiffUtil.Callback
                            public boolean areContentsTheSame(int i2, int i3) {
                                Object obj = list2.get(i2);
                                Object obj2 = list.get(i3);
                                if (obj == null || obj2 == null) {
                                    if (obj != null || obj2 != null) {
                                        throw new AssertionError();
                                    }
                                    return true;
                                }
                                return AsyncListDiffer.this.mConfig.getDiffCallback().areContentsTheSame(obj, obj2);
                            }

                            @Override // androidx.recyclerview.widget.DiffUtil.Callback
                            public Object getChangePayload(int i2, int i3) {
                                Object obj = list2.get(i2);
                                Object obj2 = list.get(i3);
                                if (obj != null && obj2 != null) {
                                    return AsyncListDiffer.this.mConfig.getDiffCallback().getChangePayload(obj, obj2);
                                }
                                throw new AssertionError();
                            }
                        });
                        AsyncListDiffer.this.mMainThreadExecutor.execute(new Runnable() { // from class: com.miui.epoxy.diff.AsyncListDiffer.1.2
                            @Override // java.lang.Runnable
                            public void run() {
                                AnonymousClass1 anonymousClass1 = AnonymousClass1.this;
                                AsyncListDiffer asyncListDiffer = AsyncListDiffer.this;
                                if (asyncListDiffer.mMaxScheduledGeneration == i) {
                                    asyncListDiffer.latchList(list, calculateDiff, runnable);
                                }
                            }
                        });
                    } catch (Exception e) {
                        Log.e("AsyncListDiffer", "diff error" + TextUtils.join("\n", e.getStackTrace()));
                    }
                }
            });
        }
    }

    public void latchList(List<T> list, DiffUtil.DiffResult diffResult, Runnable runnable) {
        List<T> list2 = this.mReadOnlyList;
        this.mList = list;
        this.mReadOnlyList = Collections.unmodifiableList(list);
        onCurrentListChanged(list2, runnable);
        diffResult.dispatchUpdatesTo(this.mUpdateCallback);
        this.mLastDiffList = null;
    }

    public final void onCurrentListChanged(List<T> list, Runnable runnable) {
        for (AsyncListDiffer$ListListener<T> asyncListDiffer$ListListener : this.mListeners) {
            asyncListDiffer$ListListener.onCurrentListChanged(list, this.mList);
        }
        if (runnable != null) {
            runnable.run();
        }
    }

    public void addListListener(AsyncListDiffer$ListListener<T> asyncListDiffer$ListListener) {
        this.mListeners.add(asyncListDiffer$ListListener);
    }
}
