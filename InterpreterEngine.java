// Interprets abstract syntax tree
public class Interpreter {
    private AST tree;  // Root of AST
    private SemanticAnalyzer analyzer;  // Semantic analyzer
    private SymbolTable symbolTable = new SymbolTable();  // Symbol table

    // Constructor takes in AST and semantic analyzer
    public Interpreter(AST tree, SemanticAnalyzer analyzer) {
        this.tree = tree;
        this.analyzer = analyzer;
    }

    // Entry point for Interpreter. Dispatches node to appropriate method
    public int visit(AST node) {
        if (node instanceof BinOp) {
            return visitBinOp((BinOp) node);
        } else if (node instanceof Num) {
            return visitNum((Num) node);
        } else if (node instanceof UnaryOp) {
            return visitUnaryOp((UnaryOp) node);
        } else if (node instanceof Compound) {
            return visitCompound((Compound) node);
        } else if (node instanceof Assign) {
            return visitAssign((Assign) node);
        } else if (node instanceof Var) {
            return visitVar((Var) node);
        } else if (node instanceof NoOp) {
            return visitNoOp((NoOp) node);
        } else {
            throw new RuntimeException("Invalid node type");
        }
    }

    // Interprets binary operations (+, -, *)
    private int visitBinOp(BinOp node) {
        if (node.token.type == TokenType.PLUS) {
            return visit(node.left) + visit(node.right);
        } else if (node.token.type == TokenType.MINUS) {
            return visit(node.left) - visit(node.right);
        } else if (node.token.type == TokenType.MUL) {
            return visit(node.left) * visit(node.right);
        } else {
            throw new RuntimeException("Invalid operator");
        }
    }

    // Interprets number nodes by returning their value
    private int visitNum(Num node) {
        return Integer.parseInt(node.token.value);
    }

    // Interprets unary operations (+, -)
    private int visitUnaryOp(UnaryOp node) {
        if (node.token.type == TokenType.PLUS) {
            return +visit(node.expr);
        } else if (node.token.type == TokenType.MINUS) {
            return -visit(node.expr);
        } else {
            throw new RuntimeException("Invalid operator");
        }
    }

    // Interprets compound nodes by visiting all of their children
    private int visitCompound(Compound node) {
        for (AST child : node.children) {
            visit(child);
        }
        return 0;  // Return value doesn't matter for compound nodes
    }

    // Interprets assignment nodes by evaluating expression and updating symbol table
    private int visitAssign(Assign node) {
        String varName = node.left.token.value;
        int value = visit(node.right);
        symbolTable.define(varName, value);
        return value;  // Return value is value of expression
    }

    // Interprets variable nodes by looking up their value in symbol table
    private int visitVar(Var node) {
        String varName = node.token.value;
        Integer value = symbolTable.lookup(varName);
        if (value == null) {
            throw new RuntimeException("Variable not found " + varName);
        }
        return value;
    }

    // Interprets NoOp nodes.
    private int visitNoOp(NoOp node) {
        return 0;  // NoOp nodes don't have a value
    }

    // Entry point for interpretation process. Starts by running semantic analyzer and then interpreter.
    public void interpret() {
        // Starts by running semantic analyzer on AST to catch semantic errors before interpreting
        analyzer.visit(tree);

        // Starts interpreting from root of AST. Traverses the entire tree, interpreting each node along the way
        visit(tree);

        // Prints out values of all variables
        for (String varName : symbolTable.getAllVariableNames()) {
            System.out.println(varName + " = " + symbolTable.lookup(varName));
        }
    }
}