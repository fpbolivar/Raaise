var jwt = require('jsonwebtoken');
const { SECRET_JWT_CODE, USER_JWT_CODE } = require("../config/Config")


const adminAuthJWT = (req, res, next) => {
  const authHeader = req.headers["authorization"];

  if (authHeader) {
    jwt.verify(authHeader, SECRET_JWT_CODE, async (err, admin) => {
      if (err) {
        res.status(401).send({
          status: 401,
          result: false,
          message: "Unauthorized: Invalid token"
        });

        return

      }
      req.admin = admin;
      next();
    });
  } else {
    res.status(401).send({
      status: 401,
      result: false,
      message: "Unauthorized: No token provided"
    });
    return
  }
};

const userAuthJWT = (req, res, next) => {
  const authHeader = req.headers["authorization"];
  if (authHeader) {
    jwt.verify(authHeader, USER_JWT_CODE, async (err, user) => {
      if (err) {
        res.status(401).send({
          status: 401,
          result: false,
          message: "Unauthorized: Invalid token"
        });

        return

      }
      req.user = user;
      next();
    });
  } else {
    res.status(401).send({
      status: 401,
      result: false,
      message: "Unauthorized: No token provided"
    });
    return
  }
};




module.exports = { adminAuthJWT, userAuthJWT }