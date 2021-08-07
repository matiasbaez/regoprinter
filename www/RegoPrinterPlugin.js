var exec = require('cordova/exec');

exports.connect = function (arg0, success, error) {
    exec(success, error, 'RegoPrinterPlugin', 'connect');
};

exports.print = function (arg0, success, error) {
    exec(success, error, 'RegoPrinterPlugin', 'print', [arg0]);
};
