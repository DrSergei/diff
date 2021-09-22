/**
 * Пакет для разбора аргументов командной строки.
 *
 * Реализует типы для представления аргументов и функцию их перерабатывающую.
 */
package parser

// Стандартная библиотека.

// Собственные пакеты.
import style.*

/**
 * Сценарии ввода.
 *
 * Используются для представлении логики ввода.
 */
enum class Input {
    NULL, FILE, COMMANDLINE, CONSOLE
}

/**
 * Сценарии вывода.
 *
 * Используются для представлении логики вывода.
 */
enum class Output {
    NULL, HELP, BRIEF, DIFF, DISTANCE
}

/**
 * Класс для хранения аргументов командной строки.
 *
 * Используется для передачи их внутри программы.
 */
data class Arguments(val input: Input, val output: Output, val options : List<String.() -> String>, val pathFile1 : String, val pathFile2: String)

/**
 * Вспомогательные функции.
 *
 * Используются для предобработки ввода.
 */
val space : String.() -> String = {  this.trim() }
val ignore : String.() -> String = {  this.toLowerCase() }

/**
 * Функция парсер.
 *
 * Проверяет, классифицирует и преобразует аргументы командной строки для дальнейшего использования.
 */
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
            "-d" -> { // вывод схожести файлов(расстояние Леванштейна)
                if (output == Output.DIFF && input != Input.NULL && input != Input.FILE)
                    output = Output.DISTANCE
                else
                    return null
            }
            "--distance" -> { // вывод схожести файлов(расстояние Леванштейна)
                if (output == Output.DIFF && input != Input.NULL && input != Input.FILE)
                    output = Output.DISTANCE
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
                println(report(Message.INVALID_ARGUMENTS, args[index]))
                return null
            }
        }
    }
    return Arguments(input, output, options.toList(), pathFile1, pathFile2)
}