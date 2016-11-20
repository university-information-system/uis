export default class AutoComplete {

    initialize() {
        $( function() {
            $(document).ready(function() {
                $('select').material_select();
            });

            $('#mandatory-autocomplete-input').materialize_autocomplete({
                multiple: {
                    enable: false
                },
                dropdown: {
                    el: '#singleDropdownMandatory',
                    itemTemplate: '<li class="ac-item" data-id="<%= item.id %>" data-text=\'<%= item.name %>\'><a href="javascript:void(0)"><%= item.name %></a></li>'
                },
                getData: function (value, callback) {
                    $.ajax({
                        url: "/admin/studyplans/json/availableSubjects",
                        data: { query: value },
                        type: 'GET',
                        success: function (data) {
                            callback(value, data);
                        }
                    });
                },
                onSelect: function (item) {
                    $('#mandatorySubjectId').val(item.id);
                }
            });

            $('#optional-autocomplete-input').materialize_autocomplete({
                multiple: {
                    enable: false
                },
                dropdown: {
                    el: '#singleDropdownOptional',
                    itemTemplate: '<li class="ac-item" data-id="<%= item.id %>" data-text=\'<%= item.name %>\'><a href="javascript:void(0)"><%= item.name %></a></li>'
                },
                getData: function (value, callback) {
                    $.ajax({
                        url: "/admin/studyplans/json/availableSubjects",
                        data: { query: value },
                        type: 'GET',
                        success: function (data) {
                            callback(value, data);
                        }
                    });
                },
                onSelect: function (item) {
                    $('#optionalSubjectId').val(item.id);
                }
            });
        });
    }
}