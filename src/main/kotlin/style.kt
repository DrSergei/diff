/**
 * Пакет для стилей.
 *
 * Поддерживает типовые сообщения, в будущем разные языки.
 */
package style

// Стандартная библиотека.

// Собственые пакеты.

/**
 *  Стили вывода.
 *
 *  Служат для подсветки действий.
 */
const val ANSI_RESET = "\u001B[0m"
const val ANSI_RED = "\u001B[31m"
const val ANSI_GREEN = "\u001B[32m"

/**
 * Типы сообщений.
 *
 * Используются для кодирования типовых сообщений.
 */
enum class Message {
    INVALID_ARGUMENTS, // неверные аргументы
    MISSING_FILE, // нет файла
    INVALID_EXTENSION, // неправильное расширение
    ERROR_READ, // зпрещено чтение
    ERROR_LOGIC, // ошибка при сопостовлении аргументов
    DIFF_FILE, // сравниваемые файлы
    WORK_TIME, // время работы
    HELP_INFO, // Справка и информация о программе
    FATAL_ERROR, // фатальная ошибка
    DIFF_INFO, // вывод сравнения
    BRIEF_INFO, // быстрое сравнение
    DISTANCE_INFO // расстояние Леванштейна
}

/**
 * Служебная функция.
 *
 * Информирует пользователя об ошибках и результатах работы.
 */
fun report(message: Message, obj: Any = ""): String {
    // Узнает регион пользователя и выбирает нужную локализацию
    val y = System.getProperty("user.language")
    if (y == "ru") {
        when (message) {
            Message.INVALID_ARGUMENTS -> return "Ошибка в аргументах $obj."
            Message.MISSING_FILE -> return "Нет файла или директории $obj."
            Message.INVALID_EXTENSION -> return "Не файл $obj."
            Message.ERROR_READ -> return "Ошибка на чтение $obj."
            Message.ERROR_LOGIC -> return "Произошла ошибка при обработке аргументов."
            Message.DIFF_FILE -> {
                if (obj is List<*> && obj.size == 2) {
                    return ANSI_RED + "-- " + "${obj.first()}" + ANSI_RESET + "\n" + ANSI_GREEN + "++ " + "${obj.last()}" + ANSI_RESET + "\n" + "Вывод"
                }
            }
            Message.WORK_TIME -> return "Время работы составило $obj мс."
            Message.HELP_INFO -> {
                return "Справка" + "\n" +
                        "утилита предназначена для сравнения файлов" + "\n" +
                        "Ключи -h или --help вызов справки" + "\n" +
                        "Ключи -q или --brief быстрая проверка на совпадение" + "\n" +
                        "Ключи -d или --distance вывод расстояния Левенштейна для файлов мера похожести " + "\n" +
                        "Ключи -f или --file ввод аргументов командной строки из файла" + "\n" +
                        "Ключи -c или --console ввод файлов из консоли" + "\n" +
                        "Ключи -s или --space убирают пробелы в начале строки" + "\n" +
                        "Ключи -i или --ignore не учитывают регистр строки" + "\n" +
                        "Ключ -- конец ввода опций" + "\n" +
                        "Утилита предназначена для сравнения двух текстовых файлов"
            }
            Message.FATAL_ERROR -> return "Скорее всего произошел выход из массива при расчете редакционного предписания, маловероятны проблемы с выделением пямяти."
            Message.DIFF_INFO -> return "$obj"
            Message.BRIEF_INFO -> return "Файлы совпадают: $obj."
            Message.DISTANCE_INFO -> return "Расстаяние Левенштейна между файлами: $obj."
        }
    } else {
        when (message) {
            Message.INVALID_ARGUMENTS -> return "Error in arguments $obj."
            Message.MISSING_FILE -> return "No file or directory $obj."
            Message.INVALID_EXTENSION -> return "Not a file $obj."
            Message.ERROR_READ -> return "Read error $obj."
            Message.ERROR_LOGIC -> return "An error occurred while processing arguments."
            Message.DIFF_FILE -> {
                if (obj is List<*> && obj.size == 2) {
                    return ANSI_RED + "-- " + "${obj.first()}" + ANSI_RESET + "\n" + ANSI_GREEN + "++ " + "${obj.last()}" + ANSI_RESET + "\n" + "Output"
                }
            }
            Message.WORK_TIME -> return "Working time was $obj ms."
            Message.HELP_INFO -> {
                return "Help" + "\n" +
                        "The utility is designed to compare files" + "\n" +
                        "The -h or --help options call help" + "\n" +
                        "The -q or --brief options quickly check for a match" + "\n" +
                        "The -d or --distance options display the Levenshtein distance for files  measure of similarity " + "\n" +
                        "The -f or --file switches input command line arguments from a file" + "\n" +
                        "The -c or --console switches input files from the console" + "\n" +
                        "The -s or --space switches remove spaces at the beginning of a line" + "\n" +
                        "The -i or --ignore options are not case-sensitive" + "\n" +
                        "Key -- end of option entry" + "\n" +
                        "The utility is designed to compare two text files"
            }
            Message.FATAL_ERROR -> return "Most likely, there was an exit from the array when calculating an editorial prescription, problems with memory allocation are unlikely."
            Message.DIFF_INFO -> return "$obj"
            Message.BRIEF_INFO -> return "The files are the same: $obj."
            Message.DISTANCE_INFO -> return "Levenshtein's distance between files: $obj."
        }
    }
    return ""
}

