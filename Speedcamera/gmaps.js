function initialize() {
  /* Set Map Properties */
  var mapProp = {
    center:{lat: 0, lng: 0},
    zoom:18,
    panControl:false,
    zoomControl:false,
    mapTypeControl:false,
    scaleControl:false,
    streetViewControl:false,
    overviewMapControl:false,
    rotateControl:false,  
    mapTypeId:google.maps.MapTypeId.HYBRID
  };

  /* Create Map */
  var map = new google.maps.Map(document.getElementById("googleMap"),mapProp);
  map.setTilt(0);

  locator.getPosition(function(position) {
	map.setCenter(position);	
  });
  
  /* Update map when change location */
  locator.addListener(function(position) {
  	var mypos = {
        	lat: position.coords.latitude,
        	lng: position.coords.longitude
      	};
      	map.setCenter(mypos);

	var speedcamera = SpeedCameras.near(mypos.lat,mypos.lng);
	for (i = 0; i < speedcamera.length; ++i) {
		var sppos = {lat:speedcamera[i].lat, lng:speedcamera[i].lng};

		if (isLocationFree(sppos)) {
    			var scmarker = new google.maps.Marker({
    			position: sppos,
    			map: map,
			icon:speedcamera[i].getIcon()
			});
			google.maps.event.addListener(scmarker, "dblclick", function() {
    				scmarker.setMap(null);
				
			});
			lookup.push([sppos.lat, sppos.lng]);
		}
		if(getDistance(mypos, sppos) < 200) {
			alert("Vorsicht!");
		}
  	};

    	new google.maps.Marker({
    		position: mypos,
    		map: map,
  	});
  });
}

/* Object to get current location */
var locator = {

  getPosition : function(callback) {
	if (navigator.geolocation) {
    		navigator.geolocation.getCurrentPosition(function(position) {
      		var position = {
        		lat: position.coords.latitude,
        		lng: position.coords.longitude
      		};
		callback(position);
      
    		}, function() {
      			handleLocationError(true, infoWindow, map.getCenter());
    		});
  		} else {
    			/* Browser doesn't support Geolocation */
    			handleLocationError(false, infoWindow, map.getCenter());
  		}
	},

  addListener : function(func) {
	navigator.geolocation.watchPosition(func);
  }
}

/* lookup if marker exist */
var lookup = [];
function isLocationFree(position) {
  for (var i = 0, l = lookup.length; i < l; i++) {
    if (lookup[i][0] === position.lat && lookup[i][1] === position.lng) {
      return false;
    }
  }
  return true;
}

/* Distance */
function getDistance(p1, p2) {
  var R = 6378137; // Earth’s mean radius in meter
  var dLat = rad(p2.lat - p1.lat);
  var dLong = rad(p2.lng - p1.lng);
  var a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
    Math.cos(rad(p1.lat)) * Math.cos(rad(p2.lat)) *
    Math.sin(dLong / 2) * Math.sin(dLong / 2);
  var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
  var d = R * c;
  return d; // returns the distance in meter
};

var rad = function(x) {
  return x * Math.PI / 180;
};


google.maps.event.addDomListener(window, 'load', initialize);
