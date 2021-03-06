var path = require("path");
var webpack = require("webpack");

var cfg = {
    devtool: "source-map",
    entry: ['./src/index.js'],
    output: {
        path: path.resolve("./out"),
        filename: "bundle.js"
    },
    module: {
        preLoaders: [
            { test: /\.js$/, exclude: /node_modules/, loader: "source-map-loader" }
        ],
        loaders: [
            { test: /\.ts$/, exclude: /node_modules/, loader: 'ts-loader' },
            {
                test: /\.jsx?$/,
                exclude: /node_modules|target/,
                loader: 'babel-loader',
                query: { cacheDirectory: true, presets: ['es2015'] },
                plugins: ['transform-runtime']
            },
            { test: /\.styl$/, exclude: /node_modules/, loader: 'style-loader!css-loader!stylus-loader' },
            { test: /\.css$/, loader: 'style-loader!css-loader' },
            { test: /\.eot(\?v=\d+\.\d+\.\d+)?$/, loader: "file" },
            { test: /\.(woff|woff2)$/, loader: "url?prefix=font/&limit=5000" },
            { test: /\.ttf(\?v=\d+\.\d+\.\d+)?$/, loader: "url?limit=10000&mimetype=application/octet-stream" },
            { test: /\.svg(\?v=\d+\.\d+\.\d+)?$/, loader: "url?limit=10000&mimetype=image/svg+xml" },
            { test: require.resolve("jquery"), loader: "imports?jQuery=jquery" },
            { test: /\.png$/, exclude: /node_modules/, loader: "url?limit=10000&mimetype=image/png" }
        ]
    },
    plugins: [
        new webpack.ProvidePlugin({
            "$": "jquery", 
            "jQuery": "jquery"
        })
    ],
    resolve: {
        alias: {
            "Mazigeen": "mazigeen-fastopt"
        },
        extensions: ['', '.ts', '.js', '.jsx'],
        root: [
            path.resolve('.'),
            path.resolve('./src/js'),
            path.resolve('./src/'),
            path.resolve('./target/scala-2.11/'),
        ]
    }
};

module.exports = cfg;
