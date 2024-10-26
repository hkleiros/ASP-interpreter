package no.uio.ifi.asp.runtime;

import java.util.HashMap;

import no.uio.ifi.asp.parser.*;

public class RuntimeDictValue extends RuntimeValue {
    HashMap<RuntimeValue, RuntimeValue> dictValue;

    public RuntimeDictValue(HashMap<RuntimeValue, RuntimeValue> hm) {
        dictValue = hm;
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where) {
        if (dictValue.isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public RuntimeValue evalLen(AspSyntax where) {
        return new RuntimeIntValue(dictValue.size());
    }

    @Override
    public String toString() {
        String str = "{";
        int i = 0;
        for (RuntimeValue a : dictValue.keySet()) {
            if (i > 0) {
                str += ", ";
            }
            str += a.showInfo() + ": " + dictValue.get(a).showInfo();
            i++;
        }
        str += "}";
        return str;
    }

    @Override
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(dictValue == null);
        }
        runtimeError("Type error for ==: " + v.getStringValue("error", where) + " is not legal !", where);
        return null;
    }

    @Override
    public RuntimeValue evalNot(AspSyntax where) {
        return new RuntimeBoolValue(!getBoolValue("dictValue", where));
    }

    @Override
    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(dictValue != null);
        }
        runtimeError("Type error for != : " + v.getStringValue("error", where) + " is not legal !", where);
        return null;
    }

    @Override
    public String showInfo() {
        return toString();
    }

    @Override
    public void evalAssignElem(RuntimeValue inx, RuntimeValue val, AspSyntax where) {
        if (inx instanceof RuntimeStringValue) {
            dictValue.put(inx, val);
        } else {
            runtimeError(inx.showInfo() + " is not a valid Dict Key!", where);
        }

    }

    @Override
    String typeName() {
        return "dict";
    }

    @Override
    public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where) {
        RuntimeValue item = dictValue.get(v);
        if (item == null) {
            runtimeError("Dictionary key: '" + v.toString() + "' is undefined !", where);
        }
        return item;
    }

}
