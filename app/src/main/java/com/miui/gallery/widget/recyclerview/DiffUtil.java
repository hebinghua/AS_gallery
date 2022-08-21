package com.miui.gallery.widget.recyclerview;

import androidx.recyclerview.widget.BatchingListUpdateCallback;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CancellationException;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineDispatcher;
import kotlinx.coroutines.JobKt;

/* compiled from: DiffUtil.kt */
/* loaded from: classes3.dex */
public final class DiffUtil {
    public static final DiffUtil INSTANCE = new DiffUtil();
    public static final Comparator<Snake> SNAKE_COMPARATOR = DiffUtil$$ExternalSyntheticLambda0.INSTANCE;

    /* compiled from: DiffUtil.kt */
    /* loaded from: classes3.dex */
    public static abstract class Callback {
        public abstract boolean areContentsTheSame(int i, int i2);

        public abstract boolean areItemsTheSame(int i, int i2);

        public abstract Object getChangePayload(int i, int i2);

        public abstract int getNewListSize();

        public abstract int getOldListSize();
    }

    /* compiled from: DiffUtil.kt */
    /* loaded from: classes3.dex */
    public static abstract class ItemCallback<T> {
        public abstract boolean areContentsTheSame(T t, T t2);

        public abstract boolean areItemsTheSame(T t, T t2);

        public final Object getChangePayload(T t, T t2) {
            return null;
        }
    }

    /* renamed from: SNAKE_COMPARATOR$lambda-0  reason: not valid java name */
    public static final int m1826SNAKE_COMPARATOR$lambda0(Snake snake, Snake snake2) {
        int x = snake.getX() - snake2.getX();
        return x == 0 ? snake.getY() - snake2.getY() : x;
    }

    public static /* synthetic */ Object calculateDiff$default(DiffUtil diffUtil, Callback callback, CoroutineDispatcher coroutineDispatcher, boolean z, Continuation continuation, int i, Object obj) throws CancellationException {
        if ((i & 4) != 0) {
            z = true;
        }
        return diffUtil.calculateDiff(callback, coroutineDispatcher, z, continuation);
    }

    public final Object calculateDiff(Callback callback, CoroutineDispatcher coroutineDispatcher, boolean z, Continuation<? super DiffResult> continuation) throws CancellationException {
        return BuildersKt.withContext(coroutineDispatcher, new DiffUtil$calculateDiff$2(callback, z, null), continuation);
    }

    /* JADX WARN: Code restructure failed: missing block: B:58:0x0109, code lost:
        if (r30[r5 - 1] < r30[r5 + 1]) goto L98;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:106:0x0098 A[EDGE_INSN: B:106:0x0098->B:36:0x0098 ?: BREAK  , SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:113:0x014c A[EDGE_INSN: B:113:0x014c->B:75:0x014c ?: BREAK  , SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x007c  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0132  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final com.miui.gallery.widget.recyclerview.DiffUtil.Snake diffPartial(com.miui.gallery.widget.recyclerview.DiffUtil.Callback r24, int r25, int r26, int r27, int r28, int[] r29, int[] r30, int r31, kotlin.coroutines.CoroutineContext r32) throws java.util.concurrent.CancellationException {
        /*
            Method dump skipped, instructions count: 426
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.widget.recyclerview.DiffUtil.diffPartial(com.miui.gallery.widget.recyclerview.DiffUtil$Callback, int, int, int, int, int[], int[], int, kotlin.coroutines.CoroutineContext):com.miui.gallery.widget.recyclerview.DiffUtil$Snake");
    }

    /* compiled from: DiffUtil.kt */
    /* loaded from: classes3.dex */
    public static final class Snake {
        public boolean removal;
        public boolean reverse;
        public int size;
        public int x;
        public int y;

        public final int getX() {
            return this.x;
        }

        public final void setX(int i) {
            this.x = i;
        }

        public final int getY() {
            return this.y;
        }

        public final void setY(int i) {
            this.y = i;
        }

        public final int getSize() {
            return this.size;
        }

        public final void setSize(int i) {
            this.size = i;
        }

        public final boolean getRemoval() {
            return this.removal;
        }

        public final void setRemoval(boolean z) {
            this.removal = z;
        }

        public final boolean getReverse() {
            return this.reverse;
        }

        public final void setReverse(boolean z) {
            this.reverse = z;
        }
    }

    /* compiled from: DiffUtil.kt */
    /* loaded from: classes3.dex */
    public static final class Range {
        public int newListEnd;
        public int newListStart;
        public int oldListEnd;
        public int oldListStart;

        public final int getOldListStart() {
            return this.oldListStart;
        }

        public final void setOldListStart(int i) {
            this.oldListStart = i;
        }

        public final int getOldListEnd() {
            return this.oldListEnd;
        }

        public final void setOldListEnd(int i) {
            this.oldListEnd = i;
        }

        public final int getNewListStart() {
            return this.newListStart;
        }

        public final void setNewListStart(int i) {
            this.newListStart = i;
        }

        public final int getNewListEnd() {
            return this.newListEnd;
        }

        public final void setNewListEnd(int i) {
            this.newListEnd = i;
        }

        public Range() {
        }

        public Range(int i, int i2, int i3, int i4) {
            this.oldListStart = i;
            this.oldListEnd = i2;
            this.newListStart = i3;
            this.newListEnd = i4;
        }
    }

    /* compiled from: DiffUtil.kt */
    /* loaded from: classes3.dex */
    public static final class DiffResult {
        public static final Companion Companion = new Companion(null);
        public final Callback mCallback;
        public final boolean mDetectMoves;
        public final int mNewListSize;
        public final int mOldListSize;
        public final int[] newItemStatuses;
        public final int[] oldItemStatuses;
        public final List<Snake> snakes;

        public DiffResult(Callback callback, List<Snake> snakes, int[] oldItemStatuses, int[] newItemStatuses, CoroutineContext context, boolean z) {
            Intrinsics.checkNotNullParameter(callback, "callback");
            Intrinsics.checkNotNullParameter(snakes, "snakes");
            Intrinsics.checkNotNullParameter(oldItemStatuses, "oldItemStatuses");
            Intrinsics.checkNotNullParameter(newItemStatuses, "newItemStatuses");
            Intrinsics.checkNotNullParameter(context, "context");
            this.snakes = snakes;
            this.oldItemStatuses = oldItemStatuses;
            this.newItemStatuses = newItemStatuses;
            Arrays.fill(oldItemStatuses, 0);
            Arrays.fill(newItemStatuses, 0);
            this.mCallback = callback;
            this.mOldListSize = callback.getOldListSize();
            this.mNewListSize = callback.getNewListSize();
            this.mDetectMoves = z;
            addRootSnake();
            findMatchingItems(context);
        }

        public final void addRootSnake() {
            Snake snake = this.snakes.isEmpty() ? null : this.snakes.get(0);
            if (snake != null && snake.getX() == 0 && snake.getY() == 0) {
                return;
            }
            Snake snake2 = new Snake();
            snake2.setX(0);
            snake2.setY(0);
            snake2.setRemoval(false);
            snake2.setSize(0);
            snake2.setReverse(false);
            this.snakes.add(0, snake2);
        }

        public final void findMatchingItems(CoroutineContext coroutineContext) throws CancellationException {
            int i = this.mOldListSize;
            int i2 = this.mNewListSize;
            int size = this.snakes.size() - 1;
            if (size >= 0) {
                while (true) {
                    int i3 = size - 1;
                    Snake snake = this.snakes.get(size);
                    int x = snake.getX() + snake.getSize();
                    int y = snake.getY() + snake.getSize();
                    if (this.mDetectMoves) {
                        while (i > x) {
                            findAddition(i, i2, size, coroutineContext);
                            i--;
                        }
                        while (i2 > y) {
                            findRemoval(i, i2, size, coroutineContext);
                            i2--;
                        }
                    }
                    int i4 = 0;
                    int size2 = snake.getSize();
                    while (i4 < size2) {
                        int i5 = i4 + 1;
                        if (!JobKt.isActive(coroutineContext)) {
                            DefaultLogger.i("DiffUtil", "cancel in findMatchingItems");
                            throw new CancellationException();
                        }
                        int x2 = snake.getX() + i4;
                        int y2 = snake.getY() + i4;
                        int i6 = this.mCallback.areContentsTheSame(x2, y2) ? 1 : 2;
                        this.oldItemStatuses[x2] = (y2 << 5) | i6;
                        this.newItemStatuses[y2] = i6 | (x2 << 5);
                        i4 = i5;
                    }
                    i = snake.getX();
                    i2 = snake.getY();
                    if (i3 < 0) {
                        return;
                    }
                    size = i3;
                }
            }
        }

        public final void findAddition(int i, int i2, int i3, CoroutineContext coroutineContext) throws CancellationException {
            if (this.oldItemStatuses[i - 1] != 0) {
                return;
            }
            findMatchingItem(i, i2, i3, false, coroutineContext);
        }

        public final void findRemoval(int i, int i2, int i3, CoroutineContext coroutineContext) throws CancellationException {
            if (this.newItemStatuses[i2 - 1] != 0) {
                return;
            }
            findMatchingItem(i, i2, i3, true, coroutineContext);
        }

        public final boolean findMatchingItem(int i, int i2, int i3, boolean z, CoroutineContext coroutineContext) throws CancellationException {
            int i4;
            int i5;
            if (z) {
                i2--;
                i5 = i;
                i4 = i2;
            } else {
                i4 = i - 1;
                i5 = i4;
            }
            if (i3 >= 0) {
                while (true) {
                    int i6 = i3 - 1;
                    Snake snake = this.snakes.get(i3);
                    int x = snake.getX() + snake.getSize();
                    int y = snake.getY() + snake.getSize();
                    int i7 = 8;
                    if (z) {
                        int i8 = i5 - 1;
                        if (x <= i8) {
                            while (true) {
                                int i9 = i8 - 1;
                                if (!JobKt.isActive(coroutineContext)) {
                                    DefaultLogger.i("DiffUtil", "cancel in findMatchingItems");
                                    throw new CancellationException();
                                } else if (this.mCallback.areItemsTheSame(i8, i4)) {
                                    if (!this.mCallback.areContentsTheSame(i8, i4)) {
                                        i7 = 4;
                                    }
                                    this.newItemStatuses[i4] = (i8 << 5) | 16;
                                    this.oldItemStatuses[i8] = (i4 << 5) | i7;
                                    return true;
                                } else if (i8 == x) {
                                    break;
                                } else {
                                    i8 = i9;
                                }
                            }
                        }
                    } else {
                        int i10 = i2 - 1;
                        if (y <= i10) {
                            while (true) {
                                int i11 = i10 - 1;
                                if (!JobKt.isActive(coroutineContext)) {
                                    DefaultLogger.i("DiffUtil", "cancel in findMatchingItems");
                                    throw new CancellationException();
                                } else if (this.mCallback.areItemsTheSame(i4, i10)) {
                                    if (!this.mCallback.areContentsTheSame(i4, i10)) {
                                        i7 = 4;
                                    }
                                    int i12 = i - 1;
                                    this.oldItemStatuses[i12] = (i10 << 5) | 16;
                                    this.newItemStatuses[i10] = (i12 << 5) | i7;
                                    return true;
                                } else if (i10 == y) {
                                    break;
                                } else {
                                    i10 = i11;
                                }
                            }
                        }
                    }
                    i5 = snake.getX();
                    i2 = snake.getY();
                    if (i6 < 0) {
                        return false;
                    }
                    i3 = i6;
                }
            } else {
                return false;
            }
        }

        public final void dispatchUpdatesTo(androidx.recyclerview.widget.ListUpdateCallback updateCallback) {
            BatchingListUpdateCallback batchingListUpdateCallback;
            Intrinsics.checkNotNullParameter(updateCallback, "updateCallback");
            if (updateCallback instanceof BatchingListUpdateCallback) {
                batchingListUpdateCallback = (BatchingListUpdateCallback) updateCallback;
            } else {
                batchingListUpdateCallback = new BatchingListUpdateCallback(updateCallback);
            }
            ArrayList arrayList = new ArrayList();
            int i = this.mOldListSize;
            int i2 = this.mNewListSize;
            int size = this.snakes.size() - 1;
            if (size >= 0) {
                int i3 = i2;
                while (true) {
                    int i4 = size - 1;
                    Snake snake = this.snakes.get(size);
                    int size2 = snake.getSize();
                    int x = snake.getX() + size2;
                    int y = snake.getY() + size2;
                    if (x < i) {
                        dispatchRemovals(arrayList, batchingListUpdateCallback, x, i - x, x);
                    }
                    if (y < i3) {
                        dispatchAdditions(arrayList, batchingListUpdateCallback, x, i3 - y, y);
                    }
                    int i5 = size2 - 1;
                    if (i5 >= 0) {
                        while (true) {
                            int i6 = i5 - 1;
                            if ((this.oldItemStatuses[snake.getX() + i5] & 31) == 2) {
                                batchingListUpdateCallback.onChanged(snake.getX() + i5, 1, this.mCallback.getChangePayload(snake.getX() + i5, snake.getY() + i5));
                            }
                            if (i6 < 0) {
                                break;
                            }
                            i5 = i6;
                        }
                    }
                    i = snake.getX();
                    i3 = snake.getY();
                    if (i4 < 0) {
                        break;
                    }
                    size = i4;
                }
            }
            batchingListUpdateCallback.dispatchLastEvent();
        }

        public final void dispatchAdditions(List<PostponedUpdate> list, androidx.recyclerview.widget.ListUpdateCallback listUpdateCallback, int i, int i2, int i3) {
            if (!this.mDetectMoves) {
                listUpdateCallback.onInserted(i, i2);
                return;
            }
            int i4 = i2 - 1;
            if (i4 < 0) {
                return;
            }
            while (true) {
                int i5 = i4 - 1;
                int[] iArr = this.newItemStatuses;
                int i6 = i4 + i3;
                int i7 = iArr[i6] & 31;
                if (i7 == 0) {
                    listUpdateCallback.onInserted(i, 1);
                    for (PostponedUpdate postponedUpdate : list) {
                        postponedUpdate.setCurrentPos(postponedUpdate.getCurrentPos() + 1);
                    }
                } else if (i7 == 4 || i7 == 8) {
                    int i8 = iArr[i6] >> 5;
                    PostponedUpdate removePostponedUpdate = Companion.removePostponedUpdate(list, i8, true);
                    Intrinsics.checkNotNull(removePostponedUpdate);
                    listUpdateCallback.onMoved(removePostponedUpdate.getCurrentPos(), i);
                    if (i7 == 4) {
                        listUpdateCallback.onChanged(i, 1, this.mCallback.getChangePayload(i8, i6));
                    }
                } else if (i7 == 16) {
                    list.add(new PostponedUpdate(i6, i, false));
                } else {
                    throw new IllegalStateException("unknown flag for pos " + i6 + ' ' + ((Object) Long.toBinaryString(i7)));
                }
                if (i5 < 0) {
                    return;
                }
                i4 = i5;
            }
        }

        public final void dispatchRemovals(List<PostponedUpdate> list, androidx.recyclerview.widget.ListUpdateCallback listUpdateCallback, int i, int i2, int i3) {
            if (!this.mDetectMoves) {
                listUpdateCallback.onRemoved(i, i2);
                return;
            }
            int i4 = i2 - 1;
            if (i4 < 0) {
                return;
            }
            while (true) {
                int i5 = i4 - 1;
                int[] iArr = this.oldItemStatuses;
                int i6 = i3 + i4;
                int i7 = iArr[i6] & 31;
                if (i7 == 0) {
                    listUpdateCallback.onRemoved(i4 + i, 1);
                    for (PostponedUpdate postponedUpdate : list) {
                        postponedUpdate.setCurrentPos(postponedUpdate.getCurrentPos() - 1);
                    }
                } else if (i7 == 4 || i7 == 8) {
                    int i8 = iArr[i6] >> 5;
                    PostponedUpdate removePostponedUpdate = Companion.removePostponedUpdate(list, i8, false);
                    Intrinsics.checkNotNull(removePostponedUpdate);
                    listUpdateCallback.onMoved(i4 + i, removePostponedUpdate.getCurrentPos() - 1);
                    if (i7 == 4) {
                        listUpdateCallback.onChanged(removePostponedUpdate.getCurrentPos() - 1, 1, this.mCallback.getChangePayload(i6, i8));
                    }
                } else if (i7 == 16) {
                    list.add(new PostponedUpdate(i6, i4 + i, true));
                } else {
                    throw new IllegalStateException("unknown flag for pos " + i6 + ' ' + ((Object) Long.toBinaryString(i7)));
                }
                if (i5 < 0) {
                    return;
                }
                i4 = i5;
            }
        }

        /* compiled from: DiffUtil.kt */
        /* loaded from: classes3.dex */
        public static final class Companion {
            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            public Companion() {
            }

            public final PostponedUpdate removePostponedUpdate(List<PostponedUpdate> list, int i, boolean z) {
                int size = list.size() - 1;
                if (size >= 0) {
                    while (true) {
                        int i2 = size - 1;
                        PostponedUpdate postponedUpdate = list.get(size);
                        if (postponedUpdate.getPosInOwnerList() == i && postponedUpdate.getRemoval() == z) {
                            list.remove(size);
                            int size2 = list.size();
                            while (size < size2) {
                                int i3 = size + 1;
                                PostponedUpdate postponedUpdate2 = list.get(size);
                                postponedUpdate2.setCurrentPos(postponedUpdate2.getCurrentPos() + (z ? 1 : -1));
                                size = i3;
                            }
                            return postponedUpdate;
                        } else if (i2 < 0) {
                            return null;
                        } else {
                            size = i2;
                        }
                    }
                } else {
                    return null;
                }
            }
        }
    }

    /* compiled from: DiffUtil.kt */
    /* loaded from: classes3.dex */
    public static final class PostponedUpdate {
        public int currentPos;
        public int posInOwnerList;
        public boolean removal;

        public PostponedUpdate(int i, int i2, boolean z) {
            this.posInOwnerList = i;
            this.currentPos = i2;
            this.removal = z;
        }

        public final int getPosInOwnerList() {
            return this.posInOwnerList;
        }

        public final int getCurrentPos() {
            return this.currentPos;
        }

        public final void setCurrentPos(int i) {
            this.currentPos = i;
        }

        public final boolean getRemoval() {
            return this.removal;
        }
    }
}
