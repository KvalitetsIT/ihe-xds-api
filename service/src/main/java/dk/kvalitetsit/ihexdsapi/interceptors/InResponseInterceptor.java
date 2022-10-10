package dk.kvalitetsit.ihexdsapi.interceptors;

import dk.kvalitetsit.ihexdsapi.dao.CacheRequestResponseHandle;
import dk.kvalitetsit.ihexdsapi.dao.entity.LogEntry;
import dk.kvalitetsit.ihexdsapi.service.IDContextService;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingMessage;

import java.util.UUID;

public class InResponseInterceptor extends LoggingInInterceptor {

   private CacheRequestResponseHandle cacheRequestAndResponseService;
   private IDContextService IDContextService;

    public InResponseInterceptor(CacheRequestResponseHandle cacheRequestAndResponseService, IDContextService IDContextService) {
        super(-1);
        this.cacheRequestAndResponseService = cacheRequestAndResponseService;
        this.IDContextService = IDContextService;

    }

    @Override
    protected String formatLoggingMessage(LoggingMessage loggingMessage) {
        String id = "Res:" + UUID.randomUUID();
        IDContextService.updateId("tempRes", id );
        cacheRequestAndResponseService.saveRequestAndResponse(id, new LogEntry(id , loggingMessage.toString()));
        return "";
    }


}
