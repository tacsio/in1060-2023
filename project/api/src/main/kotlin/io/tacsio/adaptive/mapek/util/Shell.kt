package io.tacsio.adaptive.mapek.util

class Shell {
    companion object {
        fun execProcess(command: String): Pair<String, String> {
            val process = Runtime.getRuntime().exec(command)

            val output = String(process.inputStream.readAllBytes(), Charsets.UTF_8)
            val err = String(process.errorStream.readAllBytes(), Charsets.UTF_8)

            process.destroy()

            return Pair(output, err)
        }

        fun numberOfReplicas(): Int {
            val command = "/app/kubectl get deployments.apps mapek -ojsonpath='{.status.availableReplicas}'"
            val result = execProcess(command)

            return result.first.replace("'", "").toInt()
        }
    }
}
