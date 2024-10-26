package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

abstract class AspPrimarySuffix extends AspSyntax {

    AspPrimarySuffix(int n) {
        super(n);
    }

    public static AspPrimarySuffix parse(Scanner s) {
        enterParser("primary suffix");
        AspPrimarySuffix aps = null;
        if (s.curToken().kind == leftBracketToken) {
            aps = AspSubscription.parse(s);
        } else if (s.curToken().kind == leftParToken) {
            aps = AspArguments.parse(s);
        } else {
            parserError("Expexted a '(' or '[', but got a: " + s.curToken().kind + " !", s.curLineNum());
        }
        leaveParser("primary suffix");
        return aps;
    }

}