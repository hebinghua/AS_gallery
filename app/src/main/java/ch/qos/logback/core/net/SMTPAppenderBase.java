package ch.qos.logback.core.net;

import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.boolex.EvaluationException;
import ch.qos.logback.core.boolex.EventEvaluator;
import ch.qos.logback.core.helpers.CyclicBuffer;
import ch.qos.logback.core.pattern.PatternLayoutBase;
import ch.qos.logback.core.sift.DefaultDiscriminator;
import ch.qos.logback.core.sift.Discriminator;
import ch.qos.logback.core.spi.CyclicBufferTracker;
import ch.qos.logback.core.util.ContentTypeUtil;
import ch.qos.logback.core.util.OptionHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.keyczar.Keyczar;

/* loaded from: classes.dex */
public abstract class SMTPAppenderBase<E> extends AppenderBase<E> {
    public static InternetAddress[] EMPTY_IA_ARRAY = new InternetAddress[0];
    public static final long MAX_DELAY_BETWEEN_STATUS_MESSAGES = 1228800000;
    public CyclicBufferTracker<E> cbTracker;
    public EventEvaluator<E> eventEvaluator;
    private String from;
    public Layout<E> layout;
    public String localhost;
    public String password;
    public Session session;
    private String smtpHost;
    public Layout<E> subjectLayout;
    public String username;
    public long lastTrackerStatusPrint = 0;
    public long delayBetweenStatusMessages = 300000;
    private List<PatternLayoutBase<E>> toPatternLayoutList = new ArrayList();
    private String subjectStr = null;
    private int smtpPort = 25;
    private boolean starttls = false;
    private boolean ssl = false;
    public boolean asynchronousSending = true;
    private String charsetEncoding = Keyczar.DEFAULT_ENCODING;
    public Discriminator<E> discriminator = new DefaultDiscriminator();
    private int errorCount = 0;

    public abstract boolean eventMarksEndOfLife(E e);

    public abstract void fillBuffer(CyclicBuffer<E> cyclicBuffer, StringBuffer stringBuffer);

    public abstract PatternLayoutBase<E> makeNewToPatternLayout(String str);

    public abstract Layout<E> makeSubjectLayout(String str);

    public abstract void subAppend(CyclicBuffer<E> cyclicBuffer, E e);

    public void updateMimeMsg(MimeMessage mimeMessage, CyclicBuffer<E> cyclicBuffer, E e) {
    }

    @Override // ch.qos.logback.core.AppenderBase, ch.qos.logback.core.spi.LifeCycle
    public void start() {
        if (this.cbTracker == null) {
            this.cbTracker = new CyclicBufferTracker<>();
        }
        Session buildSessionFromProperties = buildSessionFromProperties();
        this.session = buildSessionFromProperties;
        if (buildSessionFromProperties == null) {
            addError("Failed to obtain javax.mail.Session. Cannot start.");
            return;
        }
        this.subjectLayout = makeSubjectLayout(this.subjectStr);
        this.started = true;
    }

    private Session buildSessionFromProperties() {
        Properties properties = new Properties(OptionHelper.getSystemProperties());
        String str = this.smtpHost;
        if (str != null) {
            properties.put("mail.smtp.host", str);
        }
        properties.put("mail.smtp.port", Integer.toString(this.smtpPort));
        String str2 = this.localhost;
        if (str2 != null) {
            properties.put("mail.smtp.localhost", str2);
        }
        LoginAuthenticator loginAuthenticator = null;
        if (this.username != null) {
            loginAuthenticator = new LoginAuthenticator(this.username, this.password);
            properties.put("mail.smtp.auth", "true");
        }
        if (isSTARTTLS() && isSSL()) {
            addError("Both SSL and StartTLS cannot be enabled simultaneously");
        } else {
            if (isSTARTTLS()) {
                properties.put("mail.smtp.starttls.enable", "true");
                properties.put("mail.transport.protocol", "true");
            }
            if (isSSL()) {
                properties.put("mail.smtp.ssl.enable", "true");
            }
        }
        return Session.getInstance(properties, loginAuthenticator);
    }

    @Override // ch.qos.logback.core.AppenderBase
    public void append(E e) {
        if (!checkEntryConditions()) {
            return;
        }
        String discriminatingValue = this.discriminator.getDiscriminatingValue(e);
        long currentTimeMillis = System.currentTimeMillis();
        CyclicBuffer<E> orCreate = this.cbTracker.getOrCreate(discriminatingValue, currentTimeMillis);
        subAppend(orCreate, e);
        try {
            if (this.eventEvaluator.evaluate(e)) {
                CyclicBuffer<E> cyclicBuffer = new CyclicBuffer<>(orCreate);
                orCreate.clear();
                if (this.asynchronousSending) {
                    this.context.getScheduledExecutorService().execute(new SenderRunnable(cyclicBuffer, e));
                } else {
                    sendBuffer(cyclicBuffer, e);
                }
            }
        } catch (EvaluationException e2) {
            int i = this.errorCount + 1;
            this.errorCount = i;
            if (i < 4) {
                addError("SMTPAppender's EventEvaluator threw an Exception-", e2);
            }
        }
        if (eventMarksEndOfLife(e)) {
            this.cbTracker.endOfLife(discriminatingValue);
        }
        this.cbTracker.removeStaleComponents(currentTimeMillis);
        if (this.lastTrackerStatusPrint + this.delayBetweenStatusMessages >= currentTimeMillis) {
            return;
        }
        addInfo("SMTPAppender [" + this.name + "] is tracking [" + this.cbTracker.getComponentCount() + "] buffers");
        this.lastTrackerStatusPrint = currentTimeMillis;
        long j = this.delayBetweenStatusMessages;
        if (j >= MAX_DELAY_BETWEEN_STATUS_MESSAGES) {
            return;
        }
        this.delayBetweenStatusMessages = j * 4;
    }

    public boolean checkEntryConditions() {
        if (!this.started) {
            addError("Attempting to append to a non-started appender: " + getName());
            return false;
        } else if (this.eventEvaluator == null) {
            addError("No EventEvaluator is set for appender [" + this.name + "].");
            return false;
        } else if (this.layout != null) {
            return true;
        } else {
            addError("No layout set for appender named [" + this.name + "]. For more information, please visit http://logback.qos.ch/codes.html#smtp_no_layout");
            return false;
        }
    }

    @Override // ch.qos.logback.core.AppenderBase, ch.qos.logback.core.spi.LifeCycle
    public synchronized void stop() {
        this.started = false;
    }

    public InternetAddress getAddress(String str) {
        try {
            return new InternetAddress(str);
        } catch (AddressException e) {
            addError("Could not parse address [" + str + "].", e);
            return null;
        }
    }

    private List<InternetAddress> parseAddress(E e) {
        int size = this.toPatternLayoutList.size();
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < size; i++) {
            try {
                String doLayout = this.toPatternLayoutList.get(i).doLayout(e);
                if (doLayout != null && doLayout.length() != 0) {
                    arrayList.addAll(Arrays.asList(InternetAddress.parse(doLayout, true)));
                }
            } catch (AddressException e2) {
                addError("Could not parse email address for [" + this.toPatternLayoutList.get(i) + "] for event [" + e + "]", e2);
            }
        }
        return arrayList;
    }

    public List<PatternLayoutBase<E>> getToList() {
        return this.toPatternLayoutList;
    }

    public void sendBuffer(CyclicBuffer<E> cyclicBuffer, E e) {
        try {
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            StringBuffer stringBuffer = new StringBuffer();
            String fileHeader = this.layout.getFileHeader();
            if (fileHeader != null) {
                stringBuffer.append(fileHeader);
            }
            String presentationHeader = this.layout.getPresentationHeader();
            if (presentationHeader != null) {
                stringBuffer.append(presentationHeader);
            }
            fillBuffer(cyclicBuffer, stringBuffer);
            String presentationFooter = this.layout.getPresentationFooter();
            if (presentationFooter != null) {
                stringBuffer.append(presentationFooter);
            }
            String fileFooter = this.layout.getFileFooter();
            if (fileFooter != null) {
                stringBuffer.append(fileFooter);
            }
            String str = "Undefined subject";
            Layout<E> layout = this.subjectLayout;
            if (layout != null) {
                str = layout.doLayout(e);
                int indexOf = str != null ? str.indexOf(10) : -1;
                if (indexOf > -1) {
                    str = str.substring(0, indexOf);
                }
            }
            MimeMessage mimeMessage = new MimeMessage(this.session);
            String str2 = this.from;
            if (str2 != null) {
                mimeMessage.setFrom(getAddress(str2));
            } else {
                mimeMessage.setFrom();
            }
            mimeMessage.setSubject(str, this.charsetEncoding);
            List<InternetAddress> parseAddress = parseAddress(e);
            if (parseAddress.isEmpty()) {
                addInfo("Empty destination address. Aborting email transmission");
                return;
            }
            InternetAddress[] internetAddressArr = (InternetAddress[]) parseAddress.toArray(EMPTY_IA_ARRAY);
            mimeMessage.setRecipients(Message.RecipientType.TO, internetAddressArr);
            String contentType = this.layout.getContentType();
            if (ContentTypeUtil.isTextual(contentType)) {
                mimeBodyPart.setText(stringBuffer.toString(), this.charsetEncoding, ContentTypeUtil.getSubType(contentType));
            } else {
                mimeBodyPart.setContent(stringBuffer.toString(), this.layout.getContentType());
            }
            MimeMultipart mimeMultipart = new MimeMultipart();
            mimeMultipart.addBodyPart(mimeBodyPart);
            mimeMessage.setContent(mimeMultipart);
            updateMimeMsg(mimeMessage, cyclicBuffer, e);
            mimeMessage.setSentDate(new Date());
            addInfo("About to send out SMTP message \"" + str + "\" to " + Arrays.toString(internetAddressArr));
            Transport.send(mimeMessage);
        } catch (Exception e2) {
            addError("Error occurred while sending e-mail notification.", e2);
        }
    }

    public String getFrom() {
        return this.from;
    }

    public String getSubject() {
        return this.subjectStr;
    }

    public void setFrom(String str) {
        this.from = str;
    }

    public void setSubject(String str) {
        this.subjectStr = str;
    }

    public void setSMTPHost(String str) {
        setSmtpHost(str);
    }

    public void setSmtpHost(String str) {
        this.smtpHost = str;
    }

    public String getSMTPHost() {
        return getSmtpHost();
    }

    public String getSmtpHost() {
        return this.smtpHost;
    }

    public void setSMTPPort(int i) {
        setSmtpPort(i);
    }

    public void setSmtpPort(int i) {
        this.smtpPort = i;
    }

    public int getSMTPPort() {
        return getSmtpPort();
    }

    public int getSmtpPort() {
        return this.smtpPort;
    }

    public String getLocalhost() {
        return this.localhost;
    }

    public void setLocalhost(String str) {
        this.localhost = str;
    }

    public CyclicBufferTracker<E> getCyclicBufferTracker() {
        return this.cbTracker;
    }

    public void setCyclicBufferTracker(CyclicBufferTracker<E> cyclicBufferTracker) {
        this.cbTracker = cyclicBufferTracker;
    }

    public Discriminator<E> getDiscriminator() {
        return this.discriminator;
    }

    public void setDiscriminator(Discriminator<E> discriminator) {
        this.discriminator = discriminator;
    }

    public boolean isAsynchronousSending() {
        return this.asynchronousSending;
    }

    public void setAsynchronousSending(boolean z) {
        this.asynchronousSending = z;
    }

    public void addTo(String str) {
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("Null or empty <to> property");
        }
        PatternLayoutBase<E> makeNewToPatternLayout = makeNewToPatternLayout(str.trim());
        makeNewToPatternLayout.setContext(this.context);
        makeNewToPatternLayout.start();
        this.toPatternLayoutList.add(makeNewToPatternLayout);
    }

    public List<String> getToAsListOfString() {
        ArrayList arrayList = new ArrayList();
        for (PatternLayoutBase<E> patternLayoutBase : this.toPatternLayoutList) {
            arrayList.add(patternLayoutBase.getPattern());
        }
        return arrayList;
    }

    public boolean isSTARTTLS() {
        return this.starttls;
    }

    public void setSTARTTLS(boolean z) {
        this.starttls = z;
    }

    public boolean isSSL() {
        return this.ssl;
    }

    public void setSSL(boolean z) {
        this.ssl = z;
    }

    public void setEvaluator(EventEvaluator<E> eventEvaluator) {
        this.eventEvaluator = eventEvaluator;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String str) {
        this.username = str;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String str) {
        this.password = str;
    }

    public String getCharsetEncoding() {
        return this.charsetEncoding;
    }

    public void setCharsetEncoding(String str) {
        this.charsetEncoding = str;
    }

    public Layout<E> getLayout() {
        return this.layout;
    }

    public void setLayout(Layout<E> layout) {
        this.layout = layout;
    }

    /* loaded from: classes.dex */
    public class SenderRunnable implements Runnable {
        public final CyclicBuffer<E> cyclicBuffer;
        public final E e;

        public SenderRunnable(CyclicBuffer<E> cyclicBuffer, E e) {
            this.cyclicBuffer = cyclicBuffer;
            this.e = e;
        }

        @Override // java.lang.Runnable
        public void run() {
            SMTPAppenderBase.this.sendBuffer(this.cyclicBuffer, this.e);
        }
    }
}
