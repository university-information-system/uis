const main = {

    initialize: function() {
        $(document).ready(function(){
            $('.button-collapse').sideNav();
            moveLabelsForCheckboxes();
        })
    }
};

function moveLabelsForCheckboxes() {  //the label must be moved because materialize.css expects the input and label to be one after the other and spring mvc adds an extra hidden input field
    $('.checkboxLabel').each(function () {
        if(this.previousElementSibling)
            this.parentNode.insertBefore(this, this.previousElementSibling);
    })
}