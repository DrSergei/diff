// Пакет реализует основной и быстрый алгоритм сравнения. Также вспомогательные функции и дата-класс для хранения данных
package backend

// Стандартная библиотека.

// Собственные пакеты.

// Стили вывода.
const val ANSI_RESET = "\u001B[0m"
const val ANSI_RED = "\u001B[31m"
const val ANSI_GREEN = "\u001B[32m"

// Данные.
data class FastString(val data : String, val hash : Int)

// Функция сравнения строк.
fun equals(str1 : FastString, str2 : FastString) : Boolean {
    if (str1.hash != str2.hash)
        return false
    else
        return str1.data == str2.data
}

// Строит таблицу для рассчета расстаяния Леванштейна через рекуррентную формулу,
// рассчитывая все с учетом стоимости каждой операции(добавить возможность корректирования стоимости операции).
fun createTable(textFile1 : MutableList<FastString>, textFile2 : MutableList<FastString>) : MutableList<MutableList<Int>> {
    val table: MutableList<MutableList<Int>> = MutableList(textFile1.size + 1) { MutableList(textFile2.size + 1) { it } }
    for (i in 0..textFile1.size)
        table[i][0] = i
    for (row in 1..textFile1.size) {
        for (column in 1..textFile2.size) {
            // Рассмотрение случая совпадения значений или нет т.к. у нас нет замена, а только вставки и удаления.
            if (equals(textFile1[row - 1], textFile2[column - 1]))
                table[row][column] = minOf(table[row - 1][column] + 1,table[row][column - 1] + 1, table[row - 1][column - 1])
            else
                table[row][column] = minOf(table[row - 1][column] + 1,table[row][column - 1] + 1)
        }
    }
    return table
}

// Функция построения пути(редакционного предписания).
// Смотрит с конца на состояние с учетом цен и того что замены быть не может, восстанавливая наиболее выгодный ход.
fun createPath(table : MutableList<MutableList<Int>>, textFile1 : MutableList<FastString>, textFile2 : MutableList<FastString>) : List<String> {
    var row = textFile1.size
    var column = textFile2.size
    val result = mutableListOf("")
    while ((row != 0) && (column != 0)) {
        // Рассмотрение случая совпадения значений или нет т.к. у нас нет замена, а только вставки и удаления, выбираем потом наименьший.
        if (equals(textFile1[row - 1], textFile2[column - 1])) {
            when {
                table[row][column - 1] + 1 == minOf(table[row - 1][column] + 1, table[row][column - 1] + 1, table[row - 1][column - 1]) -> {
                    result.add(ANSI_GREEN + "++ ${textFile2[column - 1].data} \n" + ANSI_RESET)
                    column--
                }
                table[row - 1][column] + 1 == minOf(table[row - 1][column] + 1, table[row][column - 1] + 1, table[row - 1][column - 1]) -> {
                    result.add(ANSI_RED + "-- ${textFile1[row - 1].data} \n" + ANSI_RESET)
                    row--
                }
                else -> {
                    result.add("== ${textFile1[row - 1].data} \n")
                    row--
                    column--
                }
            }
        } else {
            when {
                table[row][column - 1] + 1 == minOf(table[row - 1][column] + 1, table[row][column - 1] + 1) -> {
                    result.add(ANSI_GREEN + "++ ${textFile2[column - 1].data} \n" + ANSI_RESET)
                    column--
                }
                else -> {
                    result.add(ANSI_RED + "-- ${textFile1[row - 1].data} \n" + ANSI_RESET)
                    row--
                }
            }
        }
    }
    // Когда уперлись в край таблицы, идем вдоль него.
    if (row == 0) {
        while (column != 0) {
            result.add(ANSI_GREEN + "++ ${textFile2[column - 1].data} \n" + ANSI_RESET)
            column--
        }
        return result.reversed() // так как с конца надо развернуть
    }
    if (column == 0) {
        while (row != 0) {
            result.add(ANSI_RED + "-- ${textFile1[row - 1].data} \n" + ANSI_RESET)
            row--
        }
        return result.reversed() // так как с конца надо развернуть
    }
    return emptyList()
}

// Главная функция сравнения, связывает две предыдущие.
fun diff(textFile1 : MutableList<FastString>, textFile2 :MutableList<FastString>) : String {
    val buf = try {
         createPath(createTable(textFile1, textFile2), textFile1, textFile2)
    } catch (e : Exception) {
        println("Скорее всего произошел выход из массива при расчете редакционного предписания, маловероятны проблемы с выделением пямяти")
        return ""
    }
    return buf.joinToString("")
}

// Быстрое построчное сравнений.
fun diffFast(textFile1 : MutableList<FastString>, textFile2 :MutableList<FastString>) : String {
    if (textFile1.size == textFile2.size) {
        if (textFile1 == textFile2)
            return "Файлы совпадают"
        else
            return "Файлы различны"
    } else
        return "Файлы различны"
}