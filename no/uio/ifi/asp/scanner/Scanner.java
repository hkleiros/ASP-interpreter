// © 2021 Dag Langmyhr, Institutt for informatikk, Universitetet i Oslo

//Skal lese programkoden fra en fil
// fjerne alle kommentarer og whitespace 
// dele resten av teksten opp i symboler 
// opprette tokens for hver linje 
package no.uio.ifi.asp.scanner;

import java.io.*;
import java.util.*;

import no.uio.ifi.asp.main.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class Scanner {
    private LineNumberReader sourceFile = null;
    private String curFileName;
    private ArrayList<Token> curLineTokens = new ArrayList<>();
    private Stack<Integer> indents = new Stack<>();
    private final int TABDIST = 4;

    public Scanner(String fileName) {
        curFileName = fileName;
        indents.push(0);

        try {
            sourceFile = new LineNumberReader(
                    new InputStreamReader(
                            new FileInputStream(fileName),
                            "UTF-8"));
        } catch (IOException e) {
            scannerError("Cannot read " + fileName + "!");
        }
    }

    private void scannerError(String message) {
        String m = "Asp scanner error";
        if (curLineNum() > 0)
            m += " on line " + curLineNum();
        m += ": " + message;

        Main.error(m);
    }

    public Token curToken() {
        while (curLineTokens.isEmpty()) {
            readNextLine();
        }
        return curLineTokens.get(0);
    }

    public void readNextToken() {
        if (!curLineTokens.isEmpty())
            curLineTokens.remove(0);
    }

    private void readNextLine() {
        curLineTokens.clear();

        // Read the next line:
        String line = null;

        try {
            line = sourceFile.readLine();
            if (line == null) {
                sourceFile.close();
                sourceFile = null;
            } else {
                Main.log.noteSourceLine(curLineNum(), line);
            }
        } catch (IOException e) {
            sourceFile = null;
            scannerError("Unspecified I/O error!");
        }

        // -- Must be changed in part 1:
        if (line == null) {
            //DEDENT 
            while (indents.peek() != 0) {
                indents.pop();
                curLineTokens.add(new Token(dedentToken, curLineNum()));
            }
            curLineTokens.add(new Token(eofToken, curLineNum()));
            for (Token t : curLineTokens)
                Main.log.noteToken(t);
            return;
        }

        if ((checkCommentLine(line) || checkBlank(line)) && (sourceFile != null))
            return;

        line = expandLeadingTabs(line);
        calculateIndentLevel(line);
        createTokens(line);

        // Terminate line:
        curLineTokens.add(new Token(newLineToken, curLineNum()));

        for (Token t : curLineTokens)
            Main.log.noteToken(t);
    }

    public void createTokens(String s) {
        char[] line = s.toCharArray();
        String midlertidig = "";

        for (int i = 0; i < line.length; i++) {
            if (isLetterAZ(line[i])) {
                while (i != line.length && (isLetterAZ(line[i]) || isDigit(line[i]))) {
                    midlertidig += line[i];
                    i++;
                }
                createNameToken(midlertidig);
                midlertidig = "";
                if (!(i >= line.length))
                    i--;

            } else if (isDigit(line[i])) {
                if (line[i] == '0' && (i == line.length - 1 || line[i + 1] != '.')) {
                    midlertidig += line[i];
                } else {
                    while (i != line.length && (isDigit(line[i]) || line[i] == '.')) {
                        if (line[i] == '.' && !isDigit(line[i + 1])) {
                            scannerError("Illegal character '.'!");
                        }
                        midlertidig += line[i];
                        i++;
                    }
                    i--;
                }
                createNumberToken(midlertidig);
                midlertidig = "";

            } else if (line[i] == '"') {
                //midlertidig += line[i];
                i++;
                while (i != line.length && (line[i] != '"')) {
                    midlertidig += line[i];
                    i++;
                    if (i == line.length)
                        scannerError("String literal not terminated !");
                }
                //midlertidig += line[i];
                createStringToken(midlertidig);
                midlertidig = "";

            } else if (line[i] == '\'') {
                //midlertidig += line[i];
                i++;
                while ((line[i] != '\'') && i != line.length) {
                    midlertidig += line[i];
                    i++;
                    if (i == line.length)
                        scannerError("String literal not terminated !");
                }

                //midlertidig += line[i];
                createStringToken(midlertidig);
                midlertidig = "";

            } else if (line[i] == '#') {
                break;
            } else {
                if (line[i] != ' ')
                    midlertidig += line[i];

                //sjekk om det er en sammensatt operator 
                if (line[i] == '/') {
                    if (line[i + 1] == '/') {
                        i++;
                        midlertidig += line[i];
                    }
                } else if (line[i] == '=' || line[i] == '<' || line[i] == '>' || line[i] == '!') {
                    if (line[i + 1] == '=') {
                        i++;
                        midlertidig += line[i];
                    }
                }
                if (midlertidig != "") {
                    createOperatorOrDelimiter(midlertidig);
                }
                midlertidig = "";
            }
        }
    }

    private void createOperatorOrDelimiter(String sym) {
        TokenKind kind = null;
        for (TokenKind tk : EnumSet.range(astToken, semicolonToken)) {
            if (sym.equals(tk.image)) {
                kind = tk;
                break;
            }
        }
        if (kind == null)
            scannerError("Illegal character: " + sym + " !");
        curLineTokens.add(new Token(kind, curLineNum()));
    }

    private void createNameToken(String value) {
        Token token = new Token(TokenKind.nameToken, curLineNum());
        token.name = value;
        token.checkResWords();
        curLineTokens.add(token);
    }

    private void createNumberToken(String value) {
        Token token = null;
        if (value.contains(".")) {
            token = new Token(floatToken, curLineNum());
            token.floatLit = Double.valueOf(value);
        } else {
            token = new Token(integerToken, curLineNum());
            token.integerLit = Integer.valueOf(value);
        }
        curLineTokens.add(token);
    }

    private void createStringToken(String value) {
        Token token = new Token(stringToken, curLineNum());
        token.stringLit = value;
        curLineTokens.add(token);
    }

    private boolean checkBlank(String s) {
        if (s == null)
            return true;
        for (char c : s.toCharArray()) {
            if (c != ' ' && c != '\t')
                return false;
        }
        return true;
    }

    private boolean checkCommentLine(String s) {
        if (s == null)
            return false;
        boolean strOrChar = false;

        for (char c : s.toCharArray()) {
            if (isDigit(c) || isLetterAZ(c))
                return false;
            if (c == '"' || c == '\'') {
                if (strOrChar)
                    strOrChar = false;
                else
                    strOrChar = true;
            } else if (c == '#' && !strOrChar) {
                return true;
            }
        }
        return false;
    }

    private void calculateIndentLevel(String line) {
        int indentNr = findIndent(line);
        if (indentNr < 0) {
            return;
        }
        if (indentNr > indents.peek()) {
            indents.push(indentNr);
            curLineTokens.add(new Token(indentToken, curLineNum()));
        } else {
            while (indentNr < indents.peek()) {
                indents.pop();
                curLineTokens.add(new Token(dedentToken, curLineNum()));
            }
        }

        if (indentNr != indents.peek()) {
            scannerError("Indentation error !");
        }
    }

    public int curLineNum() {
        return sourceFile != null ? sourceFile.getLineNumber() : 0;
    }

    private int findIndent(String s) {
        if (s == null)
            return -1;
        int indent = 0;

        while (indent < s.length() && s.charAt(indent) == ' ')
            indent++;
        return indent;
    }

    // -- Must be changed in part 1:
    private String expandLeadingTabs(String s) {
        // -- Must be changed in part 1:

        if (s == null)
            return s;

        String expanded = "";
        int n = 0;

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == (' ')) {
                expanded += " ";
                n++;
            } else if (s.charAt(i) == '\t') {
                // erstatt med 4-(n mod 4) blanke
                int rep = TABDIST - (n % TABDIST);
                for (int j = 0; j < rep; j++) {
                    expanded += " ";
                }
                n += rep;
            } else {
                expanded += s.substring(i);
                break;
            }
        }
        return expanded;
    }

    private boolean isLetterAZ(char c) {
        return ('A' <= c && c <= 'Z') || ('a' <= c && c <= 'z') || (c == '_');
    }

    private boolean isDigit(char c) {
        return '0' <= c && c <= '9';
    }

    public boolean anyEqualToken() {
        for (Token t : curLineTokens) {
            if (t.kind == equalToken)
                return true;
            if (t.kind == semicolonToken)
                return false;
        }
        return false;
    }

    // -- Must be changed in part 2:
    public boolean isCompOpr() {
        TokenKind k = curToken().kind;
        // -- Must be changed in part 2:
        // comparative operator

        // sjekk om curToken og nextToken er > < >= <= == !=
        for (TokenKind tk : EnumSet.range(doubleEqualToken, notEqualToken)) {
            if (k.equals(tk)) {
                if (k == minusToken || k == doubleSlashToken) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    public boolean isFactorPrefix() {
        TokenKind k = curToken().kind;
        // -- Must be changed in part 2:
        if (k == minusToken || k == plusToken) {
            return true;
        }
        return false;
    }

    public boolean isFactorOpr() {
        TokenKind k = curToken().kind;
        // -- Must be changed in part 2:
        if (k == astToken || k == slashToken || k == percentToken || k == doubleSlashToken) {
            return true;
        }
        return false;
    }

    public boolean isTermOpr() {
        TokenKind k = curToken().kind;
        // -- Must be changed in part 2:
        if (k == minusToken || k == plusToken) {
            return true;
        }
        return false;
    }

}
