package pt.isel.tds.reversi.model

const val BOARD_DIM = 8
const val MAX_MOVES = BOARD_DIM * BOARD_DIM

typealias Moves = Map<Cell, Player>

/**
 * Represents a board of the game.
 * @property moves the map of the moves of the game.
 * @constructor Creates a board with the given [moves] that is map from [Cell] to [Player] ([Moves]).
 * There are four possible states of board: [BoardRun], [BoardWin], [BoardDraw] and [BoardPass].
 * These hierarchy is to be used by pattern matching.
 */
sealed class Board(val moves: Moves) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Board) return false
        if (this::class != other::class) return false
        return moves.size == other.moves.size
    }
    override fun hashCode(): Int = moves.hashCode()
}

open class BoardRun(moves: Moves, val turn: Player) : Board(moves)
class BoardWin(moves: Moves, val winner: Player) : Board(moves)
class BoardDraw(moves: Moves) : Board(moves)
class BoardPass(moves : Moves, turn: Player) : BoardRun(moves, turn)


/**
 * Creates a new board with the given [first] as the first turn.
 */
val initialBoard = mapOf(
    Cell(BOARD_DIM / 2, BOARD_DIM / 2) to Player.WHITE,
    Cell(BOARD_DIM / 2 - 1, BOARD_DIM / 2 - 1) to Player.WHITE,
    Cell(BOARD_DIM / 2 - 1, BOARD_DIM / 2) to Player.BLACK,
    Cell(BOARD_DIM / 2, BOARD_DIM / 2 - 1) to Player.BLACK
)

fun createBoard(first: Player) = BoardRun(initialBoard, first)

//New Board condition
val Board.isNew: Boolean
    get() = this is BoardRun && moves.isEmpty()


/**
 * Makes a move in [cell] cell by the current turn.
 * @throws IllegalArgumentException if the [cell] is already used.
 * @throws IllegalArgumentException if the [cell] is not playable.
 * @throws IllegalStateException if the game is over (Draw or Win).
 */
fun Board.play(cell: Cell): Board {
    return when (this) {
        is BoardRun -> {
            require(moves[cell] == null) { "Position $cell used" }
            require(this.targets(cell)) { "Position $cell not playable" }
            val moves = changePieces(cell)
            val score = getScore(moves)
            when {
                isDraw(cell, score) -> BoardDraw(moves)
                isWin(cell, score).first -> BoardWin(moves, isWin(cell, score).second)
                else -> BoardRun(moves, turn.other())
            }
        }
        is BoardWin, is BoardDraw -> error("Game over")
    }
}

/**
 * Checks if there is still any available plays for the current player.
 * @return the first available play or null if there is none.
 */
fun BoardRun.checkAvailablePlays() = Cell.values.firstOrNull{ cell -> targets(cell) && !moves.contains(cell) }

/**
 * Checks if it's a pass situation.
 * @return a BoardPass if it's a pass situation,
 * @throws IllegalStateException otherwise.
 */
fun BoardRun.tryPass() = run{
    val cap = checkAvailablePlays()
    if(cap == null) BoardPass(moves, turn.other())
    else throw IllegalStateException(("There is still available plays, $cap"))
}

/**
 * Checks if it's either a win or a draw when it's a double pass situation.
 * @return a BoardWin or a BoardDraw if it's a win or a draw situation respectively,
 */
fun BoardPass.getResult(): Board {
    val (blackPieces, whitePieces) = getScore()
    return if (blackPieces == whitePieces) BoardDraw(moves)
    else BoardWin(moves, if (blackPieces > whitePieces) Player.BLACK else Player.WHITE)
}

/**
 * Checks if the play in [cell] is a line of pieces of the current player, in any direction
 * If it is, it changes the pieces in the line to the current player.
 * @param cell the cell to check.
 */
fun BoardRun.changePieces(cell: Cell) =
    buildMap {
        putAll(moves)
        put(cell, turn)
        Direction.values().forEach { dir ->
            val cellsToFlip = mutableListOf<Cell>()
            val cells = cellsInDirection(cell, dir)
            var canFlip = false
            for (c in cells) {
                if (moves[c] == turn.other()) {
                    cellsToFlip.add(c)
                } else if (moves[c] == turn) {
                    canFlip = true
                    break
                } else {
                    break
                }
            }
            if (canFlip) {
                cellsToFlip.forEach { put(it, turn) }
            }
        }
    }

/**
 * Checks if the cell is playable by the current player.
 * @param cell the cell to check.
 * @return true if the cell is playable, false otherwise.
 */
fun BoardRun.targets(cell: Cell): Boolean =
    Direction.values().any { dir ->
        if (cell + dir !in this.moves.keys || moves[cell + dir] == turn) {
            false
        } else {
            val index = cellsInDirection(cell, dir)
                .indexOfFirst { moves[it] == turn }
            if (index == -1 ) false
            else {
                val subL = cellsInDirection(cell, dir).subList(0, index)
                if(subL.any { moves[it] == null }) false
                   else subL.any { moves[it] == turn.other() && moves[it+dir]!= null}
            }
        }
    }

/**
 * Checks if the move in [cell] position is a winning move.
 */
private fun BoardRun.isWin(cell: Cell, score : Pair<Int,Int>) : Pair<Boolean, Player> {
    val (blackPieces, whitePieces) = score
    return if (moves.plus(cell to turn).size == MAX_MOVES )
        Pair(true, if (blackPieces > whitePieces) Player.BLACK else Player.WHITE)
    else Pair(false, turn)
}

/**
 * Checks if the state of the board will end the game as a Draw.
 */
private fun BoardRun.isDraw(cell : Cell, score: Pair<Int, Int>) : Boolean{
    val (blackPieces, whitePieces) = score
    return moves.plus(cell to turn).size == MAX_MOVES && blackPieces == whitePieces
}

/**
 * Gets the score of the board.
 */
fun Board.getScore(moves : Moves = this.moves): Pair<Int, Int> {
    val blackPieces = moves.count { it.value == Player.BLACK }
    val whitePieces = moves.count { it.value == Player.WHITE }
    return Pair(blackPieces, whitePieces)
}

/**
 * When it's a pass situation, checks the board type.
 * if it's a BoardPass, it returns the result of the game.
 * if it's a BoardRun, it tries to pass.
 * @throws IllegalStateException if the game is not running.
 */
fun Board.passOrRunBoard(): Board {
    if(this is BoardPass) return getResult()
    if(this is BoardRun) return tryPass()
    else throw IllegalStateException("You cant pass, the game is not running")
}
