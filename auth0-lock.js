/**
 * @providesModule Auth0Lock
 */

'use strict';

/**
 * This exposes the native Auth0Lock module as a JS module. 
 */
var { NativeModules } = require('react-native');
var LockModule = NativeModules.Auth0LockModule;

class Auth0Lock {

  constructor(options) {
    if (options) {
      this.lockOptions = {
        clientId: options.clientId,
        domain: options.domain,
        configurationDomain: options.configurationDomain
      };
    } else {
      this.lockOptions = {};
    }
  }

  show(options, callback) {
    LockModule.init(this.lockOptions);
    LockModule.show(options, callback);
  }
}

module.exports = Auth0Lock;