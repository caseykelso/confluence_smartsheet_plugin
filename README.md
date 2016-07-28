# confluence_smartsheet_plugin
Confluence Plug-in connects Smartsheet &amp; Confluence

# Setup Environment
* Install Atlas (https://developer.atlassian.com/docs/getting-started/set-up-the-atlassian-plugin-sdk-and-build-a-project/install-the-atlassian-sdk-on-a-linux-or-mac-system)
* Install Oracle JDK 1.8 (OPENJDK is not supported by Atlassian)
* Setup JAVA_HOME and env PATH
* git clone git@github.com:caseykelso/confluence_smartsheet_plugin.git
* cd confluence_smartsheet_plugin

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


