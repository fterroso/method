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

package test.results;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import test.ExecutionEntityType;
import utility.DisorderMatrix;
import utility.IdealMatrix;

/** Clase que contiene los resultados de las pruebas realizadas
 *
 * @author Fernando Terroso Saenz
 */
public class BasicTestResults {
    
    ExecutionEntityType[] modelsPredef;
    Set<String> evalCircuits;
    Map<String, IdealMatrix> idealMatrixs;
    Map<ExecutionEntityType,HashMap<String, Double>> RMSEVal;
    Map<ExecutionEntityType,HashMap<String, Double>> RMSELearn;
    Map<ExecutionEntityType, HashMap<String, DisorderMatrix>> disorderMatrix;

    public BasicTestResults(
            ExecutionEntityType[] pModelsPredef,
            Set<String> pEvalCircuits,
            Map<String, IdealMatrix> pIdealMatrixs,
            Map<ExecutionEntityType,HashMap<String, Double>> pRMSEErrorLearn,
            Map<ExecutionEntityType,HashMap<String, Double>> pRMSEErrorVal,
            Map<ExecutionEntityType, HashMap<String, DisorderMatrix>> pDisorderMatrix){

        modelsPredef = pModelsPredef;
        evalCircuits = pEvalCircuits;
        idealMatrixs = pIdealMatrixs;
        RMSELearn = pRMSEErrorLearn;
        RMSEVal = pRMSEErrorVal;
        disorderMatrix = pDisorderMatrix;
    }

    public Map<ExecutionEntityType,HashMap<String, Double>> getRMSEVal() {
        return RMSEVal;
    }

    public Map<ExecutionEntityType, HashMap<String, Double>> getRMSELearn() {
        return RMSELearn;
    }

    public Map<ExecutionEntityType, HashMap<String, DisorderMatrix>> getDisorderMatrix() {
        return disorderMatrix;
    }

    public Set<String> getEvalCircuits() {
        return evalCircuits;
    }

    public Map<String, IdealMatrix> getIdealMatrixs() {
        return idealMatrixs;
    }

    public ExecutionEntityType[] getModelsPredef() {
        return modelsPredef;
    }

    

}
