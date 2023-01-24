package com.fone.filmone.common.config

import io.vertx.core.Vertx
import io.vertx.mysqlclient.MySQLConnectOptions
import io.vertx.mysqlclient.MySQLPool
import io.vertx.sqlclient.Pool
import io.vertx.sqlclient.PoolOptions
import io.vertx.sqlclient.SqlConnectOptions
import org.hibernate.reactive.pool.impl.DefaultSqlClientPool
import java.net.URI

class MysqlConnectionPool : DefaultSqlClientPool() {
    override fun createPool(
        uri: URI,
        connectOptions: SqlConnectOptions,
        poolOptions: PoolOptions,
        vertx: Vertx
    ): Pool {
        return MySQLPool.pool(
            vertx,
            MySQLConnectOptions()
                .setHost(connectOptions.host)
                .setUser(connectOptions.user)
                .setPassword(connectOptions.password)
                .setDatabase(connectOptions.database),
            poolOptions
        )
    }
}

//class VertxMySqlonnectionPoolConfiguration : DefaultSqlClientPoolConfiguration() {
//    private lateinit var user: String
//    private var pass: String? = null
//    private var cacheMaxSize: Int? = null
//    private var sqlLimit: Int? = null
//    override fun connectOptions(uri: URI): SqlConnectOptions {
//        if (uri.scheme == "mysql") {
//            val apply = SqlConnectOptions()
//                .setUser(user)
//                .apply {
//                    pass?.let { password = it }
//                    //enable the prepared statement cache by default
//                    cachePreparedStatements = true
//
//                    cacheMaxSize?.run {
//                        if (this <= 0) {
//                            cachePreparedStatements = false
//                        } else {
//                            preparedStatementCacheMaxSize = this
//                        }
//                    }
//
//                    sqlLimit?.run { setPreparedStatementCacheSqlLimit(this) }
//                }
//            return apply
//        }
//
//        return super.connectOptions(uri)
//    }
//
//    override fun configure(configuration: MutableMap<Any?, Any?>) {
//        user = ConfigurationHelper.getString(Settings.USER, configuration)
//        pass = ConfigurationHelper.getString(Settings.PASS, configuration)
//        cacheMaxSize = ConfigurationHelper.getInteger(Settings.PREPARED_STATEMENT_CACHE_MAX_SIZE, configuration)
//        sqlLimit = ConfigurationHelper.getInteger(Settings.PREPARED_STATEMENT_CACHE_SQL_LIMIT, configuration)
//        super.configure(configuration)
//    }
//}