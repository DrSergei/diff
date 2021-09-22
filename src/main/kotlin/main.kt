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
                println(report(Message.ERROR_LOGIC))
            else
                // Проверка логичности ввода аргументов и запуск сценариев
                if (!distributionInput(arguments))
                    println(report(Message.ERROR_LOGIC))
        } catch (e : Exception) {
            println(report(Message.ERROR_LOGIC))
        }
    }
    println(report(Message.WORK_TIME, time))
}