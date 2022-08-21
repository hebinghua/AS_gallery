package androidx.work;

import android.os.Build;
import androidx.work.impl.model.WorkSpec;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/* loaded from: classes.dex */
public abstract class WorkRequest {
    public UUID mId;
    public Set<String> mTags;
    public WorkSpec mWorkSpec;

    public WorkRequest(UUID id, WorkSpec workSpec, Set<String> tags) {
        this.mId = id;
        this.mWorkSpec = workSpec;
        this.mTags = tags;
    }

    public String getStringId() {
        return this.mId.toString();
    }

    public WorkSpec getWorkSpec() {
        return this.mWorkSpec;
    }

    public Set<String> getTags() {
        return this.mTags;
    }

    /* loaded from: classes.dex */
    public static abstract class Builder<B extends Builder<?, ?>, W extends WorkRequest> {
        public WorkSpec mWorkSpec;
        public Class<? extends ListenableWorker> mWorkerClass;
        public boolean mBackoffCriteriaSet = false;
        public Set<String> mTags = new HashSet();
        public UUID mId = UUID.randomUUID();

        /* renamed from: buildInternal */
        public abstract W mo159buildInternal();

        /* renamed from: getThis */
        public abstract B mo160getThis();

        public Builder(Class<? extends ListenableWorker> workerClass) {
            this.mWorkerClass = workerClass;
            this.mWorkSpec = new WorkSpec(this.mId.toString(), workerClass.getName());
            addTag(workerClass.getName());
        }

        public final B setConstraints(Constraints constraints) {
            this.mWorkSpec.constraints = constraints;
            return mo160getThis();
        }

        public final B setInputData(Data inputData) {
            this.mWorkSpec.input = inputData;
            return mo160getThis();
        }

        public final B addTag(String tag) {
            this.mTags.add(tag);
            return mo160getThis();
        }

        public B setInitialDelay(Duration duration) {
            this.mWorkSpec.initialDelay = duration.toMillis();
            if (Long.MAX_VALUE - System.currentTimeMillis() <= this.mWorkSpec.initialDelay) {
                throw new IllegalArgumentException("The given initial delay is too large and will cause an overflow!");
            }
            return mo160getThis();
        }

        public final W build() {
            W mo159buildInternal = mo159buildInternal();
            Constraints constraints = this.mWorkSpec.constraints;
            int i = Build.VERSION.SDK_INT;
            boolean z = (i >= 24 && constraints.hasContentUriTriggers()) || constraints.requiresBatteryNotLow() || constraints.requiresCharging() || (i >= 23 && constraints.requiresDeviceIdle());
            WorkSpec workSpec = this.mWorkSpec;
            if (workSpec.expedited) {
                if (z) {
                    throw new IllegalArgumentException("Expedited jobs only support network and storage constraints");
                }
                if (workSpec.initialDelay > 0) {
                    throw new IllegalArgumentException("Expedited jobs cannot be delayed");
                }
            }
            this.mId = UUID.randomUUID();
            WorkSpec workSpec2 = new WorkSpec(this.mWorkSpec);
            this.mWorkSpec = workSpec2;
            workSpec2.id = this.mId.toString();
            return mo159buildInternal;
        }
    }
}
