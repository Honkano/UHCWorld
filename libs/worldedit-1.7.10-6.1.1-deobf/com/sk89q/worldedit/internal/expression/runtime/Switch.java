/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.internal.expression.runtime;

import com.sk89q.worldedit.internal.expression.Expression;
import com.sk89q.worldedit.internal.expression.parser.ParserException;
import com.sk89q.worldedit.internal.expression.runtime.Break;
import com.sk89q.worldedit.internal.expression.runtime.BreakException;
import com.sk89q.worldedit.internal.expression.runtime.Constant;
import com.sk89q.worldedit.internal.expression.runtime.EvaluationException;
import com.sk89q.worldedit.internal.expression.runtime.Node;
import com.sk89q.worldedit.internal.expression.runtime.RValue;
import com.sk89q.worldedit.internal.expression.runtime.Sequence;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Switch
extends Node
implements RValue {
    private RValue parameter;
    private final Map<Double, Integer> valueMap;
    private final RValue[] caseStatements;
    private RValue defaultCase;

    public Switch(int position, RValue parameter, List<Double> values, List<RValue> caseStatements, RValue defaultCase) {
        this(position, parameter, Switch.invertList(values), caseStatements, defaultCase);
    }

    private static Map<Double, Integer> invertList(List<Double> values) {
        HashMap<Double, Integer> valueMap = new HashMap<Double, Integer>();
        for (int i = 0; i < values.size(); ++i) {
            valueMap.put(values.get(i), i);
        }
        return valueMap;
    }

    private Switch(int position, RValue parameter, Map<Double, Integer> valueMap, List<RValue> caseStatements, RValue defaultCase) {
        super(position);
        this.parameter = parameter;
        this.valueMap = valueMap;
        this.caseStatements = caseStatements.toArray(new RValue[caseStatements.size()]);
        this.defaultCase = defaultCase;
    }

    @Override
    public char id() {
        return 'W';
    }

    @Override
    public double getValue() throws EvaluationException {
        double parameter = this.parameter.getValue();
        try {
            double ret = 0.0;
            Integer index = this.valueMap.get(parameter);
            if (index != null) {
                for (int i = index.intValue(); i < this.caseStatements.length; ++i) {
                    ret = this.caseStatements[i].getValue();
                }
            }
            return this.defaultCase == null ? ret : this.defaultCase.getValue();
        }
        catch (BreakException e) {
            if (e.doContinue) {
                throw e;
            }
            return 0.0;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("switch (");
        sb.append(this.parameter);
        sb.append(") { ");
        for (int i = 0; i < this.caseStatements.length; ++i) {
            RValue caseStatement = this.caseStatements[i];
            sb.append("case ");
            for (Map.Entry<Double, Integer> entry : this.valueMap.entrySet()) {
                if (entry.getValue() != i) continue;
                sb.append(entry.getKey());
                break;
            }
            sb.append(": ");
            sb.append(caseStatement);
            sb.append(' ');
        }
        if (this.defaultCase != null) {
            sb.append("default: ");
            sb.append(this.defaultCase);
            sb.append(' ');
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public RValue optimize() throws EvaluationException {
        RValue optimizedParameter = this.parameter.optimize();
        ArrayList<RValue> newSequence = new ArrayList<RValue>();
        if (optimizedParameter instanceof Constant) {
            double parameter = optimizedParameter.getValue();
            Integer index = this.valueMap.get(parameter);
            if (index == null) {
                return this.defaultCase == null ? new Constant(this.getPosition(), 0.0) : this.defaultCase.optimize();
            }
            boolean breakDetected = false;
            block0: for (int i = index.intValue(); i < this.caseStatements.length && !breakDetected; ++i) {
                RValue invokable = this.caseStatements[i].optimize();
                if (invokable instanceof Sequence) {
                    for (RValue subInvokable : ((Sequence)invokable).sequence) {
                        if (subInvokable instanceof Break) {
                            breakDetected = true;
                            continue block0;
                        }
                        newSequence.add(subInvokable);
                    }
                    continue;
                }
                newSequence.add(invokable);
            }
            if (this.defaultCase != null && !breakDetected) {
                RValue invokable = this.defaultCase.optimize();
                if (invokable instanceof Sequence) {
                    Collections.addAll(newSequence, ((Sequence)invokable).sequence);
                } else {
                    newSequence.add(invokable);
                }
            }
            return new Switch(this.getPosition(), optimizedParameter, Collections.singletonMap(parameter, 0), newSequence, null);
        }
        HashMap<Double, Integer> newValueMap = new HashMap<Double, Integer>();
        HashMap<Integer, Double> backMap = new HashMap<Integer, Double>();
        for (Map.Entry<Double, Integer> entry : this.valueMap.entrySet()) {
            backMap.put(entry.getValue(), entry.getKey());
        }
        for (int i = 0; i < this.caseStatements.length; ++i) {
            RValue invokable = this.caseStatements[i].optimize();
            Double caseValue = (Double)backMap.get(i);
            if (caseValue != null) {
                newValueMap.put(caseValue, newSequence.size());
            }
            if (invokable instanceof Sequence) {
                Collections.addAll(newSequence, ((Sequence)invokable).sequence);
                continue;
            }
            newSequence.add(invokable);
        }
        return new Switch(this.getPosition(), optimizedParameter, newValueMap, newSequence, this.defaultCase.optimize());
    }

    @Override
    public RValue bindVariables(Expression expression, boolean preferLValue) throws ParserException {
        this.parameter = this.parameter.bindVariables(expression, false);
        for (int i = 0; i < this.caseStatements.length; ++i) {
            this.caseStatements[i] = this.caseStatements[i].bindVariables(expression, false);
        }
        this.defaultCase = this.defaultCase.bindVariables(expression, false);
        return this;
    }
}

