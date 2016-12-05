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

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 * This class chooses the most suitable config class.
 *
 * @author Fernando Terroso-Saenz
 */
public class ConfigHandlerFactory {
    
    public static ConfigHandler createConfigHandler(String configPath){
        
        ConfigHandler config = null;
        
        try{
           if(configPath.endsWith("xml")){        
                SAXBuilder builder=new SAXBuilder(false);
                Document doc=builder.build(configPath);
                Element root =doc.getRootElement();                   
        
                String version = root.getChild("Version").getText();
                
                if(version.equals("1")){
                    config = new BasicXMLConfigHandler(configPath);
                }else if(version.equals("2")){
                    config = new EnhancedXMLConfigHandler(configPath);
                }                
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return config;
    }
    
}
