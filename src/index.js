require("babel-polyfill");
require("bootstrap-webpack");

require("file?name=index.html!./html/index.html");
require("file?name=favicon.png!./img/favicon.png");

require("./css/style.styl");

require("Mazigeen");

global.jQuery = require("jquery");
global.mazigeen.App().run();
