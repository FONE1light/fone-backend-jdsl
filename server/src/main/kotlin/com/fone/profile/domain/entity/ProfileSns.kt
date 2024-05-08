package com.fone.profile.domain.entity

import com.fone.common.entity.BaseEntity
import com.fone.profile.domain.enum.SNS
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "profile_sns")
class ProfileSns(
    @Column(length = 300) var url: String,
    @Enumerated(EnumType.STRING) @Column var sns: SNS,
) : BaseEntity() {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(name = "profile_id", insertable = false, updatable = false)
    var profileId: Long? = null

    @ManyToOne
    @JoinColumn(name = "profile_id")
    var profile: Profile? = null
        set(value) {
            profileId = value?.id
            field = value
        }
}

fun <T : Collection<ProfileSns>> T.setProfile(profile: Profile): T {
    forEach {
        it.profile = profile
    }
    return this
}
