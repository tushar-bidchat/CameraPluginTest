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
    
    alert("One Clicked");
}

/**
 * 
 **/
function buttonTwoClicked() {
    
    alert("Two Clicked");
}

app.initialize();