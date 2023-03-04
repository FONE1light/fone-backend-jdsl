package com.fone.common.converter

import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter
class SeparatorConverter : AttributeConverter<List<Any>, String> {
    override fun convertToDatabaseColumn(attribute: List<Any>): String {
        return attribute.map { it.toString() }.joinToString(",")
    }

    override fun convertToEntityAttribute(dbData: String): List<Any> {
        return dbData.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toList()
    }
}
