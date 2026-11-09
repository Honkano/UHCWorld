/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.util.formatting;

import com.google.common.base.Joiner;
import com.sk89q.worldedit.util.formatting.Fragment;
import com.sk89q.worldedit.util.formatting.Style;
import com.sk89q.worldedit.util.formatting.StyleSet;
import com.sk89q.worldedit.util.formatting.StyledFragment;
import java.util.LinkedList;

public class ColorCodeBuilder {
    private static final ColorCodeBuilder instance = new ColorCodeBuilder();
    private static final Joiner newLineJoiner = Joiner.on((String)"\n");
    public static final int GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH = 47;

    public String[] build(StyledFragment message) {
        StringBuilder builder = new StringBuilder();
        this.buildFragment(builder, message, message.getStyle(), new StyleSet());
        return builder.toString().split("\r?\n");
    }

    private StyleSet buildFragment(StringBuilder builder, StyledFragment message, StyleSet parentStyle, StyleSet lastStyle) {
        for (Fragment node : message.getChildren()) {
            if (node instanceof StyledFragment) {
                StyledFragment fragment = (StyledFragment)node;
                lastStyle = this.buildFragment(builder, fragment, parentStyle.extend(message.getStyle()), lastStyle);
                continue;
            }
            StyleSet style = parentStyle.extend(message.getStyle());
            builder.append(ColorCodeBuilder.getAdditive(style, lastStyle));
            builder.append(node);
            lastStyle = style;
        }
        return lastStyle;
    }

    public static String getFormattingCode(StyleSet style) {
        StringBuilder builder = new StringBuilder();
        if (style.isBold()) {
            builder.append((Object)Style.BOLD);
        }
        if (style.isItalic()) {
            builder.append((Object)Style.ITALIC);
        }
        if (style.isUnderline()) {
            builder.append((Object)Style.UNDERLINE);
        }
        if (style.isStrikethrough()) {
            builder.append((Object)Style.STRIKETHROUGH);
        }
        return builder.toString();
    }

    public static String getCode(StyleSet style) {
        StringBuilder builder = new StringBuilder();
        builder.append(ColorCodeBuilder.getFormattingCode(style));
        if (style.getColor() != null) {
            builder.append((Object)style.getColor());
        }
        return builder.toString();
    }

    public static String getAdditive(StyleSet resetTo, StyleSet resetFrom) {
        if (!resetFrom.hasFormatting() && resetTo.hasFormatting()) {
            StringBuilder builder = new StringBuilder();
            builder.append(ColorCodeBuilder.getFormattingCode(resetTo));
            if (resetFrom.getColor() != resetTo.getColor()) {
                builder.append((Object)resetTo.getColor());
            }
            return builder.toString();
        }
        if (!resetFrom.hasEqualFormatting(resetTo) || resetFrom.getColor() != null && resetTo.getColor() == null) {
            StringBuilder builder = new StringBuilder();
            builder.append((Object)Style.RESET);
            builder.append(ColorCodeBuilder.getCode(resetTo));
            return builder.toString();
        }
        if (resetFrom.getColor() != resetTo.getColor()) {
            return String.valueOf((Object)resetTo.getColor());
        }
        return "";
    }

    private String[] wordWrap(String rawString, int lineLength) {
        int i;
        if (rawString == null) {
            return new String[]{""};
        }
        if (rawString.length() <= lineLength && !rawString.contains("\n")) {
            return new String[]{rawString};
        }
        char[] rawChars = (rawString + ' ').toCharArray();
        StringBuilder word = new StringBuilder();
        StringBuilder line = new StringBuilder();
        LinkedList<String> lines = new LinkedList<String>();
        int lineColorChars = 0;
        for (i = 0; i < rawChars.length; ++i) {
            char c = rawChars[i];
            if (c == '\u00a7') {
                word.append((Object)Style.getByChar(rawChars[i + 1]));
                lineColorChars += 2;
                ++i;
                continue;
            }
            if (c == ' ' || c == '\n') {
                String transformed;
                String wordStr;
                if (line.length() == 0 && word.length() > lineLength) {
                    wordStr = word.toString();
                    transformed = this.transform(wordStr);
                    if (transformed != null) {
                        line.append(transformed);
                    } else {
                        for (String partialWord : word.toString().split("(?<=\\G.{" + lineLength + "})")) {
                            lines.add(partialWord);
                        }
                    }
                } else if (line.length() + word.length() - lineColorChars == lineLength) {
                    line.append(' ');
                    line.append((CharSequence)word);
                    lines.add(line.toString());
                    line = new StringBuilder();
                    lineColorChars = 0;
                } else if (line.length() + 1 + word.length() - lineColorChars > lineLength) {
                    wordStr = word.toString();
                    if (word.length() > lineLength && (transformed = this.transform(wordStr)) != null) {
                        if (line.length() + 1 + transformed.length() - lineColorChars > lineLength) {
                            lines.add(line.toString());
                            line = new StringBuilder(transformed);
                            lineColorChars = 0;
                        } else {
                            if (line.length() > 0) {
                                line.append(' ');
                            }
                            line.append(transformed);
                        }
                    } else {
                        for (String partialWord : wordStr.split("(?<=\\G.{" + lineLength + "})")) {
                            lines.add(line.toString());
                            line = new StringBuilder(partialWord);
                        }
                        lineColorChars = 0;
                    }
                } else {
                    if (line.length() > 0) {
                        line.append(' ');
                    }
                    line.append((CharSequence)word);
                }
                word = new StringBuilder();
                if (c != '\n') continue;
                lines.add(line.toString());
                line = new StringBuilder();
                continue;
            }
            word.append(c);
        }
        if (line.length() > 0) {
            lines.add(line.toString());
        }
        if (((String)lines.get(0)).isEmpty() || ((String)lines.get(0)).charAt(0) != '\u00a7') {
            lines.set(0, (Object)((Object)Style.WHITE) + (String)lines.get(0));
        }
        for (i = 1; i < lines.size(); ++i) {
            String pLine = (String)lines.get(i - 1);
            String subLine = (String)lines.get(i);
            char color = pLine.charAt(pLine.lastIndexOf(167) + 1);
            if (!subLine.isEmpty() && subLine.charAt(0) == '\u00a7') continue;
            lines.set(i, (Object)((Object)Style.getByChar(color)) + subLine);
        }
        return lines.toArray(new String[lines.size()]);
    }

    protected String transform(String word) {
        return null;
    }

    public static String asColorCodes(StyledFragment fragment) {
        return newLineJoiner.join((Object[])instance.build(fragment));
    }
}

