// Файл тестов.

// Стандартная библиотека.
import kotlin.test.*
import java.io.*

// Собственные пакеты.
import frontend.*
import backend.*
import parser.*
import style.*

internal class TestParser {

    @Test
    fun testparser() {
        assertEquals(false, parser(listOf("-f", "--brief", "--help", "--", "text\\test\\1.txt")) != null)
        assertEquals(true, parser(listOf("-h", "--")) != null)
        assertEquals(true, parser(listOf("--brief", "--", "text\\test\\1.txt", "text\\test\\2.txt")) != null)
        assertEquals(true, parser(listOf("--file", "--", "text\\test\\1.txt", "text\\test\\2.txt")) != null)
        assertEquals(false, parser(listOf("--help", "-i", "--", "text\\test\\1.txt", "text\\test\\2.txt")) != null)
        assertEquals(true, parser(listOf("--", "text\\test\\1.txt", "text\\test\\2.txt")) != null)
        assertEquals(true, parser(listOf("-q", "--space","--", "text\\test\\1.txt", "text\\test\\2.txt")) != null)
        assertEquals(true, parser(listOf("--ignore", "-s", "-q", "--", "text\\test\\1.txt", "text\\test\\2.txt")) != null)
        assertEquals(false, parser(listOf("-f", "-help", "--", "text\\test\\1.txt", "text\\test\\3.txt")) != null)
        assertEquals(false, parser(listOf("-h", "--help", "--")) != null)
    }
}

internal class TestFrontend {

    @Test
    fun testcheckFile() {
        assertEquals(true, checkFile(File("text\\test\\1.txt")))
        assertEquals(true, checkFile(File("text\\test\\2.txt")))
        assertEquals(true, checkFile(File("text\\test\\3.txt")))
        assertEquals(false, checkFile(File("abc")))
        assertEquals(true, checkFile(File("README.MD")))
        assertEquals(false, checkFile(File("xxx")))
        assertEquals(true, checkFile(File("gradlew.bat")))
        assertEquals(true, checkFile(File("src\\test\\kotlin\\Test.kt")))
        assertEquals(true, checkFile(File("src\\main\\kotlin\\main.kt")))
    }

    @Test
    fun testdataFile() {
        assertEquals(mutableListOf(FastString("первая", "первая".hashCode()),
                                   FastString("первая", "первая".hashCode()),
                                   FastString("третья", "третья".hashCode()),
                                   FastString("четвертая", "четвертая".hashCode()),
                                   FastString("шестая", "шестая".hashCode()),), dataFile(File("text\\test\\1.txt"), mutableListOf()))
        assertEquals(mutableListOf(FastString("первая", "первая".hashCode()),
                                   FastString("вторая", "вторая".hashCode()),
                                   FastString("третья", "третья".hashCode()),
                                   FastString("четвертая", "четвертая".hashCode()),
                                   FastString("пятая", "пятая".hashCode()),), dataFile(File("text\\test\\2.txt"), mutableListOf()))
        assertEquals(mutableListOf(FastString("Первая", "Первая".hashCode()),
                                   FastString("Вторая", "Вторая".hashCode()),
                                   FastString("Третья", "Третья".hashCode()),
                                   FastString("Четвертая", "Четвертая".hashCode()),
                                   FastString("Пятая", "Пятая".hashCode()),), dataFile(File("text\\test\\3.txt"), mutableListOf()))
    }

    @Test
    fun testistributionInput() {
        assertEquals(false, distributionInput(Arguments(Input.FILE, Output.BRIEF, mutableListOf(), "","")))
        assertEquals(true, distributionInput(Arguments(Input.COMMANDLINE, Output.BRIEF, mutableListOf(), "","")))
        assertEquals(false, distributionInput(Arguments(Input.NULL, Output.BRIEF, mutableListOf(), "","")))
        assertEquals(false, distributionInput(Arguments(Input.FILE, Output.DIFF, mutableListOf(), "","")))
        assertEquals(false, distributionInput(Arguments(Input.CONSOLE, Output.HELP, mutableListOf(), "","")))
    }
}

internal class TestBackend {

    @Test
    fun testequals() {
        assertEquals(true, equals(FastString("aaa", "aaa".hashCode()), FastString("aaa", "aaa".hashCode())))
        assertEquals(false, equals(FastString("a", "a".hashCode()), FastString("b", "b".hashCode())))
        assertEquals(false, equals(FastString("abc", "abc".hashCode()), FastString("cba", "cba".hashCode())))
        assertEquals(true, equals(FastString("я тут", "я тут".hashCode()), FastString("я тут", "я тут".hashCode())))
        assertEquals(true, equals(FastString("i am here", "i am here".hashCode()), FastString("i am here", "i am here".hashCode())))
        assertEquals(false, equals(FastString("ccc", "ccc".hashCode()), FastString("ссс", "ссс".hashCode()))) // раскладки
        assertEquals(true, equals(FastString("omg", "omg".hashCode()), FastString("omg", "omg".hashCode())))
        assertEquals(false, equals(FastString("XXX", "XXX".hashCode()), FastString("yyy", "yyy".hashCode())))
        assertEquals(false, equals(FastString(" space", " space".hashCode()), FastString("space", "space".hashCode())))
        assertEquals(true, equals(FastString("123", "123".hashCode()), FastString("123", "123".hashCode())))
    }

    @Test
    fun testdiffFast() {
        assertEquals("Файлы совпадают: true.", diffFast(mutableListOf(FastString("первая", "первая".hashCode()),
                                                                       FastString("вторая", "вторая".hashCode()),
                                                                       FastString("третья", "третья".hashCode()),
                                                                       FastString("четвертая", "четвертая".hashCode()),
                                                                       FastString("пятая", "пятая".hashCode()),), dataFile(File("text\\test\\2.txt"), mutableListOf())))
        assertEquals("Файлы совпадают: false.", diffFast(mutableListOf(FastString("первая", "первая".hashCode()),
                                                                      FastString("первая", "первая".hashCode()),
                                                                      FastString("третья", "третья".hashCode()),
                                                                      FastString("четвертая", "четвертая".hashCode()),
                                                                      FastString("пятая", "пятая".hashCode()),), dataFile(File("text\\test\\2.txt"), mutableListOf())))
        assertEquals("Файлы совпадают: false.", diffFast(mutableListOf(FastString("первая", "первая".hashCode()),
                                                                      FastString("вторая", "вторая".hashCode()),
                                                                      FastString("третья", "третья".hashCode()),
                                                                      FastString("четвертая", "четвертая".hashCode()),
                                                                      FastString("шестая", "шестая".hashCode()),), dataFile(File("text\\test\\1.txt"), mutableListOf())))
        assertEquals("Файлы совпадают: true.", diffFast(mutableListOf(FastString("Первая", "Первая".hashCode()),
                                                                       FastString("Вторая", "Вторая".hashCode()),
                                                                       FastString("Третья", "Третья".hashCode()),
                                                                       FastString("Четвертая", "Четвертая".hashCode()),
                                                                       FastString("Пятая", "Пятая".hashCode()),), dataFile(File("text\\test\\3.txt"), mutableListOf())))
        assertEquals("Файлы совпадают: true.", diffFast(mutableListOf(FastString("первая", "первая".hashCode()),
                                                                       FastString("вторая", "вторая".hashCode()),
                                                                       FastString("третья", "третья".hashCode()),
                                                                       FastString("четвертая", "четвертая".hashCode()),
                                                                       FastString("пятая", "пятая".hashCode()),), dataFile(File("text\\test\\3.txt"), mutableListOf({this.toLowerCase()}))))
    }


    @Test
    fun testdiff() {
        val str1 = ANSI_GREEN + "Добавлено строк: 1 \n" + ANSI_RESET +
                  ANSI_RED + "Удалено строк: 1 \n" + ANSI_RESET +
                  "Сохранено строк: 0 \n" +
                  ANSI_RED + "-- a \n" + ANSI_RESET +
                  ANSI_GREEN + "++ b \n" + ANSI_RESET
        assertEquals(str1, diff(mutableListOf(FastString("a", "a".hashCode())),
                                mutableListOf(FastString("b", "b".hashCode()))))
        val str2 = ANSI_GREEN + "Добавлено строк: 0 \n" + ANSI_RESET +
                   ANSI_RED + "Удалено строк: 0 \n" + ANSI_RESET +
                   "Сохранено строк: 1 \n" +
                   "== a \n"
        assertEquals(str2, diff(mutableListOf(FastString("a", "a".hashCode())),
                                mutableListOf(FastString("a", "a".hashCode()))))
        val str3 = ANSI_GREEN + "Добавлено строк: 1 \n" + ANSI_RESET +
                   ANSI_RED + "Удалено строк: 1 \n" + ANSI_RESET +
                   "Сохранено строк: 1 \n" +
                   ANSI_RED + "-- a \n" + ANSI_RESET +
                   "== b \n" +
                   ANSI_GREEN + "++ a \n" + ANSI_RESET
        assertEquals(str3, diff(mutableListOf(FastString("a", "a".hashCode()), FastString("b", "b".hashCode())),
                                mutableListOf(FastString("b", "b".hashCode()), FastString("a", "a".hashCode()))))
        val str4 = ANSI_GREEN + "Добавлено строк: 1 \n" + ANSI_RESET +
                   ANSI_RED + "Удалено строк: 1 \n" + ANSI_RESET +
                   "Сохранено строк: 1 \n" +
                   "== b \n" +
                   ANSI_RED + "-- b \n" + ANSI_RESET +
                   ANSI_GREEN + "++ a \n" + ANSI_RESET
        assertEquals(str4, diff(mutableListOf(FastString("b", "b".hashCode()), FastString("b", "b".hashCode())),
                                mutableListOf(FastString("b", "b".hashCode()), FastString("a", "a".hashCode()))))
        val str5 = ANSI_GREEN + "Добавлено строк: 1 \n" + ANSI_RESET +
                ANSI_RED + "Удалено строк: 1 \n" + ANSI_RESET +
                "Сохранено строк: 1 \n" +
                ANSI_RED + "-- a \n" + ANSI_RESET +
                "== b \n" +
                ANSI_GREEN + "++ b \n" + ANSI_RESET
        assertEquals(str5, diff(mutableListOf(FastString("a", "a".hashCode()), FastString("b", "b".hashCode())),
                                mutableListOf(FastString("b", "b".hashCode()), FastString("b", "b".hashCode()))))
    }

    @Test
    fun testdistanceLevenshtein() {
        assertEquals("Расстаяние Леванштейна между файлами: 2.", distanceLevenshtein(mutableListOf(FastString("a", "a".hashCode()), FastString("b", "b".hashCode())),
            mutableListOf(FastString("b", "b".hashCode()), FastString("b", "b".hashCode()))))
        assertEquals("Расстаяние Леванштейна между файлами: 2.", distanceLevenshtein(mutableListOf(FastString("b", "b".hashCode()), FastString("b", "b".hashCode())),
            mutableListOf(FastString("b", "b".hashCode()), FastString("a", "a".hashCode()))))
        assertEquals("Расстаяние Леванштейна между файлами: 2.", distanceLevenshtein(mutableListOf(FastString("a", "a".hashCode()), FastString("b", "b".hashCode())),
            mutableListOf(FastString("b", "b".hashCode()), FastString("a", "a".hashCode()))))
        assertEquals("Расстаяние Леванштейна между файлами: 0.", distanceLevenshtein(mutableListOf(FastString("a", "a".hashCode())),
            mutableListOf(FastString("a", "a".hashCode()))))
        assertEquals("Расстаяние Леванштейна между файлами: 2.", distanceLevenshtein(mutableListOf(FastString("a", "a".hashCode())),
            mutableListOf(FastString("b", "b".hashCode()))))

    }
}

internal class TestStyle {
    @Test
    fun testANSI() {
        assertEquals("\u001B[0m", ANSI_RESET)
        assertEquals("\u001B[31m", ANSI_RED)
        assertEquals("\u001B[32m", ANSI_GREEN)
    }

    @Test
    fun testmessage() {
        assertEquals(true, Message.values().size == 12)
    }
}