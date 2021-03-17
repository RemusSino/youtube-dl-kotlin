@file:JvmName("Main")

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

suspend fun main() = withContext(Dispatchers.IO) {
    val out = File(System.getProperty("user.home"), "Music")

    File("urls").forEachLine {
        launch { download(it, out) }
    }
}

private fun download(url: String, outDir: File) {
    if (url.startsWith("#"))
        return

    val process = ProcessBuilder("youtube-dl", "-x", "--audio-format", "mp3", url.substringBefore("&"))
        .directory(outDir)
        .inheritIO()
        .start()

    process.waitFor()

    if (process.exitValue() != 0) {
        println("for $url execution failed with code ${process.exitValue()}")
    }
}
