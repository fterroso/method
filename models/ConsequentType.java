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

package models;

/**
 *
 * @author fernando
 */
public enum ConsequentType {

    /* Consecuente de orden 0 */
    SINGLETON ("S"),
    /* Consecuente de orden mayor que 0 */
    LINEAR ("L");
    
    private final String ID;
    
    ConsequentType(String ID){
        this.ID = ID;        
    }
    
    public String getID(){
        return ID;
    }

    
}
