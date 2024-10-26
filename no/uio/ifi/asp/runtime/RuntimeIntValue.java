package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeIntValue extends RuntimeValue {
    long intValue;

    public RuntimeIntValue(long i) {
        intValue = i;
    }

    @Override
    public String toString() {
        return String.valueOf(intValue);
    }

    @Override
    public String showInfo() {
        return String.valueOf(intValue);
    }

    @Override
    public long getIntValue(String what, AspSyntax where) {
        return intValue;
    }

    @Override
    public double getFloatValue(String what, AspSyntax where) {
        return intValue;
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where) {
        if (intValue == 0) {
            return false;
        }
        return true;
    }

    @Override
    public String getStringValue(String what, AspSyntax where) {
        return String.valueOf(intValue);
    }

    @Override
    public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where) {
        if (v.typeName() == "float") {
            double vfl = v.getFloatValue("+", where);
            return new RuntimeFloatValue(intValue + vfl);
        } else if (v.typeName() == "int") {
            long vint = v.getIntValue("+", where);
            return new RuntimeIntValue(intValue + vint);
        }
        runtimeError("Type error for + : " + v.toString() + " is not legal !", where);
        return null;
    }

    @Override
    public RuntimeValue evalDivide(RuntimeValue v, AspSyntax where) {
        if (v.getFloatValue("/", where) == 0.0) {
            runtimeError("FEIL: ulovlig divisjon med 0 ! ", where);
        }
        if (v.typeName() == "int" || v.typeName() == "float") {
            return new RuntimeFloatValue(intValue / v.getFloatValue("/", where));
        }
        runtimeError("Type error for / : " + v.toString() + " is not legal !", where);
        return null;
    }

    @Override
    public RuntimeValue evalIntDivide(RuntimeValue v, AspSyntax where) {
        if (v.typeName() == "int") {
            if (v.getIntValue("//", where) == 0) {
                runtimeError("FEIL: ulovlig heltallsdivisjon med 0 ! ", where);
            }
            return new RuntimeIntValue(Math.floorDiv(intValue, v.getIntValue("//", where)));
        } else if (v.typeName() == "float") {
            return new RuntimeFloatValue(Math.floor(intValue / v.getFloatValue("//", where)));
        }
        runtimeError("Type error for // : " + v.toString() + " is not legal !", where);
        return null;
    }

    @Override
    public RuntimeValue evalModulo(RuntimeValue v, AspSyntax where) {
        if (v.typeName() == "int") {
            return new RuntimeIntValue(Math.floorMod(intValue, v.getIntValue("%", where)));
        } else if (v.typeName() == "float") {
            double vfl = v.getFloatValue("%", where);
            return new RuntimeFloatValue(intValue - vfl * Math.floor(intValue / vfl));
        }
        runtimeError("Type error for % : " + v.toString() + " is not legal !", where);
        return null;
    }

    @Override
    public RuntimeValue evalGreater(RuntimeValue v, AspSyntax where) {
        if (v.typeName() == "float" || v.typeName() == "int") {
            return new RuntimeBoolValue(intValue > v.getFloatValue(">", where));
        }
        runtimeError("Type error for > : " + v.toString() + " is not legal !", where);
        return null;
    }

    @Override
    public RuntimeValue evalGreaterEqual(RuntimeValue v, AspSyntax where) {
        if (v.typeName() == "float" || v.typeName() == "int") {
            return new RuntimeBoolValue(intValue >= v.getFloatValue(">=", where));
        }
        runtimeError("Type error for >= : " + v.toString() + " is not legal !", where);
        return null;
    }

    @Override
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
        if (v.typeName() == "float" || v.typeName() == "int") {
            return new RuntimeBoolValue(intValue == v.getFloatValue("==", where));
        }
        runtimeError("Type error for == : " + v.toString() + " is not legal !", where);
        return null;
    }

    @Override
    public RuntimeValue evalLess(RuntimeValue v, AspSyntax where) {
        if (v.typeName() == "float" || v.typeName() == "int") {
            return new RuntimeBoolValue(intValue < v.getFloatValue("<", where));
        }
        runtimeError("Type error for < : " + v.toString() + " is not legal !", where);
        return null;
    }

    @Override
    public RuntimeValue evalLessEqual(RuntimeValue v, AspSyntax where) {
        if (v.typeName() == "float" || v.typeName() == "int") {
            return new RuntimeBoolValue(intValue <= v.getFloatValue("<=", where));
        }
        runtimeError("Type error for <= : " + v.toString() + " is not legal !", where);
        return null;
    }

    @Override
    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
        if (v.typeName() == "float") {
            double vfl = v.getFloatValue("*", where);
            return new RuntimeFloatValue(intValue * vfl);
        } else if (v.typeName() == "int") {
            long vint = v.getIntValue("*", where);
            return new RuntimeIntValue(intValue * vint);
        }
        runtimeError("Type error for * : " + v.toString() + " is not legal !", where);
        return null;
    }

    @Override
    public RuntimeValue evalNegate(AspSyntax where) {
        return new RuntimeIntValue(-intValue);
    }

    @Override
    public RuntimeValue evalNot(AspSyntax where) {
        return new RuntimeBoolValue(!getBoolValue("not", where));
    }

    @Override
    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
        if (v.typeName() == "float" || v.typeName() == "int") {
            return new RuntimeBoolValue(intValue != v.getFloatValue("!=", where));
        }
        if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(true);
        }
        runtimeError("Type error for != : " + v.toString() + " is not legal !", where);
        return null;
    }

    @Override
    public RuntimeValue evalPositive(AspSyntax where) {
        return new RuntimeIntValue(+intValue);
    }

    @Override
    public RuntimeValue evalSubtract(RuntimeValue v, AspSyntax where) {
        if (v.typeName() == "float") {
            double vfl = v.getFloatValue("-", where);
            return new RuntimeFloatValue(intValue - vfl);
        } else if (v.typeName() == "int") {
            long vint = v.getIntValue("-", where);
            return new RuntimeIntValue(intValue - vint);
        }
        runtimeError("Type error for - : " + v.toString() + " is not legal !", where);
        return null;
    }

    @Override
    String typeName() {
        return "int";
    }

}
