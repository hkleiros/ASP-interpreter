package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

import java.util.ArrayList;

public class AspGlobalStmt extends AspSmallStmt {
    ArrayList<AspName> names = new ArrayList<>();

    AspGlobalStmt(int n) {
        super(n);
    }

    public static AspGlobalStmt parse(Scanner s) {
        enterParser("global stmt");
        AspGlobalStmt ags = new AspGlobalStmt(s.curLineNum());
        skip(s, globalToken);
        while (true) {
            ags.names.add(AspName.parse(s));
            if (s.curToken().kind != commaToken) {
                break;
            }
            skip(s, commaToken);
        }
        leaveParser("global stmt");
        return ags;
    }

    @Override
    public void prettyPrint() {
        prettyWrite("global ");
        int i = 0;
        for (AspName name : names) {
            if (i > 0) {
                prettyWrite(", ");
            }
            name.prettyPrint();
        }
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = null;
        for (AspName n : names) {
            if (!Main.globalScope.hasDefined(n.name)) {
                Main.globalScope.assign(n.name, null);
            }
            curScope.registerGlobalName(n.name);
        }
        return v;
    }

}
