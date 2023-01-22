package com.fone.filmone.domain.competition.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("competitions")
data class Competition (

    @Id
    var id: Long? = null,

    @Column
    var title: String,

    @Column
    var imageUrl: String,

    @Column
    var startDate: String,

    @Column
    var endDate: String,

    @Column
    var agency: String,

    @Column
    var details: String,

    @Column
    var userId: Long,

    @Column
    var viewCount: Long,
) {
    fun view() {
        viewCount += 1
    }
}