package com.fone.common.entity

import java.time.LocalDateTime
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity {

    @CreatedDate
    var createdAt: LocalDateTime = LocalDateTime.now()
        private set

    @LastModifiedDate
    var modifiedAt: LocalDateTime = LocalDateTime.now()
        private set
}
