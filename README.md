# confluence_smartsheet_plugin
Confluence Plug-in connects Smartsheet &amp; Confluence

# Setup Environment (OSX)
* Install Homebrew
```
/usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
brew update && brew upgrade
```
* Install Atlas (Atlassian Development Environment)
```
brew tap atlassian/tap
brew install atlassian/tap/atlassian-plugin-sdk
```
* Install Oracle JDK 1.8 (OPENJDK is not supported by Atlassian)
* Setup JAVA_HOME and env PATH

* Install Tools & Dependencies
```
brew install maven ant git
```

# Setup Environment (Ubuntu/Debian/Mint Linux)
* Install Tools & Dependencies
```
sudo apt-get update && sudo apt-get dist-upgrade
sudo apt-get install maven ant git
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


