// Пакет реализует различные сценарии работы с пользователем и предобработку переданных данных.
package frontend

// Стандартная библиотека.
import java.io.File

// Собственные пакеты.
import backend.*
import parser.*

// Проверка адекватности переданного файла(существования, расширения и прав доступа).
fun checkFile(file : File) : Boolean {
    if (!file.exists()) {
        println("Нет файла ${file.absolutePath}")
        return false
    }
    if (file.extension != "txt") {
        println("${file.name} не текстовый файл")
        return false
    }
    if (!file.canRead()) {
        println("${file.name} не может быть прочитан")
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

// Служебное сообщение.
fun message(file1: File, file2 : File) {
    println(ANSI_RED + "-- " + file1.name + ANSI_RESET)
    println(ANSI_GREEN + "++ " + file2.name + ANSI_RESET)
    println("Вывод")
}

// Ввод имен файлов сравнения через аргументы командной строки.
fun inputCommandLine(brief : Boolean, pathFile1 : String, pathFile2 : String, options: MutableList<String.() -> String>) : Boolean{
    val file1 = File(pathFile1)
    val file2 = File(pathFile2)
    if (checkFile(file1) && checkFile(file2)) {
        message(file1, file2)
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
    val pathFile1 = readLine()!! //обязательно строка
    val pathFile2 = readLine()!! //обязательно строка
    val file1 = File(pathFile1)
    val file2 = File(pathFile2)
    if (checkFile(file1) && checkFile(file2)) {
        message(file1, file2)
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

// Обрабатывает каждую строку файла, как аргументы командной строки.
fun inputFile(pathFile : String) : Boolean{
    val buf = File(pathFile)
    if (checkFile(buf)) {
        // Построчно запускает парсер.
        for(line in buf.readLines()) {
            var args = Array(0) {""}
            for (key in line.split(" "))
                args += key
            if (!parser(args)) {
                return false
            }
        }
    } else
        return false
    return true
}