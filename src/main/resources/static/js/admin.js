document.addEventListener("DOMContentLoaded", function () {

    document.getElementById('weeklyBtn').addEventListener('click', () => {

        let jsonWeekObj = /*[[${weekly}]]*/ {};
        let jsonWeekCountObj = /*[[${weeklyCount}]]*/ {};

        let week = JSON.parse(jsonWeekObj);
        let weekCount = JSON.parse(jsonWeekCountObj);

        let weekValue = Object.values(week);
        let weekCountValue = Object.values(weekCount);


        let data = {
            labels: ['week1', 'week2', 'week3', 'week4', 'week5', 'week6', 'week7'],
            datasets: [{
                label: 'Revenue',
                data: weekValue.reverse(),
                fill: true,
                borderColor: '#ff6384',
                backgroundColor: 'rgba(255, 99, 132, 0.2)',
                borderWidth: 2,
                lineTension: 0.2
            }, {
                label: 'SalesCount',
                data: weekCountValue.reverse(),
                fill: true,
                borderColor: '#ff0000',
                backgroundColor: 'rgba(0,0,0,0.2)',
                borderWidth: 2,
                lineTension: 0.2
            }]
        };

        combinedChart.data = data;
        combinedChart.update();
    });
    document.getElementById('dailyBtn').addEventListener('click', () => {

        let jsonDailyObj = /*[[${daily}]]*/ {};
        let jsonDailyCountObj = /*[[${dailyCount}]]*/ {};

        let daily = JSON.parse(jsonDailyObj);
        let dailyCount = JSON.parse(jsonDailyCountObj);
        let dates = Object.keys(days);
        let dailyValue = Object.values(daily);
        let dailyCountValue = Object.values(dailyCount);


        let data = {
            labels: dates.reverse(),
            datasets: [{
                label: 'Revenue',
                data: dailyValue.reverse(),
                fill: true,
                borderColor: '#ff6384',
                backgroundColor: 'rgba(255, 99, 132, 0.2)',
                borderWidth: 2,
                lineTension: 0.2
            }, {
                label: 'SalesCount',
                data: dailyCountValue.reverse(),
                fill: true,
                borderColor: '#ec0808',
                backgroundColor: 'rgba(0,0,0,0.2)',
                borderWidth: 2,
                lineTension: 0.2
            }]
        };

        combinedChart.data = data;
        combinedChart.update();
    });

    document.getElementById('monthlyBtn').addEventListener('click', () => {
        let jsonMonthObj = /*[[${monthly}]]*/ {};
        let jsonMonthCountObj = /*[[${monthlyCount}]]*/ {};
        let month = JSON.parse(jsonMonthObj);
        let monthCount = JSON.parse(jsonMonthCountObj);
        let monthlySales = Object.values(month);
        let monthlySalesCount = Object.values(monthCount)


        let data = {
            labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
            datasets: [{
                label: 'Revenue',
                data: monthlySales,
                fill: true,
                borderColor: '#ff6384',
                backgroundColor: 'rgba(255, 99, 132, 0.2)',
                borderWidth: 2,
                lineTension: 0.2
            }, {
                label: 'SalesCount',
                data: monthlySalesCount,
                fill: true,
                borderColor: '#ff003c',
                backgroundColor: 'rgba(0,0,0,0.2)',
                borderWidth: 2,
                lineTension: 0.2
            }]
        };

        combinedChart.data = data;
        combinedChart.update();
    });

    document.getElementById('yearlyBtn').addEventListener('click', () => {
        let jsonYearObj = /*[[${yearly}]]*/ {};
        let jsonYearCountObj = /*[[${yearlyCount}]]*/ {};
        let years = JSON.parse(jsonYearObj);
        let yearsCount = JSON.parse(jsonYearCountObj);
        let yearlySales = Object.values(years);
        let yearlySalesCount = Object.values(yearsCount);

        let data = {
            labels: ['2019', '2020', '2021', '2022', '2023'],
            datasets: [{
                label: 'Revenue',
                data: yearlySales,
                fill: true,
                borderColor: '#ff6384',
                backgroundColor: 'rgba(255, 99, 132, 0.2)',
                borderWidth: 2,
                lineTension: 0.2
            }, {
                label: 'SalesCount',
                data: yearlySalesCount,
                fill: true,
                borderColor: '#ff6384',
                backgroundColor: 'rgba(255, 99, 132, 0.2)',
                borderWidth: 2,
                lineTension: 0.2
            }]
        };

        combinedChart.data = data;
        combinedChart.update();
    });
});

let jsonDailyObj = /*[[${daily}]]*/ {};
let jsonDailyCountObj = /*[[${dailyCount}]]*/ {};
let days = JSON.parse(jsonDailyObj);
let daysCount = JSON.parse(jsonDailyCountObj);

let dates = Object.keys(days);
let transformedKeys = Object.values(days);
let daysCounts = Object.values(daysCount);
console.log(transformedKeys);


let data = {
    labels: dates.reverse(),
    datasets: [{
        label: 'Revenue',
        data: transformedKeys.reverse(),
        fill: true,
        borderColor: '#ff6384',
        backgroundColor: 'rgba(255, 99, 132, 0.2)',
        borderWidth: 2,
        lineTension: 0.2
    }, {
        label: 'SalesCount',
        data: daysCounts.reverse(),
        fill: true,
        borderColor: '#ff6384',
        backgroundColor: 'rgba(255, 99, 132, 0.2)',
        borderWidth: 2,
        lineTension: 0.2
    }]
};


// Create combined line chart
let combinedChart = new Chart(document.getElementById('combinedChart').getContext('2d'), {
    type: 'line',
    data: data,
    options: {
        plugins: {
            legend: {
                labels: {
                    font: {
                        size: 14, // Increase the legend font size
                        weight: 'bold'
                    }
                }
            },
        }
    }
});


// ---------------------------------------------Banner---------------------------------------------

$(document).ready(function () {
    $('#sidebarCollapse').on('click', function () {
        $('#sidebar').toggleClass('active');
    });
});

function previewImageClicked(input) {
    let fileInput = input;

    if (fileInput.files && fileInput.files[0]) {
        let reader = new FileReader();

        reader.onload = function (e) {
            let imagePreview = document.getElementById('imageBannerPreview1');
            let imagePreview1 = document.getElementById("imageBannerPreview");

            imagePreview.src = e.target.result;
            imagePreview1.style.display = "none";

        };

        // Read the contents of the selected file
        reader.readAsDataURL(fileInput.files[0]);
    }
}


function editImage(){
    document.getElementById("imageLabel").style.display = "block";
    document.getElementById("imageLabel1").style.display = "block";
    document.getElementById("changeButton").style.display = "none";
    document.getElementById("bannerImage").disabled = false ;
}

function toast(message) {

    Toastify({
        text: message,
        duration: 1000,
        destination: "https://github.com/apvarun/toastify-js",
        newWindow: true,
        close: false,
        gravity: "top", // `top` or `bottom`
        position: "center", // `left`, `center` or `right`
        stopOnFocus: true, // Prevents dismissing of toast on hover
        style: {
            background: "linear-gradient(to right, #00b09b, #96c93d)",
        },
        onClick: function () {
        }
    }).showToast();

}

//---------------------------------------Product Offer ---------------------------------------

function newOfferModal(){
    $('#newModal').modal('show');
}

function editOffer(offerId,productId){
    document.getElementById("offerId").value = offerId;
    document.getElementById("productId").value = productId;
    console.log(offerId,productId);
    $('#editModal').modal('show');


}
