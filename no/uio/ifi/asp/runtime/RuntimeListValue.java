package no.uio.ifi.asp.runtime;

import java.util.ArrayList;

import no.uio.ifi.asp.parser.AspExpr;
import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeListValue extends RuntimeValue {
    public ArrayList<RuntimeValue> listValue;

    public RuntimeListValue(ArrayList<RuntimeValue> exprs) {
        this.listValue = exprs;
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where) {
        if (listValue.isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(listValue == null);
        }
        runtimeError("Type error for == : '" + v.toString() + "' is not a legal type !", where);
        return null;
    }

    @Override
    public RuntimeValue evalLen(AspSyntax where) {
        return new RuntimeIntValue(listValue.size());
    }

    @Override
    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
        ArrayList<RuntimeValue> l = new ArrayList<>();
        for (int i = 0; i < v.getIntValue("listValue", where); i++) {
            l.addAll(listValue); // burde dette lage nye RuntimeValue objekter?
        }
        return new RuntimeListValue(l);
    }

    @Override
    public RuntimeValue evalNot(AspSyntax where) {
        return new RuntimeBoolValue(!getBoolValue("listValue", where));
    }

    @Override
    public String toString() {
        String s = "[";
        for (int i = 0; i < listValue.size(); i++) {
            if (i > 0) {
                s += ", ";
            }
            s += listValue.get(i).showInfo();
        }
        s += "]";
        return s;
    }

    @Override
    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(listValue != null);
        }
        runtimeError("Type error for != : '" + v.toString() + "' is not a legal type !", where);
        return null;
    }

    @Override
    public String showInfo() {
        return toString();
    }

    @Override
    public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            if (v.getIntValue("index", where) >= listValue.size()) {
                runtimeError(v.getIntValue("index", where) + " er en for høy index! ", where);
            } else if (v.getIntValue("index", where) < 0) {
                runtimeError(v.getIntValue("index", where) + " er en for lav index! ", where);
            }
            return listValue.get((int) v.getIntValue("list subscription", where));
        }
        runtimeError(v.getStringValue("index", where) + " er en ugyldig idex! ", where);
        return null;
    }

    @Override
    public void evalAssignElem(RuntimeValue inx, RuntimeValue val, AspSyntax where) {
        if (inx instanceof RuntimeIntValue) {
            int i = (int) inx.getIntValue("index", where);
            if (i >= listValue.size() || i < 0) {
                runtimeError("List index " + i + " out of range !", where);
            }
            listValue.set(i, val);
        }
    }

    @Override
    String typeName() {
        return "list";
    }

}
