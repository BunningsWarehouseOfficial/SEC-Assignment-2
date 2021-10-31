from texteditor.api import TextUpdateHandler
from texteditor import Control

class Callback(TextUpdateHandler):
    def textChanged(self, oldText, newText):
        caret = api.getCaretPosition()
        print "python check:", newText[(caret-2):caret+1]
        if (newText[(caret-2):caret+1] == ":-)"):
            smileyStr = u"\U0001f60a"
            api.updateText(newText[0:(caret-2)] + smileyStr + newText[caret+1:])
            api.updateCaretPosition(caret)

api.registerTextUpdateHandler(Callback())