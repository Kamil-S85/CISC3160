import java.util.*;

// Parser class implements a recursive descent parser that builds an abstract syntax tree (AST) from the input
public class Parser {
    private Token currentToken;
    private Lexer lexer;

private void error(String errorMessage) throws ParserException {
    throw new ParserException(errorMessage);
    }

    // Constructor receives a Lexer to get tokens from
    public Parser(Lexer lexer) {
        this.lexer = lexer;
        // Initializes currentToken to first token received from lexer
        this.currentToken = lexer.getNextToken();
    }

    // The 'eat' method is used to consume token of given type
    // If current token type matches passed token type, get next token, otherwise throw error
    private void eat(TokenType tokenType) {
        if (this.currentToken.type == tokenType) {
            this.currentToken = this.lexer.getNextToken();
        } else {
            throw new RuntimeException("Invalid token");
        }
    }

    private void error() throws Exception {
        ErrorHandler.error("Invalid syntax");
    }
    
    // Parses factor (number, unary operation, variable, or expression in parentheses) from input
    private AST factor() throws ParserException {
        Token token = this.currentToken;

        if (token.type == TokenType.PLUS) {
            eat(TokenType.PLUS);
            return new UnaryOp(token, factor());
        } else if (token.type == TokenType.MINUS) {
            eat(TokenType.MINUS);
            return new UnaryOp(token, factor());
        } else if (token.type == TokenType.INTEGER) {
            eat(TokenType.INTEGER);
            return new Num(token);
        } else if (token.type == TokenType.LPAREN) {
            eat(TokenType.LPAREN);
            AST result = expr();
            eat(TokenType.RPAREN);
            return result;
        } else {
            AST node = variable();
            return node;
        }
    }

    // Parses term (factor or multiplication operation on factors) from input
    private AST term() throws ParserException {
        AST node = factor();

        while (currentToken.type == TokenType.MUL) {
            Token token = currentToken;
            eat(TokenType.MUL);
            node = new BinOp(node, token, factor());
        }
        return node;
    }

    // Parses expression (term or addition/subtraction operation on terms) from input
    private AST expr() throws ParserException {
        AST node = term();

        while (currentToken.type == TokenType.PLUS || currentToken.type == TokenType.MINUS) {
            Token token = currentToken;
            if (token.type == TokenType.PLUS) {
                eat(TokenType.PLUS);
            } else if (token.type == TokenType.MINUS) {
                eat(TokenType.MINUS);
            }
            node = new BinOp(node, token, term());
        }
        return node;
    }

    // Parses assignment statement from input
    private AST assignmentStatement() {
        Var left = variable();
        Token token = currentToken;
        eat(TokenType.ASSIGN);
        AST right = expr();
        eat(TokenType.SEMICOLON);
        AST node = new Assign(left, token, right);

        return node;
    }

    // Parses empty statement
    private AST empty() {
        return new NoOp();
    }

    // Parses statement (assignment statement or empty statemen) from the input
    private AST statement() {
        AST node;
        if (currentToken.type == TokenType.IDENTIFIER) {
            // If current token is an identifier, expect an assignment statement
            node = assignmentStatement();
        } else {
            // Otherwise, it's an empty statement
            node = empty();
        }
        return node;
    }

    // Parses a list of statements from input used to process multiple statements in code
    private AST statementList() {
        AST node = statement();
        List<AST> results = new ArrayList<>();
        results.add(node);

        while (currentToken.type == TokenType.SEMICOLON) {
            eat(TokenType.SEMICOLON);
            results.add(statement());
        }

        if (currentToken.type == TokenType.IDENTIFIER) {
            eat(TokenType.IDENTIFIER);
        }

        Compound compoundNode = new Compound();
        for (AST ast : results) {
            compoundNode.add(ast);
        }
        return compoundNode;
    }

    // Starting point of grammar, parses entire input and builds AST from it
    public AST program() {
        AST node = statementList();
        if (currentToken.type != TokenType.EOF) {
            throw new RuntimeException("Invalid syntax");
        }

        return node;
    }
}

abstract class AST {
}

public class BinOp extends AST {
    public AST left;
    public Token op;
    public AST right;

    public BinOp(AST left, Token op, AST right) {
        this.left = left;
        this.op = op;
        this.right = right;
    }
}

public class UnaryOp extends AST {
    public Token op;
    public AST expr;

    public UnaryOp(Token op, AST expr) {
        this.op = op;
        this.expr = expr;
    }
}

public class Num extends AST {
    public Token token;
    public int value;

    public Num(Token token) {
        this.token = token;
        this.value = token.value;
    }
}

public class Var extends AST {
    public Token token;
    public String value;

    public Var(Token token) {
        this.token = token;
        this.value = token.value;
    }
}

public class Assign extends AST {
    public Var left;
    public Token op;
    public AST right;

    public Assign(Var left, Token op, AST right) {
        this.left = left;
        this.op = op;
        this.right = right;
    }
}

public class NoOp extends AST {
}

public class Compound extends AST {
    public List<AST> children;

    public Compound() {
        children = new ArrayList<>();
    }

    public void add(AST node) {
        children.add(node);
    }
}

