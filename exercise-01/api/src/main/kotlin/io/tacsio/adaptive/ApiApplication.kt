package io.tacsio.adaptive

import io.tacsio.adaptive.mapek.Analyzer
import io.tacsio.adaptive.mapek.Executor
import io.tacsio.adaptive.mapek.Monitor
import io.tacsio.adaptive.mapek.Planner
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class ApiApplication {

    @Bean
    fun management() = CommandLineRunner {
        val executor = Executor()
        val planner = Planner(executor.executorChannel)
        val analyzer = Analyzer(planner.plannerChannel)
        val monitor = Monitor(analyzer.analyzerChannel)

        GlobalScope.launch {
            monitor.start()
        }

        GlobalScope.launch {
            analyzer.start()
        }

        GlobalScope.launch {
            planner.start()
        }

        GlobalScope.launch {
            executor.start()
        }
    }
}

fun main(args: Array<String>) {
    runApplication<ApiApplication>(*args)
}
