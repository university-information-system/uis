
/**
 * the label must be moved because materialize.css expects the input and label
 * to be one after the other and spring mvc adds an extra hidden input field
 */
export default class CheckBoxes {

    initialize() {
        $('.checkboxLabel').each(function() {
            if(this.previousElementSibling) {
                this.parentNode.insertBefore(this, this.previousElementSibling);
            }
        });
    }
}