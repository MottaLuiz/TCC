/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;


/**
 *
 * @author Luiz
 */
public class GerenciadorCasa {

    public static void consultar() throws FileNotFoundException, IOException {
    
        // Open the bloggers RDF graph from the filesystem
InputStream in = new FileInputStream(new File("C:\\Users\\Luiz\\Documents\\NetBeansProjects\\TCC\\src\\main\\resources\\OntologiaCasa.owl"));
 
// Create an empty in-memory model and populate it from the graph
Model model = ModelFactory.createMemModelMaker().createDefaultModel();
model.read(in,null); // null base URI, since model URIs are absolute
in.close();
 
// Create a new query
String queryString = 
    "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
"SELECT ?subject ?object ?class ?subclass \n"  +
"	WHERE { ?subject  rdfs:subClassOf ?object ."
        + "?object rdfs:label \"Locais@pt\" ."
        + "?subject rdfs:label ?subclass . "
        + "?object rdfs:label ?class . "
        +  "      }";
 
Query query = QueryFactory.create(queryString);
 
// Execute the query and obtain results
QueryExecution qe = QueryExecutionFactory.create(query, model);
ResultSet results = qe.execSelect();
 
// Output query results 
ResultSetFormatter.out(System.out, results, query);
 
// Important - free up resources used running the query
qe.close();
       
        
    }
    
}
