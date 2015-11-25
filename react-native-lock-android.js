/**
 * @providesModule Auth0Lock
 */

'use strict';

/**
 * This exposes the native Auth0Lock module as a JS module. 
 */
var { NativeModules } = require('react-native');
//module.exports = NativeModules.Auth0LockModule;

var LockModule = NativeModules.Auth0LockModule;

class Auth0Lock {
  constructor(options) {
    if (options) {
      this.lockOptions = {
        clientId: options.clientId,
        domain: options.domain,
        configurationDomain: options.configurationDomain
      };
      this.nativeIntegrations = options.integrations;
    } else {
      this.lockOptions = {};
    }
  }

  show(options, callback) {
    LockModule.init(this.lockOptions);
    if (this.nativeIntegrations) {
      LockModule.nativeIntegrations(this.nativeIntegrations);
    }
    LockModule.show(options, callback);
  }
}

module.exports = Auth0Lock;