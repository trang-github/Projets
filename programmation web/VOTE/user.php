<?php
session_start();

if ( ! isset($_POST['flag'])) {
  return;
}

function logout_user() {
  $_SESSION['username'] = '';
  return true;
}
function check_login() {
  if (isset($_SESSION['username']) && $_SESSION['username'] != '') {
    return true;
  } else {
    return false;
  }
}

function check_user($input) {
  $fp = fopen('database/users.json', 'r');

  $users = '';
  if (filesize('database/users.json') > 0) {
    $users = fread($fp, filesize('database/users.json'));  
  }

  fclose($fp);

  if ($users == '') {
    return '';
  } else {
    $user_list = json_decode($users, true);

    for ($i=0; $i < count($user_list); $i++) { 

      if ($input['username'] == $user_list[$i]['username']) {
        if ($input['password'] == $user_list[$i]['password']) {
          $_SESSION['username'] = $input['username'];
          return 'correct';
        } else {
          return 'incorrect';
        }
      }
    }

    return '';
  }
}

function add_user($user) {
  $fp = fopen('database/users.json', 'r+');

  $users = '';
  if (filesize('database/users.json') > 0) {
    $users = fread($fp, filesize('database/users.json'));  
  }
  
  if ($users == '') {
    $user_list = [];
    array_push($user_list, $user);
    
    fwrite($fp, json_encode($user_list));
  } else {
    $user_list = json_decode($users, true);
    array_push($user_list, $user);

    fclose($fp);
    unlink('database/users.json');

    $fp = fopen('database/users.json', 'w');
    fwrite($fp, json_encode($user_list));
  }

  fclose($fp);
}

function get_users() {
  $fp = fopen('database/users.json', 'r');

  $users = '';
  if (filesize('database/users.json') > 0) {
    $users = fread($fp, filesize('database/users.json'));  
  }

  fclose($fp);

  $user_list = json_decode($users, true);

  return $user_list;
}

function get_user_groups() {
  $fp = fopen('database/user_groups.json', 'r');

  $user_groups = '';
  if (filesize('database/user_groups.json') > 0) {
    $user_groups = fread($fp, filesize('database/user_groups.json'));  
  }

  fclose($fp);

  $user_list = json_decode($user_groups, true);

  return $user_list;
}

$flag = $_POST['flag'];

if ($flag == 'check_login') {
  $result = check_login();
  if (isset($_POST['url']) && $_POST['url'] != '') {
    if ( ! $result) {
      $_SESSION['next_url'] = $_POST['url'];
    }
    if (isset($_POST['url']) &&  $_POST['url'] == 'ballot.php') {
      $_SESSION['ballot_id'] = '';
    }
  }

  echo json_encode($result);exit;
} else if ($flag == 'check_user') {
  $input = $_POST;
  $user = check_user($input);

  $return_data['user'] = $user;
  if ($user != '' && $user != 'incorrect') {
    if (isset($_SESSION['next_url']) && $_SESSION['next_url'] != '') {
      $return_data['url'] = $_SESSION['next_url'];
      $_SESSION['next_url'] = '';
    }
  }

  echo json_encode($return_data);exit;
} else if ($flag == 'add_user') {
  $user = $_POST;
  unset($user['flag']);

  add_user($user);

  $url = '';
  if (isset($_SESSION['next_url']) && $_SESSION['next_url'] != '') {
    $url = $_SESSION['next_url'];
    $_SESSION['next_url'] = '';
  }

  echo json_encode($url);exit;
} else if ($flag == 'logout_user') {
  $result = logout_user();

  echo json_encode($result); exit;
} else if ($flag == 'get_voters') {
  $user_list = get_users();

  echo json_encode($user_list);exit;
} else if ($flag == 'get_user_groups') {
  $user_groups = get_user_groups();

  echo json_encode($user_groups);exit;
}