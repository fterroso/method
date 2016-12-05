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

package test.basic;

import algorithms.BestCentroids.BestCentroids;
import algorithms.BestCentroids.BestCentroidsParams;
import algorithms.BestCentroids.ClusterInfo;
import datas.DirFilter;
import datas.FileAccess;
import datas.FileType;
import java.io.File;
import java.util.Arrays;
import java.util.StringTokenizer;
import models.fcsModel.FuzzyClusterSet;
import models.tskModel.TSKModel;
import test.info.BasicTestInfo;
import test.printers.BasicPrinter;
import utility.PermutationGenerator;

/**
 * Clase encargada de generar los clusters iniciales (FCSs) y guardarlos en
 * formato XML para poder ser utilizados en posteriores pruebas.
 *
 * @author Fernando Terroso Saenz
 */
public class TestBestCentroids extends BasicTest{

    @Override
    public void executeTest(BasicTestInfo pInfo, BasicPrinter pPrinter) throws Exception {

        info = pInfo;

        File fileData = new File(info.getDirectoryData());

        //Filtro para solo leer los ficheros .txt del directorio de datos de entrada
        DirFilter filter = new DirFilter("\\w+\\.txt");

        String learningRatio = info.getLearningRatio();

        StringTokenizer st = new StringTokenizer(learningRatio, ":");
        String learningElements = st.nextToken();
        int lElements = Integer.valueOf(learningElements);

        File[] totalFiles = fileData.listFiles(filter);

        PermutationGenerator pgLearn = new PermutationGenerator(totalFiles.length, lElements);

        int[] learnIndexes = pgLearn.next();

        System.out.println("Comenzando las pruebas...");

        do{

            File[] learnFiles = new File[lElements];

            for(int i = 0; i< lElements; i++){
                int learnIndex = learnIndexes[i];
                learnFiles[i] = totalFiles[learnIndex];
            }

            System.out.print("Fichero(s) a utilizar: ");
            for(int i= 0; i<= learnFiles.length-2; i++){
                //Quitamos la extension txt de los nombres de los ficheros
                StringTokenizer fLearnNameT = new StringTokenizer(learnFiles[i].getName(), ".");
                String fLearnName = fLearnNameT.nextToken();
                System.out.print(fLearnName + ", ");
            }
            StringTokenizer fLearnNameT = new StringTokenizer(learnFiles[learnFiles.length-1].getName(), ".");
            String fLearnName = fLearnNameT.nextToken();
            System.out.println(fLearnName);

            getClusters(learnFiles);

        }while((learnIndexes = pgLearn.next()) != null);

    }


    /**
     * Metodo que se encarga de generar los clusters solicitados en cada una
     * de las combinaciones de ficheros de aprendizaje
     * @param pLearnFiles Conjunto de ficheros de aprendizaje sobre los que queremos
     * encontrar los clusters
     * @return
     * @throws java.lang.Exception
     */
    protected TSKModel[] getClusters(File[] pLearnFiles) throws Exception{

        TSKModel[] tsks = new TSKModel[info.getClusters().size()];

        int index = 0;
        for(ClusterInfo ci : info.getClusters()){

            System.out.println("Cluster a crear:");
            System.out.println(ci);

            int in[] = new int[ci.getInputColumns().length];

            for(int j= 0; j< ci.getInputColumns().length; j++){
                in[j] = ci.getInputColumns()[j];
            }

            int out[] = {ci.getOutputColumn()};

            FileAccess fileAccess = new FileAccess();
            fileAccess.parse(pLearnFiles, in, out, FileType.TRAIN);

            double [][] inLearn = fileAccess.getInputDataLearn();
            double [] outLearn = fileAccess.getOutputDataLearn();

            BestCentroidsParams bcParams = info.getBcParams();
            bcParams.setCmax(ci.getNumClusters());

            BestCentroids bc= new BestCentroids(bcParams);
            
            StringBuilder fileNames = new StringBuilder();
            for(File f : pLearnFiles){

                fileNames.append(f.getName().replace(".txt", ""));
                fileNames.append("_");
            }
            fileNames.replace(fileNames.length()-1, fileNames.length(), "");

            StringBuilder sb = new StringBuilder();
            sb.append(fileNames.toString());
            sb.append("_input_");
            String inputCol = Arrays.toString(ci.getInputColumns()).replace("[", "");
            inputCol = inputCol.replace("]", "");
            inputCol = inputCol.replace(" ", "");
            inputCol = inputCol.replace(",", "_");
            sb.append(inputCol);

            FuzzyClusterSet fcs = bc.execute(inLearn, outLearn, ci.getSpace(), info.getDistanceType(), null, sb.toString());

            switch(info.getFunctionType()){
                case GAUSSIAN:
                    tsks[index++] = fcs.createGaussianTSKFromFCS(consequentType, info.getFiringType());
                    break;
                case BELL:
                case TRAPEZOIDAL:
                case TRIANGULAR:
                    throw new Exception("Imposible convertir FCS a " + info.getFunctionType() + " TSK");
            }
        }

        return tsks;
    }

}
