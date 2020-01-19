module.exports = {
	output: {
		filename: "[name]",
		path: __dirname + "/src/main/resources/static/javascript"
	},
	entry: {
		"testtool/invoker.js": "./src/main/javascript/testtool/invoker.js",
		"testtool/stubconfig.js": "./src/main/javascript/testtool/stubconfig.js"
	}
};
