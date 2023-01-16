package com.fone.filmone.domain.question.entity

import com.fone.filmone.domain.question.enum.Type
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("questions")
data class Question(

    @Id
    var id: Long? = null,

    @Column
    val email: String,

    @Column
    val type: Type,

    @Column
    val title: String,

    @Column
    val description: String,

    @Column
    val agreeToPersonalInformation: Boolean
)