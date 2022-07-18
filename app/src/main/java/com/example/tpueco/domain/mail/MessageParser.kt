package com.example.tpueco.domain.mail

import com.example.tpueco.domain.Model.MessageDate
import javax.mail.Message
import javax.mail.Multipart

class MessageParser{

companion object {

    fun parseMessage(
        message: Message
    ): com.example.tpueco.domain.Model.Message {


        val from = parseFrom(message)
        val header = message.subject
        val text = parseText(message)
        val date = parseDate(message)
        return com.example.tpueco.domain.Model.Message(from, header, text, date)
    }

    fun parseFrom(message: Message): String {
        val from: String
        if (message.from.joinToString().get(0).toString() == "=") {
            from = message.from.contentToString().substring(
                message.from.contentToString().indexOf("<") + 1,
                message.from.contentToString().indexOf(">")
            )
        } else {
            from = message.from.joinToString()
        }
        return from
    }

    fun parseText(message: Message): String {
        var text = "errorText"
        if (message.contentType.substring(0, message.contentType.indexOf(";"))
                .equals("text/plain")
        ) {
            text = getTextIfNormalType(message)
        } else if (message.contentType.substring(0, message.contentType.indexOf(";"))
                .equals("multipart/alternative")
        ) {
            text = getTextIfAlternativeType(message)
        } else if (message.contentType.substring(0, message.contentType.indexOf(";"))
                .equals("multipart/mixed")
        ) {
            text = getTextIfMixedType(message)
        }
        return text
    }

    private fun getTextIfNormalType(
        message: Message
    ): String {
        var text = "errorText"
        text = message.content as String
        return text
    }

    private fun getTextIfAlternativeType(
        message: Message
    ): String {
        var text = "errorText"
        val multipart: Multipart = message.content as Multipart
        for (i in 0..(multipart.count - 1)) {
            if (multipart.getBodyPart(i).contentType.substring(
                    0,
                    multipart.getBodyPart(i).contentType.indexOf(";")
                ).equals("text/plain")
            ) {
                text = multipart.getBodyPart(i).content.toString()
            }
        }
        return text
    }

    private fun getTextIfMixedType(
        message: Message
    ): String {
        var text = "errorText"
        val multipart: Multipart = message.content as Multipart
        for (i in 0..(multipart.count - 1)) {

            if (multipart.getBodyPart(i).contentType.substring(
                    0,
                    multipart.getBodyPart(i).contentType.indexOf(";")
                ).equals("multipart/alternative")
            ) {
                val subMultipart: Multipart = multipart.getBodyPart(i).content as Multipart
                for (i in 0..(subMultipart.count - 1)) {
                    if (subMultipart.getBodyPart(i).contentType.substring(
                            0,
                            subMultipart.getBodyPart(i).contentType.indexOf(";")
                        ).equals("text/plain")
                    ) {
                        text =
                            "<писмо содержит вложения> \n" + subMultipart.getBodyPart(i).content
                    }
                }
            }
        }
        return text
    }


    fun parseDate(message: Message): MessageDate {
        val date: String = message.receivedDate.toString()
        val year = transformYear(date)
        val month = transformMonth(date)
        val day = transformDay(date)
        val dayOfWeek = transformDayOfTheWeek(date)
        val time = transformTime(date)
        return MessageDate(year, month, day, dayOfWeek, time)
    }

    private fun transformYear(date: String): String {
        return date.substring(30, 34)
    }


    private fun transformMonth(date: String): String {
        var month = "err"
        when (date.substring(4, 7)) {
            "Jan" -> month = "янв"
            "Feb" -> month = "фев"
            "Mar" -> month = "мар"
            "Apr" -> month = "апр"
            "May" -> month = "май"
            "Jun" -> month = "июн"
            "Jul" -> month = "июл"
            "Aug" -> month = "авг"
            "Sep" -> month = "сен"
            "Oct" -> month = "окт"
            "Nov" -> month = "ноя"
            "Dec" -> month = "дек"
        }
        return month
    }

    private fun transformDay(date: String): String {
        var day = "err"
        if (date.substring(8, 9).toInt() == 0) {
            day = date.substring(9, 10)
        } else {
            day = date.substring(8, 10)
        }
        return day
    }

    private fun transformDayOfTheWeek(date: String): String {
        var dayOfTheWeek = "err"
        when (date.substring(0, 3)) {
            "Mon" -> dayOfTheWeek = "пн"
            "Thu" -> dayOfTheWeek = "вт"
            "Wed" -> dayOfTheWeek = "ср"
            "Tue" -> dayOfTheWeek = "чт"
            "Fri" -> dayOfTheWeek = "пт"
            "Sat" -> dayOfTheWeek = "сб"
            "Sun" -> dayOfTheWeek = "вс"
        }
        return dayOfTheWeek
    }


    private fun transformTime(date: String): String {
        var time = "err"
        if (date.substring(11, 12).toInt() == 0) {
            time = date.substring(12, 16)
        } else {
            time = date.substring(11, 16)
        }
        return time
    }


}
}

