# DBOwlizer Project
DBOwlizer is a tool that automatically generates ontologies from relational databases. DBOWLizer allows a user to: i) automatically represent the database schema of a relational database using the relational-model-ontology, ii) represent heuristics to map relational database elements to ontology terms using the relational-to-ontology-mapping ontology, iii) generate an OWL ontology based on the structure of a relational database schema (including views) and said heuristics, and iv) generate R2RML mappings from the relational database schema to the extracted ontology.

## Installation
Requirements:
+ MySQL 5.6 or higher.
+ Maven 
+ Java JDK 1.8 or above.  
+ Tomcat 8 or above. (If setting up as webservice)

Configuration:





## Usage
As Standalone Application:


DBOwlizer Webservice Endpoint:


## Testing


## Contributing
1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request :D


## Credits
Project PI: Natalia Villanueva Rosales
Implementation Lead: Luis Garnica
Lead Developer: Diego Aguirre
Assistant Developer: Erick Garcia
Assistant Developer: Eric Camacho

Contributors:
Michel Dumontier
Georgia Almodovar

## Future Work
1. Improve parsing mechanism for extracted view queries.
2. Code Documentation (JavaDoc Release).
3. Interfacing Web Service with open external database connections E.G. GoDaddy.
4. Extending to PostgreSQL and Oracle database managers.


## License
<a rel="license" href="http://creativecommons.org/licenses/by/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/4.0/">Creative Commons Attribution 4.0 International License</a>.
]]>