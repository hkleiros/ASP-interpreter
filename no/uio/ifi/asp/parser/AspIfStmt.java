package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

import java.util.ArrayList;

class AspIfStmt extends AspCompoundStmt {
    ArrayList<AspExpr> test = new ArrayList<>();
    ArrayList<AspSuite> body = new ArrayList<>();
    static boolean hasElse = false;

    AspIfStmt(int n) {
        super(n);
    }

    static AspIfStmt parse(Scanner s) {
        enterParser("if stmt");
        AspIfStmt ais = new AspIfStmt(s.curLineNum());

        while (true) {
            if (s.curToken().kind != ifToken) {
                hasElse = true;
                ais.test.add(null);
            } else {
                skip(s, ifToken);
                ais.test.add(AspExpr.parse(s));
            }
            skip(s, colonToken);
            ais.body.add(AspSuite.parse(s));
            if (s.curToken().kind != elseToken)
                break;
            skip(s, elseToken);
        }
        leaveParser("if stmt");
        return ais;

    }

    @Override
    void prettyPrint() {
        for (int i = 0; i < test.size(); i++) {
            if (i == 0) {
                prettyWrite("if ");
                test.get(i).prettyPrint();
            } else if (i == test.size() - 1 && hasElse) {
                prettyWriteLn();
                prettyWrite("else ");
            } else {
                prettyWrite("else if ");
                test.get(i).prettyPrint();
            }
            prettyWrite(": ");
            body.get(i).prettyPrint();
        }
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        for (int i = 0; i < test.size(); i++) {
            if (i == test.size() - 1 && hasElse) {
                trace("else: ");
                return body.get(i).eval(curScope);
            }
            RuntimeValue e = test.get(i).eval(curScope);
            if (e.getBoolValue("if stmt test", this)) {
                trace("if True #alt #" + (i + 1) + ": ...");
                return body.get(i).eval(curScope);
            }
        }
        return new RuntimeNoneValue();
    }

}
