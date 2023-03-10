package com.fone.report.domain.entity

import com.fone.common.converter.SeparatorConverter
import com.fone.common.entity.BaseEntity
import com.fone.report.domain.enum.Type
import javax.persistence.Column
import javax.persistence.Convert
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "reports")
data class Report(
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column var reportUserId: Long = 0,
    @Column var type: Type = Type.JOB_OPENING,
    @Column var typeId: Long = 0,
    @Convert(converter = SeparatorConverter::class)
    @Column(length = 500)
    var inconvenients: List<String> = listOf(),
    @Column(length = 500) var details: String = "",
    @Column var userId: Long = 0,
) : BaseEntity()
