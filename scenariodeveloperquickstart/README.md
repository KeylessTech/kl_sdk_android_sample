# Keyless SDK - Scenario Developer Quickstart

Welcome to the developer quickstart

## Setup Dependencies
We distribute the Keyless SDK through cloudsmith packages. 

We already added for you in the `settings.gradle.kts` root project file the following:

```Kotlin
 maven {
 	setUrl("https://dl.cloudsmith.io/$cloudsmithTokenPartners/keyless/partners/maven/")
 }
```

Cloudsmith needs a "token" to authorize our SDK download from `maven`. You can read the `cloudsmithTokenPartners` environment variable from the a file in the target root folder. Create a `cloudsmith.properties` file next to the `cloudsmith.template.properties`.

> [!CAUTION]
> You should not commit the `cloudsmith.properties` file containing the token in your git repository. We added it in the .gitignore file for you.

```markdown
# cloudsmith.properties content

cloudsmithTokenPartners=YOUR_CLOUDSMITH_TOKEN
```


## Setup Keyless API keys

In order to avoid committing API keys we created a template file under `kl_sdk_android_sample/scenariodeveloperquickstart/keys.properties.template` copy the content of the template into a `kl_sdk_android_sample/scenariodeveloperquickstart/keys.properties` file and add it to the module `.gitignore`.

The template looks like
```markdown
API_KEY=enter_keyless_api_key_here
HOST=enter_keyless_host_here
```

Once set we can retrieve the Keyless Api Key and Keyless Host in the `ContentViewModel` from `BuildConfig` all the wiring is already there for you.

## Next steps
Setup of the project is done, you should be able to build the project and launch it on a device.
We advise to use a real device since the camera won't work on an emulator.

The app shows buttons to perform the main actions with the Keyless SDK:
- Setup: configures the SDK. Mandatory for the SDK to work as expected.
- Enroll: "register" the user biometric with Keyless privacy preserving technology.
- Authenticate: check a face in front of the device with the face initially registered during enrollment.
- DeEnroll: "delete" the user from Keyless.
- Reset: clears the SDK state. Start from scratch after reset is successful.
