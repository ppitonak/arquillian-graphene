module("Form Submission Interception");

(function() {

    var fsi = Graphene.formSubmissionInterception;

    // TESTS

    test("accessor object not null", function() {
        ok(fsi);
    });

    test("form can be submitted", function() {
        $()
        var form = document.getElementById("form1");
        var submitButton = $(document.getElementById("form1:submit"));
        window.form1Submitted = false;

        submitButton.click();
        ok(window.form1Submitted);
    });

    test("form is prevented to being submit by test", function() {
        // given
        var form = document.getElementById("form1");
        var submitButton = $(document.getElementById("form1:submit"));
        window.form1Submitted = false;

        var handler = function(e) {
            ok(e.target === form);
            return false;
        };

        $(document).bind('submit', handler);

        submitButton.click();
        ok(!window.form1Submitted);

        $(document).unbind('submit', handler);
    });

    test("form submission can be intercepted", function() {
        // given
        var form = document.getElementById("form1");
        var submitButton = $(document.getElementById("form1:submit"));
        window.form1Submitted = false;

        ok(!fsi.getSubmittedForm());

        // when
        fsi.inject()
        submitButton.click();

        // then
        ok(!window.form1Submitted);
        ok(fsi.getSubmittedForm() === form);

        // finally
        fsi.uninject();
    });

    test("when form submission is intercepted then it can be continued", function() {
        // given
        var form = document.getElementById("form1");
        var submitButton = $(document.getElementById("form1:submit"));
        window.form1Submitted = false;
        fsi.inject()
        submitButton.click();
        ok(!window.form1Submitted);

        // when
        fsi.submitForm();
        ok(window.form1Submitted);
        ok(!fsi.getSubmittedForm());

        // finally
        fsi.uninject();
    });

})();