/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helpers;

import java.io.File;

import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;
import net.sourceforge.barbecue.output.OutputException;

/**
 *
 * @author kurtyanez
 */
public class BarCodeHelper {

    Util util = new Util();

    public void crearCodigoBarrasComensal(String IDComensal) {
        try {
            Barcode barcode = BarcodeFactory.createCode128(IDComensal);
            barcode.setBarHeight(50);
            barcode.setBarWidth(2);
            File imageFile = new File("src/Barcodes/" + IDComensal + ".png");
            BarcodeImageHandler.savePNG(barcode, imageFile);

        } catch (BarcodeException | OutputException ex) {
            util.writeStringToLog(ex.toString(), "Error_BarCodeHelper");
        }
    }
    


}
