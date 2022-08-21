package ch.qos.logback.core.rolling;

import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.rolling.helper.CompressionMode;
import ch.qos.logback.core.rolling.helper.FileNamePattern;
import ch.qos.logback.core.util.ContextUtil;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/* loaded from: classes.dex */
public class RollingFileAppender<E> extends FileAppender<E> {
    private static String COLLISION_URL = "http://logback.qos.ch/codes.html#rfa_collision";
    private static String MORE_INFO_PREFIX = "For more information, please visit ";
    private static String RFA_LATE_FILE_URL = "http://logback.qos.ch/codes.html#rfa_file_after";
    private static String RFA_NO_RP_URL = "http://logback.qos.ch/codes.html#rfa_no_rp";
    private static String RFA_NO_TP_URL = "http://logback.qos.ch/codes.html#rfa_no_tp";
    public File currentlyActiveFile;
    public RollingPolicy rollingPolicy;
    public TriggeringPolicy<E> triggeringPolicy;

    @Override // ch.qos.logback.core.FileAppender, ch.qos.logback.core.OutputStreamAppender, ch.qos.logback.core.UnsynchronizedAppenderBase, ch.qos.logback.core.spi.LifeCycle
    public void start() {
        TriggeringPolicy<E> triggeringPolicy = this.triggeringPolicy;
        if (triggeringPolicy == null) {
            addWarn("No TriggeringPolicy was set for the RollingFileAppender named " + getName());
            addWarn(MORE_INFO_PREFIX + RFA_NO_TP_URL);
        } else if (!triggeringPolicy.isStarted()) {
            addWarn("TriggeringPolicy has not started. RollingFileAppender will not start");
        } else if (checkForCollisionsInPreviousRollingFileAppenders()) {
            addError("Collisions detected with FileAppender/RollingAppender instances defined earlier. Aborting.");
            addError(MORE_INFO_PREFIX + FileAppender.COLLISION_WITH_EARLIER_APPENDER_URL);
        } else {
            if (!this.append) {
                addWarn("Append mode is mandatory for RollingFileAppender. Defaulting to append=true.");
                this.append = true;
            }
            if (this.rollingPolicy == null) {
                addError("No RollingPolicy was set for the RollingFileAppender named " + getName());
                addError(MORE_INFO_PREFIX + RFA_NO_RP_URL);
            } else if (checkForFileAndPatternCollisions()) {
                addError("File property collides with fileNamePattern. Aborting.");
                addError(MORE_INFO_PREFIX + COLLISION_URL);
            } else {
                if (isPrudent()) {
                    if (rawFileProperty() != null) {
                        addWarn("Setting \"File\" property to null on account of prudent mode");
                        setFile(null);
                    }
                    if (this.rollingPolicy.getCompressionMode() != CompressionMode.NONE) {
                        addError("Compression is not supported in prudent mode. Aborting");
                        return;
                    }
                }
                this.currentlyActiveFile = new File(getFile());
                addInfo("Active log file name: " + getFile());
                super.start();
            }
        }
    }

    private boolean checkForFileAndPatternCollisions() {
        FileNamePattern fileNamePattern;
        TriggeringPolicy<E> triggeringPolicy = this.triggeringPolicy;
        if (!(triggeringPolicy instanceof RollingPolicyBase) || (fileNamePattern = ((RollingPolicyBase) triggeringPolicy).fileNamePattern) == null || this.fileName == null) {
            return false;
        }
        return this.fileName.matches(fileNamePattern.toRegex());
    }

    private boolean checkForCollisionsInPreviousRollingFileAppenders() {
        TriggeringPolicy<E> triggeringPolicy = this.triggeringPolicy;
        return (triggeringPolicy instanceof RollingPolicyBase) && innerCheckForFileNamePatternCollisionInPreviousRFA(((RollingPolicyBase) triggeringPolicy).fileNamePattern);
    }

    private boolean innerCheckForFileNamePatternCollisionInPreviousRFA(FileNamePattern fileNamePattern) {
        Map map = (Map) this.context.getObject(CoreConstants.RFA_FILENAME_PATTERN_COLLISION_MAP);
        boolean z = false;
        if (map == null) {
            return false;
        }
        for (Map.Entry entry : map.entrySet()) {
            if (fileNamePattern.equals(entry.getValue())) {
                addErrorForCollision("FileNamePattern", ((FileNamePattern) entry.getValue()).toString(), (String) entry.getKey());
                z = true;
            }
        }
        if (this.name != null) {
            map.put(getName(), fileNamePattern);
        }
        return z;
    }

    @Override // ch.qos.logback.core.FileAppender, ch.qos.logback.core.OutputStreamAppender, ch.qos.logback.core.UnsynchronizedAppenderBase, ch.qos.logback.core.spi.LifeCycle
    public void stop() {
        super.stop();
        RollingPolicy rollingPolicy = this.rollingPolicy;
        if (rollingPolicy != null) {
            rollingPolicy.stop();
        }
        TriggeringPolicy<E> triggeringPolicy = this.triggeringPolicy;
        if (triggeringPolicy != null) {
            triggeringPolicy.stop();
        }
        Map<String, FileNamePattern> filenamePatternCollisionMap = ContextUtil.getFilenamePatternCollisionMap(this.context);
        if (filenamePatternCollisionMap == null || getName() == null) {
            return;
        }
        filenamePatternCollisionMap.remove(getName());
    }

    @Override // ch.qos.logback.core.FileAppender
    public void setFile(String str) {
        if (str != null && (this.triggeringPolicy != null || this.rollingPolicy != null)) {
            addError("File property must be set before any triggeringPolicy or rollingPolicy properties");
            addError(MORE_INFO_PREFIX + RFA_LATE_FILE_URL);
        }
        super.setFile(str);
    }

    @Override // ch.qos.logback.core.FileAppender
    public String getFile() {
        return this.rollingPolicy.getActiveFileName();
    }

    public void rollover() {
        this.lock.lock();
        try {
            closeOutputStream();
            attemptRollover();
            attemptOpenFile();
        } finally {
            this.lock.unlock();
        }
    }

    private void attemptOpenFile() {
        String activeFileName = this.rollingPolicy.getActiveFileName();
        try {
            this.currentlyActiveFile = new File(activeFileName);
            openFile(activeFileName);
        } catch (IOException e) {
            addError("setFile(" + activeFileName + ", false) call failed.", e);
        }
    }

    private void attemptRollover() {
        try {
            this.rollingPolicy.rollover();
        } catch (RolloverFailure unused) {
            addWarn("RolloverFailure occurred. Deferring roll-over.");
            this.append = true;
        }
    }

    @Override // ch.qos.logback.core.FileAppender, ch.qos.logback.core.OutputStreamAppender
    public void subAppend(E e) {
        synchronized (this.triggeringPolicy) {
            if (this.triggeringPolicy.isTriggeringEvent(this.currentlyActiveFile, e)) {
                rollover();
            }
        }
        super.subAppend(e);
    }

    public RollingPolicy getRollingPolicy() {
        return this.rollingPolicy;
    }

    public TriggeringPolicy<E> getTriggeringPolicy() {
        return this.triggeringPolicy;
    }

    public void setRollingPolicy(RollingPolicy rollingPolicy) {
        this.rollingPolicy = rollingPolicy;
        if (rollingPolicy instanceof TriggeringPolicy) {
            this.triggeringPolicy = (TriggeringPolicy) rollingPolicy;
        }
    }

    public void setTriggeringPolicy(TriggeringPolicy<E> triggeringPolicy) {
        this.triggeringPolicy = triggeringPolicy;
        if (triggeringPolicy instanceof RollingPolicy) {
            this.rollingPolicy = (RollingPolicy) triggeringPolicy;
        }
    }
}
