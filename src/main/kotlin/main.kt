// Главный файл и входная точка

// Стандартная библиотека
import frontend.distributionInput
import java.lang.Exception
import kotlin.system.measureTimeMillis

// Собственные пакеты
import parser.*
import style.*

fun main(args: Array<String>) {
    val time = measureTimeMillis {
        try {
            // Парсинг аргументов командной строки
            val arguments = parser(args.toList())
            if (arguments == null)
                report(Message.ERROR_LOGIC)
            else
                // Проверка логичности ввода аргументов и запуск сценариев
                if (!distributionInput(arguments))
                    report(Message.ERROR_LOGIC)
        } catch (e : Exception) {
            report(Message.ERROR_LOGIC)
        }
    }
    report(Message.WORK_TIME, time)
}