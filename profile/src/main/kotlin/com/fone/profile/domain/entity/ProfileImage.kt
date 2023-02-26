package com.fone.profile.domain.entity

import com.fone.common.entity.BaseEntity
import javax.persistence.*
import javax.persistence.FetchType.LAZY

@Entity
@Table(name = "profile_images")
data class ProfileImage(
    @Id @Column @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
    @Column(length = 300) var profileUrl: String,
    @ManyToOne(fetch = LAZY) @JoinColumn(name = "profile_id") var profile: Profile? = null,
) : BaseEntity() {

    constructor(url: String) : this(profileUrl = url)

    override fun toString(): String {
        return "ProfileImage(id=$id)"
    }

    fun addProfile(profile: Profile) {
        this.profile = profile
    }
}
