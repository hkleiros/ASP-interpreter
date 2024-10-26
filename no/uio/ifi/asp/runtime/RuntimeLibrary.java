// © 2021 Dag Langmyhr, Institutt for informatikk, Universitetet i Oslo

package no.uio.ifi.asp.runtime;

import java.util.ArrayList;
import java.util.Scanner;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeLibrary extends RuntimeScope {
    private Scanner keyboard = new Scanner(System.in);

    public RuntimeLibrary() {
        assign("input", new RuntimeFunc("input") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
                for (RuntimeValue v : actualParams) {
                    System.out.print(v.toString() + " ");
                }
                System.out.println();
                String s = keyboard.nextLine();
                return new RuntimeStringValue(s);
            }
        });

        // Hentet fra kompendiet s. 59
        assign("len", new RuntimeFunc("len") {
            @Override
            public RuntimeValue evalFuncCall(
                    ArrayList<RuntimeValue> actualParams,
                    AspSyntax where) {
                checkNumParams(actualParams, 1, "len", where);
                return actualParams.get(0).evalLen(where);
            }
        });

        // Hentet fra forelesning uke 45 2021, slide 40
        assign("print", new RuntimeFunc("print") {
            @Override
            public RuntimeValue evalFuncCall(
                    ArrayList<RuntimeValue> actualParams, AspSyntax where) {
                boolean i = false;
                for (RuntimeValue v : actualParams) {
                    if (i) {
                        System.out.print(" ");
                    }
                    System.out.print(v.toString());
                    i = true;
                }
                System.out.println();
                return new RuntimeNoneValue();
            }
        });

        assign("range", new RuntimeFunc("range") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
                checkNumParams(actualParams, 2, "range", where);
                long v1 = actualParams.get(0).getIntValue("range", where);
                long v2 = actualParams.get(1).getIntValue("range", where);
                ArrayList<RuntimeValue> range = new ArrayList<>();
                if (v1 < v2) {
                    while (v1 < v2) {
                        range.add(new RuntimeIntValue(v1));
                        v1++;
                    }
                }
                /*
                Range nedover: else if (v2 < v1) {  
                    while (v1 > v2) {
                        range.add(new RuntimeIntValue(v1));
                        v1--;
                    }
                } */
                return new RuntimeListValue(range);
            }
        });

        assign("int", new RuntimeFunc("int") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
                checkNumParams(actualParams, 1, "int", where);
                return new RuntimeFloatValue(actualParams.get(0).getFloatValue("int", where));
            }
        });

        assign("float", new RuntimeFunc("float") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
                checkNumParams(actualParams, 1, "float", where);
                return new RuntimeFloatValue(actualParams.get(0).getFloatValue("funcName", where));
            }
        });

        assign("str", new RuntimeFunc("str") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
                checkNumParams(actualParams, 1, "str", where);
                return new RuntimeStringValue(actualParams.get(0).toString());
            }
        });

    }

    private void checkNumParams(ArrayList<RuntimeValue> actArgs,
            int nCorrect, String id, AspSyntax where) {
        if (actArgs.size() != nCorrect)
            RuntimeValue.runtimeError("Wrong number of parameters to " + id + "!", where);
    }

}
