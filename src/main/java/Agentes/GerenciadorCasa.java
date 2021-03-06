//FUNCIONANDO LOCAL

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agentes;

import static Agentes.GerenciadorCasa.model;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
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

    static Model model = ModelFactory.createMemModelMaker().createDefaultModel();

    public static String EstadoDispositivo(String dispositivo, String local) throws FileNotFoundException, IOException {
        String resultado = null;

        String queryString
                = "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
                + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
                + "SELECT ?estado \n"
                + " WHERE {"
                + " ?disp ?prop ?estado  . "
                + " ?disp rdfs:label ?labeldisp . "
                + " ?disp rdfs:label \"" + dispositivo + "_" + local + "@pt\" ."
                + " ?prop rdfs:label \"Estado@pt\" . "
                + " } ";

        Query query = QueryFactory.create(queryString);
// Execute the query and obtain results
        QueryExecution qe = QueryExecutionFactory.create(query, model);
        ResultSet res = qe.execSelect();

        while (res.hasNext()) {
            resultado = (res.next().get("estado")).toString();
        }
// Create a new query
        return resultado;
    }

    public static Vector<String> obterComm(String dispositivo, String local) {
        Vector<String> conn = new Vector<>();

// Open the bloggers RDF graph from the filesystem
        String queryString
                = "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
                + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
                + "SELECT ?labeldisp ?prop ?endereco ?protocolo \n"
                + " WHERE {"
                + " ?disp ?prop ?endereco  ."
                + " ?disp ?props ?protocolo ."
                + " ?disp ?esta ?local . "
                + " ?disp rdfs:label \"" + dispositivo + "@pt\" ."
                + " ?local rdfs:label \"" + local + "@pt\" . "
                + " ?prop rdfs:label \"EnderecoComunicao@pt\" . "
                + " ?prop rdfs:label \"ProtocoloComunicao@pt\" ."
                + " } ";

        Query query = QueryFactory.create(queryString);
// Execute the query and obtain results
        QueryExecution qe = QueryExecutionFactory.create(query, model);
        ResultSet res = qe.execSelect();
        ResultSetFormatter.out(System.out, res, query);
        QuerySolution qs = res.next();
        conn.add(qs.getResource("endereco").toString());
        conn.add(qs.getResource("protocolo").toString());
        return conn;

    }

    public static boolean consultar() throws FileNotFoundException, IOException {

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
        QueryExecution qe = QueryExecutionFactory.create(query, model);
        ResultSet res = qe.execSelect();
        ResultSetFormatter.out(System.out, res, query);


        return true;

    }

    public static boolean consultarDispositivo(String local, String disp) {

        boolean res;
// Create a new query
        String queryString
                = "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
                + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
                + "ASK {?object rdfs:label \"" + disp + "_" + local + "@pt\" }";

        Query query = QueryFactory.create(queryString);

// Execute the query and obtain results
        QueryExecution qe = QueryExecutionFactory.create(query, model);
        res = qe.execAsk();

        qe.close();

        return res;

    }

    public static Vector<String> consultarLocaisdeDisp(String disp) throws FileNotFoundException, IOException {

        Vector<String> locaisaux = new Vector<>();
        Vector<String> locais = new Vector<>();
        // Open the bloggers RDF graph from the filesystem
        String queryString
                = "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
                + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
                + "SELECT ?labellocal  ?disp ?prop ?local \n"
                + " WHERE {"
                + " ?local ?prop ?disp  . "
                //+ " ?local rdfs:label \"" + local + "@pt\"  . "
                + " ?prop rdfs:label \"tem_dispositivo@pt\" . "
                + " ?local rdfs:label ?labellocal . "
                + " ?disp rdf:type ?class . "
                + " ?class rdfs:label \"" + disp + "_dispositivo@pt\" . "
                + " } ";
        Query query = QueryFactory.create(queryString);
// Execute the query and obtain results
        QueryExecution qe = QueryExecutionFactory.create(query, model);
        ResultSet res = qe.execSelect();
        //ResultSetFormatter.out(System.out, res, query);
        while (res.hasNext()) {
            locaisaux.add(res.next().get("labellocal").toString());

        }
        for (int i = 0; i < locaisaux.size(); i++) {
            locais.add(locaisaux.elementAt(i).substring(0, locaisaux.elementAt(i).length() - 3));

        }
// Create a new query

        return locais;

    }

    public static Vector<String> consultarDispsNoLocal(String local) throws FileNotFoundException, IOException {

        Vector<String> dispsaux = new Vector<>();
        Vector<String> disps = new Vector<>();
        String aux = new String();
        aux = "_" + local + "@pt";
        // Open the bloggers RDF graph from the filesystem
        String queryString
                = "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
                + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
                + "SELECT ?labeldisp  ?disp ?prop ?local \n"
                + " WHERE {"
                + " ?local ?prop ?disp  . "
                + " ?local rdfs:label \"" + local + "@pt\"  . "
                + " ?prop rdfs:label \"tem_dispositivo@pt\" . "
                + " ?disp rdfs:label ?labeldisp . "
                // " ?disp rdf:type ?class . "
                //" ?class rdfs:label \"" + disp + "_dispositivo@pt\" . "
                + " } ";
        Query query = QueryFactory.create(queryString);
// Execute the query and obtain results
        QueryExecution qe = QueryExecutionFactory.create(query, model);
        ResultSet res = qe.execSelect();
        //ResultSetFormatter.out(System.out, res, query);
        while (res.hasNext()) {
            dispsaux.add(res.next().get("labeldisp").toString());

        }
        for (int i = 0; i < dispsaux.size(); i++) {
            disps.add(dispsaux.elementAt(i).substring(0, dispsaux.elementAt(i).length() - aux.length()));

        }
// Create a new query

        return disps;

    }

    public static boolean consultarLocalporDisp(String local) throws FileNotFoundException, IOException {

        // Open the bloggers RDF graph from the filesystem
        String queryString
                = "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
                + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
                + "SELECT ?labeldisp ?local ?prop ?disp \n"
                + " WHERE {"
                + " ?local ?prop ?disp  . "
                + " ?local rdfs:label \"" + local + "@pt\"  . "
                + " ?prop rdfs:label \"tem_dispositivo@pt\" . "
                + " ?disp rdfs:label ?labeldisp . "
                + " } ";
        Query query = QueryFactory.create(queryString);
// Execute the query and obtain results
        QueryExecution qe = QueryExecutionFactory.create(query, model);
        ResultSet res = qe.execSelect();
        ResultSetFormatter.out(System.out, res, query);


        return true;
    }

    public static boolean consultarTodosDispsitivos() throws FileNotFoundException, IOException {

        // Open the bloggers RDF graph from the filesystem
        String queryString
                = "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
                + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
                + "SELECT ?labeldisp ?local ?prop ?disp \n"
                + " WHERE {"
                + " ?local ?prop ?disp  . "
                //+ " ?local rdfs:label \""+local+"@pt\"  . "
                + " ?prop rdfs:label \"tem_dispositivo@pt\" . "
                + " ?disp  rdf:type ?class . "
                + " ?class rdfs:label \"Dispositivos@pt\" . "
                + " ?disp rdfs:label ?labeldisp . "
                + " } ";
        Query query = QueryFactory.create(queryString);
// Execute the query and obtain results
        QueryExecution qe = QueryExecutionFactory.create(query, model);
        ResultSet res = qe.execSelect();
        ResultSetFormatter.out(System.out, res, query);

// Create a new query

        return true;

    }

    public static boolean consultarTodosLocais() throws FileNotFoundException, IOException {

        // Open the bloggers RDF graph from the filesystem
        String queryString
                = "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
                + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
                + "SELECT ?labellocal ?local \n"
                + " WHERE {"
                + " ?local  rdf:type ?class . "
                + " ?class rdfs:label \"Locais@pt\" . "
                + " ?local rdfs:label ?labellocal . "
                + " } ";

        Query query = QueryFactory.create(queryString);
// Execute the query and obtain results
        QueryExecution qe = QueryExecutionFactory.create(query, model);
        ResultSet res = qe.execSelect();
        ResultSetFormatter.out(System.out, res, query);

// Create a new query

        return true;

    }

    public static boolean verificaValorPropDisp(String disp, String local, String propvalue) {
//refazer
        boolean res;

// Create a new query
        String queryString
                = "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
                + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
                + "ASK {?disp ?prop \"" + propvalue + "\" . "
                + " ?disp rdfs:label \"" + disp + "_" + local + "@pt\" . "
                /*+ "?property rdfs:label  \"tem_dispositivo@pt\" . "*/
                //+ "?prop rdfs:label \"Estado@pt\" . "
                + "}";

        Query query = QueryFactory.create(queryString);

// Execute the query and obtain results
        QueryExecution qe = QueryExecutionFactory.create(query, model);

        res = qe.execAsk();


        qe.close();

        return res;

    }

    public static boolean AlterarProp(String disp, String local, String prop, String antigoestado, String estadonovo) {

        String queryString
                = "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
                + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
                + "DELETE { ?disp ?prop \"" + antigoestado + "\" }\n"
                + "INSERT { ?disp ?prop \"" + estadonovo + "\" }\n"
                + "WHERE {\n"
                + " ?prop rdfs:label \"" + prop + "@pt\" . "
                + " ?disp rdfs:label \"" + disp + "_" + local + "@pt\" . "
                + "  } ";
        UpdateRequest up = UpdateFactory.create(queryString);
        UpdateAction.execute(up, model);
        boolean resp = GerenciadorCasa.verificaValorPropDisp(disp, local, estadonovo);
        return resp;
    }

    static boolean consultarLocal(String local) {
        boolean res;

// Create a new query
        String queryString
                = "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
                + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
                + "ASK {?object rdfs:label \"" + local + "@pt\" }";

        Query query = QueryFactory.create(queryString);

// Execute the query and obtain results
        QueryExecution qe = QueryExecutionFactory.create(query, model);
        res = qe.execAsk();

        qe.close();

        return res;
    }

    public static String AlterarPropVolume(String disp, String local, String acao) {
        String resp = "";
        try {
            if ("ligado".equals(GerenciadorCasa.EstadoDispositivo(disp, local))) {
                int volumeant = GerenciadorCasa.ConsultarVolume(disp, local);
                int volumenovo = 0;
                if (acao == "aumentar") {
                    volumenovo = volumeant + 1;
                    if (volumenovo > 10) {
                        return disp+" em "+ local+" Volume maximo";
                    }
                }
                if (acao == "diminuir") {
                    volumenovo = volumeant - 1;
                    if (volumenovo < 0) {
                        return disp+" em "+ local+" Volume minimo";
                    }

                }
                String volnovo = String.valueOf(volumenovo);
                String volant = String.valueOf(volumeant);

                String queryString
                        = "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
                        + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                        + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
                        + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
                        + "DELETE { ?disp ?prop \"" + volant + "\"^^xsd:integer }\n"
                        + "INSERT { ?disp ?prop \"" + volnovo + "\"^^xsd:integer }\n"
                        + "WHERE {\n"
                        + " ?prop rdfs:label \"Volume@pt\" . "
                        + " ?disp rdfs:label \"" + disp + "_" + local + "@pt\" . "
                        //  + " ?volantigo rdf:datatype xsd \""+volant+"\" . "
                        //  + " ?volnovo rdf:datatype \""+volnovo+"\" . "
                        + "  } ";
                UpdateRequest up = UpdateFactory.create(queryString);
                UpdateAction.execute(up, model);
                resp = "consegui "+acao+" volume para "+disp+" em "+local;
            }else {
                resp= "dispositivo desligado nao foi possivel "+acao+" o volume do "+disp+" em "+local;
            }
        } catch (IOException ex) {
            Logger.getLogger(GerenciadorCasa.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return resp;
    }

    public static int ConsultarVolume(String dispositivo, String local) throws FileNotFoundException, IOException {
        int resultado = 10;

        String queryString
                = "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
                + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
                + "SELECT ?volume \n"
                + " WHERE {"
                + " ?disp ?prop ?volume  . "
                + " ?disp rdfs:label ?labeldisp . "
                + " ?disp rdfs:label \"" + dispositivo + "_" + local + "@pt\" ."
                + " ?prop rdfs:label \"Volume@pt\" . "
                + " } ";

        Query query = QueryFactory.create(queryString);
// Execute the query and obtain results
        QueryExecution qe = QueryExecutionFactory.create(query, model);
        ResultSet res = qe.execSelect();

        while (res.hasNext()) {
            resultado = (res.next().getLiteral("volume").getInt());
        }
// Create a new query
        return resultado;
    }

    public void init() throws FileNotFoundException, IOException {
        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        path = path.substring(0, path.length() - 2);
        String resourcesPath = path + "\\src\\main\\resources\\OntologiaCasa.owl";
        InputStream in = new FileInputStream(new File(resourcesPath));
        model.read(in, null); // null base URI, since model URIs are absolute
        in.close();
    }

    public void close() throws FileNotFoundException, IOException {
        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        path = path.substring(0, path.length() - 2);
        String resourcesPath = path + "\\src\\main\\resources\\OntologiaCasa.owl";
        StringWriter sw = new StringWriter();
        model.write(sw);
        String owlCode = sw.toString();
        File file = new File(resourcesPath);
        try {
            FileWriter fw = new FileWriter(file);
            fw.write(owlCode);
            fw.close();
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

}
