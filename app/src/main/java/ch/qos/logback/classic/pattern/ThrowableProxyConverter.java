package ch.qos.logback.classic.pattern;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.boolex.EvaluationException;
import ch.qos.logback.core.boolex.EventEvaluator;
import ch.qos.logback.core.status.ErrorStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/* loaded from: classes.dex */
public class ThrowableProxyConverter extends ThrowableHandlingConverter {
    public static final int BUILDER_CAPACITY = 2048;
    public int lengthOption;
    public List<EventEvaluator<ILoggingEvent>> evaluatorList = null;
    public List<String> ignoredStackTraceLines = null;
    public int errorCount = 0;

    public void extraData(StringBuilder sb, StackTraceElementProxy stackTraceElementProxy) {
    }

    @Override // ch.qos.logback.core.pattern.DynamicConverter, ch.qos.logback.core.spi.LifeCycle
    public void start() {
        String firstOption = getFirstOption();
        if (firstOption == null) {
            this.lengthOption = Integer.MAX_VALUE;
        } else {
            String lowerCase = firstOption.toLowerCase(Locale.US);
            if ("full".equals(lowerCase)) {
                this.lengthOption = Integer.MAX_VALUE;
            } else if ("short".equals(lowerCase)) {
                this.lengthOption = 1;
            } else {
                try {
                    this.lengthOption = Integer.parseInt(lowerCase);
                } catch (NumberFormatException unused) {
                    addError("Could not parse [" + lowerCase + "] as an integer");
                    this.lengthOption = Integer.MAX_VALUE;
                }
            }
        }
        List<String> optionList = getOptionList();
        if (optionList != null && optionList.size() > 1) {
            int size = optionList.size();
            for (int i = 1; i < size; i++) {
                String str = optionList.get(i);
                EventEvaluator<ILoggingEvent> eventEvaluator = (EventEvaluator) ((Map) getContext().getObject(CoreConstants.EVALUATOR_MAP)).get(str);
                if (eventEvaluator != null) {
                    addEvaluator(eventEvaluator);
                } else {
                    addIgnoreStackTraceLine(str);
                }
            }
        }
        super.start();
    }

    private void addEvaluator(EventEvaluator<ILoggingEvent> eventEvaluator) {
        if (this.evaluatorList == null) {
            this.evaluatorList = new ArrayList();
        }
        this.evaluatorList.add(eventEvaluator);
    }

    private void addIgnoreStackTraceLine(String str) {
        if (this.ignoredStackTraceLines == null) {
            this.ignoredStackTraceLines = new ArrayList();
        }
        this.ignoredStackTraceLines.add(str);
    }

    @Override // ch.qos.logback.core.pattern.DynamicConverter, ch.qos.logback.core.spi.LifeCycle
    public void stop() {
        this.evaluatorList = null;
        super.stop();
    }

    @Override // ch.qos.logback.core.pattern.Converter
    public String convert(ILoggingEvent iLoggingEvent) {
        EventEvaluator<ILoggingEvent> eventEvaluator;
        IThrowableProxy throwableProxy = iLoggingEvent.getThrowableProxy();
        if (throwableProxy == null) {
            return "";
        }
        if (this.evaluatorList != null) {
            boolean z = false;
            int i = 0;
            while (true) {
                if (i >= this.evaluatorList.size()) {
                    z = true;
                    break;
                }
                try {
                } catch (EvaluationException e) {
                    this.errorCount++;
                    if (this.errorCount < 4) {
                        addError("Exception thrown for evaluator named [" + eventEvaluator.getName() + "]", e);
                    } else if (this.errorCount == 4) {
                        ErrorStatus errorStatus = new ErrorStatus("Exception thrown for evaluator named [" + eventEvaluator.getName() + "].", this, e);
                        errorStatus.add(new ErrorStatus("This was the last warning about this evaluator's errors.We don't want the StatusManager to get flooded.", this));
                        addStatus(errorStatus);
                    }
                }
                if (this.evaluatorList.get(i).evaluate(iLoggingEvent)) {
                    break;
                }
                i++;
            }
            if (!z) {
                return "";
            }
        }
        return throwableProxyToString(throwableProxy);
    }

    public String throwableProxyToString(IThrowableProxy iThrowableProxy) {
        StringBuilder sb = new StringBuilder(2048);
        recursiveAppend(sb, null, 1, iThrowableProxy);
        return sb.toString();
    }

    private void recursiveAppend(StringBuilder sb, String str, int i, IThrowableProxy iThrowableProxy) {
        if (iThrowableProxy == null) {
            return;
        }
        subjoinFirstLine(sb, str, i, iThrowableProxy);
        sb.append(CoreConstants.LINE_SEPARATOR);
        subjoinSTEPArray(sb, i, iThrowableProxy);
        IThrowableProxy[] suppressed = iThrowableProxy.getSuppressed();
        if (suppressed != null) {
            for (IThrowableProxy iThrowableProxy2 : suppressed) {
                recursiveAppend(sb, CoreConstants.SUPPRESSED, i + 1, iThrowableProxy2);
            }
        }
        recursiveAppend(sb, CoreConstants.CAUSED_BY, i, iThrowableProxy.getCause());
    }

    private void subjoinFirstLine(StringBuilder sb, String str, int i, IThrowableProxy iThrowableProxy) {
        ThrowableProxyUtil.indent(sb, i - 1);
        if (str != null) {
            sb.append(str);
        }
        subjoinExceptionMessage(sb, iThrowableProxy);
    }

    private void subjoinExceptionMessage(StringBuilder sb, IThrowableProxy iThrowableProxy) {
        sb.append(iThrowableProxy.getClassName());
        sb.append(": ");
        sb.append(iThrowableProxy.getMessage());
    }

    public void subjoinSTEPArray(StringBuilder sb, int i, IThrowableProxy iThrowableProxy) {
        StackTraceElementProxy[] stackTraceElementProxyArray = iThrowableProxy.getStackTraceElementProxyArray();
        int commonFrames = iThrowableProxy.getCommonFrames();
        int i2 = this.lengthOption;
        boolean z = i2 > stackTraceElementProxyArray.length;
        if (z) {
            i2 = stackTraceElementProxyArray.length;
        }
        if (commonFrames > 0 && z) {
            i2 -= commonFrames;
        }
        int i3 = 0;
        for (int i4 = 0; i4 < i2; i4++) {
            StackTraceElementProxy stackTraceElementProxy = stackTraceElementProxyArray[i4];
            if (!isIgnoredStackTraceLine(stackTraceElementProxy.toString())) {
                ThrowableProxyUtil.indent(sb, i);
                printStackLine(sb, i3, stackTraceElementProxy);
                sb.append(CoreConstants.LINE_SEPARATOR);
                i3 = 0;
            } else {
                i3++;
                if (i2 < stackTraceElementProxyArray.length) {
                    i2++;
                }
            }
        }
        if (i3 > 0) {
            printIgnoredCount(sb, i3);
            sb.append(CoreConstants.LINE_SEPARATOR);
        }
        if (commonFrames <= 0 || !z) {
            return;
        }
        ThrowableProxyUtil.indent(sb, i);
        sb.append("... ");
        sb.append(iThrowableProxy.getCommonFrames());
        sb.append(" common frames omitted");
        sb.append(CoreConstants.LINE_SEPARATOR);
    }

    private void printStackLine(StringBuilder sb, int i, StackTraceElementProxy stackTraceElementProxy) {
        sb.append(stackTraceElementProxy);
        extraData(sb, stackTraceElementProxy);
        if (i > 0) {
            printIgnoredCount(sb, i);
        }
    }

    private void printIgnoredCount(StringBuilder sb, int i) {
        sb.append(" [");
        sb.append(i);
        sb.append(" skipped]");
    }

    private boolean isIgnoredStackTraceLine(String str) {
        List<String> list = this.ignoredStackTraceLines;
        if (list != null) {
            for (String str2 : list) {
                if (str.contains(str2)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }
}
