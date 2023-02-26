package com.fone.common.config.jpa

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EntityScan(basePackages = ["com.fone.question.domain"])
@EnableJpaRepositories(basePackages = ["com.fone.question.infrastructure"])
class QuestionJpaConfig
