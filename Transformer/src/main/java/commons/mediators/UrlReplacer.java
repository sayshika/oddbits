package commons.mediators; /**
 * Created by seshika on 8/18/14.
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.Mediator;
import org.apache.synapse.MessageContext;
import org.apache.synapse.commons.json.JsonUtil;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.wso2.carbon.utils.CarbonUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class UrlReplacer implements Mediator {

    private static final Log log = LogFactory.getLog(UrlReplacer.class);

    public UrlReplacer(){}

    @Override
    public boolean mediate(MessageContext messageContext) {

        org.apache.axis2.context.MessageContext axis2mc = ((Axis2MessageContext) messageContext).getAxis2MessageContext();
        Map<Object, String> transportHeaders = (Map) axis2mc.getProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);

        String contentType = transportHeaders.get("Content-Type");
        String jsonStr = JsonUtil.jsonPayloadToString(axis2mc);
        if(log.isDebugEnabled()) {
            log.debug("jsonStr: "+jsonStr);
        }

        BufferedReader in=null;
        try {
            String filePath = CarbonUtils.getCarbonHome() + File.separator + "repository" +
                    File.separator + "conf" + File.separator + "etc" + File.separator + "pairs.txt";
            if(log.isDebugEnabled()) {
                log.debug("filePath: "+filePath);
            }

            in = new BufferedReader(new FileReader(filePath));
            String str;
            // iterate through all replacement pairs
            while ((str = in.readLine()) != null) {

                String[] ar = str.split(",");
                if(log.isDebugEnabled()) {
                    log.debug("Replace String: "+ar[0]+","+ar[1]);
                }
                // replace all occurences of ar[0] in JSON payload
                jsonStr = jsonStr.replaceAll(ar[0],ar[1]);

                // replace all occurences of ar[0] in HTTP Headers
                for(Map.Entry<Object, String> entry : transportHeaders.entrySet()) {
                    String s;
                    if((s = entry.getValue())!=null) {
                        s = s.replaceAll(ar[0], ar[1]);
                        entry.setValue(s);
                    }
                }
            }
        } catch (IOException e) {
            String msg = "Cannot Read File";
            log.error(msg, e);
        } catch (Exception e) {
            log.error(e);
        }
        finally {
            if(in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    log.error(e);
                }
            }
        }
        if(log.isDebugEnabled()) {
            log.debug("converted jsonStr: "+jsonStr);
        }
        // Set replaced JSON payload
        JsonUtil.newJsonPayload(axis2mc, jsonStr, true, true);
        // Set replaced HTTP Headers
        axis2mc.setProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS, transportHeaders);
        for(Map.Entry<Object, String> entry : transportHeaders.entrySet()) {
            System.out.println("Header: "+entry.getValue());
        }
        return true;
    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public int getTraceState() {
        return 0;
    }

    @Override
    public void setTraceState(int i) {

    }

    @Override
    public boolean isContentAware() {
        return false;
    }

    @Override
    public int getMediatorPosition() {
        return 0;
    }

    @Override
    public void setMediatorPosition(int i) {

    }

    @Override
    public void setShortDescription(String s) {

    }

    @Override
    public String getShortDescription() {
        return null;
    }

    @Override
    public void setDescription(String s) {

    }

    @Override
    public String getDescription() {
        return null;
    }
}
