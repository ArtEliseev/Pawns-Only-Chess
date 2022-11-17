package chess

import kotlin.math.absoluteValue

class PawnsChess(private val firstPlayer: String, private val secondPlayer: String) {
    private val chessboard = MutableList(8) { i -> MutableList(8) { if (i == 1) 'W' else if (i == 6) 'B' else ' ' } }
    private val pattern = "[a-h][1-8][a-h][1-8]".toRegex()
    private var whiteEnPassant = false
    private var blackEnPassant = false
    private var input = "a1a2"
    private var startColumn = 0
    private var startRow = 0
    private var targetColumn = 0
    private var targetRow = 0
    var exit = false
        private set
    var gameOver = false
        private set

    init {
        printBoard()
    }

    private fun printBoard() {
        val line = "  +---+---+---+---+---+---+---+---+"
        println(line)
        for (i in 8 downTo 1) {
            print("$i |")
            for (j in chessboard[i - 1].indices) {
                print(" ${chessboard[i - 1][j]} |")
            }
            println("\n" + line)
        }
        println("    a   b   c   d   e   f   g   h \n")
    }

    private fun divideInput(input: String) {
        startColumn = charToInt(input[0])
        startRow = numToInt(input[1])
        targetColumn = charToInt(input[2])
        targetRow = numToInt(input[3])
    }

    fun turn (player: String) {
        var color = ""
        when (player) {
            firstPlayer -> color = "white"
            secondPlayer -> color = "black"
        }
        println("$player's turn:")
        input = readln().lowercase()
        if (input == "exit") {
            println("Bye!")
            exit = true
        } else if (!pattern.matches(input)) {
            println("Invalid Input")
            turn(player)
        } else {
            when (color) {
                "white" -> {
                    if (!invalidMoves(color, 'W')) {
                        moveOrCapture(color)
                        whiteEnPassant = input == "e2e4"
                        if (input !== "e2e4") whiteEnPassant = false
                    } else {
                        turn(player)
                    }
                }
                "black" -> {
                    if (!invalidMoves(color, 'B')) {
                        moveOrCapture(color)
                        blackEnPassant = input == "d7d5"
                        if (input !== "d7d5") blackEnPassant = false
                    } else {
                        turn(player)
                    }
                }
            }
            checkWin()
            checkStalemate(color)
        }
    }

    private fun invalidMoves(color: String, colorChar: Char): Boolean {
        divideInput(input)

        var colorRespawn = 0
        when (color) {
            "white" -> colorRespawn = 1
            "black" -> colorRespawn = 6
        }

        if (chessboard[startRow][startColumn] !== colorChar) {
            println("No $color pawn at ${input.substring(0, 2)}")
            return true
        } else if (
            startRow !== colorRespawn && (targetRow - startRow).absoluteValue > 1 || // if pawn moves 2+ cells NOT from starting position
            startRow == colorRespawn && (targetRow - startRow).absoluteValue > 2 || // if pawn moves 3+ cells from starting position
            startColumn !== targetColumn && chessboard[targetRow][targetColumn] == ' ' || // if pawn moves diagonally without capture
            (startColumn - targetColumn).absoluteValue > 1 || // if pawn moves diagonally for more than 1 line
            color == "white" && startColumn == targetColumn && startRow >= targetRow || // if white pawn moves back or stays
            color == "black" && startColumn == targetColumn && startRow <= targetRow || // if black pawn moves back or stays
            startColumn == targetColumn && chessboard[targetRow][targetColumn] !== ' ' // if the target cell is occupied by other pawn & there is the same column
        ) {
            println("Invalid Input")
            return true
        } else {
            return false
        }
    }

    private fun moveOrCapture(color: String) {
        divideInput(input)

        when (color) {
            "white" -> {
                if (blackEnPassant && input == "d4e3") {
                    chessboard[2][4] = 'B'
                    chessboard[3][4] = ' '
                    chessboard[3][3] = ' '
                } else {
                    chessboard[targetRow][targetColumn] = 'W'
                    chessboard[startRow][startColumn] = ' '
                }
            }
            "black" -> {
                if (whiteEnPassant && input == "e5d6") {
                    chessboard[5][3] = 'W'
                    chessboard[4][3] = ' '
                    chessboard[4][4] = ' '
                } else {
                    chessboard[targetRow][targetColumn] = 'B'
                    chessboard[startRow][startColumn] = ' '
                }
            }
        }
        printBoard()
    }

    private fun checkStalemate(color: String) {
        divideInput(input)

        var calcW = 0
        var calcB = 0

        for (i in chessboard.indices) {
            for (j in chessboard[i]) {
                if (j == 'W') calcW++
                if (j == 'B') calcB++
            }
        }

        when (color) {
            "white" -> {
                if (targetRow < 7 && chessboard[targetRow + 1][targetColumn] == 'B' && calcB == 1) {
                    println("Stalemate!")
                    println("Bye!")
                    gameOver = true
                }
            }
            "black" -> {
                if (targetRow > 0 && chessboard[targetRow - 1][targetColumn] == 'W' && calcW == 1) {
                    println("Stalemate!")
                    println("Bye!")
                    gameOver = true
                }
            }
        }
    }

    private fun checkWin() {
        var calcW = 0
        var calcB = 0

        if (chessboard[7].contains('W')) {
            println("White Wins!")
            println("Bye!")
            gameOver = true
        }

        if (chessboard[0].contains('B')) {
            println("Black Wins!")
            println("Bye!")
            gameOver = true
        }

        for (i in chessboard.indices) {
            if (!chessboard[i].contains('W')) calcW++
            if (!chessboard[i].contains('B')) calcB++
        }
        when {
            calcW == 8 -> {
                println("Black Wins!")
                println("Bye!")
                gameOver = true
            }
            calcB == 8 -> {
                println("White Wins!")
                println("Bye!")
                gameOver = true
            }
        }
    }

    private fun charToInt(char: Char) = char.code - (if (char.isLowerCase()) 97 else 65)

    private fun numToInt(char: Char) = char.digitToInt() - 1
}

