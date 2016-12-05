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
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import test.info.EnhancedTestInfo;
import test.results.EnhancedTestResults;

/**
 *
 * @author calcifer
 */
public class EnhancedTextPrinter implements EnhancedPrinter{

    private static final int LENGTH_TAB= 8;
    
    public void printRMSEResults(EnhancedTestInfo info, EnhancedTestResults results) {
        
        try{

            String fileName = getTestResultFileNameHead(info) + "_RMSE.txt"; 
            
            PrintWriter outRMSE = new PrintWriter(getTestResultFilePath(info)+File.separator+fileName);
            outRMSE.println("********************** Configuration parameters ***************************\n");
           
            outRMSE.println(info.getConfigFileContent());
                                
            outRMSE.println("\n********************** RMSE Test Results ***************************\n");

            for(String model : results.getEvaluatedModels()){

                Map<String, Double> errorsTest = results.getRMSETest().get(model);

                Set<String> keys = errorsTest.keySet();
                LinkedList<String> keysList = new LinkedList<String>(keys);
                Collections.sort(keysList);

                for(String key: keysList){

                    double errorEval = errorsTest.get(key);

                    outRMSE.print(model+"\t");
                    if(model.length() >= LENGTH_TAB){
                        outRMSE.print("\t");
                    }
                    
                    outRMSE.print(key+"\t");
                    if(key.length() >= LENGTH_TAB){
                        outRMSE.print("\t");
                    }
                    
                    outRMSE.println(errorEval);

                }
                outRMSE.println("\n*************************************************************************\n");

            }

            // Escribimos el encabezado del fichero
            outRMSE.println("\n********************** RMSE Train Results ***************************\n");

            // Y ahora las matrices de desorden
            for(String model : results.getEvaluatedModels()){

                Map<String, Double> errorsEval = results.getRMSETrain().get(model);

                Set<String> keys = errorsEval.keySet();
                LinkedList<String> keysList = new LinkedList<String>(keys);
                Collections.sort(keysList);

                for(String key: keysList){

                    double errorEval = errorsEval.get(key);

                    outRMSE.print(model+"\t");
                    if(model.length() >= LENGTH_TAB){
                        outRMSE.print("\t");
                    }
                    
                    outRMSE.print(key+"\t");
                    if(key.length() >= LENGTH_TAB){
                        outRMSE.print("\t");
                    }
                    
                    outRMSE.println(errorEval);

                }
                outRMSE.println("*************************************************************************\n");

            }

            outRMSE.println("***************************** FILE END ******************************\n");
            outRMSE.close();                
                         
        }catch( Exception ex ){
                ex.printStackTrace();
        }
    }
    
    
    protected String getTestResultFilePath(EnhancedTestInfo info){

        String testType = info.getTestType();

        String path = info.getOutputDir() + File.separator + "results"+File.separator+testType + File.separator;

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

    protected String getTestResultFileNameHead(EnhancedTestInfo info){
        
        Calendar calendar = Calendar.getInstance();
        
        StringBuilder fileName = new StringBuilder();
        
        fileName.append(calendar.get(Calendar.HOUR_OF_DAY));
        fileName.append(":");
        fileName.append(calendar.get(Calendar.MINUTE));
        fileName.append(":");
        fileName.append(calendar.get(Calendar.SECOND));
        
        return fileName.toString();
    }

}
