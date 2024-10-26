package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

import java.util.ArrayList;

public class AspSuite extends AspSyntax {
    ArrayList<AspStmt> stmts = new ArrayList<>();
    boolean hasNewline = false;

    AspSuite(int n) {
        super(n);
    }

    static AspSuite parse(Scanner s) {
        enterParser("suite");
        AspSuite as = new AspSuite(s.curLineNum());
        if (s.curToken().kind == newLineToken) {
            as.hasNewline = true;
            skip(s, newLineToken);
            skip(s, indentToken);
            while (s.curToken().kind != dedentToken) {
                as.stmts.add(AspStmt.parse(s));
            }
            skip(s, dedentToken);
        } else {
            as.stmts.add(AspSmallStmtList.parse(s));
        }
        leaveParser("suite");
        return as;
    }

    @Override
    void prettyPrint() {
        boolean nrStmts = false;
        if (hasNewline) {
            prettyWriteLn();
            prettyIndent();
            for (AspStmt stmt : stmts) {
                if (nrStmts) {
                    prettyWriteLn();
                }
                stmt.prettyPrint();
                nrStmts = true;
            }
            prettyDedent();
        } else {
            stmts.get(0).prettyPrint();
        }
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = null;
        for (AspStmt stmt : stmts) {
            v = stmt.eval(curScope);
        }
        if (v == null) {
            return new RuntimeNoneValue();
        }
        return v;
    }

}
