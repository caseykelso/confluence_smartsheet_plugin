# Atlassian Confluence Smartsheet Plug-in 
Confluence Plug-in connects Smartsheet &amp; Confluence

# License
Apache 2.0 License - see [LICENSE](https://github.com/caseykelso/confluence_smartsheet_plugin/blob/master/LICENSE) file

# Tested and Supported Atlassian Confluence Versions
* Confluence v5.9.12

# 3rd-Party Dependencies
| Package | Version | Source |
| ------- | ------- | ------ |
| com.smartsheet.smartsheet-sdk-java | 2.0.4 | [Github](https://github.com/smartsheet-platform/smartsheet-java-sdk) |
| org.jsoup.jsoup | 1.9.2 | [Github](https://github.com/jhy/jsoup) |
| com.atlassian.templaterenderer | 3.0.0 | [Atlassian](https://developer.atlassian.com/docs/getting-started/set-up-the-atlassian-plugin-sdk-and-build-a-project/set-up-the-sdk-prerequisites-for-linux-or-mac) |
| com.google.guava | 19.0 | [Github](https://github.com/google/guava) |
| com.atlassian.plugin | Latest | [Atlassian](https://developer.atlassian.com/docs/getting-started/set-up-the-atlassian-plugin-sdk-and-build-a-project/set-up-the-sdk-prerequisites-for-linux-or-mac) |


# Architecture
![alt text](https://github.com/caseykelso/confluence_smartsheet_plugin/blob/master/documentation/confluence_smartsheet.png "Confluence Smartsheet Architecture")

# Source Code
* JavaDoc (TBD)

| File                          |  Description                                                            |
| ----------------------------- | ----------------------------------------------------------------------- |
| pom.xml                       | Maven project file |
| README                        | This documentation file | 
| LICENSE                       | Software license file - Apache v2.0 |
| smartsheet.properties.example | smartsheet api token configuration file, rename to smartsheet.properties |
| build.xml                     | ANT build configuration for this project |
| RESTClient.java               | Standalone RESTClient that wraps Smartsheet SDK calls and presents the final report |
| SmartsheetCLI.java            | Command line interface for this project, standalone application for iterative incremental testing |
| SmartsheetMacro.java          | Atlassian Macro implementation source code |
| SmartsheetMacroServlet.java   | Atlassian Macro administrator menu menu source code |
| atlassian-plugin.xml          | Manifest for the Atlassian Macro Plug-in |



# Setup Environment (OSX)
## Install Homebrew
```
/usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
brew update && brew upgrade
```
## Install Atlas (Atlassian Development Environment)
```
brew tap atlassian/tap
brew install atlassian/tap/atlassian-plugin-sdk
```
* Install Oracle JDK 1.8 (OPENJDK is not supported by Atlassian)
* Setup JAVA_HOME and env PATH

# Install Tools & Dependencies
```
brew install maven ant git
```

# Setup Environment (Ubuntu/Debian/Mint Linux)
## Install Tools & Dependencies
```
sudo add-apt-repository ppa:webupd8team/java
sudo sh -c 'echo "deb https://sdkrepo.atlassian.com/debian/ stable contrib" >>/etc/apt/sources.list'
sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys B07804338C015B73
sudo apt-get install apt-transport-https
sudo apt-get update && sudo apt-get dist-upgrade
sudo apt-get install maven ant git atlassian-plugin-sdk oracle-java8-installer
```

# Setup working copy
```
mkdir confluence_smartsheet
cd confluence_smartsheet
repo init -m smartsheetplugin.xml -u git://github.com/caseykelso/confluence_smartsheet_plugin
repo sync
```

# Build CLI Tool & Plug-in
ant all

# Start-up Dev Environment
./confluence.sh

# Deploy Plug-in to Dev Environment
cd smartsheetplugin
atlas-mvn package

# Browser / Client
http://localhost:1990/confluence

# Rev Version
cd smartsheetplugin
atlas-mvn versions:set -DnewVersion=x.y-SNAPSHOT

# Release
ant release


