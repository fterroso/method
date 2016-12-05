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

import java.io.File;
import java.util.List;
import test.info.VarSet;
import test.info.algorithm.fromdata.ValidationType;

/**
 *
 * @author Fernando Terroso-Saenz <fterroso@um.es>
 */
public interface TestDataSet {
    
    public List<File> getTrainSet();
    public List<File> getEvalSet();  
    
    public String getTrainDataSetName();
    public String getEvalDataSetName();
    
    public double[][] getInputTrainData();
    public double[] getOutputTrainData();
        
    public double[][] getInputEvalData();
    public double[] getOutputEvalData();  

    public void generateData(VarSet varSet) throws Exception;   
    
    public ValidationType getValidationType();
     
}
