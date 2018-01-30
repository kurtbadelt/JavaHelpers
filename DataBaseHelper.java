/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helpers;

import com.mysql.jdbc.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import DataLayer.*;
import java.sql.SQLException;

/**
 *
 * @author kurtyanez
 */
public class DataBaseHelper {

    String driverName = "com.mysql.jdbc.Driver";
    String serverName = "";
    String mydatabase = "";
    String url = "";
    String username = "";
    String password = "";
    Util util = new Util();
    Statement statement;
    Connection connection;

    public String toString() {
        String output = "";
        output = this.serverName + " " + this.mydatabase + " " + this.username + " " + this.password;
        return output;
    }

    public void createConnection() {

        try {
            this.readValuesFromConfig();

            connection = DriverManager.getConnection(url.trim(), username.trim(), password.trim());

        } catch (Exception ex) {
            util.writeErrorToLog(ex, "DBHelper_CreateConnection");

        }
    }

    public void readValuesFromConfig() {

        String filename = "DBCnf";

        String data = util.readFromConfigFile(filename);
        String[] separatedData = util.separateByToken(data, ";");

        this.serverName = separatedData[0];
        this.mydatabase = separatedData[1];
        this.username = separatedData[2];
        this.password = separatedData[3];
        url = "jdbc:mysql://" + serverName + ":3306/" + mydatabase;

    }

    ////////////////////////////////-Inserts-////////////////////////////////////
    public void insertADataLayerObject(final DataLayer datalayer, String table) {
        try {

            statement = (Statement) this.connection.createStatement();
            this.statement.execute("insert into " + table + " values(" + datalayer.toDB() + ")");

        } catch (final SQLException ex) {

            util.writeErrorToLog(ex, "DBHelper_insertADataLayerObject" + table);

        }
    }

    public void insertMultiValued(String id, String data, String table) {

        try {

            statement = (Statement) this.connection.createStatement();
            this.statement.execute("insert into " + table + " values(" + id + ",'" + data + "')");

        } catch (final SQLException ex) {

            util.writeErrorToLog(ex, "DBHelper_insertMultiValued" + table);

        }

    }

    public ArrayList selectUserByUserName(String userName) {

        final ArrayList tmp = new ArrayList();

        try {
            statement = (Statement) this.connection.createStatement();
            final ResultSet results = statement.executeQuery("select * from Usuario where nombreUsuario ='" + userName.trim() + "' and estatusEnSistema='Activo';");

            while (results.next()) {

                tmp.add(new Usuario(
                        results.getString(1),
                        results.getString(2),
                        results.getString(3),
                        util.decryptString(results.getString(4)),
                        results.getString(5)
                )
                );

            }

        } catch (final Exception ex) {
            util.writeErrorToLog(ex, "DBHelper_SelectUserByUserName");

        }

        return tmp;
    }

    public Comensal selectComensalByNombre(String nombre) {
        Comensal comensal = null;

        try {
            statement = (Statement) this.connection.createStatement();
            final ResultSet results = statement.executeQuery("select * from Comensal where nombreComensal='" + nombre.trim() + "' and estatusEnSistema='Activo';");

            while (results.next()) {

                comensal = new Comensal(
                        results.getString(1),
                        results.getString(2),
                        results.getString(3),
                        results.getString(4),
                        results.getString(5)
                );

            }

        } catch (final Exception ex) {
            util.writeErrorToLog(ex, "DBHelper_SelectComensalByNombre");

        }

        return comensal;
    }

    public ArrayList selectAllUserNames() {
        final ArrayList tmp = new ArrayList();

        try {
            statement = (Statement) this.connection.createStatement();
            final ResultSet results = statement.executeQuery("select nombreUsuario from Usuario where estatusEnSistema='Activo';");

            while (results.next()) {

                tmp.add(results.getString(1));

            }

        } catch (final Exception ex) {
            util.writeErrorToLog(ex, "DBHelper_SelectAllUserNames");

        }

        return tmp;
    }
    
    public ArrayList selectFechaFrom(String tabla){
        final ArrayList output = new ArrayList();
        try{
            statement = (Statement)this.connection.createStatement();
            final ResultSet results = statement.executeQuery("select distinct(fecha) from "+tabla);
            while (results.next()){
                output.add(results.getString(1));
            }
        }catch(final Exception ex){
            util.writeErrorToLog(ex, "SelectFechaFrom_"+tabla);
        }
        return output;
    }

    public void ejecutarReporte(ReporteChart reporte) {

        try {
            statement = (Statement) this.connection.createStatement();
            final ResultSet results = statement.executeQuery(reporte.getQuery());

            while (results.next()) {
                DatoReporte dato = new DatoReporte();
                dato.setDato(results.getDouble(1));
                dato.setNombre(results.getString(2));
                reporte.getListaDatos().add(dato);

            }

        } catch (final Exception ex) {
            util.writeErrorToLog(ex, "DBHelper_ejecutarReporte");

        }

    }

    public void ejecutarReporteDetalle(ReporteDetalle reporte) {

        try {
            statement = (Statement) this.connection.createStatement();
            final ResultSet results = statement.executeQuery(reporte.getQuery());

            while (results.next()) {

                //dato.setNombre(results.getString(2));
                String output = results.getString(1) + ";"
                        + results.getString(2) + ";"
                        + results.getString(3) + ";"
                        + results.getString(4) + ";"
                        + results.getString(5) + ";"
                        + results.getString(6) + ";"
                        + results.getString(7);

                reporte.getDatos().add(output);

            }

        } catch (final Exception ex) {
            util.writeErrorToLog(ex, "DBHelper_ejecutarReporteDetalle");

        }

    }
    


    public ArrayList getUserRol(String tipoUsuarioID) {
        final ArrayList output = new ArrayList();
        try {
            statement = (Statement) this.connection.createStatement();
            final ResultSet results = statement.executeQuery("select nombreTipoUsuario from TipoUsuario where idTipoUsuario ='" + tipoUsuarioID + "' and estatusEnSistema='Activo';");

            while (results.next()) {

                output.add(results.getString(1));

            }

        } catch (final Exception ex) {
            util.writeErrorToLog(ex, "DBHelper_SelectAllUserNames");

        }

        return output;
    }

    ///////////////////////////////-Updates-////////////////////////////////////
    public void updateString(final String table, final String field, final String value, final String key, final int id) {
        try {
            statement = (Statement) this.connection.createStatement();
            statement.execute("update " + table + " set " + field + " = '"
                    + value + "' where " + key + " = " + id);

        } catch (final Exception ex) {

            util.writeErrorToLog(ex, "DBHelper_UpdateString_" + table + "-" + field + "-" + value + "-" + key);
        }
    }

    public void updateInt(final String table, final String field, final int value, final String key, final int id) {
        try {
            statement = (Statement) this.connection.createStatement();
            statement.execute("update " + table + " set " + field + " = "
                    + value + " where " + key + " = " + id);

        } catch (final Exception ex) {
            util.writeErrorToLog(ex, "DBHelper_UpdateInt_" + table + "-" + field + "-" + value + "-" + key);
        }
    }

    public void updateString(final String table, final String field, final String value, final String key, final String id) {
        try {
            statement = (Statement) this.connection.createStatement();
            statement.execute("update " + table + " set " + field + " = '"
                    + value + "' where " + key + " = '" + id + "'");

        } catch (final Exception ex) {
            util.writeErrorToLog(ex, "DBHelper_UpdateString_" + table + "-" + field + "-" + value + "-" + key);
        }
    }

    public void updateInt(final String table, final String field, final int value, final String key, final String id) {
        try {
            statement = (Statement) this.connection.createStatement();
            statement.execute("update " + table + " set " + field + " = "
                    + value + " where " + key + " = '" + id + "'");

        } catch (final Exception ex) {
            util.writeErrorToLog(ex, "DBHelper_UpdateInt_" + table + "-" + field + "-" + value + "-" + key);
        }
    }

    public ArrayList ShowTables() {
        final ArrayList tmp = new ArrayList();

        try {
            statement = (Statement) this.connection.createStatement();
            final ResultSet results = statement.executeQuery("show tables;");

            while (results.next()) {
                String tmpString = results.getString(1);

                tmp.add(tmpString);

            }

        } catch (final SQLException ex) {
            util.writeErrorToLog(ex, "DBHelper_ShowTables");

        }

        return tmp;
    }

    public String DescribeTable(String table) {
        String output = "";

        try {
            statement = (Statement) this.connection.createStatement();
            final ResultSet results = statement.executeQuery("describe " + table);

            while (results.next()) {
                output += results.getString(1) + ";";

            }

        } catch (final SQLException ex) {
            util.writeErrorToLog(ex, "DBHelper_DescribeTable");

        }

        return output;
    }

    public int countTableColumns(String table) {
        int count = 0;
        try {
            statement = (Statement) this.connection.createStatement();
            final ResultSet results = statement.executeQuery("describe " + table);

            while (results.next()) {
                count++;

            }

        } catch (final SQLException ex) {
            util.writeErrorToLog(ex, "DBHelper_CountTableColumns");

        }
        return count;
    }

    public int countTableRows(String table) {
        int count = 0;
        try {
            statement = (Statement) this.connection.createStatement();
            final ResultSet results = statement.executeQuery("SELECT COUNT(*) FROM " + table);

            while (results.next()) {
                count = results.getInt(1);

            }

        } catch (final SQLException ex) {
            util.writeErrorToLog(ex, "DBHelper_CountTableColumns");

        }
        return count;
    }

    public Comensal selectComensalByID(String id) {
        Comensal output = null;
        try {
            statement = (Statement) this.connection.createStatement();
            final ResultSet results = statement.executeQuery("select * from Comensal where idComensal='" + id + "' and estatusEnSistema='Activo';");
            while (results.next()) {
                String idComensal = results.getString(1);
                String numeroEmpleado = results.getString(2);
                String nombreComensal = results.getString(3);
                String idTipoComensal = results.getString(4);
                String estatusEnSistema = results.getString(5);
                output = new Comensal(
                        idComensal,
                        numeroEmpleado,
                        nombreComensal,
                        idTipoComensal,
                        estatusEnSistema
                );
            }
        } catch (SQLException ex) {
            util.writeErrorToLog(ex, "DBHelper_SelectComensalByID");
        }
        return output;
    }

    public String[] selectAllFromTable(String table) {
        int rowCount = this.countTableRows(table);
        String[] output = new String[rowCount];

        try {
            statement = (Statement) this.connection.createStatement();
            final ResultSet results = statement.executeQuery("select * from " + table);
            int coulumCount = this.countTableColumns(table);

            for (int j = 0; j < rowCount; j++) {
                if (results.next()) {
                    for (int i = 1; i <= coulumCount; i++) {

                        if (i == 1) {
                            String tmp = results.getString(1);
                            output[j] = tmp + ";";

                        } else {
                            String tmp = results.getString(i);
                            output[j] += tmp + ";";

                        }

                    }
                }

            }

        } catch (final SQLException ex) {
            util.writeErrorToLog(ex, "DBHelper_SelectAllFromTable" + table);

        }

        return output;
    }

    /**
     * returns the data in a given table concatenates the id + / + description
     *
     * to be used with the multi valuated
     *
     * @param table
     * @return
     */
    public ArrayList selectIDSFrom(String table) {
        final ArrayList tmp = new ArrayList();

        try {
            statement = (Statement) this.connection.createStatement();
            final ResultSet results = statement.executeQuery("select * from " + table);

            while (results.next()) {
                String tmpString = results.getString(1);

                tmp.add(tmpString);

            }

        } catch (final SQLException ex) {
            util.writeErrorToLog(ex, "DBHelper_SelectIDsFrom->" + table);

        }

        return tmp;
    }

    public ArrayList selectIDSFromWhere(String table, String param, String field) {
        final ArrayList tmp = new ArrayList();

        try {
            statement = (Statement) this.connection.createStatement();
            final ResultSet results = statement.executeQuery("select * from " + table + " where " + field + " = '" + param + "';");
            while (results.next()) {
                String tmpString = results.getString(1);

                tmp.add(tmpString);

            }

        } catch (final SQLException ex) {
            util.writeErrorToLog(ex, "DBHelper_SelectIDsFromWhere->" + table);

        }

        return tmp;
    }

    public String selectNameFromComensales(String id) {
        String output = "";
        try {
            statement = (Statement) this.connection.createStatement();
            final ResultSet results = statement.executeQuery("select nombreComensal from comensal where idcomensal = '" + id + "' and estatusEnSistema='Activo';");

            while (results.next()) {
                String tmpString = results.getString(1);

                output = tmpString;

            }

        } catch (final SQLException ex) {
            util.writeErrorToLog(ex, "DBHelper_SelectNameFromComensales");

        }

        return output;
    }

    public ArrayList selectNombreComedores() {
        ArrayList output = new ArrayList();
        try {
            statement = (Statement) this.connection.createStatement();
            final ResultSet results = statement.executeQuery("select nombreComedor from comedor where EstatusEnSistema='Activo';");

            while (results.next()) {
                String tmpString = results.getString(1);

                output.add(tmpString);

            }

        } catch (final SQLException ex) {
            util.writeErrorToLog(ex, "DBHelper_SelectNombreComedores");

        }

        return output;
    }

    public TipoComida selectTipoComidaByNombre(String Nombre) {
        TipoComida output = null;
        try {
            statement = (Statement) this.connection.createStatement();
            final ResultSet results = statement.executeQuery("Select * from tipoComida where nombreTipoComida='" + Nombre + "' and estatusEnSistema='Activo';");

            while (results.next()) {
                String id = results.getString(1);
                String nombre = results.getString(2);
                double precio = results.getDouble(3);
                String estatus = results.getString(4);

                output = new TipoComida(
                        id,
                        nombre,
                        precio,
                        estatus
                );

            }

        } catch (final SQLException ex) {
            util.writeErrorToLog(ex, "DBHelper_SelectNameFromComensales");

        }

        return output;
    }

    public TipoComensal selectTipoComensalByNombre(String Nombre) {
        TipoComensal output = null;
        try {
            statement = (Statement) this.connection.createStatement();
            final ResultSet results = statement.executeQuery("Select * from TipoComensal where nombreTipoComensal='" + Nombre + "' and estatusEnSistema='Activo';");

            while (results.next()) {
                String id = results.getString(1);
                String nombre = results.getString(2);
                String idComidaAutorizada = results.getString(3);
                int maxComidas = results.getInt(4);
                String estatus = results.getString(5);

                output = new TipoComensal(
                        id,
                        nombre,
                        idComidaAutorizada,
                        maxComidas,
                        estatus
                );

            }

        } catch (final SQLException ex) {
            util.writeErrorToLog(ex, "DBHelper_SelectTipoComensalByName");

        }

        return output;
    }

    public TipoUsuario selectTipoUsuarioByNombre(String Nombre) {
        TipoUsuario output = null;
        try {
            statement = (Statement) this.connection.createStatement();
            final ResultSet results = statement.executeQuery("Select * from TipoUsuario where nombreTipoUsuario='" + Nombre + "' and estatusEnSistema='Activo';");

            while (results.next()) {
                String id = results.getString(1);
                String nombre = results.getString(2);
                String estatus = results.getString(3);

                output = new TipoUsuario(
                        id,
                        nombre,
                        estatus
                );

            }

        } catch (final SQLException ex) {
            util.writeErrorToLog(ex, "DBHelper_SelectTipoUsuarioByName");

        }

        return output;
    }

    public ArrayList selectNombresTipos(String tabla, String nombreCampo) {
        ArrayList output = new ArrayList();
        try {
            statement = (Statement) this.connection.createStatement();
            final ResultSet results = statement.executeQuery("Select " + nombreCampo + " from " + tabla + " where estatusEnSistema='Activo'; ");

            while (results.next()) {
                String nombre = results.getString(1);
                output.add(nombre);

            }

        } catch (final SQLException ex) {
            util.writeErrorToLog(ex, "DBHelper_SelectNombresTipos");

        }

        return output;

    }

    public ArrayList selectNombresComensal() {
        ArrayList output = new ArrayList();
        output = this.selectNombresTipos("Comensal", "nombreComensal");
        return output;
    }

    public ArrayList selectNombresTipoComensal() {
        ArrayList output = new ArrayList();
        output = this.selectNombresTipos("TipoComensal", "nombreTipoComensal");
        return output;
    }

    public ArrayList selectNombresTipoComida() {
        ArrayList output = new ArrayList();
        output = this.selectNombresTipos("TipoComida", "nombreTipoComida");
        return output;
    }

    public ArrayList selectNombresTipoUsuario() {
        ArrayList output = new ArrayList();
        output = this.selectNombresTipos("TipoUsuario", "nombreTipoUsuario");
        return output;
    }

    public Comedor selectComedorByNombre(String Nombre) {
        Comedor output = null;
        try {
            statement = (Statement) this.connection.createStatement();
            final ResultSet results = statement.executeQuery("select * from comedor where nombreComedor = '" + Nombre + "' and estatusEnSistema='Activo'");

            while (results.next()) {
                String id = results.getString(1);
                String nombre = results.getString(2);
                String estatus = results.getString(3);

                output = new Comedor(
                        id,
                        nombre,
                        estatus
                );

            }

        } catch (final SQLException ex) {
            util.writeErrorToLog(ex, "DBHelper_SelectComedorByNombre");

        }

        return output;
    }

    public ArrayList selectAllComensales() {
        ArrayList output = new ArrayList();
        try {
            statement = (Statement) this.connection.createStatement();
            final ResultSet results = statement.executeQuery("select * from comensal where estatusEnSistema ='Activo'");

            while (results.next()) {
                String id = results.getString(1);
                String numeroEmpleado = results.getString(2);
                String nombre = results.getString(3);
                String idTipoComensal = results.getString(4);
                String estatusComensal = results.getString(5);
                Comensal comensal = new Comensal(id, numeroEmpleado, nombre, idTipoComensal, estatusComensal);
                output.add(comensal);

            }

        } catch (final SQLException ex) {
            util.writeErrorToLog(ex, "DBHelper_SelectAllComensales");

        }
        return output;
    }

    public ArrayList selectComensalesFromComidasPorDia(String fecha) {
        ArrayList data;
        data = this.selectComensalesFromComidasPorDia("ComidasPorDia", fecha, "fecha");
        return data;
    }

    public ArrayList selectComensalesFromComidasPorDia(String table, String param, String field) {
        final ArrayList tmp = new ArrayList();

        try {
            statement = (Statement) this.connection.createStatement();
            final ResultSet results = statement.executeQuery("select * from " + table + " where " + field + " = '" + param + "';");
            while (results.next()) {
                String tmpString = results.getString(2);
                tmp.add(tmpString);

            }

        } catch (final SQLException ex) {
            util.writeErrorToLog(ex, "DBHelper_SelectIDsFromWhere->" + table);

        }

        return tmp;
    }

    public int comidasPermitidas(String id) {
        int output = 0;
        try {
            statement = (Statement) this.connection.createStatement();
            final ResultSet results = statement.executeQuery("select comidasPermitidas from TipoComensal where idTipoComensal='" + id + "'and estatusEnSistema='Activo';");
            while (results.next()) {
                output = results.getInt(1);

            }

        } catch (final SQLException ex) {
            util.writeErrorToLog(ex, "DBHelper_comidasPermitidas");

        }

        return output;
    }

    public boolean isInComidasPorDia(String id, String fecha) {
        boolean output = false;

        ArrayList data = this.selectComensalesFromComidasPorDia(fecha);
        Comensal comensal = this.selectComensalByID(id);
        int maximoComidas = this.comidasPermitidas(comensal.getIdTipoComensal());
        int contadorComidas = 0;

        for (int i = 0; i < data.size(); i++) {
            String tmp = data.get(i).toString();

            if (tmp.equals(id)) {
                contadorComidas++;
            }
        }

        if (contadorComidas < maximoComidas) {
            output = false;
        } else {
            output = true;
        }
        return output;
    }

}
