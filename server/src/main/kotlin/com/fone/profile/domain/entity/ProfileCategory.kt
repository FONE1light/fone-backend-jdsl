package com.fone.profile.domain.entity

import com.fone.common.entity.CategoryType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "profile_categories")
data class ProfileCategory(
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column var profileId: Long,
    @Enumerated(EnumType.STRING) var type: CategoryType,
) {

    constructor(
        reqProfileId: Long,
        categoryType: CategoryType,
    ) : this(
        profileId = reqProfileId,
        type = categoryType
    )
}
