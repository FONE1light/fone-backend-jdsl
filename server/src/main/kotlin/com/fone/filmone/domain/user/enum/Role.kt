package com.fone.filmone.domain.user.enum

enum class Role {
    ROLE_USER, ROLE_ADMIN;

    companion object {

        operator fun invoke(priority: String) = Role.valueOf(priority.uppercase())
    }
}