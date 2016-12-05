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

package main;

import test.enhanced.EnhancedTest;
import test.config.ConfigHandler;
import test.config.ConfigHandlerFactory;
import test.info.BasicTestInfo;
import test.info.EnhancedTestInfo;
import test.printers.EnhancedTextPrinter;
import test.printers.EnhancedPrinter;


/**
 * Clase encargada de lanzar la instancia que lanza toda la batería de pruebas
 * destinadas al artículo.
 * @author Fernando Terroso Saenz
 */
public class AlgoritmosDifusosMain {


    public static void main(String args[]){
        try{

            System.out.println("Starting tests...");

            ConfigHandler confHandler =ConfigHandlerFactory.createConfigHandler(args[0]);
            BasicTestInfo info = confHandler.getTestInfo();

            // Obtenemos una instancia de la clase de test que el usuario nos haya
            // indicado a través del fichero/XML de configuracion
            String type = info.getTestType();
            
            Class clase = Class.forName(type);
            Object obj = clase.newInstance();
            
            /* version 2 code */
            EnhancedTest test = EnhancedTest.class.cast(obj);

            EnhancedPrinter printer = new EnhancedTextPrinter();

            test.test((EnhancedTestInfo)info, printer);

            System.out.println("TEST FINISHED!");
         
        }catch(Exception e){
            System.out.println("AN ERROR HAS ocurred: "+e.getMessage());
            e.printStackTrace();
        }
    }
}
