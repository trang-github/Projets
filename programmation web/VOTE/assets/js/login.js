$(document).ready(function() {
  addUser = (username, password) => {
    $.ajax({
      url: 'user.php',
      type: 'post',
      dataType: 'json',
      data: {
        'flag': 'add_user',
        'username': username,
        'password': password
      },
      success: function(url) {
        alert('This user has been successfully registered. Now you can use this user name.');
        if (url != '') {
          location.href = url;
          return;
        }
      }
    });
  }

  checkUser = (username, password) => {
    $.ajax({
      url: 'user.php',
      type: 'post',
      dataType: 'json',
      data: {
        'flag': 'check_user',
        'username': username,
        'password': password
      },
      success: function(result) {
        if (result['user'] == '') {
          if (confirm('This user isn\'t exist now. Would you like to add this user?')) {
            addUser(username, password);
            sessionStorage.setItem('username', username);
            $('#current_user_info').removeClass('hide');
          }
        } else if (result['user'] == 'incorrect') {
          alert('Please input password correctly.');
          return;
        } else {
          sessionStorage.setItem('username', username);
          $('#current_user_info').removeClass('hide');
          if (result['url'] != '') {
            location.href = result['url'];
            return;
          }
        }
      }
    });
  }

  loginButtonClick = () => {
    let username = $('.user-name').val();
    let password = $('.user-password').val();

    if (username != '' && password != '') {
      checkUser(username, password);
    } else {
      alert('Please input name and password correctly.');
      return;
    }
  }

  $('.login').click(function(event) {
    loginButtonClick();
  });

  // $('.login-form').on('keydown', function(e) {
  //   if (e.keyCode == 13) {
  //    loginButtonClick();
  //   }
  // });
});