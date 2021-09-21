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
    HELP_INFO // Справка и информация о программе
}

/**
 * Служебная функция.
 *
 * Информирует пользователя об ошибках.
 */
fun report(message: Message, obj : Any = "") {
    when (message) {
        Message.INVALID_ARGUMENTS -> println("Ошибка в аргументах $obj.")
        Message.MISSING_FILE -> println("Нет файла или директории $obj.")
        Message.INVALID_EXTENSION -> println("Не файл $obj.")
        Message.ERROR_READ -> println("Ошибка на чтение $obj.")
        Message.ERROR_LOGIC -> println("Произошла ошибка при обработке аргументов.")
        Message.DIFF_FILE -> {
            if (obj is List<*> && obj.size == 2) {
                println(ANSI_RED + "-- " + "${obj.first()}" + ANSI_RESET)
                println(ANSI_GREEN + "++ " + "${obj.last()}" + ANSI_RESET)
                println("Вывод")
            }
        }
        Message.WORK_TIME -> println("Время работы составило $obj мс.")
        Message.HELP_INFO -> {
            println("Справка")
            println("Утилита предназначена для сравнения файлов")
            println("Ключи -h или --help вызов справки")
            println("Ключи -q или --brief быстрая проверка на совпадение")
            println("Ключи -d или --distance вывод расстояния Леванштейна для файлов(мера похожести)")
            println("Ключи -f или --file ввод аргументов командной строки из файла")
            println("Ключи -c или --console ввод файлов из консоли")
            println("Ключи -s или --space убирают пробелы в начале строки")
            println("Ключи -i или --ignore не учитывают регистр строки")
            println("Ключ -- конец ввода опций")
            println("Утилита предназначена для сравнения двух текстовых файлов")
        }
    }
}

