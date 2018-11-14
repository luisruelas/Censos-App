package com.example.ruelas.ivanluis4e;

/**
 * Created by Ruelas on 27/10/2016.
 */
public class Paciente {

    private String nombre;
    private String fecha;
    private float peso;
    private float talla;
    private float temperatura;
    private float perdidassen;
    private int id;

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    String apellido;
    private Boolean sexo;
    Paciente(int id, String nombre,Boolean sexo, Float peso, Float talla, Float temperatura, Float perdidassen, String fecha, String apellido){
        this.nombre=nombre;
        this.id=id;
        this.fecha = fecha;
        this.peso = peso;
        this.talla = talla;
        this.temperatura = temperatura;
        this.perdidassen = perdidassen;
        this.sexo=sexo;
        this.apellido=apellido;

    }
    public int getid(){
        return this.id;
    }
    public void setid(int id){
        this.id=id;
    }
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    public Float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    public Float getTalla() {
        return talla;
    }

    public void setTalla(float talla) {
        this.talla = talla;
    }

    public Float getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(float temperatura) {
        this.temperatura = temperatura;
    }

    public Float getPerdidassen() {
        return perdidassen;
    }

    public void setPerdidassen(float perdidassen) {
        this.perdidassen = perdidassen;
    }

    public void setSexo(Boolean sexo) {
        this.sexo = sexo;
    }

    public Boolean getSexo() {
        return sexo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getFecha(){return this.fecha;}

    public String parseFecha(String fecha) {
        if(fecha.length()==14){
        String min=fecha.substring(0,2);
        String hora=fecha.substring(2,4);
        if(hora.equals("00")){
            hora="12";
        }
        String dia=fecha.substring(4,6);
        String mes=fecha.substring(6,8);
        String año=fecha.substring(8,12);
        String ampm=fecha.substring(12,14);
            return new String(hora+":"+min+ampm+" "+dia+"/"+mes+"/"+año);}
        else{return "Fecha erróenea";}
    }
}
