module("Form Submission Interception");

(function() {

    var intr = Graphene.formSubmissionInterception;
    
    // TESTS
    
    test("accessor object not null", function() {
        ok(intr);
    });
    
})();