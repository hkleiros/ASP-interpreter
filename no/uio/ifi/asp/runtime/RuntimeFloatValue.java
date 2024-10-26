package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeFloatValue extends RuntimeValue {
    double floatValue;

    public RuntimeFloatValue(double v) {
        floatValue = v;
    }

    @Override
    public String toString() {
        return String.valueOf(floatValue);
    }

    @Override
    public String showInfo() {
        return String.valueOf(floatValue);
    }

    @Override
    public double getFloatValue(String what, AspSyntax where) {
        return floatValue;
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where) {
        if (floatValue == 0) {
            return false;
        }
        return true;
    }

    @Override
    public RuntimeValue evalNot(AspSyntax where) {
        return new RuntimeBoolValue(!getBoolValue("floatvalue", where));
    }

    @Override
    public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where) {
        if (v.typeName() == "float" || v.typeName() == "int") {
            return new RuntimeFloatValue(floatValue + v.getFloatValue(v.toString(), where));
        }
        runtimeError("Type error for + : " + v.toString() + " is not legal !", where);
        return null;
    }

    @Override
    public RuntimeValue evalDivide(RuntimeValue v, AspSyntax where) {
        if (v.typeName() == "float" || v.typeName() == "int") {
            if (v.getFloatValue("floatValue", where) == 0.0) {
                runtimeError("FEIL: ulovlig divisjon med 0 ! ", where);
            }
            return new RuntimeFloatValue(floatValue / v.getFloatValue("floatValue", where));
        }
        runtimeError("Type error for / : " + v.toString() + " is not legal !", where);
        return null;
    }

    @Override
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
        if (v.typeName() == "float" || v.typeName() == "int") {
            return new RuntimeBoolValue(floatValue == v.getFloatValue("floatValue", where));
        }
        if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(floatValue == 0.0);
        }
        runtimeError("Type error for == : " + v.toString() + " is not legal !", where);
        return null;
    }

    @Override
    public RuntimeValue evalGreater(RuntimeValue v, AspSyntax where) {
        if (v.typeName() == "float" || v.typeName() == "int") {
            return new RuntimeBoolValue(floatValue > v.getFloatValue("floatValue", where));
        }
        runtimeError("Type error for > : " + v.toString() + " is not legal !", where);
        return null;
    }

    @Override
    public RuntimeValue evalGreaterEqual(RuntimeValue v, AspSyntax where) {
        if (v.typeName() == "float" || v.typeName() == "int") {
            return new RuntimeBoolValue(floatValue >= v.getFloatValue("floatValue", where));
        }
        runtimeError("Type error for >= : " + v.toString() + " is not legal !", where);
        return null;
    }

    @Override
    public RuntimeValue evalIntDivide(RuntimeValue v, AspSyntax where) {
        if (v.typeName() == "float" || v.typeName() == "int") {
            if (v.getFloatValue("floatValue", where) == 0.0) {
                runtimeError("FEIL: ulovlig heltallsdivisjon med 0 ! ", where);
            }
            return new RuntimeFloatValue(Math.floor(floatValue / v.getFloatValue("floatValue", where)));
        }
        runtimeError("Type error for // : " + v.toString() + " is not legal !", where);
        return null;
    }

    @Override
    public RuntimeValue evalLess(RuntimeValue v, AspSyntax where) {
        if (v.typeName() == "float" || v.typeName() == "int") {
            return new RuntimeBoolValue(floatValue < v.getFloatValue("floatValue", where));
        }
        runtimeError("Type error for < : " + v.toString() + " is not legal !", where);
        return null;
    }

    @Override
    public RuntimeValue evalLessEqual(RuntimeValue v, AspSyntax where) {
        if (v.typeName() == "float" || v.typeName() == "int") {
            return new RuntimeBoolValue(floatValue <= v.getFloatValue("floatValue", where));
        }
        runtimeError("Type error for <= : " + v.toString() + " is not legal !", where);
        return null;
    }

    @Override
    public RuntimeValue evalModulo(RuntimeValue v, AspSyntax where) {
        if (v.typeName() == "int" || v.typeName() == "float") {
            return new RuntimeFloatValue(floatValue
                    - v.getFloatValue("intvalue", where) * Math.floor(floatValue / v.getFloatValue("intvalue", where)));
        }
        runtimeError("Type error for % : " + v.toString() + " is not legal !", where);
        return null;
    }

    @Override
    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
        if (v.typeName() == "float" || v.typeName() == "int") {
            return new RuntimeFloatValue(floatValue * v.getFloatValue("floatValue", where));
        }
        runtimeError("Type error for * : " + v.toString() + " is not legal !", where);
        return null;
    }

    @Override
    public RuntimeValue evalNegate(AspSyntax where) {
        return new RuntimeFloatValue(-floatValue);
    }

    @Override
    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
        if (v.typeName() == "float" || v.typeName() == "int") {
            return new RuntimeBoolValue(floatValue != v.getFloatValue("floatValue", where));
        }
        if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(floatValue != 0.0);
        }
        runtimeError("Type error for != : " + v.toString() + " is not legal !", where);
        return null;
    }

    @Override
    public RuntimeValue evalPositive(AspSyntax where) {
        return new RuntimeFloatValue(+floatValue);
    }

    @Override
    public RuntimeValue evalSubtract(RuntimeValue v, AspSyntax where) {
        if (v.typeName() == "float" || v.typeName() == "int") {
            return new RuntimeFloatValue(floatValue - v.getFloatValue("float", where));
        }
        runtimeError("Type error for - : " + v.toString() + " is not legal !", where);
        return null;
    }

    @Override
    public String getStringValue(String what, AspSyntax where) {
        return String.valueOf(floatValue);
    }

    @Override
    String typeName() {
        return "float";
    }

}
