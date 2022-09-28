package dk.kvalitetsit.ihexdsapi.interceptors;

import dk.kvalitetsit.ihexdsapi.interceptors.impl.CacheRequestResponseHandleImpl;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingMessage;
import org.springframework.beans.factory.annotation.Autowired;

public class InResponseInterceptor extends LoggingInInterceptor {

   private CacheRequestResponseHandle cacheRequestAndResponseService;

    public InResponseInterceptor(CacheRequestResponseHandle cacheRequestAndResponseService) {
        super(-1);
        this.cacheRequestAndResponseService = cacheRequestAndResponseService;

    }

    @Override
    protected String formatLoggingMessage(LoggingMessage loggingMessage) {
        cacheRequestAndResponseService.saveRequestAndResponse("Response", loggingMessage.toString());
        return "";
    }


}
