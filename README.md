# Android IOE SDK

## 1. Install IOE SDK

#### Setup gradle maven:

* With Gradle v1.x - v5.x

  Open `build.grade` file and add maven line like below

``` groovy
allprojects {
    repositories {
        google()  
        mavenCentral()
        maven {
            url 'https://gitlab.com//api/v4/projects/40140113/packages/maven'
            allowInsecureProtocol true
            credentials(HttpHeaderCredentials) {
                name = "Private-Token"
                value = "{SDK_PRIVATE_KEY}"
            }
            authentication {
                header(HttpHeaderAuthentication)
            }
        }
    }
}

```

* With Gradle v6.x+

  Open `setting.gradle` file and add maven line like below

``` groovy
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        maven {
            url 'https://gitlab.com//api/v4/projects/40140113/packages/maven'
            allowInsecureProtocol true
            credentials(HttpHeaderCredentials) {
                name = "Private-Token"
                value = "{SDK_PRIVATE_KEY}"
            }
            authentication {
                header(HttpHeaderAuthentication)
            }
        }
    }
}
```

#### Open file app/build.grade then add sdk:

``` groovy 
dependencies {
...
    implementation 'ai.ftech:ioesdk:1.0.0'
}
```

#### Init IOE in file Application

``` java
override fun onCreate() {
        super.onCreate()
        ...
        FTechIOEManager.init(this)
    }
```

## 2. SDK Integration

#### Init record SDK

| Param     | Type   | Description    |
|-----------|--------|----------------|
| appId     | String | Application id |
| secretKey | String | IOE secret key |

+ After successful initial, the SDK returns a status resulting in the `onSuccess()` callback.
  Handling of successful initial here.
+ When initial fails, it will be processed at callback `onFail()`.

``` java
FTechIOEManager.initRecord(appId, secretKey, new IFTechIOECallback<Boolean>() {
            @Override
            public void onSuccess(Boolean info) {
                
            }

            @Override
            public void onFail(APIException error) {
                
            }

            @Override
            public void onCancel() {
                
            }
        });
```

#### Register recording callback

After registration, the SDK will return the corresponding status in the callback
|Status|Description|
|------|-----------|
|onStart|Called at start record|
|onRecording|Called while in process recording|
|onFail|Called when an error occurs in process recording|
|onComplete|Called when completed record process and return evaluate result|

``` java
FTechIOEManager.registerRecordingListener(new IFTechRecordingCallback() {
            @Override
            public void onStart() {

            }

            @Override
            public void onRecording() {

            }

            @Override
            public void onFail(@NonNull APIException error) {

            }

            @Override
            public void onComplete(@NonNull StopRecordIOEResponse result) {

            }
        });
```

## 3. SDK Feature

#### Start record

| Param          | Type           | Description                   |
|----------------|----------------|-------------------------------|
| referenceText  | String         | Pronunciation reference text  |
| languageAccent | LanguageAccent | Pronunciation language accent |
| extraData      | String         | Attach data (Optional)        |

+ Used to start record for IOE evaluate pronunciation
+ When successful start, the SDK starts entering the recording state, it will be called on `onStart()` in recording callback. Handling start record successfully here.
+ When start record fails, it will be processed at callback `onFail()` in recording callback.

``` java
FTechIOEManager.startRecord(referText, accent, extraData);
```

#### Stop record

+ Used to stop record and start evaluate pronunciation process
+ When successful evaluate, the SDK will be return evaluate result on `onComplete()` in recording callback. Handling stop record successfully here.
+ When stop record and evaluate fails, it will be processed at callback `onFail()` in recording callback.

``` java
FTechIOEManager.stopRecord();
```
