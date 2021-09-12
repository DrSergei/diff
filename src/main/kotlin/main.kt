// Стандартная библиотека

// Собственные пакеты
import frontend.parser1
import java.lang.Exception

fun main(args: Array<String>) {
    try {
        if (parser1(args)) {
            print("хорошечно")
            //что то

        } else {
            print("не хорошечно")
            //что то
        }
    } catch (e : Exception) {
        println("Что - то пошло не так")
    }
}