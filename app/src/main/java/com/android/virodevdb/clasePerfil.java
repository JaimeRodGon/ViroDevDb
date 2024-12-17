package com.android.virodevdb;

import android.os.Parcel;

import java.io.Serializable;

public class clasePerfil implements Serializable {

    private String email;
    private String nombre;
    private String apellidos;
    private String dniCif;
    private String direccion;
    private String cp;
    private String telefono;

    public clasePerfil(String email, String nombre, String apellidos,
                       String dniCif, String direccion, String cp, String telefono){

        this.email = email;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dniCif = dniCif;
        this.direccion = direccion;
        this.cp = cp;
        this.telefono = telefono;


    }

    protected clasePerfil(Parcel in) {
        email= in.readString();
        nombre = in.readString();
        apellidos = in.readString();
        dniCif= in.readString();
        direccion = in.readString();
        cp = in.readString();
        telefono = in.readString();

    }


    //getters y setters
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email=email;
    }


    public String getNombre(){
        return nombre;
    }
    public void setNombre(String nombre){
        this.nombre=nombre;
    }

    public String getApellidos(){ return apellidos;}
    public void setApellidos(String apellidos){
        this.apellidos=apellidos;
    }

    public String getDniCif(){ return dniCif;}
    public void setDniCif(String dniCif1){
        this.dniCif=dniCif;
    }

    public String getDireccion(){ return direccion;}
    public void setDireccion(String direccion){
        this.direccion=direccion;
    }

    public String getCp(){ return cp;}
    public void setCp(String cp){
        this.cp=cp;
    }

    public String getTelefono(){ return telefono;}
    public void setTelefono(String telefono){
        this.telefono=telefono;
    }

    @Override
    public String toString() {
        return  email +"\n"
                + nombre +"\n"
                + apellidos +"\n"
                + dniCif +"\n"
                + direccion +"\n"
                + cp +"\n"
                + telefono;

    }


}
