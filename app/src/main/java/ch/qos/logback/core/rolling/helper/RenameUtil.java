package ch.qos.logback.core.rolling.helper;

import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.RolloverFailure;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.util.EnvUtil;
import ch.qos.logback.core.util.FileUtil;
import java.io.File;

/* loaded from: classes.dex */
public class RenameUtil extends ContextAwareBase {
    public static String RENAMING_ERROR_URL = "http://logback.qos.ch/codes.html#renamingError";

    public String toString() {
        return "c.q.l.co.rolling.helper.RenameUtil";
    }

    public void rename(String str, String str2) throws RolloverFailure {
        if (str.equals(str2)) {
            addWarn("Source and target files are the same [" + str + "]. Skipping.");
            return;
        }
        File file = new File(str);
        if (file.exists()) {
            File file2 = new File(str2);
            createMissingTargetDirsIfNecessary(file2);
            addInfo("Renaming file [" + file + "] to [" + file2 + "]");
            if (file.renameTo(file2)) {
                return;
            }
            addWarn("Failed to rename file [" + file + "] as [" + file2 + "].");
            if (Boolean.TRUE.equals(areOnDifferentVolumes(file, file2))) {
                addWarn("Detected different file systems for source [" + str + "] and target [" + str2 + "]. Attempting rename by copying.");
                renameByCopying(str, str2);
                return;
            }
            addWarn("Please consider leaving the [file] option of " + RollingFileAppender.class.getSimpleName() + " empty.");
            StringBuilder sb = new StringBuilder();
            sb.append("See also ");
            sb.append(RENAMING_ERROR_URL);
            addWarn(sb.toString());
            return;
        }
        throw new RolloverFailure("File [" + str + "] does not exist.");
    }

    public Boolean areOnDifferentVolumes(File file, File file2) throws RolloverFailure {
        if (!EnvUtil.isJDK7OrHigher()) {
            return Boolean.FALSE;
        }
        File parentFile = file2.getAbsoluteFile().getParentFile();
        if (parentFile == null) {
            addWarn("Parent of target file [" + file2 + "] is null");
            return null;
        } else if (!parentFile.exists()) {
            addWarn("Parent of target file [" + file2 + "] does not exist");
            return null;
        } else {
            try {
                return Boolean.valueOf(!FileStoreUtil.areOnSameFileStore(file, parentFile));
            } catch (RolloverFailure e) {
                addWarn("Error while checking file store equality", e);
                return null;
            }
        }
    }

    public void renameByCopying(String str, String str2) throws RolloverFailure {
        new FileUtil(getContext()).copy(str, str2);
        if (!new File(str).delete()) {
            addWarn("Could not delete " + str);
        }
    }

    public void createMissingTargetDirsIfNecessary(File file) throws RolloverFailure {
        if (FileUtil.createMissingParentDirectories(file)) {
            return;
        }
        throw new RolloverFailure("Failed to create parent directories for [" + file.getAbsolutePath() + "]");
    }
}
