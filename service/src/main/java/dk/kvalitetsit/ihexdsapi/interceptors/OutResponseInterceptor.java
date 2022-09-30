package dk.kvalitetsit.ihexdsapi.interceptors;

import dk.kvalitetsit.ihexdsapi.dao.CacheRequestResponseHandle;
import dk.kvalitetsit.ihexdsapi.dao.entity.LogEntry;
import dk.kvalitetsit.ihexdsapi.service.UtilityService;
import org.apache.cxf.interceptor.LoggingMessage;
import org.apache.cxf.interceptor.LoggingOutInterceptor;

import java.util.UUID;

public class OutResponseInterceptor extends LoggingOutInterceptor {



    private CacheRequestResponseHandle cacheRequestAndResponseService;
    private UtilityService utilityService;

    public OutResponseInterceptor(CacheRequestResponseHandle cacheRequestAndResponseService, UtilityService utilityService) {
        super(-1);
        this.cacheRequestAndResponseService = cacheRequestAndResponseService;
        this.utilityService = utilityService;

    }

    @Override
    protected String formatLoggingMessage(LoggingMessage loggingMessage) {
        String id = "Req:" + UUID.randomUUID();
        utilityService.updateId("tempReq", id );
        cacheRequestAndResponseService.saveRequestAndResponse(id, new LogEntry(id, loggingMessage.toString()));
        return "";
    }



}
