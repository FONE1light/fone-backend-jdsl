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
    @Column(length = 300) var url: String,
) : BaseEntity() {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(name = "profile_id", insertable = false, updatable = false)
    var profileId: Long? = null

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "profile_id")
    var profile: Profile? = null
        set(value) {
            field = value
            profileId = value?.id
        }

    override fun toString(): String {
        return "ProfileImage(id=$id)"
    }
}
