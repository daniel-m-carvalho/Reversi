package pt.isel.tds.reversi

import pt.isel.tds.reversi.model.*
import kotlin.test.*

class ReversiTests {

   //  @get:Rule
    //val composeTestRule = createComposeRule()

    //For testing purposes
    private val drawBoard = mapOf(
        Cell(0, 0) to Player.WHITE,
        Cell(0, 1) to Player.BLACK,
        Cell(0, 2) to Player.WHITE,
        Cell(0, 3) to Player.BLACK,
        Cell(1, 0) to Player.BLACK,
        Cell(1, 1) to Player.WHITE,
        Cell(1, 2) to Player.BLACK,
        Cell(1, 3) to Player.WHITE,
        Cell(2, 0) to Player.WHITE,
        Cell(2, 1) to Player.BLACK,
        Cell(2, 2) to Player.WHITE,
        Cell(2, 3) to Player.BLACK,
        Cell(3, 0) to Player.BLACK,
        Cell(3, 1) to Player.WHITE,
        Cell(3, 2) to Player.BLACK,
        Cell(3, 3) to Player.WHITE
    )

    @Test
    fun `test initial board`() {
        val board = createBoard(Player.BLACK)
        assertEquals(board, BoardRun(initialBoard, Player.BLACK))
    }

    //Tests need more elaboration
    @Test
    fun `test draw`() {
        val board = BoardRun(drawBoard, Player.BLACK)
        assertEquals(BOARD_DIM * BOARD_DIM, board.moves.size)
    }
}