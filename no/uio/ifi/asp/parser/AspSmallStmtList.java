package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

import java.util.ArrayList;

public class AspSmallStmtList extends AspStmt {
    ArrayList<AspSmallStmt> stmts = new ArrayList<>();

    AspSmallStmtList(int n) {
        super(n);
    }

    public static AspSmallStmtList parse(Scanner s) {
        enterParser("small stmt list");
        AspSmallStmtList assl = new AspSmallStmtList(s.curLineNum());
        while (true) {
            assl.stmts.add(AspSmallStmt.parse(s));

            test(s, newLineToken, semicolonToken);
            if (s.curToken().kind == newLineToken) {
                break;
            }
            if ((s.curToken().kind == semicolonToken)) {
                skip(s, semicolonToken);
            }
        }
        if (s.curToken().kind == semicolonToken) {
            skip(s, semicolonToken);
        }
        skip(s, newLineToken);
        leaveParser("small stmt list");
        return assl;
    }

    @Override
    void prettyPrint() {
        int nrPrinted = 0;
        for (AspSmallStmt stmt : stmts) {
            if (nrPrinted > 0) {
                prettyWrite("; ");
            }
            stmt.prettyPrint();
            nrPrinted++;
        }
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = null;
        for (AspSmallStmt ass : stmts) {
            v = ass.eval(curScope);
        }
        return v;
    }

}
