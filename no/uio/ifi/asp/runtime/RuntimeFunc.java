package no.uio.ifi.asp.runtime;

import java.util.ArrayList;

import no.uio.ifi.asp.parser.AspFuncDef;
import no.uio.ifi.asp.parser.AspName;
import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeFunc extends RuntimeValue {
    AspFuncDef def; //plass i syntakstreet
    RuntimeScope defScope; //deklarasjons skop
    String funcName; //navn

    public RuntimeFunc(String name) {
        funcName = name;
    }

    public RuntimeFunc(String name, AspFuncDef def, RuntimeScope scope) {
        funcName = name;
        this.def = def;
        defScope = scope;
    }

    @Override
    String typeName() {
        return "func";
    }

    @Override
    public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
        checkNumParams(actualParams, funcName, where);
        RuntimeScope newScope = new RuntimeScope(defScope);
        ArrayList<AspName> formalParams = def.arguments;
        for (int i = 0; i < formalParams.size(); i++) {
            newScope.assign(formalParams.get(i).name, actualParams.get(i));
        }
        //Hentet fra forelesning uke 45 2021, slide 33
        try {
            def.suite.eval(newScope);
        } catch (RuntimeReturnValue e) {
            return e.value;
        }
        return new RuntimeNoneValue();
    }

    @Override
    public String showInfo() {
        return funcName;
    }

    @Override
    public String toString() {
        return funcName;
    }

    private void checkNumParams(ArrayList<RuntimeValue> actArgs,
            String id, AspSyntax where) {
        if (actArgs.size() != def.arguments.size())
            RuntimeValue.runtimeError("Wrong number of parameters to " + id + "!", where);
    }

}
