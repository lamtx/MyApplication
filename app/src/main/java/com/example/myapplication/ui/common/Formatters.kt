package com.example.myapplication.ui.common

import android.icu.text.DateFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date

fun formatDateOnly(date: Date): String {
    val formatter = SimpleDateFormat.getDateInstance(DateFormat.LONG)
    return formatter.format(date)
}

fun formatLong(value: Long): String {
    val formatter = NumberFormat.getInstance()
    return formatter.format(value)
}