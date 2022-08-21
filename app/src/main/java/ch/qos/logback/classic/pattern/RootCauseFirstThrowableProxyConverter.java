package ch.qos.logback.classic.pattern;

import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.CoreConstants;

/* loaded from: classes.dex */
public class RootCauseFirstThrowableProxyConverter extends ExtendedThrowableProxyConverter {
    @Override // ch.qos.logback.classic.pattern.ThrowableProxyConverter
    public String throwableProxyToString(IThrowableProxy iThrowableProxy) {
        StringBuilder sb = new StringBuilder(2048);
        recursiveAppendRootCauseFirst(sb, null, 1, iThrowableProxy);
        return sb.toString();
    }

    public void recursiveAppendRootCauseFirst(StringBuilder sb, String str, int i, IThrowableProxy iThrowableProxy) {
        if (iThrowableProxy.getCause() != null) {
            recursiveAppendRootCauseFirst(sb, str, i, iThrowableProxy.getCause());
            str = null;
        }
        ThrowableProxyUtil.indent(sb, i - 1);
        if (str != null) {
            sb.append(str);
        }
        ThrowableProxyUtil.subjoinFirstLineRootCauseFirst(sb, iThrowableProxy);
        sb.append(CoreConstants.LINE_SEPARATOR);
        subjoinSTEPArray(sb, i, iThrowableProxy);
        IThrowableProxy[] suppressed = iThrowableProxy.getSuppressed();
        if (suppressed != null) {
            for (IThrowableProxy iThrowableProxy2 : suppressed) {
                recursiveAppendRootCauseFirst(sb, CoreConstants.SUPPRESSED, i + 1, iThrowableProxy2);
            }
        }
    }
}
