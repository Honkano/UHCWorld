/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.util.yaml;

import com.sk89q.util.StringUtil;
import com.sk89q.util.yaml.YAMLFormat;
import com.sk89q.util.yaml.YAMLNode;
import com.sk89q.util.yaml.YAMLProcessorException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.BaseConstructor;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.emitter.ScalarAnalysis;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.reader.UnicodeReader;
import org.yaml.snakeyaml.representer.Represent;
import org.yaml.snakeyaml.representer.Representer;

public class YAMLProcessor
extends YAMLNode {
    public static final String LINE_BREAK = DumperOptions.LineBreak.getPlatformLineBreak().getString();
    public static final char COMMENT_CHAR = '#';
    protected final Yaml yaml;
    protected final File file;
    protected String header = null;
    protected YAMLFormat format;
    private final Map<String, String> comments = new HashMap<String, String>();

    public YAMLProcessor(File file, boolean writeDefaults, YAMLFormat format) {
        super(new LinkedHashMap<String, Object>(), writeDefaults);
        this.format = format;
        FancyDumperOptions options = new FancyDumperOptions();
        options.setIndent(4);
        options.setDefaultFlowStyle(format.getStyle());
        FancyRepresenter representer = new FancyRepresenter();
        representer.setDefaultFlowStyle(format.getStyle());
        this.yaml = new Yaml((BaseConstructor)new SafeConstructor(), (Representer)representer, (DumperOptions)options);
        this.file = file;
    }

    public YAMLProcessor(File file, boolean writeDefaults) {
        this(file, writeDefaults, YAMLFormat.COMPACT);
    }

    public void load() throws IOException {
        InputStream stream = null;
        try {
            stream = this.getInputStream();
            if (stream == null) {
                throw new IOException("Stream is null!");
            }
            this.read(this.yaml.load((Reader)new UnicodeReader(stream)));
        }
        catch (YAMLProcessorException e) {
            this.root = new LinkedHashMap();
        }
        finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            }
            catch (IOException iOException) {}
        }
    }

    public void setHeader(String ... headerLines) {
        StringBuilder header = new StringBuilder();
        for (String line : headerLines) {
            if (header.length() > 0) {
                header.append(LINE_BREAK);
            }
            header.append(line);
        }
        this.setHeader(header.toString());
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getHeader() {
        return this.header;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean save() {
        block21: {
            boolean bl;
            OutputStream stream = null;
            File parent = this.file.getParentFile();
            if (parent != null) {
                parent.mkdirs();
            }
            try {
                stream = this.getOutputStream();
                if (stream == null) {
                    boolean bl2 = false;
                    return bl2;
                }
                OutputStreamWriter writer = new OutputStreamWriter(stream, "UTF-8");
                if (this.header != null) {
                    writer.append(this.header);
                    writer.append(LINE_BREAK);
                }
                if (this.comments.isEmpty() || this.format != YAMLFormat.EXTENDED) {
                    this.yaml.dump((Object)this.root, (Writer)writer);
                } else {
                    for (Map.Entry entry : this.root.entrySet()) {
                        String comment = this.comments.get(entry.getKey());
                        if (comment != null) {
                            writer.append(LINE_BREAK);
                            writer.append(comment);
                            writer.append(LINE_BREAK);
                        }
                        this.yaml.dump(Collections.singletonMap(entry.getKey(), entry.getValue()), (Writer)writer);
                    }
                }
                bl = true;
            }
            catch (IOException iOException) {
                break block21;
            }
            finally {
                try {
                    if (stream != null) {
                        stream.close();
                    }
                }
                catch (IOException iOException) {}
            }
            return bl;
        }
        return false;
    }

    private void read(Object input) throws YAMLProcessorException {
        try {
            this.root = null == input ? new LinkedHashMap() : new LinkedHashMap((Map)input);
        }
        catch (ClassCastException e) {
            throw new YAMLProcessorException("Root document must be an key-value structure");
        }
    }

    public InputStream getInputStream() throws IOException {
        return new FileInputStream(this.file);
    }

    public OutputStream getOutputStream() throws IOException {
        return new FileOutputStream(this.file);
    }

    public String getComment(String key) {
        return this.comments.get(key);
    }

    public void setComment(String key, String comment) {
        if (comment != null) {
            this.setComment(key, comment.split("\\r?\\n"));
        } else {
            this.comments.remove(key);
        }
    }

    public void setComment(String key, String ... comment) {
        if (comment != null && comment.length > 0) {
            for (int i = 0; i < comment.length; ++i) {
                if (comment[i].matches("^# ?")) continue;
                comment[i] = "# " + comment[i];
            }
            String s = StringUtil.joinString(comment, LINE_BREAK);
            this.comments.put(key, s);
        } else {
            this.comments.remove(key);
        }
    }

    public Map<String, String> getComments() {
        return Collections.unmodifiableMap(this.comments);
    }

    public void setComments(Map<String, String> comments) {
        this.comments.clear();
        if (comments != null) {
            this.comments.putAll(comments);
        }
    }

    public static YAMLNode getEmptyNode(boolean writeDefaults) {
        return new YAMLNode(new LinkedHashMap<String, Object>(), writeDefaults);
    }

    private static class FancyRepresenter
    extends Representer {
        private FancyRepresenter() {
            this.nullRepresenter = new Represent(){

                public Node representData(Object o) {
                    return FancyRepresenter.this.representScalar(Tag.NULL, "");
                }
            };
        }
    }

    private class FancyDumperOptions
    extends DumperOptions {
        private FancyDumperOptions() {
        }

        public DumperOptions.ScalarStyle calculateScalarStyle(ScalarAnalysis analysis, DumperOptions.ScalarStyle style) {
            if (YAMLProcessor.this.format == YAMLFormat.EXTENDED && (analysis.scalar.contains("\n") || analysis.scalar.contains("\r"))) {
                return DumperOptions.ScalarStyle.LITERAL;
            }
            return super.calculateScalarStyle(analysis, style);
        }
    }
}

