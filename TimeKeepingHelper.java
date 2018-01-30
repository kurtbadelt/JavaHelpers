/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helpers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 *
 * @author kurtyanez
 */
public class TimeKeepingHelper {

    public String getFecha() {
        LocalDate today = LocalDate.now();
        String output = "";
        output += today.toString();
        return output;
    }

    public String getTimeStamp() {
        LocalDateTime timeStamp = LocalDateTime.now();
        String output = "";
        output += timeStamp.toString();
        return output;
    }

    public String getHora() {
        LocalTime hora = LocalTime.now();
        String output = "";
        output += hora.toString();
        return output;
    }
}
