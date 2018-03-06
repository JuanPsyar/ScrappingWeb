package com.stackexcerise.scraping;

import java.io.IOException;
import java.time.Clock;
import javax.swing.table.DefaultTableModel;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class StackBuildersExercise {

    private static final String EMPTY_STRING = "";

    public DefaultTableModel Scrapear(String url, DefaultTableModel tabla) {
        // Compruebo si me da un 200 al hacer la petición
        if (getStatusConnectionCode(url) == 200) {

            // Obtengo el HTML de la web en un objeto Document
            Document document = getHtmlDocument(url);

            // Busco todas las historias de meneame que estan dentro de: 
            //Elements entradas = document.select("div.col-md-4.col-xs-12").not("div.col-md-offset-2.col-md-4.col-xs-12");
            Elements entradas = document.select(".itemlist tr").not("tr.spacer, tr.monospacer");
            System.out.println("Entradas: " + entradas.size() + "\n");
            String[] auxiliar = null;
            String titulo = EMPTY_STRING;
            String rango = EMPTY_STRING;
            String puntos = EMPTY_STRING;
            String comentario = EMPTY_STRING;
            String cadenaDatos = EMPTY_STRING;
            Integer numComentarios= 0;
            Integer puntaje = 0;
            Boolean guardar = false;

            // Paseo cada una de las entradas
            for (Element elem : entradas) {
                titulo = elem.select("a.storylink").text().trim();
                rango = elem.select("span.rank").text().trim();
                puntos = elem.select("span.score").text().trim();
                comentario = elem.select(".subtext a[href^=\"item?id=\"]").not(".subtext span.age").text().trim();
                
                if(puntos.contains("point")){
                    puntos = puntos.substring(0, puntos.indexOf("point")-1).trim();
                }
                if (comentario.contains("comment")) {
                    auxiliar = comentario.split(" ");
                    comentario = auxiliar[auxiliar.length - 1];
                    comentario = comentario.substring(0, comentario.indexOf("comment")-1);
                    
                } else {
                    comentario = EMPTY_STRING;
                }
                if (cadenaDatos.equals(EMPTY_STRING)) {
                    if (!titulo.equals(EMPTY_STRING) && !rango.equals(EMPTY_STRING)) {
                        cadenaDatos = rango + "-%_" + titulo;
                    }
                } else {
                    if (!puntos.equals(EMPTY_STRING)) {
                        if (comentario.equals(EMPTY_STRING)) {
                            comentario = "0";
                        }
                        cadenaDatos = cadenaDatos + "-%_" + puntos + "-%_" + comentario;
                        guardar = true;
                    }
                }
                if (guardar) {
                    auxiliar = cadenaDatos.split("-%_");
                    puntaje = Integer.valueOf(auxiliar[2]);
                    numComentarios = Integer.valueOf(auxiliar[3]);
                    tabla.addRow((new Object[]{auxiliar[0],auxiliar[1],puntaje,numComentarios}));
                    System.out.println(cadenaDatos);
                    cadenaDatos = EMPTY_STRING;
                    guardar=false;
                    // Con el método "text()" obtengo el contenido que hay dentro de las etiquetas HTML
                    // Con el método "toString()" obtengo todo el HTML con etiquetas incluidas
                }
            }

        } else {
            System.out.println("El Status Code no es OK es: " + getStatusConnectionCode(url));
        }
        return tabla;
    }

    /**
     * Con esta método compruebo el Status code de la respuesta que recibo al
     * hacer la petición EJM: 200 OK	300 Multiple Choices 301 Moved Permanently
     * 305 Use Proxy 400 Bad Request	403 Forbidden 404 Not Found	500 Internal
     * Server Error 502 Bad Gateway	503 Service Unavailable
     *
     * @param url
     * @return Status Code
     */
    public static int getStatusConnectionCode(String url) {

        Response response = null;

        try {
            response = Jsoup.connect(url).userAgent("Chrome/64.0.3282.186").timeout(100000).ignoreHttpErrors(true).execute();
        } catch (IOException ex) {
            System.out.println("Excepción al obtener el Status Code: " + ex.getMessage());
        }
        return response.statusCode();
    }

    /**
     * Con este método devuelvo un objeto de la clase Document con el contenido
     * del HTML de la web que me permitirá parsearlo con los métodos de la
     * librelia JSoup
     *
     * @param url
     * @return Documento con el HTML
     */
    public static Document getHtmlDocument(String url) {

        Document doc = null;

        try {
            doc = Jsoup.connect(url).userAgent("Chrome/64.0.3282.186").timeout(100000).get();
        } catch (IOException ex) {
            System.out.println("Excepción al obtener el HTML de la página" + ex.getMessage());
        }

        return doc;

    }

    public static void main(String args[]) {

        try {

        } catch (Exception e) {
        }

    }
}
