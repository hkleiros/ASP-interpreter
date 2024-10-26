package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

import java.util.ArrayList;

class AspComparison extends AspSyntax {
    ArrayList<AspTerm> terms = new ArrayList<>();
    ArrayList<AspCompOpr> compOprs = new ArrayList<>();

    AspComparison(int n) {
        super(n);
    }

    static AspComparison parse(Scanner s) {
        enterParser("comparison");
        AspComparison ac = new AspComparison(s.curLineNum());
        while (true) {
            ac.terms.add(AspTerm.parse(s));
            if (!s.isCompOpr()) {
                break;
            }
            ac.compOprs.add(AspCompOpr.parse(s));
        }
        leaveParser("comparison");
        return ac;
    }

    @Override
    void prettyPrint() {
        terms.get(0).prettyPrint();

        for (int i = 1; i < terms.size(); i++) {
            compOprs.get(i - 1).prettyPrint();
            terms.get(i).prettyPrint();
        }
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = terms.get(0).eval(curScope);
        RuntimeValue bool = v;
        for (int i = 1; i < terms.size(); i++) {
            RuntimeValue nv = terms.get(i).eval(curScope);
            TokenKind tk = compOprs.get(i - 1).opr;
            switch (tk) {
                case doubleEqualToken:
                    bool = v.evalEqual(nv, this);
                    break;
                case notEqualToken:
                    bool = v.evalNotEqual(nv, this);
                    break;
                case greaterToken:
                    bool = v.evalGreater(nv, this);
                    break;
                case greaterEqualToken:
                    bool = v.evalGreaterEqual(nv, this);
                    break;
                case lessToken:
                    bool = v.evalLess(nv, this);
                    break;
                case lessEqualToken:
                    bool = v.evalLessEqual(nv, this);
                    break;
                default:
                    RuntimeValue.runtimeError(tk + " is not a valid AspCompOpr !", this);
            }
            if (!bool.getBoolValue("comparison operand", this)) {
                return bool;
            }
            v = nv;
        }
        return bool;
    }

}
