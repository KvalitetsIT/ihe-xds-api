package dk.kvalitetsit.ihexdsapi.upload_interceptor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.activation.DataHandler;

import org.apache.cxf.attachment.AttachmentSerializer;
import org.apache.cxf.attachment.AttachmentUtil;
import org.apache.cxf.common.util.Base64Utility;
import org.apache.cxf.message.Attachment;
import org.apache.cxf.message.Message;
public class KITAttachmentSerializer  {

    private static final String DEFAULT_MULTIPART_TYPE = "multipart/related";
    private String contentTransferEncoding = "binary";
    private Message message;
    private String bodyBoundary;
    private OutputStream out;
    private String encoding;
    private String multipartType;
    private Map<String, List<String>> rootHeaders = Collections.emptyMap();
    private boolean xop = true;
    private boolean writeOptionalTypeParameters = true;

    public KITAttachmentSerializer(Message messageParam) {

        this.message = messageParam;

    }

    public KITAttachmentSerializer(Message messageParam, AttachmentSerializer attachmentSerializer) {
        this.message = messageParam;

    }

    public KITAttachmentSerializer(Message messageParam, String multipartType, boolean writeOptionalTypeParameters, Map<String, List<String>> headers) {
        this.message = messageParam;
        this.multipartType = multipartType;
        this.writeOptionalTypeParameters = writeOptionalTypeParameters;
        this.rootHeaders = headers;
    }

    public void writeProlog() throws IOException {
        this.bodyBoundary = AttachmentUtil.getUniqueBoundaryValue();
        String bodyCt = (String)this.message.get("Content-Type");
        String bodyCtParams = null;
        String bodyCtParamsEscaped = null;
        if (bodyCt.indexOf(59) != -1) {
            int pos = bodyCt.indexOf(59);
            bodyCtParams = bodyCt.substring(pos);
            bodyCtParamsEscaped = escapeQuotes(bodyCtParams);
            bodyCt = bodyCt.substring(0, pos);
        }

        String requestMimeType = this.multipartType == null ? "multipart/related" : this.multipartType;
        StringBuilder ct = new StringBuilder(32);
        ct.append(requestMimeType);
        boolean xopOrMultipartRelated = this.xop || "multipart/related".equalsIgnoreCase(requestMimeType) || "multipart/related".startsWith(requestMimeType);
        if (xopOrMultipartRelated && requestMimeType.indexOf("type=") == -1) {
            if (this.xop) {
                ct.append("; type=\"application/xop+xml\"");
            } else {
                ct.append("; type=\"").append(bodyCt).append('"');
            }
        }

        ct.append("; boundary=\"").append(this.bodyBoundary).append('"');
        String rootContentId = this.getHeaderValue("Content-ID", "root.message@cxf.apache.org");
        if (xopOrMultipartRelated) {
            ct.append("; start=\"<").append(checkAngleBrackets(rootContentId)).append(">\"");
        }

        if (this.writeOptionalTypeParameters || this.xop) {
            ct.append("; start-info=\"").append(bodyCt);
            if (bodyCtParamsEscaped != null) {
                ct.append(bodyCtParamsEscaped);
            }

            ct.append('"');
        }

        this.message.put("Content-Type", ct.toString());
        this.out = (OutputStream)this.message.getContent(OutputStream.class);
        this.encoding = (String)this.message.get(Message.ENCODING);
        if (this.encoding == null) {
            this.encoding = StandardCharsets.UTF_8.name();
        }

        StringWriter writer = new StringWriter();
        writer.write("\r\n");
        writer.write("--");
        writer.write(this.bodyBoundary);
        StringBuilder mimeBodyCt = new StringBuilder();
        String bodyType = this.getHeaderValue("Content-Type", (String)null);
        if (bodyType == null) {
            mimeBodyCt.append(this.xop ? "application/xop+xml" : bodyCt).append("; charset=").append(this.encoding);
            if (this.xop) {
                mimeBodyCt.append("; type=\"").append(bodyCt);
                if (bodyCtParamsEscaped != null) {
                    mimeBodyCt.append(bodyCtParamsEscaped);
                }

                mimeBodyCt.append('"');
            } else if (bodyCtParams != null) {
                mimeBodyCt.append(bodyCtParams);
            }
        } else {
            mimeBodyCt.append(bodyType);
        }

        this.writeHeaders(mimeBodyCt.toString(), rootContentId, this.rootHeaders, writer);
        this.out.write(writer.getBuffer().toString().getBytes(this.encoding));
    }

    private static String escapeQuotes(String s) {
        return s.indexOf(34) != 0 ? s.replace("\"", "\\\"") : s;
    }

    public void setContentTransferEncoding(String cte) {
        this.contentTransferEncoding = cte;
    }

    private String getHeaderValue(String name, String defaultValue) {
        List<String> value = (List)this.rootHeaders.get(name);
        if (value != null && !value.isEmpty()) {
            StringBuilder sb = new StringBuilder();

            for(int i = 0; i < value.size(); ++i) {
                sb.append((String)value.get(i));
                if (i + 1 < value.size()) {
                    sb.append(',');
                }
            }

            return sb.toString();
        } else {
            return defaultValue;
        }
    }

    private void writeHeaders(String contentType, String attachmentId, Map<String, List<String>> headers, Writer writer) throws IOException {
        writer.write("\r\nContent-Type: ");
        writer.write(contentType);
        writer.write("\r\nContent-Transfer-Encoding: " + this.contentTransferEncoding + "\r\n");
        if (attachmentId != null) {
            attachmentId = checkAngleBrackets(attachmentId);
            writer.write("Content-ID: <");
            if (attachmentId.startsWith("cid:")) {
                writer.write(URLDecoder.decode(attachmentId.substring(4), StandardCharsets.UTF_8.name()));
            } else {
                String[] address = attachmentId.split("@", 2);
                if (address.length == 2) {

                    attachmentId = URLDecoder.decode(attachmentId, StandardCharsets.UTF_8.name());

                    writer.write(attachmentId);
                } else {
                    writer.write(URLEncoder.encode(attachmentId, StandardCharsets.UTF_8.name()));
                }
            }

            writer.write(">\r\n");
        }

        Iterator var10 = headers.entrySet().iterator();

        while(true) {
            Map.Entry entry;
            String name;
            do {
                do {
                    do {
                        if (!var10.hasNext()) {
                            writer.write("\r\n");
                            return;
                        }

                        entry = (Map.Entry)var10.next();
                        name = (String)entry.getKey();
                    } while("Content-Type".equalsIgnoreCase(name));
                } while("Content-ID".equalsIgnoreCase(name));
            } while("Content-Transfer-Encoding".equalsIgnoreCase(name));

            writer.write(name);
            writer.write(": ");
            List<String> values = (List)entry.getValue();

            for(int i = 0; i < values.size(); ++i) {
                writer.write((String)values.get(i));
                if (i + 1 < values.size()) {
                    writer.write(",");
                }
            }

            writer.write("\r\n");
        }
    }

    private static String checkAngleBrackets(String value) {
        return value.charAt(0) == '<' && value.charAt(value.length() - 1) == '>' ? value.substring(1, value.length() - 1) : value;
    }

    public void writeAttachments() throws IOException {
        if (this.message.getAttachments() != null) {
            Iterator var1 = this.message.getAttachments().iterator();

            while(var1.hasNext()) {
                Attachment a = (Attachment)var1.next();
                StringWriter writer = new StringWriter();
                writer.write("\r\n--");
                writer.write(this.bodyBoundary);
                Iterator<String> it = a.getHeaderNames();
                Object headers;
                if (it.hasNext()) {
                    headers = new LinkedHashMap();

                    while(it.hasNext()) {
                        String key = (String)it.next();
                        ((Map)headers).put(key, Collections.singletonList(a.getHeader(key)));
                    }
                } else {
                    headers = Collections.emptyMap();
                }

                DataHandler handler = a.getDataHandler();
                handler.setCommandMap(AttachmentUtil.getCommandMap());
                this.writeHeaders(handler.getContentType(), a.getId(), (Map)headers, writer);
                this.out.write(writer.getBuffer().toString().getBytes(this.encoding));
                if ("base64".equals(this.contentTransferEncoding)) {
                    InputStream inputStream = handler.getInputStream();
                    Throwable var8 = null;

                    try {
                        this.encodeBase64(inputStream, this.out, 4096);
                    } catch (Throwable var17) {
                        var8 = var17;
                        throw var17;
                    } finally {
                        if (inputStream != null) {
                            if (var8 != null) {
                                try {
                                    inputStream.close();
                                } catch (Throwable var16) {
                                    var8.addSuppressed(var16);
                                }
                            } else {
                                inputStream.close();
                            }
                        }

                    }
                } else {
                    handler.writeTo(this.out);
                }
            }
        }

        StringWriter writer = new StringWriter();
        writer.write("\r\n--");
        writer.write(this.bodyBoundary);
        writer.write("--");
        this.out.write(writer.getBuffer().toString().getBytes(this.encoding));
        this.out.flush();
    }

    private int encodeBase64(InputStream input, OutputStream output, int bufferSize) throws IOException {
        int avail = input.available();
        if (avail > 262143) {
            avail = 262143;
        }

        if (avail > bufferSize) {
            bufferSize = avail;
        }

        byte[] buffer = new byte[bufferSize];
        int n = input.read(buffer);
        int total = 0;

        while(true) {
            while(-1 != n) {
                if (n == 0) {
                    throw new IOException("0 bytes read in violation of InputStream.read(byte[])");
                }

                int left = n % 3;
                n -= left;
                if (n > 0) {
                    Base64Utility.encodeAndStream(buffer, 0, n, output);
                    total += n;
                }

                if (left != 0) {
                    for(int x = 0; x < left; ++x) {
                        buffer[x] = buffer[n + x];
                    }

                    n = input.read(buffer, left, buffer.length - left);
                    if (n == -1) {
                        Base64Utility.encodeAndStream(buffer, 0, left, output);
                        total += left;
                    }
                } else {
                    n = input.read(buffer);
                }
            }

            return total;
        }
    }

    public boolean isXop() {
        return this.xop;
    }

    public void setXop(boolean xop) {
        this.xop = xop;
    }
}


