package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

class AspCompOpr extends AspSyntax {
	TokenKind opr;

	AspCompOpr(int n) {
		super(n);
	}

	static AspCompOpr parse(Scanner s) {
		enterParser("comp opr");
		AspCompOpr aco = new AspCompOpr(s.curLineNum());
		if (!s.isCompOpr()) {
			parserError("Expected a compOpr got a: " + s.curToken().kind + " !", s.curLineNum());
		}
		aco.opr = s.curToken().kind;
		skip(s, aco.opr);
		leaveParser("comp opr");
		return aco;
	}

	@Override
	void prettyPrint() {
		prettyWrite(" " + opr.toString() + " ");
	}

	@Override
	RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
		return null;
	}

}
