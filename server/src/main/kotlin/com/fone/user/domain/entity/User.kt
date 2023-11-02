package com.fone.user.domain.entity

import com.fone.common.converter.SeparatorConverter
import com.fone.common.entity.BaseEntity
import com.fone.common.entity.CategoryType
import com.fone.common.entity.Gender
import com.fone.common.jwt.Role
import com.fone.common.password.PasswordService
import com.fone.user.domain.enum.Job
import com.fone.user.domain.enum.LoginType
import com.fone.user.presentation.dto.ModifyUserDto.AdminModifyUserRequest
import com.fone.user.presentation.dto.ModifyUserDto.ModifyUserRequest
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Convert
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "users")
@Suppress("ACCIDENTAL_OVERRIDE", "CONFLICTING_INHERITED_JVM_DECLARATIONS")
data class User(
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Enumerated(EnumType.STRING) var job: Job = Job.ACTOR,
    @Convert(converter = SeparatorConverter::class) var interests: List<String> = listOf(),
    @Column var name: String = "",
    @Column var nickname: String = "",
    @Column var birthday: LocalDate? = null,
    @Enumerated(EnumType.STRING) val gender: Gender = Gender.MAN,
    @Column(length = 300) var profileUrl: String = "",
    @Column(unique = true) var phoneNumber: String? = "",
    @Column var email: String = "",
    @Column val identifier: String? = null,
    @Enumerated(EnumType.STRING) val loginType: LoginType = LoginType.APPLE,
    @Column val agreeToTermsOfServiceTermsOfUse: Boolean = false,
    @Column val agreeToPersonalInformation: Boolean = false,
    @Column val isReceiveMarketing: Boolean = false,
    @Convert(converter = SeparatorConverter::class) var roles: List<String> = listOf(),
    @Column var enabled: Boolean = false,
    @JvmField @Column
    var password: String? = null,
    @Column var lastLoginDate: LocalDateTime? = null,
) : UserDetails, BaseEntity() {
    fun modifyUser(request: ModifyUserRequest) {
        this.nickname = request.nickname
        this.job = request.job
        this.interests = request.interests.map { it.toString() }.toList()
        this.profileUrl = request.profileUrl ?: this.profileUrl
    }

    fun adminModifyUser(request: AdminModifyUserRequest) {
        request.job?.also { this.job = it }
        request.interests?.also { this.interests = it.map(CategoryType::toString) }
        request.profileUrl?.also { this.profileUrl = it }
        request.nickname?.also { this.nickname = it }
        request.roles?.also { this.roles = it.map(Role::toString) }
    }

    fun login() {
        lastLoginDate = LocalDateTime.now()
    }

    fun signOutUser() {
        interests = listOf()
        name = "탈퇴한 유저"
        nickname = "탈퇴한 유저"
        birthday = null
        profileUrl = ""
        phoneNumber = null
        email = ""
        roles = listOf()
        enabled = false
    }

    fun updatePassword(newPassword: String) {
        password = PasswordService.hashPassword(newPassword)
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return roles.map { SimpleGrantedAuthority(it) }.toMutableList()
    }

    override fun getPassword(): String? {
        return password
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
