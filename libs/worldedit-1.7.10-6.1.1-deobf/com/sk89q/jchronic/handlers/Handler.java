/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.jchronic.handlers;

import com.sk89q.jchronic.Options;
import com.sk89q.jchronic.handlers.HandlerPattern;
import com.sk89q.jchronic.handlers.HandlerTypePattern;
import com.sk89q.jchronic.handlers.IHandler;
import com.sk89q.jchronic.handlers.ORGRHandler;
import com.sk89q.jchronic.handlers.ORSRHandler;
import com.sk89q.jchronic.handlers.PSRHandler;
import com.sk89q.jchronic.handlers.RGRHandler;
import com.sk89q.jchronic.handlers.RHandler;
import com.sk89q.jchronic.handlers.RdnRmnSdTTzSyHandler;
import com.sk89q.jchronic.handlers.RmnOdHandler;
import com.sk89q.jchronic.handlers.RmnSdHandler;
import com.sk89q.jchronic.handlers.RmnSdSyHandler;
import com.sk89q.jchronic.handlers.RmnSyHandler;
import com.sk89q.jchronic.handlers.SRPAHandler;
import com.sk89q.jchronic.handlers.SRPHandler;
import com.sk89q.jchronic.handlers.SdRmnSyHandler;
import com.sk89q.jchronic.handlers.SdSmSyHandler;
import com.sk89q.jchronic.handlers.SmSdHandler;
import com.sk89q.jchronic.handlers.SmSdSyHandler;
import com.sk89q.jchronic.handlers.SmSyHandler;
import com.sk89q.jchronic.handlers.SySmSdHandler;
import com.sk89q.jchronic.handlers.TagPattern;
import com.sk89q.jchronic.repeaters.EnumRepeaterDayPortion;
import com.sk89q.jchronic.repeaters.IntegerRepeaterDayPortion;
import com.sk89q.jchronic.repeaters.Repeater;
import com.sk89q.jchronic.repeaters.RepeaterDayName;
import com.sk89q.jchronic.repeaters.RepeaterDayPortion;
import com.sk89q.jchronic.repeaters.RepeaterMonthName;
import com.sk89q.jchronic.repeaters.RepeaterTime;
import com.sk89q.jchronic.tags.Grabber;
import com.sk89q.jchronic.tags.Ordinal;
import com.sk89q.jchronic.tags.OrdinalDay;
import com.sk89q.jchronic.tags.Pointer;
import com.sk89q.jchronic.tags.Scalar;
import com.sk89q.jchronic.tags.ScalarDay;
import com.sk89q.jchronic.tags.ScalarMonth;
import com.sk89q.jchronic.tags.ScalarYear;
import com.sk89q.jchronic.tags.Separator;
import com.sk89q.jchronic.tags.SeparatorAt;
import com.sk89q.jchronic.tags.SeparatorComma;
import com.sk89q.jchronic.tags.SeparatorIn;
import com.sk89q.jchronic.tags.SeparatorSlashOrDash;
import com.sk89q.jchronic.tags.TimeZone;
import com.sk89q.jchronic.utils.Span;
import com.sk89q.jchronic.utils.Tick;
import com.sk89q.jchronic.utils.Time;
import com.sk89q.jchronic.utils.Token;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class Handler {
    private static Map<HandlerType, List<Handler>> _definitions;
    private HandlerPattern[] _patterns;
    private IHandler _handler;
    private boolean _compatible;

    public Handler(IHandler handler, HandlerPattern ... patterns) {
        this(handler, true, patterns);
    }

    public Handler(IHandler handler, boolean compatible, HandlerPattern ... patterns) {
        this._handler = handler;
        this._compatible = compatible;
        this._patterns = patterns;
    }

    public boolean isCompatible(Options options) {
        return !options.isCompatibilityMode() || this._compatible;
    }

    public IHandler getHandler() {
        return this._handler;
    }

    public boolean match(List<Token> tokens, Map<HandlerType, List<Handler>> definitions) {
        int tokenIndex = 0;
        HandlerPattern[] handlerPatternArray = this._patterns;
        int n = this._patterns.length;
        int n2 = 0;
        while (n2 < n) {
            HandlerPattern pattern = handlerPatternArray[n2];
            boolean optional = pattern.isOptional();
            if (pattern instanceof TagPattern) {
                boolean match;
                boolean bl = match = tokenIndex < tokens.size() && tokens.get(tokenIndex).getTags(((TagPattern)pattern).getTagClass()).size() > 0;
                if (!match && !optional) {
                    return false;
                }
                if (match) {
                    ++tokenIndex;
                }
            } else if (pattern instanceof HandlerTypePattern) {
                if (optional && tokenIndex == tokens.size()) {
                    return true;
                }
                List<Handler> subHandlers = definitions.get((Object)((HandlerTypePattern)pattern).getType());
                for (Handler subHandler : subHandlers) {
                    if (!subHandler.match(tokens.subList(tokenIndex, tokens.size()), definitions)) continue;
                    return true;
                }
                return false;
            }
            ++n2;
        }
        return tokenIndex == tokens.size();
    }

    public String toString() {
        return "[Handler: " + this._handler + "]";
    }

    public static synchronized Map<HandlerType, List<Handler>> definitions() {
        if (_definitions == null) {
            HashMap<HandlerType, List<Handler>> definitions = new HashMap<HandlerType, List<Handler>>();
            LinkedList<Handler> timeHandlers = new LinkedList<Handler>();
            timeHandlers.add(new Handler(null, new TagPattern(RepeaterTime.class), new TagPattern(RepeaterDayPortion.class, true)));
            definitions.put(HandlerType.TIME, timeHandlers);
            LinkedList<Handler> dateHandlers = new LinkedList<Handler>();
            dateHandlers.add(new Handler((IHandler)new RdnRmnSdTTzSyHandler(), new TagPattern(RepeaterDayName.class), new TagPattern(RepeaterMonthName.class), new TagPattern(ScalarDay.class), new TagPattern(RepeaterTime.class), new TagPattern(TimeZone.class), new TagPattern(ScalarYear.class)));
            dateHandlers.add(new Handler((IHandler)new RmnSdSyHandler(), new TagPattern(RepeaterMonthName.class), new TagPattern(ScalarDay.class), new TagPattern(SeparatorComma.class, true), new TagPattern(ScalarYear.class)));
            dateHandlers.add(new Handler((IHandler)new RmnSdSyHandler(), new TagPattern(RepeaterMonthName.class), new TagPattern(ScalarDay.class), new TagPattern(ScalarYear.class), new TagPattern(SeparatorAt.class, true), new HandlerTypePattern(HandlerType.TIME, true)));
            dateHandlers.add(new Handler((IHandler)new RmnSdHandler(), new TagPattern(RepeaterMonthName.class), new TagPattern(ScalarDay.class), new TagPattern(SeparatorAt.class, true), new HandlerTypePattern(HandlerType.TIME, true)));
            dateHandlers.add(new Handler((IHandler)new RmnOdHandler(), new TagPattern(RepeaterMonthName.class), new TagPattern(OrdinalDay.class), new TagPattern(SeparatorAt.class, true), new HandlerTypePattern(HandlerType.TIME, true)));
            dateHandlers.add(new Handler((IHandler)new RmnSyHandler(), new TagPattern(RepeaterMonthName.class), new TagPattern(ScalarYear.class)));
            dateHandlers.add(new Handler((IHandler)new SdRmnSyHandler(), new TagPattern(ScalarDay.class), new TagPattern(RepeaterMonthName.class), new TagPattern(ScalarYear.class), new TagPattern(SeparatorAt.class, true), new HandlerTypePattern(HandlerType.TIME, true)));
            dateHandlers.add(new Handler((IHandler)new SmSdSyHandler(), new TagPattern(ScalarMonth.class), new TagPattern(SeparatorSlashOrDash.class), new TagPattern(ScalarDay.class), new TagPattern(SeparatorSlashOrDash.class), new TagPattern(ScalarYear.class), new TagPattern(SeparatorAt.class, true), new HandlerTypePattern(HandlerType.TIME, true)));
            dateHandlers.add(new Handler((IHandler)new SdSmSyHandler(), new TagPattern(ScalarDay.class), new TagPattern(SeparatorSlashOrDash.class), new TagPattern(ScalarMonth.class), new TagPattern(SeparatorSlashOrDash.class), new TagPattern(ScalarYear.class), new TagPattern(SeparatorAt.class, true), new HandlerTypePattern(HandlerType.TIME, true)));
            dateHandlers.add(new Handler((IHandler)new SySmSdHandler(), new TagPattern(ScalarYear.class), new TagPattern(SeparatorSlashOrDash.class), new TagPattern(ScalarMonth.class), new TagPattern(SeparatorSlashOrDash.class), new TagPattern(ScalarDay.class), new TagPattern(SeparatorAt.class, true), new HandlerTypePattern(HandlerType.TIME, true)));
            dateHandlers.add(new Handler((IHandler)new SmSdHandler(), false, new TagPattern(ScalarMonth.class), new TagPattern(SeparatorSlashOrDash.class), new TagPattern(ScalarDay.class)));
            dateHandlers.add(new Handler((IHandler)new SmSyHandler(), new TagPattern(ScalarMonth.class), new TagPattern(SeparatorSlashOrDash.class), new TagPattern(ScalarYear.class)));
            definitions.put(HandlerType.DATE, dateHandlers);
            LinkedList<Handler> anchorHandlers = new LinkedList<Handler>();
            anchorHandlers.add(new Handler((IHandler)new RHandler(), new TagPattern(Grabber.class, true), new TagPattern(Repeater.class), new TagPattern(SeparatorAt.class, true), new TagPattern(Repeater.class, true), new TagPattern(Repeater.class, true)));
            anchorHandlers.add(new Handler((IHandler)new RHandler(), new TagPattern(Grabber.class, true), new TagPattern(Repeater.class), new TagPattern(Repeater.class), new TagPattern(SeparatorAt.class, true), new TagPattern(Repeater.class, true), new TagPattern(Repeater.class, true)));
            anchorHandlers.add(new Handler((IHandler)new RGRHandler(), new TagPattern(Repeater.class), new TagPattern(Grabber.class), new TagPattern(Repeater.class)));
            definitions.put(HandlerType.ANCHOR, anchorHandlers);
            LinkedList<Handler> arrowHandlers = new LinkedList<Handler>();
            arrowHandlers.add(new Handler((IHandler)new SRPHandler(), new TagPattern(Scalar.class), new TagPattern(Repeater.class), new TagPattern(Pointer.class)));
            arrowHandlers.add(new Handler((IHandler)new PSRHandler(), new TagPattern(Pointer.class), new TagPattern(Scalar.class), new TagPattern(Repeater.class)));
            arrowHandlers.add(new Handler((IHandler)new SRPAHandler(), new TagPattern(Scalar.class), new TagPattern(Repeater.class), new TagPattern(Pointer.class), new HandlerTypePattern(HandlerType.ANCHOR)));
            definitions.put(HandlerType.ARROW, arrowHandlers);
            LinkedList<Handler> narrowHandlers = new LinkedList<Handler>();
            narrowHandlers.add(new Handler((IHandler)new ORSRHandler(), new TagPattern(Ordinal.class), new TagPattern(Repeater.class), new TagPattern(SeparatorIn.class), new TagPattern(Repeater.class)));
            narrowHandlers.add(new Handler((IHandler)new ORGRHandler(), new TagPattern(Ordinal.class), new TagPattern(Repeater.class), new TagPattern(Grabber.class), new TagPattern(Repeater.class)));
            definitions.put(HandlerType.NARROW, narrowHandlers);
            _definitions = definitions;
        }
        return _definitions;
    }

    public static Span tokensToSpan(List<Token> tokens, Options options) {
        if (options.isDebug()) {
            System.out.println("Chronic.tokensToSpan: " + tokens);
        }
        Map<HandlerType, List<Handler>> definitions = Handler.definitions();
        for (Handler handler : definitions.get((Object)HandlerType.DATE)) {
            if (!handler.isCompatible(options) || !handler.match(tokens, definitions)) continue;
            if (options.isDebug()) {
                System.out.println("Chronic.tokensToSpan: date");
            }
            LinkedList<Token> goodTokens = new LinkedList<Token>();
            for (Token token : tokens) {
                if (token.getTag(Separator.class) != null) continue;
                goodTokens.add(token);
            }
            return handler.getHandler().handle(goodTokens, options);
        }
        for (Handler handler : definitions.get((Object)HandlerType.ANCHOR)) {
            if (!handler.isCompatible(options) || !handler.match(tokens, definitions)) continue;
            if (options.isDebug()) {
                System.out.println("Chronic.tokensToSpan: anchor");
            }
            LinkedList<Token> goodTokens = new LinkedList<Token>();
            for (Token token : tokens) {
                if (token.getTag(Separator.class) != null) continue;
                goodTokens.add(token);
            }
            return handler.getHandler().handle(goodTokens, options);
        }
        for (Handler handler : definitions.get((Object)HandlerType.ARROW)) {
            if (!handler.isCompatible(options) || !handler.match(tokens, definitions)) continue;
            if (options.isDebug()) {
                System.out.println("Chronic.tokensToSpan: arrow");
            }
            LinkedList<Token> goodTokens = new LinkedList<Token>();
            for (Token token : tokens) {
                if (token.getTag(SeparatorAt.class) != null || token.getTag(SeparatorSlashOrDash.class) != null || token.getTag(SeparatorComma.class) != null) continue;
                goodTokens.add(token);
            }
            return handler.getHandler().handle(goodTokens, options);
        }
        for (Handler handler : definitions.get((Object)HandlerType.NARROW)) {
            if (!handler.isCompatible(options) || !handler.match(tokens, definitions)) continue;
            if (options.isDebug()) {
                System.out.println("Chronic.tokensToSpan: narrow");
            }
            return handler.getHandler().handle(tokens, options);
        }
        if (options.isDebug()) {
            System.out.println("Chronic.tokensToSpan: none");
        }
        return null;
    }

    public static List<Repeater<?>> getRepeaters(List<Token> tokens) {
        LinkedList repeaters = new LinkedList();
        for (Token token : tokens) {
            Repeater tag = token.getTag(Repeater.class);
            if (tag == null) continue;
            repeaters.add(tag);
        }
        Collections.sort(repeaters);
        Collections.reverse(repeaters);
        return repeaters;
    }

    public static Span getAnchor(List<Token> tokens, Options options) {
        Span outerSpan;
        Grabber grabber = new Grabber(Grabber.Relative.THIS);
        Pointer.PointerType pointer = Pointer.PointerType.FUTURE;
        List<Repeater<?>> repeaters = Handler.getRepeaters(tokens);
        int i = 0;
        while (i < repeaters.size()) {
            tokens.remove(tokens.size() - 1);
            ++i;
        }
        if (!tokens.isEmpty() && tokens.get(0).getTag(Grabber.class) != null) {
            grabber = tokens.get(0).getTag(Grabber.class);
            tokens.remove(tokens.size() - 1);
        }
        Repeater<?> head = repeaters.remove(0);
        head.setStart((Calendar)options.getNow().clone());
        Grabber.Relative grabberType = (Grabber.Relative)((Object)grabber.getType());
        if (grabberType == Grabber.Relative.LAST) {
            outerSpan = head.nextSpan(Pointer.PointerType.PAST);
        } else if (grabberType == Grabber.Relative.THIS) {
            outerSpan = repeaters.size() > 0 ? head.thisSpan(Pointer.PointerType.NONE) : head.thisSpan(options.getContext());
        } else if (grabberType == Grabber.Relative.NEXT) {
            outerSpan = head.nextSpan(Pointer.PointerType.FUTURE);
        } else {
            throw new IllegalArgumentException("Invalid grabber type " + (Object)((Object)grabberType) + ".");
        }
        if (options.isDebug()) {
            System.out.println("Chronic.getAnchor: outerSpan = " + outerSpan + "; repeaters = " + repeaters);
        }
        Span anchor = Handler.findWithin(repeaters, outerSpan, pointer, options);
        return anchor;
    }

    public static Span dayOrTime(Calendar dayStart, List<Token> timeTokens, Options options) {
        Span outerSpan = new Span(dayStart, Time.cloneAndAdd(dayStart, 5, 1L));
        if (!timeTokens.isEmpty()) {
            options.setNow(outerSpan.getBeginCalendar());
            Span time = Handler.getAnchor(Handler.dealiasAndDisambiguateTimes(timeTokens, options), options);
            return time;
        }
        return outerSpan;
    }

    public static Span findWithin(List<Repeater<?>> tags, Span span, Pointer.PointerType pointer, Options options) {
        if (options.isDebug()) {
            System.out.println("Chronic.findWithin: " + tags + " in " + span);
        }
        if (tags.isEmpty()) {
            return span;
        }
        Repeater<?> head = tags.get(0);
        LinkedList rest = tags.size() > 1 ? tags.subList(1, tags.size()) : new LinkedList();
        head.setStart(pointer == Pointer.PointerType.FUTURE ? span.getBeginCalendar() : span.getEndCalendar());
        Span h = head.thisSpan(Pointer.PointerType.NONE);
        if (span.contains(h.getBegin()) || span.contains(h.getEnd())) {
            return Handler.findWithin(rest, h, pointer, options);
        }
        return null;
    }

    public static List<Token> dealiasAndDisambiguateTimes(List<Token> tokens, Options options) {
        int dayPortionIndex = -1;
        int tokenSize = tokens.size();
        int i = 0;
        while (dayPortionIndex == -1 && i < tokenSize) {
            Token t = tokens.get(i);
            if (t.getTag(RepeaterDayPortion.class) != null) {
                dayPortionIndex = i;
            }
            ++i;
        }
        int timeIndex = -1;
        int i2 = 0;
        while (timeIndex == -1 && i2 < tokenSize) {
            Token t = tokens.get(i2);
            if (t.getTag(RepeaterTime.class) != null) {
                timeIndex = i2;
            }
            ++i2;
        }
        if (dayPortionIndex != -1 && timeIndex != -1) {
            Token t1 = tokens.get(dayPortionIndex);
            RepeaterDayPortion t1Tag = t1.getTag(RepeaterDayPortion.class);
            Object t1TagType = t1Tag.getType();
            if (RepeaterDayPortion.DayPortion.MORNING.equals(t1TagType)) {
                if (options.isDebug()) {
                    System.out.println("Chronic.dealiasAndDisambiguateTimes: morning->am");
                }
                t1.untag(RepeaterDayPortion.class);
                t1.tag(new EnumRepeaterDayPortion(RepeaterDayPortion.DayPortion.AM));
            } else if (RepeaterDayPortion.DayPortion.AFTERNOON.equals(t1TagType) || RepeaterDayPortion.DayPortion.EVENING.equals(t1TagType) || RepeaterDayPortion.DayPortion.NIGHT.equals(t1TagType)) {
                if (options.isDebug()) {
                    System.out.println("Chronic.dealiasAndDisambiguateTimes: " + t1TagType + "->pm");
                }
                t1.untag(RepeaterDayPortion.class);
                t1.tag(new EnumRepeaterDayPortion(RepeaterDayPortion.DayPortion.PM));
            }
        }
        if (options.getAmbiguousTimeRange() != 0) {
            LinkedList<Token> ttokens = new LinkedList<Token>();
            int i3 = 0;
            while (i3 < tokenSize) {
                Token t0 = tokens.get(i3);
                ttokens.add(t0);
                Token t1 = null;
                if (i3 < tokenSize - 1) {
                    t1 = tokens.get(i3 + 1);
                }
                if (t0.getTag(RepeaterTime.class) != null && ((Tick)t0.getTag(RepeaterTime.class).getType()).isAmbiguous() && (t1 == null || t1.getTag(RepeaterDayPortion.class) == null)) {
                    Token distoken = new Token("disambiguator");
                    distoken.tag(new IntegerRepeaterDayPortion(options.getAmbiguousTimeRange()));
                    ttokens.add(distoken);
                }
                ++i3;
            }
            tokens = ttokens;
        }
        if (options.isDebug()) {
            System.out.println("Chronic.dealiasAndDisambiguateTimes: " + tokens);
        }
        return tokens;
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static enum HandlerType {
        TIME,
        DATE,
        ANCHOR,
        ARROW,
        NARROW;

    }
}

