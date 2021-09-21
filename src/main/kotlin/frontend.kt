/**
 * Пакет для общения с пользователем.
 *
 * Реализует различные сценарии и их выбор.
 */
package frontend

// Стандартная библиотека.
import java.io.File

// Собственные пакеты.
import backend.*
import parser.*
import  style.*
import java.util.*

/**
 * Функция проверки файла.
 *
 * Проверяет существования, расширение и доступ на чтение.
 */
fun checkFile(file : File) : Boolean {
    if (!file.exists()) {
        report(Message.MISSING_FILE, file.absolutePath)
        return false
    }
    if (file.extension == "exe") {
        report(Message.INVALID_EXTENSION, file.name)
        return false
    }
    if (!file.canRead()) {
        report(Message.ERROR_READ, file.name)
        return false
    }
    return true
}

/**
 * Функция чтения данных из файла.
 *
 * Построчно считывает и предобрабатывает строки файла.
 */
fun dataFile(file : File,options : List<String.() -> String>) : MutableList<FastString> {
    val textFile = MutableList(0) { FastString("", "".hashCode()) }
    for (line in file.readLines()) {
        var str = line
        // Применяет опции.
        for (op in options)
            str = str.op()
        textFile.add(FastString(str, str.hashCode()))
    }
    return textFile
}

/**
 * Функция выбора сценария.
 *
 * Проверяет распарсенный вход на адекватность и запускает нужный сценарий.
 */
fun distributionInput(arguments: Arguments) : Boolean{
    // разбор данных(для удобства использования)
    val input = arguments.input
    val output = arguments.output
    val options = arguments.options
    val pathFile1 = arguments.pathFile1
    val pathFile2 = arguments.pathFile2
    when (input) {
        Input.NULL -> {
            when(output) {
                Output.HELP -> report(Message.HELP_INFO)
                Output.BRIEF -> return false
                Output.DIFF -> return false
                Output.NULL -> return false
                Output.DISTANCE -> return false
            }
        }
        Input.CONSOLE -> {
            when (output) {
                Output.HELP -> return false
                Output.BRIEF -> inputConsole( options, brief = true, distance = false)
                Output.DIFF -> inputConsole( options, brief = false, distance = false)
                Output.NULL -> return false
                Output.DISTANCE -> inputConsole( options, brief = false, distance = true)
            }
        }
        Input.COMMANDLINE ->{
            when (output) {
                Output.HELP -> return false
                Output.BRIEF -> inputCommandLine(pathFile1, pathFile2, options, brief = true, distance = false)
                Output.DIFF -> inputCommandLine(pathFile1, pathFile2, options, brief = false, distance = false)
                Output.NULL -> return false
                Output.DISTANCE -> inputCommandLine(pathFile1, pathFile2, options, brief = false, distance = true)
            }
        }
        Input.FILE -> {
            when (output) {
                Output.HELP -> return false
                Output.BRIEF -> return false
                Output.DIFF -> return false
                Output.NULL -> inputFile(pathFile1)
                Output.DISTANCE -> return false
            }
        }
    }
    return true
}

/**
 * Функция работы с командной строкой.
 *
 * Считывает имена файлов из командной строки.
 */
fun inputCommandLine(pathFile1 : String, pathFile2 : String, options: List<String.() -> String> = listOf(), brief : Boolean = false, distance : Boolean = false) : Boolean{
    val file1 = File(pathFile1)
    val file2 = File(pathFile2)
    if (checkFile(file1) && checkFile(file2)) {
        report(Message.DIFF_FILE, listOf(file1.name +" " + Date(file1.lastModified()), file2.name + " " + Date(file1.lastModified())))
        val textFile1 = dataFile(file1, options)
        val textFile2 = dataFile(file2, options)
        when {
            brief -> println(diffFast(textFile1, textFile2))
            distance -> println(distanceLevenshtein(textFile1,textFile2))
            else -> println(diff(textFile1, textFile2))
        }
        return true
    } else
        return false
}

/**
 * Функция работы с консолью.
 *
 * Считывает имена файлов из консоли.
 */
fun inputConsole(options : List<String.() -> String> = listOf(), brief : Boolean = false, distance: Boolean = false) : Boolean {
    val pathFile1 = readLine()
    val pathFile2 = readLine()
    if (pathFile1 != null && pathFile2 != null) {
        val file1 = File(pathFile1)
        val file2 = File(pathFile2)
        if (checkFile(file1) && checkFile(file2)) {
            report(Message.DIFF_FILE, listOf(file1.name, file2.name))
            val textFile1 = dataFile(file1, options)
            val textFile2 = dataFile(file2, options)
            when {
                brief -> println(diffFast(textFile1, textFile2))
                distance -> println(distanceLevenshtein(textFile1,textFile2))
                else -> println(diff(textFile1, textFile2))
            }
            return true
        } else
            return false
    } else {
        report(Message.ERROR_LOGIC)
        return false
    }
}

/**
 * Функция для файлового ввода.
 *
 * Обрабатывает каждую строку файла, как аргументы отдельного вызова утилиты.
 */
fun inputFile(pathFile : String) : Boolean{
    val buf = File(pathFile)
    if (checkFile(buf)) {
        // Построчно запускает парсер.
        for(line in buf.readLines()) {
            val arguments = parser(line.split(" "))
            if (arguments == null)
                report(Message.ERROR_LOGIC)
            else
                if (!distributionInput(arguments))
                    report(Message.ERROR_LOGIC)
        }
    } else
        return false
    return true
}