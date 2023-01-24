package com.fone.filmone.domain.question.entity

import com.fone.filmone.domain.question.enum.Type
import javax.persistence.*

@Entity
@Table(name = "questions")
data class Question(

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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