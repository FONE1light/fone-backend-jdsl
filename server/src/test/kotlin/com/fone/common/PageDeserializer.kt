package com.fone.common

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest

// Page 역직렬화하는데 필요할 수 있음. clazz의 Page가 나타내고 있는 Class을 나타냄.
class PageDeserializer<T : Any>(private val objectMapper: ObjectMapper, private val clazz: Class<T>) :
    JsonDeserializer<Page<T>>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Page<T> {
        val codec = p.codec
        val node: JsonNode = codec.readTree(p)

        val contentNode = node.get("content")
        val content = mutableListOf<T>()
        if (contentNode.isArray) {
            for (item in contentNode) {
                content.add(objectMapper.treeToValue(item, clazz))
            }
        }

        val pageable = PageRequest.of(
            node.get("number").asInt(),
            node.get("size").asInt()
        )

        return PageImpl<T>(content, pageable, node.get("totalElements").asLong())
    }
}
