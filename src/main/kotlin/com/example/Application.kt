package com.example
import com.mysql.cj.jdbc.MysqlDataSource
import com.typesafe.config.ConfigFactory
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.flywaydb.core.Flyway
import org.flywaydb.core.extensibility.ResourceTypeProvider

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val conf = ConfigFactory.load()
    val mysqlDataSource = MysqlDataSource()
    mysqlDataSource.setURL(conf.getString("database.url"))

    val load = Flyway.configure().dataSource(
        mysqlDataSource
    )
        .validateMigrationNaming(true)
        .load()
    print(load.configuration.pluginRegister.getPlugins(ResourceTypeProvider::class.java))
    load.migrate()

}
