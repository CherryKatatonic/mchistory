// AJAX Infinite Scroll Listener
    // (not yet modified for scrolling inside container elements on the page)

$(document).ready(function() {
    var win = $(window);

    // Keep track of whether we are currently loading or not
    var loadingMore = false;

    // An invisible <input> element with an id is required to
    var $cursor = $('#cursor');

    win.scroll(function () {
        // Adjust the subtraction from win.scrollTop() as needed
        var scrollBottom = $(document).height() - win.scrollTop() - 638;

        // Attempt to load more content if scrollbar approaches end of page
        if (scrollBottom <= 100) {
            loadMore();
        }
    });

    // Load more, or return false if there is nothing to load
    function loadMore() {
        // If our cursor <input> element has a valid value, start loading more content
        if (!loadingMore && $cursor.val() !== null && $cursor.val() !== '') {
            loadingMore = true;

            // Send a GET request with a cursor parameter to the server file where
                // this page's infinite scrolling is handled. In this example, text data
                // in JSON format is returned from a Java servlet and processed as JSON data.
            $.get('/[ENDPOINT-URL]?cursor=' + $cursor.val()).done(function(data) {

                // Parse JSON-formatted text response
                data = JSON.parse(data);

                // First Part of Response Object - Cursor //
                // In this example, the servlet returns a cursor string if there is still
                    // more content to load, or a null value if we have reached the end.
                $cursor.val(data[0]);

                // Second Part of Response Object - Admin //
                // In this example, the servlet returns a boolean indicating whether or not
                    // this request was made from an active admin session.
                var isAdmin = data[1];

                // Third Part of Response Object - Content //
                var nodes = data.slice(2, data.length);

                // In this example, Content is an array of Photo objects that will be injected
                    // into HTML and appended to the photo gallery
                nodes.forEach(function (item) {
                    // These functions must be Immediately Invoked so that we don't set loadingMore
                        // to false before they're finished.
                    (appendToGallery(item, isAdmin));
                    (appendToCarousel(item, isAdmin));
                });

                // Set loadingMore to false once we're done with all script executions
                loadingMore = false;
            });

            // Return true if more content was loaded
            return true;
        }

        // Return false if there was no more content to load
        return false;
    }

});