//
//  Extension+Date.swift
//  FraudsterFresh
//
//  Created by Dilpreet Singh on 8/8/22.
//

import Foundation
extension Date {
    func timeAgoDisplay() -> String {
        let formatter = RelativeDateTimeFormatter()
        formatter.unitsStyle = .full
        return formatter.localizedString(for: self, relativeTo: Date())
    }
}
extension Calendar {
    static let iso8601 = Calendar(identifier: .iso8601)
    static let gregorian = Calendar(identifier: .gregorian)
}


extension Date {
    var isDateInWeekend: Bool {
        return Calendar.iso8601.isDateInWeekend(self)
    }
    var tomorrow: Date {
        return Calendar.iso8601.date(byAdding: .day, value: 1, to: noon)!
    }
    var noon: Date {
        return Calendar.iso8601.date(bySettingHour: 12, minute: 0, second: 0, of: self)!
    }
}

extension Formatter {
    static let ddMMyyyy: DateFormatter = {
        let dateFormatter = DateFormatter()
        dateFormatter.calendar = Calendar(identifier: .iso8601)
        dateFormatter.locale = .init(identifier: "en_US_POSIX")
        dateFormatter.dateFormat = "dd.MM.yyyy"
        return dateFormatter
    }()
    static let yyyyMMdd: DateFormatter = {
        let dateFormatter = DateFormatter()
        dateFormatter.calendar = Calendar(identifier: .iso8601)
        dateFormatter.locale = .init(identifier: "en_US_POSIX")
        dateFormatter.dateFormat = "yyyy-MM-dd"
        return dateFormatter
    }()
    static let ddMMMyyyy: DateFormatter = {
        let dateFormatter = DateFormatter()
        dateFormatter.calendar = Calendar(identifier: .iso8601)
        dateFormatter.locale = .init(identifier: "en_US_POSIX")
        dateFormatter.dateFormat = "dd-MMM-yyyy"
        return dateFormatter
    }()
    static let HHMM: DateFormatter = {
        let dateFormatter = DateFormatter()
        dateFormatter.calendar = Calendar(identifier: .iso8601)
        dateFormatter.locale = .init(identifier: "en_US_POSIX")
        dateFormatter.dateFormat = "HH:mm"
        return dateFormatter
    }()
    static let hh_mm_AM_PM: DateFormatter = {
        let dateFormatter = DateFormatter()
        dateFormatter.calendar = Calendar(identifier: .iso8601)
        dateFormatter.locale = .init(identifier: "en_US_POSIX")
        dateFormatter.dateFormat = "h:mm a"
        return dateFormatter
    }()
    static let MMMddyyyy: DateFormatter = {
        let dateFormatter = DateFormatter()
        dateFormatter.calendar = Calendar(identifier: .iso8601)
        dateFormatter.locale = .init(identifier: "en_US_POSIX")
        dateFormatter.timeZone = TimeZone(secondsFromGMT: 0)
        dateFormatter.dateFormat = "MMM dd,yyyy"
        return dateFormatter
    }()
    static let MMMddyyyyHHMM: DateFormatter = {
        let dateFormatter = DateFormatter()
        dateFormatter.calendar = Calendar(identifier: .iso8601)
        dateFormatter.locale = .init(identifier: "en_US_POSIX")
        dateFormatter.dateFormat = "MMM dd,yyyy HH:mm"
        return dateFormatter
    }()
}
extension String{
    var date :Date {

        let formatter = DateFormatter()
        
        formatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
        
        formatter.timeZone = TimeZone(secondsFromGMT: 0)

        let someDateTime = formatter.date(from: self)
        return someDateTime!
    }
    var dateTZZ :Date? {

        let formatter = DateFormatter()
        
        formatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ssZ"
        
        formatter.timeZone = TimeZone(secondsFromGMT: 0)

        let someDateTime = formatter.date(from: self)
        return someDateTime
    }
    var timeAMPM :Date? {

        let formatter = DateFormatter()
       
        formatter.dateFormat = "h:mm a"
        
        formatter.timeZone = TimeZone(secondsFromGMT: 0)

        let someDateTime = formatter.date(from: self)
        return someDateTime
    }
    var dateWithoutT :Date? {

        let formatter = DateFormatter()
        
        formatter.dateFormat = "yyyy-MM-dd HH:mm:ss.SSSSSSZ"
        
        formatter.timeZone = TimeZone(secondsFromGMT: 0)

        let someDateTime = formatter.date(from: self)
        return someDateTime
    }

    var dateTZ :Date {

        let formatter = DateFormatter()
        
        formatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
        
        formatter.timeZone = .current
        let someDateTime = formatter.date(from: self)
        return someDateTime!
    }

}
extension Date {

    var MMMddyyyyHHMM: String { Formatter.MMMddyyyyHHMM.string(from: self) }
    var ddMMyyyy: String { Formatter.ddMMyyyy.string(from: self) }
    var MMMddyyyy: String { Formatter.MMMddyyyy.string(from: self) }
    var yyyyMMdd: String { Formatter.yyyyMMdd.string(from: self) }
    var yyyyMMddTimeSlot: String {


        let srDt = Formatter.yyyyMMdd.string(from: self)

        let dateFormatterGet = DateFormatter()
        dateFormatterGet.dateFormat = "yyyy-MM-dd"

        let data =  srDt.split(separator: "-")
        if data.count == 3{
            let timeSlot = "\(data[0])-\(Int(data[1]) ?? 0)-\(data[2])"
            return timeSlot
        }else{
            return srDt
        }
        
    }
    var ddMMMyyyy: String { Formatter.ddMMMyyyy.string(from: self) }
    var HHMM: String { Formatter.HHMM.string(from: self) }
    var hh_mm_AM_PM: String { Formatter.hh_mm_AM_PM.string(from: self) }


    static func dateConvertInto_specific_Format(dateText:String,currentformat:String,newformat:String)->String?{

        let dateFormatterGet = DateFormatter()
        dateFormatterGet.dateFormat = currentformat

        let dateFormatterPrint = DateFormatter()
        dateFormatterPrint.dateFormat = newformat
        if let date = dateFormatterGet.date(from: dateText) {
            print(dateFormatterPrint.string(from: date))

            return (dateFormatterPrint.string(from: date))
        } else {
            print("There was an error decoding the string")
        }
        return nil

    }

    static func dateConvertInto_specific_Format(date:Date,currentformat:String,newformat:String)->String?{

        let dateFormatterGet = DateFormatter()
        dateFormatterGet.dateFormat = currentformat
        dateFormatterGet.locale = Locale(identifier: "en_US_POSIX")
        dateFormatterGet.timeZone = TimeZone(secondsFromGMT: 0)
        let dateFormatterPrint = DateFormatter()
        dateFormatterPrint.dateFormat = newformat
        dateFormatterPrint.locale = Locale(identifier: "en_US_POSIX")
        dateFormatterPrint.timeZone = TimeZone(secondsFromGMT: 0)
        print(dateFormatterPrint.string(from: date))
        return (dateFormatterPrint.string(from: date))


    }
}
extension Date{
    func localDate() -> Date {
        let nowUTC = self
        let timeZoneOffset = Double(TimeZone.current.secondsFromGMT(for: nowUTC))
        guard let localDate = Calendar.current.date(byAdding: .second, value: Int(timeZoneOffset), to: nowUTC) else {return Date()}

        return localDate
    }

}
extension Date {
    func convertToUTC(dateToConvert:String) -> String {
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
        let convertedDate = formatter.date(from: dateToConvert)
        formatter.timeZone = TimeZone(identifier: "UTC")
        return formatter.string(from: convertedDate!)

    }

    // Convert local time to UTC (or GMT)
    func toGlobalTime() -> Date {
        let timezone = TimeZone.current
        let seconds = -TimeInterval(timezone.secondsFromGMT(for: self))
        return Date(timeInterval: seconds, since: self)
    }

    // Convert UTC (or GMT) to local time
    func toLocalTime() -> Date {

        // 1) Get the current TimeZone's seconds from GMT. Since I am in Chicago this will be: 60*60*5 (18000)
        let timezoneOffset = TimeZone.current.secondsFromGMT()

        // 2) Get the current date (GMT) in seconds since 1970. Epoch datetime.
        let epochDate = self.timeIntervalSince1970

        // 3) Perform a calculation with timezoneOffset + epochDate to get the total seconds for the
        //    local date since 1970.
        //    This may look a bit strange, but since timezoneOffset is given as -18000.0, adding epochDate and timezoneOffset
        //    calculates correctly.
        let timezoneEpochOffset = (epochDate + Double(timezoneOffset))


        // 4) Finally, create a date using the seconds offset since 1970 for the local date.
        return Date(timeIntervalSince1970: timezoneEpochOffset)
    }

}
extension String {

    func convertToUTC() -> Date {
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
        formatter.timeZone = TimeZone(abbreviation: "UTC")
        let convertedDate = formatter.date(from: self)!
        let f  = formatter.string(from:convertedDate)
        print("UTCCCC1",f)

        formatter.timeZone = NSTimeZone.system
        let dt = formatter.date(from: f)
        formatter.locale = .autoupdatingCurrent
        formatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
        let c = formatter.string(from: dt!)
        print("UTCCCC2",c)


        return convertedDate//formatter.string(from:convertedDate)

    }

    var isValidName: Bool {
        guard self.count > 0 else { return false }
        let invalidCharacters = CharacterSet(charactersIn: "abcdefghijklmnopqrstuvwxyz'’- ").inverted
        let range = self.lowercased().rangeOfCharacter(from: invalidCharacters)

        return range == nil
    }
    var isValidDateOfBirth: Bool {
        let formatter = DateFormatter()
        formatter.dateFormat = "MM/dd/yyyy"

        return formatter.date(from: self) != nil && self.count > 8
    }
    var isValidLastFourSocial: Bool {
        return self.count == 4 && (Int(self) != nil)
    }
    func MMddyyyDate() -> Date {
        let formatter = DateFormatter()
        formatter.dateFormat = "MM/dd/yyyy"
        return formatter.date(from: self) ?? Date()
    }
    // MARK: Date/Time Conversions
    func toISOString(formatter: DateFormatter) -> String? {
        formatter.locale = Locale(identifier: "on_US_POSIX")
        formatter.dateFormat = "MM/dd/yyyy"
        guard let date = formatter.date(from: self) else { return nil }
        formatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss"
        return formatter.string(from: date)
    }
    func toShortDateString() -> String {
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd'T'hh:mm:ss"
        let date = formatter.date(from: self) ?? Date()
        formatter.dateFormat = "MM/dd/yyyy"
        return formatter.string(from: date)
    }
    func toShortDateYearMonthDay() -> String {
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd'T'hh:mm:ss"
        let date = formatter.date(from: self) ?? Date()
        formatter.dateFormat = "yyyy/MM/dd"
        return formatter.string(from: date)
    }
    func toShortDateHarvestTags() -> String {
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS"
        let date = formatter.date(from: self) ?? Date()
        formatter.dateFormat = "MM/dd/yyyy"
        return formatter.string(from: date)


    }
    func toShortDateMMMddyyyy() -> String {
        let formatter = DateFormatter()
        
        formatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
        let date = formatter.date(from: self) ?? Date()
        formatter.dateFormat = "MMM dd, yyyy"
        return formatter.string(from: date)


    }

    func toDateAndTimeDate() -> Date {
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss"
        return formatter.date(from: self) ?? Date()
    }
    func toDateAndTimeString() -> String {
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss"
        if let date = formatter.date(from: self) {
            formatter.dateFormat = "MM/dd/yyyy h:mm:ss a"
            return formatter.string(from: date)
        }

        return ""
    }
    func toDateAndTimeStringWithMoreSpace() -> String {
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss"
        if let date = formatter.date(from: self) {
            formatter.dateFormat = "MM/dd/yyyy  -  h:mm:ss a"
            return formatter.string(from: date)
        }

        return ""
    }
    func toDate(dateFormatter: DateFormatter? = nil) -> Date {
        if let formatter = dateFormatter {
            return formatter.date(from: self) ?? Date()
        } else {
            let formatter = DateFormatter()
            formatter.dateFormat = "MMM dd, yyyy"
            return formatter.date(from: self) ?? Date()
        }
    }
    func toOptionalDate(dateFormatter: DateFormatter) -> Date? {
        return dateFormatter.date(from: self) ?? nil
    }
    func toTimeOnly(dateFormatter: DateFormatter) -> String {
        dateFormatter.dateFormat = "yyyy-mm-dd HH:mm"
        guard let date = dateFormatter.date(from: self) else { return self }
        dateFormatter.dateFormat = "h:mm a"
        return dateFormatter.string(from: date)
    }
    func toISODate() -> Date? {
        let formatter = DateFormatter()
        formatter.locale = Locale(identifier: "on_US_POSIX")
        formatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss"

        return formatter.date(from: self)
    }

    // MARK: String/Number Converstions
    func roundedTo(numberOfDecimalPlaces: Int) -> String {
        guard let double = Double(self) else { return self }
        return String(format:"%.\(numberOfDecimalPlaces)f", double)
    }

    // MARK: Feeding Times Specific Extensions
    func fullMonthOnly() -> String? {
        let formatter = DateFormatter()
        formatter.dateFormat = "E MMM dd yyyy"
        if let date = formatter.date(from: self) {
            formatter.dateFormat = "MMMM"
            return formatter.string(from: date) + "\n"
        } else {
            return nil
        }
    }
    func dayOnly() -> String? {
        let formatter = DateFormatter()
        formatter.dateFormat = "E MMM dd yyyy"
        if let date = formatter.date(from: self) {
            formatter.dateFormat = "dd"
            return formatter.string(from: date)
        } else {
            return nil
        }
    }
    func extratTimes() -> String? {
        // API may return the following string, this means feeding times does not exist
        guard self != "----" else {
            return nil
        }

        var times = self.split(separator: " ")
        times.remove(at: 0)
        times.remove(at: 3)

        return times.joined(separator: " ") + "\n"
    }
}
extension Date {

    static func - (lhs: Date, rhs: Date) -> TimeInterval {
        return lhs.timeIntervalSinceReferenceDate - rhs.timeIntervalSinceReferenceDate
    }

}
extension TimeInterval {
    /// 60
    static var oneMinute: TimeInterval {
        return 60
    }

    /// 3,600
    static var oneHour: TimeInterval {
        return oneMinute * 60
    }

    /// 86,400
    static var oneDay: TimeInterval {
        return oneHour * 24
    }

    /// 604,800
    static var oneWeek: TimeInterval {
        return oneDay * 7
    }

    /// 2,592,000
    static var approximatelyOneMonth: TimeInterval {
        return oneDay * 30
    }

    /// 31,557,600
    static var oneYear: TimeInterval {
        return oneDay * 365.25
    }

    func seconds() -> TimeInterval {
        return self.truncatingRemainder(dividingBy: 60).rounded(.down)
    }

    func minutes() -> TimeInterval {
        return (self / 60).rounded(.down)
    }

    func hours() -> TimeInterval {
        return (minutes() / 60).rounded(.down)
    }

    func days() -> TimeInterval {
        return (hours() / 24).rounded(.down)
    }

    func months() -> TimeInterval {
        return (days() / 30).rounded(.down)
    }

    func years() -> TimeInterval {
        return (months() / 12).rounded(.down)
    }
}
