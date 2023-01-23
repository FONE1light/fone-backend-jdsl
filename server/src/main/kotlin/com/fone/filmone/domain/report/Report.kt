package com.fone.filmone.domain.report

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("reports")
data class Report (

    @Id
    var id: Long? = null,

    @Column
    var reportUserId: Long,

    @Column
    var type: String,

    @Column
    var typeId: Long,

    @Column
    var inconvenients: String,

    @Column
    var details: String,

    @Column
    var userId: Long,
)