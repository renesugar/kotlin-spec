# Kotlin grammar tests

[![TeamCity (simple build status)](https://img.shields.io/teamcity/https/teamcity.jetbrains.com/e/Kotlin_spec_GrammarTests.svg?style=flat)](https://teamcity.jetbrains.com/viewType.html?buildTypeId=Kotlin_Spec_GrammarTests&branch_Kotlin_dev=%3Cdefault%3E&tab=buildTypeStatusDiv)

## Description

This tests infrastructure is used to testing the [Kotlin grammar (ANTLR)](https://github.com/JetBrains/kotlin-spec/tree/spec-rework/src/grammar) (the parser generated by grammar is being tested).

To testing, Kotlin compiler [PSI](https://github.com/JetBrains/kotlin/tree/master/compiler/testData/psi) and [diagnostics](https://github.com/JetBrains/kotlin/tree/master/compiler/testData/diagnostics/tests) tests are used.

## How to use

After `kotlin-spec` repository cloning, open the `src/grammar/tests` folder in IDEA as a project and configure as a gradle project.

After this, do the following:
1) Download compiler tests using the run script: `./getCompilerTests.sh` (you must have `svn` installed for this);
2) Prepare compiler diagnostics tests using gradle task: `./gradlew prepareDiagnosticsCompilerTests`;
3) Remove not yet included tests if you want: `./gradlew removeNewTests` (otherwise, those compiler tests for which the ANTLR parse tree has not yet been built and pushed will be used; therefore such tests will fail **on first run**);
4) Run tests: `./gradlew test` (at this step, the grammar files are also automatically copied to `src/main/antlr` and a parser and lexer is generated to `src/main/java/{org.jetbrains.kotlin.grammar.parser}`).