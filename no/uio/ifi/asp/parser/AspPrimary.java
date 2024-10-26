package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspPrimary extends AspSyntax {
    AspAtom atom;
    ArrayList<AspPrimarySuffix> suffixes = new ArrayList<>();

    AspPrimary(int n) {
        super(n);
    }

    public static AspPrimary parse(Scanner s) {
        enterParser("primary");
        AspPrimary ap = new AspPrimary(s.curLineNum());
        ap.atom = AspAtom.parse(s);
        while (true) {
            if (s.curToken().kind != leftParToken && s.curToken().kind != leftBracketToken) {
                break;
            }
            AspPrimarySuffix aps = AspPrimarySuffix.parse(s);
            ap.suffixes.add(aps);
        }
        leaveParser("primary");
        return ap;
    }

    @Override
    void prettyPrint() {
        atom.prettyPrint();
        for (AspPrimarySuffix suffix : suffixes) {
            suffix.prettyPrint();
        }
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = atom.eval(curScope);
        for (AspPrimarySuffix aps : suffixes) {
            if (aps instanceof AspSubscription) {
                v = v.evalSubscription(aps.eval(curScope), this);
            }

            if (aps instanceof AspArguments) {
                RuntimeListValue a = (RuntimeListValue) aps.eval(curScope);
                RuntimeValue func = curScope.find(v.showInfo(), this);
                trace("Called function " + v.showInfo() + " with parameters " + a.showInfo());
                v = func.evalFuncCall(a.listValue, this);
            }
        }
        return v;
    }

}
