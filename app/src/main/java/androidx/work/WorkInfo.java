package androidx.work;

import ch.qos.logback.core.CoreConstants;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/* loaded from: classes.dex */
public final class WorkInfo {
    public UUID mId;
    public Data mOutputData;
    public Data mProgress;
    public int mRunAttemptCount;
    public State mState;
    public Set<String> mTags;

    public WorkInfo(UUID id, State state, Data outputData, List<String> tags, Data progress, int runAttemptCount) {
        this.mId = id;
        this.mState = state;
        this.mOutputData = outputData;
        this.mTags = new HashSet(tags);
        this.mProgress = progress;
        this.mRunAttemptCount = runAttemptCount;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || WorkInfo.class != o.getClass()) {
            return false;
        }
        WorkInfo workInfo = (WorkInfo) o;
        if (this.mRunAttemptCount != workInfo.mRunAttemptCount || !this.mId.equals(workInfo.mId) || this.mState != workInfo.mState || !this.mOutputData.equals(workInfo.mOutputData) || !this.mTags.equals(workInfo.mTags)) {
            return false;
        }
        return this.mProgress.equals(workInfo.mProgress);
    }

    public int hashCode() {
        return (((((((((this.mId.hashCode() * 31) + this.mState.hashCode()) * 31) + this.mOutputData.hashCode()) * 31) + this.mTags.hashCode()) * 31) + this.mProgress.hashCode()) * 31) + this.mRunAttemptCount;
    }

    public String toString() {
        return "WorkInfo{mId='" + this.mId + CoreConstants.SINGLE_QUOTE_CHAR + ", mState=" + this.mState + ", mOutputData=" + this.mOutputData + ", mTags=" + this.mTags + ", mProgress=" + this.mProgress + '}';
    }

    /* loaded from: classes.dex */
    public enum State {
        ENQUEUED,
        RUNNING,
        SUCCEEDED,
        FAILED,
        BLOCKED,
        CANCELLED;

        public boolean isFinished() {
            return this == SUCCEEDED || this == FAILED || this == CANCELLED;
        }
    }
}
