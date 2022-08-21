package ch.qos.logback.core.rolling;

import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.joran.spi.NoAutoStart;
import ch.qos.logback.core.rolling.helper.ArchiveRemover;
import ch.qos.logback.core.rolling.helper.CompressionMode;
import ch.qos.logback.core.rolling.helper.Compressor;
import ch.qos.logback.core.rolling.helper.DefaultFileProvider;
import ch.qos.logback.core.rolling.helper.FileFilterUtil;
import ch.qos.logback.core.rolling.helper.FileNamePattern;
import ch.qos.logback.core.rolling.helper.SizeAndTimeBasedArchiveRemover;
import ch.qos.logback.core.rolling.helper.TimeBasedArchiveRemover;
import ch.qos.logback.core.util.DefaultInvocationGate;
import ch.qos.logback.core.util.FileSize;
import ch.qos.logback.core.util.InvocationGate;
import java.io.File;

@NoAutoStart
/* loaded from: classes.dex */
public class SizeAndTimeBasedFNATP<E> extends TimeBasedFileNamingAndTriggeringPolicyBase<E> {
    public static String MISSING_DATE_TOKEN = "Missing date token, that is %d, in FileNamePattern [";
    public static String MISSING_INT_TOKEN = "Missing integer token, that is %i, in FileNamePattern [";
    public int currentPeriodsCounter;
    public InvocationGate invocationGate;
    public FileSize maxFileSize;
    private final Usage usage;

    /* loaded from: classes.dex */
    public enum Usage {
        EMBEDDED,
        DIRECT
    }

    public SizeAndTimeBasedFNATP() {
        this(Usage.DIRECT);
    }

    public SizeAndTimeBasedFNATP(Usage usage) {
        this.currentPeriodsCounter = 0;
        this.invocationGate = new DefaultInvocationGate();
        this.usage = usage;
    }

    @Override // ch.qos.logback.core.rolling.TimeBasedFileNamingAndTriggeringPolicyBase, ch.qos.logback.core.spi.LifeCycle
    public void start() {
        super.start();
        if (this.usage == Usage.DIRECT) {
            addWarn(CoreConstants.SIZE_AND_TIME_BASED_FNATP_IS_DEPRECATED);
            addWarn("For more information see http://logback.qos.ch/manual/appenders.html#SizeAndTimeBasedRollingPolicy");
        }
        if (!super.isErrorFree()) {
            return;
        }
        if (this.maxFileSize == null) {
            addError("maxFileSize property is mandatory.");
            withErrors();
        }
        if (!validDateAndIntegerTokens()) {
            withErrors();
            return;
        }
        ArchiveRemover createArchiveRemover = createArchiveRemover();
        this.archiveRemover = createArchiveRemover;
        createArchiveRemover.setContext(this.context);
        ArchiveRemover createTempFileRemover = createTempFileRemover();
        this.tempFileRemover = createTempFileRemover;
        createTempFileRemover.setContext(this.context);
        computeCurrentPeriodsHighestCounterValue(FileFilterUtil.afterLastSlash(this.tbrp.fileNamePattern.toRegexForFixedDate(this.dateInCurrentPeriod)));
        if (!isErrorFree()) {
            return;
        }
        this.started = true;
    }

    private boolean validDateAndIntegerTokens() {
        boolean z;
        if (this.tbrp.fileNamePattern.getIntegerTokenConverter() == null) {
            addError(MISSING_INT_TOKEN + this.tbrp.fileNamePatternStr + "]");
            addError(CoreConstants.SEE_MISSING_INTEGER_TOKEN);
            z = true;
        } else {
            z = false;
        }
        if (this.tbrp.fileNamePattern.getPrimaryDateTokenConverter() == null) {
            addError(MISSING_DATE_TOKEN + this.tbrp.fileNamePatternStr + "]");
            z = true;
        }
        return !z;
    }

    public ArchiveRemover createArchiveRemover() {
        return new SizeAndTimeBasedArchiveRemover(this.tbrp.fileNamePattern, this.rc, new DefaultFileProvider());
    }

    public ArchiveRemover createTempFileRemover() {
        StringBuilder sb = new StringBuilder();
        TimeBasedRollingPolicy<E> timeBasedRollingPolicy = this.tbrp;
        sb.append(Compressor.computeFileNameStrWithoutCompSuffix(timeBasedRollingPolicy.fileNamePatternStr, timeBasedRollingPolicy.compressionMode));
        sb.append(".tmp");
        return new TimeBasedArchiveRemover(new FileNamePattern(sb.toString(), this.context), this.rc, new DefaultFileProvider());
    }

    public void computeCurrentPeriodsHighestCounterValue(String str) {
        File[] filesInFolderMatchingStemRegex = FileFilterUtil.filesInFolderMatchingStemRegex(new File(getCurrentPeriodsFileNameWithoutCompressionSuffix()).getParentFile(), str);
        if (filesInFolderMatchingStemRegex == null || filesInFolderMatchingStemRegex.length == 0) {
            this.currentPeriodsCounter = 0;
            return;
        }
        this.currentPeriodsCounter = FileFilterUtil.findHighestCounter(filesInFolderMatchingStemRegex, str);
        if (this.tbrp.getParentsRawFileProperty() == null && this.tbrp.compressionMode == CompressionMode.NONE) {
            return;
        }
        this.currentPeriodsCounter++;
    }

    @Override // ch.qos.logback.core.rolling.TriggeringPolicy
    public boolean isTriggeringEvent(File file, E e) {
        long currentTime = getCurrentTime();
        if (currentTime >= this.nextCheck) {
            this.elapsedPeriodsFileName = this.tbrp.fileNamePatternWithoutCompSuffix.convertMultipleArguments(this.dateInCurrentPeriod, Integer.valueOf(this.currentPeriodsCounter));
            this.currentPeriodsCounter = 0;
            setDateInCurrentPeriod(currentTime);
            computeNextCheck();
            return true;
        } else if (this.invocationGate.isTooSoon(currentTime)) {
            return false;
        } else {
            if (file == null) {
                addWarn("activeFile == null");
                return false;
            } else if (this.maxFileSize == null) {
                addWarn("maxFileSize = null");
                return false;
            } else if (file.length() < this.maxFileSize.getSize()) {
                return false;
            } else {
                this.elapsedPeriodsFileName = this.tbrp.fileNamePatternWithoutCompSuffix.convertMultipleArguments(this.dateInCurrentPeriod, Integer.valueOf(this.currentPeriodsCounter));
                this.currentPeriodsCounter++;
                return true;
            }
        }
    }

    @Override // ch.qos.logback.core.rolling.TimeBasedFileNamingAndTriggeringPolicyBase, ch.qos.logback.core.rolling.TimeBasedFileNamingAndTriggeringPolicy
    public String getCurrentPeriodsFileNameWithoutCompressionSuffix() {
        return this.tbrp.fileNamePatternWithoutCompSuffix.convertMultipleArguments(this.dateInCurrentPeriod, Integer.valueOf(this.currentPeriodsCounter));
    }

    public void setMaxFileSize(FileSize fileSize) {
        this.maxFileSize = fileSize;
    }
}
