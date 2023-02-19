package com.fone.jobOpening.domain.entity

import com.fone.common.entity.CategoryType
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

    constructor(reqJobOpeningId: Long, categoryType: CategoryType) : this(
        jobOpeningId = reqJobOpeningId,
        type = categoryType,
    )
}