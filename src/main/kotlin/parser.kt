// Пакет реализует разбор аргументов командной строки и проверку их правильности. Осуществляет выбор сценария работы.
package parser

// Стандартная библиотека.

// Собственные пакеты.
import style.*

// Перечисления сценариев ввода и вывода для парсера.
enum class Input {
    NULL, FILE, COMMANDLINE, CONSOLE
}

enum class Output {
    NULL, HELP, BRIEF, DIFF
}

// Класс для хранения аргументов переданных пользователем.
data class Arguments(val input: Input, val output: Output, val options : MutableList<String.() -> String>, val paths : List<String>)

// Опции предобработки строки.
val space : String.() -> String = {  this.trim() }
val ignore : String.() -> String = {  this.toLowerCase() }

// Разбирает аргументы командной строки и реализует выбор дальнейшего сценария работы.
fun parser(args : List<String>) : Arguments? {
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
                } else if (index + 1 < args.size)
                    pathFile1 = args[index + 1]
                break
            }
            "-i" -> { // не учитывать регистр
                if ((output == Output.DIFF || output == Output.BRIEF) && input != Input.FILE && input != Input.NULL)
                    options.add(ignore)
                else
                    return null
            }
            "--ignore" -> { // не учитывать регистр
                if ((output == Output.DIFF || output == Output.BRIEF) && input != Input.FILE && input != Input.NULL)
                    options.add(ignore)
                else
                    return null
            }
            "-s" -> { // отбрасывать ведущие пробелы
                if ((output == Output.DIFF || output == Output.BRIEF) && input != Input.FILE && input != Input.NULL)
                    options.add(space)
                else
                    return null
            }
            "--space" -> { // отбрасывать ведущие пробелы
                if ((output == Output.DIFF || output == Output.BRIEF) && input != Input.FILE && input != Input.NULL)
                    options.add(space)
                else
                    return null
            }
            "-h" -> { // вызов справки
                if (output == Output.DIFF && input == Input.COMMANDLINE && options.isEmpty()) {
                    output = Output.HELP
                    input = Input.NULL
                }
                else
                    return null
            }
            "--help" -> { // вызов справки
                if (output == Output.DIFF && input == Input.COMMANDLINE && options.isEmpty()) {
                    output = Output.HELP
                    input = Input.NULL
                }
                else
                    return null
            }
            "-q" -> { // вывод только сравнения(быстрый режим)
                if (output == Output.DIFF && input != Input.NULL && input != Input.FILE)
                    output = Output.BRIEF
                else
                    return null
            }
            "--brief" -> { // вывод только сравнения(быстрый режим)
                if (output == Output.DIFF && input != Input.NULL && input != Input.FILE)
                    output = Output.BRIEF
                else
                    return null
            }
            "-f" -> { // ввод из файла
                if (input == Input.COMMANDLINE && output == Output.DIFF && options.isEmpty()) {
                    input = Input.FILE
                    output = Output.NULL
                }
                else
                    return null
            }
            "--file" -> { // ввод из файла
                if (input == Input.COMMANDLINE && output == Output.DIFF && options.isEmpty()) {
                    input = Input.FILE
                    output = Output.NULL
                }
                else
                    return null
            }
            "-c" -> { // ввод с консоли
                if (input == Input.COMMANDLINE && output != Output.HELP)
                    input = Input.CONSOLE
                else
                    return null
            }
            "--console" -> { // ввод с консоли
                if (input == Input.COMMANDLINE && output != Output.HELP)
                    input = Input.CONSOLE
                else
                    return null
            }
            else -> { // обработка неверных аргументов
                report(Message.INVALID_ARGUMENTS, args[index])
                return null
            }
        }
    }
    return Arguments(input, output, options, listOf(pathFile1, pathFile2))
}