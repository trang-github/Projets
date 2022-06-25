$(document).ready(function() {
  checkLogin = (url) => {
    $.ajax({
      url: 'user.php',
      type: 'post',
      dataType: 'json',
      data: {
        'flag': 'check_login',
        'url': url
      },
      success: function(res) {
        if (res) {
          location.href = url;
          return;
        } else {
          location.href = 'login.php';
        }
      }
    });
  }

  $('.vote').click(function() {
    sessionStorage.setItem('select_ballot_for', 'vote');
    checkLogin('select_ballot.php');
  });

  $('.new-ballot').click(function() {
    checkLogin('ballot.php');
  });

  $('.manage-ballot').click(function() {
    sessionStorage.setItem('select_ballot_for', 'manage')
    checkLogin('select_ballot.php');
  });
});