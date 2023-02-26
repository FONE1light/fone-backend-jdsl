package com.fone.user.domain.entity

import com.fone.common.converter.SeparatorConverter
import com.fone.common.entity.BaseEntity
import com.fone.common.entity.Gender
import com.fone.user.domain.enum.Job
import com.fone.user.domain.enum.SocialLoginType
import com.fone.user.presentation.dto.ModifyUserDto.*
import java.time.LocalDate
import javax.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "users")
data class User(
    @Id @Column @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
    @Enumerated(EnumType.STRING) var job: Job,
    @Convert(converter = SeparatorConverter::class) var interests: List<String> = listOf(),
    @Column var nickname: String,
    @Column var birthday: LocalDate?,
    @Enumerated(EnumType.STRING) val gender: Gender,
    @Column(length = 300) var profileUrl: String,
    @Column var phoneNumber: String,
    @Column var email: String,
    @Enumerated(EnumType.STRING) val socialLoginType: SocialLoginType,
    @Column val agreeToTermsOfServiceTermsOfUse: Boolean,
    @Column val agreeToPersonalInformation: Boolean,
    @Column val isReceiveMarketing: Boolean,
    @Convert(converter = SeparatorConverter::class) var roles: List<String>,
    @Column var enabled: Boolean,
) : UserDetails, BaseEntity() {
    fun modifyUser(request: ModifyUserRequest) {
        this.nickname = request.nickname
        this.job = request.job
        this.interests = request.interests.map { it.toString() }.toList()
        this.profileUrl = request.profileUrl ?: this.profileUrl
    }

    fun signOutUser() {
        interests = listOf()
        nickname = "탈퇴한 유저"
        birthday = null
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
