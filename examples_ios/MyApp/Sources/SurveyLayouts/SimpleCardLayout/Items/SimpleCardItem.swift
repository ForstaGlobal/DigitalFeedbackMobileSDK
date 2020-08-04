import Foundation
import ConfirmitMobileSDK
import UIKit

protocol SimpleCardItemDelegate: class {
    func onBackClicked(index: Int)
    func onNextClicked(index: Int)
}

class SimpleCardItem: UIView {
    @IBOutlet var cardView: UIView!
    @IBOutlet var rootContainer: UIView!
    @IBOutlet var headerContainer: UIView!
    @IBOutlet var lblHeader: UILabel!
    @IBOutlet var btnBack: UIButton!
    @IBOutlet var btnNext: UIButton!
    @IBOutlet var lblValidation: UILabel!
    @IBOutlet var validationContainer: UIView!
    
    var index: Int = -1
    var isFirst: Bool = false
    var isLast: Bool = false
    
    weak var delegate: SimpleCardItemDelegate?
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        SimpleCardStyle.styleCard(cardView, rootContainer)
        headerContainer.backgroundColor = SimpleCardColor.button
        SimpleCardStyle.styleLabelTitle(lblHeader)
        SimpleCardStyle.styleTextButton(btnBack)
        SimpleCardStyle.styleTextButton(btnNext)
        SimpleCardStyle.styleValidation(lblValidation, validationContainer)
    }
    
    func setupItem(index: Int, showBack: Bool, showNext: Bool, isFirst: Bool, isLast: Bool) {
        self.index = index
        btnBack.isHidden = !showBack
        btnNext.isHidden = !showNext
        self.isFirst = isFirst
        self.isLast = isLast
    }
    
    func handleValidation(errors: [ValidationQuestionError]) {
        if errors.count > 0 {
            lblValidation.text = "Please verify submitted answer"
            validationContainer.isHidden = false
            rootContainer.layer.borderColor = SimpleCardColor.error.cgColor
            shake()
        } else {
            lblValidation.text = ""
            validationContainer.isHidden = true
            rootContainer.layer.borderColor = UIColor.clear.cgColor
        }
    }
    
    private func shake() {
        guard #available(iOS 10.0, *) else {
            return
        }
        
        let card: UIView = rootContainer
        let duration: TimeInterval = 0.3
        let translation: CGFloat = 10
        let propertyAnimator = UIViewPropertyAnimator(duration: duration, dampingRatio: 0.2) {
            card.transform = CGAffineTransform(translationX: translation, y: 0)
        }
        
        propertyAnimator.addAnimations({
            card.transform = CGAffineTransform(translationX: 0, y: 0)
        }, delayFactor: 0.2)
        
        propertyAnimator.startAnimation()
    }
    
    func validate(question: DefaultQuestion) -> Bool {
        do {
            let errors = try question.validate()
            if errors.count > 0 {
                handleValidation(errors: errors)
                return false
            }
        } catch {
            return false
        }
        handleValidation(errors: [])
        return true
    }
    
    func onNext() -> Bool { return true }
    
    func onBack() -> Bool { return true }
    
    func onShow() -> Bool { return true }
    
    @IBAction func onBackClicked(_ sender: Any) {
        if onBack() {
            delegate?.onBackClicked(index: index)
        }
    }
    
    @IBAction func onNextClicked(_ sender: Any) {
        if onNext() {
            delegate?.onNextClicked(index: index)
        }
    }
}
