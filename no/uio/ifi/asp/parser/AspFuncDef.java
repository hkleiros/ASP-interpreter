package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

import java.util.ArrayList;

public class AspFuncDef extends AspCompoundStmt {
    public AspName name;
    public ArrayList<AspName> arguments = new ArrayList<>();
    public AspSuite suite;

    AspFuncDef(int n) {
        super(n);
    }

    static AspFuncDef parse(Scanner s) {
        enterParser("func def");
        AspFuncDef afd = new AspFuncDef(s.curLineNum());
        skip(s, defToken);
        afd.name = AspName.parse(s);
        skip(s, leftParToken);
        while (s.curToken().kind != rightParToken) {
            afd.arguments.add(AspName.parse(s));
            if (s.curToken().kind != commaToken) {
                break;
            }
            skip(s, commaToken);
        }
        skip(s, rightParToken);
        skip(s, colonToken);
        afd.suite = AspSuite.parse(s);
        leaveParser("func def");
        return afd;
    }

    @Override
    void prettyPrint() {
        prettyWrite("def ");
        name.prettyPrint();
        prettyWrite(" (");
        int i = 0;
        for (AspName argument : arguments) {
            if (i > 0) {
                prettyWrite(", ");
            }
            argument.prettyPrint();
            i++;
        }
        prettyWrite("): ");
        suite.prettyPrint();
        prettyWriteLn();
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = new RuntimeFunc(name.name, this, curScope);
        curScope.assign(name.name, v);
        trace("def " + name.name);
        return v;
    }
}
