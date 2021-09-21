// Пакет реализует различные сценарии работы с пользователем и предобработку переданных данных.
package frontend

// Стандартная библиотека.
import java.io.File

// Собственные пакеты.
import backend.*
import parser.*
import  style.*

// Проверка адекватности переданного файла(существования, расширения и прав доступа).
fun checkFile(file : File) : Boolean {
    if (!file.exists()) {
        report(Message.MISSING_FILE, file.absolutePath)
        return false
    }
    if (file.extension != "txt") {
        report(Message.INVALID_EXTENSION, file.name)
        return false
    }
    if (!file.canRead()) {
        report(Message.ERROR_READ, file.name)
        return false
    }
    return true
}

// Считывает строки из файла и предобрабатывает их.
fun dataFile(file : File,options : MutableList<String.() -> String>) : MutableList<FastString> {
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

// Разбор случаев(верный, покрыт тестами).
fun distributionInput(arguments: Arguments) : Boolean{
    // Переданные вход и выход
    val input = arguments.input
    val output = arguments.output
    // Переданные опции предобработки
    val options = arguments.options
    // Переданные пути до файлов
    val pathFile1 = arguments.paths[0]
    val pathFile2 = arguments.paths[1]
    when (input) {
        Input.NULL -> {
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

// Ввод имен файлов сравнения через аргументы командной строки.
fun inputCommandLine(brief : Boolean, pathFile1 : String, pathFile2 : String, options: MutableList<String.() -> String>) : Boolean{
    val file1 = File(pathFile1)
    val file2 = File(pathFile2)
    if (checkFile(file1) && checkFile(file2)) {
        report(Message.DIFF_FILE, listOf(file1.name, file2.name))
        //message(file1, file2)
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

// Ввод имен файлов сравнения из консоли.
fun inputConsole(brief : Boolean, options : MutableList<String.() -> String>) : Boolean {
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

// Обрабатывает каждую строку файла, как аргументы командной строки.
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