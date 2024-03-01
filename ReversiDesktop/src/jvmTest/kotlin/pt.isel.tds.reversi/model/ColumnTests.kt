package pt.isel.tds.reversi.model

import kotlin.test.*
import pt.isel.tds.reversi.model.*
/*
* Tests for the Column class.
*/

class ColumnTests {
    @Test fun `Test column properties`() {
        val sut = Column('D')
        assertEquals('D', sut.symbol)
        assertEquals(3, sut.index)
        assertEquals("Column D", sut.toString())
        val other = Column('D')
        assertEquals(sut, other)
    }
    @Test fun `Convert a symbol to a Column`() {
        assertNull('3'.toColumnOrNull())
        assertNull(('A'+BOARD_DIM).toColumnOrNull())
        val ex = assertFailsWith<IllegalArgumentException> { '?'.toColumn() }
        assertEquals("Invalid column ?", ex.message)
    }
    @Test fun `Column values are correct`() {
        assertEquals(BOARD_DIM, Column.values.size)
        assertEquals(0, Column.values[0].index)
        assertEquals('A'+BOARD_DIM-1, Column.values[BOARD_DIM-1].symbol)
    }
}



