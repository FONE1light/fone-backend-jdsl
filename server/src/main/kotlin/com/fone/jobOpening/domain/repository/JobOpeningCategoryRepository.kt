package com.fone.jobOpening.domain.repository

import com.fone.common.entity.CategoryType

interface JobOpeningCategoryRepository {

    suspend fun saveAll(jobOpeningCategories: List<com.fone.jobOpening.domain.entity.JobOpeningCategory>): List<com.fone.jobOpening.domain.entity.JobOpeningCategory>

    suspend fun deleteByJobOpeningId(jobOpeningId: Long): Int

    suspend fun findByJobOpeningIds(jobOpeningIds: List<Long>): Map<Long, List<CategoryType>>

    suspend fun findByJobOpeningId(jobOpeningId: Long): List<CategoryType>
}
