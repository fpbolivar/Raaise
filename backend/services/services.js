const Models = require('../models')

var InsertData = function (modelName, objToSave) {

    return new Promise((resolve, reject) => {
        new Models[modelName](objToSave).save().then(data => {
            return resolve(data);
        }).catch(err => {
            return reject(err);
        })
    })
};

var getData = function (modelName, criteria, projection, options) {

    return new Promise((resolve, reject) => {
        Models[modelName].find(criteria, projection, options).then(data => {
            return resolve(data)
        }).catch(err => {
            return reject(err);
        })

    })
}

var getoneData = function (modelName, finddata, projection, options) {

    return new Promise((resolve, reject) => {
        Models[modelName].findOne(finddata, projection, options).then(data => {
            return resolve(data)
        }).catch(err => {
            return reject(err);
        })

    })
}


var updateData = function (modelName, criteria, dataToSet, options) {
    return new Promise((resolve, reject) => {
        Models[modelName].findOneAndUpdate(criteria, dataToSet, options).then(data => {
            return resolve(JSON.parse(JSON.stringify(data)));
        }).catch(err => {
            return reject(err);
        })
    })
};

var findWithPopulate = function (modelName, criteria, projection, options, populate) {
    return new Promise((resolve, reject) => {
        Models[modelName].find(criteria, projection, options).populate(populate)
            .then(data => {
                return resolve(JSON.parse(JSON.stringify(data)));
            }).catch(err => {
                return reject(err);
            })
    })
}

let dropIndex = (modelName, criteria) => {
    return new Promise((resolve, reject) => {
        Models[modelName].deleteMany(criteria).then(() => {
            return resolve();
        }).catch(err => {
            return reject(err);
        })
    })
};

var findOneWithPopulate = function (modelName, criteria, projection, options, populate) {
    return new Promise((resolve, reject) => {
        Models[modelName].findOne(criteria, projection, options).populate(populate)
            .then(data => {
                return resolve(JSON.parse(JSON.stringify(data)));
            }).catch(err => {
                return reject(err);
            })
    })
}

var updateMany = function (modelName, criteria, dataToSet, options) {
    return new Promise((resolve, reject) => {
        Models[modelName].updateMany(criteria, dataToSet, options).then(data => {
            return resolve(JSON.parse(JSON.stringify(data)));
        }).catch(err => {
            return reject(err);
        })
    })
};






module.exports = {
    InsertData: InsertData,
    getData: getData,
    updateData: updateData,
    findWithPopulate: findWithPopulate,
    getoneData: getoneData,
    dropIndex: dropIndex,
    findOneWithPopulate: findOneWithPopulate,
    updateMany:updateMany
};