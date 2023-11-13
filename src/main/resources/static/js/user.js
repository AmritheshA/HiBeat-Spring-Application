// ------------------------------------------product Details---------------------------------
$(document).ready(function () {
    $('#myForm').submit(function (event) {
        event.preventDefault();
        let form = document.getElementById('myForm');
        let formData = new FormData(form);

        $.ajax({
            url: '/user/write-review',
            method: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function (response) {
                if (response === "success") {
                    Toastify({
                        text: "Review Posted",
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
                        } // Callback after click
                    }).showToast();
                    window.location.reload();
                    form.reset();
                } else if (response === "failed") {
                    Toastify({
                        text: "Review Failed Post",
                        duration: 1000,
                        destination: "https://github.com/apvarun/toastify-js",
                        newWindow: true,
                        close: false,
                        gravity: "top", // `top` or `bottom`
                        position: "center", // `left`, `center` or `right`
                        stopOnFocus: true, // Prevents dismissing of toast on hover
                        style: {
                            background: "linear-gradient(to right, #f00707, #00d4ff);",
                        },
                        onClick: function () {
                        } // Callback after click
                    }).showToast();
                    form.reset();
                } else if (response === "exist") {
                    Toastify({
                        text: "You already posted a review",
                        duration: 1000,
                        destination: "https://github.com/apvarun/toastify-js",
                        newWindow: true,
                        close: false,
                        gravity: "top", // `top` or `bottom`
                        position: "center", // `left`, `center` or `right`
                        stopOnFocus: true, // Prevents dismissing of toast on hover
                        style: {
                            background: "linear-gradient(to right, #ff6347, #ff0000);",
                        },
                        onClick: function () {
                        } // Callback after click
                    }).showToast();
                    form.reset();
                }
            },
            error: function (xhr, status, error) {
                Toastify({
                    text: "Your form was not sent successfully.",
                    duration: 1000,
                    destination: "https://github.com/apvarun/toastify-js",
                    newWindow: true,
                    close: false,
                    gravity: "top", // `top` or `bottom`
                    position: "center", // `left`, `center` or `right`
                    stopOnFocus: true, // Prevents dismissing of toast on hover
                    style: {
                        background: "linear-gradient(to right, #f00707, #f00727);",
                    },
                    onClick: function () {
                    } // Callback after click
                }).showToast();
            }
        });
    });
});

// function postReply() {
//     let reviewId = document.getElementById("reviewId").value;
//
//     $.ajax({
//         url: `/user/save-replay?replay=${replay}&reviewId=${reviewId}`,
//         method: "GET",
//         success: function (response) {
//             if (response !== "null" ) {
//                 $(document).ready(function () {
//                     let newComment = document.createElement("div");
//                     newComment.className = "comment";
//                     newComment.innerHTML = `<span class="comment-author">${response}:</span>
//                 <span class="comment-text">${replay}</span>`;
//
//                     let commentsContainer = document.querySelector(".comments-container");
//                     if (commentsContainer) {
//                         commentsContainer.appendChild(newComment);
//                         document.getElementById("replyInput").value = "";
//                     } else {
//                         console.error("Comments container not found");
//                     }
//                 });
//             }else{
//                 Toastify({
//                     text: "You already posted a comment",
//                     duration: 1000,
//                     destination: "https://github.com/apvarun/toastify-js",
//                     newWindow: true,
//                     close: false,
//                     gravity: "top", // `top` or `bottom`
//                     position: "center", // `left`, `center` or `right`
//                     stopOnFocus: true, // Prevents dismissing of toast on hover
//                     style: {
//                         background: "linear-gradient(to right, #00b09b, #96c93d)",
//                     },
//                     onClick: function () {
//                     } // Callback after click
//                 }).showToast();
//                 document.getElementById("replyInput").value = "";
//
//             }
//
//         }
//     })
//
// }

function openModalForEditing(reviewId) {
    let title = document.getElementById("reviewTitle");
    let review = document.getElementById("exampleFormControlTextarea2")
    let rating = document.getElementById("exampleFormControlInput4");
    $.ajax({
        url: `/user/get-review?id=${reviewId}`,
        method: "GET",
        success: function (response) {

            title.value = response[0];
            review.value = response[1];
            rating.value = response[2]
        }
    })
    // Your logic here
    $('#editReviewModal').modal('show');
}

// $('#myForm1').submit(function (event) {
//     event.preventDefault();
//     let form = document.getElementById('myForm');
//     let formData = new FormData(form);
//
//     $.ajax({
//             url: '/user/edit-review',
//             method: 'POST',
//             data: formData,
//             processData: false,
//             contentType: false,
//             success: function (response) {
//                 if (response === "success") {
//                     Toastify({
//                         text: "Review Edited",
//                         duration: 1000,
//                         destination: "https://github.com/apvarun/toastify-js",
//                         newWindow: true,
//                         close: false,
//                         gravity: "top", // `top` or `bottom`
//                         position: "center", // `left`, `center` or `right`
//                         stopOnFocus: true, // Prevents dismissing of toast on hover
//                         style: {
//                             background: "linear-gradient(to right, #00b09b, #96c93d)",
//                         },
//                         onClick: function () {
//                         }
//                     })
//                 }
//             }
//         }
//     )
// });

function changeImage() {
    let fileInput = document.getElementById('imageInput1');
    let label = document.getElementById('imageLabel');
    let div = document.getElementById('imageDiv');
    let button = document.getElementById('changeButton');
    fileInput.disabled = false;
    label.style.display = "block";
    div.style.display = "block";
    button.style.display = "none";
}

function openModalForImage(element) {
    let src = element.src;
    document.getElementById("modalImage").src = src;
    $('#imageModal').modal('show');

}

function previewImage() {

    let fileInput = document.getElementById('imageInput');
    let allowedExtensions = /(\.jpg|\.png)$/i;

    if (fileInput.files.length > 0) {
        let fileSize = fileInput.files[0].size; // in bytes

        // Check file size (1MB = 1048576 bytes)
        if (fileSize > 1048576) {
            alert('File size exceeds the limit. Please choose a smaller file.');
            fileInput.value = '';
            return;
        }

        // Check file type
        let fileName = fileInput.files[0].name;
        if (!allowedExtensions.exec(fileName)) {
            alert('Invalid file type. Please choose a JPG or PNG file.');
            fileInput.value = '';
            return;
        }
    } else {
        alert('Please choose a file before submitting.');
        return;
    }


    let input = document.getElementById('imageInput');
    let preview = document.getElementById('preview');
    preview.style.display = 'block';

    if (input.files && input.files[0]) {
        let reader = new FileReader();

        reader.onload = function (e) {
            preview.src = e.target.result;
        }

        reader.readAsDataURL(input.files[0]);
    }
}
