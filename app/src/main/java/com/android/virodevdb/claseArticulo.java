package com.android.virodevdb;

import android.os.Parcel;

import java.io.Serializable;

public class claseArticulo implements Serializable {

    private String numArticulo;
    private String refArticulo;
    private String nomArticulo;
    private String precArticulo;

    public claseArticulo(String numArticulo, String refArticulo, String nomArticulo,
                         String precArticulo){

        this.numArticulo = numArticulo;
        this.refArticulo = refArticulo;
        this.nomArticulo = nomArticulo;
        this.precArticulo = precArticulo;


    }

    protected claseArticulo(Parcel in) {
        numArticulo = in.readString();
        refArticulo = in.readString();
        nomArticulo = in.readString();
        precArticulo = in.readString();
    }


    //getters y setters
    public String getNumArticulo(){
        return numArticulo;
    }
    public void setNumArticulo(String numArticulo){
        this.numArticulo=numArticulo;
    }

    public String getRefArticulo(){
        return refArticulo;
    }
    public void setRefArticulo(String refArticulo){
        this.refArticulo=refArticulo;
    }

    public String getNomArticulo(){
        return nomArticulo;
    }
    public void setNomArticulo(String nomArticulo){
        this.nomArticulo=nomArticulo;
    }

    public String getPrecArticulo(){
        return precArticulo;
    }
    public void setPrecArticulo(String precArticulo){
        this.precArticulo=precArticulo;
    }

    @Override
    public String toString() {
        return  refArticulo +"\n"
                + nomArticulo +"\n"
                + precArticulo;
    }



}
