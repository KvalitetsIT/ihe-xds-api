package dk.kvalitetsit.ihexdsapi.upload_interceptor;

import org.apache.cxf.attachment.AttachmentUtil;
import org.apache.cxf.common.i18n.BundleUtils;
import org.apache.cxf.interceptor.*;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageUtils;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

import java.io.*;
import java.util.*;

public class KITAttachmentOutInterceptor extends AbstractPhaseInterceptor<Message> {
    public static final String WRITE_ATTACHMENTS = "write.attachments";
    public static final String ATTACHMENT_OUT_CHECKED = "attachment.out.checked";
    public static final String WRITE_OPTIONAL_TYPE_PARAMETERS = "write.optional.type.parameters";
    private static final ResourceBundle BUNDLE = BundleUtils.getBundle(AttachmentOutInterceptor.class);
    private KITAttachmentOutInterceptor.KITAttachmentOutEndingInterceptor ending = new KITAttachmentOutInterceptor.KITAttachmentOutEndingInterceptor();
    private boolean writeOptionalTypeParameters = true;


    public KITAttachmentOutInterceptor() {
        super(Phase.PRE_STREAM);
        addBefore(AttachmentOutInterceptor.class.getName());
    }

    @Override
    public void handleMessage(Message message) throws Fault {

        this.removeAttachmentOutInterceptor(message);
        this.runAttachmentOutInterceptor(message);


    }


    private void removeAttachmentOutInterceptor(Message message) {
        Iterator<Interceptor<? extends Message>> iterator =
                message.getInterceptorChain().iterator();
        Interceptor<?> removeInterceptor = null;
        for (; iterator.hasNext(); ) {
            Interceptor<?> interceptor = iterator.next();
            if (interceptor.getClass().getName().equals(AttachmentOutInterceptor.class.getName())) {
                removeInterceptor = interceptor;
                break;
            }
        }
        if (removeInterceptor != null) {
            message.getInterceptorChain().remove(removeInterceptor);
        }

    }

    protected String getMultipartType() {
        return "multipart/related";
    }

    protected boolean writeOptionalTypeParameters() {
        return this.writeOptionalTypeParameters;
    }

    protected Map<String, List<String>> getRootHeaders() {
        return Collections.emptyMap();
    }


    private void runAttachmentOutInterceptor(Message message) {

        if (message.get("attachment.out.checked") == null || !(Boolean) message.get("attachment.out.checked")) {
            message.put("attachment.out.checked", Boolean.TRUE);
            boolean mtomEnabled = AttachmentUtil.isMtomEnabled(message);
            boolean writeAtts = MessageUtils.getContextualBoolean(message, "write.attachments", false) || message.getAttachments() != null && !message.getAttachments().isEmpty();
            this.writeOptionalTypeParameters = MessageUtils.getContextualBoolean(message, "write.optional.type.parameters", true);
            if (mtomEnabled || writeAtts) {
                if (message.getContent(OutputStream.class) != null) {
                    KITAttachmentSerializer serializer = new KITAttachmentSerializer(message, this.getMultipartType(), this.writeOptionalTypeParameters(), this.getRootHeaders());
                    serializer.setXop(mtomEnabled);
                    String contentTransferEncoding = (String) message.getContextualProperty(Message.CONTENT_TRANSFER_ENCODING);
                    if (contentTransferEncoding != null) {
                        serializer.setContentTransferEncoding(contentTransferEncoding);
                    }

                    try {
                        serializer.writeProlog();
                    } catch (IOException var7) {
                        throw new Fault(new org.apache.cxf.common.i18n.Message("WRITE_ATTACHMENTS", BUNDLE, new Object[0]), var7);
                    }

                    message.setContent(KITAttachmentSerializer.class, serializer);
                    message.getInterceptorChain().add(this.ending);
                }
            }
        }
    }

    public class KITAttachmentOutEndingInterceptor extends AbstractPhaseInterceptor<Message> {
        public KITAttachmentOutEndingInterceptor() {
            super("pre-stream-ending");
        }

        public void handleMessage(Message message) {
            KITAttachmentSerializer ser = (KITAttachmentSerializer) message.getContent(KITAttachmentSerializer.class);
            if (ser != null) {
                try {
                    String cte = (String) message.getContextualProperty(Message.CONTENT_TRANSFER_ENCODING);
                    if (cte != null) {
                        ser.setContentTransferEncoding(cte);
                    }

                    ser.writeAttachments();
                } catch (IOException var4) {
                    throw new Fault(new org.apache.cxf.common.i18n.Message("WRITE_ATTACHMENTS", BUNDLE, new Object[0]), var4);
                }
            }

        }
    }


}