var isPhotoSet = false;

var pictureFromCamera = document.createElement("img");
    pictureFromCamera.setAttribute("height", "300px");
    pictureFromCamera.setAttribute("width", "100%");      
    pictureFromCamera.setAttribute("id", "camera_image"); 

var app = {
    // Application Constructor
    initialize: function() {
        this.bindEvents();
    },
   
    bindEvents: function() {
        document.addEventListener('deviceready', this.onDeviceReady, false);
    },

    onDeviceReady: function() {
        document.getElementById('button_one').addEventListener('click', buttonOneClicked, false);
        document.getElementById('button_two').addEventListener('click', buttonTwoClicked, false);
    },
};

/**
 * 
 **/
function buttonOneClicked() {
    
    // alert("One Clicked");
    getCameraPicture();
}

/**
 * 
 **/
function buttonTwoClicked() {
    
    clearPicture();
    
//    if(isPhotoSet == false) {
//        setPicture();
//    }
//    else {
//        clearPicture();
//    }
}

function setPicture() {
    pictureFromCamera.setAttribute("src", "images/logo.png");
    document.getElementById("camera_pic").appendChild(pictureFromCamera);
    isPhotoSet = true;
}

function setPicture(src) {
    pictureFromCamera.setAttribute("src", src);
    document.getElementById("camera_pic").appendChild(pictureFromCamera);
    isPhotoSet = true;
}

function clearPicture() {      
    var imageNode = document.getElementById("camera_image")
    imageNode.parentNode.removeChild(imageNode);
    isPhotoSet = false;
}

/**
 * Default Implemenetion of Capture Picture from Camera Gallery
 */      
function getCameraPicture() {
    
    var cameraSourceType = navigator.camera.PictureSourceType;
    var cameraSourceTypeSaved = cameraSourceType.SAVEDPHOTOALBUM; // {PHOTOLIBRARY: 0, CAMERA: 1, SAVEDPHOTOALBUM: 2} = $1
    var destinationType = navigator.camera.DestinationType; // {DATA_URL: 0, FILE_URI: 1, NATIVE_URI: 2}
    
    var options = {
        quality: 50,
        destinationType: destinationType.FILE_URI,
        correctOrientation: true,
        sourceType: cameraSourceTypeSaved
    }
       
    var onSuccess = function(result) {
        console.log("Share completed? " + result.completed); // On Android apps mostly return false even while it's true
        console.log("Shared to app: " + result.app); // On Android result.app is currently empty. On iOS it's empty when sharing is cancelled (result.completed=false)
        
        setPicture(result);
    }

    var onError = function(msg) {
        console.log("Sharing failed with message: " + msg);
    }

    navigator.camera.getPicture(onSuccess, onError, options);
}


app.initialize();