const express = require('express')
const app = express()
const port = 3000
var bodyParser = require('body-parser')
var cors = require('cors')
app.use(cors())
app.use(bodyParser.json())
const admin = require('./router/admimRouters')
const user = require("./router/userRouters")
require('./config/dbConfig')
const fileupload = require("express-fileupload");

app.use(fileupload());

// define router
app.use('/admin', admin)
app.use('/user', user)

app.get('/', (req, res) => {
  res.send({ message: 'scrip tube project ' })

})

app.listen(port, () => {
  console.log(`Server listening on port ${port}`)
})