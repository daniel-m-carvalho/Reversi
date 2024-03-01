package storage

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import pt.isel.tds.reversi.storage.Serializer
import pt.isel.tds.reversi.storage.Storage
import pt.isel.tds.storage.TextFileStorage

class TextFileStorageTest {
    private lateinit var storage: Storage<String, String>
    private val key = "testKey"
    private val data = "testData"

    @Before
    fun setup() {
        storage = TextFileStorage("games", MySerializer())
    }

    @Test
    fun testCreate() {
        storage.create(key, data)
        val storedData = runBlocking { storage.read(key) }
        assertEquals(data, storedData)
    }

    @Test
    fun `Read a non-existent key`() {
        val key = ""
        val storedData = runBlocking { storage.read(key) }
        assertNull(storedData)
    }


    @Test
    fun testDelete() {
        val storageDataBefore = runBlocking{ storage.read(key)}
        assertEquals(data,storageDataBefore)

        storage.delete(key)

        val storageDataAfter= runBlocking{ storage.read(key)}
        assertNull(storageDataAfter)

    }

    // Implement tests for update and delete methods as well

    class MySerializer : Serializer<String, String> {
        override fun serialize(data: String): String {
            return data
        }

        override fun deserialize(stream: String): String {
            return stream
        }
    }

}