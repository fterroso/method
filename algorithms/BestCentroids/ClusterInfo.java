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

package algorithms.BestCentroids;

import algorithms.PartitionedSpace;

/**
 * Clase que guarda la información de un cluster a ser generado por Best
 * Centroids. Esta clase es sobre todo utilizada en el paquete test
 *
 * @author Fernado Terroso Saenz
 */
public class ClusterInfo {

    private int[] inputColumns;
    private int outputColumn;
    private int numClusters;
    private PartitionedSpace space;

    public ClusterInfo(){}
    
    public ClusterInfo(
            int[] pInputColumns,
            int pOutputColumn,
            int pNumClusters,
            PartitionedSpace pSpace){

        inputColumns = pInputColumns;
        outputColumn = pOutputColumn;
        numClusters = pNumClusters;
        space = pSpace;

    }

    public int[] getInputColumns() {
        return inputColumns;
    }

    public void setInputColumns(int[] inputColums) {
        this.inputColumns = inputColums;
    }

    public int getNumClusters() {
        return numClusters;
    }

    public void setNumClusters(int numClusters) {
        this.numClusters = numClusters;
    }

    public int getOutputColumn() {
        return outputColumn;
    }

    public void setOutputColumn(int outputColumn) {
        this.outputColumn = outputColumn;
    }

    public PartitionedSpace getSpace() {
        return space;
    }

    public void setSpace(PartitionedSpace space) {
        this.space = space;
    }



    @Override
    public String toString(){

        StringBuilder sb = new StringBuilder();
        sb.append("Cluster:\n\tEspacio:");
        sb.append(space);
        sb.append("\n\tInput: [");
        for(int i : inputColumns){
            sb.append(i);
            sb.append(", ");
        }
        sb.replace(sb.length()-2, sb.length(), "");
        sb.append("]\n\tOutput: ");
        sb.append(outputColumn);
        sb.append("\n\tNumero: ");
        sb.append(numClusters);
        sb.append("\n");

        return sb.toString();
    }

}
