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

package datas.preprocess;

/**
 * Clase que contiene los parametros de preprocesamiento de los datos
 *
 * @author Fernando Terroso Saenz
 */
public class DataPreProcessorParams {

    /* Directorio de donde leer los datos a preprocesar */
    private String inputDir;
    /* Directorio donde dejar los datos ya preprocesados */
    private String outputDir;
    /* Tipo de maniobra a tratar */
    private String manauverToHandle;
    /* Numero de instancias de manauverToHandle que se pretende alcanzar */
    private int numberToReach;

    public DataPreProcessorParams(){}

    public String getInputDir() {
        return inputDir;
    }

    public void setInputDir(String inputDir) {
        this.inputDir = inputDir;
    }

    public String getManauverToHandle() {
        return manauverToHandle;
    }

    public void setManauverToHandle(String manauverToHandle) {
        this.manauverToHandle = manauverToHandle;
    }

    public int getNumberToReach() {
        return numberToReach;
    }

    public void setNumberToReach(int numberToReach) {
        this.numberToReach = numberToReach;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }

}
