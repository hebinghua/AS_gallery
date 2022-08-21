package ch.qos.logback.classic.net;

import ch.qos.logback.core.net.ssl.ConfigurableSSLSocketFactory;
import ch.qos.logback.core.net.ssl.SSLComponent;
import ch.qos.logback.core.net.ssl.SSLConfiguration;
import ch.qos.logback.core.net.ssl.SSLParametersConfiguration;
import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;

/* loaded from: classes.dex */
public class SSLSocketReceiver extends SocketReceiver implements SSLComponent {
    private SocketFactory socketFactory;
    private SSLConfiguration ssl;

    @Override // ch.qos.logback.classic.net.SocketReceiver
    public SocketFactory getSocketFactory() {
        return this.socketFactory;
    }

    @Override // ch.qos.logback.classic.net.SocketReceiver, ch.qos.logback.classic.net.ReceiverBase
    public boolean shouldStart() {
        try {
            SSLContext createContext = getSsl().createContext(this);
            SSLParametersConfiguration parameters = getSsl().getParameters();
            parameters.setContext(getContext());
            this.socketFactory = new ConfigurableSSLSocketFactory(parameters, createContext.getSocketFactory());
            return super.shouldStart();
        } catch (Exception e) {
            addError(e.getMessage(), e);
            return false;
        }
    }

    @Override // ch.qos.logback.core.net.ssl.SSLComponent
    public SSLConfiguration getSsl() {
        if (this.ssl == null) {
            this.ssl = new SSLConfiguration();
        }
        return this.ssl;
    }

    @Override // ch.qos.logback.core.net.ssl.SSLComponent
    public void setSsl(SSLConfiguration sSLConfiguration) {
        this.ssl = sSLConfiguration;
    }
}
