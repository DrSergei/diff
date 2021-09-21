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
    val input = arguments.input
    val output = arguments.output
    val options = arguments.options
    val pathFile1 = arguments.paths[0]
    val pathFile2 = arguments.paths[1]
    when (input) {
        Input.NULL -> {
            when(output) {
                Output.HELP -> {
                    println("Справка")
                    println("Утилита предназначена для сравнения файлов")
                    println("Ключи -h или --help вызов справки")
                    println("Ключи -q или --brief быстрая проверка на совпадение")
                    println("Ключи -f или --file ввод аргументов командной строки из файла")
                    println("Ключи -c или --console ввод файлов из консоли")
                    println("Ключи -s или --space убирают пробелы в начале строки")
                    println("Ключи -i или --ignore не учитывают регистр строки")
                    println("Ключ -- конец ввода опций")
                    println("Утилита предназначена для сравнения двух текстовых файлов")
                }
                Output.BRIEF -> return false
                Output.DIFF -> return false
                Output.NULL -> return false
            }
        }
        Input.CONSOLE -> {
            when (output) {
                Output.HELP -> return false
                Output.BRIEF -> inputConsole(true, options)
                Output.DIFF -> inputConsole(false, options)
                Output.NULL -> return false
            }
        }
        Input.COMMANDLINE ->{
            when (output) {
                Output.HELP -> return false
                Output.BRIEF -> inputCommandLine(true, pathFile1, pathFile2, options)
                Output.DIFF -> inputCommandLine(false, pathFile1, pathFile2, options)
                Output.NULL -> return false
            }
        }
        Input.FILE -> {
            when (output) {
                Output.HELP -> return false
                Output.BRIEF -> return false
                Output.DIFF -> return false
                Output.NULL -> inputFile(pathFile1)
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
fun inputCommandLine(brief : Boolean, pathFile1 : String, pathFile2 : String, options: List<String.() -> String>) : Boolean{
    val file1 = File(pathFile1)
    val file2 = File(pathFile2)
    if (checkFile(file1) && checkFile(file2)) {
        report(Message.DIFF_FILE, listOf(file1.name, file2.name))
        val textFile1 = dataFile(file1, options)
        val textFile2 = dataFile(file2, options)
        if (brief)
            println(diffFast(textFile1, textFile2))
        else
            println(diff(textFile1, textFile2))
        return true
    } else
        return false
}

/**
 * Функция работы с консолью.
 *
 * Считывает имена файлов из консоли.
 */
fun inputConsole(brief : Boolean, options : List<String.() -> String>) : Boolean {
    val pathFile1 = readLine()
    val pathFile2 = readLine()
    if (pathFile1 != null && pathFile2 != null) {
        val file1 = File(pathFile1)
        val file2 = File(pathFile2)
        if (checkFile(file1) && checkFile(file2)) {
            report(Message.DIFF_FILE, listOf(file1.name, file2.name))
            val textFile1 = dataFile(file1, options)
            val textFile2 = dataFile(file2, options)
            if (brief)
                println(diffFast(textFile1, textFile2))
            else
                println(diff(textFile1, textFile2))
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