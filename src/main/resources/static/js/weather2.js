$(document).ready(function () {

    var userData, weatherData, temp, wind;
    var measurement = "metric";
    var success = false;

    getIPdata();

    var baseURL = "http://api.openweathermap.org/data/2.5/weather?";
    var params = "q=Seoul,KR";
    var units = "&units=" + measurement;
    var appID = "&APPID=da305acee04b5d2eb11974a72be94349";


    function getIPdata() {
        $.ajax({
            url: "http://ip-api.com/json",
            dataType: "json",
            success: function (data) {
                success = true;
                params = "lat=" + data.lat + "&lon=" + data.lon;
                userData = data;
                getOWMdata();
            },
            error: function () {
                $("#app-status").html(
                    "죄송합니다! 우리는 당신의 위치를 알 수 없습니다."
                );
                success = false;
                getOWMdata();
            }
        });
    }

    // 날씨정보 보기 (https://openweathermap.org/)

    function getOWMdata() {
        $.ajax({
            url: baseURL + params + units + appID,
            dataType: "json",
            success: function (data) {
                showDate();
                weatherData = data;
                showWeather();
            },
            error: function () {
                success = false;
                $("#app-status").html(
                    "죄송합니다! 이 시간대 날씨를 얻을 수 없습니다."
                );
                showWeather();
            }
        });
    }

    /*--------------------------------------------------------------
    # showDate()
    --------------------------------------------------------------*/
    function showDate() {
        var now = new Date();
        var days = [
            "Sunday", "Monday", "Tuesday", "Wednesday",
            "Thursday", "Friday", "Saturday"
        ];
        var months = [
            "January", "February", "March",
            "April", "May", "June",
            "July", "August", "September",
            "October", "November", "December"
        ];
        // getDay returns a number 0-6, so days[0] = Sunday
        var weekday = days[now.getDay()];
        // getDay returns a number 0-11, so months[0] = January
        var month = months[now.getMonth()];

        // fullDate = Weekday, Month DD YYYY | using non-breaking space characters (&nbsp;) to get things to wrap together
        var fullDate = weekday + ", " + month + "&nbsp;" + now.getDate() + "&nbsp;" + now.getFullYear();

        $("#date").html(fullDate);
    }


    function showWeather() {

        $("#location").html(userData.city + ", " + userData.countryCode);

        $("#lat_long").html("Lat: " + userData.lat + ", Lon: " + userData.lon);

        temp = weatherData.main.temp;

        $("#temp").html(Math.round(temp) + "&deg;C");

        $("#condition").html(weatherData.weather[0].description);

        /* Get rid of 'N/A' icon and change to icon mapped based on wather condition ID
           Read More here: https://openweathermap.org/weather-conditions
           Also weather Icon Mappings https://erikflowers.github.io/weather-icons/api-list.html */
        $("#condition_icon").removeClass("wi-na").addClass("wi-owm-" + weatherData.weather[0].id);

        $("#pressure").html(weatherData.main.pressure);
        $("#humidity").html(weatherData.main.humidity);

        wind = weatherData.wind.speed;

        $("#wind").html(Math.round(wind) + " km/h");

        if (success) {
            $("#app-status").slideUp(300);
        }
    }


    function convert() {
        if (measurement === "imperial") {
            measurement = "metric";
            temp = (temp - 32) * (5 / 9);
            wind = wind / 0.62137;
            $("#temp, #wind").fadeOut(300, function () {
                $("button>span").html("F");
                $("#wind").html(Math.round(wind) + " km/h");
                $("#temp").html(Math.round(temp) + "&deg;C");
                $("#temp, #wind").fadeIn(300);
            });
        } else {
            measurement = "imperial";
            temp = temp * 9 / 5 + 32;
            wind = (wind * 0.62137).toFixed(1);
            $("#temp").fadeOut(300, function () {
                $("button>span").html("C");
                $("#temp").html(Math.round(temp) + "&deg;F");
                $("#wind").html(wind + " mph");
                $("#temp, #wind").fadeIn(300);
            });
        }
    }

    $("button").click(function () {
        convert();
    });
});

// 날씨 아이콘 맵
var codeIconMap = {
    200: "wi-day-snow-thunderstorm", // thunderstorm with light rain
    201: "wi-night-storm-showers", //	thunderstorm with rain
    202: "wi-thunderstorm", // thunderstorm with heavy rain
    210: "wi-day-lightning", //	light thunderstorm
    211: "wi-day-lightning", // thunderstorm
    221: "wi-lightning", // ragged thunderstorm
    230: "wi-day-thunderstorm", //	thunderstorm with light drizzle
    231: "wi-storm-showers", // thunderstorm with drizzle
    232: "wi-thunderstorm", //	thunderstorm with heavy drizzle

    //Group 3xx: Drizzle
    300: "wi-day-sprinkle", //light intensity drizzle
    301: "wi-day-showers", //drizzle
    302: "wi-day-rain", //heavy intensity drizzle
    310: "wi-day-sprinkle", //light intensity drizzle rain
    311: "wi-day-showers", //drizzle rain
    312: "wi-day-rain", //heavy intensity drizzle rain
    313: "wi-day-showers", //shower rain and drizzle
    314: "wi-day-rain", //heavy shower rain and drizzle
    321: "wi-day-rain", //shower drizzle

    //Group 5xx: Rain
    500: "wi-day-sprinkle", //light rain
    501: "wi-day-showers", //	moderate rain
    502: "wi-day-rain", //heavy intensity rain
    503: "wi-showers", //	very heavy
    504: "wi-rain", //extreme rain
    511: "wi-rain-mix", //freezing rain
    520: "wi-day-sprinkle", //light intensity shower rain
    521: "wi-day-showers", //shower rain
    522: "wi-day-rain", // 	heavy intensity shower rain
    531: "wi-showers" //	ragged shower rain
};