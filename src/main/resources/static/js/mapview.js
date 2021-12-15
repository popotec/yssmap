let markers = [];
let infos = [];
let map;
let stations=[];
let isIncludeNoStocks=false;

window.addEventListener('DOMContentLoaded', event => {

    let mapContainer = document.getElementById('map'), // 지도를 표시할 div
        mapOption = {
            center: new kakao.maps.LatLng(37.0, 127.0), // 지도의 중심좌표
            level: 8 // 지도의 확대 레벨
        };
    map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다

    kakao.maps.event.addListener(map, 'bounds_changed', function(data) {

        // alert('center changed!');
        requestStationDatas();
    });

    serUserPosition();

});
function requestStationDatas(){
    let position = map.getCenter();
    let centerLatitude = position.getLat();
    let centerLongitude = position.getLng();

    $.ajax({
        url: "/api/stations/near-center",
        data: {
            latitude:centerLatitude,
            longitude:centerLongitude,
        },
        type: "GET",
    }).done(function (data) {
        stations=data;
        initStations();
    });
}

function closeAllInfos() {
    for (const info of infos) {
        info.close();
    }
}

function setMapType() {
    <!-- btn , selected_btn-->
    var mapControl = document.getElementById('btnToggleStocks');
    if (mapControl.className == 'selected_btn') { //제고 없음 포함 -> 제고없음 제외 상태로 변경
        mapControl.className = 'unselected_btn';
        mapControl.textContent = "재고없음 포함";
        isIncludeNoStocks=false;
    } else { // 제고없음 제외 -> 제고없음 포화 상태로 변경
        mapControl.className = 'selected_btn';
        mapControl.textContent = "재고없음 제외";
        isIncludeNoStocks = true;
    }
    initStations();
}

function initMarkersAndInfos() {
    for (const marker of markers) {
        marker.setMap(null);
    }
    for (const info of infos) {
        info.close();
    }

    markers = [];
    infos = [];
}

function serUserPosition() {

    // HTML5의 geolocation으로 사용할 수 있는지 확인합니다
    if (navigator.geolocation) {

        // GeoLocation을 이용해서 접속 위치를 얻어옵니다
        navigator.geolocation.getCurrentPosition(function (position) {

            var lat = position.coords.latitude, // 위도
                lon = position.coords.longitude; // 경도

            var locPosition = new kakao.maps.LatLng(lat, lon), // 마커가 표시될 위치를 geolocation으로 얻어온 좌표로 생성합니다
                message = '<div style="padding:5px;">현재위치</div>'; // 인포윈도우에 표시될 내용입니다

            // 마커와 인포윈도우를 표시합니다

            var imageSrc = './images/icons8-car-200.png', // 마커이미지의 주소입니다
                imageSize = new kakao.maps.Size(44, 49), // 마커이미지의 크기입니다
                imageOption = {offset: new kakao.maps.Point(27, 69)}; // 마커이미지의 옵션입니다. 마커의 좌표와 일치시킬 이미지 안에서의 좌표를 설정합니다.

            // 마커의 이미지정보를 가지고 있는 마커이미지를 생성합니다
            var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imageOption)

            const marker = new kakao.maps.Marker({
                position: locPosition,
                image: markerImage
            });
            marker.setMap(map);
            map.setCenter(locPosition);
            requestStationDatas();
        });

    }
}

function initStations() {

    initMarkersAndInfos();

    let j = 0;
    for (var station of stations) {
        j++;
        if (!isIncludeNoStocks && station.stocks == 0) {
            continue;
        }
        const markerPosition = new kakao.maps.LatLng(station.latitude, station.longitude);
        const marker = new kakao.maps.Marker({
            position: markerPosition
        });
        markers.push(marker);

        const info = new kakao.maps.InfoWindow({
            removable: false,
            position: marker.position,
            content: '<div class="info-title">' +
                '<span style="font-size:13px; font-weight: bold"> ' + station.name + '&nbsp;&nbsp;</span>' +
                '<span onclick="navi(' + j + ')"><i style="size:20px;" class="fas fa-directions"></i></span><br/>' +
                '<span style="font-size:12px;">보유: ' + station.stocks + 'ℓ &nbsp;</span>' +
                '<span style="font-size:11px;">(₩' + station.prices + '/ℓ) </span><br/>' +
                '<span style="font-size:12px;color:darkgray">' + station.address + '</span><br/> ' +
                '<span style="font-size:12px;color:darkgray"><i class="fas fa-phone-alt"></i>&nbsp;' + station.telNo + '</span>' +
                '</div>'
        });
        infos.push(info);
    }

    for (var i = 0; i < markers.length; i++) {
        const marker = markers[i];
        const infoview = infos[i];

        marker.setMap(map);

        kakao.maps.event.addListener(marker, 'click', function () {
            closeAllInfos();
            infoview.open(map, this);
        });
    }
}

function navi(stationId) {
    Kakao.Navi.start({
        name: stations[stationId].name,
        x: Number(stations[stationId].longitude),
        y: Number(stations[stationId].latitude),
        coordType: 'wgs84'
    })
}



Kakao.init('7d3da1b07e90a4db7425503f70e966a6');