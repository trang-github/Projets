<?php require_once('layout/header.php') ?>

  <div class="container container-flex">
    <div class="sidebar">
      <div class="sidebar-buttons">
        <button class="btn create-ballot">Create ballot</button>
        <div>
          <button class="btn vote-own disabled">Vote</button>
          <label for="" class="vote-own-label hide">Done</label>
        </div>
        <div>
          <button class="btn view-rate disabled">View rate</button>
          <label for="" class="vote-rage-label hide"></label>
        </div>
        <button class="btn close-ballot disabled">Close ballot</button>
        <button class="btn remove-ballot top-margin">Remove ballot</button>
      </div>
      <button class="btn exit">Exit</button>
    </div>

    <form action="post" class="new-ballot-form">
      <div class="use-info mb-2">
        <label for="">A) User Information</label>
        <input type="text" class="user-name readonly" value="<?php echo $_SESSION['username'] ?>" readonly />
      </div>

      <div class="vote-question mb-2">
        <label for="">B) Question:</label>
        <textarea class="question" rows="5"></textarea>
      </div>

      <div class="vote-answer mb-2">
        <label for="">C) Options:</label>
        <div class="answer-list">
          <div class="new-answer">
            <input type="text" />
            <button type="button" class="add-answer btn-sm">Add</button>
          </div>
        </div>
      </div>

      <div class="voters">
        <label class="voters-label" for="">D) Voters:
          <select multiple>
          </select>
        </label>
        <div class="voter-list">
          <div class="new-voter">
            <input type="text" readonly />
            <div class="actions">
              <button type="button" class="add-voter btn-sm">Add</button>
            </div>
          </div>
        </div>
      </div>

      <div class="vote-result hide">
        <label class="vote-result-label" for="">E) Results</label>
        <div class="vote-result-table"></div>
      </div>
    </form>
  </div>

  <script src="assets/js/ballot.js"></script>

  <?php require_once('layout/footer.php') ?>