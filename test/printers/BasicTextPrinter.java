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
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import test.info.BasicTestInfo;
import test.ExecutionEntityType;
import test.results.BasicTestResults;
import utility.DisorderMatrix;

/**
 * Clase encargada de volcar el contenido de las pruebas sobre un fichero
 * de texto plano (.txt) en formato libre.
 * @author Fernando Terroso Saenz
 */
public class BasicTextPrinter extends BasicPrinter{

    private static final int LENGTH_TAB= 8;

    public boolean printResults(
            BasicTestInfo info,
            BasicTestResults results ) {
        
        boolean output = true;

        try{

            String testFilePath = getTestFilePath(info);
            String testFileName = getTestFileName(info);
            String testFileExt = getTestFileExtension();

            PrintWriter out = new PrintWriter(testFilePath + File.separator + testFileName + testFileExt);
            PrintWriter out2 = new PrintWriter(testFilePath + File.separator + testFileName + "_columns" + testFileExt);
            PrintWriter outRMSE = new PrintWriter(testFilePath + File.separator + testFileName + "_RMSE_eval" + testFileExt);

            // Escribimos el encabezado del fichero
            out.println("***** RESULTADOS DE LAS PRUEBAS REALIZADAS *****\n");

            out.println("MATRICES DE DESORDEN\n");
            out.println("Filas: valor real; Columnas: valor inferido\n");

            // Y ahora las matrices de desorden
                for(ExecutionEntityType model : results.getModelsPredef()){
                    HashMap<String, DisorderMatrix>  matrixModel = results.getDisorderMatrix().get(model);

                    Map<String, Double> errorsEval = results.getRMSEVal().get(model);

                    Set<String> keys = matrixModel.keySet();
                    LinkedList<String> keysList = new LinkedList<String>(keys);
                    Collections.sort(keysList);

                    String last= "";
                    for(String key: keysList){
                        StringTokenizer sto = new StringTokenizer(key, "-");
                        String keyLearn = sto.nextToken();
                        sto.nextToken();
                        keyLearn += ("-" + sto.nextToken());
                        keyLearn += ("-" + sto.nextToken());

                        double errorEval = errorsEval.get(key);

                        sto = new StringTokenizer(key, "-");
                        String p = sto.nextToken();
                        p += ("-" + sto.nextToken());

                        if(!p.equals(last)){
                            last = p;
                            out2.println(model+ "_" + p);
                            outRMSE.println(model + "_" + p);
                        }

                        outRMSE.println(errorEval);

                        DisorderMatrix matrixCircuit = matrixModel.get(key);

                        out.println("Modelo: " + model + "_" + key + "\n");

                        int[][] matrix = matrixCircuit.getDisorderMatrix();

                        out.print("\t");
                        for(int i =0; i < matrix[0].length; i++){
                            out.print(i+1 + "\t\t");
                        }
                        out.println("\n");

                        for(int i = 0; i< matrix.length; i++){
                            int total = 0;
                            for(int j = 0; j< matrix[i].length; j++){
                                total += matrix[i][j];
                            }
                            out.print(i+1 + "\t");
                            for(int j = 0; j<matrix[i].length; j++){
                                StringBuffer aux = new StringBuffer();
                                double percent = (double) matrix[i][j]/ (double) total;
                                percent *= 100;
                                double percentRounded = round(percent, getDecimals());
                                aux.append(percentRounded);
                                aux.append(" (");
                                aux.append(matrix[i][j]);
                                aux.append(")");
                                out.print(aux + "\t");
                                if( aux.length() < 8){
                                    out.print("\t");
                                }
                            }
                            out.println("");
                        }
                        int numInstances = matrixCircuit.getIdealMatrix().getNumInstances();
                        int numErrors = matrixCircuit.getNumErrors();
                        double totalError = (double) numErrors / (double) numInstances;
                        totalError *= 100;
                        totalError = round(totalError, getDecimals());
                        StringBuilder buff = new StringBuilder("\nNumero de clasificaciones erroneas: ");
                        buff.append(numErrors);
                        buff.append(" (");
                        buff.append(totalError);
                        buff.append("%)");
                        out.print(buff.toString());
                        out.println("\n-------------------------------------------------------------------\n");

                        out2.println(totalError);
                    }
                    out.println("*************************************************************************\n");

                }

                out.println("*****************************FIN DE FICHERO******************************\n");
                out.close();
                out2.close();
                outRMSE.close();

            }catch( Exception ex ){
                ex.printStackTrace();
                output = false;

        }
        finally{
            return output;
        }
    }

    protected String getTestFilePath(BasicTestInfo info){

        String testType = info.getTestType();

        String path = info.getOutputDir() + File.separator + testType + File.separator + "errors";

        File outputDir = new File(path);

        if(!outputDir.exists()){
            outputDir.mkdirs();
        }

        Calendar calendar = Calendar.getInstance();

        StringBuilder cal = new StringBuilder();
        cal.append(calendar.get(Calendar.DAY_OF_MONTH));
        cal.append("_");
        cal.append(calendar.get(Calendar.MONTH)+1);
        cal.append("_");
        cal.append(calendar.get(Calendar.YEAR));


        String dateAndTime = cal.toString();

        path += File.separator + dateAndTime;
        File dateAndTimeDir = new File(path);

        dateAndTimeDir.mkdir();

        return path;
    }

    protected String getTestFileExtension(){
        return ".txt";
    }

    // Metodo que imprime una tabla hash en formato de tabla tabulada
    private String printAsTable(
            Set<String> columnNames,
            Map<String, HashMap<String, Object>> elements){

        String out = "";

        TreeMap<String, HashMap<String, Object>> sortedElements = new TreeMap<String, HashMap<String, Object>>(elements);

        Set<String> rows = sortedElements.keySet();

        out += "\t\t\t\t\t";

        // Ponemos los nombres de las columnas
        for(String columnName : columnNames){
            if(columnName.length() >= LENGTH_TAB){
                columnName = reduceString(columnName);
            }
            out += columnName + "\t";
            if(columnName.length() < LENGTH_TAB){
                out += "\t";
            }
        }
        out += "\n\n";

        // Ahora ponemos las filas y su contenido
        for(String row : rows){

            String rowAux = row;
            if(rowAux.length() > LENGTH_TAB * 4){
                rowAux = reduceString(rowAux);
            }

            int length = rowAux.length();
            while(length < LENGTH_TAB *5){
                rowAux += "\t";
                length += LENGTH_TAB;
            }
            out += rowAux;

            HashMap<String, Object> rowElements = sortedElements.get(row);

            for(String columnName : columnNames){
                if(rowElements.containsKey(columnName)){
                    Object value = rowElements.get(columnName);
                    String valueStr = String.valueOf(value);
                    out += valueStr + "\t";

                    if(valueStr.length() <= LENGTH_TAB){
                        out += "\t";
                    }
                }
                else{
                    out += "X\t\t";
                }
            }
            out +="\n";
        }

        return out;
    }
}
