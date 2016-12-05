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
import algorithms.anfis.AnfisParams;
import java.util.List;
import models.ConsequentType;
import models.FiringType;
import models.MembershipFunctionType;
import models.fcsModel.Constants.DistanceType;
import test.info.algorithm.fromdata.CrossoverInfo;
import test.info.algorithm.fromdata.ValidationType;

/**
 *
 * @author calcifer
 */
public class AlgorithmInfo {

    List<String> algothmSeq;

    private ConsequentType consequentType;
    private MembershipFunctionType membershipType;
    private FiringType firingType;
    private DistanceType distanceType;
    
    private AnfisParams anfisInfo;
    private BestCentroidsParams bestCentroidInfo;

    private String algorithmSource;

    ValidationType validationType;
    CrossoverInfo crossValidationInfo;

    public CrossoverInfo getCrossValidationInfo() {
        return crossValidationInfo;
    }

    public void setCrossValidationInfo(CrossoverInfo crossValidationInfo) {
        this.crossValidationInfo = crossValidationInfo;
    }
    
    public ValidationType getValidationType() {
        return validationType;
    }

    public void setValidationType(ValidationType validationType) {
        this.validationType = validationType;
    }

    public List<String> getAlgothmSeq() {
        return algothmSeq;
    }

    public void setAlgothmSeq(List<String> algothmSeq) {
        this.algothmSeq = algothmSeq;
    }
    
    public AnfisParams getAnfisInfo() {
        return anfisInfo;
    }

    public void setAnfisInfo(AnfisParams anfisInfo) {
        this.anfisInfo = anfisInfo;
    }

    public BestCentroidsParams getBestCentroidInfo() {
        return bestCentroidInfo;
    }

    public void setBestCentroidInfo(BestCentroidsParams bestCentroidInfo) {
        this.bestCentroidInfo = bestCentroidInfo;
    }

    public ConsequentType getConsequentType() {
        return consequentType;
    }

    public void setConsequentType(ConsequentType consequentType) {
        this.consequentType = consequentType;
    }

    public MembershipFunctionType getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(MembershipFunctionType membershipType) {
        this.membershipType = membershipType;
    }

    public String getAlgorithmSource() {
        return algorithmSource;
    }

    public void setAlgorithmSource(String algorithmSource) {
        this.algorithmSource = algorithmSource;
    }

    public DistanceType getDistanceType() {
        return distanceType;
    }

    public void setDistanceType(DistanceType distanceType) {
        this.distanceType = distanceType;
    }

    public FiringType getFiringType() {
        return firingType;
    }

    public void setFiringType(FiringType firingtype) {
        this.firingType = firingtype;
    }
    
}
