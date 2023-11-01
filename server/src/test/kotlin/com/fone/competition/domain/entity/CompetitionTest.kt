package com.fone.competition.domain.entity

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class CompetitionTest : DescribeSpec({
    describe("Competition") {
        it("viewCount 증가") {
            val competition = Competition(
                title = "제목",
                imageUrl = "https://www.naver.com",
                screeningStartDate = null,
                screeningEndDate = null,
                exhibitStartDate = null,
                exhibitEndDate = null,
                showStartDate = null,
                agency = "방송통신 위원회",
                details = "#참가자격...",
                userId = 1,
                viewCount = 0,
                scrapCount = 0,
                linkUrl = "https://www.naver.com"
            )

            competition.view()

            competition.viewCount shouldBe 1
        }
    }
})
