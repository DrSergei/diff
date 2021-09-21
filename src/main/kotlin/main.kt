// Главный файл и входная точка

// Стандартная библиотека
import frontend.distributionInput
import java.lang.Exception
import kotlin.system.measureTimeMillis

// Собственные пакеты
import parser.*

fun main(args: Array<String>) {
    val time = measureTimeMillis {
        try {
            // Предобработка аргументов командной строки
            val arguments = parser(args.toList())
            if (arguments == null)
                println("Неверные аргменты")
            else
                // Проверка логичности ввода аргументов
                if (!distributionInput(arguments))
                    println("Неверные аргументы")
        } catch (e : Exception) {
            println("Произошла ошибка")
        }
    }
    println("Время работы составило $time мс")
}