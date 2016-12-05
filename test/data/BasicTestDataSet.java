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
package test.data;

import datas.FileAccess;
import datas.FileType;
import java.io.File;
import java.util.List;
import test.info.VarSet;
import test.info.algorithm.fromdata.ValidationType;

/**
 *
 * @author Fernando Terroso-Saenz
 */
public class BasicTestDataSet implements TestDataSet{

    List<File> trainSet;
    List<File> evalSet;
    
    String trainSetName = null;
    String evalSetName = null;
    
    double[][] inputTrainData;
    double[] outputTrainData;
    
    double[][] inputEvalData;
    double[] outputEvalData;
    
    int evalRowPosition = -1; //for leave-one-out validation
    
    ValidationType validationType;

    public void setEvalRowPosition(int evalRowPosition) {
        this.evalRowPosition = evalRowPosition;
    }

    public int getEvalRowPosition() {
        return evalRowPosition;
    }
    
    public List<File> getTrainSet() {
        return trainSet;
    }

    public List<File> getEvalSet() {
        return evalSet;
    }

    public void setEvalSet(List<File> testSet) {
        this.evalSet = testSet;
    }

    public void setTrainSet(List<File> trainSet) {
        this.trainSet = trainSet;
    }   

    public double[][] getInputTrainData() {
        return inputTrainData;
    }

    public double[] getOutputTrainData() {
        return outputTrainData;
    }

    private void setInputTrainData(double[][] inTrainData) {
        inputTrainData= inTrainData;
    }

    private void setOutputTrainData(double[] outTrainData) {
        outputTrainData = outTrainData;
    }

    public double[][] getInputEvalData() {
       return inputEvalData;
    }

    public double[] getOutputEvalData() {
        return outputEvalData;
    }

    private void setInputEvalData(double[][] inTrainData) {
       inputEvalData = inTrainData;
    }

    private void setOutputEvalData(double[] outTrainData) {
        outputEvalData = outTrainData;
    }
    
    public String getTrainDataSetName(){
        if(trainSetName == null){
            StringBuilder trainFilesName = new StringBuilder();
            for(File f : getTrainSet()){

                trainFilesName.append(f.getName().replace(".txt", ""));
                trainFilesName.append("_");
            }
            trainFilesName.replace(trainFilesName.length()-1, trainFilesName.length(), "");
            trainSetName = trainFilesName.toString();
        }
        return trainSetName;
    }
    
    public String getEvalDataSetName(){
        
        if(evalSetName == null){
            switch(getValidationType()){
                case CROSS_VALIDATION:
                    StringBuilder trainFilesName = new StringBuilder();
                    for(File f : getEvalSet()){

                        trainFilesName.append(f.getName().replace(".txt", ""));
                        trainFilesName.append("_");
                    }
                    trainFilesName.replace(trainFilesName.length()-1, trainFilesName.length(), "");
                    evalSetName = trainFilesName.toString();
                    break;
                case LEAVE_ONE_OUT:
                    evalSetName = "eval";
                    break;
            }            
        }
        
        return evalSetName;        
    }
    
    public ValidationType getValidationType(){
        return validationType;
    }

    public void setValidationType(ValidationType validationType) {
        this.validationType = validationType;
    }


    public void generateData(VarSet varSet) throws Exception{
        if(evalRowPosition < 0){
            
            FileAccess fileAccess = new FileAccess();             
            fileAccess.parse(
                    getTrainSet().toArray(new File[]{}), 
                    varSet.getInput(),
                    varSet.getOutput(), 
                    FileType.TRAIN);
            
            setInputTrainData(fileAccess.getInputDataLearn());
            setOutputTrainData(fileAccess.getOutputDataLearn());

            fileAccess = new FileAccess();
            fileAccess.parse(
                    getEvalSet().toArray(new File[]{}), 
                    varSet.getInput(), 
                    varSet.getOutput(), 
                    FileType.EVAL);

            setInputEvalData(fileAccess.getInputDataEvaluate());
            setOutputEvalData(fileAccess.getOutputDataEvaluate());
            
        }else{ // Here we're using leave-one-out validation
            
            FileAccess fileAccess = new FileAccess();             
            fileAccess.parse(
                    getTrainSet().toArray(new File[]{}), 
                    varSet.getInput(), 
                    varSet.getOutput(), 
                    FileType.TRAIN);
                        
            double[][] trainIn = fileAccess.getInputDataLearn();
            double[] trainOut = fileAccess.getOutputDataLearn();
            
            double[][] finalTrainIn = new double[trainIn.length-1][trainIn[0].length];
            double[] finalTrainOut = new double[trainOut.length-1];

            double[][] finalEvalIn = new double[1][trainIn[1].length];
            double[] finalEvalOut = new double[]{trainOut[evalRowPosition]};
            
            boolean evalRowPosPassed = false;
            for(int i= 0; i< trainIn.length; i++){
                if(i!= evalRowPosition){
                    int aux = (evalRowPosPassed)? i-1: i;
                    System.arraycopy(trainIn[i], 0 , finalTrainIn[aux], 0, trainIn[i].length);
                    finalTrainOut[aux] = trainOut[i];
                }else{
                    evalRowPosPassed = true;
                    System.arraycopy(trainIn[i], 0, finalEvalIn[0], 0, trainIn[i].length);
                }
            }
            
            setInputTrainData(finalTrainIn);
            setOutputTrainData(finalTrainOut);
            
            setInputEvalData(finalEvalIn);
            setOutputEvalData(finalEvalOut);

        }
    }
       

}
