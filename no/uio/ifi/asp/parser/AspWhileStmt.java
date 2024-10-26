package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspWhileStmt extends AspCompoundStmt {
    AspExpr test;
    AspSuite body;

    AspWhileStmt(int n) {
        super(n);
    }

    static AspWhileStmt parse(Scanner s) {
        enterParser("while stmt");
        AspWhileStmt aws = new AspWhileStmt(s.curLineNum());
        skip(s, whileToken);
        aws.test = AspExpr.parse(s);
        skip(s, colonToken);
        aws.body = AspSuite.parse(s);
        leaveParser("while stmt");
        return aws;
    }

    @Override
    void prettyPrint() {
        prettyWrite("while ");
        test.prettyPrint();
        prettyWrite(" : ");
        body.prettyPrint();
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        //Hentet fra forelesning uke 44, slide 8
        while (true) {
            RuntimeValue t = test.eval(curScope);
            if (!t.getBoolValue("while loop test", this)) {
                break;
            }
            trace("while True: ...");
            body.eval(curScope);
        }
        trace("while False:");
        return new RuntimeNoneValue();
    }

}
