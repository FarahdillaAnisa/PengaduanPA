package com.icha.layananpengaduanpa.helper

import java.text.SimpleDateFormat
import java.util.*

fun main() {
    println(getCurrentDate())
    println(displayDate("2022-10-7"))
}

fun getCurrentDate(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val date = Date()
    return dateFormat.format(date)
}

fun displayDate(date: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val dateFormat = SimpleDateFormat("dd-MMMM-yyyy", Locale.getDefault())
    val dateInput = inputFormat.parse(date)
    return dateFormat.format(dateInput)
}