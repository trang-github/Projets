<?php
session_start();

if ( ! isset($_POST['flag'])) {
  return;
}

function create_ballot($ballot) {
  $fp = fopen('database/ballots.json', 'r+');

  $ballots = '';
  if (filesize('database/ballots.json') > 0) {
    $ballots = fread($fp, filesize('database/ballots.json'));  
  }

  $ballot_id = date('YmdHis');
  $ballot['id'] = $ballot_id;
  $ballot['status'] = 'open';

  if ($ballots == '') {
    fwrite($fp, json_encode(array($ballot)));
  } else {
    $current_ballots = json_decode($ballots, true);
    array_push($current_ballots, $ballot);
    
    fclose($fp);
    unlink('database/ballots.json');

    $fp = fopen('database/ballots.json', 'w');
    fwrite($fp, json_encode($current_ballots));
  }

  fclose($fp);

  /** ballot result db */
  $fp = fopen('database/ballot_result.json', 'r+');

  $ballot_results = '';
  if (filesize('database/ballot_result.json') > 0) {
    $ballot_results = fread($fp, filesize('database/ballot_result.json'));  
  }

  $insert_data = array(
    'id' => $ballot_id,
    'status' => 'open',
    'rate' => '0/' . strval($ballot['total_votes_allowed']),
    'own' => '',
    'answers' => new class{}
  );
  if ($ballot_results == '') {
    fwrite($fp, json_encode(array($insert_data)));
  } else {
    $current_ballot_results = json_decode($ballot_results, true);
    array_push($current_ballot_results, $insert_data);
    
    fclose($fp);
    unlink('database/ballot_result.json');

    $fp = fopen('database/ballot_result.json', 'w');
    fwrite($fp, json_encode($current_ballot_results));
  }

  fclose($fp);

  return $ballot_id;
}

function get_ballots() {
  $username = $_SESSION['username'];
  $fp = fopen('database/ballots.json', 'r');

  $ballots = '';
  if (filesize('database/ballots.json') > 0) {
    $ballots = fread($fp, filesize('database/ballots.json'));  
  }

  fclose($fp);

  $ballot_list = [];
  if ($ballots != '') {
    $ballots = json_decode($ballots, true);

    for ($i=0; $i < count($ballots); $i++) { 
      if ($username == $ballots[$i]['organizer']) {
        array_push($ballot_list, $ballots[$i]['id']);
      }
    }
  }

  return $ballot_list;
}

function get_ballots_for_vote() {
  $username = $_SESSION['username'];
  $fp = fopen('database/ballots.json', 'r');

  $ballots = '';
  if (filesize('database/ballots.json') > 0) {
    $ballots = fread($fp, filesize('database/ballots.json'));  
  }

  fclose($fp);

  $ballot_list = [];
  if ($ballots != '') {
    $ballots = json_decode($ballots, true);

    for ($i=0; $i < count($ballots); $i++) { 
      $voters = $ballots[$i]['voters'];
      for ($j=0; $j < count($voters); $j++) {
        if ($username == $voters[$j]['username']) {
          array_push($ballot_list, $ballots[$i]['id']);
          break;
        }
      }
    }
  }

  return $ballot_list;
}

function get_ballot($id) {
  $username = $_SESSION['username'];
  $fp = fopen('database/ballots.json', 'r');

  $ballots = '';
  if (filesize('database/ballots.json') > 0) {
    $ballots = fread($fp, filesize('database/ballots.json'));  
  }

  fclose($fp);

  $ballot = '';
  if ($ballots != '') {
    $ballots = json_decode($ballots, true);

    for ($i=0; $i < count($ballots); $i++) { 
      if ($id == $ballots[$i]['id']) {
        $ballot = $ballots[$i];
        break;
      }
    }
  }

  return $ballot;
}

function get_ballot_result($id) {
  $fp = fopen('database/ballot_result.json', 'r');

  $ballots = '';
  if (filesize('database/ballot_result.json') > 0) {
    $ballots = fread($fp, filesize('database/ballot_result.json'));  
  }

  fclose($fp);

  $ballot = '';
  if ($ballots != '') {
    $ballots = json_decode($ballots, true);

    for ($i=0; $i < count($ballots); $i++) { 
      if ($id == $ballots[$i]['id']) {
        $ballot = $ballots[$i];
        break;
      }
    }
  }

  return $ballot;
}

function close_ballot($id) {
  $ballots_result = file_get_contents('database/ballot_result.json');
  $ballots_result = json_decode($ballots_result, true);
  $ballot = '';
  for ($i=0; $i < count($ballots_result); $i++) { 
    if ($id == $ballots_result[$i]['id']) {
      $ballots_result[$i]['status'] = 'close';
      $ballot = $ballots_result[$i];
      break;
    }
  }
  $ballots_result = json_encode($ballots_result);
  file_put_contents('database/ballot_result.json', $ballots_result);

  $ballots = file_get_contents('database/ballots.json');
  $ballots = json_decode($ballots, true);
  for ($i=0; $i < count($ballots); $i++) { 
    if ($id == $ballots[$i]['id']) {
      $ballots[$i]['status'] = 'close';
      break;
    }
  }
  $ballots = json_encode($ballots);
  file_put_contents('database/ballots.json', $ballots);
  return $ballot;
}

function remove_ballot($id) {

  $fp = fopen('database/ballot_result.json', 'r+');

  $ballots = '';
  if (filesize('database/ballot_result.json') > 0) {
    $ballots = fread($fp, filesize('database/ballot_result.json'));  
  }

  if ($ballots != '') {
    $ballots = json_decode($ballots, true);

    for ($i=0; $i < count($ballots); $i++) { 
      if ($id == $ballots[$i]['id']) {
        array_splice($ballots, $i, 1);
        break;
      }
    }
  }

  fclose($fp);
  unlink('database/ballot_result.json');
  $fp = fopen('database/ballot_result.json', 'w');
  fwrite($fp, json_encode($ballots));
  fclose($fp);

  $fp = fopen('database/ballots.json', 'r+');

  $ballots = '';
  if (filesize('database/ballots.json') > 0) {
    $ballots = fread($fp, filesize('database/ballots.json'));  
  }

  if ($ballots != '') {
    $ballots = json_decode($ballots, true);

    for ($i=0; $i < count($ballots); $i++) { 
      if ($id == $ballots[$i]['id']) {
        array_splice($ballots, $i, 1);
        break;
      }
    }
  }

  fclose($fp);
  unlink('database/ballots.json');
  $fp = fopen('database/ballots.json', 'w');
  fwrite($fp, json_encode($ballots));
  fclose($fp);
}

function vote_for_ballot($id, $answer) {
  $ballots_result = file_get_contents('database/ballot_result.json');
  $ballots_result = json_decode($ballots_result, true);
  for ($i=0; $i < count($ballots_result); $i++) { 
    if ($id == $ballots_result[$i]['id']) {
      if ((!isset($ballots_result[$i]['answers'][$answer]))) {
        $ballots_result[$i]['answers'][$answer] = 1;
      } else {
        $ballots_result[$i]['answers'][$answer] += 1;
      }
      $rate = explode('/', $ballots_result[$i]['rate']);
      $rate[0] = strval((int)$rate[0] + 1);
      $rate = join('/', $rate);
      $ballots_result[$i]['rate'] = $rate;
      break;
    }
  }
  $ballots_result = json_encode($ballots_result);
  file_put_contents('database/ballot_result.json', $ballots_result);

  $username = $_SESSION['username'];
  $ballots = file_get_contents('database/ballots.json');
  $ballots = json_decode($ballots, true);
  $votes_left = -1;
  for ($i=0; $i < count($ballots); $i++) { 
    if ($id == $ballots[$i]['id']) {
      $voters = $ballots[$i]['voters'];
      for ($j=0; $j < count($voters); $j++) {
        if ($username == $voters[$j]['username']) {
          $voters[$j]['total_votes_left'] -= 1;
          $votes_left = $voters[$j]['total_votes_left'];
          $ballots[$i]['voters'][$j]['total_votes_left'] = $voters[$j]['total_votes_left'];
          break;
        }
      }
      if ($votes_left >= 0) {
        break;
      }
    }
  }
  $ballots = json_encode($ballots);
  file_put_contents('database/ballots.json', $ballots);
  return $votes_left;
}

$flag = $_POST['flag'];

if ($flag == 'create_ballot') {
  $ballot = $_POST['ballot'];
  $ballot_id = create_ballot($ballot);

  $_SESSION['ballot_id'] = $ballot_id;

  echo json_encode($ballot_id);exit;
} else if ($flag == 'get_ballots') {
  $ballot_list = get_ballots();

  echo json_encode($ballot_list);exit;
} else if ($flag == 'get_ballots_for_vote') {
  $ballot_list = get_ballots_for_vote();

  echo json_encode($ballot_list);exit;
} else if ($flag == 'set_ballot') {
  $id = $_POST['id'];

  $_SESSION['ballot_id'] = $id;

  echo json_encode('');exit;
} else if ($flag == 'get_ballot') {
  $id = $_SESSION['ballot_id'];

  $ballot = get_ballot($id);

  echo json_encode($ballot);exit;
} else if ($flag == 'exit_ballot') {
  $_SESSION['ballot_id'] = '';

  echo json_encode('');exit;
} else if ($flag == 'get_ballot_result') {
  $id = $_SESSION['ballot_id'];
  $ballot = get_ballot_result($id);

  echo json_encode($ballot);exit;
} else if ($flag == 'close_ballot') {
  $id = $_SESSION['ballot_id'];
  $ballot = close_ballot($id);

  echo json_encode($ballot);exit;
} else if ($flag == 'vote_for_ballot') {
  $id = $_SESSION['ballot_id'];
  $votes_left = vote_for_ballot($id, $_POST['answer']);

  echo json_encode($votes_left);exit;
} else if ($flag == 'remove_ballot') {
  $id = $_SESSION['ballot_id'];
  remove_ballot($id);

  echo json_encode(true);exit;
}