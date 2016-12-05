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

package test.config;

import algorithms.BestCentroids.BestCentroidsParams;
import algorithms.BestCentroids.ClusterInfo;
import algorithms.PartitionedSpace;
import algorithms.anfis.AnfisParams;
import algorithms.anfis.AnfisType;
import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.Attributes;
import models.ConsequentType;
import models.FiringType;
import models.MembershipFunctionType;
import models.TCoNormType;
import models.fcsModel.Constants.DistanceType;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import test.CentroidsInitType;
import test.info.BasicTestInfo;

/**
 * Clase que implementa la interfaz ConfigHandler y lee los datos de configuracion
 * de un fichero XML
 *
 * @author Fernando Terroso Saenz
 */
public class BasicXMLConfigHandler implements ConfigHandler {


    String url;
    BasicTestInfo testInfo = null;
    AnfisParams anfisParams = null;
    BestCentroidsParams  bcParams = null;

    /* Variables necesarias para el procesamiento de los clusters */
    List<Integer> clusterInputs;
    int clusterOutput;
    int clusterNum;
    PartitionedSpace clusterSpace;

    List<ClusterInfo> clusters;

    /**
     * Constructor de la clase
     * @param pUrl Url del XML de configuracion
     */
    public BasicXMLConfigHandler(String pUrl){
        url = pUrl;
    }

    /**
     * Metodo para cambiar el XML de configuracion a parsear
     * @param pUrl
     */
    public void setSource(String pUrl) {
        url = pUrl;
        // Como la URL es nueva inicializamos de nuevo las dos instancias
        testInfo = null;
        anfisParams = null;
        bcParams = null;
    }

    /**
     * Devuelve un objeto BasicTestInfo encapsulando toda la informacion de
     * configuracion del test
     * @return Un objeto BasicTestInfo
     */
    public BasicTestInfo getTestInfo() {
        //Mientras no cambie la URL devolvemos siempre la misma informacion
        if(testInfo == null){
            try{

                testInfo = new BasicTestInfo();

                anfisParams = new AnfisParams();
                bcParams = new BestCentroidsParams();

                clusters = new LinkedList<ClusterInfo>();
                clusterInputs = new LinkedList<Integer>();

                XMLReader reader = XMLReaderFactory.createXMLReader();
                reader.setContentHandler(new XMLParser());
                reader.parse(new InputSource(new FileInputStream(url)));
                testInfo.setAnfisParams(anfisParams);
                testInfo.setBcParams(bcParams);
            }catch(Exception e){
                System.out.println("Error en la lectura del XML de configuracion: " + e.getLocalizedMessage());
                e.printStackTrace();
            }
        }
        
        return testInfo;
    }

    /**
     * Clase interna encargada de parsear el XML de configuracion
     */

    private class XMLParser extends DefaultHandler{

        String value;

        public void startElement(
                String uri,
                String localName,
                String name,
                Attributes attributes) throws SAXException {
            value = null;
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            // Guardamos el texto en la variable temporal
            value = new String(ch,start,length);
        }

        /**
         * Metodo que salta al llegar a la etiqueta final </> de un elemento
         * @param uri
         * @param localName
         * @param name
         * @throws org.xml.sax.SAXException
         */
        @Override
        public void endElement(String uri, String localName, String name)
          throws SAXException {

            if (localName.equals("Type")){
               testInfo.setTestType(value);
           }else if (localName.equals("DirectoryCircuits")){
               testInfo.setDirectoryData(value);
           }else if (localName.equals("DirectoryXML")){
               testInfo.setDirectoryXML(value);
           }else if (localName.equals("FileCentroids")){
               testInfo.setFileCentroids(value);
           }else if (localName.equals("FileRules")){
               testInfo.setFileRules(value);
           }else if (localName.equals("OutputDir")){
               testInfo.setOutputDir(value);
           }else if (localName.equals("NumClasses")){
               int aux = Integer.valueOf(value);
               testInfo.setNumClasses(aux);
           }else if (localName.equals("NumRules")){
               int aux = Integer.valueOf(value);
               testInfo.setNumRules(aux);
           }else if (localName.equals("Fuzziness")){
               double aux = Double.valueOf(value);
               testInfo.setFuzziness(aux);
           }else if (localName.equals("DistanceType")){
               DistanceType dt = DistanceType.valueOf(value);
               testInfo.setDistanceType(dt);
           }else if (localName.equals("FunctionType")){
               MembershipFunctionType ft = MembershipFunctionType.valueOf(value.toUpperCase());
               testInfo.setFunctionType(ft);
           }else if (localName.equals("FiringType")){
               FiringType ft = FiringType.valueOf(value);
               testInfo.setFiringType(ft);
           }else if (localName.equals("CentroidsInitType")){
               CentroidsInitType cit = CentroidsInitType.valueOf(value);
               testInfo.setCentroidsInitType(cit);
           }else if (localName.equals("FSC-T-conorm")){
               TCoNormType tn = TCoNormType.valueOf(value);
               testInfo.setTCoNorm(tn);
           }else if (localName.equals("AnfisType")){
               AnfisType at = AnfisType.valueOf(value.toUpperCase());
               testInfo.setAnfisType(at);
           }else if (localName.equals("AnfisConsequentType")){
               ConsequentType ct = ConsequentType.valueOf(value.toUpperCase());
               anfisParams.setConsequentType(ct);
           }else if (localName.equals("Epochs")){
               anfisParams.setEpochs(Integer.valueOf(value));
           }else if (localName.equals("Kappa")){
               anfisParams.setKappa(Double.valueOf(value));
           }else if (localName.equals("M")){
               anfisParams.setM(Integer.valueOf(value));
           }else if (localName.equals("N")){
               anfisParams.setN(Integer.valueOf(value));
           }else if (localName.equals("P")){
               anfisParams.setP(Double.valueOf(value));
           }else if (localName.equals("Q")){
               anfisParams.setQ(Double.valueOf(value));
           }else if (localName.equals("Alfa")){
               bcParams.setAlfa(Double.valueOf(value));
           }else if (localName.equals("Epsilon")){
               bcParams.setEpsilon(Double.valueOf(value));
           }else if (localName.equals("Umbral")){
               bcParams.setUmbral(Double.valueOf(value));
           }else if (localName.equals("MaxIteration")){
               bcParams.setMaxIteration(Integer.valueOf(value));
           }else if (localName.equals("BCOutputDir")){
               bcParams.setOutputDir(value);
           }else if (localName.equals("ClusterSpace")){
               clusterSpace = PartitionedSpace.valueOf(value);  
           }else if (localName.equals("Input")){
               String[] aux = value.split(",");
               for(String a : aux){
                   clusterInputs.add(Integer.valueOf(a));
               }
           }else if (localName.equals("Output")){
               clusterOutput = Integer.valueOf(value);
           }else if (localName.equals("NumClusters")){
               clusterNum = Integer.valueOf(value);
           }else if (localName.equals("Cluster")){
               int aux[] = new int[clusterInputs.size()];
               for(int i= 0; i< clusterInputs.size(); i++){
                   aux[i] = clusterInputs.get(i);
               }               
               ClusterInfo ci = new ClusterInfo(aux, clusterOutput, clusterNum, clusterSpace);
               clusters.add(ci);
               clusterInputs = new LinkedList<Integer>();
           }else if (localName.equals("Clusters")){
               testInfo.setClusters(clusters);
           }else if (localName.equals("InputColumns")){
               String[] aux = value.split(",");
               int inCol[] = new int[aux.length];
               int i = 0;
               for(String a : aux){
                   inCol[i++] = Integer.valueOf(a);
               }
               testInfo.setUtilizarEntrada(inCol);
           }else if (localName.equals("OutputColumns")){
               String[] aux = value.split(",");
               int outCol[] = new int[aux.length];
               int i = 0;
               for(String a : aux){
                   outCol[i++] = Integer.valueOf(a);
               }
               testInfo.setUtilizarSalida(outCol);
           }else if (localName.equals("LearningRatio")){
               testInfo.setLearningRatio(value);
           }
        }
    }
}
