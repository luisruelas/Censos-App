package com.example.ruelas.ivanluis4e;

import java.util.HashMap;

/**
 * Created by Ruelas on 21/10/2016.
 */
public class Converter {

    public static float calcularRB(){
        return 343.7678687f;
    }
    public static float calcularPI(){
        return 343.7678687f;
    }
    public static float calcularPS(){
        return 343.7678687f;
    }
    public static float calcularRT(){
        return 343.7678687f;
    }
    public static HashMap<String, Float> calcularExtras(){
        HashMap<String, Float> electro = new HashMap<>();
        Float sodio = new Float(140.23f);
        Float potasio = new Float(4.3f);
        Float glucosa = new Float(85.8924f);
        electro.put("sodio", sodio);
        electro.put ("potasio", potasio);
        electro.put ("glucosa", glucosa);
        return electro;
    }
    public static float calcularGXM(){
        return 343.7678687f;
    }
    public static float calcularMGXM(){
        return 343.7678687f;
    }
}
