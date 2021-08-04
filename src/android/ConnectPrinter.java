
package cordova.plugin.regoprinter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.serialport.DeviceControl;

import android.widget.Toast;

import com.speedata.libutils.ConfigUtils;
import com.speedata.libutils.ReadBean;

import java.io.IOException;
import java.util.List;

import cordova.plugin.regoprinter.utils.ApplicationContext;

public class ConnectPrinter {

	public int state;
	public ApplicationContext context;
	public boolean mBconnect = false;

	private ReadBean mRead;
	private DeviceControl deviceControl;

	public ConnectPrinter(Context context) {
		this.context = (ApplicationContext) context;
		this.context.setObject(this.context);

		connect();
	}

	public void connect() {

		modelJudgmen();

		if (mBconnect) {
			context.getObject().CON_CloseDevices(context.getState());

			mBconnect = false;
		} else {

			System.out.println("----RG---CON_ConnectDevices");

			if (state > 0) {
				Toast.makeText(context, "Impresora conectada correctamente",
						Toast.LENGTH_SHORT).show();

				mBconnect = true;
				context.setState(state);
				context.setName("RG-E48");
			} else {
				Toast.makeText(context, "No se pudo conectar a la impresora",
						Toast.LENGTH_SHORT).show();
				mBconnect = false;
			}
		}
	}


	private void modelJudgmen() {
		boolean configFileExists = ConfigUtils.isConfigFileExists();
		mRead = ConfigUtils.readConfig(context);

		ReadBean.PrintBean print = mRead.getPrint();
		String powerType = print.getPowerType();
		int braut = print.getBraut();
		List<Integer> gpio = print.getGpio();
		String serialPort = print.getSerialPort();
		state = context.getObject().CON_ConnectDevices("RG-E487",
				serialPort + ":" + braut + ":1:1", 200);

		int[] intArray = new int[gpio.size()];
		String intArrayStr="";

		for (int i = 0; i < gpio.size(); i++) {
			intArray[i] = gpio.get(i);
			intArrayStr=intArrayStr+intArray[i]+" ";
		}

		try {
			deviceControl = new DeviceControl(powerType,intArray);
			deviceControl.PowerOnDevice();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void print(String data) {
		context.getObject().CON_PageStart(context.getState(), false,
                60, // width
                40  // height
        );

        {

            context.getObject().ASCII_CtrlAlignType(context.getState(),
                    0);

            context.getObject().ASCII_PrintString(context.getState(),
                    0, // width
                    0, // height
                    0, // bold
                    0, // underline
                    0, // small
                    data, "gb2312");

            context.getObject().ASCII_CtrlFeedLines(context.getState(),
                    1);
            context.getObject().ASCII_CtrlPrintCRLF(context.getState(),
                    1);


        }
        context.getObject().CON_PageEnd(context.getState(),
                context.getPrintway());
	}

}
