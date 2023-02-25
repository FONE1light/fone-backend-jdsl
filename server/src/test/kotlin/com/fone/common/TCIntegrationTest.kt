package com.fone.common

import com.fone.filmone.ServerApplication
import com.github.dockerjava.api.model.ExposedPort
import com.github.dockerjava.api.model.PortBinding
import com.github.dockerjava.api.model.Ports
import org.junit.runner.RunWith
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@SpringBootTest(classes = [ServerApplication::class])
@RunWith(SpringRunner::class)
@Testcontainers
@AutoConfigureWebTestClient
abstract class TCIntegrationTest {

    companion object {
        @JvmStatic
        val MY_SQL_CONTAINER: MySQLContainer<*> =
            // image for linux/arm64/v8 m1 support
            DockerImageName.parse("mysql/mysql-server:8.0.26")
                .asCompatibleSubstituteFor("mysql")
                .let { compatibleImageName -> MySQLContainer<Nothing>(compatibleImageName) }
                .apply {
                    withDatabaseName(DATABASE_NAME)
                    withUsername(USERNAME)
                    withPassword(PASSWORD)
                    withEnv("MYSQL_USER", USERNAME)
                    withEnv("MYSQL_PASSWORD", PASSWORD)
                    withEnv("MYSQL_ROOT_PASSWORD", PASSWORD)
                    withCommand("--character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci")
                    withUrlParam("useTimezone", "true")
                    withUrlParam("serverTimezone", "Asia/Seoul")
                    withCreateContainerCmdModifier {
                        it.withPortBindings(
                            PortBinding(
                                Ports.Binding.bindPort(33006),
                                ExposedPort(3306)
                            )
                        )
                            .withHostName("app-host")
                    }
                    start()
                }

        const val DATABASE_NAME: String = "fone"
        const val USERNAME: String = "root"
        const val PASSWORD: String = "fone-flim-be"
    }
}