package dk.kvalitetsit.ihexdsapi.service.decorators;

import org.apache.cxf.binding.soap.Soap11;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.binding.soap.interceptor.SoapPreProtocolOutInterceptor;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.StaxOutInterceptor;
import org.apache.cxf.phase.Phase;

import javax.mail.Message;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CxfContentIDDecorator extends AbstractSoapInterceptor {

    public CxfContentIDDecorator() {
        super(Phase.PREPARE_SEND);
        //addBefore(SoapPreProtocolOutInterceptor.class.getName());

    }

    @Override
    public void handleMessage(SoapMessage message) throws Fault {

        System.out.println(message.getContent(String.class));


        for (int i = 0; i < message.getExchange().getOutMessage().size(); i++) {
            System.out.println(message.getExchange().getOutMessage().get(i));        }
     /*   System.out.println(message.getExchange().getOutMessage().getContent(String.class));
        System.out.println(message.getAttachments().toString());
        System.out.println(message.getVersion().getVersion());
        System.out.println(message.values());
        System.out.println(message.getHeaders());
        for (int i = 0; i < message.getHeaders().size(); i++) {
            System.out.println(message.getHeaders().get(i).getName());
        }
        InputStream in = message.getContent(InputStream.class);
        try {
            byte payload[] = IOUtils.readBytesFromStream(in);
            System.out.println(payload);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        XMLStreamReader body = message.getContent(XMLStreamReader.class);
        System.out.println(body);

*/
        System.out.println(".......................... HER SLUTTER TESTEN .........");

    }
}
