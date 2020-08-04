import Foundation
import ConfirmitMobileSDK
import UIKit

class SimpleCardController: SurveyLayoutController {
    @IBOutlet var btnClose: UIButton!
    @IBOutlet var scrollView: UIScrollView!
    
    var isInitialized: Bool = false
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        scrollView.delegate = self
        scrollView.bounces = false
        scrollView.showsVerticalScrollIndicator = false
        scrollView.showsHorizontalScrollIndicator = false
        scrollView.isScrollEnabled = false
        
        SimpleCardStyle.styleTextButton(btnClose)
        
        loadFrame(surveyFrameDelegate: self)
    }
    
    func setupPages(pages: [UIView]) {
        for subView in scrollView.subviews {
            subView.removeFromSuperview()
        }
        
        scrollView.frame = CGRect(x: 0, y: 0, width: view.frame.width, height: view.frame.height)
        scrollView.contentSize = CGSize(width: scrollView.frame.width * CGFloat(pages.count), height: scrollView.frame.height)
        scrollView.isPagingEnabled = true
        
        for i in 0 ..< pages.count {
            pages[i].frame = CGRect(x: view.frame.width * CGFloat(i), y: 0, width: view.frame.width, height: view.frame.height)
            scrollView.addSubview(pages[i])
        }
        
        _ = (scrollView.subviews[0] as! SimpleCardItem).onShow()
    }
    
    func scrollTo(index: Int) {
        let positionWidth = scrollView.frame.width * CGFloat(index)
        scrollView.setContentOffset(CGPoint(x: positionWidth, y: 0), animated: true)
    }
    
    @IBAction func onCloseClicked(_ sender: Any) {
        _ = surveyFrame.quit(upload: AppConfigs.uploadImcomplete)
    }
}

extension SimpleCardController: SurveyFrameDelegate {
    func onSurveyPageReady(page: SurveyPage) {
        if isInitialized {
            errorCloseSurvey(message: "Simple Card layout only supported with single page")
            return
        }
        
        var list = [SimpleCardItem]()
        for (index, quesiton) in page.questions.enumerated() {
            var cardItem: SimpleCardItem?
            switch quesiton {
            case let textQuestion as TextQuestion:
                let item: SimpleCardTextItem = Utils.fromNib()
                item.setupQuestion(question: textQuestion)
                cardItem = item
            case let numericQuestion as NumericQuestion:
                let item: SimpleCardNumericItem = Utils.fromNib()
                item.setupQuestion(question: numericQuestion)
                cardItem = item
            case let singleQuestion as SingleQuestion:
                let item: SimpleCardSingleItem = Utils.fromNib()
                item.setupQuestion(question: singleQuestion)
                cardItem = item
            case let multiQuestion as MultiQuestion:
                let item: SimpleCardMultiItem = Utils.fromNib()
                item.setupQuestion(question: multiQuestion)
                cardItem = item
            default:
                break
            }
            
            guard let resultCardItem = cardItem else {
                errorCloseSurvey(message: "Simple Card layout only supported with [Text, Numeric, Single, Multi] questions.")
                return
            }
            
            resultCardItem.setupItem(index: index,
                                     showBack: showBack(page: page, index: index),
                                     showNext: showNext(page: page, index: index),
                                     isFirst: index == 0,
                                     isLast: index == page.questions.count - 1)
            resultCardItem.delegate = self
            list.append(resultCardItem)
        }
        
        setupPages(pages: list)
        
        isInitialized = true
    }
    
    private func showBack(page: SurveyPage, index: Int) -> Bool {
        if index == 0 {
            return page.showBackward
        }
        
        return true
    }
    
    private func showNext(page: SurveyPage, index: Int) -> Bool {
        if index >= page.questions.count - 1 {
            return page.showForward
        }
        
        return true
    }
    
    func onSurveyErrored(page: SurveyPage, values: [String: String?], error: Error) {
        errorCloseSurvey(error: error)
    }
    
    func onSurveyFinished(page: SurveyPage, values: [String: String?]) {
        closeSurvey()
    }
    
    func onSurveyQuit(values: [String: String?]) {
        closeSurvey()
    }
}

extension SimpleCardController: SimpleCardItemDelegate {
    func onBackClicked(index: Int) {
        if index <= 0 {
            _ = surveyFrame.back()
        } else {
            let newIndex = index - 1
            if (scrollView.subviews[newIndex] as! SimpleCardItem).onShow() {
                dismissKeyboard()
            }
            scrollTo(index: newIndex)
        }
    }
    
    func onNextClicked(index: Int) {
        if index >= scrollView.subviews.count - 1 {
            _ = surveyFrame.next()
        } else {
            let newIndex = index + 1
            if (scrollView.subviews[newIndex] as! SimpleCardItem).onShow() {
                dismissKeyboard()
            }
            scrollTo(index: newIndex)
        }
    }
}

extension SimpleCardController: UIScrollViewDelegate {}
