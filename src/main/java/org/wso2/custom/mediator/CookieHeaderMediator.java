package org.wso2.custom.mediator;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.mediators.AbstractMediator;
import org.apache.synapse.transport.nhttp.NhttpConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Map;
public class CookieHeaderMediator extends AbstractMediator {
    protected static final Log log = LogFactory.getLog(CookieHeaderMediator.class);

    public boolean mediate(MessageContext synCtx) {
        try {
            if (log.isDebugEnabled()) {
                log.debug("Executing the CookieHeaderMediator...");
            }

            // Load the Axis2 Message Context
            org.apache.axis2.context.MessageContext msgContext = ((Axis2MessageContext) synCtx).getAxis2MessageContext();

            // Extracting the excess transport headers and transport headers from the Axis2 Message Context
            Map excessTransportHeaders = (Map) msgContext.getProperty(NhttpConstants.EXCESS_TRANSPORT_HEADERS);
            Map transportHeaders = (Map) msgContext.getProperty("TRANSPORT_HEADERS");

            log.debug("### CGMSUB-28 - Transport Header : " + transportHeaders + " ###");
            log.debug("### CGMSUB-28 - Excess Transport Header : " + excessTransportHeaders + " ###");

            log.debug("### CGMSUB-28 - Transport Header - Cookie : " + transportHeaders.get("set-cookie") + " ###");

            String str = transportHeaders.get("set-cookie").toString();
            String[] arrOfStr = str.split(";", 3);
            String firstComponent = arrOfStr[0];
            String[] firstComponentArray = firstComponent.split("=", 2);
            String firstComponentName = firstComponentArray[0];
            String firstComponentValue = firstComponentArray[1];
            String pathComponent = arrOfStr[1];
            String lastComponent = arrOfStr[2];
            String[] pathArray = pathComponent.split("=", 2);
            String pathName = pathArray[0];
            String pathValue = pathArray[1];
            String newCookieValue = firstComponent + ";" + pathName + "=" + "/a" + pathValue + ";" + lastComponent;

            log.debug("### CGMSUB-28 - Cookie : " + newCookieValue + " ###");
            transportHeaders.put("set-cookie", newCookieValue);
            msgContext.setProperty("TRANSPORT_HEADERS", transportHeaders);
            log.debug("### CGMSUB-28 - Transport Header After : " + transportHeaders + " ###");



        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug("Exception: " + e);
            }
            handleException("Exception", e, synCtx);
        }
        return true;
    }
}
