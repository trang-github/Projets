$(document).ready(function() {
  getBallots = () => {
    let action = sessionStorage.getItem('select_ballot_for');
    let flag = 'get_ballots';
    if (action == 'vote') {
      flag = 'get_ballots_for_vote';
    }
    $.ajax({
      url: 'admin.php',
      type: 'post',
      dataType: 'json',
      data: {
        'flag': flag
      },
      success: function(ballots) {
        let ballotsHtml = '<option value=""></option>';

        if (ballots.length > 0) {
          for (let i = 0; i < ballots.length; i++) {
            ballotsHtml += '<option value="' + ballots[i] + '">' + ballots[i] + '</option>';
          }
        }

        $('.select-ballots').html(ballotsHtml);
      }
    });
  }

  $('.select-ballot').click(function() {
    let id = $('.select-ballots').val();
    if (id == '') {
      alert('Please select ballot id.');
      return;
    }

    $.ajax({
      url: 'admin.php',
      type: 'post',
      dataType: 'json',
      data: {
        'flag': 'set_ballot',
        'id': id
      },
      success: function() {
        let action = sessionStorage.getItem('select_ballot_for');
        if (action == 'vote') {
          location.href = 'vote.php';
        } else {
          location.href = 'ballot.php';
        }
      }
    });
  });

  let action = sessionStorage.getItem('select_ballot_for');
  if (action == 'vote') {
    $('.select-ballot').html('Vote');
  }
  getBallots();
});