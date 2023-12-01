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

//----------------------------------------------------shop-----------------------------------------
// $(document).ready(function () {
//     // Event listener for checkbox change
//     $('input[type="checkbox"]').change(function () {
//         // Check if the checkbox is checked
//         if ($(this).prop('checked')) {
//             // Get label text
//             let label = $(this).parent().text().trim();
//
//             // Get filter type (Brand, Category, Type)
//             let filterType = $(this).closest('ul.checkbox-list').prev('span').text().trim();
//
//             // AJAX request for checked state
//             $.ajax({
//                 url: `/user/filter`,
//                 method: 'GET',
//                 data:{
//                     type: filterType,
//                     value:label
//                 },
//                 success: function (response) {
//
//                     console.log(response);
//                     // updateProductHTML(response,true);
//
//                 }
//             });
//         } else {
//
//             let label = $(this).parent().text().trim();
//
//             let filterType = $(this).closest('ul.checkbox-list').prev('span').text().trim();
//
//             $.ajax({
//                 url: `/user/filterProducts?type=${filterType}&value=${label}&checked=false`,
//                 method: 'GET',
//                 success: function (response) {
//                     console.log("unchecked")
//                     console.log(response)
//                     updateProductHTML(response,false);
//
//                 }
//             });
//
//         }
//     });
// });

$(document).ready(function () {


    let filterData = {};
    let checked = 0;
    let unchecked = 0;

    $('input[type="checkbox"]').change(function () {
        // Get label text
        let label = $(this).parent().text().trim();

        // Get filter type (Brand, Category, Type)
        let filterType = $(this).closest('ul.checkbox-list').prev('span').text().trim();

        // If the filter type does not exist in the data object, initialize it as an empty array
        if (!filterData.hasOwnProperty(filterType)) {
            filterData[filterType] = [];
        }

        // If the checkbox is checked, add the label to the array
        if ($(this).is(':checked')) {
            filterData[filterType].push(label);
            checked++;
        } else {
            filterData[filterType] = filterData[filterType].filter(item => item !== label);
            unchecked++;
        }

        if (checked - unchecked != 0) {
            $.ajax({
                url: '/user/filter',
                method: 'post',
                dataType: 'json',
                contentType: 'application/json',
                data: JSON.stringify({
                    filterData
                }),
                success: function (response) {
                    console.log(response);
                    updateProductHTML(response);
                }
            });
        } else {
            $.ajax({
                url: '/user/filter',
                method: 'post',
                dataType: 'json',
                contentType: 'application/json',
                data: JSON.stringify({
                    "status": "true"
                }),
                success: function (response) {
                    console.log(response);
                    updateProductHTML(response);
                }
            });
        }
    });
});


//---------------------------------------------shop---------------------------------------------

function updateProductHTML(products) {
    $('.childDiv').remove();

    for (let i = 0; i < products.length; i++) {

        let product = products[i];

        let newElement = $('<div class="product childDiv" id="productListing-' + product.id + '">\n' +
            '            <a style="text-decoration: none;color:black !important;"\n' +
            '               href="/user/product-details/' + product.id + '">\n' +
            '                <img alt="Product Image" src="/uploads/' + product.images[0] + '">\n' +
            '                <h3>' + product.name + '</h3>\n' +
            '                <p style="font-family: \'Poppins\'; font-size: 12px;overflow: hidden; text-overflow: ellipsis;">\n' +
            '                    ' + product.description + '\n' +
            '                </p>\n' +
            '                <a class="add-to-wishlist" onclick="addToWishlist(this)"\n' +
            '                   style="position: absolute; top: 10px; right: 10px; cursor: pointer"\n' +
            '                   data-product-id="' + product.id + '">\n' +
            '                    <img id="wishlistButton-' + product.id + '" alt="Add to Favorites"\n' +
            '                         style="width: 24px; height: 24px;"\n' +
            '                         src="/image/heart-regular.svg">\n' +
            '                </a>\n' +
            '            </a>\n' +
            '            <div class="price-and-cart">\n' +
            '                <p>\n' +
            '                    <span>' + product.price + '</span>\n' +
            '                    <del>$119</del>\n' +
            '                </p>\n' +
            '                <button class="add-to-cart" onclick="addToCart(this)" data-product-id="' + product.id + '">Add to Cart\n' +
            '                </button>\n' +
            '            </div>\n' +
            '        </div>');


        $('.parentDiv').append(newElement);

    }
}

let entry = 0;

function addToWishlist(element) {
    let productId = element.getAttribute('data-product-id');
    let wishlistButton = document.getElementById('wishlistButton-' + productId);
    let wishlistCount = document.getElementById('wishlist-badge');
    let count = parseInt(wishlistCount.innerText);

    if (entry === 0) {
        $.ajax({
            url: '/user/add-to-wishlist?id=' + productId,
            method: "GET",
            success: function (response) {

                if (response === "success") {
                    toast("Added To Wishlist");
                    wishlistButton.src = "/image/red-heart.png";
                    wishlistCount.innerText = count + 1;
                    entry = 1;
                } else if (response === "sameProduct") {
                    toast("Product Already Exist In Wishlist")
                    entry = 1;
                } else {
                    location.href = "/login"
                }
            }
        });
    } else {
        $.ajax({
            url: '/user/removeFromWishlist?id=' + productId,
            method: "GET",
            success: function (response) {
                if (response === "success") {
                    toast("Product Removed");
                    wishlistButton.src = "/image/heart-regular.svg";
                    wishlistCount.innerText = count - 1;
                    entry = 0;
                } else {
                    location.href = "/login"
                }
            }
        });
    }
}

function addToCart(element) {
    let productId = element.getAttribute('data-prdoucts-id');

    $.ajax({
        url: '/user/cart/add-to-cart-fromShop?id=' + productId,
        method: "GET",
        success: function (response) {
            let cartBadge = document.getElementById('cart-badge');
            const cartCount = parseInt(cartBadge.innerText);
            if (response === "success") {
                    cartBadge.innerText = cartCount +1;
                toast("Added To Cart");
            } else if (response === "sameProduct") {
                toast("Product Already Exist In Cart");
            } else {
                location.href = "/login";
            }
        }
    });
}

function toast(message) {
    Toastify({
        text: message,
        duration: 3000,
        position: "center",
        className: "info",
        style: {
            background: "linear-gradient(to right, #00b09b, #96c93d)",
        }
    }).showToast();
}