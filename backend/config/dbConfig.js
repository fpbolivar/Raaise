const { DB_URL } = require("./Config")
const mongoose = require('mongoose')
mongoose.connect(DB_URL).then(() => {
    console.log("Connection Sucessfull")
}).catch((err) => {
    console.log("Connection Failed", err)
})