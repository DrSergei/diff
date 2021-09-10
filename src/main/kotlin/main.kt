// Стандартная библиотека

// Собственные пакеты
import frontend.parser

fun main(args: Array<String>) {
    if (parser(args)) {
        print("хорошечно")
        //что то

    } else {
        print("не хорошечно")
        //что то
    }
}