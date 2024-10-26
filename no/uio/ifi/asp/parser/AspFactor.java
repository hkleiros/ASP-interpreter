package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspFactor extends AspSyntax {
    ArrayList<AspFactorPrefix> prefixes = new ArrayList<>();
    ArrayList<AspPrimary> primaries = new ArrayList<>();
    ArrayList<AspFactorOpr> factorOprs = new ArrayList<>();

    AspFactor(int n) {
        super(n);
    }

    public static AspFactor parse(Scanner s) {
        enterParser("factor");
        AspFactor af = new AspFactor(s.curLineNum());
        while (true) {
            if (s.isFactorPrefix()) {
                af.prefixes.add(AspFactorPrefix.parse(s));
            } else {
                af.prefixes.add(null);
            }
            af.primaries.add(AspPrimary.parse(s));
            if (!s.isFactorOpr()) {
                break;
            }
            af.factorOprs.add(AspFactorOpr.parse(s));
        }
        leaveParser("factor");
        return af;
    }

    @Override
    void prettyPrint() {

        for (int i = 0; i < primaries.size(); i++) {
            AspFactorPrefix prefix = prefixes.get(i);
            if (prefix != null) {
                prefix.prettyPrint();
            }
            primaries.get(i).prettyPrint();
            if (i != primaries.size() - 1)
                factorOprs.get(i).prettyPrint();
        }
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = primaries.get(0).eval(curScope);
        if (prefixes.get(0) != null) {
            TokenKind tk = prefixes.get(0).prefix;
            switch (tk) {
                case plusToken:
                    v = v.evalPositive(this);
                    break;
                case minusToken:
                    v = v.evalNegate(this);
                    break;
                default:
                    RuntimeValue.runtimeError(tk + " is not a valid AspFactorPrefix !", this);
            }
        }
        for (int i = 1; i < prefixes.size(); i++) {
            RuntimeValue nextPrimary = primaries.get(i).eval(curScope);
            if (prefixes.get(i) != null) {
                TokenKind tk = prefixes.get(i).prefix;
                switch (tk) {
                    case plusToken:
                        nextPrimary = nextPrimary.evalPositive(this);
                        break;
                    case minusToken:
                        nextPrimary = nextPrimary.evalNegate(this);
                        break;
                    default:
                        RuntimeValue.runtimeError(tk + " is not a valid AspFactorPrefix !", this);
                }
            }
            TokenKind tk = factorOprs.get(i - 1).opr;
            switch (tk) {
                case astToken:
                    v = v.evalMultiply(nextPrimary, this);
                    break;
                case slashToken:
                    v = v.evalDivide(nextPrimary, this);
                    break;
                case percentToken:
                    v = v.evalModulo(nextPrimary, this);
                    break;
                case doubleSlashToken:
                    v = v.evalIntDivide(nextPrimary, this);
                    break;
                default:
                    //RuntimeValue.runtimeError(tk + " is not a valid AspFactorOpr !", this);
                    Main.panic("Illegal factor operator: " + tk + " !");
            }
        }
        return v;
    }

}
