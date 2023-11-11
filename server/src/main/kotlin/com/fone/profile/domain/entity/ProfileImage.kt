package com.fone.profile.domain.entity

import com.fone.common.entity.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType.LAZY
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "profile_images")
data class ProfileImage(
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Deprecated("url으로 대체됩니다.")
    @Column(length = 300)
    var profileUrl: String,
    @Column(length = 300) var url: String,
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "profile_id")
    var profile: Profile? = null,
) : BaseEntity() {

    constructor(url: String) : this(profileUrl = url, url = url)

    override fun toString(): String {
        return "ProfileImage(id=$id)"
    }

    fun addProfile(profile: Profile) {
        this.profile = profile
    }
}
