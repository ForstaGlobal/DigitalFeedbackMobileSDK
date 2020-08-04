import Foundation
import ConfirmitMobileSDK
import UIKit

protocol SimpleFbRatingViewDelegate: class {
    func onNextClicked(question: SingleQuestion) -> [ValidationQuestionError]
    func onFaceChanged(face: SimpleFbFaceIcon)
}

enum SimpleFbFaceIcon: String {
    case worst = "outline_sentiment_very_dissatisfied_black_48pt"
    case veryBad = "outline_mood_bad_black_48pt"
    case bad = "outline_sentiment_dissatisfied_black_48pt"
    case good = "outline_sentiment_satisfied_black_48pt"
    case veryGood = "outline_mood_black_48pt"
    case best = "outline_sentiment_very_satisfied_black_48pt"
}

enum SimpleFbStarIcon: String {
    case on = "round_star_black_48pt"
    case off = "round_fiber_manual_record_black_18pt"
}

class SimpleFbRatingView: UIView {
    @IBOutlet var btnNext: UIButton!
    @IBOutlet var lblText: UILabel!
    @IBOutlet var lblInst: UILabel!
    @IBOutlet var lblValidation: UILabel!
    @IBOutlet var stackView: UIStackView!
    @IBOutlet var imgIcon: UIImageView!
    
    weak var surveyFrame: SurveyFrame!
    weak var singleQuestion: SingleQuestion!
    weak var delegate: SimpleFbRatingViewDelegate?
    
    private var initialSelected: Int = -1
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        SimpleFbStyle.styleLabelText(lblInst)
        SimpleFbStyle.styleLabelText(lblValidation)
        SimpleFbStyle.styleLabelTitle(lblText)
        SimpleFbStyle.styleOutlinedButton(btnNext)
        
        lblValidation.text = ""
    }
    
    func setup(surveyFrame: SurveyFrame, singleQuestion: SingleQuestion) {
        self.surveyFrame = surveyFrame
        self.singleQuestion = singleQuestion
        lblText.text = singleQuestion.text.get()
        lblInst.text = singleQuestion.instruction.get()
        
        if let selected = singleQuestion.selected() {
            initialSelected = singleQuestion.answers.firstIndex(where: { $0.code == selected.code }) ?? -1
        }
        
        imgIcon.tintColor = UIColor.black
        imgIcon.image = UIImage(named: SimpleFbFaceIcon.good.rawValue)
        
        createStars(totalCount: singleQuestion.answers.count)
    }
    
    private func updateFace(index: Int, total: Int) {
        let ratio = Float(index) / Float(total)
        var icon: SimpleFbFaceIcon
        switch total {
        case 0, 1:
            icon = .good
        case 2:
            icon = index == 0 ? .bad : .good
        case 3, 4, 5:
            if ratio < 0.25 {
                icon = .veryBad
            } else if ratio < 0.50 {
                icon = .bad
            } else if ratio < 0.75 {
                icon = .good
            } else {
                icon = .veryGood
            }
        default:
            if ratio < 0.166 {
                icon = .worst
            } else if ratio < 0.333 {
                icon = .veryBad
            } else if ratio < 0.5 {
                icon = .bad
            } else if ratio < 0.666 {
                icon = .good
            } else if ratio < 0.833 {
                icon = .veryGood
            } else {
                icon = .best
            }
        }
        
        UIView.transition(with: imgIcon,
                          duration: 0.2,
                          options: .transitionFlipFromRight,
                          animations: { self.imgIcon.image = UIImage(named: icon.rawValue) },
                          completion: nil)
        delegate?.onFaceChanged(face: icon)
    }
    
    private func updateStarts(selected: Int) {
        for (index, view) in stackView.arrangedSubviews.enumerated() {
            var icon: SimpleFbStarIcon = .off
            if index <= selected {
                icon = .on
            }
            if let imageView = view as? UIImageView {
                UIView.transition(with: imageView,
                                  duration: 0.2,
                                  options: .transitionCrossDissolve,
                                  animations: { imageView.image = UIImage(named: icon.rawValue) },
                                  completion: nil)
            }
        }
    }
    
    private func createStars(totalCount: Int) {
        for index in 0..<totalCount {
            let frame = CGRect(x: 0, y: 0, width: 48, height: 48)
            let image = UIImageView(image: UIImage(named: SimpleFbStarIcon.off.rawValue))
            image.frame = frame
            image.contentMode = .center
            image.tintColor = SimpleFbColor.button
            
            let tapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(onStarClicked(tapGestureRecognizer:)))
            image.isUserInteractionEnabled = true
            image.addGestureRecognizer(tapGestureRecognizer)
            image.tag = index
            
            stackView.addArrangedSubview(image)
        }
    }
    
    @objc func onStarClicked(tapGestureRecognizer: UITapGestureRecognizer) {
        let tappedImage = tapGestureRecognizer.view as! UIImageView
        let clickedIndex = tappedImage.tag
        updateStarts(selected: clickedIndex)
        singleQuestion.select(answer: singleQuestion.answers[clickedIndex])
        updateFace(index: clickedIndex, total: singleQuestion.answers.count)
    }
    
    @IBAction func onNextClicked(_ sender: Any) {
        if let errors = delegate?.onNextClicked(question: singleQuestion), errors.count > 0 {
            lblValidation.text = "Wait! we haven't heard you yet!"
        }
    }
}
