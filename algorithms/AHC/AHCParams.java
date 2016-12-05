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

package algorithms.AHC;

import java.io.File;

/**
 * Clase con los principales parámetros de configuración del algoritmo AHC
 *
 * @author Fernando Terroso Saenz
 */
public class AHCParams {

    String XMLPath;
    String executionName;
    String tempFileName = "FCS_AHC_";

    public String getXMLPath() {
        return XMLPath;
    }

    public void setXMLPath(String pXMLPath) {
        this.XMLPath = pXMLPath;
        if(!XMLPath.endsWith(File.separator)){
            XMLPath.concat(File.separator);
        }
    }

    public String getExecutionName() {
        return executionName;
    }

    public void setExecutionName(String executionName) {
        this.executionName = executionName;
    }

    public String getTempFileName() {
        return tempFileName;
    }


    @Override
    public String toString() {
        return "AHCParams{" + "XMLPath=" + XMLPath + "executionName=" + executionName + '}';
    }

    


}
