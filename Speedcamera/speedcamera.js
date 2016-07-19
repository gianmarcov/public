/* Speed Camera Type */
var SpeedCameraType = {
  FIXED_SPEED_CONTROL: 1,
  FIXED_SPEED_AND_RED_LIGHT_CONTROL: 2,
  TEMPORARLY_SPEED_CONTROL: 3,
};

/* Speed Camera Icons */
var SpeedCameraIcon = {
  1 : "./images/FIXED_SPEED_CONTROL.png",
  2 : "./images/FIXED_SPEED_AND_RED_LIGHT_CONTROL.png",
  3 : "./images/TEMPORARLY_SPEED_CONTROL.png"
};

/* Single SpeedCamera */
function SpeedCamera(lat, lng, type) {
	this.type = type;
    	this.lat = lat;
    	this.lng = lng;

	this.getIcon = function () {
    		return SpeedCameraIcon[this.type];
	}; 
};

/* Array contains SpeedCamera */
var SpeedCameras = {
		array: new Array(),

		init: function () {
			for(var i = 0;i<360;i++) {
				SpeedCameras.array[i] = [];
				for(var n = 0;n<360;n++) {
					SpeedCameras.array[i][n] = [];
				}
			}
		},


		add: function (speedcamera) {
			var ilat = convertGeographicalDimensionsToIndex(Math.round(speedcamera.lat));
			var ilng = convertGeographicalDimensionsToIndex(Math.round(speedcamera.lng));
			var index = this.array[ilat][ilng].length + 1;
			
			SpeedCameras.array[ilat][ilng][0] = speedcamera;
			
		},

		near: function(lat,lng) {
			var ilat = convertGeographicalDimensionsToIndex(Math.round(lat));
			var ilng = convertGeographicalDimensionsToIndex(Math.round(lng));
			
			return this.array[ilat][ilng];
		}
}
SpeedCameras.init();

/* 
 * Convert functions 
 */
function convertGeographicalDimensionsToIndex(geogrphicaldimension) {
	return ((geogrphicaldimension < 0) ? geogrphicaldimension + 85 : geogrphicaldimension);
}

function convertIndexToGeographicalDimensions(geogrphicaldimension) {
	return ((geogrphicaldimension > 85) ? geogrphicaldimension - 85 : geogrphicaldimension);
}