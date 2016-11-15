
import SideNav from "./side-nav";
import CheckBoxes from "./checkboxes";

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
};

init();

// Exports for calls from HTML
window.showMessage = showMessage;