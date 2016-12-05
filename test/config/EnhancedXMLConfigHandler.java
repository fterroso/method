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

import algorithms.AHC.AHCParams;
import algorithms.BestCentroids.BestCentroidsParams;
import algorithms.PartitionedSpace;
import algorithms.anfis.AnfisParams;
import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import models.ConsequentType;
import models.FiringType;
import models.MembershipFunctionType;
import models.fcsModel.Constants.DistanceType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import test.info.EnhancedTestInfo;
import test.info.AlgorithmInfo;
import test.info.ModelInfo;
import test.info.VarSet;
import test.info.algorithm.fromdata.ValidationType;
import test.info.algorithm.fromdata.CrossoverInfo;
import test.info.algorithm.fromdata.LearningRatio;

/**
 * Enhanced xml config parser
 *
 * @author Fernando Terroso
 */
public class EnhancedXMLConfigHandler implements ConfigHandler {

    EnhancedTestInfo info = new EnhancedTestInfo();    
    
    public EnhancedXMLConfigHandler(String pURL){
        setSource(pURL);
    }
    
    public void setSource(String pURL) {
        
        try{
            File configFile = new File(pURL);
            Scanner scan = new Scanner(configFile);  
            scan.useDelimiter("\\Z");  
            String configFileContent = scan.next();
            info.setConfigFileContent(configFileContent);
            
            SAXBuilder builder=new SAXBuilder(false);
            Document doc=builder.build(pURL);
            Element root =doc.getRootElement();

            Element type = root.getChild("Type");
            parseTypeElement(type);

            Element dirInfo = root.getChild("DirInfo");
            parseDirInfo(dirInfo);

            Element entryInfo = root.getChild("EntryInfo");
            parseEntryInfoElement(entryInfo);
            
            Element modelInfo = root.getChild("ModelsInfo");
            parseModelInfoElement(modelInfo);

            Element algInfoE = root.getChild("AlgorithmsInfo");
            parseAlgInfoElement(algInfoE);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    protected void parseTypeElement(Element type){
        info.setTestType(type.getText());        
    }
    
    protected void parseDirInfo(Element dirInfo){
        Element in = dirInfo.getChild("DataDir");
        info.setDirectoryData(in.getText());
        
        Element out = dirInfo.getChild("OutputDir");
        info.setOutputDir(out.getText());
    }
    
    protected void parseEntryInfoElement(Element entryInfo){
        Element varSetsE = entryInfo.getChild("VarSets");
        List<Element> varSets = varSetsE.getChildren("VarSet");
        List<VarSet> listVarSets = new LinkedList<VarSet>();
        for(Element varSetE : varSets){
            String inStr = varSetE.getChild("InputVar").getText();
            String[] ins = inStr.split(",");
            int[] in = new int[ins.length];
            for(int i = 0; i< ins.length; i++){
                in[i] = Integer.valueOf(ins[i]);
            }
            
            String outStr = varSetE.getChild("OutputVar").getText();
            String[] outs = outStr.split(",");
            int[] out = new int[outs.length];
            for(int i = 0; i< outs.length; i++){
                out[i] = Integer.valueOf(outs[i]);
            }
            
            VarSet varSet = new VarSet();
            varSet.setInput(in);
            varSet.setOutput(out);            
            
            listVarSets.add(varSet);
            
        }
        info.setVarSets(listVarSets);
    }
    
    protected void parseModelInfoElement(Element modelInfoE){
        
        ModelInfo modelInfo = new ModelInfo();
        
        String xml = modelInfoE.getChild("SerializeXML").getText();
        boolean val = xml.equals("YES");
        modelInfo.setGenerateXML(val);
        
        String plot = modelInfoE.getChild("GeneratePlot").getText();
        val = plot.equals("YES");
        modelInfo.setGeneratePlot(val);
        
        info.setModelInfo(modelInfo);
    }    
    
    protected void parseAlgInfoElement(Element algInfoElement){
        
        AlgorithmInfo algInfo = new AlgorithmInfo();
                
        String funcType = algInfoElement.getChild("FunctionType").getText();               
        algInfo.setMembershipType(MembershipFunctionType.valueOf(funcType.toUpperCase()));

        String consqType = algInfoElement.getChild("ConsequentType").getText();       
        algInfo.setConsequentType(ConsequentType.valueOf(consqType.toUpperCase()));
        
        String distanceType = algInfoElement.getChild("DistanceType").getText();       
        algInfo.setDistanceType(DistanceType.valueOf(distanceType.toLowerCase()));
        
        String firingType = algInfoElement.getChild("FiringType").getText();       
        algInfo.setFiringType(FiringType.valueOf(firingType.toUpperCase()));
        
        String algSeqStr = algInfoElement.getChildText("AlgSeq");
        String[] algSeq = algSeqStr.split(",");
        
        List<String> algSeqList = Arrays.asList(algSeq);
        algInfo.setAlgothmSeq(algSeqList);        
                          
            Element validationE = algInfoElement.getChild("validation");
          
            Element typeE = validationE.getChild("type");
            String typeStr = typeE.getText();
            
            ValidationType type = ValidationType.valueOf(typeStr.toUpperCase());
            algInfo.setValidationType(type);
            
            switch(type){
                case CROSS_VALIDATION:
                    CrossoverInfo crossover = parseCrossValidationElement(validationE.getChild("cross_validation"));
                    algInfo.setCrossValidationInfo(crossover);
                    break;
            } 
        
        Element bestCentroidElement = algInfoElement.getChild("BestCentroidsInfo");
        
        if(bestCentroidElement != null){
            BestCentroidsParams bcInfo = new BestCentroidsParams();
            
            bcInfo.setAlfa(Double.valueOf(bestCentroidElement.getChild("Alfa").getText()));            
            bcInfo.setEpsilon(Double.valueOf(bestCentroidElement.getChild("Epsilon").getText()));            
            bcInfo.setCmax(Integer.valueOf(bestCentroidElement.getChild("Cmax").getText()));
            bcInfo.setMaxIteration(Integer.valueOf(bestCentroidElement.getChild("MaxIteration").getText()));
            bcInfo.setUmbral(Double.valueOf(bestCentroidElement.getChild("Umbral").getText()));
            bcInfo.setOutputDir(bestCentroidElement.getChild("BCOutputDir").getText());
            
            Element ahcElement = bestCentroidElement.getChild("AHCInfo");
            AHCParams ahcInfo = parseAHCElement(ahcElement);
            
            bcInfo.setAhcParams(ahcInfo);       
            
            String space = bestCentroidElement.getChild("ClusterSpace").getText();
            bcInfo.setSpace(PartitionedSpace.valueOf(space.toUpperCase()));

            algInfo.setBestCentroidInfo(bcInfo);
        }

        Element anfisElement = algInfoElement.getChild("AnfisInfo");
        
        if(anfisElement != null){
            AnfisParams anfisInfo = new AnfisParams();
                     
            anfisInfo.setConsequentType(ConsequentType.valueOf(anfisElement.getChild("AnfisConsequentType").getText().toUpperCase()));
            anfisInfo.setEpochs(Integer.valueOf(anfisElement.getChild("Epochs").getText()));
            anfisInfo.setKappa(Double.valueOf(anfisElement.getChild("Kappa").getText()));
            anfisInfo.setM(Integer.valueOf(anfisElement.getChild("M").getText()));
            anfisInfo.setN(Integer.valueOf(anfisElement.getChild("N").getText()));
            anfisInfo.setP(Double.valueOf(anfisElement.getChild("P").getText()));
            anfisInfo.setQ(Double.valueOf(anfisElement.getChild("Q").getText()));
            
            algInfo.setAnfisInfo(anfisInfo);
        }
        info.setAlgInfo(algInfo);
    }
    
    protected CrossoverInfo parseCrossValidationElement(Element crossValidationElement){
        CrossoverInfo crossover = new CrossoverInfo();

        Element ratioE = crossValidationElement.getChild("LearningRatio");
        int train = Integer.valueOf(ratioE.getChild("Train").getText());
        int test = Integer.valueOf(ratioE.getChild("Test").getText());
        
        LearningRatio ratio = new LearningRatio();
        ratio.setTest(test);
        ratio.setTrain(train);
        
        crossover.setLearningRatio(ratio);
        
        return crossover;
        
    }
    
    
    protected AHCParams parseAHCElement(Element AHCElement){
        
        AHCParams ahcInfo = new AHCParams();
        
        String tempPath = AHCElement.getChild("TempXMLDir").getText();
        ahcInfo.setXMLPath(tempPath);
        
        return ahcInfo;
    }

    public EnhancedTestInfo getTestInfo() {
        return info;
    }
   
}
