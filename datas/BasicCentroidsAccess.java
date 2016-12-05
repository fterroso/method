/*
 * Copyright 2014 University of Murcia (Fernando Terroso-Saenz (fterroso@um.es), Mercedes Valdes-Vela, Antonio F. Skarmeta)
 * 
 * This file is part of METHOD.
 * 
 * METHOD is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * METHOD is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Foobar.  If not, see http://www.gnu.org/licenses/.
 * 
 */

package datas;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Acceso a los datos de los centroides de un fichero de texto plano del sistema de ficheros.
 * @author Fernando Terroso Saenz
 */
public class BasicCentroidsAccess extends CentroidsAccess{

    /* Centroides indexados por circuitos (String) y reglas (RuleElement) */
    HashMap<String,HashMap<RuleElement,double[]>> centroids;

    public void parse(String path){
        try{

            File file = new File(path);
            Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            StreamTokenizer fileTokens = new StreamTokenizer(reader);
            fileTokens.resetSyntax();
            fileTokens.eolIsSignificant(true);
            fileTokens.wordChars(' ','~');
            fileTokens.commentChar('#');
            fileTokens.nextToken();

            centroids = new HashMap<String,HashMap<RuleElement,double[]>>();

            // Elementos en el mismo orden en el que estan en el fichero de centroides

             while (fileTokens.ttype != StreamTokenizer.TT_EOF) {
                 /* fileTokens.sval contiene una linea del fichero */
                 if(fileTokens.sval != null){
                    StringTokenizer stringTokens = new StringTokenizer(fileTokens.sval, " ");

                    String nombreCircuito = stringTokens.nextToken();
                    System.out.println(nombreCircuito);

                    HashMap<RuleElement,double[]> centroidesCircuito = new HashMap<RuleElement,double[]>();

                    // Accedemos a cada uno de los elementos de la linea leida
                    for(RuleElement elemento : getRules() ){

                        String aux = stringTokens.nextToken();
                        StringTokenizer auxToken = new StringTokenizer(aux, "|");
                        double media = Double.parseDouble(auxToken.nextToken());
                        double varianza = Double.parseDouble(auxToken.nextToken());
                        System.out.println("\t" + elemento +" (" + media + ", " + varianza + ")");

                        double [] auxArray = {media, varianza};
                        centroidesCircuito.put(elemento, auxArray);
                    }

                    centroids.put(nombreCircuito, centroidesCircuito);

                 }
                 fileTokens.nextToken();
             }
        }catch( Exception e){
            System.out.println("Error al leer del fichero de centroides: " + e.getMessage());
            e.printStackTrace();
        }

    }


        /**
     * Metodo que permite acceder a los datos de los centroides.
     * @param circuitName Nombre del circuito al cual se quiere acceder
     * @param element Tipo de dato a acceder (Ac+, Ac-, V+, etc)
     */
    public double[] getData(String circuitName, RuleElement element){
        HashMap<RuleElement,double[]> centroidesCircuito = centroids.get(circuitName);
        return centroidesCircuito.get(element);
    }

}
