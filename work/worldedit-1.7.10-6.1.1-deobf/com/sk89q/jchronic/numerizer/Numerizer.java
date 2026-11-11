/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.jchronic.numerizer;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Numerizer {
    protected static DirectNum[] DIRECT_NUMS;
    protected static TenPrefix[] TEN_PREFIXES;
    protected static BigPrefix[] BIG_PREFIXES;
    private static final Pattern DEHYPHENATOR;
    private static final Pattern DEHALFER;
    private static final Pattern DEHAALFER;
    private static final Pattern ANDITION_PATTERN;

    static {
        LinkedList<DirectNum> directNums = new LinkedList<DirectNum>();
        directNums.add(new DirectNum("eleven", "11"));
        directNums.add(new DirectNum("twelve", "12"));
        directNums.add(new DirectNum("thirteen", "13"));
        directNums.add(new DirectNum("fourteen", "14"));
        directNums.add(new DirectNum("fifteen", "15"));
        directNums.add(new DirectNum("sixteen", "16"));
        directNums.add(new DirectNum("seventeen", "17"));
        directNums.add(new DirectNum("eighteen", "18"));
        directNums.add(new DirectNum("nineteen", "19"));
        directNums.add(new DirectNum("ninteen", "19"));
        directNums.add(new DirectNum("zero", "0"));
        directNums.add(new DirectNum("one", "1"));
        directNums.add(new DirectNum("two", "2"));
        directNums.add(new DirectNum("three", "3"));
        directNums.add(new DirectNum("four(\\W|$)", "4$1"));
        directNums.add(new DirectNum("five", "5"));
        directNums.add(new DirectNum("six(\\W|$)", "6$1"));
        directNums.add(new DirectNum("seven(\\W|$)", "7$1"));
        directNums.add(new DirectNum("eight(\\W|$)", "8$1"));
        directNums.add(new DirectNum("nine(\\W|$)", "9$1"));
        directNums.add(new DirectNum("ten", "10"));
        directNums.add(new DirectNum("\\ba\\b", "1"));
        DIRECT_NUMS = directNums.toArray(new DirectNum[directNums.size()]);
        LinkedList<TenPrefix> tenPrefixes = new LinkedList<TenPrefix>();
        tenPrefixes.add(new TenPrefix("twenty", 20L));
        tenPrefixes.add(new TenPrefix("thirty", 30L));
        tenPrefixes.add(new TenPrefix("fourty", 40L));
        tenPrefixes.add(new TenPrefix("fifty", 50L));
        tenPrefixes.add(new TenPrefix("sixty", 60L));
        tenPrefixes.add(new TenPrefix("seventy", 70L));
        tenPrefixes.add(new TenPrefix("eighty", 80L));
        tenPrefixes.add(new TenPrefix("ninety", 90L));
        tenPrefixes.add(new TenPrefix("ninty", 90L));
        TEN_PREFIXES = tenPrefixes.toArray(new TenPrefix[tenPrefixes.size()]);
        LinkedList<BigPrefix> bigPrefixes = new LinkedList<BigPrefix>();
        bigPrefixes.add(new BigPrefix("hundred", 100L));
        bigPrefixes.add(new BigPrefix("thousand", 1000L));
        bigPrefixes.add(new BigPrefix("million", 1000000L));
        bigPrefixes.add(new BigPrefix("billion", 1000000000L));
        bigPrefixes.add(new BigPrefix("trillion", 1000000000000L));
        BIG_PREFIXES = bigPrefixes.toArray(new BigPrefix[bigPrefixes.size()]);
        DEHYPHENATOR = Pattern.compile(" +|(\\D)-(\\D)");
        DEHALFER = Pattern.compile("a half", 2);
        DEHAALFER = Pattern.compile("(\\d+)(?: | and |-)*haAlf", 2);
        ANDITION_PATTERN = Pattern.compile("(\\d+)( | and )(\\d+)(?=\\W|$)");
    }

    public static String numerize(String str) {
        StringBuffer matcherBuffer;
        Matcher matcher;
        String numerizedStr = str;
        numerizedStr = DEHYPHENATOR.matcher(numerizedStr).replaceAll("$1 $2");
        numerizedStr = DEHALFER.matcher(numerizedStr).replaceAll("haAlf");
        Object[] objectArray = DIRECT_NUMS;
        int n = DIRECT_NUMS.length;
        int n2 = 0;
        while (n2 < n) {
            DirectNum dn = objectArray[n2];
            numerizedStr = dn.getName().matcher(numerizedStr).replaceAll(dn.getNumber());
            ++n2;
        }
        objectArray = TEN_PREFIXES;
        n = TEN_PREFIXES.length;
        n2 = 0;
        while (n2 < n) {
            Object tp = objectArray[n2];
            matcher = ((Prefix)tp).getName().matcher(numerizedStr);
            if (matcher.find()) {
                matcherBuffer = new StringBuffer();
                do {
                    if (matcher.group(1) == null) {
                        matcher.appendReplacement(matcherBuffer, String.valueOf(((Prefix)tp).getNumber()));
                        continue;
                    }
                    matcher.appendReplacement(matcherBuffer, String.valueOf(((Prefix)tp).getNumber() + Long.parseLong(matcher.group(1).trim())));
                } while (matcher.find());
                matcher.appendTail(matcherBuffer);
                numerizedStr = matcherBuffer.toString();
            }
            ++n2;
        }
        objectArray = BIG_PREFIXES;
        n = BIG_PREFIXES.length;
        n2 = 0;
        while (n2 < n) {
            Object bp = objectArray[n2];
            matcher = ((Prefix)bp).getName().matcher(numerizedStr);
            if (matcher.find()) {
                matcherBuffer = new StringBuffer();
                do {
                    if (matcher.group(1) == null) {
                        matcher.appendReplacement(matcherBuffer, String.valueOf(((Prefix)bp).getNumber()));
                        continue;
                    }
                    matcher.appendReplacement(matcherBuffer, String.valueOf(((Prefix)bp).getNumber() * Long.parseLong(matcher.group(1).trim())));
                } while (matcher.find());
                matcher.appendTail(matcherBuffer);
                numerizedStr = matcherBuffer.toString();
                numerizedStr = Numerizer.andition(numerizedStr);
            }
            ++n2;
        }
        Matcher matcher2 = DEHAALFER.matcher(numerizedStr);
        if (matcher2.find()) {
            StringBuffer matcherBuffer2 = new StringBuffer();
            do {
                matcher2.appendReplacement(matcherBuffer2, String.valueOf(Float.parseFloat(matcher2.group(1).trim()) + 0.5f));
            } while (matcher2.find());
            matcher2.appendTail(matcherBuffer2);
            numerizedStr = matcherBuffer2.toString();
        }
        return numerizedStr;
    }

    public static String andition(String str) {
        StringBuffer anditionStr = new StringBuffer(str);
        Matcher matcher = ANDITION_PATTERN.matcher(anditionStr);
        while (matcher.find()) {
            if (!matcher.group(2).equalsIgnoreCase(" and ") && matcher.group(1).length() <= matcher.group(3).length()) continue;
            anditionStr.replace(matcher.start(), matcher.end(), String.valueOf(Integer.parseInt(matcher.group(1).trim()) + Integer.parseInt(matcher.group(3).trim())));
            matcher = ANDITION_PATTERN.matcher(anditionStr);
        }
        return anditionStr.toString();
    }

    protected static class BigPrefix
    extends Prefix {
        public BigPrefix(String name, long number) {
            super(Pattern.compile("(\\d*) *" + name, 2), number);
        }
    }

    protected static class DirectNum {
        private Pattern _name;
        private String _number;

        public DirectNum(String name, String number) {
            this._name = Pattern.compile(name, 2);
            this._number = number;
        }

        public Pattern getName() {
            return this._name;
        }

        public String getNumber() {
            return this._number;
        }
    }

    protected static class Prefix {
        private Pattern _name;
        private long _number;

        public Prefix(Pattern name, long number) {
            this._name = name;
            this._number = number;
        }

        public Pattern getName() {
            return this._name;
        }

        public long getNumber() {
            return this._number;
        }
    }

    protected static class TenPrefix
    extends Prefix {
        public TenPrefix(String name, long number) {
            super(Pattern.compile("(?:" + name + ")( *\\d(?=\\D|$))*", 2), number);
        }
    }
}

