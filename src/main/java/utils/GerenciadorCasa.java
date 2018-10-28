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
import org.apache.jena.update.UpdateAction;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;

/**
 *
 * @author Luiz
 */
public class GerenciadorCasa {
    
   Model model = ModelFactory.createMemModelMaker().createDefaultModel();

    public static boolean consultar(Model modelaux) throws FileNotFoundException, IOException {

        // Open the bloggers RDF graph from the filesystem
       
        String queryString
                = "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
                + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
                + "SELECT ?labeldisp ?prop ?estado \n"
                + " WHERE {"
                + " ?disp ?prop ?estado  . "
                + " ?disp rdfs:label ?labeldisp . "
                + " ?disp rdfs:label \"televisao_sala@pt\" . "
                // + " ?prop rdfs:label \"Estado@pt\" . "
                + " } ";
        Query query = QueryFactory.create(queryString);
// Execute the query and obtain results
        QueryExecution qe = QueryExecutionFactory.create(query, modelaux);
        ResultSet res = qe.execSelect();
        ResultSetFormatter.out(System.out, res, query);
        while (res.hasNext()){
            System.out.println(res.next().get("estado"));
        }
// Create a new query
        
        return true;

    }

    public Model getModel() {
        return model;
    }

    public static boolean consultarDispositivo(String local, String disp, Model modelaux) {

        boolean res;

// Create a new query
        String queryString
                = "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
                + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
                + "ASK {?local ?property ?object . "
                + "?local rdfs:label \"" + local + "@pt\" . "
                /*+ "?property rdfs:label  \"tem_dispositivo@pt\" . "*/
                + "?object rdfs:label \"" + disp + "@pt\" }";

        Query query = QueryFactory.create(queryString);

// Execute the query and obtain results
        QueryExecution qe = QueryExecutionFactory.create(query, modelaux);
        res = qe.execAsk();
        System.out.println("\n " + res + "\n");

//while (results.hasNext()){
//System.out.println(results.next().get("object").asResource().listProperties().toList().toString());
//}
// Output query results 
// Important - free up resources used running the query
        qe.close();

        return res;

    }
    public static boolean consultarLocal(String local, Model modelaux) {

        boolean res;

// Create a new query
        String queryString
                = "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
                + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
                + "ASK {?local . "
                + "?local rdfs:label \"" + local + "@pt\" . "
                /*+ "?property rdfs:label  \"tem_dispositivo@pt\" . "*/
                + " }";

        Query query = QueryFactory.create(queryString);

// Execute the query and obtain results
        QueryExecution qe = QueryExecutionFactory.create(query, modelaux);
        res = qe.execAsk();
        System.out.println("\n " + res + "\n");

//while (results.hasNext()){
//System.out.println(results.next().get("object").asResource().listProperties().toList().toString());
//}
// Output query results 
// Important - free up resources used running the query
        qe.close();

        return res;

    }

   public static boolean verificaValorPropDisp(String disp, String propvalue, Model modelaux) {

        boolean res;

// Create a new query
        String queryString
                = "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
                + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
                + "ASK {?disp ?prop \"" + propvalue + "\" . "
                + "?disp rdfs:label \"" + disp + "@pt\" . "
                /*+ "?property rdfs:label  \"tem_dispositivo@pt\" . "*/
                //+ "?prop rdfs:label \"Estado@pt\" . "
                + "}";

        Query query = QueryFactory.create(queryString);

// Execute the query and obtain results
        QueryExecution qe = QueryExecutionFactory.create(query, modelaux);
        res = qe.execAsk();

//while (results.hasNext()){
//System.out.println(results.next().get("object").asResource().listProperties().toList().toString());
//}
// Output query results 
// Important - free up resources used running the query
        qe.close();

        return res;

    }

    private static boolean AlterarProp(String disp,String prop, String antigoestado, String estadonovo, Model model) {

        String queryString
                = "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
                + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
                + "DELETE { ?disp ?prop \"" + antigoestado + "\" }\n"
                + "INSERT { ?disp ?prop \"" + estadonovo + "\" }\n"
                + "WHERE {\n"
                + " ?prop rdfs:label \""+prop+"@pt\" . "
                + " ?disp rdfs:label \"" + disp + "@pt\" . "
                + "  } ";

        UpdateRequest up = UpdateFactory.create(queryString);

// Execute the query and obtain results
        UpdateAction.execute(up, model);
//while (results.hasNext()){
//System.out.println(results.next().get("object").asResource().listProperties().toList().toString());
//}
// Output query results 
// Important - free up resources used running the query
        return verificaValorPropDisp(disp, estadonovo, model);
    }

    public void init() throws FileNotFoundException, IOException {
         File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        path = path.substring(0, path.length() - 2);
        //System.out.println(path);
        String resourcesPath = path + "\\src\\main\\resources\\OntologiaCasa.owl";
        InputStream in = new FileInputStream(new File(resourcesPath));
//InputStream in = new FileInputStream(new File("D:\\faculdade\\TCC\\TCC - versão final\\TCC\\src\\main\\resources\\OntologiaCasa.owl"));

// Create an empty in-memory model and populate it from the graph
        model.read(in, null); // null base URI, since model URIs are absolute
        in.close();
    }

}
