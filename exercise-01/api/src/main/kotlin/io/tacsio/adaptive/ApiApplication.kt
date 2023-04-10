package io.tacsio.adaptive

import io.tacsio.adaptive.mapek.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class ApiApplication {

    @Bean
    fun startManagement(@Value("\${mapek.enabled:false}") enabled: Boolean) = CommandLineRunner {
        val knowledge = Knowledge()
        val executor = Executor(knowledge)
        val planner = Planner(knowledge, executor.executorInChannel)
        val analyzer = Analyzer(knowledge, planner.plannerInChannel)
        val monitor = Monitor(knowledge, analyzer.analyzerInChannel)

        if (enabled) {
            GlobalScope.launch { monitor.start() }
            GlobalScope.launch { analyzer.start() }
            GlobalScope.launch { planner.start() }
            GlobalScope.launch { executor.start() }
        }
    }
}

fun main(args: Array<String>) {
    runApplication<ApiApplication>(*args)
}
