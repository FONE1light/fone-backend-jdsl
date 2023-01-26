package com.fone.filmone.domain.user.entity

import com.fone.filmone.common.converter.SeparatorConverter
import com.fone.filmone.domain.common.Gender
import com.fone.filmone.domain.common.Interest
import com.fone.filmone.domain.user.enum.Job
import com.fone.filmone.domain.user.enum.Role
import com.fone.filmone.domain.user.enum.SocialLoginType
import com.fone.filmone.presentation.user.ModifyUserDto.ModifyUserRequest
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*


@Entity
@Table(name = "users")
data class User(

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column
    var job: Job,

    @Convert(converter = SeparatorConverter::class)
    var interests: List<Interest> = listOf(),

    @Column
    var nickname: String,

    @Column
    var birthday: String,

    @Column
    val gender: Gender,

    @Column
    var profileUrl: String,

    @Column
    var phoneNumber: String,

    @Column
    var email: String,

    @Column
    val socialLoginType: SocialLoginType,

    @Column
    val agreeToTermsOfServiceTermsOfUse: Boolean,

    @Column
    val agreeToPersonalInformation: Boolean,

    @Column
    val isReceiveMarketing: Boolean,

    @Convert(converter = SeparatorConverter::class)
    var roles: List<Role>,

    @Column
    var enabled: Boolean,
) : UserDetails {
    fun modifyUser(request: ModifyUserRequest) {
        this.nickname = request.nickname
        this.job = request.job
        this.interests = request.interests
        this.profileUrl = request.profileUrl ?: this.profileUrl
    }

    fun signOutUser() {
        interests = listOf()
        nickname = "탈퇴한 유저"
        birthday = ""
        profileUrl = ""
        phoneNumber = ""
        email = ""
        roles = listOf()
        enabled = false
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return roles.map { SimpleGrantedAuthority(it.toString()) }.toMutableList()
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

