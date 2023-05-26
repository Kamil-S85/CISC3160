// The symbol table is a map from variable names to their values
public class SymbolTable {
    // HashMap to store symbols
    private Map<String, Integer> symbols = new HashMap<>();

    // Adds new variable to symbol table with its value
    public void define(String name, int value) {
        symbols.put(name, value);
    }

    // Returns value of variable from symbol table
    public Integer lookup(String name) {
        return symbols.get(name);
    }

    public Collection<String> getAllVariableNames() {
        return symbols.keySet();
    }
}

// Semantic Analyzer that will check AST for semantic correctness
public class SemanticAnalyzer {
    // Symbol table to keep track of variables
    private SymbolTable symbolTable = new SymbolTable();

    // Entry point for Semantic Analyzer. Dispatches node to appropriate method
    public void visit(AST node) throws SemanticAnalyzerException {
        if (node instanceof Compound) {
            visitCompound((Compound) node);
        } else if (node instanceof Assign) {
            visitAssign((Assign) node);
        } else if (node instanceof Var) {
            visitVar((Var) node);
        } else if (node instanceof Num) {
            visitNum((Num) node);
        } else if (node instanceof NoOp) {
            visitNoOp((NoOp) node);
        }
    }

    // Called when Compound node present. Visits all children of this node
    private void visitCompound(Compound node) throws SemanticAnalyzerException {
        for (AST child : node.children) {
            visit(child);
        }
    }

    // Called when Assign node present. Adds variable to symbol table and then visits right side of assignment
    private void visitAssign(Assign node) throws SemanticAnalyzerException {
        String varName = node.left.token.value;
        symbolTable.define(varName, 0);  // Assume all variables are integers
        visit(node.right);
    }

    // Called when Var node present. Checks if variable has been defined in symbol table, if not, throws error
    private void visitVar(Var node) throws SemanticAnalyzerException {
        String varName = node.token.value;
        if (symbolTable.lookup(varName) == null) {
            throw new SemanticAnalyzerException("Error: Variable not found " + varName);
        }
    }

    // Called when Num node present.
    private void visitNum(Num node) {
    }

    // Called when NoOp node present.
    private void visitNoOp(NoOp node) {
    }
}
