package androidx.work;

import android.annotation.SuppressLint;
import androidx.work.WorkInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/* loaded from: classes.dex */
public final class WorkQuery {
    public final List<UUID> mIds;
    public final List<WorkInfo.State> mStates;
    public final List<String> mTags;
    public final List<String> mUniqueWorkNames;

    public WorkQuery(Builder builder) {
        this.mIds = builder.mIds;
        this.mUniqueWorkNames = builder.mUniqueWorkNames;
        this.mTags = builder.mTags;
        this.mStates = builder.mStates;
    }

    public List<UUID> getIds() {
        return this.mIds;
    }

    public List<String> getUniqueWorkNames() {
        return this.mUniqueWorkNames;
    }

    public List<String> getTags() {
        return this.mTags;
    }

    public List<WorkInfo.State> getStates() {
        return this.mStates;
    }

    /* loaded from: classes.dex */
    public static final class Builder {
        public List<UUID> mIds = new ArrayList();
        public List<String> mUniqueWorkNames = new ArrayList();
        public List<String> mTags = new ArrayList();
        public List<WorkInfo.State> mStates = new ArrayList();

        @SuppressLint({"BuilderSetStyle"})
        public static Builder fromUniqueWorkNames(List<String> uniqueWorkNames) {
            Builder builder = new Builder();
            builder.addUniqueWorkNames(uniqueWorkNames);
            return builder;
        }

        public Builder addUniqueWorkNames(List<String> uniqueWorkNames) {
            this.mUniqueWorkNames.addAll(uniqueWorkNames);
            return this;
        }

        public Builder addTags(List<String> tags) {
            this.mTags.addAll(tags);
            return this;
        }

        public Builder addStates(List<WorkInfo.State> states) {
            this.mStates.addAll(states);
            return this;
        }

        public WorkQuery build() {
            if (this.mIds.isEmpty() && this.mUniqueWorkNames.isEmpty() && this.mTags.isEmpty() && this.mStates.isEmpty()) {
                throw new IllegalArgumentException("Must specify ids, uniqueNames, tags or states when building a WorkQuery");
            }
            return new WorkQuery(this);
        }
    }
}
