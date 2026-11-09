/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.internal.expression.parser;

import com.sk89q.worldedit.internal.expression.Expression;
import com.sk89q.worldedit.internal.expression.Identifiable;
import com.sk89q.worldedit.internal.expression.lexer.tokens.IdentifierToken;
import com.sk89q.worldedit.internal.expression.lexer.tokens.KeywordToken;
import com.sk89q.worldedit.internal.expression.lexer.tokens.NumberToken;
import com.sk89q.worldedit.internal.expression.lexer.tokens.OperatorToken;
import com.sk89q.worldedit.internal.expression.lexer.tokens.Token;
import com.sk89q.worldedit.internal.expression.parser.ParserException;
import com.sk89q.worldedit.internal.expression.parser.ParserProcessors;
import com.sk89q.worldedit.internal.expression.parser.UnaryOperator;
import com.sk89q.worldedit.internal.expression.parser.UnboundVariable;
import com.sk89q.worldedit.internal.expression.runtime.Break;
import com.sk89q.worldedit.internal.expression.runtime.Conditional;
import com.sk89q.worldedit.internal.expression.runtime.Constant;
import com.sk89q.worldedit.internal.expression.runtime.For;
import com.sk89q.worldedit.internal.expression.runtime.Function;
import com.sk89q.worldedit.internal.expression.runtime.Functions;
import com.sk89q.worldedit.internal.expression.runtime.LValue;
import com.sk89q.worldedit.internal.expression.runtime.RValue;
import com.sk89q.worldedit.internal.expression.runtime.Return;
import com.sk89q.worldedit.internal.expression.runtime.Sequence;
import com.sk89q.worldedit.internal.expression.runtime.SimpleFor;
import com.sk89q.worldedit.internal.expression.runtime.Switch;
import com.sk89q.worldedit.internal.expression.runtime.While;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int position = 0;
    private Expression expression;

    private Parser(List<Token> tokens, Expression expression) {
        this.tokens = tokens;
        this.expression = expression;
    }

    public static RValue parse(List<Token> tokens, Expression expression) throws ParserException {
        return new Parser(tokens, expression).parse();
    }

    private RValue parse() throws ParserException {
        RValue ret = this.parseStatements(false);
        if (this.position < this.tokens.size()) {
            Token token = this.peek();
            throw new ParserException(token.getPosition(), "Extra token at the end of the input: " + token);
        }
        ret.bindVariables(this.expression, false);
        return ret;
    }

    private RValue parseStatements(boolean singleStatement) throws ParserException {
        ArrayList<RValue> statements = new ArrayList<RValue>();
        block19: while (this.position < this.tokens.size()) {
            boolean expectSemicolon = false;
            Token current = this.peek();
            block0 : switch (current.id()) {
                case '{': {
                    this.consumeCharacter('{');
                    statements.add(this.parseStatements(false));
                    this.consumeCharacter('}');
                    break;
                }
                case '}': {
                    break block19;
                }
                case 'k': {
                    String keyword = ((KeywordToken)current).value;
                    switch (keyword.charAt(0)) {
                        case 'i': {
                            RValue falsePart;
                            ++this.position;
                            RValue condition = this.parseBracket();
                            RValue truePart = this.parseStatements(true);
                            if (this.hasKeyword("else")) {
                                ++this.position;
                                falsePart = this.parseStatements(true);
                            } else {
                                falsePart = null;
                            }
                            statements.add(new Conditional(current.getPosition(), condition, truePart, falsePart));
                            break block0;
                        }
                        case 'w': {
                            ++this.position;
                            RValue condition = this.parseBracket();
                            RValue body = this.parseStatements(true);
                            statements.add(new While(current.getPosition(), condition, body, false));
                            break block0;
                        }
                        case 'd': {
                            if (this.hasKeyword("default")) break block19;
                            ++this.position;
                            RValue body = this.parseStatements(true);
                            this.consumeKeyword("while");
                            RValue condition = this.parseBracket();
                            statements.add(new While(current.getPosition(), condition, body, true));
                            expectSemicolon = true;
                            break block0;
                        }
                        case 'f': {
                            this.consumeCharacter('(');
                            int oldPosition = ++this.position;
                            RValue init = this.parseExpression(true);
                            if (this.peek().id() == ';') {
                                ++this.position;
                                RValue condition = this.parseExpression(true);
                                this.consumeCharacter(';');
                                RValue increment = this.parseExpression(true);
                                this.consumeCharacter(')');
                                RValue body = this.parseStatements(true);
                                statements.add(new For(current.getPosition(), init, condition, increment, body));
                                break block0;
                            }
                            this.position = oldPosition;
                            Token variableToken = this.peek();
                            if (!(variableToken instanceof IdentifierToken)) {
                                throw new ParserException(variableToken.getPosition(), "Expected identifier");
                            }
                            RValue variable = this.expression.getVariable(((IdentifierToken)variableToken).value, true);
                            if (!(variable instanceof LValue)) {
                                throw new ParserException(variableToken.getPosition(), "Expected variable");
                            }
                            ++this.position;
                            Token equalsToken = this.peek();
                            if (!(equalsToken instanceof OperatorToken) || !((OperatorToken)equalsToken).operator.equals("=")) {
                                throw new ParserException(variableToken.getPosition(), "Expected '=' or a term and ';'");
                            }
                            ++this.position;
                            RValue first = this.parseExpression(true);
                            this.consumeCharacter(',');
                            RValue last = this.parseExpression(true);
                            this.consumeCharacter(')');
                            RValue body = this.parseStatements(true);
                            statements.add(new SimpleFor(current.getPosition(), (LValue)variable, first, last, body));
                            break block0;
                        }
                        case 'b': {
                            ++this.position;
                            statements.add(new Break(current.getPosition(), false));
                            break block0;
                        }
                        case 'c': {
                            if (this.hasKeyword("case")) break block19;
                            ++this.position;
                            statements.add(new Break(current.getPosition(), true));
                            break block0;
                        }
                        case 'r': {
                            ++this.position;
                            statements.add(new Return(current.getPosition(), this.parseExpression(true)));
                            expectSemicolon = true;
                            break block0;
                        }
                        case 's': {
                            ++this.position;
                            RValue parameter = this.parseBracket();
                            ArrayList<Double> values = new ArrayList<Double>();
                            ArrayList<RValue> caseStatements = new ArrayList<RValue>();
                            RValue defaultCase = null;
                            this.consumeCharacter('{');
                            while (this.peek().id() != '}') {
                                if (this.position >= this.tokens.size()) {
                                    throw new ParserException(current.getPosition(), "Expected '}' instead of EOF");
                                }
                                if (defaultCase != null) {
                                    throw new ParserException(current.getPosition(), "Expected '}' instead of " + this.peek());
                                }
                                if (this.hasKeyword("case")) {
                                    ++this.position;
                                    Token valueToken = this.peek();
                                    if (!(valueToken instanceof NumberToken)) {
                                        throw new ParserException(current.getPosition(), "Expected number instead of " + this.peek());
                                    }
                                    ++this.position;
                                    values.add(((NumberToken)valueToken).value);
                                    this.consumeCharacter(':');
                                    caseStatements.add(this.parseStatements(false));
                                    continue;
                                }
                                if (this.hasKeyword("default")) {
                                    ++this.position;
                                    this.consumeCharacter(':');
                                    defaultCase = this.parseStatements(false);
                                    continue;
                                }
                                throw new ParserException(current.getPosition(), "Expected 'case' or 'default' instead of " + this.peek());
                            }
                            this.consumeCharacter('}');
                            statements.add(new Switch(current.getPosition(), parameter, values, caseStatements, defaultCase));
                            break block0;
                        }
                        default: {
                            throw new ParserException(current.getPosition(), "Unexpected keyword '" + keyword + "'");
                        }
                    }
                }
                default: {
                    statements.add(this.parseExpression(true));
                    expectSemicolon = true;
                }
            }
            if (expectSemicolon) {
                if (this.peek().id() != ';') break;
                ++this.position;
            }
            if (!singleStatement) continue;
            break;
        }
        switch (statements.size()) {
            case 0: {
                if (singleStatement) {
                    throw new ParserException(this.peek().getPosition(), "Statement expected.");
                }
                return new Sequence(this.peek().getPosition(), new RValue[0]);
            }
            case 1: {
                return (RValue)statements.get(0);
            }
        }
        return new Sequence(this.peek().getPosition(), statements.toArray(new RValue[statements.size()]));
    }

    private RValue parseExpression(boolean canBeEmpty) throws ParserException {
        LinkedList<Identifiable> halfProcessed = new LinkedList<Identifiable>();
        boolean expressionStart = true;
        block7: while (this.position < this.tokens.size()) {
            Token current = this.peek();
            switch (current.id()) {
                case '0': {
                    halfProcessed.add(new Constant(current.getPosition(), ((NumberToken)current).value));
                    ++this.position;
                    expressionStart = false;
                    continue block7;
                }
                case 'i': {
                    IdentifierToken identifierToken = (IdentifierToken)current;
                    ++this.position;
                    Token next = this.peek();
                    if (next.id() == '(') {
                        halfProcessed.add(this.parseFunctionCall(identifierToken));
                    } else {
                        RValue variable = this.expression.getVariable(identifierToken.value, false);
                        if (variable == null) {
                            halfProcessed.add(new UnboundVariable(identifierToken.getPosition(), identifierToken.value));
                        } else {
                            halfProcessed.add(variable);
                        }
                    }
                    expressionStart = false;
                    continue block7;
                }
                case '(': {
                    halfProcessed.add(this.parseBracket());
                    expressionStart = false;
                    continue block7;
                }
                case ')': 
                case ',': 
                case ';': 
                case '}': {
                    break block7;
                }
                case 'o': {
                    if (expressionStart) {
                        halfProcessed.add(new UnaryOperator((OperatorToken)current));
                    } else {
                        halfProcessed.add(current);
                    }
                    ++this.position;
                    expressionStart = true;
                    continue block7;
                }
                default: {
                    halfProcessed.add(current);
                    ++this.position;
                    expressionStart = false;
                    continue block7;
                }
            }
        }
        if (halfProcessed.isEmpty() && canBeEmpty) {
            return new Sequence(this.peek().getPosition(), new RValue[0]);
        }
        return ParserProcessors.processExpression(halfProcessed);
    }

    private Token peek() {
        if (this.position >= this.tokens.size()) {
            return new NullToken(this.tokens.get(this.tokens.size() - 1).getPosition() + 1);
        }
        return this.tokens.get(this.position);
    }

    private Function parseFunctionCall(IdentifierToken identifierToken) throws ParserException {
        this.consumeCharacter('(');
        try {
            if (this.peek().id() == ')') {
                ++this.position;
                return Functions.getFunction(identifierToken.getPosition(), identifierToken.value, new RValue[0]);
            }
            ArrayList<RValue> args = new ArrayList<RValue>();
            block6: while (true) {
                args.add(this.parseExpression(false));
                Token current = this.peek();
                ++this.position;
                switch (current.id()) {
                    case ',': {
                        continue block6;
                    }
                    case ')': {
                        break block6;
                    }
                    default: {
                        throw new ParserException(current.getPosition(), "Unmatched opening bracket");
                    }
                }
                break;
            }
            return Functions.getFunction(identifierToken.getPosition(), identifierToken.value, args.toArray(new RValue[args.size()]));
        }
        catch (NoSuchMethodException e) {
            throw new ParserException(identifierToken.getPosition(), "Function '" + identifierToken.value + "' not found", e);
        }
    }

    private RValue parseBracket() throws ParserException {
        this.consumeCharacter('(');
        RValue ret = this.parseExpression(false);
        this.consumeCharacter(')');
        return ret;
    }

    private boolean hasKeyword(String keyword) {
        Token next = this.peek();
        return next instanceof KeywordToken && ((KeywordToken)next).value.equals(keyword);
    }

    private void assertCharacter(char character) throws ParserException {
        Token next = this.peek();
        if (next.id() != character) {
            throw new ParserException(next.getPosition(), "Expected '" + character + "'");
        }
    }

    private void assertKeyword(String keyword) throws ParserException {
        if (!this.hasKeyword(keyword)) {
            throw new ParserException(this.peek().getPosition(), "Expected '" + keyword + "'");
        }
    }

    private void consumeCharacter(char character) throws ParserException {
        this.assertCharacter(character);
        ++this.position;
    }

    private void consumeKeyword(String keyword) throws ParserException {
        this.assertKeyword(keyword);
        ++this.position;
    }

    private final class NullToken
    extends Token {
        private NullToken(int position) {
            super(position);
        }

        @Override
        public char id() {
            return '\u0000';
        }

        public String toString() {
            return "NullToken";
        }
    }
}

