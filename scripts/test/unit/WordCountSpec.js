describe("Wordcount", function () {
  var $countParagraph = $('<p class="wordCount" role="status" aria-live="assertive" aria-relevant="text" id="wordcount" />'),
      wordsWhenCountIs = {
        'none' : ' words remaining',
        'oneTooMany' : '1 too many',
        'tooMany' : ' words too many',
        'oneLeft' : '1 word remaining',
        'someLeft' : ' words remaining'
      },
      $textbox,
      toHTML,
      sampleCopy49Words,
      sampleCopy50Words,
      sampleCopy51Words,
      sampleCopy52Words;

  
  sampleCopy49Words = "The suite can reduce risk by providing the tooling for performance management of the cloud infrastructure and backup and restore of associated workload storage. It also provides automated provisioning as well as monitoring capability for improved Service Level Agreement (SLA) and policy focus. These IBM cloud capabilities offer the";
  sampleCopy50Words = "The suite can reduce risk by providing the tooling for performance management of the cloud infrastructure and backup and restore of associated workload storage. It also provides automated provisioning as well as monitoring capability for improved Service Level Agreement (SLA) and policy focus. These IBM cloud capabilities offer the scalability";
  sampleCopy51Words = "The suite can reduce risk by providing the tooling for performance management of the cloud infrastructure and backup and restore of associated workload storage. It also provides automated provisioning as well as monitoring capability for improved Service Level Agreement (SLA) and policy focus. These IBM cloud capabilities offer the scalability and";
  sampleCopy52Words = "The suite can reduce risk by providing the tooling for performance management of the cloud infrastructure and backup and restore of associated workload storage. It also provides automated provisioning as well as monitoring capability for improved Service Level Agreement (SLA) and policy focus. These IBM cloud capabilities offer the scalability and functionality";

  toHTML = function ($elm) {
    var $wrapper = $('<div />');

    $wrapper.append($elm.clone());
    return $wrapper.html();
  };

  beforeEach(function () {
    $textbox = $('<textarea data-max-length-in-words="50" />');
    $(document.body).append($textbox);
  });

  afterEach(function () {
    $textbox.siblings('p').remove();
    $textbox.remove();
  });

  describe("When called", function () {
    it("Should add a paragraph below the textbox saying the number of words in it", function () {
      var $count = $countParagraph.text("50" + wordsWhenCountIs.none);

      GOVUK.GDM.wordCounter();
      expect($textbox.siblings('p.wordCount').length).toEqual(1);
      expect(toHTML($textbox.siblings('p.wordCount'))).toEqual(toHTML($count));
    });

    it("Should add an aria-controls attribute to the textbox linking it to the paragraph", function () {
      GOVUK.GDM.wordCounter();
      expect($textbox.attr('aria-controls')).toEqual('wordcount');
    });

    it("Should set the count to what's in the data-max-length-in-words attribute on the textbox", function () {
      $textbox.attr('data-max-length-in-words', '40');
      GOVUK.GDM.wordCounter();
      expect($textbox.siblings('p.wordCount').text()).toEqual('40 words remaining');
    });

    it("Should give the correct wordcount for a textbox that has content when the page loads", function () {
      $textbox.val('Words entered');
      GOVUK.GDM.wordCounter();
      expect($textbox.siblings('p.wordCount').text()).toEqual('48 words remaining');
    });
  });

  describe("When content is added to the textbox", function () {
    it("Should have the correct word count if some words are entered by keyup event", function () {
      GOVUK.GDM.wordCounter();
      $textbox.val('Word entered');
      $textbox.trigger('keyup');
      expect($textbox.siblings('p.wordCount').text()).toEqual('48 words remaining');
    });

    it("Should have the correct word count if some words are entered by paste event", function () {
      GOVUK.GDM.wordCounter();
      $textbox.val('Words entered');
      $textbox.trigger('paste');
      expect($textbox.siblings('p.wordCount').text()).toEqual('48 words remaining');
    });

    it("Should have the correct word count if some words are entered by change event", function () {
      GOVUK.GDM.wordCounter();
      $textbox.val('Words entered');
      $textbox.trigger('change');
      expect($textbox.siblings('p.wordCount').text()).toEqual('48 words remaining');
    });

    it("Should have the correct word count if a single word is entered", function () {
      GOVUK.GDM.wordCounter();
      $textbox.val('Word');
      $textbox.trigger('keyup');
      expect($textbox.siblings('p.wordCount').text()).toEqual('49 words remaining');
    });

    it("Should have the correct word count if 50 words are entered", function () {
      GOVUK.GDM.wordCounter();
      $textbox.val(sampleCopy50Words);
      $textbox.trigger('keyup');
      expect($textbox.siblings('p.wordCount').text()).toEqual('0 words remaining');
    });

    it("Should have the correct word count if 51 words are entered", function () {
      GOVUK.GDM.wordCounter();
      $textbox.val(sampleCopy51Words);
      $textbox.trigger('keyup');
      expect($textbox.siblings('p.wordCount').text()).toEqual('1 word too many');
    });

    it("Should have the correct word count if 52 words are entered", function () {
      GOVUK.GDM.wordCounter();
      $textbox.val(sampleCopy52Words);
      $textbox.trigger('keyup');
      expect($textbox.siblings('p.wordCount').text()).toEqual('2 words too many');
    });
  });
});