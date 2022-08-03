package com.example.school2120app.data.xlsx

import com.example.school2120app.core.util.FileCaching
import com.example.school2120app.domain.model.contacts.ContactInfo
import com.example.school2120app.domain.model.contacts.ContactsList
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileInputStream
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactsParser @Inject constructor(): XlsxParser<ContactsList> {

    private val contactsList: ArrayList<ContactInfo> = ArrayList()

    override suspend fun parse(stream: InputStream): ContactsList {
        val schedulePath = FileCaching.save(stream = stream, filePrefix = "contacts_")
        val fis = FileInputStream(schedulePath)
        val myWorkBook = XSSFWorkbook(fis)
        val numOfSheets = myWorkBook.numberOfSheets

        val content = StringBuilder("")
        loop@ for (sheet in 0 until numOfSheets) {
            var isHeader = true
            val curSheet = myWorkBook.getSheetAt(sheet)
            val rowIterator: Iterator<Row> = curSheet.iterator()
            while (rowIterator.hasNext()) {
                if (isHeader){
                    isHeader = false
                    rowIterator.next()
                    continue
                }
                val row = rowIterator.next()
                val cellIterator = row.cellIterator()
                while (cellIterator.hasNext()) {
                    val cell = cellIterator.next()
                    when(cell.cellType){
                        Cell.CELL_TYPE_STRING -> {
                            content.append(cell.stringCellValue).append("&")
                        }
                        Cell.CELL_TYPE_BLANK -> {
                            continue@loop
                        }
                    }
                }
                val contactRow = content.split("&")
                contactsList.add(
                    ContactInfo(
                        position = contactRow[0],
                        name = contactRow[1],
                        address = contactRow[2],
                        lat = contactRow[3].split(", ")[0].toDouble(),
                        lon = contactRow[3].split(", ")[1].toDouble(),
                        buildingType = contactRow[4],
                        phone = contactRow[5],
                        email = contactRow[6],
                        photoUrl = contactRow[7]
                    )
                )
                content.setLength(0)
            }
        }

        return ContactsList(contacts = contactsList)
    }
}