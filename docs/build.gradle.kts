import at.phatbl.shellexec.ShellExec
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
}

buildscript {
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.41")
    }
}

apply(plugin="kotlin")

group = "org.jetbrains.kotlin.spec"
version = "0.1"

val pdfBuildDir = "$buildDir/spec/pdf"
val scriptsDir = "$projectDir/scripts/build"
val ls: String = System.lineSeparator()

fun getScriptText(scriptName: String): String {
    val buildTemplate = File("$scriptsDir/$scriptName.sh").readText()

    return "PROJECT_DIR=$projectDir$ls$buildTemplate"
}

repositories {
    maven { setUrl("https://jitpack.io") }
    maven {
        setUrl("https://dl.bintray.com/vorpal-research/kotlin-maven")
    }
    mavenCentral()
}

java.sourceSets {
    "main" {
        java.srcDir("src/kotlin")
    }
}

dependencies {
    compile("ru.spbstu:g4toEBNF:0.0.0.2")
    compile("ru.spbstu:kotlin-pandoc:0.0.7")
    compile("ru.spbstu:simple-diagrammer:0.0.0.2")
    compile("com.github.ajalt:clikt:1.7.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

fun ShellExec.buildBySections(format: String, wrapper: String) {
    val ls = System.lineSeparator()
    val excludeFiles = setOf("grammar.generated", "index")
    val commands = mutableListOf("PROJECT_DIR=${project.rootDir}")
    val buildScriptsDir = "${project.rootDir}/docs/scripts/build"
    val buildTemplate = File("$buildScriptsDir/$wrapper").readText()

    File("${project.rootDir}/docs/src/md/kotlin.core").listFiles()?.forEach {
        val section = it.nameWithoutExtension

        if (it.extension != "md" || excludeFiles.contains(section))
            return@forEach

        commands.add(buildTemplate.replace("<section>", section))
    }

    workingDir = File(buildScriptsDir)
    command = commands.joinToString(ls)

    File("./build/spec/$format/sections").mkdirs()
}

tasks.create<Task>("prepareBuildPdf") {
    dependsOn("convertGrammar")

    doFirst { File(pdfBuildDir).mkdirs() }
}

tasks.create<ShellExec>("buildPdf") {
    dependsOn("prepareBuildPdf")

    doFirst {
        workingDir = File(scriptsDir)
        command = getScriptText("buildPdf")

        File("./build/spec/pdf").mkdirs()
    }
}

tasks.create<ShellExec>("buildPdfBySections") {
    dependsOn("prepareBuildPdf")

    doFirst { buildBySections("pdf", "buildPdfBySections.sh") }
}

tasks.create<JavaExec>("convertGrammar") {
    val inputFile = "./src/md/kotlin.core/Grammar.g4"
    val outputFile = "./src/md/kotlin.core/grammar.generated.md"

    inputs.file(inputFile)
    outputs.file(outputFile)

    classpath = java.sourceSets["main"].runtimeClasspath
    main = "org.jetbrains.kotlin.spec.ConvertGrammarKt"
    args = listOf("-i", inputFile, "-o", outputFile)
}

tasks.create<JavaExec>("execute") {
    classpath = java.sourceSets["main"].runtimeClasspath
    main = if (project.hasProperty("mainClass")) project.property("mainClass") as String else ""
    if (project.hasProperty("args")) {
        args = (project.property("args") as String).split(" ")
    }
    standardInput = System.`in`
    standardOutput = System.out
    errorOutput = System.err
}
