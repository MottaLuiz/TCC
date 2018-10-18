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
        
        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        path = path.substring(0, path.length() - 2);
        //System.out.println(path);
        String resourcesPath = path + "\\src\\main\\resources\\OntologiaCasa.owl";
        InputStream in = new FileInputStream(new File(resourcesPath));
//InputStream in = new FileInputStream(new File("D:\\faculdade\\TCC\\TCC - versão final\\TCC\\src\\main\\resources\\OntologiaCasa.owl"));
 
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
"SELECT ?subject ?object ?sublabel ?oblabel \n"  +
"	WHERE { ?subject  owl:someValuesFrom ?object ."
        + " ?object rdfs:label ?oblabel . "
        //+ "?object rdfs:subPropertyOf ?object ."
       // + "?subject rdfs:label \"Sala_local@pt\" . "
        + "OPTIONAL { ?subject rdfs:label ?sublabel. } "
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

    public static boolean consultar(FrameTarefa frame) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
