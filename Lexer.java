// Token class defines tokens in language
class Token {
    // Token type
    public TokenType type;
    // Token text
    public String value;

    // Constructor: initializes a new Token with a type and value
    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }
}

// Tokens in language
enum TokenType {
    IDENTIFIER, // Variables names
    INTEGER, // Numbers
    PLUS, // '+' operator
    MINUS, // '-' operator
    MUL, // '*' operator
    LPAREN, // '('
    RPAREN, // ')'
    ASSIGN, // '='
    SEMICOLON, // ';'
    EOF, // End of file/input.
}

// Lexer class reads characters from input and groups them into tokens
public class Lexer {
    // Input string to tokenize
    private String input;

    // Index of current character in input string
    private int pos = 0;

    // Current character in input string
    private char currentChar;

    // Constructor: initializes new Lexer with an input string
    public Lexer(String input) {
        this.input = input;
        // Starts with first character of input
        currentChar = input.charAt(pos);
    }

    // Moves to next character in input
    private void advance() {
        // Moves to next character
        pos++;
        // At end of input sets currentChar to null character
        if (pos >= input.length()) {
            currentChar = '\0';
        } else {
            // Otherwise updates currentChar to new current character
            currentChar = input.charAt(pos);
        }
    }

    private void error() throws Exception {
        ErrorHandler.error("Invalid character: " + (char) currentChar);
    }
    
    // Skips whitespace characters in input
    private void skipWhitespace() {
        // While current character is a whitespace character, moves to next character
        while (currentChar != '\0' && Character.isWhitespace(currentChar)) {
            advance();
        }
    }

    // Reads integer from input
    private Token integer() {
        // Holds characters of integer
        StringBuilder result = new StringBuilder();
        // While current character is a digit adds it to result and move to next character
        while (currentChar != '\0' && Character.isDigit(currentChar)) {
            result.append(currentChar);
            advance();
        }
        // Returns new INTEGER token with the integer as its value
        return new Token(TokenType.INTEGER, result.toString());
    }

    // Reads identifier from input
    private Token id() {
        // Holds characters of identifier
        StringBuilder result = new StringBuilder();
        // While current character is a letter or digit, adds it to the result and moves to next character
        while (currentChar != '\0' && Character.isLetterOrDigit(currentChar)) {
            result.append(currentChar);
            advance();
        }
        // Returns new IDENTIFIER token with the identifier as its value
        return new Token(TokenType.IDENTIFIER, result.toString());
    }

       // Reads the next token from input
       public Token getNextToken() throws Exception {
        // Continues until all characters in the input are read
        while (currentChar != '\0') {
            // If current character is whitespace skip it and continue to next character
            if (Character.isWhitespace(currentChar)) {
                skipWhitespace();
                continue;
            }

            // If current character is a digit, return an INTEGER token
            if (Character.isDigit(currentChar)) {
                return integer();
            }

            // If current character is a letter, return an IDENTIFIER token
            if (Character.isLetter(currentChar)) {
                return id();
            }

            // For each possible operator or punctuation mark, return corresponding token
            if (currentChar == '+') {
                advance();
                return new Token(TokenType.PLUS, "+");
            }
            if (currentChar == '-') {
                advance();
                return new Token(TokenType.MINUS, "-");
            }
            if (currentChar == '*') {
                advance();
                return new Token(TokenType.MUL, "*");
            }
            if (currentChar == '(') {
                advance();
                return new Token(TokenType.LPAREN, "(");
            }
            if (currentChar == ')') {
                advance();
                return new Token(TokenType.RPAREN, ")");
            }
            if (currentChar == '=') {
                advance();
                return new Token(TokenType.ASSIGN, "=");
            }
            if (currentChar == ';') {
                advance();
                return new Token(TokenType.SEMICOLON, ";");
            }

            // If current character is none of the above, it's an error.
            error();
        }

        // If all characters in the input are read, return an EOF token.
        return new Token(TokenType.EOF, "");
    }   
}
