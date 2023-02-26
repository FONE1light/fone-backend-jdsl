package com.fone.question.domain.entity

import com.fone.common.entity.BaseEntity
import com.fone.question.domain.enum.Type
import javax.persistence.*

@Entity
@Table(name = "questions")
data class Question(
    @Id @Column @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
    @Column val email: String,
    @Enumerated(EnumType.STRING) val type: Type,
    @Column val title: String,
    @Column(length = 500) val description: String,
    @Column val agreeToPersonalInformation: Boolean,
) : BaseEntity()
