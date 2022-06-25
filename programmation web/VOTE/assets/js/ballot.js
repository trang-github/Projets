$(document).ready(function() {

  let userGroups = [];

  getVoters = () => {
    $.ajax({
      url: 'user.php',
      type: 'post',
      dataType: 'json',
      data: {
        'flag': 'get_voters'
      },
      success: function(voters) {

        if (voters.length > 0) {
          let votersHtml = '';

          for (let i = 0; i < voters.length; i++) {
            votersHtml += '<option value="' + voters[i]['username'] + '">' + voters[i]['username'] + '</option>';
          }

          $('.voters .voters-label select').html(votersHtml);

          getUserGroups();
        }
      }
    });
  }

  getUserGroups = () => {
    $.ajax({
      url: 'user.php',
      type: 'post',
      dataType: 'json',
      data: {
        'flag': 'get_user_groups'
      },
      success: function(groups) {

        if (groups.length > 0) {
          userGroups = groups;
          let groupsHtml = '';

          for (let i = 0; i < groups.length; i++) {
            let voters = '(';
            for (let j=0; j < groups[i]['users'].length; j++) {
              voters = voters + groups[i]['users'][j] + ',';
            }
            voters = voters.slice(0,-1);
            voters = voters + ')';
            groupsHtml += '<option value="' + groups[i]['groupName'] + voters + '">' + groups[i]['groupName'] + voters + '</option>';
          }

          $('.voters .voters-label select').append(groupsHtml);
        }
      }
    });
  }

  createBallot = () => {
    let ballot = {};

    let organizer = $('.new-ballot-form .user-name').val();
    if (!organizer || organizer == '') {
      organizer = sessionStorage.getItem('username');
    }
    ballot['organizer'] = organizer;
    ballot['question'] = $('.new-ballot-form .question').val();

    ballot['answers'] = [];
    $('.answer-list .answer').each(function() {
      ballot['answers'].push($(this).find('input').val());
    });

    ballot['voters'] = [];
    let total_votes = 0;

    $('.voter-list .voter').each(function() {
      let username = $(this).find('.voter-name').val();
      // if (username[username.length-1]==')') {
      //   let index = username.indexOf('(');
      //   username = username.slice(index+1, -1);
      //   username = username.split(',');
      // } else {
      //   username = [username];
      // }
      // for (let i=0; i<username.length; i++) {
        let voter = {};
        voter['username'] = username;
        voter['proxy'] = 0;
  
        if ($(this).find('.proxy-1').is(':checked')) {
          voter['proxy'] = voter['proxy'] + 1;
        }
        if ($(this).find('.proxy-2').is(':checked')) {
          voter['proxy'] = voter['proxy'] + 1;
        }
  
        voter['total_votes_left'] = voter['proxy'] + 1;
        total_votes = total_votes + voter['total_votes_left'];
        ballot['voters'].push(voter);
      // }
    });

    ballot['total_votes_allowed'] = total_votes;

    $.ajax({
      url: 'admin.php',
      type: 'post',
      dataType: 'json',
      data: {
        'flag': 'create_ballot',
        'ballot': ballot
      },
      success: function(res) {
        $('.create-ballot').addClass('disabled');
        $('.vote-own').removeClass('disabled');
        $('.view-rate').removeClass('disabled');
        $('.close-ballot').removeClass('disabled');
        $('.remove-ballot').removeClass('disabled');
        location.href = 'ballot.php';
      }
    });
  }

  setAnswers = (answers) => {
    let html = '';

    for (let i = 0; i < answers.length; i++) {
      html += '<div class="answer">' +
        '<input type="text" value="' + answers[i] + '" class="readonly">' +
        '<button type="button" class="btn-sm remove-answer disabled">Remove</button>' +
      '</div>';  
    }

    $('.answer-list').html(html);
  }

  setVoters = (voters) => {
    let html = '';

    for (let i = 0; i < voters.length; i++) {
      html += '<div class="voter">' +
        '<input type="text" class="readonly voter-name" value="' + voters[i]['username'] + '" readonly="">' +
          '<div class="actions">';
      if (voters[i]['proxy'] > 0) {
        html += '<input type="checkbox" class="proxy-1" checked>';
      } else {
        html += '<input type="checkbox" class="proxy-1 disabled">';
      }

      if (voters[i]['proxy'] > 1) {
        html += '<input type="checkbox" class="proxy-2" checked>';
      } else {
        html += '<input type="checkbox" class="proxy-2 disabled">';
      }

      html += '<button type="button" class="remove-voter btn-sm disabled">Remove</button>' +
        '</div>' +
      '</div>';
    }

    $('.voter-list').html(html);
  }

  setClosedBallotState = (ballot) => {
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
    $('.vote-own').addClass('disabled');
    $('.view-rate').addClass('disabled');
    $('.close-ballot').addClass('disabled');
    $('.vote-rage-label').html(ballot['rate']);
    $('.vote-rage-label').removeClass('hide');
  }

  $('.answer-list').on('click', '.add-answer', function() {
    let val = $(this).prev().val().trim();
    if (val == '') {
      alert('Please input answer');
      return;
    }

    let newAnswer = '<div class="new-answer">' +
        '<input type="text" />' +
        '<button type="button" class="add-answer btn-sm">Add</button>' +
      '</div>';
    $(this).removeClass('add-answer').addClass('remove-answer');
    $(this).text('Remove');
    $(this).parent().removeClass('new-answer').addClass('answer');
    $(this).prev().addClass('readonly');

    $('.answer-list').append(newAnswer);
  });

  $('.answer-list').on('click', '.remove-answer', function() {
    $(this).parent().remove();
  });

  $('.voter-list').on('click', '.add-voter', function() {
    let users = $(this).parent().prev().val();
    if (users == '') {
      alert('Please select voter');
      return;
    }
    let group = '';
    if (users[users.length-1]==')') {
      group = users;
      let index = users.indexOf('(');
      users = users.slice(index+1, -1);
    }
    users = users.split(',');
    let voters = '';
    for (let i=0; i<users.length; i++) {
      let val = users[i];
      // if (group == '') {
        voters = voters + '<div class="voter">' +
        '<input type="text" class="readonly voter-name" value=' + val + ' readonly />' +
        '<div class="actions">' +
          '<input type="checkbox" class="proxy-1" />' +
          '<input type="checkbox" class="proxy-2" />' +
          '<button type="button" class="remove-voter btn-sm">Remove</button>' +
        '</div></div>';
      // } else {
        $('input[value="' + val + '"]').parent().remove();
      // }

      $('.voters .voters-label select option[value="' + val + '"]').addClass('hide');
    }
    if (group != '') {
      $('.voters .voters-label select option[value="' + group + '"]').addClass('hide');
      // voters = voters + '<div class="voter">' +
      //   '<input type="text" class="readonly voter-name" value="' + group + '" readonly />' +
      //   '<div class="actions">' +
      //     '<input type="checkbox" class="proxy-1" />' +
      //     '<input type="checkbox" class="proxy-2" />' +
      //     '<button type="button" class="remove-voter btn-sm">Remove</button>' +
      //   '</div></div>';
    }
    $('.voters .voters-label select').val('');
    $('.voter-list').append(voters);
    $('.new-voter').remove();
    let newVoter = '<div class="new-voter">' +
      '<input type="text" class="voter-name" readonly />' +
      '<div class="actions">' +
        '<button type="button" class="add-voter btn-sm">Add</button>' +
      '</div>' +
      '</div>';
    $('.voter-list').append(newVoter);
  });

  $('.voter-list').on('click', '.remove-voter', function() {
    let val = $(this).parent().prev().val();

    $('.voters .voters-label select option[value="' + val + '"]').removeClass('hide');

    $(this).parent().parent().remove();

    if (val[val.length-1]==')') {
      let index = val.indexOf('(');
      val = val.slice(index+1, -1);
      val = val.split(',');
      for (let i=0; i<val.length; i++) {
        $('.voters .voters-label select option[value="' + val[i] + '"]').removeClass('hide');
      }
    }
  });

  $('.voters .voters-label select').change(function() {
    let voter = $(this).val();
    $('.voter-list .new-voter input').val(voter);
  });

  $('.exit').click(function() {
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

  $('.view-rate').click(function() {
    $.ajax({
      url: 'admin.php',
      type: 'post',
      dataType: 'json',
      data: {
        'flag': 'get_ballot_result'
      },
      success: function(ballot) {
        $('.vote-rage-label').html(ballot['rate']);
        $('.vote-rage-label').removeClass('hide');
      }
    });
  });

  $('.vote-own').click(function() {
    $.ajax({
      url: 'admin.php',
      type: 'post',
      dataType: 'json',
      data: {
        'flag': 'get_ballot'
      },
      success: function(ballot) {
        let canVote = false;
        ballot['voters'].forEach((voter) => {
          if (voter['username'] == ballot['organizer']) {
            if (voter['total_votes_left'] > 0) {
              location.href = 'vote.php';
            } else {
              $('.vote-own-label').removeClass('hide');
            }
            canVote = true;
          }
        });
        if (!canVote) {
          alert('You cannot vote for this ballot');
        }
      }
    });
  });

  $('.close-ballot').click(function() {
    $.ajax({
      url: 'admin.php',
      type: 'post',
      dataType: 'json',
      data: {
        'flag': 'close_ballot'
      },
      success: function(ballot) {
        setClosedBallotState(ballot);
      }
    });
  });

  $('.create-ballot').click(function() {
    if ($('.new-ballot-form .question').val().trim() == '') {
      alert('Please input question.');
      return;
    }
    if ($('.answer-list .answer').length == 0) {
      alert('Please add answers.');
      return;
    }
    if ($('.voter-list .voter').length == 0) {
      alert('Please add voters.');
      return;
    }

    createBallot();
  });

  $.ajax({
    url: 'admin.php',
    type: 'post',
    dataType: 'json',
    data: {
      'flag': 'get_ballot'
    },
    success: function(ballot) {
      if (ballot) {
        setVoters(ballot['voters']);
        setAnswers(ballot['answers']);
        $('.question').val(ballot['question']).addClass('readonly');
        $('.create-ballot').addClass('disabled');
        $('.vote-own').removeClass('disabled');
        $('.view-rate').removeClass('disabled');
        $('.close-ballot').removeClass('disabled');
        if (ballot['status'] == 'close') {
          $.ajax({
            url: 'admin.php',
            type: 'post',
            dataType: 'json',
            data: {
              'flag': 'get_ballot_result'
            },
            success: function(ballot) {
              setClosedBallotState(ballot);
            }
          });
        }
      } else {
        getVoters();
        $('.remove-ballot').addClass('disabled');
      }
    }
  });

  $('.remove-ballot').click(function() {
    $.ajax({
      url: 'admin.php',
      type: 'post',
      dataType: 'json',
      data: {
        'flag': 'remove_ballot'
      },
      success: function(result) {
        if (result) {
          alert('Ballot removed successfully');
          location.href = 'index.php';
        } else {
          alert('Unable to delete ballot. Please try again later');
        }
      }
    });
  });
});