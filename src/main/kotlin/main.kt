import java.io.*

// Строит таблицу для рассчета расстаяния Леванштейна через рекуррентную формулу,
// рассчитывая все с учетом стоимости каждой операции(добавить возможность корректирования стоимости операции)

fun createTable(str1 : List<String>, str2 : List<String>) : Array<Array<Int>> {
    val table: Array<Array<Int>> = Array(str1.size + 1) { Array(str2.size + 1) { it } }
    for (i in 0..str1.size)
        table[i][0] = i
    for (row in 1..str1.size) {
        for (column in 1..str2.size) {
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
// смотрит с конца на состояние с учетом цен
fun createPath(table : Array<Array<Int>>, str1: List<String>, str2: List<String>) : List<String> {
    var row = str1.size
    var column = str2.size
    val result = mutableListOf("")
    while ((row != 0) && (column != 0)) {
        //Рассмотрение случая совпадения значений или нет т.к. у нас нет замена, а только вставки и удаления
        if (str1[row - 1] == str2[column - 1]) {
            if (table[row][column - 1] + 1 == minOf(table[row - 1][column] + 1, table[row][column - 1] + 1, table[row - 1][column - 1])) {
                result.add("insert ${str2[column - 1]} \n")
                column--
            } else if (table[row - 1][column] + 1 == minOf(table[row - 1][column] + 1, table[row][column - 1] + 1, table[row - 1][column - 1])) {
                result.add("delete ${str1[row - 1]} \n")
                row--
            } else {
                result.add("match ${str1[row - 1]} \n")
                row--
                column--
            }
        } else {
            if (table[row][column - 1] + 1 == minOf(table[row - 1][column] + 1, table[row][column - 1] + 1)) {
                result.add("insert ${str2[column - 1]} \n")
                column--
            } else {
                result.add("delete ${str1[row - 1]} \n")
                row--
            }
        }
    }
    if (row == 0) {
        while (column != 0) {
            result.add("insert ${str2[column - 1]} \n")
            column--
        }
        return result.reversed() // так как с конца надо развернуть
    }
    if (column == 0) {
        while (row != 0) {
            result.add("delete ${str1[row - 1]} \n")
            row--
        }
        return result.reversed() // так как с конца надо развернуть
    }
    return emptyList()
}

//главная функция сравнения (надо дописать расшифровку редакционного предписания)
fun diff(file1 : List<String>, file2 : List<String>) {
    val buf = createPath(createTable(file1, file2), file1, file2)
    print(buf.joinToString(""))
}

fun inputCommandLine(args: Array<String>) {
    println("Командная строка")
    if (args.size != 2) {
        print("Неверное число аргументов")
    } else {
        val pathFile1 = args[0]
        val pathFile2 = args[1]
        val file1 = File(pathFile1)
        val file2 = File(pathFile2)
        if (!file1.exists()) {
            println("Нет файла $pathFile1")
            return
        }
        if (file1.extension != "txt") {
            println("$pathFile1 не текстовый файл")
            return
        }
        if (!file2.exists()) {
            println("Нет файла $pathFile2")
            return
        }
        if (file2.extension != "txt") {
            println("$pathFile2 не текстовый файл")
            return
        }
        val textFile1 = file1.readLines()
        val textFile2 = file2.readLines()
        diff(textFile1, textFile2)
    }
}

fun inputConsole() {
    println("Консоль")
    val pathFile1 = readLine()!! //обязательно строка
    val pathFile2 = readLine()!! //обязательно строка
    val file1 = File(pathFile1)
    val file2 = File(pathFile2)
    if (!file1.exists()) {
        println("Нет файла $pathFile1")
        return
    }
    if (file1.extension != "txt") {
        println("$pathFile1 не текстовый файл")
        return
    }
    if (!file2.exists()) {
        println("Нет файла $pathFile2")
        return
    }
    if (file2.extension != "txt") {
        println("$pathFile2 не текстовый файл")
        return
    }
    val textFile1 = file1.readLines()
    val textFile2 = file2.readLines()
    diff(textFile1, textFile2)
}
fun main(args: Array<String>) {

    // Работа с командной строкой или консолью.
    if(args.isNotEmpty()) {
        inputCommandLine(args)
    } else {
        inputConsole()
    }
}