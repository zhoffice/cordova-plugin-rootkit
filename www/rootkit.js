var exec = require('cordova/exec');

exports.changeOomAdj = function (adjNum, success, error) {
  exec(success, error, "rootkit", "changeOomAdj", [adjNum]);
};

exports.lockOomAdj = function (adjNum, interval, success, error) {
  exec(success, error, "rootkit", "lockOomAdj", [adjNum, interval]);
}
