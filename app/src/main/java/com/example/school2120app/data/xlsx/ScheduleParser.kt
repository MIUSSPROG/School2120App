package com.example.school2120app.data.xlsx

import com.example.school2120app.core.util.FileCaching
import com.example.school2120app.domain.model.schedule.local.LessonInfo
import com.example.school2120app.domain.model.schedule.local.Schedule
import com.example.school2120app.domain.model.schedule.local.ScheduleByBuilding
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.time.MonthDay
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScheduleParser @Inject constructor() : XlsxParser<ScheduleByBuilding> {

    private lateinit var schedules: ArrayList<Schedule>
    private lateinit var grades: ArrayList<String>
    private lateinit var letters: ArrayList<String>
    private lateinit var curWeekday: String
    private lateinit var lessonRoomArray: MutableList<String>
    private lateinit var contentCell: String
    private lateinit var curSheetNum: String

    override suspend fun parse(stream: InputStream): ScheduleByBuilding {

        try {
            schedules = ArrayList<Schedule>()
            grades = ArrayList<String>()
            letters = ArrayList<String>()

            var isScheduleHeaderPassed: Boolean
            var isScheduleClassList: Boolean
            var isLesson: Boolean

            var classNum: Int // количество классов в параллели
            var curClass: Int // текущий класс в параллели
//            var contentCell: String // содержимое ячейки

//            var lessonRoomArray: MutableList<String> // список кабинетов(в случае разделенного по подгруппам урока)
            var lesson = ""
            var room = "" // кабинет урока

            var letter = ""
            var grade = ""

            var curClassCount = 0

            // проверка наличия файла schedule в папке cache
            val schedulePath = FileCaching.save(stream = stream, filePrefix = "schedule_")
            val fis = FileInputStream(schedulePath)
            val myWorkBook = XSSFWorkbook(fis)
            val numOfSheets = myWorkBook.numberOfSheets

            // читаем листы xlsx файла
            for (sheetNum in 0 until numOfSheets) { // numOfSheets
                curSheetNum = sheetNum.toString()
                isScheduleHeaderPassed = false
                isScheduleClassList = false
                isLesson = false

                val curSheet = myWorkBook.getSheetAt(sheetNum)
                val rowIterator: Iterator<Row> = curSheet.iterator()
                classNum = 0

                while (rowIterator.hasNext()) {
                    curClass = 0
                    if (isScheduleHeaderPassed) {
                        isScheduleClassList = true
                        isScheduleHeaderPassed = false
                    }

                    val row = rowIterator.next()
                    val cellIterator = row.cellIterator()
                    while (cellIterator.hasNext()) {
                        val cell = cellIterator.next()
                        when (cell.cellType) {
                            Cell.CELL_TYPE_STRING -> {
                                contentCell = cell.stringCellValue.trim()
                                if (contentCell.contains("Утверждено")) break
                                if (contentCell.contains("Расписание")) {
                                    isScheduleHeaderPassed = true // строка с заголовком расписания пройдена
                                    break
                                }
                                if (contentCell in listOf(Monday, Tuesday, Wednesday, Thursday, Friday)) {
                                    isScheduleClassList = false
                                    val lastClasses = schedules.takeLast(classNum)
                                    for (schedule in lastClasses) {
                                        if (contentCell == Monday) { // на первом проходе выделяем память для списка уроков текущего дня под все классы
                                            schedule.weekdayLessons = mutableMapOf()
                                        }
                                        curWeekday = contentCell
                                        schedule.weekdayLessons!![curWeekday] = mutableListOf()
                                    }
                                    isLesson = true
                                    continue
                                }

                                if (isScheduleClassList) { // строка с перечнем классов в параллели
                                    grade = ""
                                    letter = ""
                                    classNum++
                                    when (contentCell.length) {
                                        2 -> {
                                            grade = contentCell[0].toString() // 9а
                                            letter = contentCell[1].toString()
                                        }
                                        3 -> {
                                            grade = contentCell.substring(0..1) // 10а
                                            letter = contentCell[2].toString()
                                        }
                                    }
                                    val schedule = Schedule(grade = grade, letter = letter)
                                    schedules.add(schedule)

                                } else {
                                    contentCell = contentCell.trim().replace("-", "") // окна отмечаются ---
                                    lessonRoomArray = contentCell.split(" ").filter { it.isNotEmpty() }.toMutableList()
                                    if (lessonRoomArray.isEmpty()) {
                                        lesson = emptyLesson
                                        room = emptyRoom
                                    } else if (contentCell.contains("физическая культура")) {
                                        lesson = "физическая культура"
                                        room = "спортзал"
                                    }else if (contentCell.contains("профил.труд")){
                                        lesson = "профил.труд"
                                        room = "мастерская"
                                    }
                                    else if (lessonRoomArray.size == 1){
                                        lesson = lessonRoomArray.removeLast()
                                        room = "Уточняйте у преподавателя"
                                    }
                                    else {
                                        room = lessonRoomArray.removeLast()
                                        lesson = lessonRoomArray.reduce{a, b -> "$a $b"}
//                                        Log.d("lesson", "$sheetNum $curClass $curWeekday $lesson $room")
                                    }

                                    schedules[curClassCount + curClass].weekdayLessons?.get(curWeekday)?.add(LessonInfo(name = lesson, room = room))
                                    curClass++
                                }
                            }

//                        Cell.CELL_TYPE_NUMERIC -> {
//                        }
//
                        Cell.CELL_TYPE_BLANK -> {
                            if (isLesson && curClass != 0){
                                curClass++
                            }
                        }

                        }
                    }
                }
                curClassCount += classNum
            }
//            println(schedules)
//            for (schedule in schedules){
//                println("${schedule.grade} ${schedule.letter}")
//                for (weekday in schedule.weekdayLessons!!){
//                    println(weekday.key)
//                    for (lesson in weekday.value){
//                        println("${lesson.name} ${lesson.room}")
//                    }
//                }
//            }
        }catch (e: Exception){
//            println(schedules)
            println(e.message)
            println(lessonRoomArray)
            println(contentCell)
            println(curSheetNum)
            println(e.stackTrace)

        }
        return ScheduleByBuilding(building = "ш4", scheduleList = schedules)

    }

    companion object{
        const val cachePath = "/data/data/com.example.school2120app/cache"
        const val emptyLesson = "-------------------------"
        const val emptyRoom = ""
        const val Monday = "Понедельник"
        const val Tuesday = "Вторник"
        const val Wednesday = "Среда"
        const val Thursday = "Четверг"
        const val Friday = "Пятница"
    }
}