package io.tacsio.adaptive

import io.tacsio.adaptive.mapek.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class ApiApplication {

    private val log = LoggerFactory.getLogger(ApiApplication::class.java)

    @Bean
    fun startManagement(@Value("\${api.mapek.enabled}") enabled: Boolean) = CommandLineRunner {
        val knowledge = Knowledge()
        val executor = Executor(knowledge)
        val planner = Planner(knowledge, executor.executorInChannel)
        val analyzer = Analyzer(knowledge, planner.plannerInChannel)
        val monitor = Monitor(knowledge, analyzer.analyzerInChannel)

        if (enabled) {
            log.debug("Running monitor.")
            GlobalScope.launch { monitor.start() }
            log.debug("Running analyzer.")
            GlobalScope.launch { analyzer.start() }
            log.debug("Running planner.")
            GlobalScope.launch { planner.start() }
            log.debug("Running executor.")
            GlobalScope.launch { executor.start() }
        }
    }
}

fun main(args: Array<String>) {
    runApplication<ApiApplication>(*args)
}
