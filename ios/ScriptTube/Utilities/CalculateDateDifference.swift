//
//  CalculateDateDifference.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 13/12/22.
//

import Foundation
extension Date {
    //MARK: - Calculate Difference between dates
    func offsetFrom(date: Date) -> String {

        let dayHourMinuteSecond: Set<Calendar.Component> = [.day, .hour, .minute, .second]
        let difference = NSCalendar.current.dateComponents(dayHourMinuteSecond, from: date, to: self)

        let seconds = "\(difference.second ?? 0) seocnds"
        let minutes = "\(difference.minute ?? 0) mins"
        
        let hours = "\(difference.hour ?? 0) hrs"
        
        let days = "\(difference.day ?? 0) days"
       
        if let day = difference.day, day          > 0 { return days }
        if let hour = difference.hour, hour       > 0 { return hours }
        if let minute = difference.minute, minute > 0 { return minutes }
        if let second = difference.second, second > 0 { return seconds }
        return ""
    }
    func shortOffsetFrom(date: Date) -> String {

        let dayHourMinuteSecond: Set<Calendar.Component> = [.day, .hour, .minute, .second]
        let difference = NSCalendar.current.dateComponents(dayHourMinuteSecond, from: date, to: self)

        let seconds = "\(difference.second ?? 0) s"
        let minutes = "\(difference.minute ?? 0) m"
        
        let hours = "\(difference.hour ?? 0) h"
        
        let days = "\(difference.day ?? 0) d"
        
        if let day = difference.day, day          > 0 { return days }
        if let hour = difference.hour, hour       > 0 { return hours }
        if let minute = difference.minute, minute > 0 { return minutes }
        if let second = difference.second, second > 0 { return seconds }
        return ""
    }

}
