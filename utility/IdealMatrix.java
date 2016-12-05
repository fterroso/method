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

package utility;

/**
 * Clase que contiene el numero de ejemplos de cada tipo en un fichero de
 * entrada dado.
 * @author Feranando Terroso Saenz
 */
public class IdealMatrix {

    private int[] matrix;
    private int numInstances = 0;
    private String fileName;

    public IdealMatrix( String pFileName, double[] pOutputs, int numClasses){

        matrix = new int[numClasses];
        for(int i = 0; i < pOutputs.length; i++){

            int out = (int) pOutputs[i];
            matrix[out-1]++;
        }
        numInstances = pOutputs.length;
        fileName = pFileName;
    }

    public int[] getMatrix() {
        return matrix;
    }

    public int getNumInstances() {
        return numInstances;
    }

    public String getFileName() {
        return fileName;
    }

    

}
