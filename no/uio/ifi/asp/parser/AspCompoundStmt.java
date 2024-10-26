package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

abstract class AspCompoundStmt extends AspStmt {

    AspCompoundStmt(int n) {
        super(n);
    }

    static AspCompoundStmt parse(Scanner s) {
        enterParser("compound stmt");
        AspCompoundStmt acs = null;
        TokenKind cur = s.curToken().kind;
        if (cur == ifToken) {
            acs = AspIfStmt.parse(s);
        } else if (cur == whileToken) {
            acs = AspWhileStmt.parse(s);
        } else if (cur == forToken) {
            acs = AspForStmt.parse(s);
        } else if (cur == defToken) {
            acs = AspFuncDef.parse(s);
        } else {
            parserError("Expexted CompoundStmt but got: " + cur + " !", s.curLineNum());
        }
        leaveParser("compound stmt");
        return acs;
    }

}