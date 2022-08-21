package ch.qos.logback.core.rolling.helper;

import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.util.FileSize;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

/* loaded from: classes.dex */
public class TimeBasedArchiveRemover extends ContextAwareBase implements ArchiveRemover {
    private final DateParser dateParser;
    public final FileNamePattern fileNamePattern;
    private final FileProvider fileProvider;
    private final FileSorter fileSorter;
    private final RollingCalendar rc;
    private int maxHistory = 0;
    private long totalSizeCap = 0;

    public String toString() {
        return "c.q.l.core.rolling.helper.TimeBasedArchiveRemover";
    }

    public TimeBasedArchiveRemover(FileNamePattern fileNamePattern, RollingCalendar rollingCalendar, FileProvider fileProvider) {
        this.fileNamePattern = fileNamePattern;
        this.rc = rollingCalendar;
        this.fileProvider = fileProvider;
        DateParser dateParser = new DateParser(fileNamePattern);
        this.dateParser = dateParser;
        this.fileSorter = new FileSorter(dateParser, new IntParser(fileNamePattern));
    }

    @Override // ch.qos.logback.core.rolling.helper.ArchiveRemover
    public void clean(Date date) {
        List<String> findFiles = findFiles();
        for (String str : filterFiles(findFiles, createExpiredFileFilter(date))) {
            delete(new File(str));
        }
        long j = this.totalSizeCap;
        if (j != 0 && j > 0) {
            capTotalSize(findFiles);
        }
        for (String str2 : findEmptyDirs()) {
            delete(new File(str2));
        }
    }

    private boolean delete(File file) {
        addInfo("deleting " + file);
        boolean deleteFile = this.fileProvider.deleteFile(file);
        if (!deleteFile) {
            addWarn("cannot delete " + file);
        }
        return deleteFile;
    }

    private void capTotalSize(List<String> list) {
        String[] strArr = (String[]) list.toArray(new String[0]);
        this.fileSorter.sort(strArr);
        long j = 0;
        long j2 = 0;
        for (String str : strArr) {
            File file = new File(str);
            long length = this.fileProvider.length(file);
            if (j2 + length > this.totalSizeCap) {
                addInfo("Deleting [" + file + "] of size " + new FileSize(length));
                if (!delete(file)) {
                    length = 0;
                }
                j += length;
            }
            j2 += length;
        }
        addInfo("Removed  " + new FileSize(j) + " of files");
    }

    @Override // ch.qos.logback.core.rolling.helper.ArchiveRemover
    public void setMaxHistory(int i) {
        this.maxHistory = i;
    }

    @Override // ch.qos.logback.core.rolling.helper.ArchiveRemover
    public void setTotalSizeCap(long j) {
        this.totalSizeCap = j;
    }

    @Override // ch.qos.logback.core.rolling.helper.ArchiveRemover
    public Future<?> cleanAsynchronously(Date date) {
        return this.context.getScheduledExecutorService().submit(new ArchiveRemoverRunnable(date));
    }

    private FilenameFilter createExpiredFileFilter(final Date date) {
        return new FilenameFilter() { // from class: ch.qos.logback.core.rolling.helper.TimeBasedArchiveRemover.1
            @Override // java.io.FilenameFilter
            public boolean accept(File file, String str) {
                return TimeBasedArchiveRemover.this.rc.normalizeDate(TimeBasedArchiveRemover.this.dateParser.parseFilename(str)).compareTo(TimeBasedArchiveRemover.this.rc.normalizeDate(TimeBasedArchiveRemover.this.rc.getEndOfNextNthPeriod(date, -TimeBasedArchiveRemover.this.maxHistory))) < 0;
            }
        };
    }

    private List<String> filterFiles(List<String> list, FilenameFilter filenameFilter) {
        String[] strArr;
        ArrayList arrayList = new ArrayList();
        for (String str : (String[]) list.toArray(new String[0])) {
            if (filenameFilter.accept(null, str)) {
                arrayList.add(str);
                list.remove(str);
            }
        }
        return arrayList;
    }

    private List<String> findFiles() {
        return new FileFinder(this.fileProvider).findFiles(this.fileNamePattern.toRegex());
    }

    private List<String> findEmptyDirs() {
        List<String> findDirs = new FileFinder(this.fileProvider).findDirs(this.fileNamePattern.toRegex());
        Collections.reverse(findDirs);
        ArrayDeque arrayDeque = new ArrayDeque();
        for (String str : findDirs) {
            int length = this.fileProvider.list(new File(str), null).length;
            if (length == 0 || (length == 1 && arrayDeque.size() > 0 && str.equals(arrayDeque.peekLast()))) {
                arrayDeque.add(str);
            }
        }
        return Arrays.asList((String[]) arrayDeque.toArray(new String[0]));
    }

    /* loaded from: classes.dex */
    public class ArchiveRemoverRunnable implements Runnable {
        public Date now;

        public ArchiveRemoverRunnable(Date date) {
            this.now = date;
        }

        @Override // java.lang.Runnable
        public void run() {
            TimeBasedArchiveRemover.this.clean(this.now);
        }
    }
}
