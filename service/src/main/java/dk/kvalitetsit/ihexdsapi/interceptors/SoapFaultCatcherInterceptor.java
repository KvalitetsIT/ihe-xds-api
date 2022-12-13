package dk.kvalitetsit.ihexdsapi.interceptors;

import dk.kvalitetsit.ihexdsapi.dgws.ItiException;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class SoapFaultCatcherInterceptor  extends AbstractPhaseInterceptor<Message> {

    public SoapFaultCatcherInterceptor() {
        super(Phase.RECEIVE);
    }

    @Override
    public void handleMessage(Message message) throws Fault {

        InputStream i = message.getContent(InputStream.class);

        StringBuilder textBuilder = new StringBuilder();
        // Converts bytes into string but clears the InputStream for bytes.
        try  {
            Reader reader = new BufferedReader(new InputStreamReader
                    (i, Charset.forName(StandardCharsets.UTF_8.name())));
            int c = 0;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        message.setContent(InputStream.class, new ByteArrayInputStream(textBuilder.toString().getBytes()));
        if (textBuilder.toString().contains("<faultstring>internal_error_minlog</faultstring>")) {
            throw new RuntimeException("ID does not exists");
        }
    }

}
