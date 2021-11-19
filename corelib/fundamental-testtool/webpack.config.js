module.exports = {
	output: {
		filename: "[name]",
		path: __dirname + "/src/main/resources/static/testtool/javascript"
	},
	entry: {
		"invoker.js": "./src/main/javascript/invoker.js",
		"stubconfig.js": "./src/main/javascript/stubconfig.js"
	}
};
