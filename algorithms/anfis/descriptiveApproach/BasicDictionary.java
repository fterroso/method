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
import java.util.HashMap;
import java.util.Set;

/**
 * Clase basica de diccionario para Anfis descriptivo
 *
 * @author Fernando Terroso Saenz
 */
public class BasicDictionary implements Dictionary{

    HashMap<RuleElement, Relationship> dictionary;
    
    public BasicDictionary(){
        dictionary = new HashMap<RuleElement, Relationship>();
    }

    public Relationship getRelation(RuleElement pRuleElement) {
        return dictionary.get(pRuleElement);
    }

    public void setRelation(RuleElement pRuleElement, Relationship pRelationship) {
        dictionary.put(pRuleElement, pRelationship);
    }

    public Set<RuleElement> getRuleElements(){
        return dictionary.keySet();
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        Set<RuleElement> rElements = dictionary.keySet();
        for(RuleElement rElement : rElements){
            sb.append(rElement);
            sb.append(" ");
            Relationship relation = dictionary.get(rElement);
            sb.append(relation);
            sb.append("\n");
        }

        return sb.toString();
    }
}
