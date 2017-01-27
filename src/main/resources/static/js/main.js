import SideNav from "./side-nav";
import CheckBoxes from "./checkboxes";
import AutoCompleteCourseTags from "./autocomplete-course-tags"
import AutoCompleteSubjects from "./autocomplete-subjects";
import AutoCompleteLecturerForSubjects from "./autocomplete-lecturer-for-subject";
import Select from "./select";
import Modals from "./modals";
import LanguageSwitcher from "./language-switcher";

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

    const autoCompleteCourseTags = new AutoCompleteCourseTags();
    autoCompleteCourseTags.initialize();

    const autoCompleteSubjects = new AutoCompleteSubjects();
    autoCompleteSubjects.initialize();

    const $wrapper = $('.autocomplete-wrapper');

    const autocompleteLecturersToSubjects = new AutoCompleteLecturerForSubjects($wrapper);
    autocompleteLecturersToSubjects.initialize();

    const select = new Select();
    select.initialize('select');

    const modals = new Modals();
    modals.initialize();

    const languageSwitcher = new LanguageSwitcher();
    languageSwitcher.init("[data-action~='switch-language']");
};

init();

// Exports for calls from HTML
window.showMessage = showMessage;
