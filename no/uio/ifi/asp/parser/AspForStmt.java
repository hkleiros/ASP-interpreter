package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspForStmt extends AspCompoundStmt {
    AspName name;
    AspExpr list;
    AspSuite body;

    AspForStmt(int n) {
        super(n);
    }

    static AspForStmt parse(Scanner s) {
        enterParser("for stmt");
        AspForStmt afs = new AspForStmt(s.curLineNum());
        skip(s, forToken);
        afs.name = AspName.parse(s);
        skip(s, inToken);
        afs.list = AspExpr.parse(s);
        skip(s, colonToken);
        afs.body = AspSuite.parse(s);
        leaveParser("for stmt");
        return afs;
    }

    @Override
    void prettyPrint() {
        prettyWrite("for ");
        name.prettyPrint();
        prettyWrite(" in ");
        list.prettyPrint();
        prettyWrite(": ");
        body.prettyPrint();
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        int nr = 1;
        RuntimeValue listE = list.eval(curScope);
        if (listE instanceof RuntimeListValue) {
            RuntimeListValue listV = (RuntimeListValue) listE;
            for (int n = 0; n < listV.listValue.size(); n++) {
                RuntimeValue listElement = listV.evalSubscription(new RuntimeIntValue((long) n), this);
                curScope.assign(name.name, listElement);
                trace("for  #" + nr + ": " + name.name + " = " + listElement.showInfo());
                body.eval(curScope);
                nr++;
            }
        } else {
            parserError("For loop range '" + listE.showInfo() + "' is not a list !", lineNum);
        }
        return null;
    }

}
