//
//  Manager.swift


import UIKit

final class Manager: NSObject, UIViewControllerTransitioningDelegate {
    private let loaf: FraudsterToast
    private let size: CGSize
    var animator: Animator
    
    init(loaf: FraudsterToast, size: CGSize) {
        self.loaf = loaf
        self.size = size
        self.animator = Animator(duration: 0.4, loaf: loaf, size: size)
    }
    
    func presentationController(forPresented presented: UIViewController, presenting: UIViewController?, source: UIViewController) -> UIPresentationController? {
        return Controller(
            presentedViewController: presented,
            presenting: presenting,
            loaf: loaf,
            size: size
        )
    }
    
    func animationController(forPresented presented: UIViewController, presenting: UIViewController, source: UIViewController) -> UIViewControllerAnimatedTransitioning? {
        animator.presenting = true
        return animator
    }
    
    func animationController(forDismissed dismissed: UIViewController) -> UIViewControllerAnimatedTransitioning? {
        animator.presenting = false
        return animator
    }
}
