# confluence_smartsheet_plugin
Confluence Plug-in connects Smartsheet &amp; Confluence

# Setup Environment
* Install Atlas (https://developer.atlassian.com/docs/getting-started/set-up-the-atlassian-plugin-sdk-and-build-a-project/install-the-atlassian-sdk-on-a-linux-or-mac-system)
* Install JDK 1.8 or later and setup JAVA_HOME and Paths correctly

# Build
* cd smartsheetplugin
* atlas-compile

# Package
* cd smartsheetplugin
* atlas-package

# Start-up Dev Environment
* cd smartsheetplugin
* atlas-run-standalone

# Deploy Plug-in to Dev Environment
* cd smartsheetplugin
* atlas-run

# Release
* mvn release:prepare
* mvn release:peform

