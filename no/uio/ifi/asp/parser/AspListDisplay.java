package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

import java.util.ArrayList;

public class AspListDisplay extends AspAtom {
    ArrayList<AspExpr> exprs = new ArrayList<>();

    AspListDisplay(int n) {
        super(n);
    }

    public static AspListDisplay parse(Scanner s) {
        enterParser("list display");
        AspListDisplay ald = new AspListDisplay(s.curLineNum());
        skip(s, leftBracketToken);
        while (s.curToken().kind != rightBracketToken) {
            ald.exprs.add(AspExpr.parse(s));
            if (s.curToken().kind != commaToken) {
                break;
            }
            skip(s, commaToken);
        }
        skip(s, rightBracketToken);
        leaveParser("list display");
        return ald;
    }

    @Override
    void prettyPrint() {
        prettyWrite("[");
        int i = 0;
        for (AspExpr expr : exprs) {
            if (i > 0) {
                prettyWrite(", ");
            }
            expr.prettyPrint();
            i++;
        }
        prettyWrite("]");
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        ArrayList<RuntimeValue> a = new ArrayList<>();
        for (AspExpr e : exprs) {
            a.add(e.eval(curScope));
        }
        return new RuntimeListValue(a);
    }

}
