package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

class AspBooleanLiteral extends AspAtom {
    boolean value;

    AspBooleanLiteral(int n) {
        super(n);
    }

    public static AspBooleanLiteral parse(Scanner s) {
        enterParser("boolean literal");
        AspBooleanLiteral abl = new AspBooleanLiteral(s.curLineNum());
        abl.value = false;
        test(s, trueToken, falseToken);
        if (s.curToken().kind == trueToken) {
            abl.value = true;
            skip(s, trueToken);
        } else {
            skip(s, falseToken);
        }
        leaveParser("boolean literal");
        return abl;
    }

    @Override
    void prettyPrint() {
        if (value) {
            prettyWrite("True");
        } else {
            prettyWrite("False");
        }
    }

    // Fra forelesning uke 41, slide 10
    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return new RuntimeBoolValue(value);
    }

}
