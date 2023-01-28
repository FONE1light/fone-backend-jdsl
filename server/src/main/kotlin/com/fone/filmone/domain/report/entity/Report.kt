package com.fone.filmone.domain.report.entity

import com.fone.common.converter.SeparatorConverter
import com.fone.filmone.domain.common.BaseEntity
import javax.persistence.*

@Entity
@Table(name = "reports")
data class Report(

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column
    var reportUserId: Long,

    @Column
    var type: String,

    @Column
    var typeId: Long,

    @Convert(converter = SeparatorConverter::class)
    @Column(length = 500)
    var inconvenients: List<String> = listOf(),

    @Column(length = 500)
    var details: String,

    @Column
    var userId: Long,
) : BaseEntity()