

import Foundation
import UIKit
typealias statusCode = Int?

class APIManager{

    class func postService(url:String,parameters:[String:String],completionHandler: @escaping (JSON?, Error?, statusCode) -> ()){
        print(url)
        
        let manager = Session.default
        
        manager.session.configuration.timeoutIntervalForRequest = 30
        
        print(url)
         print(parameters)
        let headers:HTTPHeaders=[
            "Authorization": UserDefaultHelper.getAccessToken()
        ]
            print(headers)
        manager.request(url, method: .post, parameters: parameters, encoder: JSONParameterEncoder.default, headers: headers, interceptor: nil).responseJSON { (response) in
            switch response.result{
            case .success(let value):
                
                let json = JSON(value)
                
                print(response.response?.statusCode ?? "")
                completionHandler(json, nil,response.response?.statusCode)
            case .failure(let error):
                completionHandler(nil, error,response.response?.statusCode)
            }
        }
        
    }
    class func postServiceWithBoolParams(url:String,parameters:[String:Bool],completionHandler: @escaping (JSON?, Error?, statusCode) -> ()){
        print(url)
        
        let manager = Session.default
        
        manager.session.configuration.timeoutIntervalForRequest = 30
        
        print(url)
         print(parameters)
        let headers:HTTPHeaders=[
            "Authorization": UserDefaultHelper.getAccessToken()
        ]
            print(headers)
        manager.request(url, method: .post, parameters: parameters, encoder: JSONParameterEncoder.default, headers: headers, interceptor: nil).responseJSON { (response) in
            switch response.result{
            case .success(let value):
                
                let json = JSON(value)
                
                print(response.response?.statusCode ?? "")
                completionHandler(json, nil,response.response?.statusCode)
            case .failure(let error):
                completionHandler(nil, error,response.response?.statusCode)
            }
        }
        
    }
    class func postServiceWithArrayParameter(url:String,parameters:[String:[String]],completionHandler: @escaping (JSON?, Error?, statusCode) -> ()){
        print(url)
        
        let manager = Session.default
        
        manager.session.configuration.timeoutIntervalForRequest = 30
        
        
        print(url)
         print(parameters)
        let headers:HTTPHeaders=[
            "Authorization": UserDefaultHelper.getAccessToken()
        ]
            print(headers)
        manager.request(url, method: .post, parameters: parameters, encoder: JSONParameterEncoder.default, headers: headers, interceptor: nil).responseJSON { (response) in
            switch response.result{
            case .success(let value):
                
                let json = JSON(value)
                
                print(response.response?.statusCode ?? "")
                completionHandler(json, nil,response.response?.statusCode)
            case .failure(let error):
                completionHandler(nil, error,response.response?.statusCode)
            }
        }
        
    }
    class func postService_Without_Header(url:String,parameters:[String:String],completionHandler: @escaping (JSON?, Error?, statusCode) -> ()){
        print(url)
        
        let manager = Session.default
        
        manager.session.configuration.timeoutIntervalForRequest = 30

        
        print(url)
         print(parameters)
        manager.request(url, method: .post, parameters: parameters, encoder: JSONParameterEncoder.default, interceptor: nil).responseJSON { (response) in
            switch response.result{
            case .success(let value):
                
                let json = JSON(value)
                
                print(response.response?.statusCode ?? "")
                completionHandler(json, nil,response.response?.statusCode)
            case .failure(let error):
                completionHandler(nil, error,response.response?.statusCode)
            }
        }
        
    }
    class func postService_Without_HeaderEncondJson(url:String,parameters:[String:[String]],completionHandler: @escaping (JSON?, Error?, statusCode) -> ()){
        print(url)
        
//        let manager = Session.default
       
      //  manager.session.configuration.timeoutIntervalForRequest = 30

//        let deativate = ServerTrustManager(
//            evaluators: ["macronodes.com:8443": DisabledTrustEvaluator()])
//        let manager = Session(serverTrustManager: deativate)
//
//
//
//        let configuration = URLSessionConfiguration.default
//
//        manager.session.configuration.timeoutIntervalForRequest = 3000
//        print(url)
//         print(parameters)
        
       
//        private static var Manager: Alamofire.SessionManager =
//
//                         // Create the server trust policies
//                         let serverTrustPolicies: [String: ServerTrustPolicy] = [
//
//                              "devportal:8443": .disableEvaluation
//                         ]
//
//                         // Create custom manager
//                         let configuration = URLSessionConfiguration.default
//                         configuration.httpAdditionalHeaders = Alamofire.SessionManager.defaultHTTPHeaders
//                         let manager = Alamofire.SessionManager(
//                              configuration: URLSessionConfiguration.default,
//                              serverTrustPolicyManager: ServerTrustPolicyManager(policies: serverTrustPolicies)
//                         )
//
//                         return manager
        let manager = Session.default
        
        manager.session.configuration.timeoutIntervalForRequest = 30

        
        print(url)
         print(parameters)
        
        manager.request(url, method: .post, parameters: parameters, encoder: JSONParameterEncoder.default, interceptor: nil).responseJSON { (response) in
            switch response.result{
            case .success(let value):
                
                let json = JSON(value)
                
                print(response.response?.statusCode ?? "")
                completionHandler(json, nil,response.response?.statusCode)
            case .failure(let error):
                completionHandler(nil, error,response.response?.statusCode)
            }
        }
        
    }
    class func formData(url:String,parameters:[String:Any], completionHandler: @escaping (JSON?, Error?, statusCode) -> ()){
        
        let manager = Session.default
        manager.session.configuration.timeoutIntervalForRequest = 30
        
        let headers:HTTPHeaders=[
            "Authorization": UserDefaultHelper.getAccessToken()
        ]
        print(headers)
         print(url)
         print(parameters)
        manager.upload(multipartFormData: { (multipartFormData) in
            for (key, value) in parameters {
                print("FORMDATA",key,value)
                
                multipartFormData.append((value as AnyObject).data(using: String.Encoding.utf8.rawValue)!, withName: key)
            }
        }, to: url,method:.post,headers: headers).uploadProgress { (progress) in
            
            print(progress)
            
        }.responseJSON { response in
            switch response.result{
            case .success(let value):
                
                let json = JSON(value)
                
                print(response.response?.statusCode ?? "")
                completionHandler(json, nil,response.response?.statusCode)
            case .failure(let error):
                completionHandler(nil, error,response.response?.statusCode)
            }
        }
        
        
        
    }
    
    
    
    
    class func postService_encoding_without_JSON(url:String,parameters:[String:Any], completionHandler: @escaping (JSON?, Error?, statusCode) -> ()){
        
        let manager = Session.default
        manager.session.configuration.timeoutIntervalForRequest = 30
        let headers:HTTPHeaders=[
            "Authorization": UserDefaultHelper.getAccessToken()
        ]
        print(parameters)
            print(url)
        manager.request(url, method: .post, parameters: parameters, headers: headers, interceptor: nil).responseJSON { (response) in
            switch response.result {
            case .success(let value):
                
                let json = JSON(value)
                print(response.response?.statusCode ?? "")
                completionHandler(json, nil,response.response?.statusCode)
                
                
            case .failure(let error):
                _ = response.response?.allHeaderFields
                print(response.response?.statusCode ?? "")
                completionHandler(nil, error,response.response?.statusCode)
                
            }
        }
        
    }
    
    
    class func delete_Service(url:String, completionHandler: @escaping (JSON?, Error?, statusCode) -> ()){
        let headers:HTTPHeaders=[
            "Authorization": UserDefaultHelper.getAccessToken()
        ]
        
        let manager = Session.default
       
        manager.request(url, method: .delete, headers: headers, interceptor: nil).responseJSON { (response) in
            switch response.result {
            case .success(let value):
                
                let json = JSON(value)
                //print(response.response?.statusCode)
                completionHandler(json, nil,response.response?.statusCode)
                
                
            case .failure(let error):
                _ = response.response?.allHeaderFields
                print(response.response?.statusCode ?? "")
                completionHandler(nil, error,response.response?.statusCode)
                
            }
        }
        
        
        
        
        
    }
    class func postService_without_encoding_without_JSON_without_Header(url:String,parameters:[String:Any], completionHandler: @escaping (JSON?, Error?, statusCode) -> ()){
        
        let manager = Session.default
        manager.session.configuration.timeoutIntervalForRequest = 30
        manager.request(url, method: .post, parameters: parameters).responseJSON { (response) in
            switch response.result {
            case .success(let value):
                
                let json = JSON(value)
                print(response.response?.statusCode ?? "")
                completionHandler(json, nil,response.response?.statusCode)
                
                
            case .failure(let error):
                _ = response.response?.allHeaderFields
                print(response.response?.statusCode ?? "")
                completionHandler(nil, error,response.response?.statusCode)
                
            }
        }
        
        
        
        
        
    }
    class func putService_With_Header(url:String,parameters:JSON, completionHandler: @escaping (JSON?, Error?, statusCode) -> ()){

        let manager = Session.default
        manager.session.configuration.timeoutIntervalForRequest = 30
        let headers:HTTPHeaders=[
            "Authorization": UserDefaultHelper.getAccessToken(),
            "X-Installation-Token":UserDefaultHelper.installationID()
        ]
        print(headers)
        print(parameters)
        manager.request(url, method: .put, parameters: parameters,encoder:JSONParameterEncoder.default,headers: headers).responseJSON { (response) in
            print(response.response)
            switch response.result {
                case .success(let value):

                    let json = JSON(value)

                    print(response.response?.statusCode ?? "")
                    completionHandler(json, nil,response.response?.statusCode)


                case .failure(let error):
                    _ = response.response?.allHeaderFields
                    print(response.response?.statusCode ?? "")
                    completionHandler(nil, error,response.response?.statusCode)

            }
        }





    }
    class func postService_With_Header(url:String,parameters:JSON, completionHandler: @escaping (JSON?, Error?, statusCode) -> ()){

        let manager = Session.default
        manager.session.configuration.timeoutIntervalForRequest = 30
        let headers:HTTPHeaders=[
            "Authorization": UserDefaultHelper.getAccessToken(),
            "X-Installation-Token":UserDefaultHelper.installationID()
        ]
        print(parameters)
        print(headers)
        manager.request(url, method: .post, parameters: parameters,encoder:JSONParameterEncoder.default,headers: headers).responseJSON { (response) in
            print(response.response)
            switch response.result {
                case .success(let value):

                    let json = JSON(value)

                    print(response.response?.statusCode ?? "")
                    completionHandler(json, nil,response.response?.statusCode)


                case .failure(let error):
                    _ = response.response?.allHeaderFields
                    print(response.response?.statusCode ?? "")
                    completionHandler(nil, error,response.response?.statusCode)

            }
        }





    }
    //
    class func postService_without_encoding_JSON_without_Header21(url:String,parameters:[String:String],completionHandler: @escaping (JSON?, Error?, statusCode) -> ()){
        print(url)
        
        let manager = Session.default
        
        manager.session.configuration.timeoutIntervalForRequest = 30
        
        manager.request(url, method: .post, parameters: parameters, encoder:URLEncodedFormParameterEncoder.default, interceptor: nil).responseJSON { (response) in
            switch response.result{
            case .success(let value):
                
                let json = JSON(value)
                
                print(response.response?.statusCode ?? "")
                completionHandler(json, nil,response.response?.statusCode)
            case .failure(let error):
                completionHandler(nil, error,response.response?.statusCode)
            }
        }
        
    }
    
    class func postService_without_encoding_JSON_without_Parameter(url:String,completionHandler: @escaping (JSON?, Error?, statusCode) -> ()){
        print(url)
        let headers:HTTPHeaders=[
            "Authorization": UserDefaultHelper.getAccessToken()
        ]
        let manager = Session.default
        
        manager.session.configuration.timeoutIntervalForRequest = 30
        
        manager.request(url, method: .post,headers: headers, interceptor: nil).responseJSON { (response) in
            switch response.result{
            case .success(let value):
                
                let json = JSON(value)
                
                print(response.response?.statusCode ?? "")
                completionHandler(json, nil,response.response?.statusCode)
            case .failure(let error):
                completionHandler(nil, error,response.response?.statusCode)
            }
        }
        
    }
    
    
    
    //
    class  func getService(url:String,completionHandler: @escaping (JSON?, Error?,statusCode) -> ()){
      
        let manager = Session.default
        let headers:HTTPHeaders=[
            "Authorization":UserDefaultHelper.getAccessToken(),
            "X-Installation-Token":UserDefaultHelper.installationID()
        ]
        manager.session.configuration.timeoutIntervalForRequest = 30
        print(headers)
        print("URL---",url)
          
        manager.request(url, method:.get,headers: headers, interceptor: nil).responseJSON { (response) in
            switch response.result{
            case .success(let value):
                
                let json = JSON(value)
                //print(json)
                print(response.response?.statusCode ?? "")
                completionHandler(json, nil,response.response?.statusCode)
            case .failure(let error):
                completionHandler(nil, error,response.response?.statusCode)
            }
        }
        
        
    }
    class  func getServiceOneSignal(url:String,completionHandler: @escaping (JSON?, Error?,statusCode) -> ()){

        let manager = Session.default
        let headers:HTTPHeaders=[
            "Authorization":""
        ]
        manager.session.configuration.timeoutIntervalForRequest = 30
        print(headers)
        print("URL---",url)

        manager.request(url, method:.get,headers: headers, interceptor: nil).responseJSON { (response) in
            switch response.result{
                case .success(let value):

                    let json = JSON(value)

                    print(response.response?.statusCode ?? "")
                    completionHandler(json, nil,response.response?.statusCode)
                case .failure(let error):
                    completionHandler(nil, error,response.response?.statusCode)
            }
        }


    }
    class  func getServiceWithParam(url:String,params: [String:String],completionHandler: @escaping (JSON?, Error?,statusCode) -> ()){
      
        let manager = Session.default
        let headers:HTTPHeaders=[
            "Authorization":UserDefaultHelper.getAccessToken(),
            "X-Installation-Token":UserDefaultHelper.installationID()
        ]
        manager.session.configuration.timeoutIntervalForRequest = 30
        print(headers)
        print("URL---",url)
          
        manager.request(url, method:.get,parameters: params,headers: headers,interceptor: nil).responseJSON { (response) in
            switch response.result{
            case .success(let value):
                
                let json = JSON(value)
                //print(json)
                print(response.response?.statusCode ?? "")
                completionHandler(json, nil,response.response?.statusCode)
            case .failure(let error):
                completionHandler(nil, error,response.response?.statusCode)
            }
        }
        
        
    }
    class  func getService_without_Header(url:String,completionHandler: @escaping (JSON?, Error?,statusCode) -> ()){
        
        
        
        let manager = Session.default
        
        manager.session.configuration.timeoutIntervalForRequest = 30
        
        manager.request(url, method:.get, interceptor: nil).responseJSON { (response) in
            switch response.result{
            case .success(let value):
                
                let json = JSON(value)
                
                print(response.response?.statusCode ?? "")
                completionHandler(json, nil,response.response?.statusCode)
            case .failure(let error):
                completionHandler(nil, error,response.response?.statusCode)
            }
        }
        
    }
    class  func deleteService(url:String,completionHandler: @escaping (JSON?, Error?,statusCode) -> ()){
        
        let headers:HTTPHeaders=[
            "Authorization": UserDefaultHelper.getAccessToken(),
            "X-Installation-Token":UserDefaultHelper.installationID()
        ]

        let manager = Session.default
        manager.session.configuration.timeoutIntervalForRequest = 30

        print(url)

        manager.request(url, method:.delete,headers: headers, interceptor: nil).responseJSON { (response) in
            switch response.result{
            case .success(let value):
                
                let json = JSON(value)
                
                print(response.response?.statusCode ?? "")
                completionHandler(json, nil,response.response?.statusCode)
            case .failure(let error):
                completionHandler(nil, error,response.response?.statusCode)
            }
        }
        
    }
    class func MultipartService(url:String,parameters:[String:Any],image_is_Selected:Bool,images:[String:UIImage]?=nil,Uploading_Status: @escaping (Int) -> (),completionHandler: @escaping (JSON?, Error?, statusCode) -> ()){
      let headers:HTTPHeaders=[
          "Authorization": UserDefaultHelper.getAccessToken(),
          "X-Installation-Token":UserDefaultHelper.installationID()
      ]
        let manager = Session.default
        
        manager.session.configuration.timeoutIntervalForRequest = 30
    
        manager.upload(multipartFormData: { (multipartFormData) in
            print(image_is_Selected)
                       
            
            
                        if(image_is_Selected){
            
                            for (key, value) in images! {
                                print(key)
                                print(value)
            
                                let imageData = value.jpegData(compressionQuality: 0.5)
                                multipartFormData.append(imageData!, withName:key , fileName: "\(Date().timeIntervalSince1970).jpeg", mimeType: "image/jpg")
            
                            }
            
                        }
                        for (key, value) in parameters {
                            
                          multipartFormData.append("\(value)".data(using:.utf8)!, withName: key)
                        }
            
            }, to: url,method:.post,headers:headers)
            
            
            .uploadProgress { progress in
                print("Upload Progress: \(progress.fractionCompleted)")
                 Uploading_Status(Int(progress.fractionCompleted*100))
            }
            .responseJSON { response in
               switch response.result{
               case .success(let value):
                   
                   let json = JSON(value)
                   
                   print(response.response?.statusCode ?? "")
                   completionHandler(json, nil,response.response?.statusCode)
               case .failure(let error):
                   completionHandler(nil, error,response.response?.statusCode)
               }
            }
        
        
    }
    class func MultipartVideoImageService(url:String,parameters:[String:Any],image_is_Selected:Bool,images:[String:UIImage]?=nil,video_is_Selected:Bool,resourcesVideo:[String:URL],Uploading_Status: @escaping (Int) -> (),completionHandler: @escaping (JSON?, Error?, statusCode) -> ()){
      let headers:HTTPHeaders=[
          "Authorization": UserDefaultHelper.getAccessToken(),
         
      ]
        let manager = Session.default
        
        manager.session.configuration.timeoutIntervalForRequest = 30
    
        manager.upload(multipartFormData: { (multipartFormData) in
            print(image_is_Selected)
                       
          
            
                        if(image_is_Selected){
            
                            for (key, value) in images! {
                                print(key)
                                print(value)
            
                                let imageData = value.jpegData(compressionQuality: 0.5)
                                multipartFormData.append(imageData!, withName:key , fileName: "\(Date().timeIntervalSince1970).jpeg", mimeType: "image/jpg")
            
                            }
            
                        }
            if(video_is_Selected){
                for (key, value) in resourcesVideo {
                    multipartFormData.append(value, withName: key)
                }
                                  
            }
                        for (key, value) in parameters {
                            
                          multipartFormData.append("\(value)".data(using:.utf8)!, withName: key)
                        }
            
            }, to: url,method:.post,headers:headers)
            
            
            .uploadProgress { progress in
                print("Upload Progress: \(progress.fractionCompleted)")
                 Uploading_Status(Int(progress.fractionCompleted*100))
            }
            .responseJSON { response in
               switch response.result{
               case .success(let value):
                   
                   let json = JSON(value)
                   print(json)
                   print(response.response?.statusCode ?? "")
                   completionHandler(json, nil,response.response?.statusCode)
               case .failure(let error):
                   completionHandler(nil, error,response.response?.statusCode)
               }
            }
        
        
    }
    
    class func MultipleImageUpload(url:String,images:[UIImage],KEY_IMG:String,Uploading_Status: @escaping (Int) -> (),completionHandler: @escaping (JSON?, Error?, statusCode) -> ()){
        let headers:HTTPHeaders=[
            "Authorization": UserDefaultHelper.getAccessToken()
        ]
        let manager = Session.default

        manager.session.configuration.timeoutIntervalForRequest = 30

        manager.upload(multipartFormData: { (multipartFormData) in




                            for IMG in images {
                              

                                let imageData = IMG.jpegData(compressionQuality: 0.5)
                                multipartFormData.append(imageData!, withName:KEY_IMG , fileName: "\(Date().timeIntervalSince1970).jpeg", mimeType: "image/jpg")

                            }




            }, to: url,method:.post,headers:headers)


            .uploadProgress { progress in
                print("Upload Progress: \(progress.fractionCompleted)")
                 Uploading_Status(Int(progress.fractionCompleted*100))
            }
            .responseJSON { response in
               switch response.result{
               case .success(let value):

                   let json = JSON(value)

                   print(response.response?.statusCode ?? "")
                   completionHandler(json, nil,response.response?.statusCode)
               case .failure(let error):
                   completionHandler(nil, error,response.response?.statusCode)
               }
            }


    }
    //CancelRequest
    class func CancelAllRequest(){
        let manager = Session.default
        
        manager.session.configuration.timeoutIntervalForRequest = 30
        
        manager.session.getTasksWithCompletionHandler { (dataTasks, uploadTasks, downloadTasks) in
            dataTasks.forEach { $0.cancel() }
            uploadTasks.forEach { $0.cancel() }
            downloadTasks.forEach { $0.cancel() }
        }
    }
}
