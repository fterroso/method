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

package models.tskModel;
import algorithms.anfis.DescriptiveAnfis;
/**
 *
 * @author as
 */
public class TSKDescriptiveClassifier extends TSKModel{
    int classes[]; //numerized classes
    DescriptiveAnfis anfis;
    TSKModel initialTSKmodel;
    TSKModel finalTSKModel;
 /*********************
 *todos estos metodos sirven para crear un modelo TSK descriptivo
 * al que le pasamos "las particiones" de las variables de entrada
 * y las posibles clases consecuentes y genera el TSK correspondiente con
 * todas las posibles combinaciones de estas particiones de entrada
 * asignando a cada regla como consecuente la clase con la fuzzy frequency mayor
 *********************/

    public TSKDescriptiveClassifier(int c[],DescriptiveAnfis a, double examples[][]){
        try{
            classes=c;
            anfis=a;
            //creamos el modelo tsk inicial a partir de anfis sin aprender
            initialTSKmodel=anfis.createTSKModel();
            int maxFF[]=classWithMaxFF(initialTSKmodel,examples);
            anfis.run();//como es descriptivo ajustara los conjuntos difusos pero manteniendolos iguales para todas las reglas
            finalTSKModel=anfis.createTSKModel();
        }
        catch(Exception e){
               System.out.println("Se ha producido un error al crear el TSKModel correspondiente a la ejecucin de ANFIS.");
                e.getStackTrace();
                System.exit(0);  
            }
    }
    
    //para cada regla calcula la clase con mayor FF
    private int[] classWithMaxFF(TSKModel tskm,double examples[][]){
            double[][] w=fuzzyFrequency(tskm,examples);
            int maxFFclass[] =new int[w.length];
            for(int r=0;r<tskm.getNumRules();r++)
                maxFFclass[r]=classes[maxPos(w[r])];
            return maxFFclass;
    }
    //calcula la frecuencia difusa de cada clase para cada regla 
    //la salida es una matriz con una fila para cada regla y una columna para
    //cada clase. Por tanto wrc es la la FF de la clase c en la regla r
    private  double[][] fuzzyFrequency(TSKModel tskm,double examples[][]){
         double w[][]= new double[tskm.getNumRules()][classes.length];   
         double[] rulesTruth;//almacenar� el grado de disparo de cada regla;  
         for (int r=0; r<tskm.getNumRules();r++)
            for(int c=0;c<classes.length;c++)
               w[r][c]=0;
         for(int p=0; p<examples.length;p++)
         {  
            rulesTruth=tskm.getTruth(examples[p]);
            for (int r=0; r<tskm.getNumRules();r++)
                if (rulesTruth[r]>0) 
                    w[r][(int)(examples[p][tskm.getAttsToOutput()])]+=rulesTruth[r];
         }         
         return w;
    }
     private int maxPos(double vector[]){
         int max=0;
         for(int i=0;i<vector.length;i++)
             if (vector[i]>vector[max]) max=i;
         return max;
     }

}
