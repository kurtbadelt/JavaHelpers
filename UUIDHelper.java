/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helpers;

import java.util.UUID;

/**
 *
 * @author kurtyanez
 */
public class UUIDHelper {

    public String getComensalID() {
        String output = "cms-";
        output += UUID.randomUUID().toString().substring(0,10);
        return output;
    }

    public String getComedorID() {
        String output = "cmd-";
        output += UUID.randomUUID().toString();
        return output;
    }

    public String getComidasPorDiaID() {
        String output = "cpd-";
        output += UUID.randomUUID().toString();
        return output;
    }

    public String getTipoComensalID() {
        String output = "tcs-";
        output += UUID.randomUUID().toString();
        return output;
    }

    public String getTipoComidaID() {
        String output = "tcd-";
        output += UUID.randomUUID().toString();
        return output;
    }

    public String getUsuarioID() {
        String output = "usr-";
        output += UUID.randomUUID().toString();
        return output;
    }

    public String getTipoUsuarioID() {
        String output = "tus-";
        output += UUID.randomUUID().toString();
        return output;
    }

    public String getExcepcionID() {
        String output = "exc-";
        output += UUID.randomUUID().toString();
        return output;
    }
}
