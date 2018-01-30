/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helpers;

import DataLayer.Comedor;
import DataLayer.Comensal;
import DataLayer.ComidasPorDia;
import DataLayer.TipoComensal;
import DataLayer.TipoComida;
import DataLayer.Usuario;
import PresentationLayer.AprobarExcepcion;
import PresentationLayer.panels.PanelImprimirComensal;
import PresentationLayer.panels.PanelImprimirMultiplesComensales;
import PresentationLayer.panels.PanelChecar;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.TextArea;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

/**
 *
 * @author kurtyanez
 */
public class GUIHelper {

    Usuario user = null;
    Comensal comensal = null;
    TipoComida comida = null;
    Comedor comedor = null;

    public Usuario getUsuarioGlobal() {
        return usuarioGlobal;
    }
    Usuario usuarioGlobal = null;

    public void fillTablesCombo(JComboBox combo) {
        DataBaseHelper ldb = new DataBaseHelper();
        ldb.createConnection();
        this.fillComboBox(combo, ldb.ShowTables());
    }

    public void fillComboBox(JComboBox combo, ArrayList data) {
        combo.removeAllItems();
        for (int i = 0; i < data.size(); i++) {
            combo.addItem(data.get(i));
        }

    }

    public void fillComboEstatus(JComboBox combo) {
        combo.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[]{"Activo", "Inactivo"}));

    }

    public void fillComboComedores(JComboBox combo) {
        DataBaseHelper dbh = new DataBaseHelper();
        dbh.createConnection();
        this.fillComboBox(combo, dbh.selectNombreComedores());

    }

    public void fillComboFechasFrom(JComboBox combo, String tabla) {
        DataBaseHelper dbh = new DataBaseHelper();
        dbh.createConnection();
        this.fillComboBox(combo, dbh.selectFechaFrom(tabla));

    }

    public void fillComboComensales(JComboBox combo, int modificador) {
        DataBaseHelper dbh = new DataBaseHelper();
        dbh.createConnection();
        ArrayList data = dbh.selectNombresComensal();
        combo.removeAllItems();
        if (modificador == 1) {
            combo.addItem("-Todos Los Comensales-");
        }

        for (int i = 0; i < data.size(); i++) {
            combo.addItem(data.get(i));
        }

    }

    public void fillLista(JList jList, ArrayList data) {
        DefaultListModel<String> dlm = new DefaultListModel<String>();

        for (int i = 0; i < data.size(); i++) {
            dlm.addElement(data.get(i).toString());

        }
        jList.setModel(dlm);

    }

    public void clearLista(JList jList) {
        DefaultListModel<String> dlm = new DefaultListModel<String>();
        jList.setModel(dlm);
    }

    public void fillListaComensales(JList jList) {
        DataBaseHelper dbh = new DataBaseHelper();
        dbh.createConnection();
        this.fillLista(jList, dbh.selectNombresComensal());

    }

    public void fillComboUsuarios(JComboBox combo) {
        DataBaseHelper dbh = new DataBaseHelper();
        dbh.createConnection();
        this.fillComboBox(combo, dbh.selectAllUserNames());

    }

    public void fillComboTipoUsuarios(JComboBox combo) {
        DataBaseHelper dbh = new DataBaseHelper();
        dbh.createConnection();
        this.fillComboBox(combo, dbh.selectNombresTipoUsuario());

    }

    public void fillComboTipoComidas(JComboBox combo) {
        DataBaseHelper dbh = new DataBaseHelper();
        dbh.createConnection();
        this.fillComboBox(combo, dbh.selectNombresTipoComida());

    }

    public void fillComboTipoComensal(JComboBox combo) {
        DataBaseHelper dbh = new DataBaseHelper();
        dbh.createConnection();
        this.fillComboBox(combo, dbh.selectNombresTipoComensal());

    }

    public boolean validarUsuario(String userName, char[] pass) {
        DataBaseHelper dbh = new DataBaseHelper();
        Util util = new Util();
        dbh.createConnection();
        ArrayList userList = dbh.selectUserByUserName(userName);
        Usuario usr = (Usuario) userList.get(0);

        String password = "";
        try {
            String storedPass = util.decryptString(usr.getPassword());

            for (int i = 0; i < pass.length; i++) {
                password += Character.toString(pass[i]);
            }

            if (storedPass.equals(password)) {
                this.usuarioGlobal = usr;

                return true;
            }
        } catch (Exception ex) {

            util.writeErrorToLog(ex, "ValidarUsuario");

        }

        return false;
    }

    public String getUserLevel() {
        String output = "";

        if (this.getUsuarioGlobal() != null) {
            DataBaseHelper dbh = new DataBaseHelper();
            dbh.createConnection();
            output += dbh.getUserRol(this.getUsuarioGlobal().getIdTipoUsuario().toString());

        }

        return output;
    }

    public void mostrarComidasServidas(String fecha, JLabel numeroComidas) {
        DataBaseHelper dbh = new DataBaseHelper();
        dbh.createConnection();
        int comidasServidas = dbh.selectComensalesFromComidasPorDia(fecha).size();
        numeroComidas.setText("Comidas Servidas hoy: " + comidasServidas);

    }

    public boolean checarComensal(String Fecha, JLabel jLabelStatus, JTextField Nombre, JTextField cBarras, JComboBox comboComedor, JComboBox comboComida) {

        String codigoBarras = cBarras.getText().trim();
        DataBaseHelper dbh = new DataBaseHelper();
        dbh.createConnection();
        String nombreComensal = dbh.selectNameFromComensales(codigoBarras);
        String nombreComedor = comboComedor.getSelectedItem().toString();
        String nombreTipoComida = comboComida.getSelectedItem().toString();

        if (nombreComensal.equals("")) {
            Nombre.setText("Comensal no encontrado en la base de datos");
            return false;
        } else {
            Nombre.setText(nombreComensal);
            boolean duplicado = dbh.isInComidasPorDia(codigoBarras, Fecha);

            if (!duplicado) {
                //validar que el nùmero de comidas sea el permitido.
                jLabelStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ok_mid.png")));
                cBarras.setText("");
////
                this.guardarComidasPorDia(nombreComedor, codigoBarras, nombreTipoComida);
////
                return true;
            } else {

                jLabelStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/cancel_mid.png"))); // NOI18N
                cBarras.setText("");
                return false;
            }
        }

    }

    public void guardarComidasPorDia(String nombreComedor, String codigoBarras, String nombreTipoComida) {
        DataBaseHelper dbh = new DataBaseHelper();
        dbh.createConnection();
        Comedor comedor = dbh.selectComedorByNombre(nombreComedor);
        Comensal comensal = dbh.selectComensalByID(codigoBarras);
        TipoComida tipoComida = dbh.selectTipoComidaByNombre(nombreTipoComida);

        if ((comedor != null) && (comensal != null) && (tipoComida != null)) {
            ComidasPorDia comidasPorDia = new ComidasPorDia(
                    comensal.getIdComensal(),
                    comensal.getIdTipoComensal(),
                    comedor.getIdComedor(),
                    tipoComida.getIdTipoComida(),
                    "Activo"
            );

            dbh.insertADataLayerObject(comidasPorDia, "ComidasPorDia");

        }

    }

    public JScrollPane mostrarUsuarios(JTextField jTextFieldUser, JPasswordField jPasswordField1) {

        DataBaseHelper dbh = new DataBaseHelper();
        dbh.createConnection();
        ArrayList usuarios = dbh.selectAllUserNames();
        JPanel me = new JPanel();
        me.setLayout(new GridLayout(0, 1));
        //   BoxLayout boxLayout = new BoxLayout(me, BoxLayout.Y_AXIS); // top to bottom
        //   me.setLayout(boxLayout);
        final JButton buttons[] = new JButton[usuarios.size()];
        for (int i = 0; i < buttons.length; i++) {

            buttons[i] = new JButton();
        }

        for (int i = 0; i < buttons.length; i++) {
            final int j = i;
            buttons[i].setText(usuarios.get(i).toString());
            buttons[i].setIcon(new ImageIcon(getClass().getResource("/Icons/user_micro.png")));
            buttons[i].setSize(211, 50);
            buttons[i].addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    String nombre = buttons[j].getText();
                    jTextFieldUser.setText(nombre);
                    jPasswordField1.requestFocus();
                }
            });
        }

        for (int i = 0; i < buttons.length; i++) {
            me.add(buttons[i]);

        }

        JScrollPane jScrollPaneBtns = new JScrollPane(me);
        jScrollPaneBtns.setBounds(new Rectangle(1, 1, 220, 360));  // Generated
        jScrollPaneBtns.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        jScrollPaneBtns.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        return jScrollPaneBtns;
    }

    public void crearAviso(PanelChecar panel) {
        this.generarObjetosParaExcepcion(panel);
        JDialog.setDefaultLookAndFeelDecorated(true);

        Object[] options = {"Si, Continuar",
            "No, Cancelar"};
        int response = JOptionPane.showOptionDialog(panel,//parent container of JOptionPane
                "Está a punto de aprobar una excepción",
                "Favor de confirmar",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,//do not use a custom Icon
                options,//the titles of buttons
                options[0]);//default button title

        switch (response) {
            case JOptionPane.NO_OPTION:

                break;
            case JOptionPane.YES_OPTION:
                //Abrir panel para autorizar
                java.awt.Frame parent = new java.awt.Frame();
                AprobarExcepcion aprobar = new AprobarExcepcion(parent, false);

                //generar objetos para excepcion
                aprobar.setValores(user, comensal, comida, comedor, panel);
                //aprobar set valores
                aprobar.setVisible(true);
                break;
            case JOptionPane.CLOSED_OPTION:

                break;
            default:
                break;
        }
    }

    public void generarObjetosParaExcepcion(PanelChecar panel) {

        DataBaseHelper dbh = new DataBaseHelper();
        dbh.createConnection();

        this.user = (Usuario) dbh.selectUserByUserName(panel.getNombreUsuario()).get(0);
        this.comedor = dbh.selectComedorByNombre(panel.getNombreComedor());
        this.comensal = dbh.selectComensalByNombre(panel.getNombreComensal());
        this.comida = dbh.selectTipoComidaByNombre(panel.getNombreComida());

    }

    public static void infoBox(String infoMessage, String titleBar) {
        JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
    }

    public void generarCredencial(PanelImprimirComensal panel, String nombreComensal) {
        DataBaseHelper dbh = new DataBaseHelper();
        dbh.createConnection();
        String nombre = nombreComensal;
        Util util = new Util();

        Comensal comensal = dbh.selectComensalByNombre(nombre);

        if (comensal != null) {
            comensal.createBarCode();

            String comedor = util.readFromConfigFile("currentComedor");

            BadgeHelper bh = new BadgeHelper(
                    "PROCOMIN",
                    comedor,
                    comensal.getNombreComensal(),
                    comensal.getNumeroEmpleado()
            );

            String archivoCodigoBarras = "src/Barcodes/" + comensal.getIdComensal() + ".png";
            String archivoLogo = util.readFromConfigFile("currentLogo");
            String archivoSalida = "src/Credenciales/credencial_" + comensal.getIdComensal();
            bh.crearCredencial(archivoCodigoBarras, archivoLogo, archivoSalida);
            String urlCredencial = archivoSalida + ".jpg";
            //Mostrar imagen en el Panel
            try {

                BufferedImage myPicture = ImageIO.read(new File(urlCredencial));
                panel.getjLabelOutput().setIcon(new ImageIcon(myPicture));

                PrinterJob pj = PrinterJob.getPrinterJob();
//...
                /* if (pj.printDialog()) {
                    pj.print();

                }*/
            } catch (IOException ex) {

                util.writeErrorToLog(ex, "GUIHelper_generarCredencial");
            }
            /*catch (PrinterException ex) {

                util.writeErrorToLog(ex, "generarCredencial");
            }*/

        }
    }

    public void generarMultiplesCredenciales(PanelImprimirMultiplesComensales panel, ArrayList nombreComensal) {
        DataBaseHelper dbh = new DataBaseHelper();
        dbh.createConnection();

////////////////////////////////////////////////////////////////////////////////
        for (int i = 0; i < nombreComensal.size(); i++) {

            Comensal comensal = dbh.selectComensalByNombre(nombreComensal.get(i).toString());

            if (comensal != null) {
                comensal.createBarCode();
                Util util = new Util();
                String comedor = util.readFromConfigFile("currentComedor");

                BadgeHelper bh = new BadgeHelper(
                        "PROCOMIN",
                        comedor,
                        comensal.getNombreComensal(),
                        comensal.getNumeroEmpleado()
                );

                String archivoCodigoBarras = "src/Barcodes/" + comensal.getIdComensal() + ".png";
                String archivoLogo = util.readFromConfigFile("currentLogo");
                String archivoSalida = "src/Credenciales/credencial_" + comensal.getIdComensal();
                bh.crearCredencial(archivoCodigoBarras, archivoLogo, archivoSalida);

            }
        }
        panel.getjLabelOutput().setText("Credenciales Guardadas");
////////////////////////////////////////////////////////////////////////////////
    }

    public void importarComensales(JComboBox jComboBoxTipoComensales, JLabel jLabelArchivo, TextArea textAreaOutput) {
        Util util = new Util();
        String tipoComensal = jComboBoxTipoComensales.getSelectedItem().toString();
        DataBaseHelper dbh = new DataBaseHelper();
        dbh.createConnection();
        TipoComensal tc = dbh.selectTipoComensalByNombre(tipoComensal);

        try {
            ArrayList listaComensales = util.leerArchivoComensales(jLabelArchivo.getText().trim(), tc.getIdTipoComensal());
            textAreaOutput.setText("Registros a importar: " + listaComensales.size() + System.getProperty("line.separator"));
            for (int i = 0; i < listaComensales.size(); i++) {
                Comensal comensal = (Comensal) listaComensales.get(i);
                textAreaOutput.setText(textAreaOutput.getText() + comensal.toString() + "....Importado" + System.getProperty("line.separator"));
                dbh.insertADataLayerObject(comensal, "Comensal");

            }
        } catch (IOException ex) {
            ex.printStackTrace();
            util.writeErrorToLog(ex, "GUIHelper_ImportarComensales");
        }
    }

}
