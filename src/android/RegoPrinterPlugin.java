package cordova.plugin.regoprinter;

import android.content.Context;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cordova.plugin.regoprinter.ConnectPrinter;

/**
 * This class echoes a string called from JavaScript.
 */
public class RegoPrinterPlugin extends CordovaPlugin {

    ConnectPrinter printer;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("connect")) {
            this.connect();
        } else if (action.equals("print")) {
            String message = args.getString(0);
            this.print(message, callbackContext);
            return true;
        }
        return false;
    }

    private void connect() {
        Context context = this.cordova.getActivity().getApplicationContext();
        printer = new ConnectPrinter(context);
    }

    private void print(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            System.out.println("==== MESSAGE: " + message);

            printer.print(message);
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }
}
