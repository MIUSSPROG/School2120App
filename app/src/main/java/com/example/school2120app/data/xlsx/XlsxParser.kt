package com.example.school2120app.data.xlsx

import java.io.InputStream

interface XlsxParser<T> {
    suspend fun parse(stream: InputStream): T
}