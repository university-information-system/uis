export default class AutoCompleteCourseTags {

    initialize() {
        $( function() {

            if($('#tags').length) {

                if($('#courseIdForTags').length) {
                    $.ajax({
                        url: "/lecturer/courses/json/tags",
                        data: { courseId: $('#courseIdForTags').val() },
                        type: 'GET',
                        success: function (data) {
                            for(var i=0; i< data.length; i++) {
                                $('#tags').materialtags('add', data[i]);
                            }
                            $('#tagsData').val($('#tags').materialtags()[0].itemsArray);
                        }
                    });
                }

                var allTags = new Bloodhound({
                    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('name'),
                    queryTokenizer: Bloodhound.tokenizers.whitespace,
                    prefetch: {
                        url: '/lecturer/courses/json/tags',
                        cache: false,
                        filter: function(list) {
                            return $.map(list, function(name) {
                                return { name: name };
                            });
                        }
                    }
                });
                allTags.initialize();

                $('#tags').materialtags({
                    freeInput: true,
                    typeaheadjs: {
                        name: 'allTags',
                        displayKey: 'name',
                        valueKey: 'name',
                        source: allTags.ttAdapter()
                    }
                });

                $('#tags').on('itemAdded', function(event) {
                    $('#tagsData').val($('#tags').materialtags()[0].itemsArray);
                });

                $('#tags').on('itemRemoved', function(event) {
                    $('#tagsData').val($('#tags').materialtags()[0].itemsArray);
                });

                // clear input after pressing enter
                $('.n-tag.tt-input').keyup(function(e){
                    if(e.keyCode == 13) {
                        $('.n-tag.tt-input').val('');
                        $('.n-tag.tt-input').typeahead('val', '');
                    }
                });

            }

        });

    };


}