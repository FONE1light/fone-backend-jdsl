package com.fone.filmone.presentation.competition

import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

class RetrieveCompetitionDto {

    data class RetrieveCompetitionResponse(
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
}