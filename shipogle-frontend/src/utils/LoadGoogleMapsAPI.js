export const loadGoogleMapsAPI = (apiKey) => {
  return new Promise((resolve, reject) => {
    if (window.google) {
      resolve(window.google.maps);
      return;
    }

    const script = document.createElement("script");
    script.src = `https://maps.googleapis.com/maps/api/js?key=${apiKey}`;
    script.async = true;

    script.addEventListener("load", () => {
      resolve(window.google.maps);
    });

    script.addEventListener("error", () => {
      reject(new Error("Failed to load Google Maps API"));
    });

    document.body.appendChild(script);
  });
};
