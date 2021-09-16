// Главный файл и входная точка

// Стандартная библиотека
import java.lang.Exception
import kotlin.system.measureTimeMillis

// Собственные пакеты
import parser.*

fun main(args: Array<String>) {
    val time = measureTimeMillis {
        try {
            if (!parser(args))
                print("Попробуйте переписать аргументы")
        } catch (e : Exception) {
            println("Посмотрите сообщения об ошибках, попробуйте перезапустить программу")
        }
    }
    println("Время работы составило $time мс")
}