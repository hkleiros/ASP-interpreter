package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

abstract class AspSmallStmt extends AspExpr {

    AspSmallStmt(int n) {
        super(n);
    }

    //Hentet fra forelesning uke 21.09.2022 
    public static AspSmallStmt parse(Scanner s) {
        enterParser("small stmt");
        AspSmallStmt as = null;
        TokenKind cur = s.curToken().kind;
        if (cur == globalToken) {
            as = AspGlobalStmt.parse(s);
        } else if (cur == passToken) {
            as = AspPassStmt.parse(s);
        } else if (cur == returnToken) {
            as = AspReturnStmt.parse(s);
        } else if (cur == nameToken && s.anyEqualToken()) {
            as = AspAssignment.parse(s);
        } else {
            as = AspExprStmt.parse(s);
        }
        leaveParser("small stmt");
        return as;
    }

}