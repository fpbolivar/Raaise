const c = require("../config/Config");
const wallet = require("../lib/wallet");
const Stripe = require("stripe")
const stripe = Stripe(c.STRIPE.SK_dev);
const services = require("../services/services");
const moment = require("moment");
const { validatorErrorResponse, sendError, sripeErrorHandling } = require("../utils/universalFunction");


const getCards = async (req, res) => {
    try {
        const { userId } = req.user;
        const user = await services.getoneData(
            "user",
            { _id: userId }
        );
        let cards = [];
        let cardData = await stripe.customers.listSources(
            user.customerId,
            { 'object': 'card' }
        );
        for (let i = 0; i < cardData.data.length; i++) {
            cards.push({
                id: cardData.data[i]['id'],
                object: cardData.data[i]['object'],
                brand: cardData.data[i]['brand'],
                country: cardData.data[i]['country'],
                funding: cardData.data[i]['funding'],
                last4: cardData.data[i]['last4'],
            });
        }
        return res.status(200).send({
            status: 200,
            message: "success",
            user: user.customerId,
            cards: cards
        });
    } catch (error) {
        if (error) {
            sendError(error, res);
        }
    }
};

const deleteCards = async (req, res) => {
    try {
        const { userId } = req.user;
        const { cardId } = req.body;
        if (!cardId) {
            return await validatorErrorResponse(res, "cardId required", "cardId");
        }
        const user = await services.getoneData(
            "user",
            { _id: userId }
        );
        const cardDelete = await stripe.customers.deleteSource(
            user.customerId,
            cardId
        );
        if (cardDelete.deleted) {
            return res.status(200).send({
                status: 200,
                message: "card deleted successfully"
            });
        } else {
            return res.status(400).send({
                status: 400,
                message: "something went wrong"
            });
        }
    } catch (error) {
        if (error && error.type == 'StripeInvalidRequestError') {
            return res.status(500).send({
                status: 500,
                message: error.message
            });
        } else if (error) {
            sendError(error, res);
        }
    }
};

/**
 *  PAYMENT BY VIEWER TO ADMIN
 */
const makePayment = async (req, res) => {
    try {
        const { userId } = req.user;
        const { token, amount, donateTo, videoId } = req.body;
        // handle validation
        if (!token) {
            return validatorErrorResponse(
                res,
                "required",
                "token"
            );
        }
        // get user for check customer id
        const user = await services.getoneData(
            "user",
            { _id: userId }
        );
        // get payment setting
        const paymentSettings = await services.getoneData(
            "paymentSetting"
        );
        // calculate admin amount
        const adminAmount = (amount * paymentSettings.adminCommision) / 100;
        // calculate user amount
        const userAmount = amount - adminAmount;
        let customerId = user.customerId;
        if (!customerId) {
            // create stripe customer
            const customer = await stripe.customers.create({
                description: user.userName || "customer created",
            });
            customerId = customer.id
            await services.updateData(
                "user",
                {
                    _id: userId,
                },
                {
                    customerId: customerId,
                }
            );
        }

        // get cards data
        let cardData = await stripe.customers.listSources(
            customerId,
            { 'object': 'card' }
        );

        // get token
        const tokens = await stripe.tokens.retrieve(
            token
        );

        let card = null;
        // check if card is already added
        if (cardData.data) {
            let sources = cardData.data;
            for (let i = 0; i < sources.length; i++) {
                if (sources[i]['fingerprint'] == tokens.card.fingerprint) {
                    card = sources[i];
                }
            }
        }

        if (!card) {
            // create source
            card = await stripe.customers.createSource(customerId, { 'source': tokens.id });
        }
        // update default card
        const updatecustomer = await stripe.customers.update(customerId, { 'default_source': card.id });
        if (updatecustomer.default_source) {
            // update create charge
            const charge = await stripe.charges.create({
                amount: amount * 100,
                currency: 'usd',
                // source: token,
                customer: customerId,
                description: ''
            });
            if (charge.id) {
                // create txn
                await services.InsertData("transaction", {
                    userId: donateTo,
                    donatedBy: userId,
                    status: charge.status,
                    transactionId: charge.id,
                    amount: amount,
                    videoId: videoId
                });
                if (charge.status == "succeeded") {
                    // add to wallet
                    await services.InsertData("wallet", {
                        userId: donateTo,
                        donatedBy: userId,
                        status: "credit",
                        adminAmount: adminAmount,
                        userAmount: userAmount,
                        amount: amount,
                        videoId: videoId
                    });
                    // update donate to wallet to user
                    await wallet.updateUserWallet(donateTo);
                    return res.status(200).send({
                        status: 200,
                        message: "payment success."
                    });
                }
            }
        }
        return res.status(400).send({
            status: 400,
            message: "something went wrong",
        });
    } catch (error) {
        console.log(error)
        if (error) {
            sendError(error, res);
        }
    }
};

// add update bank account
const addUpdateBankDetails = async (req, res) => {
    const { accountId, routingNumber, bankPhone, address, city, state, postalCode } = req.body;
    if (!accountId) {
        return await validatorErrorResponse(res, "accountId required", "accountId");
    }
    if (!routingNumber) {
        return await validatorErrorResponse(res, "routingNumber required", "routingNumber");
    }
    if (!bankPhone) {
        return await validatorErrorResponse(res, "bankPhone required", "bankPhone");
    }
    if (!address) {
        return await validatorErrorResponse(res, "address required", "address");
    }
    if (!city) {
        return await validatorErrorResponse(res, "city required", "city");
    }
    if (!state) {
        return await validatorErrorResponse(res, "state required", "state");
    }
    if (!postalCode) {
        return await validatorErrorResponse(res, "postalCode required", "postalCode");
    }
    try {
        const { userId } = req.user;
        const modelName = "user";
        const user = await services.getoneData(modelName, { _id: userId }, { password: false });
        const ipA = require("ip");
        const ip = ipA.address();
        const country = "US";
        let addressArray = {
            city: city,
            country: country,
            line1: address,
            line2: address,
            postal_code: postalCode,
            state: state,
        };
        let extAccount = {
            object: "bank_account",
            country: country,
            currency: "USD",
            routing_number: routingNumber,
            account_number: accountId
        };
        let account = {};
        // check and create
        if (!user.stripeAccountId) {
            account = await stripe.accounts.create({
                type: "custom",
                country: "US",
                email: user.email,
                capabilities: {
                    card_payments: { requested: true },
                    transfers: { requested: true },
                },
                external_account: extAccount,
                business_type: "individual",
                individual: {
                    address: addressArray,
                    dob: {
                        day: Math.floor(
                            Math.random() * (30 - 1 + 1) + 1
                        ),
                        month: Math.floor(
                            Math.random() * (12 - 1 + 1) + 1
                        ),
                        year: Math.floor(
                            Math.random() * (1990 - 1900 + 1) + 1900
                        ),
                    },
                    email: user.email,
                    first_name: user.name,
                    last_name: user.name,
                    phone: bankPhone,
                    political_exposure: "none",
                    ssn_last_4: "0000",
                    id_number: "000000000",
                },
                business_profile: {
                    name: user.name,
                    support_email: user.email,
                    support_phone: bankPhone,
                    url: "https://net-vest.com/",
                    mcc: "5399",
                    support_address: addressArray,
                },
                tos_acceptance: {
                    date: moment().unix(),
                    ip: ip,
                },
            });
            // update bank account
        } else {
            account = await stripe.accounts.update(user.stripeAccountId, {
                email: user.email,
                capabilities: {
                    card_payments: { requested: true },
                    transfers: { requested: true },
                },
                external_account: extAccount,
                business_type: "individual",
                individual: {
                    address: addressArray,
                    dob: {
                        day: Math.floor(
                            Math.random() * (30 - 1 + 1) + 1
                        ),
                        month: Math.floor(
                            Math.random() * (12 - 1 + 1) + 1
                        ),
                        year: Math.floor(
                            Math.random() * (1990 - 1900 + 1) + 1900
                        ),
                    },
                    email: user.email,
                    first_name: user.name,
                    last_name: user.name,
                    phone: bankPhone,
                    political_exposure: "none",
                    ssn_last_4: "0000",
                    id_number: "000000000",
                },
                business_profile: {
                    name: user.name,
                    support_email: user.email,
                    support_phone: bankPhone,
                    url: "https://scriptube.com/",
                    mcc: "5399",
                    support_address: addressArray,
                },
                tos_acceptance: {
                    date: moment().unix(),
                    ip: ip,
                },
            });
        }
        if (account.id) {
            const updateData = {
                stripeAccountId: account.id,
                accountId: accountId,
                routingNumber: routingNumber,
                bankPhone: bankPhone,
                address: address,
                city: city,
                state: state,
                postalCode: postalCode,
                isVerified: false
            };
            await services.updateData(
                modelName,
                { _id: userId },
                updateData,
                { new: true }
            );
            return res.status(200).send({
                status: 200,
                message: "bank added successfully"
            });
        }
        return res.status(400).send({
            status: 400,
            message: "bank account cannot be added please try again later."
        });
    } catch (error) {
        console.log(error)
        if (error) {
            sripeErrorHandling(error, res);
        }
    }
};

/**
 * send payemnt request to admin
 */
const sendRequestToPayment = async (req, res) => {
    try {
        const { userId } = req.user
        const { amount } = req.body
        // get user for checking wallet
        const user = await services.getoneData("user", { _id: userId }, { password: false });
        if (amount <= user.walletCreditAmount) { // check wallet amount is available or not
            await services.InsertData("paymentRequest", {
                userId: userId,
                status: "pending",
                amount: amount
            });
            return res.status(200).send({
                status: 200,
                message: "success"
            });
        } else {
            return res.status(200).send({
                status: 200,
                message: "You don't have infucent balance"
            });
        }
    } catch (error) {
        if (error) {
            sendError(error, res);
        }
    }
}

module.exports = {
    makePayment,
    addUpdateBankDetails,
    sendRequestToPayment,
    getCards,
    deleteCards
}