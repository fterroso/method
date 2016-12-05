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

package test.printers;

import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Formatter;
import models.fcsModel.FuzzyClusterSet;
import test.info.BasicTestInfo;
import test.results.BasicTestResults;

/**
 * Clase base con los metodos a ser implementados por los diferentes
 * Printers utilizados para volcar el contenido de las pruebas realizadas.
 * @author Fernando Terroso Saenz
 */
public abstract class BasicPrinter {

    /* Numero de decimales a mostrar en las matrices de desorden */
    private int decimals = 3;

    protected static final String FILE_NAME_PART_SEPARATOR = "-";
    
    public int getDecimals() {
        return decimals;
    }

    public void setDecimals(int pDecimals){
        decimals = pDecimals;
    }

    /**
     * Redondea pNumber a un numero con tantos decimales como indica pDecimals
     * @param pNumber Numero a redondear
     * @param pDecimals Decimales que debe de tener el numero final
     * @return
     */
    public double round( double pNumber, int pDecimals ) {
        return Math.round(pNumber*Math.pow(10,pDecimals))/Math.pow(10,pDecimals);
    }

     /**
     * Metodo que determina la inferencia dado un ejemplo de entrada x
      * @param info Clase que alberga la informacion de configuracion de la prueba
      * @param pModelsPredef Modelos que componen las pruebas (TSK, Anfis...)
      * @param pIdealMatrixs Array con las matrices ideales de cada circuito
      * @param pRMSEError Error RMSE clasificado por modelo
      * @param pDisorderMatrix Matrices de desorden clasificadas por modelo
      * @return Devuelve T o F en funcion de si todo ha ido bien
     */
    public abstract boolean printResults(
            BasicTestInfo info,
            BasicTestResults results );

    /**
     * Metodo que devuelve la extension del fichero en el que dejar los
     * resultados
     * @return .txt, .xml, .pdf... 
     */
    protected abstract String getTestFileExtension();


    /**
     * Metodo que devuelve la ruta donde debe de crearse el fichero con la
     * salida del test
     * @param pInfo Informacion de configuracion del test 
     * @return
     */
    protected abstract String getTestFilePath(BasicTestInfo pInfo);

    /**
     * Metodo que añade a un fichero las reglas que resultan de combinar
     * los centroides de los dos FCSs que se pasan como parámetro.
     * @param info Informacion del test
     * @param pFVel FCS con los centroides de velocidad
     * @param pFAc FCS con los centroides de aceleracion
     * @param pCircuit Numero de circuito al que pertenecen ambos FCSs.
     */
    public void printRules(BasicTestInfo info, FuzzyClusterSet pFVel, FuzzyClusterSet pFAc, String pCircuit){

        try{
            String path = getTestFilePath(info) + File.separator + getTestFileName(info) + "_rules" + getTestFileExtension();
            Formatter f = new Formatter(new FileWriter(path, true));

            double[][] cVel = pFVel.getCentroids();
            double[][] cAc = pFAc.getCentroids();

            f.format("\n%s, clusters velocidad: %d, clusters aceleracion: %d\n\n", pCircuit, cVel.length, cAc.length);
            f.format("%11s\t%12s\t%12s\n", "Velocidad", "Aceleracion", "Consecuente");

            for(int i= 0; i< cVel.length; i++){
                for(int j = 0; j < cAc.length; j++){
                    for(int k = 0; k< cVel[0].length-1; k++){
                        f.format("%11.3f\t%12.3f\n", cVel[i][k], cAc[j][k]);
                        double media = (cVel[i][cVel[0].length-1] + cAc[j][cVel[0].length-1])/2;
                        f.format("%11.3f\t%12.3f\t%12.3f\n\n", cVel[i][cVel[0].length-1], cAc[j][cVel[0].length-1], media);
                    }
                }
            }
            f.close();

        }catch(Exception e){
            System.out.println("Exception: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }


    /**
     * Metodo que define el nombre del fichero en donde volcar los resultados
     * en funcion del tipo de test que se ha ejecutado
     * @param info
     * @return
     */
    protected String getTestFileName(BasicTestInfo info){

        String type = info.getTestType();
        StringBuilder sb = new StringBuilder(type);

        if(type.equals("test.Test5Rules") ){
            sb.append(FILE_NAME_PART_SEPARATOR);
            int[] in = info.getUtilizarEntrada();
            sb.append(Arrays.toString(in).replace(" ", ""));
            sb.append(FILE_NAME_PART_SEPARATOR);
            sb.append(info.getAnfisType());
            sb.append(FILE_NAME_PART_SEPARATOR);
            sb.append(info.getAnfisParams().getConsequentType());
        }
        else if(type.equals("test.Test12Rules")){
            sb.append(FILE_NAME_PART_SEPARATOR);
            int[] in = info.getUtilizarEntrada();
            sb.append(Arrays.toString(in).replace(" ", ""));
            sb.append(FILE_NAME_PART_SEPARATOR);
            sb.append(info.getAnfisParams().getEpochs());
            sb.append(FILE_NAME_PART_SEPARATOR);
            sb.append(info.getFiringType());
        }
        else if(type.equals("test.TestFromXML")){
            sb.append(FILE_NAME_PART_SEPARATOR);
            String xml = info.getDirectoryXML();
            String[] parts = xml.split(File.separator);
            sb.append(parts[parts.length-1]);
            sb.append(FILE_NAME_PART_SEPARATOR);
            sb.append(info.getAnfisParams().getEpochs());
            sb.append(FILE_NAME_PART_SEPARATOR);
            sb.append(info.getAnfisType());
            sb.append(FILE_NAME_PART_SEPARATOR);
            sb.append(info.getAnfisParams().getConsequentType());
        }
        else if(type.equals("test.TestFindBestAnfis")){
            sb.append(FILE_NAME_PART_SEPARATOR);
            String xml = info.getDirectoryXML();
            String[] parts = xml.split(File.separator);
            sb.append(parts[parts.length-1]);
            sb.append(FILE_NAME_PART_SEPARATOR);
            sb.append(info.getAnfisParams().getEpochs());
        }

        Calendar calendar = Calendar.getInstance();
        
        sb.append(FILE_NAME_PART_SEPARATOR);
        sb.append(calendar.get(Calendar.HOUR_OF_DAY));
        sb.append(":");
        sb.append(calendar.get(Calendar.MINUTE));
        sb.append(":");
        sb.append(calendar.get(Calendar.SECOND));
        
        return sb.toString();

    }

    /**
     * Clase encargada de reducir el tamaño de una cadena realizando una serie
     * de operaciones sobre ella.
     * @param str Cadena a reducir
     * @return
     */
    protected String reduceString(String str){
        String out = "";

        out = str.replaceAll("ircuito", "");
        out = out.replace("DESCRIPTIVE", "DES");
        out = out.replace("numclust", "nc");
        return out;
    }

}
