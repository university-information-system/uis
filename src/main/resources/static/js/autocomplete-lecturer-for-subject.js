export default class AutoCompleteSubjects {

    constructor(wrapper) {
        this.wrapper = wrapper;
    }

    initialize(selector) {
        const $input = this.wrapper.find('input[type=text]');
        const $realInput = this.wrapper.find('input[type=hidden]');
        const jsonUrl = this.wrapper.data("url");

        $input.materialize_autocomplete({
            multiple: {
                enable: false
            },
            dropdown: {
                el: '.dropdown-content',
                itemTemplate: '<li class="ac-item" data-id="<%= item.id %>" data-text=\'<%= item.name %>\'><a href="javascript:void(0)"><%= item.name %></a></li>',
            },
            getData: function (value, callback) {
                const url = jsonUrl + `?search=${value}`;

                $.ajax({
                   url: url,
                })
                .then(data => {
                    callback(value, data);
                });
            },
            onSelect: function (item) {
                $realInput.val(item.id);
            },
        });

    }
}