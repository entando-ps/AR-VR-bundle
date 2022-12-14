window.onload = async() => {

     async function getPois() {

        const options = {
            method: 'get',
            headers: {
                "Content-Type": "application/json",
                "i-edx-token": "554c8ffbe6a574c33680fb594ab8c0952b54d9301fc941a34ba27434cfd8cbca",
            }
        };

      const poiList = await fetch('https://i-edx.k8s-entando.org/entandopia/accesscode/0-0-10-snapshot/api/pois', options)
          .then((response) => {
          return response.json();
        })
        .then((data) => {
        let pois = data;
        return pois;
        })
      return poiList
    }


    let method = 'dynamic';
    // de-comment following line for static places
    method = 'static';

    if (method === 'static') {
        let places = await getPois();
        renderPlaces(places);
    }

    if (method !== 'static') {
        // first get current user location
        return navigator.geolocation.getCurrentPosition(function(position) {

                // than use it to load from remote APIs some places nearby
                dynamicLoadPlaces(position.coords)
                    .then((places) => {
                        renderPlaces(places);
                    })
            },
            (err) => console.error('Error in retrieving position', err), {
                enableHighAccuracy: true,
                maximumAge: 0,
                timeout: 27000,
            }
        );
    }
};


function renderPlaces(places) {
    let scene = document.querySelector('a-scene');
    places.forEach((place) => {

        const entPath = window.location.hostname;
        const iconsrc = "/entando-de-app/cmsresources/arvr/AR/assets/assets.png";
        console.log( "entPath", entPath  );
        console.log( "iconsrc", iconsrc );
        const test = (entPath + iconsrc);
        console.log("test",test);

        const latitude = place.location.lat;
        const longitude = place.location.lng;

        // create place icon for a-scene
        const icon = document.createElement('a-image');
        icon.setAttribute('gps-entity-place', `latitude: ${latitude}; longitude: ${longitude}`);
        icon.setAttribute('name', place.name);
        icon.setAttribute('src',  iconsrc );
        icon.setAttribute('href', place.link);
        icon.setAttribute('info', place.info);
        icon.setAttribute('look-at', '[gps-camera]');

        // for debug purposes dimension are bigger
        icon.setAttribute('scale', '7, 7');

        // icon.addEventListener('loaded', () => window.dispatchEvent(new CustomEvent('gps-entity-place-loaded')));
        icon.addEventListener('loaded', () => {
            window.dispatchEvent(new CustomEvent('gps-entity-place-loaded', {
                detail: {
                    component: this.el
                }
            }))
        });


        const clickListener = function(ev) {
            ev.stopPropagation();
            ev.preventDefault();
            if (document.contains(document.getElementById('place-label'))) {
                document.getElementById('place-label').remove();
            }
            if (document.getElementById('modal-body').innerHTML.trim() != '') {
                document.getElementById('modal-title').innerHTML = "";
                document.getElementById('modal-body').innerHTML = "";
                document.getElementById('modal-link').innerHTML = "";
            }
            const name = ev.target.getAttribute('name');
            const link = ev.target.getAttribute('href');
            const info = ev.target.getAttribute('info');
            const el = ev.detail.intersection && ev.detail.intersection.object.el;

            if (el && el === ev.target) {

                //create button modal
                const button = document.createElement('button');
                button.setAttribute('id', 'modal-btn');
                button.innerText = "approfondisci";

                const label = document.createElement('span');

                const container = document.createElement('div');
                container.setAttribute('id', 'place-label');

                const poiLink = document.createElement('a');
                poiLink.setAttribute('id', 'place-link');
                poiLink.setAttribute('href', link);

                // place contents on modal
                document.getElementById('modal-title').textContent += name;
                document.getElementById('modal-body').textContent += info;
                document.getElementById('modal-link').appendChild(poiLink);

                label.innerText = name;
                poiLink.innerText = "Approfondisci";

                container.appendChild(label);
                // container.appendChild(poiLink);
                container.appendChild(button);

                document.body.appendChild(container);

                // create variables for modal
                const modal = document.getElementById("modal-info");
                const span = document.getElementsByClassName("close")[0];
                const modalContainer = document.getElementById("modal-container");

                //set onclick
                button.onclick = function() {
                    modalContainer.removeAttribute("class")
                    modalContainer.classList.add("one");
                    document.body.classList.add("modal-active");

                }
                span.onclick = function() {
                    modalContainer.classList.add("out");
                    document.body.classList.remove("modal-active");

                }
                window.onclick = function(event) {
                    if (event.target == modal) {
                        modalContainer.classList.add("out");
                        document.body.classList.remove("modal-active");
                    }
                }

                setTimeout(() => {
                    container.parentElement.removeChild(container);
                }, 9999500);
            }
        };

        icon.addEventListener('click', clickListener);
        scene.appendChild(icon);
    });
}
