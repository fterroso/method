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

package algorithms.anfis.descriptiveApproach;

import datas.RuleElement;

/**
 * Encapsula la relacion que existe entre un antecedente y otro en caso de que
 * la hubiera.
 *
 * @author Fernando Terroso Saenz
 */
public class Relationship {

    private RelationshipType type;
    private RuleElement element;
    // Posicion 0: media, posicion 1: varianza.
    private double[] values = null;

    public Relationship(RelationshipType pType, Object... pValues){
        type = pType;
        
        switch(type){
            case ABOVE:
            case BELOW:
                element = (RuleElement) pValues[0];
                break;
            case NO_RELATION:
                values = (double[]) pValues[0];
                break;
        }
    }

    public RuleElement getElement() {
        return element;
    }

    public void setElement(RuleElement element) {
        this.element = element;
    }

    public RelationshipType getType() {
        return type;
    }

    public void setType(RelationshipType type) {
        this.type = type;
    }

    public double[] getValues() {
        return values;
    }

    public void setValues(double[] values) {
        this.values = values;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        switch(getType()){
            case ABOVE:
            case BELOW:
                RuleElement e = getElement();
                sb.append(getType());
                sb.append("-->");
                sb.append(e);
                break;
            case NO_RELATION:
                double[] value = getValues();
                sb.append(value[0]);
                sb.append(", ");
                sb.append(value[1]);
                break;
        }
        sb.append("]");
        return sb.toString();
    }
}
