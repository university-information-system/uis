import SideNav from "./side-nav";
import CheckBoxes from "./checkboxes";
import AutoCompleteSubjects from "./autocomplete-subjects";
import AutoCompleteLecturerForSubjects from "./autocomplete-lecturer-for-subject";
import Select from "./select";

// Flash Messages
const showMessage = (message) => {
    "use strict";

    setTimeout(() => {
        Materialize.toast(message, 5000);
    }, 1000);
};

const init = () => {
    "use strict";

    const checkboxes = new CheckBoxes();
    checkboxes.initialize();

    const sideNav = new SideNav();
    sideNav.initialize();

    const autoCompleteSubjects = new AutoCompleteSubjects();
    autoCompleteSubjects.initialize();

    const $wrapper = $('.autocomplete-wrapper');

    const autocompleteLecturersToSubjects = new AutoCompleteLecturerForSubjects($wrapper);
    autocompleteLecturersToSubjects.initialize();

    const select = new Select();
    select.initialize('select');
};

init();

// Exports for calls from HTML
window.showMessage = showMessage;
