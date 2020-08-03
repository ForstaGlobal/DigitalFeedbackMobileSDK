import Foundation
import UIKit

class SimpleFbBackgroundView: UIView {
    open override class var layerClass: AnyClass {
        return CAGradientLayer.classForCoder()
    }
    
    private var animations: [SimpleFbFaceIcon: CAAnimation] = [:]
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        let gradientLayer = layer as! CAGradientLayer
        gradientLayer.colors = [SimpleFbColor.backgroundFrom.cgColor, SimpleFbColor.backgroundTo.cgColor]
        
        let from = SimpleFbColor.backgroundFrom
        let to = SimpleFbColor.backgroundTo
        
        createAnimation(layer: gradientLayer, face: .worst, from: from.darker(by: 15)!, to: to.darker(by: 15)!)
        createAnimation(layer: gradientLayer, face: .veryBad, from: from.darker(by: 10)!, to: to.darker(by: 10)!)
        createAnimation(layer: gradientLayer, face: .bad, from: from.darker(by: 5)!, to: to.darker(by: 5)!)
        createAnimation(layer: gradientLayer, face: .good, from: from, to: to)
        createAnimation(layer: gradientLayer, face: .veryGood, from: from.lighter(by: 5)!, to: to.lighter(by: 5)!)
        createAnimation(layer: gradientLayer, face: .best, from: from.lighter(by: 10)!, to: to.lighter(by: 10)!)
    }
    
    private func createAnimation(layer: CAGradientLayer, face: SimpleFbFaceIcon, from: UIColor, to: UIColor) {
        let animation = CABasicAnimation(keyPath: "colors")
        animation.duration = 0.3
        animation.toValue = [from.cgColor, to.cgColor]
        animation.fillMode = .forwards
        animation.isRemovedOnCompletion = false
        animations[face] = animation
    }
    
    func updateColor(face: SimpleFbFaceIcon) {
        let gradientLayer = layer as! CAGradientLayer
        let animation = animations[face]! as! CABasicAnimation
        gradientLayer.add(animation, forKey: face.rawValue)
    }
}
