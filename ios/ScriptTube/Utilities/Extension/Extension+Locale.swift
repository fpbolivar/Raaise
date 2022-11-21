//
//  Extension+Locale.swift
//  FraudsterFresh
//
//  Created by Dilpreet Singh on 10/6/22.
//

import Foundation
extension Locale {
    func localizedCurrencySymbol(forCurrencyCode currencyCode: String) -> String? {
        guard let languageCode = languageCode, let regionCode = regionCode else { return nil }

        /*
         Each currency can have a symbol ($, £, ¥),
         but those symbols may be shared with other currencies.
         For example, in Canadian and American locales,
         the $ symbol on its own implicitly represents CAD and USD, respectively.
         Including the language and region here ensures that
         USD is represented as $ in America and US$ in Canada.
         */
        let components: [String: String] = [
            NSLocale.Key.languageCode.rawValue: languageCode,
            NSLocale.Key.countryCode.rawValue: regionCode,
            NSLocale.Key.currencyCode.rawValue: currencyCode,
        ]

        let identifier = Locale.identifier(fromComponents: components)

        return Locale(identifier: identifier).currencySymbol
    }
}

//Locale.isoCurrencyCodes.compactMap { (code) -> String? in
//    guard let name = Locale.current.localizedString(forCurrencyCode: code),
//          let symbol = Locale.current.localizedCurrencySymbol(forCurrencyCode: code)
//    else {
//        return nil
//    }
//
//    return "\(name) - \(symbol) (\(code))"
//}
