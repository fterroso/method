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
import java.io.FileReader;
import java.io.LineNumberReader;

/**
 *
 * @author Fernando Terroso-Saenz <fterroso@um.es>
 */
public class LeaveOneOutCollection extends BasicTestDataSetCollection{
    
    int maxLineNumber;
    
    protected TestDataSet getCurrent(){
        return list.get(index);
    }
    
    @Override
    public TestDataSet next() {
        
        BasicTestDataSet dataset = (BasicTestDataSet) getCurrent();
        int evalPos = dataset.getEvalRowPosition();
        dataset.setEvalRowPosition(evalPos++);
        
        if(dataset.getEvalRowPosition() > maxLineNumber){
            
            dataset = (BasicTestDataSet)list.get(index++);
            File f = dataset.getTrainSet().get(0);
            try{
                LineNumberReader  lnr = new LineNumberReader(new FileReader(f));
                lnr.skip(Long.MAX_VALUE);
                maxLineNumber = lnr.getLineNumber();
            }catch(Exception e){
                e.printStackTrace();
            }
            
        }
        
        return dataset;
          
    }
    
}
