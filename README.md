# Shopware Upload-Tool
This is a simple connector to the Shopware-API, that allows you to upload and download data from and to Shopware with Excel-Spreadsheets.
This application is highly customized for my own needs, so if you want to use it, you might want to change some parts of it.

## Installation
### Requirements
* Java 1.7 or higher
* Maven 3.0.4 or higher (only for building the project)

### Installation from binary
To install without downloading the sources or modifying the files, simply download the Jar-File from the [lastest release](https://github.com/sebastianwieland/shopware-uploadtool/releases/latest).

Execute it by double-clicking on it, or by running `java -jar path/to/shopware-uploadtool-version-jar-with-dependencies.jar`

### Build
To build the project, navigate to the project-root. Then run maven by executing
```
mvn package
```
One executable Jar-File without, and one with all dependencies are generated in the target-directory. The one with dependencies is executable without further requirements.