# cordova-plugin-crop

> Crop an image in a Cordova app


## Install

```
$ cordova plugin add --save https://github.com/bidchatindia/Bidchat-Plugin-ImageCrop.git
```


## Usage

```js
plugins.crop(function success () {

}, function fail () {

}, '/path/to/image', options)
```

## API

 * quality: Number

The resulting JPEG quality. default: 100

 * imageHeight: Number

The Crop Rectangle Height.

### Example 

```js
function cropImage(imagePath) {
    
    var onSucess = function(message) {
        alert("Crop Success : " + message);
        setPicture(message);
    };
    
    var onFailure = function(message) {
        alert("Crop Failed with error" + message.code);
    };
    
    var options = { 
       imageHeight : 300
    };
    
    plugins.crop(onSucess, onFailure, imagePath, options);
}
```

### For Android-Cordova
In case you get this error "Error: Width (0) and height (0) cannot be <= 0" add the following to gradle


 android {  
   defaultConfig {  
     generatedDensities = []  
  }  

  // This is handled for you by the 2.0+ Gradle Plugin  
  aaptOptions {  
    additionalParameters "--no-version-vectors"  
  }  
 }

 https://github.com/Yalantis/uCrop/issues/84

### Libraries used

 * iOS: [PEPhotoCropEditor](https://github.com/kishikawakatsumi/PEPhotoCropEditor)
 * Android: [uCrop](https://github.com/Yalantis/uCrop)

## License

MIT © [Bidchat](https://github.com/bidchatindia)
