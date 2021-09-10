package backend

// Стандартная библиотека

// Собственные пакеты

// Данные
data class FastString(val data : String, val hash : Int)

// Функция сравнения строк
fun equals(str1 : FastString, str2 : FastString) : Boolean {
    if (str1.hash != str2.hash) {
        return false
    } else {
        return str1.data == str2.data
    }
}


// Строит таблицу для рассчета расстаяния Леванштейна через рекуррентную формулу,
// рассчитывая все с учетом стоимости каждой операции(добавить возможность корректирования стоимости операции)
fun createTable(textFile1 : MutableList<FastString>, textFile2 : MutableList<FastString>) : MutableList<MutableList<Int>> {
    val table: MutableList<MutableList<Int>> = MutableList(textFile1.size + 1) { MutableList(textFile2.size + 1) { it } }
    for (i in 0..textFile1.size)
        table[i][0] = i
    for (row in 1..textFile1.size) {
        for (column in 1..textFile2.size) {
            //Рассмотрение случая совпадения значений или нет т.к. у нас нет замена, а только вставки и удаления
            if (equals(textFile1[row - 1], textFile2[column - 1])) {
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
fun createPath(table : MutableList<MutableList<Int>>, textFile1 : MutableList<FastString>, textFile2 : MutableList<FastString>) : List<String> {
    var row = textFile1.size
    var column = textFile2.size
    val result = mutableListOf("")
    while ((row != 0) && (column != 0)) {
        //Рассмотрение случая совпадения значений или нет т.к. у нас нет замена, а только вставки и удаления
        if (equals(textFile1[row - 1], textFile2[column - 1])) {
            when {
                table[row][column - 1] + 1 == minOf(table[row - 1][column] + 1, table[row][column - 1] + 1, table[row - 1][column - 1]) -> {
                    result.add("++ ${textFile2[column - 1].data} \n")
                    column--
                }
                table[row - 1][column] + 1 == minOf(table[row - 1][column] + 1, table[row][column - 1] + 1, table[row - 1][column - 1]) -> {
                    result.add("-- ${textFile1[row - 1].data} \n")
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
                    result.add("++ ${textFile2[column - 1].data} \n")
                    column--
                }
                else -> {
                    result.add("-- ${textFile1[row - 1].data} \n")
                    row--
                }
            }
        }
    }
    if (row == 0) {
        while (column != 0) {
            result.add("++ ${textFile2[column - 1].data} \n")
            column--
        }
        return result.reversed() // так как с конца надо развернуть
    }
    if (column == 0) {
        while (row != 0) {
            result.add("-- ${textFile1[row - 1].data} \n")
            row--
        }
        return result.reversed() // так как с конца надо развернуть
    }
    return emptyList()
}

//главная функция сравнения (надо дописать расшифровку редакционного предписания)
fun diff(file1 : MutableList<FastString>, file2 :MutableList<FastString>) : String {
    val buf = createPath(createTable(file1, file2), file1, file2)
    return buf.joinToString("")
}
