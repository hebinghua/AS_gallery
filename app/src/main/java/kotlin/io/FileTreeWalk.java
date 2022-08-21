package kotlin.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Iterator;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Unit;
import kotlin.collections.AbstractIterator;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;

/* compiled from: FileTreeWalk.kt */
/* loaded from: classes3.dex */
public final class FileTreeWalk implements Sequence<File> {
    public final FileWalkDirection direction;
    public final int maxDepth;
    public final Function1<File, Boolean> onEnter;
    public final Function2<File, IOException, Unit> onFail;
    public final Function1<File, Unit> onLeave;
    public final File start;

    /* JADX WARN: Multi-variable type inference failed */
    public FileTreeWalk(File file, FileWalkDirection fileWalkDirection, Function1<? super File, Boolean> function1, Function1<? super File, Unit> function12, Function2<? super File, ? super IOException, Unit> function2, int i) {
        this.start = file;
        this.direction = fileWalkDirection;
        this.onEnter = function1;
        this.onLeave = function12;
        this.onFail = function2;
        this.maxDepth = i;
    }

    public /* synthetic */ FileTreeWalk(File file, FileWalkDirection fileWalkDirection, Function1 function1, Function1 function12, Function2 function2, int i, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(file, (i2 & 2) != 0 ? FileWalkDirection.TOP_DOWN : fileWalkDirection, function1, function12, function2, (i2 & 32) != 0 ? Integer.MAX_VALUE : i);
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public FileTreeWalk(File start, FileWalkDirection direction) {
        this(start, direction, null, null, null, 0, 32, null);
        Intrinsics.checkNotNullParameter(start, "start");
        Intrinsics.checkNotNullParameter(direction, "direction");
    }

    @Override // kotlin.sequences.Sequence
    public Iterator<File> iterator() {
        return new FileTreeWalkIterator(this);
    }

    /* compiled from: FileTreeWalk.kt */
    /* loaded from: classes3.dex */
    public static abstract class WalkState {
        public final File root;

        public abstract File step();

        public WalkState(File root) {
            Intrinsics.checkNotNullParameter(root, "root");
            this.root = root;
        }

        public final File getRoot() {
            return this.root;
        }
    }

    /* compiled from: FileTreeWalk.kt */
    /* loaded from: classes3.dex */
    public static abstract class DirectoryState extends WalkState {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public DirectoryState(File rootDir) {
            super(rootDir);
            Intrinsics.checkNotNullParameter(rootDir, "rootDir");
        }
    }

    /* compiled from: FileTreeWalk.kt */
    /* loaded from: classes3.dex */
    public final class FileTreeWalkIterator extends AbstractIterator<File> {
        public final ArrayDeque<WalkState> state;
        public final /* synthetic */ FileTreeWalk this$0;

        /* compiled from: FileTreeWalk.kt */
        /* loaded from: classes3.dex */
        public /* synthetic */ class WhenMappings {
            public static final /* synthetic */ int[] $EnumSwitchMapping$0;

            static {
                int[] iArr = new int[FileWalkDirection.values().length];
                iArr[FileWalkDirection.TOP_DOWN.ordinal()] = 1;
                iArr[FileWalkDirection.BOTTOM_UP.ordinal()] = 2;
                $EnumSwitchMapping$0 = iArr;
            }
        }

        public FileTreeWalkIterator(FileTreeWalk this$0) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            this.this$0 = this$0;
            ArrayDeque<WalkState> arrayDeque = new ArrayDeque<>();
            this.state = arrayDeque;
            if (this$0.start.isDirectory()) {
                arrayDeque.push(directoryState(this$0.start));
            } else if (this$0.start.isFile()) {
                arrayDeque.push(new SingleFileState(this, this$0.start));
            } else {
                done();
            }
        }

        @Override // kotlin.collections.AbstractIterator
        public void computeNext() {
            File gotoNext = gotoNext();
            if (gotoNext != null) {
                setNext(gotoNext);
            } else {
                done();
            }
        }

        public final DirectoryState directoryState(File file) {
            int i = WhenMappings.$EnumSwitchMapping$0[this.this$0.direction.ordinal()];
            if (i != 1) {
                if (i != 2) {
                    throw new NoWhenBranchMatchedException();
                }
                return new BottomUpDirectoryState(this, file);
            }
            return new TopDownDirectoryState(this, file);
        }

        public final File gotoNext() {
            File step;
            while (true) {
                WalkState peek = this.state.peek();
                if (peek == null) {
                    return null;
                }
                step = peek.step();
                if (step == null) {
                    this.state.pop();
                } else if (Intrinsics.areEqual(step, peek.getRoot()) || !step.isDirectory() || this.state.size() >= this.this$0.maxDepth) {
                    break;
                } else {
                    this.state.push(directoryState(step));
                }
            }
            return step;
        }

        /* compiled from: FileTreeWalk.kt */
        /* loaded from: classes3.dex */
        public final class BottomUpDirectoryState extends DirectoryState {
            public boolean failed;
            public int fileIndex;
            public File[] fileList;
            public boolean rootVisited;
            public final /* synthetic */ FileTreeWalkIterator this$0;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public BottomUpDirectoryState(FileTreeWalkIterator this$0, File rootDir) {
                super(rootDir);
                Intrinsics.checkNotNullParameter(this$0, "this$0");
                Intrinsics.checkNotNullParameter(rootDir, "rootDir");
                this.this$0 = this$0;
            }

            @Override // kotlin.io.FileTreeWalk.WalkState
            public File step() {
                if (!this.failed && this.fileList == null) {
                    Function1 function1 = this.this$0.this$0.onEnter;
                    boolean z = false;
                    if (function1 != null && !((Boolean) function1.mo2577invoke(getRoot())).booleanValue()) {
                        z = true;
                    }
                    if (z) {
                        return null;
                    }
                    File[] listFiles = getRoot().listFiles();
                    this.fileList = listFiles;
                    if (listFiles == null) {
                        Function2 function2 = this.this$0.this$0.onFail;
                        if (function2 != null) {
                            function2.invoke(getRoot(), new AccessDeniedException(getRoot(), null, "Cannot list files in a directory", 2, null));
                        }
                        this.failed = true;
                    }
                }
                File[] fileArr = this.fileList;
                if (fileArr != null) {
                    int i = this.fileIndex;
                    Intrinsics.checkNotNull(fileArr);
                    if (i < fileArr.length) {
                        File[] fileArr2 = this.fileList;
                        Intrinsics.checkNotNull(fileArr2);
                        int i2 = this.fileIndex;
                        this.fileIndex = i2 + 1;
                        return fileArr2[i2];
                    }
                }
                if (this.rootVisited) {
                    Function1 function12 = this.this$0.this$0.onLeave;
                    if (function12 != null) {
                        function12.mo2577invoke(getRoot());
                    }
                    return null;
                }
                this.rootVisited = true;
                return getRoot();
            }
        }

        /* compiled from: FileTreeWalk.kt */
        /* loaded from: classes3.dex */
        public final class TopDownDirectoryState extends DirectoryState {
            public int fileIndex;
            public File[] fileList;
            public boolean rootVisited;
            public final /* synthetic */ FileTreeWalkIterator this$0;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public TopDownDirectoryState(FileTreeWalkIterator this$0, File rootDir) {
                super(rootDir);
                Intrinsics.checkNotNullParameter(this$0, "this$0");
                Intrinsics.checkNotNullParameter(rootDir, "rootDir");
                this.this$0 = this$0;
            }

            /* JADX WARN: Code restructure failed: missing block: B:35:0x0086, code lost:
                if (r0.length == 0) goto L32;
             */
            @Override // kotlin.io.FileTreeWalk.WalkState
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public java.io.File step() {
                /*
                    r10 = this;
                    boolean r0 = r10.rootVisited
                    r1 = 0
                    if (r0 != 0) goto L2d
                    kotlin.io.FileTreeWalk$FileTreeWalkIterator r0 = r10.this$0
                    kotlin.io.FileTreeWalk r0 = r0.this$0
                    kotlin.jvm.functions.Function1 r0 = kotlin.io.FileTreeWalk.access$getOnEnter$p(r0)
                    r2 = 0
                    r3 = 1
                    if (r0 != 0) goto L12
                    goto L23
                L12:
                    java.io.File r4 = r10.getRoot()
                    java.lang.Object r0 = r0.mo2577invoke(r4)
                    java.lang.Boolean r0 = (java.lang.Boolean) r0
                    boolean r0 = r0.booleanValue()
                    if (r0 != 0) goto L23
                    r2 = r3
                L23:
                    if (r2 == 0) goto L26
                    return r1
                L26:
                    r10.rootVisited = r3
                    java.io.File r0 = r10.getRoot()
                    return r0
                L2d:
                    java.io.File[] r0 = r10.fileList
                    if (r0 == 0) goto L4d
                    int r2 = r10.fileIndex
                    kotlin.jvm.internal.Intrinsics.checkNotNull(r0)
                    int r0 = r0.length
                    if (r2 >= r0) goto L3a
                    goto L4d
                L3a:
                    kotlin.io.FileTreeWalk$FileTreeWalkIterator r0 = r10.this$0
                    kotlin.io.FileTreeWalk r0 = r0.this$0
                    kotlin.jvm.functions.Function1 r0 = kotlin.io.FileTreeWalk.access$getOnLeave$p(r0)
                    if (r0 != 0) goto L45
                    goto L4c
                L45:
                    java.io.File r2 = r10.getRoot()
                    r0.mo2577invoke(r2)
                L4c:
                    return r1
                L4d:
                    java.io.File[] r0 = r10.fileList
                    if (r0 != 0) goto L9b
                    java.io.File r0 = r10.getRoot()
                    java.io.File[] r0 = r0.listFiles()
                    r10.fileList = r0
                    if (r0 != 0) goto L7e
                    kotlin.io.FileTreeWalk$FileTreeWalkIterator r0 = r10.this$0
                    kotlin.io.FileTreeWalk r0 = r0.this$0
                    kotlin.jvm.functions.Function2 r0 = kotlin.io.FileTreeWalk.access$getOnFail$p(r0)
                    if (r0 != 0) goto L68
                    goto L7e
                L68:
                    java.io.File r2 = r10.getRoot()
                    kotlin.io.AccessDeniedException r9 = new kotlin.io.AccessDeniedException
                    java.io.File r4 = r10.getRoot()
                    r5 = 0
                    r7 = 2
                    r8 = 0
                    java.lang.String r6 = "Cannot list files in a directory"
                    r3 = r9
                    r3.<init>(r4, r5, r6, r7, r8)
                    r0.invoke(r2, r9)
                L7e:
                    java.io.File[] r0 = r10.fileList
                    if (r0 == 0) goto L88
                    kotlin.jvm.internal.Intrinsics.checkNotNull(r0)
                    int r0 = r0.length
                    if (r0 != 0) goto L9b
                L88:
                    kotlin.io.FileTreeWalk$FileTreeWalkIterator r0 = r10.this$0
                    kotlin.io.FileTreeWalk r0 = r0.this$0
                    kotlin.jvm.functions.Function1 r0 = kotlin.io.FileTreeWalk.access$getOnLeave$p(r0)
                    if (r0 != 0) goto L93
                    goto L9a
                L93:
                    java.io.File r2 = r10.getRoot()
                    r0.mo2577invoke(r2)
                L9a:
                    return r1
                L9b:
                    java.io.File[] r0 = r10.fileList
                    kotlin.jvm.internal.Intrinsics.checkNotNull(r0)
                    int r1 = r10.fileIndex
                    int r2 = r1 + 1
                    r10.fileIndex = r2
                    r0 = r0[r1]
                    return r0
                */
                throw new UnsupportedOperationException("Method not decompiled: kotlin.io.FileTreeWalk.FileTreeWalkIterator.TopDownDirectoryState.step():java.io.File");
            }
        }

        /* compiled from: FileTreeWalk.kt */
        /* loaded from: classes3.dex */
        public final class SingleFileState extends WalkState {
            public final /* synthetic */ FileTreeWalkIterator this$0;
            public boolean visited;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public SingleFileState(FileTreeWalkIterator this$0, File rootFile) {
                super(rootFile);
                Intrinsics.checkNotNullParameter(this$0, "this$0");
                Intrinsics.checkNotNullParameter(rootFile, "rootFile");
                this.this$0 = this$0;
            }

            @Override // kotlin.io.FileTreeWalk.WalkState
            public File step() {
                if (this.visited) {
                    return null;
                }
                this.visited = true;
                return getRoot();
            }
        }
    }
}
