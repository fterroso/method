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
package test.enhanced;

import algorithms.BestCentroids.BestCentroids;
import algorithms.BestCentroids.BestCentroidsParams;
import algorithms.anfis.Anfis;
import datas.DirFilter;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import models.FuzzyModel;
import models.TCoNormType;
import models.fcsModel.FuzzyClusterSet;
import models.fcsModel.FuzzyClusterSetIO;
import models.tskModel.TSKModel;
import test.data.BasicTestDataSet;
import test.data.BasicTestDataSetCollection;
import test.data.LeaveOneOutCollection;
import test.data.TestDataSet;
import test.data.TestDataSetCollection;
import test.info.AlgorithmInfo;
import test.info.EnhancedTestInfo;
import test.info.VarSet;
import test.info.algorithm.fromdata.LearningRatio;
import test.info.algorithm.fromdata.ValidationType;
import test.printers.EnhancedPrinter;
import utility.Error;
import utility.PermutationGenerator;


/**
 * New generation of fuzzy test classes
 *
 * @author Fernando Terroso-Saenz
 */
public abstract class EnhancedTest{

    /* Hash con el error RMSE de validacion de los diferentes modelos y en funcion de los circuitos empleados */
    protected Map<String,HashMap<String, Double>> RMSETest = new HashMap<String,HashMap<String, Double>>();
    /* Hash con el error RMSE de aprendizaje de los diferentes modelos y en funcion de los circuitos empleados */
    protected Map<String,HashMap<String, Double>> RMSETrain  = new HashMap<String,HashMap<String, Double>>();
    
    public abstract void test(EnhancedTestInfo pInfo, EnhancedPrinter pPrinter);
    
    protected double calculateRMSEForFuzzyModel(
            FuzzyModel model,
            double[][] in,
            double[] out) throws Exception{

        double[] salidasDis=new double[in.length];

        Error e=new Error(out);

        
        //Calculamos el error 
        for (int z=0;z<in.length;z++) {
            
            salidasDis[z]= model.makeInference(in[z]);
            
        }

        return e.RMSE(salidasDis);
    }
    
    protected double calculateRMSEForAnfis(
            Anfis anfis,
            double[][] in,
            double[] out) throws Exception{

        double[] salidasDis=new double[in.length];

        Error e=new Error(out);
        
        //Calculamos el error 
        for (int z=0;z<in.length;z++) {
            
            salidasDis[z]= anfis.evaluate(in[z]);
            
        }

        return e.RMSE(salidasDis);
    }
    
    protected void storeTrainRMSE(String modelID, String dataSetID, double RMSE){
        
        // Almacenamos el error RMSE de aprendizaje...
        HashMap<String, Double> aux = RMSETrain.get(modelID);

        if(aux == null ){
            aux = new HashMap<String, Double>();
        }
        aux.put(dataSetID, RMSE);
        RMSETrain.put(modelID, aux);        
        
    }
    
    protected void storeEvalRMSE(String modelID, String dataSetID, double RMSE){
        
        // Almacenamos el error RMSE de evaluacion...
        HashMap<String, Double> aux = RMSETest.get(modelID);

        if(aux == null ){
            aux = new HashMap<String, Double>();
        }
        aux.put(dataSetID, RMSE);
        RMSETest.put(modelID, aux);
        
    }
    
    private List<File> getFilesFromDirectory(String dirPath){
         File fileData = new File(dirPath);

         DirFilter filter = new DirFilter("\\w+\\.txt");
         List<File> files = Arrays.asList(fileData.listFiles(filter));
         
         return files;
    }
    
    protected TestDataSetCollection getDataSetLeaveOneOut(EnhancedTestInfo info){
         TestDataSetCollection result = new LeaveOneOutCollection();
         
         List<File> files = getFilesFromDirectory(info.getDirectoryData());
         
         for(File f : files){
             BasicTestDataSet dataSet = new BasicTestDataSet();
             dataSet.setTrainSet(Arrays.asList(f));
             dataSet.setEvalRowPosition(0);
             dataSet.setValidationType(ValidationType.LEAVE_ONE_OUT);
             
             result.add(dataSet);
         }
         
         return result;
    }
    
    protected TestDataSetCollection getDataSetCrossValidation(EnhancedTestInfo info){
        
        TestDataSetCollection dataSetList = new BasicTestDataSetCollection();

        try{
        
            LearningRatio lr = info.getAlgInfo().getCrossValidationInfo().getLearningRatio();

            int trainRate = lr.getTrain();
            int testRate = lr.getTest();

            List<File> files = getFilesFromDirectory(info.getDirectoryData());

            PermutationGenerator pgTrain = new PermutationGenerator(files.size(), trainRate);

            int[] trainIndexes = pgTrain.next();

            do{

                List<File> trainFiles = new LinkedList<File>();
                List<File> evalFiles = new LinkedList<File>();
                
                evalFiles.addAll(files); 
                for(int i : trainIndexes){
                    trainFiles.add(files.get(i));
                    evalFiles.remove(files.get(i));
                }

                if(testRate < evalFiles.size()){

                    PermutationGenerator pgTest = new PermutationGenerator(evalFiles.size(), testRate);
                    int[] testIndexes = pgTest.next();

                    do{
                        BasicTestDataSet dataSet = new BasicTestDataSet();
                        dataSet.setTrainSet(trainFiles);

                        List<File> test = new LinkedList<File>();

                        for(int i : testIndexes){
                            test.add(evalFiles.get(i));
                        }

                        dataSet.setEvalSet(test);  
                        dataSet.setValidationType(ValidationType.CROSS_VALIDATION);

                        dataSetList.add(dataSet);

                    }while((testIndexes = pgTest.next()) != null);
                }else if(testRate == evalFiles.size()){
                    BasicTestDataSet dataSet = new BasicTestDataSet();
                    dataSet.setTrainSet(trainFiles);
                    dataSet.setEvalSet(evalFiles);
                    dataSetList.add(dataSet);
                }else{
                    throw new Exception("No supported condition");
                }



            }while((trainIndexes = pgTrain.next()) != null);

        }catch(Exception e){
            e.printStackTrace();      
        }

        return dataSetList;                
    }
    
    protected FuzzyModel[] executeBestCentroids(
            VarSet varSet, 
            TestDataSet dataSet, 
            AlgorithmInfo algInfo) throws Exception{

        BestCentroidsParams bcInfo = algInfo.getBestCentroidInfo();        
        TSKModel[] tsks = new TSKModel[1];
        
        double [][] inLearn = dataSet.getInputTrainData();
        double [] outLearn = dataSet.getOutputTrainData();
        
        BestCentroids bc= new BestCentroids(bcInfo);

        StringBuilder modelName = new StringBuilder();
        modelName.append(dataSet.getTrainDataSetName());
        modelName.append("_input_");
        String inputCol = Arrays.toString(varSet.getInput()).replace("[", "");
        inputCol = inputCol.replace("]", "");
        inputCol = inputCol.replace(" ", "");
        inputCol = inputCol.replace(",", "_");
        modelName.append(inputCol);

        FuzzyClusterSet fcs = bc.execute(inLearn, outLearn, bcInfo.getSpace(), algInfo.getDistanceType(), null, modelName.toString());

        switch(algInfo.getMembershipType()){
            case GAUSSIAN:
                tsks[0] = fcs.createGaussianTSKFromFCS(algInfo.getConsequentType(), algInfo.getFiringType());
                break;
            case BELL:
            case TRAPEZOIDAL:
            case TRIANGULAR:
                throw new Exception("Imposible convertir FCS a " + algInfo.getMembershipType() + " TSK");
        }

        return tsks;
    }
    
    protected FuzzyModel[] executePredefinedCentroids(
            VarSet varSet,
            TestDataSet dataSet, 
            AlgorithmInfo algInfo){
        
        double[][] predefinedCentroids = new double[120][4];
        int i = 0;        
        
        for(double dayInterval = 0; dayInterval < 6; dayInterval++){            
            for(double dayType= 1; dayType< 5; dayType++){
                predefinedCentroids[i][0] = dayInterval;
                predefinedCentroids[i][1] = dayType;
                predefinedCentroids[i][2] = 0;
                predefinedCentroids[i][3] = 0;
                
                predefinedCentroids[++i][0] = dayInterval;
                predefinedCentroids[i][1] = dayType;
                predefinedCentroids[i][2] = 1;
                predefinedCentroids[i][3] = 8;

                
                predefinedCentroids[++i][0] = dayInterval;
                predefinedCentroids[i][1] = dayType;
                predefinedCentroids[i][2] = 4;
                predefinedCentroids[i][3] = 18;

                
                predefinedCentroids[++i][0] = dayInterval;
                predefinedCentroids[i][1] = dayType;
                predefinedCentroids[i][2] = 8;
                predefinedCentroids[i][3] = 25;
                
                i++;
            }
        }
        
        StringBuilder modelName = new StringBuilder();
        modelName.append(dataSet.getTrainDataSetName());
        modelName.append("_input_");
        String inputCol = Arrays.toString(varSet.getInput()).replace("[", "");
        inputCol = inputCol.replace("]", "");
        inputCol = inputCol.replace(" ", "");
        inputCol = inputCol.replace(",", "_");
        modelName.append(inputCol);
        
        FuzzyClusterSet fcsInitial=new FuzzyClusterSetIO(
                modelName.toString(),
                dataSet.getInputTrainData(),
                dataSet.getOutputTrainData(),
                2.0, 
                algInfo.getDistanceType(), 
                predefinedCentroids,
                null, 
                TCoNormType.MAXIMUM);

        return new FuzzyModel[] {fcsInitial};
    
   }
    
    protected FuzzyModel[] executeAnfis(            
            TestDataSet dataSet, 
            AlgorithmInfo algInfo,
            FuzzyModel[] inModels) throws Exception{
        
        FuzzyModel[] outModels = new FuzzyModel[inModels.length];
        
        int i = 0;
        for(FuzzyModel model : inModels){
            
            TSKModel tsk = null;
            if(model instanceof TSKModel){
                tsk = (TSKModel) model;
            }else if(model instanceof FuzzyClusterSet){
                FuzzyClusterSet fcs = (FuzzyClusterSet) model;
                tsk = fcs.createGaussianTSKFromFCS(algInfo.getConsequentType(), algInfo.getFiringType());
            }
            
            if(tsk == null){
                throw new Exception("The model has not been initialized!");
            }
            
            /***** Training *****/
            
            //Read training data set
            double [][] inTrain = dataSet.getInputTrainData();   
            double [] outTrain = dataSet.getOutputTrainData();   
                        
            Anfis anfis = new Anfis(tsk, inTrain, outTrain, algInfo.getAnfisInfo());
                        
            double RMSETrainVal = Double.MAX_VALUE;
            
            try{
                RMSETrainVal = anfis.make_learning();
            }catch(Exception ex){
                System.out.println("Excepcion en el aprendizaje de anfis. Ponemos -1 en el error "+ex);
                RMSETrainVal = -1;
            }
            
            storeTrainRMSE(tsk.getTotalModelIdentifier(), dataSet.getTrainDataSetName(), RMSETrainVal);

            
            /***** Evaluation *****/            
            double [][] inEval = dataSet.getInputEvalData();
            double [] outEval = dataSet.getOutputEvalData();
            
            double RMSEEvalVal = calculateRMSEForAnfis(anfis, inEval, outEval);
                        
            storeEvalRMSE(tsk.getTotalModelIdentifier(), dataSet.getEvalDataSetName(), RMSEEvalVal);
            
            System.out.println("RMSE [train:"+RMSETrainVal+"; eval:"+RMSEEvalVal+"]");
            outModels[i++] = anfis.createTSKModel();
            
        }
        
        return outModels;
    
    }
    
    protected void generateXML(FuzzyModel[] models, EnhancedTestInfo info){
        
        String xmlpath = generateXMLPath(info);
        
        System.out.print("Generating XMLs of the models...");

        for(FuzzyModel model : models){
            try{
                model.setSerializationPath(xmlpath);
                model.writeDown();
            }catch(Exception e){
                System.out.println("Error al serializar modelo " + model.getTotalModelIdentifier());
                e.printStackTrace();
            }
        } 
        System.out.println("OK!");

    }
    
    protected void generatePlot(FuzzyModel[] models, EnhancedTestInfo info){
        
        String plotPath = generatePlotPath(info) + File.separator;
        
        System.out.print("Generating plots of the models...");
        
        for(FuzzyModel model : models){
            
            if(model instanceof TSKModel){
                TSKModel tsk = (TSKModel) model;
                tsk.setPlotPath(plotPath);
                try{
                    tsk.writeDownGNUPlot();
                }catch(Exception e){
                    System.out.println("Error al serializar modelo " + model.getTotalModelIdentifier());
                    e.printStackTrace();
                }
            }           
        }       
        System.out.println("OK!");
    }
    
    protected String generateXMLPath(EnhancedTestInfo info){
        
        String path = "";
        if(info.getOutputDir().endsWith(File.separator)){
            path = info.getOutputDir() + "xml";          
        }else{
            path = info.getOutputDir() +File.separator+"xml";
        }        
        
        File f = new File(path);
        f.mkdir();
        
        return path;
    }
    
    protected String generatePlotPath(EnhancedTestInfo info){
        
        String path = "";
        if(info.getOutputDir().endsWith(File.separator)){
            path = info.getOutputDir() + "plot";          
        }else{
            path = info.getOutputDir() +File.separator+"plot";
        }
                
        File f = new File(path);
        f.mkdir();
        
        return path;
    }    
    
}
