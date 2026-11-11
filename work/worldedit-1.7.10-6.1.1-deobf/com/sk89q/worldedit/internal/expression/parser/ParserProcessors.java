/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.internal.expression.parser;

import com.sk89q.worldedit.internal.expression.Identifiable;
import com.sk89q.worldedit.internal.expression.lexer.tokens.OperatorToken;
import com.sk89q.worldedit.internal.expression.lexer.tokens.Token;
import com.sk89q.worldedit.internal.expression.parser.ParserException;
import com.sk89q.worldedit.internal.expression.parser.UnaryOperator;
import com.sk89q.worldedit.internal.expression.runtime.Conditional;
import com.sk89q.worldedit.internal.expression.runtime.Operators;
import com.sk89q.worldedit.internal.expression.runtime.RValue;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public final class ParserProcessors {
    private static final Map<String, String> unaryOpMap = new HashMap<String, String>();
    private static final Map<String, String>[] binaryOpMapsLA;
    private static final Map<String, String>[] binaryOpMapsRA;

    private ParserProcessors() {
    }

    static RValue processExpression(LinkedList<Identifiable> input) throws ParserException {
        return ParserProcessors.processBinaryOpsRA(input, binaryOpMapsRA.length - 1);
    }

    private static RValue processBinaryOpsLA(LinkedList<Identifiable> input, int level) throws ParserException {
        if (level < 0) {
            return ParserProcessors.processUnaryOps(input);
        }
        LinkedList<Identifiable> lhs = new LinkedList<Identifiable>();
        LinkedList<Identifiable> rhs = new LinkedList<Identifiable>();
        String operator = null;
        Iterator<Identifiable> it = input.descendingIterator();
        while (it.hasNext()) {
            Identifiable identifiable = it.next();
            if (operator == null) {
                rhs.addFirst(identifiable);
                if (!(identifiable instanceof OperatorToken) || (operator = binaryOpMapsLA[level].get(((OperatorToken)identifiable).operator)) == null) continue;
                rhs.removeFirst();
                continue;
            }
            lhs.addFirst(identifiable);
        }
        RValue rhsInvokable = ParserProcessors.processBinaryOpsLA(rhs, level - 1);
        if (operator == null) {
            return rhsInvokable;
        }
        RValue lhsInvokable = ParserProcessors.processBinaryOpsLA(lhs, level);
        try {
            return Operators.getOperator(input.get(0).getPosition(), operator, lhsInvokable, rhsInvokable);
        }
        catch (NoSuchMethodException e) {
            Token operatorToken = (Token)input.get(lhs.size());
            throw new ParserException(operatorToken.getPosition(), "Couldn't find operator '" + operator + "'");
        }
    }

    private static RValue processBinaryOpsRA(LinkedList<Identifiable> input, int level) throws ParserException {
        if (level < 0) {
            return ParserProcessors.processTernaryOps(input);
        }
        LinkedList<Identifiable> lhs = new LinkedList<Identifiable>();
        LinkedList<Identifiable> rhs = new LinkedList<Identifiable>();
        String operator = null;
        for (Identifiable identifiable : input) {
            if (operator == null) {
                lhs.addLast(identifiable);
                if (!(identifiable instanceof OperatorToken) || (operator = binaryOpMapsRA[level].get(((OperatorToken)identifiable).operator)) == null) continue;
                lhs.removeLast();
                continue;
            }
            rhs.addLast(identifiable);
        }
        RValue lhsInvokable = ParserProcessors.processBinaryOpsRA(lhs, level - 1);
        if (operator == null) {
            return lhsInvokable;
        }
        RValue rhsInvokable = ParserProcessors.processBinaryOpsRA(rhs, level);
        try {
            return Operators.getOperator(input.get(0).getPosition(), operator, lhsInvokable, rhsInvokable);
        }
        catch (NoSuchMethodException e) {
            Token operatorToken = (Token)input.get(lhs.size());
            throw new ParserException(operatorToken.getPosition(), "Couldn't find operator '" + operator + "'");
        }
    }

    private static RValue processTernaryOps(LinkedList<Identifiable> input) throws ParserException {
        LinkedList<Identifiable> lhs = new LinkedList<Identifiable>();
        LinkedList<Identifiable> mhs = new LinkedList<Identifiable>();
        LinkedList<Identifiable> rhs = new LinkedList<Identifiable>();
        int partsFound = 0;
        int conditionalsFound = 0;
        for (Identifiable identifiable : input) {
            char character = identifiable.id();
            switch (character) {
                case '?': {
                    ++conditionalsFound;
                    break;
                }
                case ':': {
                    --conditionalsFound;
                }
            }
            if (conditionalsFound < 0) {
                throw new ParserException(identifiable.getPosition(), "Unexpected ':'");
            }
            switch (partsFound) {
                case 0: {
                    if (character == '?') {
                        partsFound = 1;
                        break;
                    }
                    lhs.addLast(identifiable);
                    break;
                }
                case 1: {
                    if (conditionalsFound == 0 && character == ':') {
                        partsFound = 2;
                        break;
                    }
                    mhs.addLast(identifiable);
                    break;
                }
                case 2: {
                    rhs.addLast(identifiable);
                }
            }
        }
        if (partsFound < 2) {
            return ParserProcessors.processBinaryOpsLA(input, binaryOpMapsLA.length - 1);
        }
        RValue lhsInvokable = ParserProcessors.processBinaryOpsLA(lhs, binaryOpMapsLA.length - 1);
        RValue mhsInvokable = ParserProcessors.processTernaryOps(mhs);
        RValue rhsInvokable = ParserProcessors.processTernaryOps(rhs);
        return new Conditional(input.get(lhs.size()).getPosition(), lhsInvokable, mhsInvokable, rhsInvokable);
    }

    private static RValue processUnaryOps(LinkedList<Identifiable> input) throws ParserException {
        Identifiable last;
        LinkedList<UnaryOperator> postfixes = new LinkedList<UnaryOperator>();
        while (true) {
            if (input.isEmpty()) {
                throw new ParserException(-1, "Expression missing.");
            }
            last = input.removeLast();
            if (last instanceof OperatorToken) {
                postfixes.addLast(new UnaryOperator(last.getPosition(), "x" + ((OperatorToken)last).operator));
                continue;
            }
            if (!(last instanceof UnaryOperator)) break;
            postfixes.addLast(new UnaryOperator(last.getPosition(), "x" + ((UnaryOperator)last).operator));
        }
        Identifiable center = last;
        if (!(center instanceof RValue)) {
            throw new ParserException(center.getPosition(), "Expected expression, found " + center);
        }
        input.addAll(postfixes);
        RValue ret = (RValue)center;
        while (!input.isEmpty()) {
            Identifiable last2 = input.removeLast();
            int lastPosition = last2.getPosition();
            if (last2 instanceof UnaryOperator) {
                String operator = ((UnaryOperator)last2).operator;
                if (operator.equals("+")) continue;
                String opName = unaryOpMap.get(operator);
                if (opName != null) {
                    try {
                        ret = Operators.getOperator(lastPosition, opName, ret);
                        continue;
                    }
                    catch (NoSuchMethodException e) {
                        throw new ParserException(lastPosition, "No such prefix operator: " + operator);
                    }
                }
            }
            if (last2 instanceof Token) {
                throw new ParserException(lastPosition, "Extra token found in expression: " + last2);
            }
            if (last2 instanceof RValue) {
                throw new ParserException(lastPosition, "Extra expression found: " + last2);
            }
            throw new ParserException(lastPosition, "Extra element found: " + last2);
        }
        return ret;
    }

    static {
        unaryOpMap.put("-", "neg");
        unaryOpMap.put("!", "not");
        unaryOpMap.put("~", "inv");
        unaryOpMap.put("++", "inc");
        unaryOpMap.put("--", "dec");
        unaryOpMap.put("x++", "postinc");
        unaryOpMap.put("x--", "postdec");
        unaryOpMap.put("x!", "fac");
        Object[][][] binaryOpsLA = new Object[][][]{{{"^", "pow"}, {"**", "pow"}}, {{"*", "mul"}, {"/", "div"}, {"%", "mod"}}, {{"+", "add"}, {"-", "sub"}}, {{"<<", "shl"}, {">>", "shr"}}, {{"<", "lth"}, {">", "gth"}, {"<=", "leq"}, {">=", "geq"}}, {{"==", "equ"}, {"!=", "neq"}, {"~=", "near"}}, {{"&&", "and"}}, {{"||", "or"}}};
        Object[][][] binaryOpsRA = new Object[][][]{{{"=", "ass"}, {"+=", "aadd"}, {"-=", "asub"}, {"*=", "amul"}, {"/=", "adiv"}, {"%=", "amod"}, {"^=", "aexp"}}};
        binaryOpMapsLA = new Map[binaryOpsLA.length];
        Map[] lBinaryOpMapsLA = binaryOpMapsLA;
        block8: for (int i = 0; i < binaryOpsLA.length; ++i) {
            Object[][] a = binaryOpsLA[i];
            switch (a.length) {
                case 0: {
                    lBinaryOpMapsLA[i] = Collections.emptyMap();
                    continue block8;
                }
                case 1: {
                    Object[] first = a[0];
                    lBinaryOpMapsLA[i] = Collections.singletonMap((String)first[0], (String)first[1]);
                    continue block8;
                }
                default: {
                    HashMap<String, String> m = lBinaryOpMapsLA[i] = new HashMap<String, String>();
                    for (Object[] element : a) {
                        m.put((String)element[0], (String)element[1]);
                    }
                }
            }
        }
        binaryOpMapsRA = new Map[binaryOpsRA.length];
        Map[] lBinaryOpMapsRA = binaryOpMapsRA;
        block10: for (int i = 0; i < binaryOpsRA.length; ++i) {
            Object[][] a = binaryOpsRA[i];
            switch (a.length) {
                case 0: {
                    lBinaryOpMapsRA[i] = Collections.emptyMap();
                    continue block10;
                }
                case 1: {
                    Object[] first = a[0];
                    lBinaryOpMapsRA[i] = Collections.singletonMap((String)first[0], (String)first[1]);
                    continue block10;
                }
                default: {
                    HashMap<String, String> m = lBinaryOpMapsRA[i] = new HashMap<String, String>();
                    for (Object[] element : a) {
                        m.put((String)element[0], (String)element[1]);
                    }
                }
            }
        }
    }
}

