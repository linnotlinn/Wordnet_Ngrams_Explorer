$(function() {
    // DOM elements
    const plot = document.getElementById('plot');
    const textresult = document.getElementById('textresult');
    const loadingOverlay = document.getElementById('loadingOverlay');

    // Server configuration
    const host = 'http://localhost:4568'; // Updated port
    const history_server = host + '/history';
    const historytext_server = host + '/historytext';
    const hyponyms_server = host + '/hyponyms';
    const hypohist_server = host + '/hypohist';
    const commonancestors_server = host + '/ancestors';

    let ngordnetQueryType = "HYPONYMS";

    // Utility functions
    function showLoading() {
        loadingOverlay.classList.add('show');
    }

    function hideLoading() {
        loadingOverlay.classList.remove('show');
    }

    function showError(message) {
        hideLoading();
        alert('Error: ' + message);
    }

    function validateInputs() {
        const words = document.getElementById('words').value.trim();
        const startYear = parseInt(document.getElementById('start').value);
        const endYear = parseInt(document.getElementById('end').value);
        const k = document.getElementById('k').value.trim();

        if (!words) {
            showError('Please enter at least one word.');
            return false;
        }

        if (startYear < 1400 || startYear > 2100) {
            showError('Start year must be between 1400 and 2100.');
            return false;
        }

        if (endYear < 1400 || endYear > 2100) {
            showError('End year must be between 1400 and 2100.');
            return false;
        }

        if (startYear > endYear) {
            showError('Start year cannot be after end year.');
            return false;
        }

        if (k && (parseInt(k) < 0)) {
            showError('K value must be 0 or greater.');
            return false;
        }

        return true;
    }

    function get_params() {
        return {
            words: document.getElementById('words').value.trim(),
            startYear: document.getElementById('start').value,
            endYear: document.getElementById('end').value,
            k: document.getElementById('k').value,
            ngordnetQueryType: ngordnetQueryType
        };
    }

    function makeRequest(url, params, successCallback, errorCallback) {
        if (!validateInputs()) {
            return;
        }

        showLoading();
        console.log('Making request to:', url, 'with params:', params);

        $.get({
            async: false,
            url: url,
            data: params,
            success: function(data) {
                console.log('Response received:', data);
                hideLoading();
                successCallback(data);
            },
            error: function(xhr, status, error) {
                console.error('Request failed:', status, error);
                hideLoading();
                errorCallback(xhr, status, error);
            },
            dataType: 'json'
        });
    }

    // Event handlers
    $('#history').click(function() {
        ngordnetQueryType = "HISTORY";
        $("#textresult").hide();
        $("#plot").show();

        const params = get_params();
        makeRequest(
            history_server,
            params,
            function(data) {
                plot.src = 'data:image/png;base64,' + data;
                plot.classList.add('fade-in');
            },
            function(xhr, status, error) {
                showError('Failed to load history chart. Please try again.');
            }
        );
    });

    $('#historytext').click(function() {
        ngordnetQueryType = "HISTORY_TEXT";
        $("#plot").hide();
        $("#textresult").show();

        const params = get_params();
        makeRequest(
            historytext_server,
            params,
            function(data) {
                textresult.value = data;
                textresult.classList.add('fade-in');
            },
            function(xhr, status, error) {
                showError('Failed to load history text. Please try again.');
            }
        );
    });

    $('#hyponyms').click(function() {
        ngordnetQueryType = "HYPONYMS";
        $("#plot").hide();
        $("#textresult").show();

        const params = get_params();
        makeRequest(
            hyponyms_server,
            params,
            function(data) {
                textresult.value = data;
                textresult.classList.add('fade-in');
            },
            function(xhr, status, error) {
                showError('Failed to load hyponyms. Please try again.');
            }
        );
    });

    $('#hypohist').click(function() {
        ngordnetQueryType = "HYPOHIST";
        $("#plot").show();
        $("#textresult").hide();

        const params = get_params();
        makeRequest(
            hypohist_server,
            params,
            function(data) {
                plot.src = 'data:image/png;base64,' + data;
                plot.classList.add('fade-in');
            },
            function(xhr, status, error) {
                showError('Failed to load hyponym history chart. Please try again.');
            }
        );
    });

    $('#commonancestors').click(function() {
        ngordnetQueryType = "ANCESTORS";
        $("#plot").hide();
        $("#textresult").show();

        const params = get_params();
        makeRequest(
            hyponyms_server, // Using hyponyms endpoint for ancestors
            params,
            function(data) {
                textresult.value = data;
                textresult.classList.add('fade-in');
            },
            function(xhr, status, error) {
                showError('Failed to load common ancestors. Please try again.');
            }
        );
    });

    // Input validation and enhancement
    $('#words').on('input', function() {
        const value = $(this).val();
        if (value) {
            $(this).removeClass('error').addClass('success');
        } else {
            $(this).removeClass('success error');
        }
    });

    $('#start, #end').on('input', function() {
        const value = parseInt($(this).val());
        const min = 1400;
        const max = 2100;
        
        if (value >= min && value <= max) {
            $(this).removeClass('error').addClass('success');
        } else {
            $(this).removeClass('success').addClass('error');
        }
    });

    $('#k').on('input', function() {
        const value = $(this).val();
        if (value === '' || (parseInt(value) >= 0)) {
            $(this).removeClass('error').addClass('success');
        } else {
            $(this).removeClass('success').addClass('error');
        }
    });

    // Keyboard shortcuts
    $(document).keydown(function(e) {
        // Ctrl+Enter or Cmd+Enter to trigger hyponyms
        if ((e.ctrlKey || e.metaKey) && e.keyCode === 13) {
            e.preventDefault();
            $('#hyponyms').click();
        }
        
        // Enter key in words field to trigger hyponyms
        if (e.keyCode === 13 && document.activeElement.id === 'words') {
            e.preventDefault();
            $('#hyponyms').click();
        }
    });

    // Auto-hide loading overlay on escape
    $(document).keydown(function(e) {
        if (e.keyCode === 27) { // Escape key
            hideLoading();
        }
    });

    // Initialize the page
    function initializePage() {
        // Set default focus
        $('#words').focus();
        
        // Add fade-in animation to main content
        $('.main-content').addClass('fade-in');
        
        // Show initial state
        $("#textresult").hide();
        $("#plot").show();
        
        console.log('WordNet & NGram Explorer initialized successfully');
    }

    // Initialize when document is ready
    initializePage();
});