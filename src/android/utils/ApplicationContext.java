package cordova.plugin.regoprinter.utils;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import rego.printlib.export.regoPrinter;
import cordova.plugin.regoprinter.utils.preDefiniation.TransferMode;

public class ApplicationContext extends Application {
    private regoPrinter printer;
    private int myState = 0;
    private String printName = "RG-MTP58B";
    private int alignTypetext;

    private TransferMode printmode = TransferMode.TM_NONE;
    private boolean labelmark = true;

    public regoPrinter getObject() {
        return printer;
    }

    public void setObject(Context context) {
        printer = new regoPrinter(context);
    }

    public void setPrintway(int printway) {

        switch (printway) {
            case 0:
                printmode = TransferMode.TM_NONE;
                break;
            case 1:
                printmode = TransferMode.TM_DT_V1;
                break;
            default:
                printmode = TransferMode.TM_DT_V2;
                break;
        }

    }

    public int getPrintway() {
        return printmode.getValue();
    }

    public String getName() {
        return printName;
    }

    public void setName(String name) {
        printName = name;
    }

    public void setState(int state) {
        myState = state;
    }

    public int getState() {
        return myState;
    }

    public void PrintNLine(int lines) {
        // TODO Auto-generated method stub
        printer.ASCII_PrintBuffer(myState, new byte[]{0x1B, 0x66, 1,
                (byte) lines}, 4);

    }

    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }
}
