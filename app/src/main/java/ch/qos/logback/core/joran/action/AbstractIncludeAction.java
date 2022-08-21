package ch.qos.logback.core.joran.action;

import ch.qos.logback.core.joran.spi.ActionException;
import ch.qos.logback.core.joran.spi.InterpretationContext;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.Loader;
import ch.qos.logback.core.util.OptionHelper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import org.xml.sax.Attributes;

/* loaded from: classes.dex */
public abstract class AbstractIncludeAction extends Action {
    private static final String FILE_ATTR = "file";
    private static final String OPTIONAL_ATTR = "optional";
    private static final String RESOURCE_ATTR = "resource";
    private static final String URL_ATTR = "url";
    private String attributeInUse;
    private boolean optional;

    @Override // ch.qos.logback.core.joran.action.Action
    public void end(InterpretationContext interpretationContext, String str) throws ActionException {
    }

    public abstract void processInclude(InterpretationContext interpretationContext, URL url) throws JoranException;

    public void handleError(String str, Exception exc) {
        if (exc != null && ((exc instanceof FileNotFoundException) || (exc instanceof UnknownHostException))) {
            addWarn(str, exc);
        } else {
            addError(str, exc);
        }
    }

    @Override // ch.qos.logback.core.joran.action.Action
    public void begin(InterpretationContext interpretationContext, String str, Attributes attributes) throws ActionException {
        this.attributeInUse = null;
        this.optional = OptionHelper.toBoolean(attributes.getValue(OPTIONAL_ATTR), false);
        if (!checkAttributes(attributes)) {
            return;
        }
        try {
            URL inputURL = getInputURL(interpretationContext, attributes);
            if (inputURL == null) {
                return;
            }
            processInclude(interpretationContext, inputURL);
        } catch (JoranException e) {
            optionalWarning("Error while parsing " + this.attributeInUse, e);
        }
    }

    public void close(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException unused) {
            }
        }
    }

    private boolean checkAttributes(Attributes attributes) {
        String value = attributes.getValue("file");
        String value2 = attributes.getValue("url");
        String value3 = attributes.getValue("resource");
        int i = !OptionHelper.isEmpty(value) ? 1 : 0;
        if (!OptionHelper.isEmpty(value2)) {
            i++;
        }
        if (!OptionHelper.isEmpty(value3)) {
            i++;
        }
        if (i == 0) {
            optionalWarning(String.format("One of \"%1$s\", \"%2$s\" or \"%3$s\" attributes must be set.", "file", "resource", "url"), null);
            return false;
        } else if (i > 1) {
            optionalWarning(String.format("Only one of \"%1$s\", \"%2$s\" or \"%3$s\" attributes should be set.", "file", "resource", "url"), null);
            return false;
        } else if (i == 1) {
            return true;
        } else {
            throw new IllegalStateException("Count value [" + i + "] is not expected");
        }
    }

    private URL attributeToURL(String str) {
        try {
            URL url = new URL(str);
            url.openStream().close();
            return url;
        } catch (MalformedURLException e) {
            optionalWarning("URL [" + str + "] is not well formed.", e);
            return null;
        } catch (IOException e2) {
            optionalWarning("URL [" + str + "] cannot be opened.", e2);
            return null;
        }
    }

    private URL resourceAsURL(String str) {
        URL resourceBySelfClassLoader = Loader.getResourceBySelfClassLoader(str);
        if (resourceBySelfClassLoader == null) {
            optionalWarning("Could not find resource corresponding to [" + str + "]", null);
            return null;
        }
        return resourceBySelfClassLoader;
    }

    private URL filePathAsURL(String str) {
        File file = new File(str);
        if (!file.exists() || !file.isFile()) {
            optionalWarning("File does not exist [" + str + "]", new FileNotFoundException(str));
            return null;
        }
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getAttributeInUse() {
        return this.attributeInUse;
    }

    public boolean isOptional() {
        return this.optional;
    }

    private URL getInputURL(InterpretationContext interpretationContext, Attributes attributes) {
        String value = attributes.getValue("file");
        String value2 = attributes.getValue("url");
        String value3 = attributes.getValue("resource");
        if (!OptionHelper.isEmpty(value)) {
            String subst = interpretationContext.subst(value);
            this.attributeInUse = subst;
            return filePathAsURL(subst);
        } else if (!OptionHelper.isEmpty(value2)) {
            String subst2 = interpretationContext.subst(value2);
            this.attributeInUse = subst2;
            return attributeToURL(subst2);
        } else if (!OptionHelper.isEmpty(value3)) {
            String subst3 = interpretationContext.subst(value3);
            this.attributeInUse = subst3;
            return resourceAsURL(subst3);
        } else {
            throw new IllegalStateException("A URL stream should have been returned");
        }
    }

    public void optionalWarning(String str, Exception exc) {
        if (!isOptional()) {
            handleError(str, exc);
        }
    }
}
