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
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.update.Update;
import org.apache.jena.update.UpdateAction;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;

/**
 *
 * @author Luiz
 */
public class GerenciadorCasa {

    public static boolean consultar() throws FileNotFoundException, IOException {

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
        model.read(in, null); // null base URI, since model URIs are absolute
        in.close();

// Create a new query
        String queryString
                = "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
                + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
                + "DELETE { ?disp ?prop 'desligado' }\n"
                + "INSERT { ?disp ?prop 'ligado' }\n"
                + "WHERE\n"
                + "  { ?prop rdfs:label \"Estado@pt\"  ."
                + " ?disp rdfs:label \"som_varanda@pt\""
                + "  } ";

        UpdateRequest up = UpdateFactory.create(queryString);

// Execute the query and obtain results
        UpdateAction.execute(up, model);

//while (results.hasNext()){
//System.out.println(results.next().get("object").asResource().listProperties().toList().toString());
//}
// Output query results 
// Important - free up resources used running the query
        return ConsultarUP("som_varanda", "ligado", model);

    }

    public static boolean consultarDispositivo(String local, String disp) throws FileNotFoundException, IOException {

        boolean res;
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
        model.read(in, null); // null base URI, since model URIs are absolute
        in.close();

// Create a new query
        String queryString
                = "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
                + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
                + "ASK {?local ?property ?object . "
                + "?local rdfs:label \"" + local + "@pt\" . "
                + "?subject rdf:type ?local . "
                + "?property rdfs:label  \"tem_dispositivo@pt\" . "
                + "?object rdfs:label \"" + disp + "@pt\" ."
                + "      }";

        Query query = QueryFactory.create(queryString);

// Execute the query and obtain results
        QueryExecution qe = QueryExecutionFactory.create(query, model);
        res = qe.execAsk();

//while (results.hasNext()){
//System.out.println(results.next().get("object").asResource().listProperties().toList().toString());
//}
// Output query results 
// Important - free up resources used running the query
        qe.close();

        return res;

    }

    private static boolean ConsultarUP(String disp, String estado, Model model) throws FileNotFoundException, IOException {

        boolean res;


// Create a new query
        String queryString
                = "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
                + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
                + "ASK {?local ?property ?object . "
                + "?disp rdfs:label \"" + disp + "@pt\" . "
                + "?prop rdfs:label \"Estado@pt\" ."
                + " ?estado xsd:\"" + estado + "\""
                + "}";

        Query query = QueryFactory.create(queryString);

// Execute the query and obtain results
        QueryExecution qe = QueryExecutionFactory.create(query, model);
        res = qe.execAsk();

//while (results.hasNext()){
//System.out.println(results.next().get("object").asResource().listProperties().toList().toString());
//}
// Output query results 
// Important - free up resources used running the query
        qe.close();

        return res;

    }

}
