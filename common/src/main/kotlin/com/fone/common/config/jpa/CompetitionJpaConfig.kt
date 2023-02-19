package com.fone.common.config.jpa

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EntityScan(basePackages = ["com.fone.filmone.domain.competition"])
@EnableJpaRepositories(basePackages = ["com.fone.filmone.infrastructure.competition"])
class CompetitionJpaConfig