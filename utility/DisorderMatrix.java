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
 * Clase encargada de almacenar la matriz de desorden de un determinado modelo
 * utilizando los datos del fichero contenido en idealMatrix
 * @author Fernando Terroso Saenz
 */
public class DisorderMatrix {

    IdealMatrix idealMatrix;
    int [][] disorderMatrix;
    int numErrors = 0;

    public DisorderMatrix(IdealMatrix pIdealMatrix, int[][] pMatrix){
        idealMatrix = pIdealMatrix;
        disorderMatrix = pMatrix;

        for(int i = 0; i< disorderMatrix.length; i++){
            for(int j = 0; j < disorderMatrix[0].length; j++){
                if(i != j){
                    numErrors += disorderMatrix[i][j];
                }
            }
        }
    }

    public int[][] getDisorderMatrix() {
        return disorderMatrix;
    }

    public IdealMatrix getIdealMatrix() {
        return idealMatrix;
    }

    public int getNumErrors() {
        return numErrors;
    }
    
}
