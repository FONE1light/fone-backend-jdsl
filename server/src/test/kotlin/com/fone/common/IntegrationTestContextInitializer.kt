package com.fone.common

import com.github.dockerjava.api.model.PortBinding
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.utility.DockerImageName

class IntegrationTestContextInitializer :
    ApplicationContextInitializer<ConfigurableApplicationContext> {

    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        val DATABASE_NAME: String = "fone"
        val USERNAME: String = "root"
        val PASSWORD: String = "fone-flim-be"

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
                    it.withPortBindings(PortBinding.parse("33006:3306"))
                }
                start()
            }

        val REDIS_CONTAINER =
            GenericContainer(
                DockerImageName.parse("redis:5.0.3-alpine")
            ).withCreateContainerCmdModifier {
                it.withPortBindings(PortBinding.parse("6380:6379"))
            }

        REDIS_CONTAINER.start()

        System.setProperty("spring.redis.host", REDIS_CONTAINER.host)
        System.setProperty("spring.redis.port", REDIS_CONTAINER.getMappedPort(6379).toString())
    }
}
