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

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Fernando Terroso-Saenz <fterroso@um.es>
 */
public class EnhancedTestResults {
    
    Map<String,HashMap<String, Double>> RMSETest;
    Map<String,HashMap<String, Double>> RMSETrain;

    public Map<String, HashMap<String, Double>> getRMSETest() {
        return RMSETest;
    }

    public void setRMSETest(Map<String, HashMap<String, Double>> RMSETest) {
        this.RMSETest = RMSETest;
    }

    public Map<String, HashMap<String, Double>> getRMSETrain() {
        return RMSETrain;
    }

    public void setRMSETrain(Map<String, HashMap<String, Double>> RMSETrain) {
        this.RMSETrain = RMSETrain;
    }
    
    public List<String> getEvaluatedModels(){
        
        Set<String> keys = RMSETrain.keySet();
        List<String> keysList = new LinkedList<String>(keys);
        Collections.sort(keysList);

        return keysList;
    }
   
}
