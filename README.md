# Setup
## Installing ANTLR4 for Command Line
### Download and configure BASH PROFILE
ANTLR4 is needed to parse method names extracted by `javap` command.

Follow these instructions to install ANTLR4 in your Mac/Linux:
```shell script
cd /usr/local/lib
sudo curl -O http://www.antlr.org/download/antlr-4.7.2-complete.jar
# edit BASH profile
nano ~/.bash_profile
# Paste the following
export CLASSPATH=".:/usr/local/lib/antlr-4.7.2-complete.jar:$CLASSPATH"
alias antlr4='java -jar /usr/local/lib/antlr-4.7.2-complete.jar'
alias a4='java -jar /usr/local/lib/antlr-4.7.2-complete.jar'
alias grun='java org.antlr.v4.gui.TestRig'
```

In case you read this in late 2020, the ANTLR version may have changed.
Go to download page <https://www.antlr.org/download.html>.
Then look for this text within the page "All users should download the ANTLR tool itself...". you will find the download link in the text.

Reference:
<https://riptutorial.com/antlr/example/10198/installing-for-command-line-use>

### Compatibility
Apparently version 4.7.2 is the last version compatible with Java 8.

### Aliases
You may have noticed there are aliases `antlr4` and `a4` in the instalation process.
They are very useful to avoid writing long ANTLR commands.
These commands are used to generate lexer/parser scripts from G4 grammar file.
We are not going to detail on how to create such files yet. 
Please keep reading.

### Testing ANTLR4 on the Command Line
This is a very nice video explaining how to test ANTLR4 on both:
- Terminal
- IntelliJ
<https://www.youtube.com/watch?v=eW4WFgRtFeY&feature=youtu.be>

## Installing ANTLR4 for Maven
This guide explains the whole process. But if you want to know the whole detail. Read all the following sub-sections.
Also make sure to read comments to the POM file.
References:
<https://www.baeldung.com/java-antlr>
This is ANTLR4 official github site
https://github.com/antlr/antlr4/blob/master/doc/java-target.md

### ANTLR4 Maven Plugin
This is official's ANTLR4 maven plugin page. It starts explaining how to add the plugin to the POM file.
<https://www.antlr.org/api/maven-plugin/latest/usage.html>
The usage section explains the command `mvn antlr4:antlr4` and all its arguments.
<https://www.antlr.org/api/maven-plugin/latest/antlr4-mojo.html>

### Give your Maven project access to ANTL4 compiled classes with build-helper-maven-plugin
In order to add generated sources to the sources pathes of a project the add-sources goal from the maven helper plugin can be used.
When the sources are generated they are immediately picked up by eclipse and compilation errors are avoided.

Reference: <http://www.jeeatwork.com/?p=166>

## Maven strategies to package JAR Artifacts
This is a very nice guide explaining all the pros & cons of differente packaging strategies:
<https://www.baeldung.com/executable-jar-with-maven>

This project uses IntelliJ's Artifact creation strategy, which is explained here:
<https://stackoverflow.com/questions/1082580/how-to-build-jars-from-intellij-properly/45303637#45303637>

Originally this project used *maven-assembly-plugin* because it lets you include other JAR dependencies into a single JAR file.
Official documentation here:
<https://maven.apache.org/plugins/maven-assembly-plugin/usage.html>

## Java 8 Grammar & Parser/Lexer file generation
Here you can find Java8 grammar files for Parser and Lexer. You will need them for this project:
<https://github.com/antlr/grammars-v4/tree/master/java/java8>

To generate Parser/Lexer/Listener Java classes from grammar files, do the following:
- As the initial baeldung guide <https://www.baeldung.com/java-antlr> explains, ANTLR4 plugin looks up for grammar files at *src/main/antlr4*.
  Copy the grammar files there, but make sure to use the same package structure of your solution.
  If your Main package is "com.example.app", then your grammar files should be copied to folder
  path: *src/main/antlr4/com.example/app*. The tree should look like this:
src/main/antlr4
└── com
    └── sopristec
        └── extractor
            ├── Java8Lexer.g4
            └── Java8Parser.g4

- Then execute `mvn antlr4:antlr4`. It will save the generated files to *src/<PACKAGE_PATH>/*

### There is one Gotcha
If you want to have your Java classes under a package structure, e.g. 'org.example.myclass', your grammar files must be under the same folder structure your Java classes package.
Otherwise, your classes won't have access to ANTLR4 generated files.

## ANTLR4 Tree walking
Here other ways to walk a tree, without an explicit Listening class:
<https://stackoverflow.com/questions/23305232/using-antlr-to-get-identifiers-and-function-names>
<https://stackoverflow.com/questions/15050137/once-grammar-is-complete-whats-the-best-way-to-walk-an-antlr-v4-tree>
<https://stackoverflow.com/questions/42882152/accessing-nested-parse-trees-java-antlr>

## Composing XML Extensions file for NewRelic Java Agent

This is a quick guide with examples on how to instrument a Standalone Java application: 
<https://docs.newrelic.com/docs/agents/java-agent/custom-instrumentation/java-xml-instrumentation-examples>

## Including Log4j2 dependencies in a JAR artifact

There is a very nice guide in this page <https://stackoverflow.com/questions/45933081/intellijs-artifacts-and-log4j-how-to-run>. Which summarizes to:

* created a Java project in IDEA
* added library dependencies to log4j-api and log4j-core, version 2.x
* added a main class that just logs a single line using log4j
* added an log4j2.xml to "src/main/resources" folder. Otherwise it won't be included inside the artifact.
* created an artifact:
    * Project Settings -> Artifacts -> "+" -> Jar -> from modules with dependencies
    * selected the main class
    * and kept the "JAR files from libraries" -> extract to the target JAR" selected
* build the artifact: Build -> Build Artifacts
* executed the artifact from the out/artifacts/test-artifact folder using "java -jar test-artifact"

### Gotcha when including Log4j2 in a JAR

It happend that we added log4j2 dependencies to POM file once the project had been progressed.
But when executing the *JAR Application*, it complained about missing dependencies.
The error is simple: when you edit the **Project Structure** to create an Artifact from your project,
it gets configured with the dependencies at the POM file at the moment, 
i.e. the Artifact configuration does not update dependencies automatically.

All you need to do is re-create the Artifact configuration. You can directly the dependecies configured for an
artifact at ".idea/artifacts/YOUR_ARTIFACT_NAME.xml"

### More references
<https://logging.apache.org/log4j/2.x/manual/configuration.html>
<https://medium.com/@ruhshan_ahmed/how-to-use-log4j-in-jar-applications-ed9b8f8ece98>
Different ways to configure:
<https://www.codejava.net/coding/how-to-configure-log4j-as-logging-mechanism-in-java>
