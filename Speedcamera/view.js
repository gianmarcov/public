/* View create commands */
function create(type) {
	locator.getPosition(function(position) {
		SpeedCameras.add(new SpeedCamera(position.lat,position.lng,type));
	});
}
