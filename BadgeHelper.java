/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helpers;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author kurtyanez
 */
public class BadgeHelper {

    String titulo = "";
    String comedor = "";
    String nombreEmpleado = "";
    String numeroEmpleado = "";
    Util util;

    public BadgeHelper(String titulo, String comedor, String nombreEmpleado, String numeroEmpleado) {
        util = new Util();
        this.titulo = titulo;
        this.comedor = comedor;
        this.nombreEmpleado = nombreEmpleado;
        this.numeroEmpleado = numeroEmpleado;
    }

    public void crearCredencial(String archivoCodigoBarras, String archivoLogo, String archivoSalida) {
        int ancho = 1028;
        int alto = 673;
        int xInicioTexto = 50;
        int yInicioTexto = 70;

        BufferedImage bufferedImage = new BufferedImage(ancho, alto,
                BufferedImage.TYPE_INT_RGB);
        Graphics graphics = bufferedImage.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, ancho, alto);
        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font("Century Gothic", Font.BOLD, 35));

        graphics.drawString(titulo, xInicioTexto, yInicioTexto);
        graphics.drawString("Comedor: " + comedor, xInicioTexto, yInicioTexto + 50);

        if (this.nombreEmpleado.length() < 25) {
            System.out.println(" caso 1 Longitud " + this.nombreEmpleado.length());
            graphics.drawString("Nombre: " + nombreEmpleado, xInicioTexto, yInicioTexto + 100);
            graphics.drawString("Número Empleado: " + numeroEmpleado, xInicioTexto, yInicioTexto + 155);
        } else {

            if (this.nombreEmpleado.length() > 28) {

                String tmp = nombreEmpleado.substring(0, 28) + "...";
                graphics.drawString("Nombre: ", xInicioTexto, yInicioTexto + 100);
                graphics.drawString(tmp, xInicioTexto, yInicioTexto + 155);
                graphics.drawString("Número Empleado: " + numeroEmpleado, xInicioTexto, yInicioTexto + 200);
            }
            if (this.nombreEmpleado.length() <= 28) {

                graphics.drawString("Nombre: ", xInicioTexto, yInicioTexto + 100);
                graphics.drawString(nombreEmpleado, xInicioTexto, yInicioTexto + 155);
                graphics.drawString("Número Empleado: " + numeroEmpleado, xInicioTexto, yInicioTexto + 200);
            }

        }

        try {
            BufferedImage barcode = null;
            BufferedImage logo = null;
            barcode = ImageIO.read(new File(archivoCodigoBarras));
            logo = ImageIO.read(new File(archivoLogo));

            int anchoBarcode = barcode.getWidth() + 250;
            int xInicioCB = (ancho - anchoBarcode) / 2;

            graphics.drawImage(barcode, xInicioCB, (alto - (barcode.getHeight() + 150)),
                    anchoBarcode, barcode.getHeight() + 25, null);

            graphics.drawImage(logo, (ancho - (logo.getWidth() + 25)), 15, logo.getWidth(), logo.getHeight(), null);

            ImageIO.write(bufferedImage, "jpg", new File(archivoSalida + ".jpg"));

        } catch (IOException e) {
            util.writeErrorToLog(e, "BadgeHelper_CrearCredencial");
            e.printStackTrace();

        }

    }

}
