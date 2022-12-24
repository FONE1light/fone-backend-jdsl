package com.fone.filmone.domain.user

import com.fone.filmone.domain.user.enum.Gender
import com.fone.filmone.domain.user.enum.Interest
import com.fone.filmone.domain.user.enum.Job
import com.fone.filmone.domain.user.enum.SocialLoginType
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table


@Table("users")
data class User(

    @Id
    val id: Long? = null,

    @Column
    val job: Job,

    @Column
    val interests: String,

    @Column
    val nickname: String,

    @Column
    val birthday: String,

    @Column
    val gender: Gender,

    @Column
    val profileUrl: String,

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
)

