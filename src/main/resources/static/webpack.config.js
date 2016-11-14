
const ExtractTextPlugin = require("extract-text-webpack-plugin");
const path = require("path");

const extractScss = new ExtractTextPlugin("css/style.css");

module.exports = {
    entry: [
        "babel-polyfill",
        "./js/main.js",
        "./scss/style.scss",
    ],
    output: {
        path: "./min",
        filename: "js/main.js",
    },
    module: {
        loaders: [
            {
                loader: "babel-loader",
                include: [
                    path.resolve(__dirname, "js"),
                ],
                exclude: [
                    path.resolve(__dirname, "node_modules"),
                ],
                test: /\.js$/i,
                query: {
                    plugins: ["transform-runtime"],
                    presets: ["es2015", "stage-0"],
                }

            },
            {
                test: /\.scss$/i,
                loader: extractScss.extract(["css", "sass"]),
            }
        ],
    },
    plugins: [
        extractScss,
    ],
};
