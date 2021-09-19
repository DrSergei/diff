// Пакет реализует разбор аргументов командной строки и проверку их правильности. Осуществляет выбор сценария работы.
package parser

// Стандартная библиотека.

// Собственные пакеты.
import frontend.*

// Перечисления сценариев ввода и вывода для парсера.
enum class Input {
    NULL, FILE, COMMANDLINE, CONSOLE
}

enum class Output {
    NULL, HELP, BRIEF, DIFF
}

// Опции предобработки строки.
val space : String.() -> String = {  this.trim() }
val ignore : String.() -> String = {  this.toLowerCase() }
// Разбирает аргументы командной строки и реализует выбор дальнейшего сценария работы.
fun parser(args : List<String>) : Boolean {
    // вход и выход по умолчанию
    var input = Input.COMMANDLINE
    var output = Output.DIFF
    // пути к файлам
    var pathFile1 = ""
    var pathFile2 = ""
    // опции
    val options = mutableListOf<String.() -> String>()
    // разбор строки
    for (index in args.indices) {
        when (args[index]) {
            "--" -> { // конец ввода опций
                if (index + 2 < args.size) {
                    pathFile1 = args[index + 1]
                    pathFile2 = args[index + 2]
                } else if (index + 1 < args.size) {
                    pathFile1 = args[index + 1]
                }
                break
            }
            "-i" -> { // не учитывать регистр
                if ((output == Output.DIFF || output == Output.BRIEF) && input != Input.FILE && input != Input.NULL)
                    options.add(ignore)
                else
                    return false
            }
            "--ignore" -> { // не учитывать регистр
                if ((output == Output.DIFF || output == Output.BRIEF) && input != Input.FILE && input != Input.NULL)
                    options.add(ignore)
                else
                    return false
            }
            "-s" -> { // отбрасывать ведущие пробелы
                if ((output == Output.DIFF || output == Output.BRIEF) && input != Input.FILE && input != Input.NULL)
                    options.add(space)
                else
                    return false
            }
            "--space" -> { // отбрасывать ведущие пробелы
                if ((output == Output.DIFF || output == Output.BRIEF) && input != Input.FILE && input != Input.NULL)
                    options.add(space)
                else
                    return false
            }
            "-h" -> { // вызов справки
                if (output == Output.DIFF && input == Input.COMMANDLINE && options.isEmpty()) {
                    output = Output.HELP
                    input = Input.NULL
                }
                else
                    return false
            }
            "--help" -> { // вызов справки
                if (output == Output.DIFF && input == Input.COMMANDLINE && options.isEmpty()) {
                    output = Output.HELP
                    input = Input.NULL
                }
                else
                    return false
            }
            "-q" -> { // вывод только сравнения(быстрый режим)
                if (output == Output.DIFF && input != Input.NULL && input != Input.FILE)
                    output = Output.BRIEF
                else
                    return false
            }
            "--brief" -> { // вывод только сравнения(быстрый режим)
                if (output == Output.DIFF && input != Input.NULL && input != Input.FILE)
                    output = Output.BRIEF
                else
                    return false
            }
            "-f" -> { // ввод из файла
                if (input == Input.COMMANDLINE && output == Output.DIFF && options.isEmpty()) {
                    input = Input.FILE
                    output = Output.NULL
                }
                else
                    return false
            }
            "--file" -> { // ввод из файла
                if (input == Input.COMMANDLINE && output == Output.DIFF && options.isEmpty()) {
                    input = Input.FILE
                    output = Output.NULL
                }
                else
                    return false
            }
            "-c" -> { // ввод с консоли
                if (input == Input.COMMANDLINE && output != Output.HELP)
                    input = Input.CONSOLE
                else
                    return false
            }
            "--console" -> { // ввод с консоли
                if (input == Input.COMMANDLINE && output != Output.HELP)
                    input = Input.CONSOLE
                else
                    return false
            }
            else -> { // обработка неверных аргументов
                println("Неверный аргумент ${args[index]}")
                return false
            }
        }
    }
    // Разбор случаев(верный, покрыт тестами).
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