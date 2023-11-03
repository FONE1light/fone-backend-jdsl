package com.fone.common.converter

import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter
class URLSeparatorConverter : AttributeConverter<List<String>, String> {
    override fun convertToDatabaseColumn(attribute: List<String>): String {
        return attribute.joinToString(" ")
    }

    override fun convertToEntityAttribute(dbData: String): List<String> {
        return dbData.split(" ").toList()
    }
}
