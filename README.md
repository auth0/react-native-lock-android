# react-native-lock-android

[![License][license-image]][license-url]
[![NPM version][npm-image]][npm-url]
[![CI Status][travis-image]][travis-url]

[Auth0](https://auth0.com) is an authentication broker that supports social identity providers as well as enterprise identity providers such as Active Directory, LDAP, Google Apps and Salesforce.

**react-native-lock-android** is a wrapper around [Lock.Android](https://github.com/auth0/Lock.Android) so it can be used from an Android React Native application


## Requirements

* React Native 0.16
* Android API 16+ 


## Installation

Run `npm install --save react-native-lock-android` to add the package to your app's dependencies.

Then include the `com.auth0.android:lock-react` library in your android project.
Add the dependency in the `build.gradle` file of the android project:

```gradle
compile 'com.auth0.android:lock-react:+'
```

You must also add the `LockReactPackage` to the `ReactInstanceManager` in the `onCreate` method of the `MainActivity`.

```java
public class MainActivity extends Activity implements DefaultHardwareBackBtnHandler {
    ...
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ...        
        mReactInstanceManager = ReactInstanceManager.builder()
                /* ... */
                .addPackage(new LockReactPackage())
                /* ... */
                .build();
        ...
    }
    ...
}
```

Finally you need to declare the Lock activities in your `AndroidManifest.xml` file.

```xml
<!--Auth0 Lock-->
<activity
  android:name="com.auth0.lock.LockActivity"
  android:theme="@style/Lock.Theme"
  android:screenOrientation="portrait"
  android:launchMode="singleTask">
</activity>
<!--Auth0 Lock End-->
<!--Auth0 Lock Embedded WebView-->
<activity 
    android:name="com.auth0.identity.web.WebViewActivity" 
    android:theme="@style/Lock.Theme">
</activity>
<!--Auth0 Lock Embedded WebView End-->
<!--Auth0 Lock Passwordless-->
<activity
    android:name="com.auth0.lock.passwordless.LockPasswordlessActivity"
    android:theme="@style/Lock.Theme"
    android:screenOrientation="portrait"
    android:launchMode="singleTask">
</activity>
<activity 
    android:name="com.auth0.lock.passwordless.CountryCodeActivity" 
    android:theme="@style/Lock.Theme">
</activity>
<!--Auth0 Lock Passwordless End-->
```

> For more information and configuration options you should see the Lock.Android [docs](https://github.com/auth0/Lock.Android)

### Native integrations

If you need Facebook or Google+ native authentication please continue reading to learn how to configure them. Otherwise please go directly to [usage](#usage)

To allow native logins using other Android apps, e.g: Google+, Facebook, etc, you need to explicitly add them by calling `addIdentityProvider` in your `LockReactPackage` instance before adding it to the `ReactInstanceManager`.

```java
public class MainActivity extends Activity implements DefaultHardwareBackBtnHandler {
    ...
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ...
        LockReactPackage lockReactPackage = new LockReactPackage();

        /* If you would like to add native integrations, add them here */
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

Each native integration requires it's own configuration. If you added some of them please follow the corresponding instructions.


#### Facebook

Lock uses the native Facebook SDK to obtain the user's access token so you'll need to configure it using your Facebook App info. If you don't have one, please create a Facebook Application in [Facebook Dev Site](https://developers.facebook.com/apps). Remember to register the package name and hash of your android application. 

Once you have your Facebook App, you need to enable and configure the connection in Facebook's Connection settings on your Auth0 account. You need to set up your FB app id and secret.

Then add Lock Facebook's dependency to your `build.gradle`

```gradle
compile 'com.auth0.android:lock-facebook:2.3.+'
```

Finally in your project's `AndroidManifest.xml` add the following entries inside the `<application>` tag:

```xml
<activity android:name="com.facebook.FacebookActivity"
          android:configChanges="keyboard|keyboardHidden|screenLayout|screenSize|orientation"
          android:theme="@android:style/Theme.Translucent.NoTitleBar"
          android:label="@string/app_name" />
<meta-data android:name="com.facebook.sdk.ApplicationId" android:value="@string/facebook_app_id"/>
```

The value `@string/facebook_app_id` is your Facebook Application ID that you can get from [Facebook Dev Site](https://developers.facebook.com/apps) after you create your Facebook Application. You could just add this value to your `strings.xml` like this:

```xml
<string name="facebook_app_id">YOUR_FB_APP_ID_GOES_HERE</string>
```

> For more information on how to configure this, please check [Facebook Getting Started Guide](https://developers.facebook.com/docs/android/getting-started).

> For more information and configuration options you should see the Lock-Facebook.Android [docs](https://github.com/auth0/Lock-Facebook.Android)

#### Google+

First you'll need to register your application in Google+, to do it follow the instructions in Step 1 of this [guide](https://developers.google.com/+/mobile/android/getting-started). 

You also need to enable the connection in your Auth0 dashboard.

Then add Lock GooglePlus' dependency to your `build.gradle`

```gradle
compile 'com.auth0.android:lock-googleplus:2.3.+'
```

Finally in your project's `AndroidManifest.xml` add the following entries:

```xml
<uses-permission android:name="android.permission.GET_ACCOUNTS" />
<uses-permission android:name="android.permission.USE_CREDENTIALS" />
<meta-data android:name="com.google.android.gms.version" android:value="@integer/google_play_services_version" />
```

> For more information and configuration options you should see the Lock-GooglePlus.Android [docs](https://github.com/auth0/Lock-GooglePlus.Android)

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

[![Lock.png](https://cdn.auth0.com/mobile-sdk-lock/lock-android-default.png)](https://auth0.com)


### SMS Passwordless

> In order to be able to authenticate the user, your Auth0 account must have the SMS connection enabled and configured in your [dashboard](https://manage.auth0.com/#/connections/passwordless)

```js
lock.show({
  connections: ["sms"]
}, (err, profile, token) => {
  console.log('Logged in!');
});
```
And you'll see SMS Passwordless login screen

[![Lock.png](https://cdn.auth0.com/mobile-sdk-lock/lock-android-pwdless-sms.png)](https://auth0.com)

### Email Passwordless

> In order to be able to authenticate the user, your Auth0 account must have the Email connection enabled and configured in your [dashboard](https://manage.auth0.com/#/connections/passwordless)

```js
lock.show({
  connections: ["email"]
}, (err, profile, token) => {
  console.log('Logged in!');
});
```
And you'll see Email Passwordless login screen

[![Lock.png](https://cdn.auth0.com/mobile-sdk-lock/lock-android-pwdless-email.png)](https://auth0.com)

## API

### Lock

####.show(options, callback)
Show Lock's authentication screen as a modal screen using the connections configured for your applications or the ones specified in the `options` parameter. This is the list of valid options:

* **closable** (`boolean`): If Lock screen can be dismissed. Default is `false`.
* **connections** (`[string]`): List of enabled connections to use for authentication. Must be enabled in your app's dashboard first. If you leave it empty, Lock will use all the enabled connections.
* **useMagicLink** (`boolean`): When using a passwordless connection, activate this option to send a Magic/App link instead of the code. Default is `false`.
* **authParams** (`object`): Object with the parameters to be sent to the Authentication API, e.g. `scope`.

The callback will have the error if anything went wrong or after a successful authentication, it will yield the user's profile info and tokens.

## FAQ

### Error: duplicate files during packaging of APK

If you observe an error like this when trying to run the project:

```
Error: duplicate files during packaging of APK /<PATH>/android/app/build/outputs/apk/app-debug-unaligned.apk
  Path in archive: META-INF/NOTICE
  Origin 1: /<PATH>/.gradle/caches/modules-2/files-2.1/com.fasterxml.jackson.core/jackson-databind/2.4.1/f07c773f7b3a03c3801d405cadbdc93f7548e321/jackson-databind-2.4.1.jar
  Origin 2: /<PATH>/.gradle/caches/modules-2/files-2.1/com.fasterxml.jackson.core/jackson-core/2.4.1/b130bcfb5a9c410c3cbd2e0adec9437e69a39e2c/jackson-core-2.4.1.jar
You can ignore those files in your build.gradle:
  android {
    packagingOptions {
      exclude 'META-INF/NOTICE'
    }
  }
:app:packageDebug FAILED
```

You must follow the advice and ignore the files adding the following to the `build.gradle` of the `app` module, inside the `android` section:

```
packagingOptions {
    exclude 'META-INF/LICENSE'
    exclude 'META-INF/NOTICE'
}
```

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
[license-image]: http://img.shields.io/:license-mit-blue.svg?style=flat
[license-url]: http://doge.mit-license.org
[npm-image]: https://img.shields.io/npm/v/react-native-lock-android.svg?style=flat
[npm-url]: https://npmjs.org/package/react-native-lock-android
[travis-image]: http://img.shields.io/travis/auth0/react-native-lock-android.svg?style=flat
[travis-url]: https://travis-ci.org/auth0/react-native-lock-android
