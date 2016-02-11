# Shopware Upload-Tool
This is a simple connector to the Shopware-API, that allows you to upload and download data from and to Shopware with Excel-Spreadsheets.
This application is highly customized for my own needs, so if you want to use it, you might want to change some parts of it.

## Installation
### Requirements
* Java 1.8 or higher
* Maven 3.0.4 or higher (only for building the project)

### Installation from binary
To install without downloading the sources or modifying the files, simply download the Jar-File from the [lastest release](https://github.com/sebastianwieland/shopware-uploadtool/releases/latest).

Execute it by double-clicking on it, or by running `java -jar path/to/shopware-uploadtool-`_`version`_`-jar-with-dependencies.jar`

### Building
To build the project, navigate to the project-root. Then run maven by executing
```
mvn package
```
One executable Jar-File without, and one with all dependencies are generated in the target-directory. The one with dependencies is executable without further requirements.

## Usage
This program allows to export and import product-data to and from Shopware via Excel-Spreadsheets.

### Opening the program
* Execute the jar-file by double-clicking on it, or by executing `java -jar path/to/shopware-uploadtool-`_`version`_`-jar-with-dependencies.jar` in a terminal window.
* Log-in with your Shopware-Credentials. An API-key can be generated in the Shopware-Backend User settings. The FTP-credentials are not used yet.
* You may store your credentials using the button on the bottom of the window.

### Excel-File structure
The Excel-File, that is used for exporting and importing data, must have a specific structure. An example can be generated with the button in the Information-Tab. It must be in the Office OpenXML-Format (.xlsx) and must contain of two spreadsheets, one for Articles and one for Variants.

#### Columns
Cells / Columns, that are empty, are ignored. To empty a column, you have to insert an empty string. That can be done with an excel-formula: `=''`. Columns, that don't exist, are treated like empty columns.

There are following columns:
* **Shopware-ID**: Internal ID of an article in Shopware. This is never seen in the storefront and can't be set manually. When uploading existing articles, this column can be used as identification for the article. When uploading new articles, this column is ignored.
* **Artikelnummer**: User-defined ID for the article. This can be displayed in the storefront and must be set manually. When uploading new articles, the upload fails, if this value isn't set or unique. When uploading existing articles, this value is used as identification, unless a Shopware-ID is provided.
* **Aktiv**: Wether or not this article is active, i.e. it gets displayed in the storefront. Possible values: 0 = inactive, 1 = active.
* **Konfigurator-Gruppen**: For Articles with variants, here you must provide all configurator-groups, i.e. properties that change with every variant. Multiple groups are delimited with a line-break.
* **Variante**: Here, the configurator-group values for the main variant are defined. Multiple values are delimited with a line-break. Syntax: `configurator-group-name = configurator-group-value`. Example: `Color = Red`.
* **Variante Aktiv**: Wether or not this variant is active.
* **Neue Artikelnummer**: If you want to change the _Artikelnummer_ of an article, you can provide a new one here.
* **Hervorgehoben**: Wether or not an article is highlighted in the storefront. Possible values: 0 = not higlighted, 1 = highlighted.
* **Hersteller-ID**: The ID of the supplier in Shopware.
* **EAN**: The EAN of an article.
* **Name**: The name of an article.
* **Kurzbeschreibung**: A short description for the article.
* **Beschreibung**: A long description for the article.
* **Varianten-Zusatztext**: A short description for the variant, which gets displayed next to the name
* **Preise**: All prices for all customer-groups. Multiple prices are delimited with line-breaks. Syntax: `price (pseudoprice) from->to [customer group key]`. Pseudoprice, from->to and customer group key can be omitted. from->to can be used to define prices for specific amounts of the article. Example: `20 1->10` (new line) `17.50 11->X` means, that each article costs 20, if you buy 10 or less of this article, and 17.50, if you buy more than 10 of this article.
* **Preise ersetzen**: If you update the prices, this column specifies wether you want to keep the old prices or replace them with new ones. Values: 0 = keep old prices, append new prices; 1 = drop old prices, replace with new prices.
* **Individuelle Optionen**: This uses the plugin 'Custom Products'. Here, you specify the ID of the CustomizingGroup.
* **Gravurschema**: an URL to an image-path, where the scheme for the engraving is displayed.
* **Template**: Here you can specify to use a different template than the standard one (`/frontend/detail/index.tpl`). The template must be stored in the `/frontend/detail/` template directory.
* **Suchbegriffe**: Meta-Keywords.
* **Kategorien**: IDs of all categories for this article, delimited by new-lines.
* **Kategorien ersetzen**: If you update the categories, this column specifies wether you want to keep the old categories or replace them with new ones. Values: 0 = keep old categories, append new categories; 1 = drop old categories, replace with new categories.
* **Eigenschafts-Set-ID**: ID of the filter-group for this article.
* **Eigenschaften**: Properties of this article. If a property doesn't exist, it will be created as a filterable property. Multiple properties are delimited by new-lines. Syntax: `property-name = property-value`.
* **Bestand**: Article-stock.
* **Mindestbestand**: Minimal article-stock for the article to be visible in the storefront.
* **Versandkostenfrei**: Wether or not the article is shipping-free. Values: 0 = not shipping-free, 1 = shipping-free.
* **Verfügbar ab**:  This date is displayed as the first shipping-date for an article, if provided. This value must be either formatted as date in excel, or as string with the syntax `YYYY-MM-DD`.
* **Erscheinungsdatum**: The date of first upload of this article.
* **Lieferzeit**: Shipping-time in days.
* **Benachrichtigung**: If this article is not available, this column specifies wether or not customers can assign for a notification when tis article is active again. Values: 0 = no notification, 1 = notification active.
* **Verpackungseinheit**: Pack-unit of the article.
* **Inhalt**: Volume of the article.
* **Referenzeinheit**: Reference volume of the article.
* **Höhe**: Height.
* **Breite**: Width.
* **Gewicht**: Weight.
* **Steuergruppen-ID**: ID of the tax-group in shopware.
* **Bilder**: URLs to images, which are downloaded by shopware for this article. Multiple images are delimited with new-lines. Main image is marked with the prefix `main:`.
* **Bilder ersetzen**: If you update the article, this value specifies if you want to keep or drop the existing images for this article. Values: 0 = keep old images, append new ones, 1 = drop old images, replace with new ones.

For variants: many of these columns do not exists, but there are only few columns, that do exist only for variants: 
* **Varianten-Shopware-ID**: The internal ID for this variant. Cannot be set manually. This value can be used as identification for the variant when updating.
* **Varianten-Artikelnummer**: The id, that's visible in the storefront. This value is used as identification when updating, unless a _Varianten-Shopware-ID_ is provided.
* **Eltern-Shopware-ID**: The ID of the parent article.
* **Eltern-Artikelnummer**: The _Artikelnummer_ of the parent article. This column is used as identification for the parent-article, unless an _Eltern-Shopware-ID_ is provided.
* **Neue Varianten-Artikelnummer**: If you want to replace the _Varianten-Artikelnummer_, you can do it here.
