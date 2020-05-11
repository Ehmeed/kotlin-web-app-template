config.devServer = config.devServer || {}; // create devServer in case it is undefined
config.devServer.watchOptions = {
    "aggregateTimeout": 5000,
    "poll": 1000
};
config.devServer.host = '0.0.0.0';
