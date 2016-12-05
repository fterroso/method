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

package datas;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;

/**
 * Clase encargada de filtrar los nombre de los ficheros de un
 * directorio a traves de un patron dado (Ej: *.txt)
 * @author Fernando Terroso Saenz
 */
public class DirFilter implements FilenameFilter{

    private final Pattern pattern;

    public DirFilter(String regex){
        pattern = Pattern.compile(regex);
    }

    public boolean accept(File dir, String name){
        return pattern.matcher(name).matches();
    }

}
