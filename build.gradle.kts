import at.phatbl.shellexec.ShellExec
import java.nio.file.Paths

plugins {
    id("at.phatbl.shellexec") version "1.1.3"
}

val htmlBuildDir = "$buildDir/spec/html"
val resourcesBuildDir = "$htmlBuildDir/resources"
val jsBuildDir = "$resourcesBuildDir/js"
val scriptsDir = "$projectDir/scripts/build"
val ls: String = System.lineSeparator()

fun getScriptText(scriptName: String): String {
    val buildTemplate = File("$scriptsDir/$scriptName.sh").readText()

    return "PROJECT_DIR=$projectDir$ls$buildTemplate"
}

tasks.create<Task>("prepareBuildHtml") {
    dependsOn("docs:convertGrammar")
    dependsOn("front-end:webpack-bundle")
    dependsOn("copyResources")
    dependsOn("copyBuiltJs").mustRunAfter("front-end:webpack-bundle")

    doFirst {
        File(jsBuildDir).mkdirs()
    }
}

tasks.create<Copy>("copyResources") {
    from("$projectDir/front-end/resources")
    into(resourcesBuildDir)
}

tasks.create<Copy>("copyBuiltJs") {
    from("$projectDir/front-end/build/js")
    into(jsBuildDir)
}

tasks.create<Task>("buildPdf") {
    dependsOn("docs:buildPdf")
}

tasks.create<Task>("buildPdfBySections") {
    dependsOn("docs:buildPdfBySections")
}

tasks.create<ShellExec>("buildHtml") {
    dependsOn("prepareBuildHtml")

    doFirst {
        workingDir = File(scriptsDir)
        command = getScriptText("buildHtml")
    }
}

tasks.create<ShellExec>("buildHtmlBySections") {
    dependsOn("prepareBuildHtml")

    doFirst {
        workingDir = File(scriptsDir)
        command = getScriptText("buildHtmlBySections")

        Paths.get("$htmlBuildDir/sections").toFile().apply { deleteRecursively(); mkdirs() }
    }
}
