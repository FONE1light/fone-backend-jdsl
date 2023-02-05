package com.fone.filmone.domain.job_opening.entity

import com.fone.filmone.domain.common.CategoryType
import javax.persistence.*

@Entity
@Table(name = "job_opening_categories")
data class JobOpeningCategory(

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column
    var jobOpeningId: Long,

    @Enumerated(EnumType.STRING)
    var type: CategoryType,
) {

    constructor(reqJobOpeningId: Long, categoryType: CategoryType): this(
        jobOpeningId = reqJobOpeningId,
        type = categoryType,
    )
}