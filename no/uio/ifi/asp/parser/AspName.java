package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspName extends AspAtom {
    public String name;

    AspName(int n) {
        super(n);
    }

    public static AspName parse(Scanner s) {
        enterParser("name");
        AspName an = new AspName(s.curLineNum());
        an.name = s.curToken().name;
        Token tk = s.curToken();
        if (tk.kind != nameToken) {
            parserError("Expected a name token, but got a: " + tk.kind + " !", s.curLineNum());
        }
        skip(s, nameToken);
        leaveParser("name");
        return an;
    }

    @Override
    void prettyPrint() {
        prettyWrite(name);
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        //Hentet fra forelesning uke 44, slide 23
        return curScope.find(name, this);
    }

}
