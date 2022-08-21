package cn.kuaipan.android.kss.download;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import ch.qos.logback.core.util.FileSize;
import cn.kuaipan.android.http.IKscTransferListener;
import cn.kuaipan.android.kss.IKssDownloadRequestResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

/* loaded from: classes.dex */
public class LoadMap {
    public final BlockSpace[] mBlocks;
    public IKscTransferListener mListener;
    public final HashMap<Space, LoadRecorder> mRecorders = new HashMap<>();
    public final ArrayList<Space> mEmptySpaces = new ArrayList<>();

    /* loaded from: classes.dex */
    public enum VerifyState {
        NOT_VERIFY,
        VERIFING,
        VERIFIED
    }

    public LoadMap(IKssDownloadRequestResult iKssDownloadRequestResult, IKscTransferListener iKscTransferListener) {
        int blockCount = iKssDownloadRequestResult.getBlockCount();
        this.mBlocks = new BlockSpace[blockCount];
        long j = 0;
        for (int i = 0; i < blockCount; i++) {
            IKssDownloadRequestResult.Block block = iKssDownloadRequestResult.getBlock(i);
            BlockSpace blockSpace = new BlockSpace(block, j);
            this.mBlocks[i] = blockSpace;
            this.mEmptySpaces.addAll(Arrays.asList(blockSpace.getAllSpaces()));
            j += block.size;
        }
        this.mListener = iKscTransferListener;
        if (iKscTransferListener != null) {
            iKscTransferListener.setReceiveTotal(iKssDownloadRequestResult.getTotalSize());
        }
    }

    public void verify(KssAccessor kssAccessor, boolean z) throws IOException {
        int i = 0;
        while (true) {
            BlockSpace[] blockSpaceArr = this.mBlocks;
            if (i < blockSpaceArr.length) {
                BlockSpace blockSpace = blockSpaceArr[i];
                if (!blockSpace.verify(kssAccessor, z)) {
                    resetBlock(i);
                    IKscTransferListener iKscTransferListener = this.mListener;
                    if (iKscTransferListener != null) {
                        iKscTransferListener.received(blockSpace.start - blockSpace.end);
                    }
                }
                i++;
            } else {
                return;
            }
        }
    }

    public synchronized LoadRecorder obtainRecorder() {
        Space allocEmptySpace = allocEmptySpace();
        if (allocEmptySpace != null) {
            LoadRecorder loadRecorder = new LoadRecorder(this, allocEmptySpace);
            this.mRecorders.put(allocEmptySpace, loadRecorder);
            return loadRecorder;
        }
        Space findMaxInUsedSpace = findMaxInUsedSpace();
        if (findMaxInUsedSpace != null && findMaxInUsedSpace.size() > 65536) {
            Space halve = findMaxInUsedSpace.halve();
            LoadRecorder loadRecorder2 = new LoadRecorder(this, halve);
            this.mRecorders.put(halve, loadRecorder2);
            return loadRecorder2;
        }
        return null;
    }

    public synchronized void recycleRecorder(LoadRecorder loadRecorder) {
        Space space = loadRecorder.getSpace();
        if (this.mRecorders.remove(space) == null) {
            return;
        }
        if (space.tryMerge()) {
            return;
        }
        this.mEmptySpaces.add(space);
    }

    public void resetBlock(int i) {
        Space[] allSpaces;
        if (i >= 0) {
            BlockSpace[] blockSpaceArr = this.mBlocks;
            if (i < blockSpaceArr.length) {
                BlockSpace blockSpace = blockSpaceArr[i];
                synchronized (blockSpace) {
                    for (Space space : blockSpace.getAllSpaces()) {
                        LoadRecorder remove = this.mRecorders.remove(space);
                        if (remove != null) {
                            remove.recycle();
                        }
                        this.mEmptySpaces.remove(space);
                    }
                    blockSpace.reset();
                    this.mEmptySpaces.addAll(Arrays.asList(blockSpace.getAllSpaces()));
                }
                return;
            }
        }
        throw new IndexOutOfBoundsException();
    }

    public final Space allocEmptySpace() {
        long j = -1;
        int i = -1;
        for (int i2 = 0; i2 < this.mEmptySpaces.size(); i2++) {
            long size = this.mEmptySpaces.get(i2).size();
            if (j < size) {
                i = i2;
                j = size;
            }
        }
        if (i >= 0) {
            return this.mEmptySpaces.remove(i);
        }
        return null;
    }

    public void onSpaceRemoved(int i) {
        IKscTransferListener iKscTransferListener = this.mListener;
        if (iKscTransferListener != null) {
            iKscTransferListener.received(i);
        }
    }

    public final Space findMaxInUsedSpace() {
        long j = -1;
        Space space = null;
        for (Space space2 : this.mRecorders.keySet()) {
            long size = space2.size();
            if (j < size) {
                space = space2;
                j = size;
            }
        }
        return space;
    }

    public void initSize(long j) {
        synchronized (this) {
            int length = this.mBlocks.length;
            this.mEmptySpaces.clear();
            IKscTransferListener iKscTransferListener = this.mListener;
            long j2 = 0;
            if (iKscTransferListener != null) {
                iKscTransferListener.setReceivePos(0L);
            }
            int i = 0;
            while (i < length) {
                BlockSpace blockSpace = this.mBlocks[i];
                blockSpace.reset();
                long size = blockSpace.size() + j2;
                if (j >= size) {
                    blockSpace.setSpaces(new long[0]);
                    IKscTransferListener iKscTransferListener2 = this.mListener;
                    if (iKscTransferListener2 != null) {
                        iKscTransferListener2.received(blockSpace.end - blockSpace.start);
                    }
                } else {
                    blockSpace.setSpaces(new long[]{j2, size});
                }
                this.mEmptySpaces.addAll(Arrays.asList(blockSpace.getAllSpaces()));
                i++;
                j2 = size;
            }
        }
    }

    public boolean load(Bundle bundle) {
        if (bundle == null) {
            return false;
        }
        try {
            ArrayList parcelableArrayList = bundle.getParcelableArrayList("blocks");
            int size = parcelableArrayList.size();
            if (size != this.mBlocks.length) {
                Log.w("LoadMap", "Block count is wrong in kinfo, ignore saved map");
                return false;
            }
            for (int i = 0; i < size; i++) {
                Bundle bundle2 = (Bundle) parcelableArrayList.get(i);
                long j = bundle2.getLong("block_start");
                long j2 = bundle2.getLong("block_end");
                BlockSpace blockSpace = this.mBlocks[i];
                if (blockSpace.start == j && blockSpace.end == j2) {
                }
                Log.w("LoadMap", "Block start/ends is wrong in kinfo, ignore saved map");
                return false;
            }
            synchronized (this) {
                this.mEmptySpaces.clear();
                IKscTransferListener iKscTransferListener = this.mListener;
                if (iKscTransferListener != null) {
                    iKscTransferListener.setReceivePos(0L);
                }
                long j3 = 0;
                for (int i2 = 0; i2 < size; i2++) {
                    BlockSpace blockSpace2 = this.mBlocks[i2];
                    blockSpace2.reset();
                    blockSpace2.setSpaces(((Bundle) parcelableArrayList.get(i2)).getLongArray("space_info"));
                    this.mEmptySpaces.addAll(Arrays.asList(blockSpace2.getAllSpaces()));
                    if (this.mListener != null) {
                        j3 += (blockSpace2.end - blockSpace2.start) - blockSpace2.size();
                    }
                }
                IKscTransferListener iKscTransferListener2 = this.mListener;
                if (iKscTransferListener2 != null && j3 != 0) {
                    iKscTransferListener2.received(j3);
                }
            }
            return true;
        } catch (Throwable unused) {
            Log.w("LoadMap", "Meet exception Block count is wrony in kinfo, ignore saved map");
            return false;
        }
    }

    public void save(Bundle bundle) {
        if (bundle == null) {
            return;
        }
        int length = this.mBlocks.length;
        ArrayList<? extends Parcelable> arrayList = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            BlockSpace blockSpace = this.mBlocks[i];
            Bundle bundle2 = new Bundle();
            bundle2.putLong("block_start", blockSpace.start);
            bundle2.putLong("block_end", blockSpace.end);
            ArrayList arrayList2 = blockSpace.spaces;
            int size = arrayList2.size();
            long[] jArr = new long[size * 2];
            for (int i2 = 0; i2 < size; i2++) {
                Space space = (Space) arrayList2.get(i2);
                int i3 = i2 * 2;
                jArr[i3] = space.start;
                jArr[i3 + 1] = space.end;
            }
            bundle2.putLongArray("space_info", jArr);
            arrayList.add(bundle2);
        }
        bundle.putParcelableArrayList("blocks", arrayList);
    }

    public boolean isCompleted() {
        BlockSpace[] blockSpaceArr;
        for (BlockSpace blockSpace : this.mBlocks) {
            if (blockSpace.size() > 0 || !blockSpace.isVerified()) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        return Arrays.toString(this.mBlocks);
    }

    /* loaded from: classes.dex */
    public class Space {
        public final BlockSpace block;
        public long end;
        public long start;

        public Space(BlockSpace blockSpace, long j, long j2) {
            if (j2 < j) {
                throw new IndexOutOfBoundsException();
            }
            this.block = blockSpace;
            this.start = j;
            this.end = j2;
        }

        public long getStart() {
            return this.start;
        }

        public void remove(int i) {
            synchronized (this.block) {
                this.start = Math.min(this.start + i, this.end);
            }
        }

        public final Space halve() {
            long j = this.start;
            long j2 = this.end;
            long j3 = j + ((j2 - j) / 2);
            if (j3 % FileSize.KB_COEFFICIENT > 0) {
                j3 = ((j3 / FileSize.KB_COEFFICIENT) + 1) * FileSize.KB_COEFFICIENT;
            }
            Space space = new Space(this.block, j3, j2);
            this.block.spaces.add(this);
            this.end = j3;
            return space;
        }

        public long size() {
            long j;
            synchronized (this.block) {
                j = this.end - this.start;
            }
            return j;
        }

        public final boolean tryMerge() {
            return this.block.tryMerge(this);
        }

        public final boolean tryMerge(Space space) {
            if (space.start != this.end) {
                return false;
            }
            this.end = space.end;
            return true;
        }

        public String toString() {
            return this.start + "-" + this.end;
        }
    }

    /* loaded from: classes.dex */
    public class BlockSpace {
        public final long end;
        public final String sha1;
        public final long start;
        public VerifyState verifyState;
        public final ArrayList<Space> spaces = new ArrayList<>();
        public int verifyFailCount = 0;

        public BlockSpace(IKssDownloadRequestResult.Block block, long j) {
            this.sha1 = block.sha1;
            this.start = j;
            this.end = j + block.size;
            reset();
        }

        public boolean isVerified() {
            return this.verifyState == VerifyState.VERIFIED;
        }

        public synchronized void setSpaces(long[] jArr) {
            this.spaces.clear();
            this.verifyState = VerifyState.NOT_VERIFY;
            if (jArr == null || jArr.length % 2 != 0) {
                this.spaces.add(new Space(this, this.start, this.end));
            }
            int length = jArr.length / 2;
            for (int i = 0; i < length; i++) {
                int i2 = i * 2;
                this.spaces.add(new Space(this, jArr[i2], jArr[i2 + 1]));
            }
        }

        public final synchronized void reset() {
            this.verifyState = VerifyState.NOT_VERIFY;
            this.spaces.clear();
            this.spaces.add(new Space(this, this.start, this.end));
        }

        public final synchronized boolean tryMerge(Space space) {
            if (space.size() <= 0) {
                this.spaces.remove(space);
                return true;
            }
            Iterator<Space> it = this.spaces.iterator();
            while (it.hasNext()) {
                Space next = it.next();
                if (next != space && next.tryMerge(space)) {
                    return true;
                }
            }
            return false;
        }

        public final synchronized Space[] getAllSpaces() {
            ArrayList<Space> arrayList;
            arrayList = this.spaces;
            return (Space[]) arrayList.toArray(new Space[arrayList.size()]);
        }

        public final synchronized long size() {
            long j;
            j = 0;
            Iterator<Space> it = this.spaces.iterator();
            while (it.hasNext()) {
                j += it.next().size();
            }
            return j;
        }

        public final synchronized boolean verify(KssAccessor kssAccessor, boolean z) throws IOException {
            VerifyState verifyState = this.verifyState;
            VerifyState verifyState2 = VerifyState.NOT_VERIFY;
            if (verifyState == verifyState2 && size() <= 0 && this.verifyFailCount < 2) {
                this.verifyState = VerifyState.VERIFING;
                boolean _verify = _verify(kssAccessor);
                if (!_verify) {
                    if (z) {
                        this.verifyFailCount++;
                    }
                    if (this.verifyFailCount >= 2) {
                        throw new IOException("Sha1 verify failed more than MAX_VERIFY_COUNT");
                    }
                }
                if (_verify) {
                    this.verifyState = VerifyState.VERIFIED;
                } else {
                    this.verifyState = verifyState2;
                }
                return _verify;
            }
            return true;
        }

        public final boolean _verify(KssAccessor kssAccessor) {
            kssAccessor.lock();
            boolean z = false;
            try {
                try {
                    long j = this.start;
                    String sha1 = kssAccessor.sha1(j, this.end - j);
                    if (sha1 != null) {
                        z = sha1.equalsIgnoreCase(this.sha1);
                    }
                } catch (Exception e) {
                    Log.w("LoadMap", "Meet exception when verify sha1.", e);
                }
                return z;
            } finally {
                kssAccessor.unlock();
            }
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Block(");
            sb.append(this.start);
            sb.append("-");
            sb.append(this.end);
            sb.append("):");
            if (this.spaces.isEmpty()) {
                sb.append(this.verifyState);
            } else {
                sb.append(Arrays.toString(this.spaces.toArray()));
            }
            return sb.toString();
        }
    }

    public long getBlockStart(long j) {
        if (j < 0) {
            Log.d("LoadMap", "start: " + j);
            throw new IndexOutOfBoundsException();
        }
        long j2 = -1;
        BlockSpace[] blockSpaceArr = this.mBlocks;
        int length = blockSpaceArr.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            BlockSpace blockSpace = blockSpaceArr[i];
            if (j >= blockSpace.start && j < blockSpace.end) {
                j2 = blockSpace.start;
                break;
            }
            i++;
        }
        if (j2 < 0) {
            throw new IndexOutOfBoundsException();
        }
        return j2;
    }
}
