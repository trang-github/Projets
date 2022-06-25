<?php require_once('layout/header.php') ?>

  <div class="vote-container">
    <form action="post" class="vote-form">
      <div class="use-info mb-2">
        <label for="">A) User Information</label>
        <input type="text" class="user-name readonly" value="<?php echo $_SESSION['username'] ?>" readonly />
      </div>

      <div class="vote-question mb-2">
        <label for="">B) Ballot's Code:</label>
        <input type="text" class="ballot-code-input readonly" value="<?php echo $_SESSION['ballot_id'] ?>" readonly />
      </div>

      <div class="ballot-code mb-2">
        <label for="">C) Question:</label>
        <textarea class="question readonly" rows="5" value="" readonly></textarea>
      </div>

      <div class="ballot-options mb-2">
        <label for="">D) Pick one option and click vote:</label>
        <div class="select_answer_block">
          <div class="select_answers">
          </div>
          <div>
            <button type="button" id="vote">Vote</button>
            <button type="button" id="result">Result</button>
            <button type="button" id="exit">Leave</button>
          </div>
        </div>
      </div>

      <div class="votes_count_info"></div>

      <div class="vote-result hide">
        <label class="vote-result-label" for="">E) Results</label>
        <div class="vote-result-table"></div>
      </div>
    </form>
  </div>

  <script src="assets/js/vote.js"></script>

<?php require_once('layout/footer.php') ?>