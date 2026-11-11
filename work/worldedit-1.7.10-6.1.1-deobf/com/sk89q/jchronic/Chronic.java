/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.jchronic;

import com.sk89q.jchronic.Options;
import com.sk89q.jchronic.handlers.Handler;
import com.sk89q.jchronic.numerizer.Numerizer;
import com.sk89q.jchronic.repeaters.Repeater;
import com.sk89q.jchronic.tags.Grabber;
import com.sk89q.jchronic.tags.Ordinal;
import com.sk89q.jchronic.tags.Pointer;
import com.sk89q.jchronic.tags.Scalar;
import com.sk89q.jchronic.tags.Separator;
import com.sk89q.jchronic.tags.TimeZone;
import com.sk89q.jchronic.utils.Span;
import com.sk89q.jchronic.utils.Token;
import java.util.LinkedList;
import java.util.List;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class Chronic {
    public static final String VERSION = "0.2.3";

    private Chronic() {
    }

    public static Span parse(String text) {
        return Chronic.parse(text, new Options());
    }

    public static Span parse(String text, Options options) {
        String normalizedText = Chronic.preNormalize(text);
        List<Token> tokens = Chronic.baseTokenize(normalizedText);
        LinkedList<Class<Repeater>> optionScannerClasses = new LinkedList<Class<Repeater>>();
        optionScannerClasses.add(Repeater.class);
        for (Class clazz : optionScannerClasses) {
            try {
                tokens = (LinkedList<Token>)clazz.getMethod("scan", List.class, Options.class).invoke(null, tokens, options);
            }
            catch (Throwable e) {
                throw new RuntimeException("Failed to scan tokens.", e);
            }
        }
        LinkedList<Class> linkedList = new LinkedList<Class>();
        linkedList.add(Grabber.class);
        linkedList.add(Pointer.class);
        linkedList.add(Scalar.class);
        linkedList.add(Ordinal.class);
        linkedList.add(Separator.class);
        linkedList.add(TimeZone.class);
        for (Class scannerClass : linkedList) {
            try {
                tokens = (List)scannerClass.getMethod("scan", List.class, Options.class).invoke(null, tokens, options);
            }
            catch (Throwable e) {
                throw new RuntimeException("Failed to scan tokens.", e);
            }
        }
        LinkedList<Token> taggedTokens = new LinkedList<Token>();
        for (Token token : tokens) {
            if (!token.isTagged()) continue;
            taggedTokens.add(token);
        }
        tokens = taggedTokens;
        if (options.isDebug()) {
            System.out.println("Chronic.parse: " + tokens);
        }
        Span span = Handler.tokensToSpan(tokens, options);
        if (options.isGuess()) {
            span = Chronic.guess(span);
        }
        return span;
    }

    protected static String preNormalize(String text) {
        String normalizedText = text.toLowerCase();
        normalizedText = Chronic.numericizeNumbers(normalizedText);
        normalizedText = normalizedText.replaceAll("['\"\\.]", "");
        normalizedText = normalizedText.replaceAll("([/\\-,@])", " $1 ");
        normalizedText = normalizedText.replaceAll("\\btoday\\b", "this day");
        normalizedText = normalizedText.replaceAll("\\btomm?orr?ow\\b", "next day");
        normalizedText = normalizedText.replaceAll("\\byesterday\\b", "last day");
        normalizedText = normalizedText.replaceAll("\\bnoon\\b", "12:00");
        normalizedText = normalizedText.replaceAll("\\bmidnight\\b", "24:00");
        normalizedText = normalizedText.replaceAll("\\bbefore now\\b", "past");
        normalizedText = normalizedText.replaceAll("\\bnow\\b", "this second");
        normalizedText = normalizedText.replaceAll("\\b(ago|before)\\b", "past");
        normalizedText = normalizedText.replaceAll("\\bthis past\\b", "last");
        normalizedText = normalizedText.replaceAll("\\bthis last\\b", "last");
        normalizedText = normalizedText.replaceAll("\\b(?:in|during) the (morning)\\b", "$1");
        normalizedText = normalizedText.replaceAll("\\b(?:in the|during the|at) (afternoon|evening|night)\\b", "$1");
        normalizedText = normalizedText.replaceAll("\\btonight\\b", "this night");
        normalizedText = normalizedText.replaceAll("(?=\\w)([ap]m|oclock)\\b", " $1");
        normalizedText = normalizedText.replaceAll("\\b(hence|after|from)\\b", "future");
        normalizedText = Chronic.numericizeOrdinals(normalizedText);
        return normalizedText;
    }

    protected static String numericizeNumbers(String text) {
        return Numerizer.numerize(text);
    }

    protected static String numericizeOrdinals(String text) {
        return text;
    }

    protected static List<Token> baseTokenize(String text) {
        String[] words = text.split(" ");
        LinkedList<Token> tokens = new LinkedList<Token>();
        String[] stringArray = words;
        int n = words.length;
        int n2 = 0;
        while (n2 < n) {
            String word = stringArray[n2];
            tokens.add(new Token(word));
            ++n2;
        }
        return tokens;
    }

    public static Span guess(Span span) {
        if (span == null) {
            return null;
        }
        long guessValue = span.getWidth() > 1L ? span.getBegin() + span.getWidth() / 2L : span.getBegin();
        Span guess = new Span(guessValue, guessValue);
        return guess;
    }
}

