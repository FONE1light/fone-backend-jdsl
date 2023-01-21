package com.fone.filmone.domain.job_opening.entity

import com.fone.filmone.domain.common.Career
import com.fone.filmone.domain.common.Gender
import com.fone.filmone.domain.common.Type
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("job_openings")
data class JobOpening(

    @Id
    var id: Long? = null,

    @Column
    val title: String,

    @Column
    val interests: String,

    @Column
    val deadline: String,

    @Column
    val casting: String,

    @Column
    val numberOfRecruits: Int,

    @Column
    val gender: Gender,

    @Column
    val ageMax: Int,

    @Column
    val ageMin: Int,

    @Column
    val career: Career,

    @Column
    val type: Type,

    @Column
    val domains: String,

    @Column
    val userId: Long,

    @Column
    var viewCount: Long,
) {
    fun view() {
        viewCount += 1
    }
}