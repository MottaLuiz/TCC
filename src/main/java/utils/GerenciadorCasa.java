/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;

/**
 *
 * @author Luiz
 */
public class GerenciadorCasa {

    public static void consultar() {
    
        String banda = "iron maden";
        String prefixos = "PREFIX foaf: <http://xmls.com/foaf/0.1.>"+
                "PREFIX dbpedia-owl: <http://dbpedia.org/ontology/>"+
                "PREFIX dbpprop: <http://dbpedia.org/property/>";
        
        String queryBanda = prefixos + "SELECT ?desc ? membersLink ?website " +
                "WHERE { " +
                "?x a dbpedia-owl:Band . " +
                "?x dbpprop:name . " +
                "?x dbpedia-owl:abstract ?desc . " +
                "?x foaf:homepage ?website . " +
                "?x dbpprop:currentMembers ?membersLink . " +
                
                "FILTER (lcase(dtr(?name)) = \"" + banda.toLowerCase() + "\")" +
                "FILTER (langMatches(lang(?desc), \"PT\"))"+
                "}";
        try (QueryExecution queryExecution = QueryExecutionFactory.sparqlService("http:dbpedia.org/sparql", queryBanda)) {
            ResultSet results = queryExecution.execSelect();
            ResultSetFormatter.out(System.out, results);
        }
        
    }
    
}
