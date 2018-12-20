/*  This file is part of FancyDiff-HTML.

    FancyDiff-HTML is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FancyDiff-HTML is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with FancyDiff-HTML.  If not, see <https://www.gnu.org/licenses/>.
    */
package org.pawlost.work.html.elements;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class HighterElement extends LesserElement {

    public HighterElement(Document diff) {
        super(diff);
    }

    public void createMultiDifference(Element newElement, int childIndex) {
        Element help = mainElement.clone();
        mainElement = mainElement.parent();
        mainElement.child(childIndex).remove();
        String text = "<" + help.tagName() + ">" + help.text() + "</" + help.tagName() + ">";
        appendChange(text);
        if (!newElement.ownText().isEmpty()) {
            mainElement.child(mainElement.children().size() - 1).append("<font class='FancyDiff' color='green'><" + newElement.tagName() + ">"
                    + newElement.text() + "</" + newElement.tagName() + "></font>");
        }
    }

    public void createMultiDifference(int childIndex) {
        Element help = mainElement.clone();
        mainElement = mainElement.parent();
        mainElement.child(childIndex).remove();
        String text = "<" + help.tagName() + ">" + help.toString() + "</" + help.tagName() + ">";
        appendChange(text);
    }
}