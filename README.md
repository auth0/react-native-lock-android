# react-native-lock-android

[![License](http://img.shields.io/:license-mit-blue.svg?style=flat)](http://doge.mit-license.org)
[![NPM version][npm-image]][npm-url]
[![CI Status][travis-image-android]][travis-url-android]

[Auth0](https://auth0.com) is an authentication broker that supports social identity providers as well as enterprise identity providers such as Active Directory, LDAP, Google Apps and Salesforce.

**react-native-lock-android** is a wrapper around [Lock.Android](https://github.com/auth0/Lock.Android) so it can be used from an Android React Native application


## Requirements

* React Native
* Android API 16+ 


## Installation

Run `npm install --save react-native-lock-android` to add the package to your app's dependencies.

You need to include the `com.auth0.android:lock-react-native` library in your android project.
Add the dependency in the `build.gradle` file of the android project:

```gradle
compile 'com.auth0.android:lock-react-native'
```

You must also add the `LockReactPackage` to the `ReactInstanceManager` in the `onCreate` method of the `MainActivity`.

```java
public class MainActivity extends Activity implements DefaultHardwareBackBtnHandler {
    ...
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ...
        LockReactPackage lockReactPackage = new LockReactPackage();

        /* If you would like to add native integrations, add them like this */
        lockReactPackage.addIdentityProvider(Strategies.Facebook, new FacebookIdentityProvider(this));
        lockReactPackage.addIdentityProvider(Strategies.GooglePlus, new GooglePlusIdentityProvider(this));
        
        mReactInstanceManager = ReactInstanceManager.builder()
                /* ... */
                .addPackage(lockReactPackage)
                /* ... */
                .build();
        ...
    }
    ...
}
```

Finally you should see the Lock.Android [docs](https://github.com/auth0/Lock.Android) to find out how to add the Lock activities to your `AndroidManifest.xml` file.


## Usage

Let's require `react-native-lock-android` module:

```js
var Auth0Lock = require('react-native-lock-android');
```

And initialize it with your Auth0 credentials that you can get from [our dashboard](https://app.auth0.com/#/applications)

```js
var lock = new Auth0Lock({clientId: "YOUR_CLIENT_ID", domain: "YOUR_DOMAIN"});
```

### Email/Password, Enterprise & Social authentication

```js
lock.show({}, (err, profile, token) => {
  console.log('Logged in!');
});
```

And you'll see our native login screen

[![Lock.png](http://blog.auth0.com.s3.amazonaws.com/Lock-Widget-Android-Screenshot.png)](https://auth0.com)


### SMS Passwordless

```js
lock.show({
  connections: ["sms"]
}, (err, profile, token) => {
  console.log('Logged in!');
});
```
And you'll see SMS Passwordless login screen

[![Lock.png](http://blog.auth0.com.s3.amazonaws.com/Lock-SMS-Android-Screenshot.png)](https://auth0.com)


### Email Passwordless

```js
lock.show({
  connections: ["email"]
}, (err, profile, token) => {
  console.log('Logged in!');
});
```
And you'll see Email Passwordless login screen

[![Lock.png](https://cdn.auth0.com/mobile-sdk-lock/lock-ios-pwdless-email.png)](https://auth0.com)


## API

### Lock

####.show(options, callback)
Show Lock's authentication screen as a modal screen using the connections configured for your applications or the ones specified in the `options` parameter. This is the list of valid options:

* **closable** (`boolean`): If Lock screen can be dismissed. Default is `false`.
* **connections** (`[string]`): List of enabled connections to use for authentication. Must be enabled in your app's dashboard first. If you leave it empty, Lock will use all the enabled connections.
* **useMagicLink** (`boolean`): When using a passwordless connection, activate this option to send a Magic/App link instead of the code. Default is `false`.
* **authParams** (`object`): Object with the parameters to be sent to the Authentication API, e.g. `scope`.

The callback will have the error if anything went wrong or after a successful authentication, it will yield the user's profile info and tokens.

## Issue Reporting

If you have found a bug or if you have a feature request, please report them at this repository issues section. Please do not report security vulnerabilities on the public GitHub issue tracker. The [Responsible Disclosure Program](https://auth0.com/whitehat) details the procedure for disclosing security issues.

## What is Auth0?

Auth0 helps you to:

* Add authentication with [multiple authentication sources](https://docs.auth0.com/identityproviders), either social like **Google, Facebook, Microsoft Account, LinkedIn, GitHub, Twitter, Box, Salesforce, amont others**, or enterprise identity systems like **Windows Azure AD, Google Apps, Active Directory, ADFS or any SAML Identity Provider**.
* Add authentication through more traditional **[username/password databases](https://docs.auth0.com/mysql-connection-tutorial)**.
* Add support for **[linking different user accounts](https://docs.auth0.com/link-accounts)** with the same user.
* Support for generating signed [Json Web Tokens](https://docs.auth0.com/jwt) to call your APIs and **flow the user identity** securely.
* Analytics of how, when and where users are logging in.
* Pull data from other sources and add it to the user profile, through [JavaScript rules](https://docs.auth0.com/rules).

## Create a free account in Auth0

1. Go to [Auth0](https://auth0.com) and click Sign Up.
2. Use Google, GitHub or Microsoft Account to login.

## Author

Auth0

## License

react-native-lock-android is available under the MIT license. See the [LICENSE file](LICENSE) for more info.

<!-- Variables -->
[npm-image]: https://img.shields.io/npm/v/react-native-lock-android.svg?style=flat
[npm-url]: https://npmjs.org/package/react-native-lock-android
[travis-image-android]: http://img.shields.io/travis/auth0/react-native-lock-android.svg?style=flat
[travis-url-android]: https://travis-ci.org/auth0/react-native-lock-android
