$(document).ready(function() {
    $.ajax({
      url: 'admin.php',
      type: 'post',
      dataType: 'json',
      data: {
        'flag': 'get_ballot'
      },
      success: function(ballot) {
        if (ballot) {
          $('.question').val(ballot['question']);

          let radiobuttons = '';
          for(let i=0; i < ballot['answers'].length; i++) {
              radiobuttons = radiobuttons + '<input type="radio" name="answer" id="' +
              ballot['answers'][i] +
              '" value="' +
              ballot['answers'][i] + '"/>' + 
              '<label for="' + ballot['answers'][i] + '">' + ballot['answers'][i] + '</label><br>';
          }
          $('.select_answers').html(radiobuttons);
        }
        if (ballot['status'] == 'close') {
          $('#vote').addClass('disabled');
          $('input[type="radio"]').prop('disabled', true);
          $('.votes_count_info').html('The ballot is closed for voting.');
          $('.votes_count_info').addClass('red_text');
        } else {
          $('#result').addClass('hide');
          ballot['voters'].forEach((voter) => {
            $username = sessionStorage.getItem('username');
            if (voter['username'] == $username) {
              if (voter['total_votes_left'] > 0) {
                $('.votes_count_info').html('You have ' + voter['total_votes_left'] + ' votes remaining.');
                $('.votes_count_info').addClass('green_text');
              } else {
                $('.votes_count_info').html('You have 0 votes remaining.');
                $('.votes_count_info').addClass('red_text');
                $('#vote').addClass('disabled');
                $('input[type="radio"]').prop('disabled', true);
              }
            }
          });
        }
      }
    });

    $('#exit').click(function() {
        $.ajax({
          url: 'admin.php',
          type: 'post',
          dataType: 'json',
          data: {
            'flag': 'exit_ballot'
          },
          success: function() {
            location.href = 'index.php';
          }
        });
    });

    $('#vote').on('click', function() {
        const answer = $('input[name=answer]:checked').val();
        if (!answer) {
            alert('Please select an answer to vote');
            return;
        }
        $.ajax({
            url: 'admin.php',
            type: 'post',
            dataType: 'json',
            data: {
                'flag': 'vote_for_ballot',
                'answer': answer
            },
            success: function(votes_left) {
              alert('Voted successfully');
              if (votes_left > 0) {
                $('.votes_count_info').html('You have ' + votes_left + ' votes remaining.');
                $('.votes_count_info').addClass('green_text');
              } else {
                $('.votes_count_info').html('You have 0 votes remaining.');
                $('.votes_count_info').addClass('red_text');
                $('#vote').addClass('disabled');
                $('input[type="radio"]').prop('disabled', true);
              }
            }
        });
    });

    $('#result').click(function() {
        $.ajax({
            url: 'admin.php',
            type: 'post',
            dataType: 'json',
            data: {
              'flag': 'get_ballot_result'
            },
            success: function(ballot) {
              let totalAnswers = ballot['rate'].split('/')[0];
              let answerObject = ballot['answers'];
              let answersHtml = '';
              if (jQuery.isEmptyObject(answerObject)) {
                answersHtml = '<p>No votes were given.</p>';
              } else {
                Object.keys(answerObject).forEach((key) => {
                  let percent = parseFloat((answerObject[key]/totalAnswers)*100).toFixed(0);
                  answersHtml += '<p>' + key + ' => ' + answerObject[key] + ' = ' + percent + '%</p>';
                });
              }
              $('.vote-result-table').html(answersHtml);
              $('.vote-result').removeClass('hide');
            }
        });
    });
  });