package frontend

// Стандартная библиотека
import java.io.*


// Собственные пакеты
import backend.*

// Проверка адекватности переданного файла
fun checkFile(file : File) : Boolean {
    if (!file.exists()) {
        println("Нет файла ${file.absolutePath}")
        return false
    }
    if (file.extension != "txt") {
        println("${file.name} не текстовый файл")
        return false
    }
    return true
}

fun dataFile(file : File) : MutableList<FastString> {
    val textFile = MutableList(0) { FastString("", "".hashCode()) }
    for (line in file.readLines()) {
        textFile.add(FastString(line, line.hashCode()))
    }
    return textFile
}

//Ввод имен файлов сравнения через аргументы командной строки
fun inputCommandLine(args: Array<String>) : Boolean{
    val pathFile1 = args[0]
    val pathFile2 = args[1]
    val file1 = File(pathFile1)
    val file2 = File(pathFile2)
    if (checkFile(file1) && checkFile(file2)) {
        println("-- " + file1.name)
        println("++ " + file2.name)
        println()
        val textFile1 = dataFile(file1)
        val textFile2 = dataFile(file2)
        print(diff(textFile1, textFile2))
        return true
    } else
        return false
}

//Ввод имен файлов сравнения из консоли
fun inputConsole() : Boolean {
    val pathFile1 = readLine()!! //обязательно строка
    val pathFile2 = readLine()!! //обязательно строка
    val file1 = File(pathFile1)
    val file2 = File(pathFile2)
    if (checkFile(file1) && checkFile(file2)) {
        println("-- " + file1.name)
        println("++ " + file2.name)
        println()
        val textFile1 = dataFile(file1)
        val textFile2 = dataFile(file2)
        print(diff(textFile1, textFile2))
        return true
    } else
        return false
}

// Если вкратце, то файл читается построчно. В каждой строке должны быть аргументы командной строки.
fun inputFile(args: Array<String>) : Boolean{
    val buf = File(args[1])
    if (checkFile(buf)) {
        for(line in buf.readLines()) {
            if (!parser(line.split(" ") as Array<String>)) {
                return false
            }
        }
    } else
        return false

    return true
}

//Разбирает аргументы командной строки и реализует выбор дальнейшего сценария работы
fun parser(args: Array<String>) : Boolean {
    if(args.isNotEmpty()) {
        if (args.size == 1) {
            if ((args[0] == "-h") || (args[0] == "--help")) {
                println("Справка")
                println("Утилита предоставляет возможность построчного сравнение двух текствовых файлов")
                println("Работа с командной строкой")
                println(" \"\" работа в интерактивном режиме")
                println(" \"file1\" \"file2\" сравнение введенных файлов")
                println(" \"-f или --file\" \"file\" использует каждую строчку в файле как аргументы командной строки")
                println(" \"-h или --help\" вызывает справку")
                //TODO("Написать нормальную справку")
                return true
            } else {
                println("Неверные аргументы командной строки")
                return false
            }
        }
        else if (args.size == 2) {
            if ((args[0] == "-f") || (args[0] == "--file"))
                return inputFile(args)
            else
                return inputCommandLine(args)

        }
        else {
            println("Неверные аргументы командной строки")
            return false
        }
    } else {
        return inputConsole()
    }
}