/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helpers;

import DataLayer.ReporteComidasPorDia;
import DataLayer.ReporteDetalleComidas;
import DataLayer.ReporteDetalleExcepciones;
import DataLayer.ReporteExcepcionesPorDia;
import DataLayer.ReportePorTipoComida;
import DataLayer.ReportePorTipoComidaTodosComensales;
import DataLayer.ReporteVentaDiaria;
import javax.swing.JTable;

/**
 *
 * @author kurtbadelt
 */
public class ReportsHelper {

    public void ejecutarReporte(int tipo, JTable jTable1, String modificador) {

        DataBaseHelper dbh = new DataBaseHelper();
        Util util = new Util();
        switch (tipo) {
            case 1:
                ReporteComidasPorDia reporteCPD = new ReporteComidasPorDia();

                dbh.createConnection();
                dbh.ejecutarReporte(reporteCPD);
                try {
                    util.fillTable(jTable1, reporteCPD.ListToArray(), reporteCPD.getHeaders(), ";");
                } catch (Exception ex) {
                    util.writeErrorToLog(ex, "PanelReportesTablas_ejecutarReporte_" + tipo);
                }

                break;
            case 2:
                ReporteDetalleComidas reporteDC = new ReporteDetalleComidas();
                dbh.createConnection();
                dbh.ejecutarReporteDetalle(reporteDC);

                try {
                    util.fillTable(jTable1, reporteDC.ListToArray(), reporteDC.getHeaders(), ";");
                } catch (Exception ex) {
                    util.writeErrorToLog(ex, "PanelReportesTablas_ejecutarReporte_" + tipo);
                }
                break;
            case 3:

                ReporteExcepcionesPorDia reporteEPD = new ReporteExcepcionesPorDia();

                dbh.createConnection();
                dbh.ejecutarReporte(reporteEPD);
                try {
                    util.fillTable(jTable1, reporteEPD.ListToArray(), reporteEPD.getHeaders(), ";");
                } catch (Exception ex) {
                    util.writeErrorToLog(ex, "PanelReportesTablas_ejecutarReporte_" + tipo);
                }

                break;
            case 4:
                ReporteDetalleExcepciones reporteDE = new ReporteDetalleExcepciones();
                dbh.createConnection();
                dbh.ejecutarReporteDetalle(reporteDE);

                try {
                    util.fillTable(jTable1, reporteDE.ListToArray(), reporteDE.getHeaders(), ";");
                } catch (Exception ex) {
                    util.writeErrorToLog(ex, "PanelReportesTablas_ejecutarReporte_" + tipo);
                }
                break;
            case 5:
                ReporteVentaDiaria reporteVD = new ReporteVentaDiaria();

                dbh.createConnection();
                dbh.ejecutarReporte(reporteVD);
                try {
                    util.fillTable(jTable1, reporteVD.ListToArray(), reporteVD.getHeaders(), ";");
                } catch (Exception ex) {
                    util.writeErrorToLog(ex, "PanelReportesTablas_ejecutarReporte_" + tipo);
                }

                break;
            case 6:
                ReportePorTipoComida reporteTC = new ReportePorTipoComida(modificador);

                dbh.createConnection();
                dbh.ejecutarReporteDetalle(reporteTC);
                try {
                    util.fillTable(jTable1, reporteTC.ListToArray(), reporteTC.getHeaders(), ";");
                } catch (Exception ex) {
                    util.writeErrorToLog(ex, "PanelReportesTablas_ejecutarReporte_" + tipo);
                    ex.printStackTrace();
                }

                break;
            case 7:
                ReportePorTipoComidaTodosComensales reporteTCAC = new ReportePorTipoComidaTodosComensales();

                dbh.createConnection();
                dbh.ejecutarReporteDetalle(reporteTCAC);
                try {
                    util.fillTable(jTable1, reporteTCAC.ListToArray(), reporteTCAC.getHeaders(), ";");
                } catch (Exception ex) {
                    util.writeErrorToLog(ex, "PanelReportesTablas_ejecutarReporte_" + tipo);
                    ex.printStackTrace();
                }

                break;
        }

    }

}
