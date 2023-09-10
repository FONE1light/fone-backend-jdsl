package com.fone.question.domain.entity

import com.fone.common.entity.BaseEntity
import com.fone.question.domain.enum.Type
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "questions")
data class Question(
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column var email: String = "",
    @Enumerated(EnumType.STRING) var type: Type = Type.USE_QUESTION,
    @Column var title: String = "",
    @Column(length = 500) var description: String = "",
    @Column var agreeToPersonalInformation: Boolean = false,
    var userId: Long? = null,
) : BaseEntity()
