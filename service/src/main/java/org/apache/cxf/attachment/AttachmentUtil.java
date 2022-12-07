//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.apache.cxf.attachment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import javax.activation.CommandInfo;
import javax.activation.CommandMap;
import javax.activation.DataContentHandler;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.activation.URLDataSource;
import org.apache.cxf.common.logging.LogUtils;
import org.apache.cxf.common.util.StringUtils;
import org.apache.cxf.helpers.FileUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.message.Attachment;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageUtils;

public final class AttachmentUtil {
   public static final String BODY_ATTACHMENT_ID = "root.message@cxf.apache.org";
    static final String BINARY = "binary";
    private static final Logger LOG = LogUtils.getL7dLogger(AttachmentUtil.class);
    private static final AtomicInteger COUNTER = new AtomicInteger();
    private static volatile int counter;
    private static final String ATT_UUID = UUID.randomUUID().toString();
    private static final Random BOUND_RANDOM = new Random();
    private static final CommandMap DEFAULT_COMMAND_MAP = CommandMap.getDefaultCommandMap();
    private static final MailcapCommandMap COMMAND_MAP = new EnhancedMailcapCommandMap();

    private AttachmentUtil() {
    }

    public static CommandMap getCommandMap() {
        return COMMAND_MAP;
    }

    public static boolean isMtomEnabled(Message message) {
        return MessageUtils.getContextualBoolean(message, "mtom-enabled", false);
    }

    public static void setStreamedAttachmentProperties(Message message, CachedOutputStream bos) throws IOException {
        Object directory = message.getContextualProperty("attachment-directory");
        if (directory != null) {
            if (directory instanceof File) {
                bos.setOutputDir((File)directory);
            } else {
                if (!(directory instanceof String)) {
                    throw new IOException("The value set as attachment-directory should be either an instance of File or String");
                }

                bos.setOutputDir(new File((String)directory));
            }
        }

        Object threshold = message.getContextualProperty("attachment-memory-threshold");
        if (threshold != null) {
            if (threshold instanceof Number) {
                long t = ((Number)threshold).longValue();
                if (t >= 0L) {
                    bos.setThreshold(t);
                } else {
                    LOG.warning("Threshold value overflowed long. Setting default value!");
                    bos.setThreshold(102400L);
                }
            } else {
                if (!(threshold instanceof String)) {
                    throw new IOException("The value set as attachment-memory-threshold should be either an instance of Number or String");
                }

                try {
                    bos.setThreshold(Long.parseLong((String)threshold));
                } catch (NumberFormatException var8) {
                    throw new IOException("Provided threshold String is not a number", var8);
                }
            }
        } else if (!CachedOutputStream.isThresholdSysPropSet()) {
            bos.setThreshold(102400L);
        }

        Object maxSize = message.getContextualProperty("attachment-max-size");
        if (maxSize != null) {
            if (maxSize instanceof Number) {
                long size = ((Number)maxSize).longValue();
                if (size >= 0L) {
                    bos.setMaxSize(size);
                } else {
                    LOG.warning("Max size value overflowed long. Do not set max size!");
                }
            } else {
                if (!(maxSize instanceof String)) {
                    throw new IOException("The value set as attachment-max-size should be either an instance of Number or String");
                }

                try {
                    bos.setMaxSize(Long.parseLong((String)maxSize));
                } catch (NumberFormatException var7) {
                    throw new IOException("Provided threshold String is not a number", var7);
                }
            }
        }

    }

    public static String createContentID(String ns) throws UnsupportedEncodingException {
        // tend to change
        String cid = "cxf.apache.org";

        String name = ATT_UUID + "-" + String.valueOf(++counter);
        if (ns != null && (ns.length() > 0)) {
            cid = ns;
        }
        return URLEncoder.encode(name, "UTF-8") + "@" + URLEncoder.encode(cid, "UTF-8");
    }

    public static String getUniqueBoundaryValue() {
        long leastSigBits;
        long mostSigBits;
        synchronized(BOUND_RANDOM) {
            mostSigBits = BOUND_RANDOM.nextLong();
            leastSigBits = BOUND_RANDOM.nextLong();
        }

        mostSigBits &= -61441L;
        mostSigBits |= 16384L;
        leastSigBits &= 4611686018427387903L;
        leastSigBits |= Long.MIN_VALUE;
        UUID result = new UUID(mostSigBits, leastSigBits);
        return "uuid:" + result.toString();
    }

    public static Map<String, DataHandler> getDHMap(Collection<Attachment> attachments) {
        Map<String, DataHandler> dataHandlers = null;
        if (attachments != null) {
            if (attachments instanceof LazyAttachmentCollection) {
                dataHandlers = ((LazyAttachmentCollection)attachments).createDataHandlerMap();
            } else {
                dataHandlers = new DHMap(attachments);
            }
        }

        return (Map)(dataHandlers == null ? new LinkedHashMap() : dataHandlers);
    }

    public static String cleanContentId(String id) {
        if (id != null) {
            if (id.startsWith("<")) {
                id = id.substring(1, id.length() - 1);
            }

            if (id.startsWith("cid:")) {
                try {
                    id = id.substring(4);
                    id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
                } catch (UnsupportedEncodingException var2) {
                }
            }
        }

        if (id == null) {
            id = "root.message@cxf.apache.org";
        }

        return id;
    }

    static String getHeaderValue(List<String> v) {
        return v != null && !v.isEmpty() ? (String)v.get(0) : null;
    }

    static String getHeaderValue(List<String> v, String delim) {
        return v != null && !v.isEmpty() ? String.join(delim, v) : null;
    }

    static String getHeader(Map<String, List<String>> headers, String h) {
        return getHeaderValue((List)headers.get(h));
    }

    static String getHeader(Map<String, List<String>> headers, String h, String delim) {
        return getHeaderValue((List)headers.get(h), delim);
    }

    public static Attachment createAttachment(InputStream stream, Map<String, List<String>> headers) throws IOException {
        String id = cleanContentId(getHeader(headers, "Content-ID"));
        AttachmentImpl att = new AttachmentImpl(id);
        String ct = getHeader(headers, "Content-Type");
        String cd = getHeader(headers, "Content-Disposition");
        String fileName = getContentDispositionFileName(cd);
        String encoding = null;

        Map.Entry e;
        String name;
        for(Iterator var8 = headers.entrySet().iterator(); var8.hasNext(); att.setHeader(name, getHeaderValue((List)e.getValue()))) {
            e = (Map.Entry)var8.next();
            name = (String)e.getKey();
            if ("Content-Transfer-Encoding".equalsIgnoreCase(name)) {
                encoding = getHeader(headers, name);
                if ("binary".equalsIgnoreCase(encoding)) {
                    att.setXOP(true);
                }
            }
        }

        if (encoding == null) {
            encoding = "binary";
        }

        InputStream ins = decode(stream, encoding);
        if (ins != stream) {
            headers.remove("Content-Transfer-Encoding");
        }

        DataSource source = new AttachmentDataSource(ct, ins);
        if (!StringUtils.isEmpty(fileName)) {
            ((AttachmentDataSource)source).setName(FileUtils.stripPath(fileName));
        }

        att.setDataHandler(new DataHandler(source));
        return att;
    }

    static String getContentDispositionFileName(String cd) {
        if (StringUtils.isEmpty(cd)) {
            return null;
        } else {
            ContentDisposition c = new ContentDisposition(cd);
            String s = c.getParameter("filename");
            if (s == null) {
                s = c.getParameter("name");
            }

            return s;
        }
    }

    public static InputStream decode(InputStream in, String encoding) throws IOException {
        if (encoding == null) {
            return in;
        } else {
            encoding = encoding.toLowerCase();
            if (!"binary".equals(encoding) && !"7bit".equals(encoding) && !"8bit".equals(encoding)) {
                if ("base64".equals(encoding)) {
                    return new Base64DecoderStream(in);
                } else if ("quoted-printable".equals(encoding)) {
                    return new QuotedPrintableDecoderStream(in);
                } else {
                    throw new IOException("Unknown encoding " + encoding);
                }
            } else {
                return in;
            }
        }
    }

    public static boolean isTypeSupported(String contentType, List<String> types) {
        if (contentType == null) {
            return false;
        } else {
            contentType = contentType.toLowerCase();
            Iterator var2 = types.iterator();

            String s;
            do {
                if (!var2.hasNext()) {
                    return false;
                }

                s = (String)var2.next();
            } while(contentType.indexOf(s) == -1);

            return true;
        }
    }

    public static Attachment createMtomAttachment(boolean isXop, String mimeType, String elementNS, byte[] data, int offset, int length, int threshold) {
        if (isXop && length > threshold) {
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }

            ByteDataSource source = new ByteDataSource(data, offset, length);
            source.setContentType(mimeType);
            DataHandler handler = new DataHandler(source);

            String id;
            try {
                id = createContentID(elementNS);
            } catch (UnsupportedEncodingException var11) {
                throw new Fault(var11);
            }

            AttachmentImpl att = new AttachmentImpl(id, handler);
            att.setXOP(isXop);
            return att;
        } else {
            return null;
        }
    }

    public static Attachment createMtomAttachmentFromDH(boolean isXop, DataHandler handler, String elementNS, int threshold) {
        if (!isXop) {
            return null;
        } else {
            try {
                DataSource ds = handler.getDataSource();
                if (ds instanceof FileDataSource) {
                    FileDataSource fds = (FileDataSource)ds;
                    File file = fds.getFile();
                    if (file.length() < (long)threshold) {
                        return null;
                    }
                } else if (ds.getClass().getName().endsWith("ObjectDataSource")) {
                    Object o = handler.getContent();
                    if (o instanceof String && ((String)o).length() < threshold) {
                        return null;
                    }

                    if (o instanceof byte[] && ((byte[])((byte[])o)).length < threshold) {
                        return null;
                    }
                }
            } catch (IOException var9) {
            }

            String id;
            try {
                id = createContentID(elementNS);
            } catch (UnsupportedEncodingException var8) {
                throw new Fault(var8);
            }

            AttachmentImpl att = new AttachmentImpl(id, handler);
            if (!StringUtils.isEmpty(handler.getName())) {
                String file = handler.getName();
                File f = new File(file);
                if (f.exists() && f.isFile()) {
                    file = f.getName();
                }

                att.setHeader("Content-Disposition", "attachment;name=\"" + file + "\"");
            }

            att.setXOP(isXop);
            return att;
        }
    }

    public static DataSource getAttachmentDataSource(String contentId, Collection<Attachment> atts) {
        if (contentId.startsWith("cid:")) {
            try {
                contentId = URLDecoder.decode(contentId.substring(4), StandardCharsets.UTF_8.name());
            } catch (UnsupportedEncodingException var3) {
                contentId = contentId.substring(4);
            }

            return loadDataSource(contentId, atts);
        } else if (contentId.indexOf("://") == -1) {
            return loadDataSource(contentId, atts);
        } else {
            try {
                return new URLDataSource(new URL(contentId));
            } catch (MalformedURLException var4) {
                throw new Fault(var4);
            }
        }
    }

    private static DataSource loadDataSource(String contentId, Collection<Attachment> atts) {
        return new LazyDataSource(contentId, atts);
    }

    static {
        COMMAND_MAP.addMailcap("image/*;;x-java-content-handler=" + ImageDataContentHandler.class.getName());
    }

    static class DHMap extends AbstractMap<String, DataHandler> {
        final Collection<Attachment> list;

        DHMap(Collection<Attachment> l) {
            this.list = l;
        }

        public Set<Map.Entry<String, DataHandler>> entrySet() {
            return new AbstractSet<Map.Entry<String, DataHandler>>() {
                public Iterator<Map.Entry<String, DataHandler>> iterator() {
                    final Iterator<Attachment> it = DHMap.this.list.iterator();
                    return new Iterator<Map.Entry<String, DataHandler>>() {
                        public boolean hasNext() {
                            return it.hasNext();
                        }

                        public Map.Entry<String, DataHandler> next() {
                            final Attachment a = (Attachment)it.next();
                            return new Map.Entry<String, DataHandler>() {
                                public String getKey() {
                                    return a.getId();
                                }

                                public DataHandler getValue() {
                                    return a.getDataHandler();
                                }

                                public DataHandler setValue(DataHandler value) {
                                    return null;
                                }
                            };
                        }

                        public void remove() {
                            it.remove();
                        }
                    };
                }

                public int size() {
                    return DHMap.this.list.size();
                }
            };
        }

        public DataHandler put(String key, DataHandler value) {
            Iterator<Attachment> i = this.list.iterator();
            DataHandler ret = null;

            while(i.hasNext()) {
                Attachment a = (Attachment)i.next();
                if (a.getId().equals(key)) {
                    i.remove();
                    ret = a.getDataHandler();
                    break;
                }
            }

            this.list.add(new AttachmentImpl(key, value));
            return ret;
        }
    }

    static final class EnhancedMailcapCommandMap extends MailcapCommandMap {
        EnhancedMailcapCommandMap() {
        }

        public synchronized DataContentHandler createDataContentHandler(String mimeType) {
            DataContentHandler dch = super.createDataContentHandler(mimeType);
            if (dch == null) {
                dch = AttachmentUtil.DEFAULT_COMMAND_MAP.createDataContentHandler(mimeType);
            }

            return dch;
        }

        public DataContentHandler createDataContentHandler(String mimeType, DataSource ds) {
            DataContentHandler dch = super.createDataContentHandler(mimeType);
            if (dch == null) {
                dch = AttachmentUtil.DEFAULT_COMMAND_MAP.createDataContentHandler(mimeType, ds);
            }

            return dch;
        }

        public synchronized CommandInfo[] getAllCommands(String mimeType) {
            CommandInfo[] commands = super.getAllCommands(mimeType);
            CommandInfo[] defaultCommands = AttachmentUtil.DEFAULT_COMMAND_MAP.getAllCommands(mimeType);
            List<CommandInfo> cmdList = new ArrayList(Arrays.asList(commands));
            CommandInfo[] allCommandArray = defaultCommands;
            int var6 = defaultCommands.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                CommandInfo defCmdInfo = allCommandArray[var7];
                String defCmdName = defCmdInfo.getCommandName();
                boolean cmdNameExist = false;
                CommandInfo[] var11 = commands;
                int var12 = commands.length;

                for(int var13 = 0; var13 < var12; ++var13) {
                    CommandInfo cmdInfo = var11[var13];
                    if (cmdInfo.getCommandName().equals(defCmdName)) {
                        cmdNameExist = true;
                        break;
                    }
                }

                if (!cmdNameExist) {
                    cmdList.add(defCmdInfo);
                }
            }

            allCommandArray = new CommandInfo[0];
            return (CommandInfo[])cmdList.toArray(allCommandArray);
        }

        public synchronized CommandInfo getCommand(String mimeType, String cmdName) {
            CommandInfo cmdInfo = super.getCommand(mimeType, cmdName);
            if (cmdInfo == null) {
                cmdInfo = AttachmentUtil.DEFAULT_COMMAND_MAP.getCommand(mimeType, cmdName);
            }

            return cmdInfo;
        }

        public synchronized String[] getMimeTypes() {
            String[] mimeTypes = super.getMimeTypes();
            String[] defMimeTypes = AttachmentUtil.DEFAULT_COMMAND_MAP.getMimeTypes();
            Set<String> mimeTypeSet = new HashSet();
            Collections.addAll(mimeTypeSet, mimeTypes);
            Collections.addAll(mimeTypeSet, defMimeTypes);
            String[] mimeArray = new String[0];
            return (String[])mimeTypeSet.toArray(mimeArray);
        }
    }
}
