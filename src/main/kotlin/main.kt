
import java.util.*

import kotlin.math.*

// строит таблицу для рассчета расстаяния Леванштейна через рекуррентную формулу
fun createTable(str1 : String, str2 : String) : Array<Array<Int>> {
    val table: Array<Array<Int>> = Array(str1.length + 1, { Array(str2.length + 1, {it})})
    for (i in 0..str1.length)
        table[i][0] = i
    for (row in 1..str1.length) {
        for (column in 1..str2.length) {
            //Рассмотрение случая совпадения значений или нет т.к. у нас нет замена, а только вставки и удаления
            if (str1[row - 1] == str2[column - 1]) {
                table[row][column] = minOf(table[row - 1][column] + 1,table[row][column - 1] + 1, table[row - 1][column - 1])
            } else {
                table[row][column] = minOf(table[row - 1][column] + 1,table[row][column - 1] + 1)
            }
        }
    }
    return table
}

// построение пути(редакционного предписания)
fun createPath(table : Array<Array<Int>>, str1: String, str2: String) : String {
    var row = str1.length
    var column = str2.length
    var result = ""
    while ((row != 0) && (column != 0)) {
        //Рассмотрение случая совпадения значений или нет т.к. у нас нет замена, а только вставки и удаления
        if (str1[row - 1] == str2[column - 1]) {
            if (table[row - 1][column] + 1 == minOf(table[row - 1][column] + 1, table[row][column - 1] + 1, table[row - 1][column - 1])) {
                result += "d"
                row--;
            } else if (table[row][column - 1] + 1 == minOf(table[row - 1][column] + 1, table[row][column - 1] + 1, table[row - 1][column - 1])) {
                result += "i"
                column--;
            } else {
                result += "m"
                row--;
                column--;
            }
        } else {
            if (table[row - 1][column] + 1 == minOf(table[row - 1][column] + 1, table[row][column - 1] + 1)) {
                result += "d"
                row--;
            } else {
                result += "i"
                column--;
            }
        }
    }
    if (row == 0) {
        while (column != 0) {
            result += "i"
            column--;
        }
        return result
    }
    if (column == 0) {
        while (row != 0) {
            result += "d"
            row--;
        }
        return result
    }
    return "";
}

//главная функция сравнения (надо дописать расшифровку редакционного предписания)
fun diff(file1 : String, file2 : String) {
    val buf = createPath(createTable(file1, file2), file1, file2)
    print(buf)
}


fun main(args: Array<String>) {

    // Работа с командной строкой или консолью.
    //Сделать работу с файлами, а не строками
    if(args.isNotEmpty()) {
        println("Командная строка")
        if (args.size != 2) {
            print("Неверное число аргументов")
        } else {
            val file1 = args[0]
            val file2 = args[1]
            diff(file1, file2)
        }
    } else {
        println("Консоль")
        val file1 = readLine()!!
        val file2 = readLine()!!
        diff(file1, file2)
    }
}