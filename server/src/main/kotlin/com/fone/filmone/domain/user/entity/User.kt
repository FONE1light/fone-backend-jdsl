package com.fone.filmone.domain.user.entity

import com.fone.filmone.domain.user.enum.Gender
import com.fone.filmone.domain.user.enum.Job
import com.fone.filmone.domain.user.enum.SocialLoginType
import com.fone.filmone.presentation.user.ModifyUserDto.ModifyUserRequest
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails


@Table("users")
data class User(

    @Id
    val id: Long? = null,

    @Column
    var job: Job,

    @Column
    var interests: String,

    @Column
    var nickname: String,

    @Column
    val birthday: String,

    @Column
    val gender: Gender,

    @Column
    var profileUrl: String,

    @Column
    val phoneNumber: String,

    @Column
    val email: String,

    @Column
    val socialLoginType: SocialLoginType,

    @Column
    val agreeToTermsOfServiceTermsOfUse: Boolean,

    @Column
    val agreeToPersonalInformation: Boolean,

    @Column
    val isReceiveMarketing: Boolean,

    @Column
    val roles: String,

    @Column
    val enabled: Boolean,
) : UserDetails {
    fun modifyUser(request: ModifyUserRequest) {
        this.nickname = request.nickname
        this.job = request.job
        this.interests = request.interests.joinToString(",")
        this.profileUrl = request.profileUrl ?: this.profileUrl
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return roles.split(",").map {
            SimpleGrantedAuthority(
                it
            )
        }.toMutableList()
    }

    override fun getPassword(): String {
        return ""
    }

    override fun getUsername(): String {
        return email
    }

    override fun isAccountNonExpired(): Boolean {
        return false
    }

    override fun isAccountNonLocked(): Boolean {
        return false
    }

    override fun isCredentialsNonExpired(): Boolean {
        return false
    }

    override fun isEnabled(): Boolean {
        return enabled
    }
}

