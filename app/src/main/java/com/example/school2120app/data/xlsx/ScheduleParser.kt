package com.example.school2120app.data.xlsx

import com.example.school2120app.domain.model.schedule.local.Schedule
import com.example.school2120app.domain.model.schedule.local.ScheduleByBuilding
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Paths
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScheduleParser @Inject constructor() : XlsxParser<ScheduleByBuilding> {
    override suspend fun parse(stream: InputStream): List<ScheduleByBuilding> {

        val schedules = ArrayList<Schedule>()
        val grades = ArrayList<String>()
        val letters = ArrayList<String>()

        var isSchedulePassed: Boolean
        var isMonday: Boolean
        var isTuesday: Boolean
        var isWednesday: Boolean
        var isThursday: Boolean
        var isFriday: Boolean
        var isLessonNum: Boolean // начало строки с уроками

        var colNum: Int // количество классов в параллели

        var curNum: Int // текущий класс в параллели

        var c1 = 0
        var c2 = 0
        var lessonRoom: Array<String?>
        val lesson = ""
        val room = ""

//        val path = Paths.get(contentRoot)
//        val file = Files.createTempFile(path, "schedule", ".xlsx")
//        Files.write(file, stream.readBytes())
        File(cachePath).walk().forEach {
            val splitPath = it.toString().split('/')
            if (splitPath[splitPath.size-1].startsWith("schedule")){
                println("exist")
            }
        }
        val file = createTempFile(prefix = "schedule_", suffix = ".xlsx")
        file.writeBytes(stream.readBytes())
//        val fis = FileInputStream(file)
//        val myWorkBook = XSSFWorkbook(fis)
//        val numOfSheets = myWorkBook.numberOfSheets
        return emptyList()
//        stream.use { input ->
//
//        }

//        val fis: FileInputStream = FileInputStream(stream)
//        val myWorkBook = XSSFWorkbook(fis)
    }

    companion object{
        val cachePath = "/data/data/com.example.school2120app/cache"
    }
}