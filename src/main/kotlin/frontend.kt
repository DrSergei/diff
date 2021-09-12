package frontend

// Стандартная библиотека
import backend.*
import java.io.File

// Собственные пакеты

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
    if (!file.canRead()) {
        println("${file.name} не может быть прочитан")
        return false
    }
    return true
}

// Считывает строки из файла
fun dataFile(file : File) : MutableList<FastString> {
    val textFile = MutableList(0) { FastString("", "".hashCode()) }
    for (line in file.readLines()) {
        textFile.add(FastString(line, line.hashCode()))
    }
    return textFile
}

//Ввод имен файлов сравнения через аргументы командной строки
fun inputCommandLine(brief : Boolean, pathFile1 : String, pathFile2 : String) : Boolean{
    val file1 = File(pathFile1)
    val file2 = File(pathFile2)
    if (checkFile(file1) && checkFile(file2)) {
        println(ANSI_RED + "-- " + file1.name + ANSI_RESET)
        println(ANSI_GREEN + "++ " + file2.name + ANSI_RESET)
        println()
        val textFile1 = dataFile(file1)
        val textFile2 = dataFile(file2)
        if (brief)
            print(diffFast(textFile1, textFile2))
        else
            print(diff(textFile1, textFile2))
        return true
    } else
        return false
}

//Ввод имен файлов сравнения из консоли
fun inputConsole(brief : Boolean) : Boolean {
    val pathFile1 = readLine()!! //обязательно строка
    val pathFile2 = readLine()!! //обязательно строка
    val file1 = File(pathFile1)
    val file2 = File(pathFile2)
    if (checkFile(file1) && checkFile(file2)) {
        println(ANSI_RED + "-- " + file1.name + ANSI_RESET)
        println(ANSI_GREEN + "++ " + file2.name + ANSI_RESET)
        println()
        val textFile1 = dataFile(file1)
        val textFile2 = dataFile(file2)
        if (brief)
            print(diffFast(textFile1, textFile2))
        else
            print(diff(textFile1, textFile2))
        return true
    } else
        return false
}

// Если вкратце, то файл читается построчно. В каждой строке должны быть аргументы командной строки.
fun inputFile(pathFile : String) : Boolean{
    val buf = File(pathFile)
    if (checkFile(buf)) {
        for(line in buf.readLines()) {
            if (!parser1(line.split(" ") as Array<String>)) {
                return false
            }
        }
    } else
        return false

    return true
}

// Перечисления сценариев ввода и вывода для парсера
enum class Input {
    NULL, FILE, COMMANDLINE, CONSOLE
}

enum class Output {
    NULL, HELP, BRIEF, DIFF
}

//Разбирает аргументы командной строки и реализует выбор дальнейшего сценария работы
fun parser1(args : Array<String>) : Boolean {
    var input = Input.COMMANDLINE
    var output = Output.DIFF
    //val trim : String.() -> String = {  this.trim() }
    //val ignore : String.() -> String = {  this.toLowerCase() }
    //var option = MutableList<String.() -> String>(0) {trim}
    var pathFile1 = ""
    var pathFile2 = ""
    for (index in args.indices) {
        when (args[index]) {
            "--" -> {
                if (index + 2 < args.size) {
                    pathFile1 = args[index + 1]
                    pathFile2 = args[index + 2]
                } else if (index + 1 < args.size) {
                    pathFile1 = args[index + 1]
                }
                break
            }
            "-h" -> {
                if (output == Output.DIFF && input == Input.COMMANDLINE) {
                    output = Output.HELP
                    input = Input.NULL
                }
                else
                    return false
            }
            "--help" -> {
                if (output == Output.DIFF && input == Input.COMMANDLINE) {
                    output = Output.HELP
                    input = Input.NULL
                }
                else
                    return false
            }
            "-q" -> {
                if (output == Output.DIFF && input != Input.NULL)
                    output = Output.BRIEF
                else
                    return false
            }
            "--brief" -> {
                if (output == Output.DIFF && input != Input.NULL)
                    output = Output.BRIEF
                else
                    return false
            }
            "-f" -> {
                if (input == Input.COMMANDLINE && output == Output.DIFF) {
                    input = Input.FILE
                    output = Output.NULL
                }
                else
                    return false
            }
            "--file" -> {
                if (input == Input.COMMANDLINE && output == Output.DIFF) {
                    input = Input.FILE
                    output = Output.NULL
                }
                else
                    return false
            }
            "-c" -> {
                if (input == Input.COMMANDLINE && output != Output.HELP)
                    input = Input.CONSOLE
                else
                    return false
            }
            "--console" -> {
                if (input == Input.COMMANDLINE && output != Output.HELP)
                    input = Input.CONSOLE
                else
                    return false
            }
            else -> {
                println("Неверный аргумент ${args[index]}")
                return false
            }
        }
    }
    when (input) {
        Input.NULL -> {
            println("Справка")
        }
        Input.CONSOLE -> {
            when (output) {
                Output.HELP -> {
                    return false
                }
                Output.BRIEF -> {
                    inputConsole(true)
                }
                Output.DIFF -> {
                    inputConsole(false)
                }
            }
        }
        Input.COMMANDLINE ->{
            when (output) {
                Output.HELP -> {
                    return false
                }
                Output.BRIEF -> {
                    inputCommandLine(true, pathFile1, pathFile2)
                }
                Output.DIFF -> {
                    inputCommandLine(false, pathFile1, pathFile2)
                }
            }
        }
        Input.FILE -> {
            when (output) {
                Output.HELP -> {
                    return false
                }
                Output.BRIEF -> {
                    return false
                }
                Output.DIFF -> {
                    inputFile(pathFile1)
                }
            }
        }
    }
    return true
}


