package ch.qos.logback.core.rolling;

import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.rolling.helper.ArchiveRemover;
import ch.qos.logback.core.rolling.helper.CompressionMode;
import ch.qos.logback.core.rolling.helper.Compressor;
import ch.qos.logback.core.rolling.helper.FileFilterUtil;
import ch.qos.logback.core.rolling.helper.FileNamePattern;
import ch.qos.logback.core.rolling.helper.RenameUtil;
import ch.qos.logback.core.util.FileSize;
import java.io.File;
import java.util.Date;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/* loaded from: classes.dex */
public class TimeBasedRollingPolicy<E> extends RollingPolicyBase implements TriggeringPolicy<E> {
    public static final String FNP_NOT_SET = "The FileNamePattern option must be set before using TimeBasedRollingPolicy. ";
    private ArchiveRemover archiveRemover;
    public Future<?> cleanUpFuture;
    public Future<?> compressionFuture;
    private Compressor compressor;
    public FileNamePattern fileNamePatternWithoutCompSuffix;
    public Future<?> tempFileCleanUpFuture;
    private ArchiveRemover tempFileRemover;
    public TimeBasedFileNamingAndTriggeringPolicy<E> timeBasedFileNamingAndTriggeringPolicy;
    private RenameUtil renameUtil = new RenameUtil();
    private int maxHistory = 0;
    public FileSize totalSizeCap = new FileSize(0);
    public boolean cleanHistoryOnStart = false;

    @Override // ch.qos.logback.core.rolling.RollingPolicyBase, ch.qos.logback.core.spi.LifeCycle
    public void start() {
        this.renameUtil.setContext(this.context);
        if (this.fileNamePatternStr != null) {
            this.fileNamePattern = new FileNamePattern(this.fileNamePatternStr, this.context);
            determineCompressionMode();
            Compressor compressor = new Compressor(this.compressionMode);
            this.compressor = compressor;
            compressor.setContext(this.context);
            this.fileNamePatternWithoutCompSuffix = new FileNamePattern(Compressor.computeFileNameStrWithoutCompSuffix(this.fileNamePatternStr, this.compressionMode), this.context);
            addInfo("Will use the pattern " + this.fileNamePatternWithoutCompSuffix + " for the active file");
            if (this.compressionMode == CompressionMode.ZIP) {
                this.zipEntryFileNamePattern = new FileNamePattern(transformFileNamePattern2ZipEntry(this.fileNamePatternStr), this.context);
            }
            if (this.timeBasedFileNamingAndTriggeringPolicy == null) {
                this.timeBasedFileNamingAndTriggeringPolicy = new DefaultTimeBasedFileNamingAndTriggeringPolicy();
            }
            this.timeBasedFileNamingAndTriggeringPolicy.setContext(this.context);
            this.timeBasedFileNamingAndTriggeringPolicy.setTimeBasedRollingPolicy(this);
            this.timeBasedFileNamingAndTriggeringPolicy.start();
            if (!this.timeBasedFileNamingAndTriggeringPolicy.isStarted()) {
                addWarn("Subcomponent did not start. TimeBasedRollingPolicy will not start.");
                return;
            }
            if (this.maxHistory != 0) {
                ArchiveRemover archiveRemover = this.timeBasedFileNamingAndTriggeringPolicy.getArchiveRemover();
                this.archiveRemover = archiveRemover;
                archiveRemover.setMaxHistory(this.maxHistory);
                this.archiveRemover.setTotalSizeCap(this.totalSizeCap.getSize());
                ArchiveRemover tempFileRemover = this.timeBasedFileNamingAndTriggeringPolicy.getTempFileRemover();
                this.tempFileRemover = tempFileRemover;
                tempFileRemover.setMaxHistory(1);
                this.tempFileRemover.setTotalSizeCap(this.totalSizeCap.getSize());
                if (this.cleanHistoryOnStart) {
                    addInfo("Cleaning on start up");
                    Date date = new Date(this.timeBasedFileNamingAndTriggeringPolicy.getCurrentTime());
                    this.cleanUpFuture = this.archiveRemover.cleanAsynchronously(date);
                    addInfo("Cleaning temp file on start up");
                    this.tempFileCleanUpFuture = this.tempFileRemover.cleanAsynchronously(date);
                }
            } else if (!isUnboundedTotalSizeCap()) {
                addWarn("'maxHistory' is not set, ignoring 'totalSizeCap' option with value [" + this.totalSizeCap + "]");
            }
            super.start();
            return;
        }
        addWarn(FNP_NOT_SET);
        addWarn(CoreConstants.SEE_FNP_NOT_SET);
        throw new IllegalStateException("The FileNamePattern option must be set before using TimeBasedRollingPolicy. See also http://logback.qos.ch/codes.html#tbr_fnp_not_set");
    }

    public boolean isUnboundedTotalSizeCap() {
        return this.totalSizeCap.getSize() == 0;
    }

    @Override // ch.qos.logback.core.rolling.RollingPolicyBase, ch.qos.logback.core.spi.LifeCycle
    public void stop() {
        if (!isStarted()) {
            return;
        }
        waitForAsynchronousJobToStop(this.compressionFuture, "compression");
        waitForAsynchronousJobToStop(this.cleanUpFuture, "clean-up");
        waitForAsynchronousJobToStop(this.tempFileCleanUpFuture, "temp-file-clean-up");
        super.stop();
    }

    private void waitForAsynchronousJobToStop(Future<?> future, String str) {
        if (future != null) {
            try {
                future.get(30L, TimeUnit.SECONDS);
            } catch (TimeoutException e) {
                addError("Timeout while waiting for " + str + " job to finish", e);
            } catch (Exception e2) {
                addError("Unexpected exception while waiting for " + str + " job to finish", e2);
            }
        }
    }

    private String transformFileNamePattern2ZipEntry(String str) {
        return FileFilterUtil.afterLastSlash(FileFilterUtil.slashify(str));
    }

    public void setTimeBasedFileNamingAndTriggeringPolicy(TimeBasedFileNamingAndTriggeringPolicy<E> timeBasedFileNamingAndTriggeringPolicy) {
        this.timeBasedFileNamingAndTriggeringPolicy = timeBasedFileNamingAndTriggeringPolicy;
    }

    public TimeBasedFileNamingAndTriggeringPolicy<E> getTimeBasedFileNamingAndTriggeringPolicy() {
        return this.timeBasedFileNamingAndTriggeringPolicy;
    }

    @Override // ch.qos.logback.core.rolling.RollingPolicy
    public void rollover() throws RolloverFailure {
        String elapsedPeriodsFileName = this.timeBasedFileNamingAndTriggeringPolicy.getElapsedPeriodsFileName();
        String afterLastSlash = FileFilterUtil.afterLastSlash(elapsedPeriodsFileName);
        if (this.compressionMode == CompressionMode.NONE) {
            if (getParentsRawFileProperty() != null) {
                this.renameUtil.rename(getParentsRawFileProperty(), elapsedPeriodsFileName);
            }
        } else if (getParentsRawFileProperty() == null) {
            this.compressionFuture = this.compressor.asyncCompress(elapsedPeriodsFileName, elapsedPeriodsFileName, afterLastSlash);
        } else {
            this.compressionFuture = renameRawAndAsyncCompress(elapsedPeriodsFileName, afterLastSlash);
        }
        if (this.archiveRemover != null) {
            this.cleanUpFuture = this.archiveRemover.cleanAsynchronously(new Date(this.timeBasedFileNamingAndTriggeringPolicy.getCurrentTime()));
        }
    }

    public Future<?> renameRawAndAsyncCompress(String str, String str2) throws RolloverFailure {
        String str3 = str + System.nanoTime() + ".tmp";
        this.renameUtil.rename(getParentsRawFileProperty(), str3);
        return this.compressor.asyncCompress(str3, str, str2);
    }

    @Override // ch.qos.logback.core.rolling.RollingPolicy
    public String getActiveFileName() {
        String parentsRawFileProperty = getParentsRawFileProperty();
        return parentsRawFileProperty != null ? parentsRawFileProperty : this.timeBasedFileNamingAndTriggeringPolicy.getCurrentPeriodsFileNameWithoutCompressionSuffix();
    }

    @Override // ch.qos.logback.core.rolling.TriggeringPolicy
    public boolean isTriggeringEvent(File file, E e) {
        return this.timeBasedFileNamingAndTriggeringPolicy.isTriggeringEvent(file, e);
    }

    public int getMaxHistory() {
        return this.maxHistory;
    }

    public void setMaxHistory(int i) {
        this.maxHistory = i;
    }

    public boolean isCleanHistoryOnStart() {
        return this.cleanHistoryOnStart;
    }

    public void setCleanHistoryOnStart(boolean z) {
        this.cleanHistoryOnStart = z;
    }

    public String toString() {
        return "c.q.l.core.rolling.TimeBasedRollingPolicy@" + hashCode();
    }

    public void setTotalSizeCap(FileSize fileSize) {
        addInfo("setting totalSizeCap to " + fileSize.toString());
        this.totalSizeCap = fileSize;
    }
}
