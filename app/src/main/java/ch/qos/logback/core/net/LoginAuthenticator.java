package ch.qos.logback.core.net;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/* loaded from: classes.dex */
public class LoginAuthenticator extends Authenticator {
    public String password;
    public String username;

    public LoginAuthenticator(String str, String str2) {
        this.username = str;
        this.password = str2;
    }

    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(this.username, this.password);
    }
}
