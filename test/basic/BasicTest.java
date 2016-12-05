

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

package test.basic;

import algorithms.anfis.Anfis;
import java.io.File;
import java.util.Arrays;
import java.util.Calendar;
import java.util.EnumMap;
import java.util.Formatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import models.ConsequentType;
import models.FuzzyModel;
import test.ExecutionEntityType;
import test.info.BasicTestInfo;
import test.printers.BasicPrinter;
import utility.DisorderMatrix;
import utility.Error;
import utility.IdealMatrix;

/** Clase abstracta con los elementos comunes a todas las clases de BasicTest
 * realizadas
 *
 * @author Fernando Terroso Saenz
 */
public abstract class BasicTest {

    protected static final String[] CIRCUITS = {"circuito1", "circuito2", "circuito3", "circuito4"};

    protected BasicTestInfo info;
    protected BasicPrinter printer;

    /*--------------------TSK--------------------------*/
    /*Consequent type. Por defecto, singleton*/
    protected ConsequentType consequentType= ConsequentType.SINGLETON;
    /*FIN TSK*/

    /* Hash con el error RMSE de validacion de los diferentes modelos y en funcion de los circuitos empleados */
    protected Map<ExecutionEntityType,HashMap<String, Double>> RMSEVal;
    /* Hash con el error RMSE de aprendizaje de los diferentes modelos y en funcion de los circuitos empleados */
    protected Map<ExecutionEntityType,HashMap<String, Double>> RMSELearn;
    /* Hash con las matrices de desorden de los modeles en funcion de los circuitos empleados */
    protected Map<ExecutionEntityType, HashMap<String, DisorderMatrix>> disorderMatrix;
    /* Hash con las matrices ideales de cada fichero de entrada */
    protected Map<String, IdealMatrix> idealMatrixs;
    /* Conjunto con los nombres de los circuitos usados para evaluar */
    protected Set<String> usedCircuits;


    /****** Methods ******/
    public abstract void executeTest(BasicTestInfo pInfo, BasicPrinter pPrinter) throws Exception;

    /**
     * Metodo que inicializa las variables principales de todos los test
     * @param pInfo
     * @param pPrinter
     */
    protected void initTest(BasicTestInfo pInfo, BasicPrinter pPrinter){
        
        info = pInfo;
        printer = pPrinter;

        usedCircuits = new HashSet<String>();
        RMSEVal = new EnumMap<ExecutionEntityType,HashMap<String, Double>>(ExecutionEntityType.class);
        RMSELearn = new EnumMap<ExecutionEntityType,HashMap<String, Double>>(ExecutionEntityType.class);

        disorderMatrix = new EnumMap<ExecutionEntityType, HashMap<String, DisorderMatrix>>(ExecutionEntityType.class);
        idealMatrixs = new HashMap<String, IdealMatrix>();

    }

    /**
     * Metodo que obtiene el error RMSE y la matriz de desorden de los modelos a usar en la bateria de pruebas
     */
    protected double calculateRMSEAndMatrix(
            FuzzyModel model,
            Anfis anfis,
            double[][] in,
            double[] out,
            String fEvalName,
            String modelConf,
            ExecutionEntityType modelName,
            String... extras) throws Exception{

        int numErrors = 0;
        int numClasses = info.getNumClasses();
        int [][] matrizDesorden = new int[numClasses][numClasses];
        
        for(int i = 0; i< numClasses; i++){
            Arrays.fill(matrizDesorden[i], 0);
        }

        double[] salidasDis=new double[in.length];
        double[] salidasDisRed=new double[in.length];

        Error e=new Error(out);

        //Calculamos el error y la matriz de desorden
        for (int z=0;z<in.length;z++) {
            if(model != null){
                salidasDis[z]= model.makeInference(in[z]);
            }
            else{
                salidasDis[z] = anfis.evaluate(in[z]);
            }

            //REDONDEO
            salidasDisRed[z]=Math.round(salidasDis[z]);

            if (salidasDisRed[z]<1)
                salidasDisRed[z]=1;
            if (salidasDisRed[z]>info.getNumClasses())
                salidasDisRed[z]=info.getNumClasses();

            int i=(int)salidasDisRed[z];
            int r=(int)out[z];

            if(i >= 4){
                i = 2;
            }
            if(r >= 4){
                r = 2;
            }

            matrizDesorden[r-1][i-1]++;

            if(r!=i){
                numErrors++;
            }
        }

        double error = e.RMSE(salidasDis);

        //Almacenamos el error RMSE
        storeRMSEVal(modelName, modelConf, error);

        // Almacenamos la matriz de desorden
       HashMap<String, DisorderMatrix> auxM = disorderMatrix.get(modelName);

        if(auxM == null ){
            auxM = new HashMap<String, DisorderMatrix>();
        }

        IdealMatrix ideal;
        if(extras.length > 0){
            ideal = idealMatrixs.get(extras[0]);
        }
        else{
            ideal = idealMatrixs.get(fEvalName);
        }

        DisorderMatrix dMatrix = new DisorderMatrix(ideal, matrizDesorden);
        auxM.put(modelConf, dMatrix);
        disorderMatrix.put(modelName, auxM);

        double percentage = (double) numErrors /(double) in.length;
        percentage *= 100;
        
        return 100-percentage;

    }

    protected void storeRMSEVal(ExecutionEntityType model, String modelConf, double error){

        // Almacenamos el error RMSE de validacion...
        HashMap<String, Double> aux = RMSEVal.get(model);

        if(aux == null ){
            aux = new HashMap<String, Double>();
        }
        aux.put(modelConf, error);
        RMSEVal.put(model, aux);

    }

    protected void storeRMSELearn(ExecutionEntityType model, String modelConf, double error){

        // Almacenamos el error RMSE de aprendizaje...
        HashMap<String, Double> aux = RMSELearn.get(model);

        if(aux == null ){
            aux = new HashMap<String, Double>();
        }
        aux.put(modelConf, error);
        RMSELearn.put(model, aux);

    }

    protected void printOutput(double[] real, double[] inferred){
       Formatter f = new Formatter(System.out);

       f.format("\nReal\tInferido\n");

       for(int i = 0; i< real.length; i++){
           f.format("%d\t%d\n", (int) real[i], (int) inferred[i]);
       }
    }

    protected String getGNUPlotPath(){

        String testType = info.getTestType();

        String path = info.getOutputDir() + testType + File.separator + "GNUPlot";

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

        return path + File.separator;
    }

    protected String getXMLPath(){

        String testType = info.getTestType();

        String path = info.getOutputDir() + testType + File.separator + "XML";

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

        return path + File.separator;
    }
}
