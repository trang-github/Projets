$(document).ready(function() {
    checkLogin = () => {
        $.ajax({
            url: 'user.php',
            type: 'post',
            dataType: 'json',
            data: {
              'flag': 'check_login'
            },
            success: function(res) {
                if (res) {
                    $('#current_user_info').removeClass('hide');
                } else {
                    $('#current_user_info').addClass('hide');
                }
            }
        });
        // if (sessionStorage.getItem('username') && sessionStorage.getItem('username') != '') {
        //     $('#current_user_info').removeClass('hide');
        // } else {
        //     $('#current_user_info').addClass('hide');
        // }
    }

    $('.logout').click(function() {
        $.ajax({
            url: 'user.php',
            type: 'post',
            dataType: 'json',
            data: {
              'flag': 'logout_user'
            },
            success: function(result) {
                if (result) {
                    $('#current_user_info').addClass('hide');
                    sessionStorage.removeItem('username');
                    location.href = 'index.php';
                } else {
                    $('#current_user_info').removeClass('hide');
                    alert('Unable to logout. Please try again.');
                }
            }
        });
    })

    checkLogin();
})