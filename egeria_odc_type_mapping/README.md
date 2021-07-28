# ODPi/Egeria - Oracle Data Catalog - Type Mapping

This Page is used to document the plan on how to map between 
Egeria and OCD metadata types. As of the [Open Metadata Repository Connector Implementation Instructions](https://wiki.lfaidata.foundation/display/EG/Implement+an+Open+Metadata+Repository+Connector) having a plan on how to map types is a prerequisite before implementing a connector.

This mapping as based on the [Egeria Metadata
Model](https://egeria.odpi.org/open-metadata-implementation/repository-services/docs/metadata-meta-model.html)
and the [Oracle Data Catalog
Types](https://docs.oracle.com/en-us/iaas/api/#/en/data-catalog/20190325/), or its [Java API Docs](https://docs.oracle.com/en-us/iaas/tools/java/1.36.5/com/oracle/bmc/datacatalog/model/package-summary.html).


At a starting point, we are going to map the information, which is harvested by Oracle Data Catalog 
from a database.


|Egeria Type           |ODC Type            |Comments            |Examples            |
|----------------------|--------------------|--------------------|--------------------|
|Database              |Data Asset          |                    |database, filesystem|
|RelationalDBSchemaType|Folder              |                    |DB schema, directory|
|RelationalTable       |Entity              |                    |Table, File         |
|RelationalColumn      |Attribute           |                    |Column, ...         |

