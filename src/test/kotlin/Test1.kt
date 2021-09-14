// Стандартная библиотека
import kotlin.test.*
import java.io.*

// Собственные пакеты
import frontend.*
import backend.*

internal class TestFrontend {

    @Test
    fun testcheckFile() {
        assertEquals(true, checkFile(File("1.txt")))
        assertEquals(true, checkFile(File("2.txt")))
        assertEquals(false, checkFile(File("abc")))
        assertEquals(false, checkFile(File("main.kt")))
    }

    @Test
    fun testparser() {
        assertEquals(false, parser1(arrayOf("-f", "--brief", "--help", "--", "1.txt")))
        assertEquals(true, parser1(arrayOf("-h", "--",)))
        assertEquals(true, parser1(arrayOf("--brief", "--", "1.txt", "2.txt")))
        assertEquals(false, parser1(arrayOf("-h", "--help", "--")))
    }

    @Test
    fun testdataFile() {

        assertEquals(mutableListOf(FastString("первая", "первая".hashCode()),
                                   FastString("первая", "первая".hashCode()),
                                   FastString("третья", "третья".hashCode()),
                                   FastString("четвертая", "четвертая".hashCode()),
                                   FastString("шестая", "шестая".hashCode()),), dataFile(File("1.txt")))

        assertEquals(mutableListOf(FastString("первая", "первая".hashCode()),
                                   FastString("вторая", "вторая".hashCode()),
                                   FastString("третья", "третья".hashCode()),
                                   FastString("четвертая", "четвертая".hashCode()),
                                   FastString("пятая", "пятая".hashCode()),), dataFile(File("2.txt")))
    }


}

internal class TestBackend {

    @Test
    fun testequals() {
        assertEquals(true, equals(FastString("aaa", "aaa".hashCode()), FastString("aaa", "aaa".hashCode())))
        assertEquals(false, equals(FastString("a", "a".hashCode()), FastString("b", "b".hashCode())))
        assertEquals(false, equals(FastString("abc", "abc".hashCode()), FastString("cba", "cba".hashCode())))
        assertEquals(false, equals(FastString("xxx", "xxx".hashCode()), FastString("yyy", "yyy".hashCode())))

    }

    @Test
    fun testdiffFast() {
        assertEquals(true, diffFast(mutableListOf(FastString("первая", "первая".hashCode()),
                                                          FastString("вторая", "вторая".hashCode()),
                                                          FastString("третья", "третья".hashCode()),
                                                          FastString("четвертая", "четвертая".hashCode()),
                                                          FastString("пятая", "пятая".hashCode()),), dataFile(File("2.txt"))))
        assertEquals(false, diffFast(mutableListOf(FastString("первая", "первая".hashCode()),
                                                           FastString("первая", "первая".hashCode()),
                                                           FastString("третья", "третья".hashCode()),
                                                           FastString("четвертая", "четвертая".hashCode()),
                                                           FastString("пятая", "пятая".hashCode()),), dataFile(File("2.txt"))))
        assertEquals(false, diffFast(mutableListOf(FastString("первая", "первая".hashCode()),
                                                           FastString("вторая", "вторая".hashCode()),
                                                           FastString("третья", "третья".hashCode()),
                                                           FastString("четвертая", "четвертая".hashCode()),
                                                           FastString("шестая", "шестая".hashCode()),), dataFile(File("1.txt"))))
    }

    @Test
    fun testdiff() {
        assertEquals(ANSI_RED + "-- a \n" + ANSI_RESET + ANSI_GREEN + "++ b \n" + ANSI_RESET,
            diff(mutableListOf(FastString("a", "a".hashCode())),
                 mutableListOf(FastString("b", "b".hashCode()))))
        assertEquals("== a \n",
            diff(mutableListOf(FastString("a", "a".hashCode())),
                 mutableListOf(FastString("a", "a".hashCode()))))
        assertEquals(ANSI_RED + "-- a \n" + ANSI_RESET + "== b \n" + ANSI_GREEN + "++ a \n" + ANSI_RESET,
            diff(mutableListOf(FastString("a", "a".hashCode()), FastString("b", "b".hashCode())),
                 mutableListOf(FastString("b", "b".hashCode()), FastString("a", "a".hashCode()))))
    }


}
