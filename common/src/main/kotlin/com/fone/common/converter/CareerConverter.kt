package com.fone.common.converter

import com.fone.common.entity.Career
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter
class CareerConverter : AttributeConverter<Set<Career>, String> {
    override fun convertToDatabaseColumn(attribute: Set<Career>): String {
        return attribute.joinToString()
    }

    override fun convertToEntityAttribute(data: String?): Set<Career> {
        if (data.isNullOrBlank()) return setOf()
        return data.split(",").map { Career.valueOf(it.trim()) }.toSet()
    }
}
