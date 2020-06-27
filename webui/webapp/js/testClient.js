var deviceOrientation = 'landscape';

if (window.matchMedia("(orientation: portrait)").matches) {
    deviceOrientation = 'portrait';
} else if (window.matchMedia("(orientation: landscape)").matches) {
    deviceOrientation = 'landscape';
}
console.log('orientation: ', deviceOrientation); // More on Orientation https://stackoverflow.com/questions/20600800/js-client-side-exif-orientation-rotate-and-mirror-jpeg-images


connect('testClient', () => {
    //var imgElement = document.getElementById('canvas');
    var imgElement = document.getElementById('testimage');
    //submitImage(imgElement);
});