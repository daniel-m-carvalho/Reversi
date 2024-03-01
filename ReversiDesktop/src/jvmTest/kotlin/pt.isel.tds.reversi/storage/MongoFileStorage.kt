package pt.isel.tds.reversi.storage

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import pt.isel.tds.storage.MongoDriver
import pt.isel.tds.storage.getDocument
import pt.isel.tds.storage.insertDocument
import java.awt.Dimension
import kotlin.test.assertNotNull


class MongoTests {

    data class Doc(val _id: String, val field : Int)
    @Test
    fun `test insert Document`(){
        MongoDriver().use {drv ->
            val collection = drv.getCollection<Doc>("test")
            collection.insertDocument(Doc("id1", 1))
            val doc = collection.getDocument("id1")
            assertNotNull(doc)
            kotlin.test.assertEquals(1, doc.field)
        }
    }
}