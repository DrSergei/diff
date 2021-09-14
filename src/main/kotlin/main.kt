// Стандартная библиотека

// Собственные пакеты
import frontend.parser1
import java.lang.Exception

fun main(args: Array<String>) {
    try {
        parser1(args)
    } catch (e : Exception) {
        println("Посмотрите сообщения об ошибках, попробуйте перезапустить программу")
    }
}