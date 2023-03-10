package com.fone.common

import com.github.dockerjava.api.model.ExposedPort
import com.github.dockerjava.api.model.PortBinding
import com.github.dockerjava.api.model.Ports
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
                    it.withPortBindings(
                        PortBinding(Ports.Binding.bindPort(33006), ExposedPort(3306))
                    ).withHostName("app-host")
                }
                start()
            }

        val REDIS_CONTAINER =
            GenericContainer(DockerImageName.parse("redis:5.0.3-alpine")).withExposedPorts(6379)

        REDIS_CONTAINER.start()

        System.setProperty("spring.redis.host", REDIS_CONTAINER.getHost())
        System.setProperty("spring.redis.port", REDIS_CONTAINER.getMappedPort(6379).toString())
    }
}
