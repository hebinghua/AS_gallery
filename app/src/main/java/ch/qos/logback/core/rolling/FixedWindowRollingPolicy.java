package ch.qos.logback.core.rolling;

import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.rolling.helper.CompressionMode;
import ch.qos.logback.core.rolling.helper.Compressor;
import ch.qos.logback.core.rolling.helper.FileFilterUtil;
import ch.qos.logback.core.rolling.helper.FileNamePattern;
import ch.qos.logback.core.rolling.helper.RenameUtil;
import java.io.File;
import java.util.Date;

/* loaded from: classes.dex */
public class FixedWindowRollingPolicy extends RollingPolicyBase {
    public static final String FNP_NOT_SET = "The \"FileNamePattern\" property must be set before using FixedWindowRollingPolicy. ";
    private static int MAX_WINDOW_SIZE = 20;
    public static final String PRUDENT_MODE_UNSUPPORTED = "See also http://logback.qos.ch/codes.html#tbr_fnp_prudent_unsupported";
    public static final String SEE_PARENT_FN_NOT_SET = "Please refer to http://logback.qos.ch/codes.html#fwrp_parentFileName_not_set";
    public static final String ZIP_ENTRY_DATE_PATTERN = "yyyy-MM-dd_HHmm";
    public Compressor compressor;
    public RenameUtil util = new RenameUtil();
    public int minIndex = 1;
    public int maxIndex = 7;

    @Override // ch.qos.logback.core.rolling.RollingPolicyBase, ch.qos.logback.core.spi.LifeCycle
    public void start() {
        this.util.setContext(this.context);
        if (this.fileNamePatternStr != null) {
            this.fileNamePattern = new FileNamePattern(this.fileNamePatternStr, this.context);
            determineCompressionMode();
            if (isParentPrudent()) {
                addError("Prudent mode is not supported with FixedWindowRollingPolicy.");
                addError(PRUDENT_MODE_UNSUPPORTED);
                throw new IllegalStateException("Prudent mode is not supported.");
            } else if (getParentsRawFileProperty() == null) {
                addError("The File name property must be set before using this rolling policy.");
                addError(SEE_PARENT_FN_NOT_SET);
                throw new IllegalStateException("The \"File\" option must be set.");
            } else {
                if (this.maxIndex < this.minIndex) {
                    addWarn("MaxIndex (" + this.maxIndex + ") cannot be smaller than MinIndex (" + this.minIndex + ").");
                    addWarn("Setting maxIndex to equal minIndex.");
                    this.maxIndex = this.minIndex;
                }
                int maxWindowSize = getMaxWindowSize();
                if (this.maxIndex - this.minIndex > maxWindowSize) {
                    addWarn("Large window sizes are not allowed.");
                    this.maxIndex = this.minIndex + maxWindowSize;
                    addWarn("MaxIndex reduced to " + this.maxIndex);
                }
                if (this.fileNamePattern.getIntegerTokenConverter() == null) {
                    throw new IllegalStateException("FileNamePattern [" + this.fileNamePattern.getPattern() + "] does not contain a valid IntegerToken");
                }
                if (this.compressionMode == CompressionMode.ZIP) {
                    this.zipEntryFileNamePattern = new FileNamePattern(transformFileNamePatternFromInt2Date(this.fileNamePatternStr), this.context);
                }
                Compressor compressor = new Compressor(this.compressionMode);
                this.compressor = compressor;
                compressor.setContext(this.context);
                super.start();
                return;
            }
        }
        addError(FNP_NOT_SET);
        addError(CoreConstants.SEE_FNP_NOT_SET);
        throw new IllegalStateException("The \"FileNamePattern\" property must be set before using FixedWindowRollingPolicy. See also http://logback.qos.ch/codes.html#tbr_fnp_not_set");
    }

    public int getMaxWindowSize() {
        return MAX_WINDOW_SIZE;
    }

    private String transformFileNamePatternFromInt2Date(String str) {
        return FileFilterUtil.afterLastSlash(FileFilterUtil.slashify(str)).replace("%i", "%d{yyyy-MM-dd_HHmm}");
    }

    @Override // ch.qos.logback.core.rolling.RollingPolicy
    public void rollover() throws RolloverFailure {
        if (this.maxIndex >= 0) {
            File file = new File(this.fileNamePattern.convertInt(this.maxIndex));
            if (file.exists()) {
                file.delete();
            }
            for (int i = this.maxIndex - 1; i >= this.minIndex; i--) {
                String convertInt = this.fileNamePattern.convertInt(i);
                if (new File(convertInt).exists()) {
                    this.util.rename(convertInt, this.fileNamePattern.convertInt(i + 1));
                } else {
                    addInfo("Skipping roll-over for inexistent file " + convertInt);
                }
            }
            int i2 = AnonymousClass1.$SwitchMap$ch$qos$logback$core$rolling$helper$CompressionMode[this.compressionMode.ordinal()];
            if (i2 == 1) {
                this.util.rename(getActiveFileName(), this.fileNamePattern.convertInt(this.minIndex));
            } else if (i2 == 2) {
                this.compressor.compress(getActiveFileName(), this.fileNamePattern.convertInt(this.minIndex), null);
            } else if (i2 == 3) {
                this.compressor.compress(getActiveFileName(), this.fileNamePattern.convertInt(this.minIndex), this.zipEntryFileNamePattern.convert(new Date()));
            }
        }
    }

    /* renamed from: ch.qos.logback.core.rolling.FixedWindowRollingPolicy$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$ch$qos$logback$core$rolling$helper$CompressionMode;

        static {
            int[] iArr = new int[CompressionMode.values().length];
            $SwitchMap$ch$qos$logback$core$rolling$helper$CompressionMode = iArr;
            try {
                iArr[CompressionMode.NONE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$ch$qos$logback$core$rolling$helper$CompressionMode[CompressionMode.GZ.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$ch$qos$logback$core$rolling$helper$CompressionMode[CompressionMode.ZIP.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    @Override // ch.qos.logback.core.rolling.RollingPolicy
    public String getActiveFileName() {
        return getParentsRawFileProperty();
    }

    public int getMaxIndex() {
        return this.maxIndex;
    }

    public int getMinIndex() {
        return this.minIndex;
    }

    public void setMaxIndex(int i) {
        this.maxIndex = i;
    }

    public void setMinIndex(int i) {
        this.minIndex = i;
    }
}
