package pt.isel.tds.reversi.model

import kotlin.test.*
import pt.isel.tds.reversi.model.*

class CellTests {
    @Test
    fun `Get a cell by Row and Column and test properties`() {
        val sut = Cell(Row(1), Column('B'))
        assertEquals(Row(1), sut.row)
        assertEquals(Column('B'), sut.col)
        assertEquals(0, sut.row.index)
        assertEquals(1, sut.col.index)
        assertEquals("1B", sut.toString())
    }


    @Test
    fun `Get a cell by Row index and Column index`() {
        val sut = Cell(0, 1)
        assertEquals(Row(1), sut.row)
        assertEquals(Column('B'), sut.col)
        assertEquals(0, sut.row.index)
        assertEquals(1, sut.col.index)
        assertEquals("1B", sut.toString())
    }


    @Test
    fun `Get a Cell instance`() {
        val sut = Cell(Row(2), Column('D'))
        assertEquals(sut, Cell(1, 3))
        //assertSame(sut, Cell(1, 3))
    }

    @Test
    fun `Get all valid cells`() {
        assertEquals(BOARD_DIM * BOARD_DIM, Cell.values.size)
        assertEquals(Cell(Row(1), Column('A')), Cell.values[0])
        assertEquals(Cell(Row(BOARD_DIM), Column('A' + BOARD_DIM - 1)), Cell.values[BOARD_DIM * BOARD_DIM - 1])
    }
}

