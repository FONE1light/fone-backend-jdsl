package com.fone.filmone.presentation.competition

import com.fone.filmone.domain.competition.entity.Competition
import com.fone.filmone.domain.competition.entity.CompetitionPrize
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

class RetrieveCompetitionDto {

    data class RetrieveCompetitionsResponse(
        val competitions: Slice<CompetitionDto>,
    ) {

        constructor(
            pageable: Pageable,
            competitionDtos: List<CompetitionDto>
        ) : this(

            competitions = PageImpl(
                competitionDtos,
                pageable,
                competitionDtos.size.toLong(),
            )
        )
    }

    data class RetrieveCompetitionResponse(
        val competition: CompetitionDto
    ) {

        constructor(
            com: Competition,
            prizes: List<CompetitionPrize>
        ) : this(
            competition = CompetitionDto(com, prizes)
        )
    }
}