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

package test.info;

import algorithms.BestCentroids.BestCentroidsParams;
import algorithms.BestCentroids.ClusterInfo;
import algorithms.anfis.AnfisParams;
import algorithms.anfis.AnfisType;
import java.io.File;
import java.util.Calendar;
import java.util.List;
import models.FiringType;
import models.MembershipFunctionType;
import models.TCoNormType;
import models.fcsModel.Constants.DistanceType;
import test.CentroidsInitType;

/**
 * Clase que encapsula la informacion de configuracion de una prueba
 * @author Fernando Terroso Saenz
 */
public class BasicTestInfo {

    private static final String TEST_BC = "test.TestBestCentroids";

    /* Tipo de test a realizar (5rules, 12rules, descriptive...)*/
    private String testType;
    /* Atributos de entrada a tener en cuenta */
    /* Importante el orden: primero vel, luego ac */
    private int[] utilizarEntrada; 
    /* Atributo de salida a tener en cuenta */
    private int[] utilizarSalida;
    /* Numero de clases en las que se puede clasificar un ejemplo (ST, CR, AC, DC) */
    private int numClasses;
    /* Numero de reglas (centroides) a utilizar/generar en caso de ser necesario */
    private int numRules;
    /* Directorio donde leer los XMLs */
    private String directoryXML;
    /* Nombre del directorio con los diferentes ficheros con los datos de aprendizaje y evaluacion*/
    private String directoryData;
    /* Fichero con los centroides introducidos a mano */
    private String fileCentroids;
    /* Fichero con las reglas introducidas a mano */
    private String fileRules;

    private Double fuzziness;

    private DistanceType distanceType;
    /*Tipo de funcion de pertenencia a usar */
    private MembershipFunctionType functionType;

    private FiringType firingType;

    /* Directorio donde dejar el fichero de salida */
    private String outputDir;
    /* Fecha de la realizacion de la prueba */
    private String date;
    /* Hora de la realizacion de la prueba */
    private String time;
    /* Numero de ficheros a utilizar como aprendizaje y evaluacion */
    private String learningRatio;
    /* Parametros de configuracion de Anfis */
    private AnfisParams anfisParams;
    /* Parametros de conficugracion de Best Centroids */
    private BestCentroidsParams bcParams = null;
    private CentroidsInitType centroidsInitType;
    /* T-norma a utilizar en la inferencia del fcs */
    private TCoNormType tCoNorm;
    /* Tipo de anfis a utilizar NORMAL o DESCRIPTIVE */
    private AnfisType anfisType;
    /* Clusters a ser generados por BestCentroids */
    List<ClusterInfo> clusters;


    public BasicTestInfo(){
        Calendar calendar = Calendar.getInstance();
        
        StringBuffer aux = new StringBuffer();
        aux.append(calendar.get(Calendar.DAY_OF_MONTH));
        aux.append("/");
        aux.append(calendar.get(Calendar.MONTH)+1);
        aux.append("/");
        aux.append(calendar.get(Calendar.YEAR));

        date = aux.toString();

        aux = new StringBuffer();
        aux.append(calendar.get(Calendar.HOUR_OF_DAY));
        aux.append(":");
        aux.append(calendar.get(Calendar.MINUTE));
        aux.append(":");
        aux.append(calendar.get(Calendar.SECOND));

        time = aux.toString();
    }

    public BasicTestInfo(
            String pTestType,
            String pDirectoryData,
            String pXMLDirectory,
            String pFileCentroids,
            String pFileRules,
            int[] pUtilizarEntrada,
            int[] pUtilizarSalida,
            int pNumClasses,
            int pNumRules,
            String pLearningRatio,
            CentroidsInitType pCentroidsInitType,
            Double pFuzziness,
            DistanceType pDistanceType,
            MembershipFunctionType pFunctionType,
            FiringType pFiringType,
            TCoNormType pTConorm,
            AnfisType pAnfisType,
            AnfisParams pAnfisParams,
            String pOutputDir,
            Object... otherParams) {

        Calendar calendar = Calendar.getInstance();

        init(pTestType, pDirectoryData, pXMLDirectory, pFileCentroids, pFileRules, pUtilizarEntrada, pUtilizarSalida, pNumClasses, pNumRules, pLearningRatio, pCentroidsInitType, pFuzziness, pDistanceType, pFunctionType, pFiringType, pTConorm, pAnfisType, pAnfisParams, pOutputDir, calendar, otherParams);

    }

    /* Constructor especificando el momento de generacion del test */
    public BasicTestInfo(
            String pTestType,
            String pDirectoryData,
            String pXMLDirectory,
            String pFileCentroids,
            String pFileRules,
            int[] pUtilizarEntrada,
            int[] pUtilizarSalida,
            int pNumClasses,
            int pNumRules,
            String pLearningRatio,
            CentroidsInitType pCentroidsInitType,
            Double pFuzziness,
            DistanceType pDistanceType,
            MembershipFunctionType pFunctionType,
            FiringType pFiringType,
            TCoNormType pTConorm,
            AnfisType pAnfisType,
            AnfisParams pAnfisParams,
            String pOutputDir,
            Calendar pCalendar,
            Object... otherParams) {

        init(pTestType, pDirectoryData, pXMLDirectory, pFileCentroids, pFileRules, pUtilizarEntrada, pUtilizarSalida, pNumClasses, pNumRules, pLearningRatio, pCentroidsInitType,pFuzziness, pDistanceType, pFunctionType, pFiringType, pTConorm, pAnfisType, pAnfisParams, pOutputDir, pCalendar, otherParams);

    }

    private void init(
            String pTestType,
            String pDirectoryData,
            String pXMLDirectory,
            String pFileCentroids,
            String pFileRules,
            int[] pUtilizarEntrada,
            int[] pUtilizarSalida,
            int pNumClasses,
            int pNumRules,
            String pLearningRatio,
            CentroidsInitType pCentroidsInitType,
            Double pFuzziness,
            DistanceType pDistanceType,
            MembershipFunctionType pFunctionType,
            FiringType pFiringType,
            TCoNormType pTConorm,
            AnfisType pAnfisType,
            AnfisParams pAnfisParams,
            String pOutputDir,
            Calendar calendar,
            Object... otherParams){

        testType = pTestType;
        directoryData = pDirectoryData;
        directoryXML = pXMLDirectory;
        fileCentroids = pFileCentroids;
        fileRules = pFileRules;
        utilizarEntrada = pUtilizarEntrada;
        utilizarSalida = pUtilizarSalida;
        numClasses = pNumClasses;
        numRules = pNumRules;
        learningRatio = pLearningRatio;
        fuzziness = pFuzziness;
        distanceType = pDistanceType;
        functionType = pFunctionType;
        firingType = pFiringType;
        tCoNorm = pTConorm;
        anfisType = pAnfisType;
        anfisParams = pAnfisParams;
        outputDir = pOutputDir;
        centroidsInitType = pCentroidsInitType;

        if(centroidsInitType.equals(CentroidsInitType.automatic)){
            bcParams = (BestCentroidsParams) otherParams[0];
        }

        if(testType.equals(TEST_BC)){

           clusters = (List<ClusterInfo>) otherParams[1];
        }

        /* Añadimos el caracter '/' al final si no lo tuviera */
        if(!outputDir.endsWith(File.separator)){
            outputDir += File.separator;
        }

        /* Añadimos el caracter '/' al final si no lo tuviera */
        if(!directoryData.endsWith(File.separator)){
            directoryData += File.separator;
        }

        /* Añadimos el caracter '/' al final si no lo tuviera */
        if(!directoryXML.endsWith(File.separator)){
            directoryXML += File.separator;
        }

        StringBuffer aux = new StringBuffer();
        aux.append(calendar.get(Calendar.DAY_OF_MONTH));
        aux.append("/");
        aux.append(calendar.get(Calendar.MONTH)+1);
        aux.append("/");
        aux.append(calendar.get(Calendar.YEAR));

        date = aux.toString();

        aux = new StringBuffer();
        aux.append(calendar.get(Calendar.HOUR_OF_DAY));
        aux.append(":");
        aux.append(calendar.get(Calendar.MINUTE));
        aux.append(":");
        aux.append(calendar.get(Calendar.SECOND));

        time = aux.toString();

    }

    /* Permitimos cambiar solamente el nombre del test para poder realizar */
    /* diferentes pruebas con identidos parametros, cada una de las cuales */
    /* se llamara de manera diferente */

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public void setCentroidsInitType(CentroidsInitType centroidsInitType) {
        this.centroidsInitType = centroidsInitType;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDirectoryXML(String pDirectoryXML) {
        directoryXML = pDirectoryXML;

        /* Añadimos el caracter '/' al final si no lo tuviera */
        if(!directoryXML.endsWith(File.separator)){
            directoryXML += File.separator;
        }
    }



    public void setDirectoryData(String pDirectoryData) {
        directoryData = pDirectoryData;

        /* Añadimos el caracter '/' al final si no lo tuviera */
        if(!directoryData.endsWith(File.separator)){
            directoryData += File.separator;
        }
    }

    public void setDistanceType(DistanceType distanceType) {
        this.distanceType = distanceType;
    }

    public void setFileCentroids(String fileCentroids) {
        this.fileCentroids = fileCentroids;
    }

    public void setFileRules(String fileRules) {
        this.fileRules = fileRules;
    }

    public void setFiringType(FiringType firingType) {
        this.firingType = firingType;
    }

    public void setFuzziness(Double fuzziness) {
        this.fuzziness = fuzziness;
    }

    public void setLearningRatio(String learningRatio) {
        this.learningRatio = learningRatio;
    }

    public void setNumClasses(int numClasses) {
        this.numClasses = numClasses;
    }

    public void setNumRules(int numRules) {
        this.numRules = numRules;
    }

    public void setOutputDir(String pOutputDir) {
        outputDir = pOutputDir;
        /* Añadimos el caracter '/' al final si no lo tuviera */
        if(!outputDir.endsWith(File.separator)){
            outputDir += File.separator;
        }
    }

    public void setTCoNorm(TCoNormType tCoNorm) {
        this.tCoNorm = tCoNorm;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setUtilizarEntrada(int[] utilizarEntrada) {
        this.utilizarEntrada = utilizarEntrada;
    }

    public void setUtilizarSalida(int[] utilizarSalida) {
        this.utilizarSalida = utilizarSalida;
    }

    public void setAnfisType(AnfisType anfisType) {
        this.anfisType = anfisType;
    }

    public void setAnfisParams(AnfisParams anfisParams) {
        this.anfisParams = anfisParams;
    }

    public void setBcParams(BestCentroidsParams bcParams) {
        this.bcParams = bcParams;
    }

    public void setClusters(List<ClusterInfo> clusters) {
        this.clusters = clusters;
    }

    public void setFunctionType(MembershipFunctionType functionType) {
        this.functionType = functionType;
    }


    /*************** Metodos getter ***************/

    public String getTestType() {
        return testType;
    }

    public int getNumClasses() {
        return numClasses;
    }

    public int getNumRules() {
        return numRules;
    }

    public int[] getUtilizarEntrada() {
        return utilizarEntrada;
    }

    public int[] getUtilizarSalida() {
        return utilizarSalida;
    }

    public String getDirectoryData() {
        return directoryData;
    }

    public String getDirectoryXML() {
        return directoryXML;
    }

    public String getFileCentroids() {
        return fileCentroids;
    }

    public String getFileRules() {
        return fileRules;
    }

    public String getDate() {
        return date;
    }

    public String getLearningRatio() {
        return learningRatio;
    }

    public DistanceType getDistanceType() {
        return distanceType;
    }

    public FiringType getFiringType() {
        return firingType;
    }

    public Double getFuzziness() {
        return fuzziness;
    }

    public TCoNormType getTCoNorm() {
        return tCoNorm;
    }

    public AnfisType getAnfisType() {
        return anfisType;
    }

    public AnfisParams getAnfisParams() {
        return anfisParams;
    }

    public BestCentroidsParams getBcParams() {
        return bcParams;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public String getTime() {
        return time;
    }

    public CentroidsInitType getCentroidsInitType() {
        return centroidsInitType;
    }

    public List<ClusterInfo> getClusters() {
        return clusters;
    }

    public MembershipFunctionType getFunctionType() {
        return functionType;
    }


    /*************** Fin metodos Getter ***************/


    @Override
    public String toString(){
        StringBuffer output = new StringBuffer();

        output.append("Datos relativos al test\n\n");
        output.append("Tipo de prueba (clase java): " + testType + "\n");
        output.append("Fecha de realizacion: " + date + "\n");
        output.append("Hora de realizacion: " + time + "\n");
        output.append("Entrada utilizada: {");
        for(int i = 0; i< utilizarEntrada.length-1; i++){
            output.append(utilizarEntrada[i] + ",");
        }
        output.append(utilizarEntrada[utilizarEntrada.length-1] + "}\n");
        output.append("Salida utilizada: {");
         for(int i = 0; i< utilizarSalida.length-1; i++){
            output.append(utilizarSalida[i] + ",");
        }
        output.append(utilizarSalida[utilizarSalida.length-1] + "}\n");
        output.append("Numero de reglas: " + numRules + "\n");
        output.append("Tipo de inicializacion centroides: " + centroidsInitType + "\n");
        output.append("Ratio aprendizaje/evaluacion: " + learningRatio + "\n");
        output.append("Fuzziness: " + fuzziness + "\n");
        output.append("Firing type: " + firingType + "\n");
        output.append("Distance type: " + distanceType + "\n");
        output.append("Function type: " + functionType + "\n");
        output.append("FCS T-conorm: " + tCoNorm + "\n");
        output.append("Tipo de Anfis: " + anfisType + "\n");
        output.append(anfisParams);
        
        if(bcParams != null){
            output.append(bcParams);
        }

        if(clusters != null){
            output.append("Numero de clusters a generar:\n");
            output.append(clusters);
        }

        return output.toString();
    }

}
