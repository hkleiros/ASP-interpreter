package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

class AspTerm extends AspSyntax {
    ArrayList<AspFactor> factors = new ArrayList<>();
    ArrayList<AspTermOpr> termOprs = new ArrayList<>();

    AspTerm(int n) {
        super(n);
    }

    static AspTerm parse(Scanner s) {
        enterParser("term");
        AspTerm at = new AspTerm(s.curLineNum());
        while (true) {
            at.factors.add(AspFactor.parse(s));
            if (!s.isTermOpr()) {
                break;
            }
            at.termOprs.add(AspTermOpr.parse(s));
        }
        leaveParser("term");
        return at;
    }

    @Override
    void prettyPrint() {
        factors.get(0).prettyPrint();
        for (int i = 1; i < factors.size(); i++) {
            termOprs.get(i - 1).prettyPrint();
            factors.get(i).prettyPrint();
        }

    }

    //Hentet fra forelesning uke 41, slide 16
    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = factors.get(0).eval(curScope);
        for (int i = 1; i < factors.size(); i++) {
            TokenKind k = termOprs.get(i - 1).term;
            switch (k) {
                case minusToken:
                    v = v.evalSubtract(factors.get(i).eval(curScope), this);
                    break;
                case plusToken:
                    v = v.evalAdd(factors.get(i).eval(curScope), this);
                    break;
                default:
                    Main.panic("Illegal term operator: " + k + " !");
            }
        }
        return v;
    }

}
