package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

import java.util.ArrayList;
import java.util.HashMap;

public class AspDictDisplay extends AspAtom {
    ArrayList<AspStringLiteral> keys = new ArrayList<>();
    ArrayList<AspExpr> items = new ArrayList<>();

    AspDictDisplay(int n) {
        super(n);
    }

    public static AspDictDisplay parse(Scanner s) {
        enterParser("dict display");
        AspDictDisplay add = new AspDictDisplay(s.curLineNum());
        skip(s, leftBraceToken);
        while (s.curToken().kind != rightBraceToken) {
            add.keys.add(AspStringLiteral.parse(s));
            skip(s, colonToken);
            add.items.add(AspExpr.parse(s));
            if (s.curToken().kind != commaToken) {
                break;
            }
            skip(s, commaToken);
        }
        skip(s, rightBraceToken);
        leaveParser("dict display");
        return add;
    }

    @Override
    void prettyPrint() {
        prettyWrite("{");
        for (int i = 0; i < keys.size(); i++) {
            if (i > 0) {
                prettyWrite(", ");
            }
            keys.get(i).prettyPrint();
            prettyWrite(": ");
            items.get(i).prettyPrint();

        }
        prettyWrite("}");
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        HashMap<RuntimeValue, RuntimeValue> dictValue = new HashMap<>();
        for (int i = 0; i < keys.size(); i++) {
            RuntimeValue s = keys.get(i).eval(curScope);
            RuntimeValue v = items.get(i).eval(curScope);
            dictValue.put(s, v);
        }
        return new RuntimeDictValue(dictValue);
    }

}
