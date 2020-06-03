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

### Java 8 Grammar & Parser/Lexer file generation
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

#### There is one Gotcha
If you want to have your Java classes under a package structure, e.g. 'org.example.myclass', your grammar files must be under the same folder structure your Java classes package.
Otherwise, your classes won't have access to ANTLR4 generated files.

### Give your Maven project access to ANTL4 compiled classes with build-helper-maven-plugin
In order to add generated sources to the sources pathes of a project the add-sources goal from the manve helper plugin can be used.
When the sources are generated they are immediately picked up by eclipse and compilation errors are avoided.

Reference: <http://www.jeeatwork.com/?p=166>

### Maven strategies to package JAR Artifacts
This is a very nice guide explaining all the pros & cons of differente packaging strategies:
<https://www.baeldung.com/executable-jar-with-maven>

The strategy used by this project is *maven-assembly-plugin* because it lets you include other JAR dependencies into a single JAR file.
Official documentation here:
<https://maven.apache.org/plugins/maven-assembly-plugin/usage.html>

## ANTLR4 Tree walking
Here other ways to walk a tree, without an explicit Listening class:
<https://stackoverflow.com/questions/23305232/using-antlr-to-get-identifiers-and-function-names>
<https://stackoverflow.com/questions/15050137/once-grammar-is-complete-whats-the-best-way-to-walk-an-antlr-v4-tree>
<https://stackoverflow.com/questions/42882152/accessing-nested-parse-trees-java-antlr>

