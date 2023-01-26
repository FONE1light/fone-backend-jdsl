package com.fone.filmone.domain.report.entity

import com.fone.filmone.common.converter.SeparatorConverter
import javax.persistence.*

@Entity
@Table(name = "reports")
data class Report (

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
    var inconvenients: List<String> = listOf(),

    @Column
    var details: String,

    @Column
    var userId: Long,
)