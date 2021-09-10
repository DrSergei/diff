import frontend.*


import kotlin.test.*
import java.io.*

internal class TestFrontend {

    @Test
    fun testcheckFile1() {
        assertEquals(true, checkFile(File("1.txt")))
    }

    @Test
    fun testcheckFile2() {
        assertEquals(true, checkFile(File("2.txt")))
    }

    @Test
    fun testcheckFile3() {
        assertEquals(false, checkFile(File("3.txt")))
    }


}
