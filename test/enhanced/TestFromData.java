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

import java.util.List;
import models.FuzzyModel;
import test.ExecutionEntityType;
import test.data.TestDataSet;
import test.data.TestDataSetCollection;
import test.info.AlgorithmInfo;
import test.info.ModelInfo;
import test.info.EnhancedTestInfo;
import test.info.VarSet;
import test.info.algorithm.fromdata.ValidationType;
import test.printers.EnhancedPrinter;
import test.results.EnhancedTestResults;

/**
 *
 * @author Fernando Terroso-Saenz<fterroso@um.es>
 */
public class TestFromData extends EnhancedTest{

    @Override
    @SuppressWarnings("CallToThreadDumpStack")
    public void test(EnhancedTestInfo pInfo, EnhancedPrinter pPrinter) {
        try{
            
            AlgorithmInfo algInfo = pInfo.getAlgInfo();
            ModelInfo modelInfo = pInfo.getModelInfo();
                        
            List<String> algSeq = algInfo.getAlgothmSeq();
            ValidationType typeDataSet =  algInfo.getValidationType();// algDataSource.getType();

            TestDataSetCollection dataSets = null;
            switch(typeDataSet){
                case CROSS_VALIDATION:            
                    dataSets = getDataSetCrossValidation(pInfo);
                    break; 
                case LEAVE_ONE_OUT:
                    dataSets = getDataSetLeaveOneOut(pInfo);
            }

            if(dataSets != null){
                FuzzyModel[] models = null;
                for(VarSet varSet: pInfo.getVarSets()){

                    while(dataSets.hasNext()){
                        
                        TestDataSet dataSet = dataSets.next();
                          
                        dataSet.generateData(varSet);
                        
                        for(String alg : algSeq){

                           ExecutionEntityType exEntity = ExecutionEntityType.valueOf(alg.toUpperCase());

                           System.out.println("Executing "+exEntity+" ["+varSet+"]...");
                           
                           switch(exEntity){
                               case ANFIS:
                                   models = executeAnfis(dataSet, algInfo, models);
                                   break;
                               case BEST_CENTROIDS:
                                   models = executeBestCentroids(varSet, dataSet, algInfo);
                                   break;
                               case PREDEFINED_CENTROIDS:
                                   models = executePredefinedCentroids(varSet, dataSet, algInfo);
                                   break;
                           }
                        }
                    }
                    
                    if(modelInfo.mustGenerateXML()){
                        generateXML(models, pInfo);
                    }

                    if(modelInfo.mustGeneratePlot()){
                        generatePlot(models, pInfo);
                    }                     
                    
                }
                
                EnhancedTestResults results = new EnhancedTestResults();
                results.setRMSETest(RMSETest);
                results.setRMSETrain(RMSETrain);
                        
                pPrinter.printRMSEResults(pInfo, results);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
}
