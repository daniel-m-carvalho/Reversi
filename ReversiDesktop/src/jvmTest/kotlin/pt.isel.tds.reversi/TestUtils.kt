package pt.isel.tds.reversi

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotSame
import org.junit.Test
import pt.isel.tds.reversi.model.Moves
import pt.isel.tds.reversi.storage.Serializer
import kotlin.io.path.Path
import kotlin.io.path.deleteExisting
import kotlin.io.path.exists

