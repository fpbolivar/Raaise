const services = require("../services/services");
const c = require("../config/Config");
const _ = require("lodash");  
const Stripe = require("stripe")
const stripe = Stripe(c.STRIPE.SK_dev);

const updateUserWallet = async(userId) => {
    const userWallet = await services.getData("wallet",{ userId: userId },{userAmount: 1,status: 1});
	const userTotalCreditAmount = _.sumBy(userWallet, function(o) { 
		if(o.status == "credit"){
			return parseFloat(o.userAmount);
		}
		return 0;
	});
	const userDebitAmount = _.sumBy(userWallet, function(o) { 
		if(o.status == "debit"){
			return parseFloat(o.userAmount);
		}
		return 0;
	});

	// update user credit/pending and debited wallet amount
	const userPendingAmount = userTotalCreditAmount - userDebitAmount;
	await services.updateData("user",{ _id: userId },
		{walletCreditAmount: userPendingAmount.toFixed(2),walletDebitAmount: userDebitAmount.toFixed(2)},
		{ new: true }
	);
	return true
}

// for cronjob
const verifyBankAccounts  = async () => {
	const users = await services.getData("user",{ isVerified: false });
	if (users.length) {
		for (let i = 0; i < users.length; i++) {
			const user = users[i];
			if(user.stripeAccountId){
				const account = await stripe.accounts.retrieve(user.stripeAccountId);
				if(account.requirements.errors.length > 0){
					let errors = account.requirements.errors.filter(function(item){return item.code == "invalid_address_city_state_postal_code";});
					if(!errors.length){
						errors = account.requirements.errors.filter(function(item){return item.code == "invalid_street_address";});
					}
					if(errors.length){
						await services.updateData("user",{ _id: user._id },
							{donation_comment: errors[0].reason,isVerified : false},
							{ new: true }
						);
					}
				}else{
					await services.updateData("user",{ _id: user._id },
						{donation_comment: "",isVerified : true},
						{ new: true }
					);
				}
			}
		}
	}
    console.log("user","success");
}


module.exports = {
	updateUserWallet,
	verifyBankAccounts
}