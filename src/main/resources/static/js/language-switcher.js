
// String.prototype.startswith polyfill
// https://developer.mozilla.org/en/docs/Web/JavaScript/Reference/Global_Objects/String/startsWith
if (!String.prototype.startsWith) {
    String.prototype.startsWith = function(searchString, position) {
        position = position || 0;
        return this.substr(position, searchString.length) === searchString;
    };
}


export default class LanguageSwitcher {

    redirectWithGetParam(paramName, paramValue) {
        const param = `${paramName}=${paramValue}`;

        const params = window.location.search.split(/[&?]/)
            .filter(param => param)
            .filter(param => ! param.startsWith(`${paramName}=`));

        console.log("params")

        params.push(param);
        const query = "?" + params.join("&");

        console.log("params:", params, "query:", query);

        window.location.search = query;
    }

    switchLanguage(language) {
        const langParam = `lang=${language}`;
        this.redirectWithGetParam("lang", language);
    }

    init(selector) {
        const $element = $(selector);
        const that = this;

        $element.on("click", function(evt) {
            evt.preventDefault();

            const language = $(this).data("language");
            that.switchLanguage(language);
        });
    }
};

