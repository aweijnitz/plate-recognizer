

function imgElementToBase64(imgDOMElement) {
    const format = 'image/jpeg'
    const canvas = document.createElement('canvas');
    const ctx = canvas.getContext('2d');
    canvas.width = imgDOMElement.width;
    canvas.height = imgDOMElement.height;
    imgDOMElement.crossOrigin = 'Anonymous';
    ctx.drawImage(imgDOMElement, 0, 0);
    return canvas.toDataURL(format);
}


function loadImagetoDataUrl(srcUrl) {
    var format = 'image/jpg';
    var img = new Image();
    img.crossOrigin = 'Anonymous';
    img.onload = function() {
        var canvas = document.createElement('CANVAS');
        var ctx = canvas.getContext('2d');
        var dataURL;
        canvas.height = this.naturalHeight;
        canvas.width = this.naturalWidth;
        ctx.drawImage(this, 0, 0);
        dataURL = canvas.toDataURL(format);
        callback(dataURL);
    };
    // Load the image
    img.src = src;
    // make sure the load event fires for cached images too
    if (img.complete || img.complete === undefined) {
        // Flush cache
        img.src = 'data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///ywAAAAAAQABAAACAUwAOw==';
        // Try again
        img.src = src;
    }
}