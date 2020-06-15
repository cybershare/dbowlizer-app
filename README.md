[![DOI](https://zenodo.org/badge/22285/cybershare/dbowlizer-app.svg)](https://zenodo.org/badge/latestdoi/22285/cybershare/dbowlizer-app)
[![Build Status](https://img.shields.io/badge/build-passing-brightgreen.svg)]()
[![Endpoint Status](https://img.shields.io/badge/endpoint-offline-red)]()

# DBOwlizer Project
DBOwlizer is a tool that automatically generates ontologies from relational databases. DBOWLizer allows a user to: i) automatically represent the database schema of a relational database using the relational-model-ontology, ii) represent heuristics to map relational database elements to ontology terms using the relational-to-ontology-mapping ontology, iii) generate an OWL ontology based on the structure of a relational database schema (including views) and said heuristics, and iv) generate R2RML mappings from the relational database schema to the extracted ontology.

## Installation
Requirements:
+ MySQL 5.6 or higher.
+ Maven 
+ Java JDK 1.8 or above.  
+ Tomcat 8 or above. (If setting up as webservice)


## Usage
As Standalone Application:
+ Import Maven project into Eclipse.
+ Open the schema2owl.config.properties file located on src/main/resources.
+ Change the output path under the #Output Settings comment, make sure to have r/w permissions on the desired path.
+ Change the Default Database Settings to your own MySQL configuration.
+ Build the project with Maven.
+ Run as Java Application (Main class located on the com.cybershare.dbowlizer.main package).


As Webservice Endpoint:
+ Change the service parameter at schema2owl.config.properties to "on".
+ Uncomment the webservice setting parameters and provide an absolute path as required.
+ Make sure to copy the local input ontologies to a directory with r/w access.
+ Change your outputURL according to your webserver settings pointing to the physical output path.
+ Generate WAR file with MAVEN.
+ Import WAR into Tomcat 8.


DBOwlizer Webservice Endpoint:
Our service endpoint can be accesed at http://visko.cybershare.utep.edu/dbowlizer-endpoint/
The endpoint is currently configured to use our test case database (employees). We will soon enable the webservice to
specify the database connection to MySQL databases with open external access.


## Testing
+ Our current test inputs and outputs can be consulted at our project website. 


## Contributing
1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request :D


## Credits
+ Project PI: Natalia Villanueva Rosales
+ Implementation Lead: Luis Garnica
+ Lead Developer: Diego Aguirre
+ Assistant Developer: Erick Garcia
+ Assistant Developer: Eric Camacho


Contributors:
+ Michel Dumontier
+ Georgia Almodovar


## Future Work
1. Improve parsing mechanism for extracted view queries.
2. Interfacing Web Service with open external database connections E.G. GoDaddy.
3. Extending to PostgreSQL and Oracle database managers.
4. Code refactoring to improve maintainability and code readability.
5. Code Documentation (JavaDoc Release).


## Acknowledgment
This work used resources from Cyber-ShARE Center of Excellence, which is supported by National Science Foundation grant number HRD-0734825. Unless otherwise stated, work by Cyber-ShARE is licensed under a Creative Commons Attribution 3.0 Unported License. Permissions beyond the scope of this license may be available at http://cybershare.utep.edu/content/cyber-share-acknowledgment.


## Project Website
+ http://dbowlizer.cybershare.utep.edu
