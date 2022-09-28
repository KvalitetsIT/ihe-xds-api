package dk.kvalitetsit.ihexdsapi.interceptors;

import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingMessage;
import org.apache.cxf.interceptor.LoggingOutInterceptor;

public class OutResponseInterceptor extends LoggingOutInterceptor {



    private CacheRequestResponseHandle cacheRequestAndResponseService;

    public OutResponseInterceptor(CacheRequestResponseHandle cacheRequestAndResponseService) {
        super(-1);
        this.cacheRequestAndResponseService = cacheRequestAndResponseService;

    }

    @Override
    protected String formatLoggingMessage(LoggingMessage loggingMessage) {
        cacheRequestAndResponseService.saveRequestAndResponse("Request", loggingMessage.toString());
        return "";
    }



}
