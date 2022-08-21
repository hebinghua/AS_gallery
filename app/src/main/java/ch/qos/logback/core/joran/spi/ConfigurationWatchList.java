package ch.qos.logback.core.joran.spi;

import ch.qos.logback.core.joran.action.Action;
import ch.qos.logback.core.spi.ContextAwareBase;
import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class ConfigurationWatchList extends ContextAwareBase {
    public List<File> fileWatchList = new ArrayList();
    public List<Long> lastModifiedList = new ArrayList();
    public URL mainURL;

    public ConfigurationWatchList buildClone() {
        ConfigurationWatchList configurationWatchList = new ConfigurationWatchList();
        configurationWatchList.mainURL = this.mainURL;
        configurationWatchList.fileWatchList = new ArrayList(this.fileWatchList);
        configurationWatchList.lastModifiedList = new ArrayList(this.lastModifiedList);
        return configurationWatchList;
    }

    public void clear() {
        this.mainURL = null;
        this.lastModifiedList.clear();
        this.fileWatchList.clear();
    }

    public void setMainURL(URL url) {
        this.mainURL = url;
        if (url != null) {
            addAsFileToWatch(url);
        }
    }

    private void addAsFileToWatch(URL url) {
        File convertToFile = convertToFile(url);
        if (convertToFile != null) {
            this.fileWatchList.add(convertToFile);
            this.lastModifiedList.add(Long.valueOf(convertToFile.lastModified()));
        }
    }

    public void addToWatchList(URL url) {
        addAsFileToWatch(url);
    }

    public URL getMainURL() {
        return this.mainURL;
    }

    public List<File> getCopyOfFileWatchList() {
        return new ArrayList(this.fileWatchList);
    }

    public boolean changeDetected() {
        int size = this.fileWatchList.size();
        for (int i = 0; i < size; i++) {
            if (this.lastModifiedList.get(i).longValue() != this.fileWatchList.get(i).lastModified()) {
                return true;
            }
        }
        return false;
    }

    public File convertToFile(URL url) {
        if (Action.FILE_ATTRIBUTE.equals(url.getProtocol())) {
            return new File(URLDecoder.decode(url.getFile()));
        }
        addInfo("URL [" + url + "] is not of type file");
        return null;
    }
}
