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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.pawlost.work.html.core.NormalConvert;

import java.util.ArrayList;

public class WholeElement {

    private ArrayList<Document> oldChapters;
    private ArrayList<Document> newChapters;

    private ArrayList<Document> compared;

    public WholeElement (Document oldDocument, Document newDocument){
        oldChapters = new ArrayList<>();
        newChapters = new ArrayList<>();
        compared = new ArrayList<>();

        oldChapters = divideToChapters(oldDocument);
        newChapters = divideToChapters(newDocument);
    }

    public int chaptersSize (){
        return getNewChapters().size() <= getNewChapters().size() ?
                getNewChapters().size() : getOldChapters().size();
    }

    public Document createDifference(){
        Document difference = Jsoup.parse("");
        for (Document document : compared){
            difference.append(document.html());
        }
        return difference;
    }

    private ArrayList<Document> divideToChapters(Document document){
        ArrayList<Document> arrayElements = new ArrayList<>();
        Elements elements = document.select(NormalConvert.DOC_WRAPPER_ID);
        for(Element element : elements){
            arrayElements.add(Jsoup.parse(element.html()));
        }
        return arrayElements;
    }

    public void addCompared(Document compared) {
        this.compared.add(compared);
    }

    public void removeOldChapters(int index){
        oldChapters.remove(index);
    }
    public void removeNewChapters(int index){
        newChapters.remove(index);
    }

    public ArrayList<Document> getNewChapters() {
        return newChapters;
    }

    public ArrayList<Document> getOldChapters() {
        return oldChapters;
    }

}
