package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspPassStmt extends AspSmallStmt {
    Token pass;

    AspPassStmt(int n) {
        super(n);
    }

    public static AspPassStmt parse(Scanner s) {
        enterParser("pass stmt");
        AspPassStmt aps = new AspPassStmt(s.curLineNum());
        aps.pass = s.curToken();
        test(s, passToken);
        skip(s, aps.pass.kind);
        leaveParser("pass stmt");
        return aps;
    }

    @Override
    public void prettyPrint() {
        prettyWrite(pass.toString());
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        // Hentet fra forelesning uke 44 2022, slide 4
        trace("pass");
        return null;
    }

}
