package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

import java.util.ArrayList;

public class AspAssignment extends AspSmallStmt {
    AspName name;
    ArrayList<AspSubscription> subscriptions = new ArrayList<>();
    AspExpr expr;

    AspAssignment(int n) {
        super(n);
    }

    public static AspAssignment parse(Scanner s) {
        enterParser("assignment");
        AspAssignment aa = new AspAssignment(s.curLineNum());
        aa.name = AspName.parse(s);
        while (true) {
            if (s.curToken().kind == equalToken) {
                break;
            }
            aa.subscriptions.add(AspSubscription.parse(s));
        }
        skip(s, equalToken);
        aa.expr = AspExpr.parse(s);
        leaveParser("assignment");
        return aa;
    }

    @Override
    public void prettyPrint() {
        name.prettyPrint();
        for (AspSubscription sub : subscriptions) {
            sub.prettyPrint();
        }
        prettyWrite(" = ");
        expr.prettyPrint();
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        if (subscriptions.isEmpty()) {
            if (curScope.hasGlobalName(name.name)) {
                RuntimeValue v = expr.eval(curScope);
                Main.globalScope.assign(name.name, v);
                trace(name.name + " = " + v.showInfo());
            } else {
                RuntimeValue v = expr.eval(curScope);
                trace(name.name + " = " + v.showInfo());
                curScope.assign(name.name, v);
            }
        } else {
            RuntimeValue a = name.eval(curScope);
            String subs = "";
            int n = 0;
            for (int i = 0; i < subscriptions.size() - 1; i++) {
                n++;
                RuntimeValue s = subscriptions.get(i).eval(curScope);
                subs += "[" + s.showInfo() + "]";
                a = a.evalSubscription(s, this);
            }
            RuntimeValue i = subscriptions.get(n).eval(curScope);
            RuntimeValue v = expr.eval(curScope);
            a.evalAssignElem(i, v, this);
            curScope.assign(a.showInfo(), v);

            trace(name.name + subs + "[" + i.showInfo() + "] = " + v.showInfo());
        }

        return curScope.find(name.name, this);
    }

}
