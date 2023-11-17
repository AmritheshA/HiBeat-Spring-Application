function openModal(){
    $('#dateSelectionModal').modal('show');
}

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
