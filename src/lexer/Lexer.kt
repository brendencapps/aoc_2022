package lexer

enum class Type {
    LEFT_BRACE,
    RIGHT_BRACE,
    LEFT_PARENTHESES,
    RIGHT_PARENTHESES,
    COMMA,
    SEMICOLON,
    PLUS,
    EQUAL,
    IDENTIFIER,
    LET,
    FUNCTION,
    NUMBER,
    EOF,
    ILLEGAL
}

data class Token(val type: Type, val identifier: String = "")

class Lexer(private val input: String) {
    //val tokenFlow
    init {

    }
}