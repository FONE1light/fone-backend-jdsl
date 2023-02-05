package com.fone.filmone.domain.profile.entity

import com.fone.filmone.domain.common.CategoryType
import javax.persistence.*

@Entity
@Table(name = "profile_categories")
data class ProfileCategory(

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column
    var profileId: Long,

    @Enumerated(EnumType.STRING)
    var type: CategoryType,
) {

    constructor(reqProfileId: Long, categoryType: CategoryType) : this(
        profileId = reqProfileId,
        type = categoryType,
    )
}