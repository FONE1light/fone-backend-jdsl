package com.fone.jobOpening.infrastructure

import com.fone.common.exception.BadLocationException
import com.fone.jobOpening.domain.entity.Location
import com.fone.jobOpening.domain.repository.LocationRepository
import com.fone.jobOpening.presentation.dto.FifthPage
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.querydsl.expression.count
import com.linecorp.kotlinjdsl.spring.data.reactive.query.SpringDataHibernateMutinyReactiveQueryFactory
import com.linecorp.kotlinjdsl.spring.reactive.listQuery
import com.linecorp.kotlinjdsl.spring.reactive.singleQuery
import com.linecorp.kotlinjdsl.spring.reactive.singleQueryOrNull
import io.smallrye.mutiny.coroutines.asFlow
import io.smallrye.mutiny.coroutines.awaitSuspending
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapMerge
import org.springframework.stereotype.Repository

@Repository
class LocationRepositoryImpl(
    private val queryFactory: SpringDataHibernateMutinyReactiveQueryFactory,
) : LocationRepository {
    init {
        repo = this
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    override suspend fun saveAll(locations: Collection<Location>): Collection<Location> {
        queryFactory.withFactory { session, _ ->
            val locationsUni =
                locations.map {
                    if (it.id == null) {
                        session.persist(it)
                    } else {
                        session.merge(it)
                    }.toMulti()
                }
            locationsUni.asFlow().flatMapMerge {
                it.asFlow()
            }.collect()
            session.flush().awaitSuspending()
        }
        return locations
    }

    override suspend fun findLocation(
        region: String,
        district: String,
    ): Location? {
        return queryFactory.withFactory { session, factory ->
            factory.singleQueryOrNull<Location> {
                select(Location::class.java)
                from(Location::class.java)
                whereAnd(
                    col(Location::region).equal(region),
                    col(Location::district).equal(district)
                )
            }
        }
    }

    override suspend fun getAllRegions(): List<String> {
        return queryFactory.withFactory { factory ->
            factory.listQuery {
                select(distinct = true, col(Location::region))
                from(Location::class.java)
                orderBy(col(Location::region).asc())
            }
        }
    }

    override suspend fun getDistricts(region: String): List<String> {
        return queryFactory.withFactory { factory ->
            factory.listQuery {
                select(col(Location::district))
                from(Location::class.java)
                where(col(Location::region).equal(region))
                orderBy(col(Location::district).asc())
            }
        }
    }

    override suspend fun count(): Long {
        return queryFactory.withFactory { factory ->
            factory.singleQuery {
                select(listOf(count(Location::id)))
                from(Location::class.java)
            }
        }
    }
}

private lateinit var repo: LocationRepository

suspend fun FifthPage.toLocation(): Location {
    return repo.findLocation(workingCity, workingDistrict) ?: throw BadLocationException()
}
