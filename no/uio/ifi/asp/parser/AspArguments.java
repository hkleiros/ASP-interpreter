package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

import java.util.ArrayList;

public class AspArguments extends AspPrimarySuffix {
    ArrayList<AspExpr> exprs = new ArrayList<>();

    AspArguments(int n) {
        super(n);
    }

    public static AspArguments parse(Scanner s) {
        enterParser("arguments");
        AspArguments aa = new AspArguments(s.curLineNum());
        skip(s, leftParToken);
        while (true) {
            if (s.curToken().kind == rightParToken) {
                break;
            }
            aa.exprs.add(AspExpr.parse(s));
            if (s.curToken().kind != commaToken) {
                break;
            }
            skip(s, commaToken);
        }
        skip(s, rightParToken);
        leaveParser("arguments");
        return aa;
    }

    @Override
    void prettyPrint() {
        prettyWrite("(");
        boolean i = false;
        for (AspExpr expr : exprs) {
            if (i) {
                prettyWrite(", ");
            }
            expr.prettyPrint();
            i = true;
        }
        prettyWrite(")");
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        ArrayList<RuntimeValue> params = new ArrayList<>();
        RuntimeValue v = null;
        for (AspExpr expr : exprs) {
            v = expr.eval(curScope);
            params.add(v);
        }
        return new RuntimeListValue(params);
    }

}
