package chess

fun main() {
    println("Pawns-Only Chess")
    println("First Player's name:")
    val firstPlayer = readln()
    println("Second Player's name:")
    val secondPlayer = readln()
    val pawnsChess = PawnsChess(firstPlayer, secondPlayer)

    while (true) {
        if (!pawnsChess.exit && !pawnsChess.gameOver) pawnsChess.turn(firstPlayer)
        else break
        if (!pawnsChess.exit && !pawnsChess.gameOver) pawnsChess.turn(secondPlayer)
        else break
    }
}


