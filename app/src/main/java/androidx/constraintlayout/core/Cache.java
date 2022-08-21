package androidx.constraintlayout.core;

/* loaded from: classes.dex */
public class Cache {
    public Pools$Pool<ArrayRow> optimizedArrayRowPool = new Pools$Pool<T>(256) { // from class: androidx.constraintlayout.core.Pools$SimplePool
        public final Object[] mPool;
        public int mPoolSize;

        {
            if (r2 <= 0) {
                throw new IllegalArgumentException("The max pool size must be > 0");
            }
            this.mPool = new Object[r2];
        }

        @Override // androidx.constraintlayout.core.Pools$Pool
        public T acquire() {
            int i = this.mPoolSize;
            if (i > 0) {
                int i2 = i - 1;
                Object[] objArr = this.mPool;
                T t = (T) objArr[i2];
                objArr[i2] = null;
                this.mPoolSize = i - 1;
                return t;
            }
            return null;
        }

        @Override // androidx.constraintlayout.core.Pools$Pool
        public boolean release(T t) {
            int i = this.mPoolSize;
            Object[] objArr = this.mPool;
            if (i < objArr.length) {
                objArr[i] = t;
                this.mPoolSize = i + 1;
                return true;
            }
            return false;
        }

        @Override // androidx.constraintlayout.core.Pools$Pool
        public void releaseAll(T[] tArr, int i) {
            if (i > tArr.length) {
                i = tArr.length;
            }
            for (int i2 = 0; i2 < i; i2++) {
                T t = tArr[i2];
                int i3 = this.mPoolSize;
                Object[] objArr = this.mPool;
                if (i3 < objArr.length) {
                    objArr[i3] = t;
                    this.mPoolSize = i3 + 1;
                }
            }
        }
    };
    public Pools$Pool<ArrayRow> arrayRowPool = new Pools$Pool<T>(256) { // from class: androidx.constraintlayout.core.Pools$SimplePool
        public final Object[] mPool;
        public int mPoolSize;

        {
            if (r2 <= 0) {
                throw new IllegalArgumentException("The max pool size must be > 0");
            }
            this.mPool = new Object[r2];
        }

        @Override // androidx.constraintlayout.core.Pools$Pool
        public T acquire() {
            int i = this.mPoolSize;
            if (i > 0) {
                int i2 = i - 1;
                Object[] objArr = this.mPool;
                T t = (T) objArr[i2];
                objArr[i2] = null;
                this.mPoolSize = i - 1;
                return t;
            }
            return null;
        }

        @Override // androidx.constraintlayout.core.Pools$Pool
        public boolean release(T t) {
            int i = this.mPoolSize;
            Object[] objArr = this.mPool;
            if (i < objArr.length) {
                objArr[i] = t;
                this.mPoolSize = i + 1;
                return true;
            }
            return false;
        }

        @Override // androidx.constraintlayout.core.Pools$Pool
        public void releaseAll(T[] tArr, int i) {
            if (i > tArr.length) {
                i = tArr.length;
            }
            for (int i2 = 0; i2 < i; i2++) {
                T t = tArr[i2];
                int i3 = this.mPoolSize;
                Object[] objArr = this.mPool;
                if (i3 < objArr.length) {
                    objArr[i3] = t;
                    this.mPoolSize = i3 + 1;
                }
            }
        }
    };
    public Pools$Pool<SolverVariable> solverVariablePool = new Pools$Pool<T>(256) { // from class: androidx.constraintlayout.core.Pools$SimplePool
        public final Object[] mPool;
        public int mPoolSize;

        {
            if (r2 <= 0) {
                throw new IllegalArgumentException("The max pool size must be > 0");
            }
            this.mPool = new Object[r2];
        }

        @Override // androidx.constraintlayout.core.Pools$Pool
        public T acquire() {
            int i = this.mPoolSize;
            if (i > 0) {
                int i2 = i - 1;
                Object[] objArr = this.mPool;
                T t = (T) objArr[i2];
                objArr[i2] = null;
                this.mPoolSize = i - 1;
                return t;
            }
            return null;
        }

        @Override // androidx.constraintlayout.core.Pools$Pool
        public boolean release(T t) {
            int i = this.mPoolSize;
            Object[] objArr = this.mPool;
            if (i < objArr.length) {
                objArr[i] = t;
                this.mPoolSize = i + 1;
                return true;
            }
            return false;
        }

        @Override // androidx.constraintlayout.core.Pools$Pool
        public void releaseAll(T[] tArr, int i) {
            if (i > tArr.length) {
                i = tArr.length;
            }
            for (int i2 = 0; i2 < i; i2++) {
                T t = tArr[i2];
                int i3 = this.mPoolSize;
                Object[] objArr = this.mPool;
                if (i3 < objArr.length) {
                    objArr[i3] = t;
                    this.mPoolSize = i3 + 1;
                }
            }
        }
    };
    public SolverVariable[] mIndexedVariables = new SolverVariable[32];
}
