export default class AutoComplete {

    initialize() {
        $( function() {
            $(document).ready(function() {
                $('select').material_select();
            });
            $( "#mandatory-autocomplete-input").autocomplete({
                source: function (request, response) {
                    $.ajax({
                        url: "/admin/studyplans/json/availableSubjects",
                        data: { query: request.term },
                        type: 'GET',
                        success: function (data) {
                            response($.map(data, function (subject) {
                                console.log(subject);
                                return {
                                    label: subject.name + " (" + subject.ects + " ECTS)",
                                    value: JSON.stringify(subject)
                                }
                            }));
                        }
                    })
                },
                select: function (event, ui) {
                    $('#mandatory-autocomplete-input').val(ui.item.label);
                    $('#mandatorySubjectJson').val(ui.item.value);
                    return false;
                },
                minLength: 2
            });
            $( "#optional-autocomplete-input").autocomplete({
                source: function (request, response) {
                    $.ajax({
                        url: "/admin/studyplans/json/availableSubjects",
                        data: { query: request.term },
                        type: 'GET',
                        success: function (data) {
                            response($.map(data, function (subject) {
                                console.log(subject);
                                return {
                                    label: subject.name + " (" + subject.ects + " ECTS)",
                                    value: JSON.stringify(subject)
                                }
                            }));
                        }
                    })
                },
                select: function (event, ui) {
                    $('#optional-autocomplete-input').val(ui.item.label);
                    $('#optionalSubjectJson').val(ui.item.value);
                    return false;
                },
                minLength: 2
            });
        });
    }
}