package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeStringValue extends RuntimeValue {
    String stringValue;

    public RuntimeStringValue(String v) {
        stringValue = v;
    }

    @Override
    String typeName() {
        return "String";
    }

    @Override
    public String showInfo() {
        if (stringValue.indexOf("'") >= 0)
            return ('"' + stringValue + '"');
        else
            return ("'" + stringValue + "'");
    }

    @Override
    public String toString() {
        return stringValue;
    }

    @Override
    public String getStringValue(String what, AspSyntax where) {
        return stringValue;
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where) {
        if (stringValue.isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where) {
        String vVal = v.getStringValue(v.toString(), where);

        return new RuntimeStringValue(stringValue + vVal);
    }

    @Override
    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
        long times = v.getIntValue(v.toString(), where);
        String str = "";
        for (int i = 0; i < times; i++) {
            str += stringValue;
        }
        return new RuntimeStringValue(str);
    }

    //returnerer boolske verdier
    @Override
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) {
            return new RuntimeBoolValue((stringValue.equals(v.getStringValue(v.toString(), where))));
        }
        if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(stringValue.equals(""));
        }
        runtimeError("Type error for ==.", where);
        return null;
    }

    @Override
    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) {
            return new RuntimeBoolValue(!(stringValue.equals(v.toString())));
        }
        if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(!stringValue.equals(""));
        }
        runtimeError("Type error for != : " + v.toString() + " is not legal !", where);
        return null;
    }

    @Override
    public RuntimeValue evalNot(AspSyntax where) {
        return new RuntimeBoolValue(!getBoolValue(toString(), where));
    }

    @Override
    public RuntimeValue evalGreater(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) {
            return new RuntimeBoolValue(stringValue.compareTo(v.getStringValue("eval greater", where)) > 0);
        }
        runtimeError("Type error for > : " + v.toString() + " is not legal !", where);
        return null;
    }

    @Override
    public RuntimeValue evalGreaterEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) {
            return new RuntimeBoolValue(stringValue.compareTo(v.getStringValue("eval greater", where)) >= 0);
        }
        runtimeError("Type error for >= : " + v.toString() + " is not legal !", where);
        return null;
    }

    @Override
    public RuntimeValue evalLess(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) {
            return new RuntimeBoolValue(stringValue.compareTo(v.getStringValue("eval greater", where)) < 0);
        }
        runtimeError("Type error for < : " + v.toString() + " is not legal !", where);
        return null;
    }

    @Override
    public RuntimeValue evalLessEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) {
            return new RuntimeBoolValue(stringValue.compareTo(v.getStringValue("eval greater", where)) <= 0);
        }
        runtimeError("Type error for <= : " + v.toString() + " is not legal !", where);
        return null;
    }

    @Override
    public RuntimeValue evalLen(AspSyntax where) {
        return new RuntimeIntValue(stringValue.length());
    }

    @Override
    public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            if (v.getIntValue("index", where) >= stringValue.length()) {
                runtimeError(v.getIntValue("index", where) + " index out of bounds for range 0 to "
                        + stringValue.length() + " ! ", where);
            } else if (v.getIntValue("index", where) < 0) {
                runtimeError(v.getIntValue("index", where) + " index out of bounds for range 0 to "
                        + stringValue.length() + " ! ", where);
            }
            return new RuntimeStringValue("" + stringValue.charAt((int) v.getIntValue("index", where)));
        }
        runtimeError(v.toString() + " is not a valid index !", where);
        return null;
    }

    @Override
    public int hashCode() {
        return stringValue.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        RuntimeValue test = (RuntimeValue) obj;
        return toString().equals(test.toString());
    }
}
