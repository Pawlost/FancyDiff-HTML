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

import java.util.ArrayList;

public class WholeElement {
    String oldPath;
    String newPath;

    private Document oldDocument;
    private Document newDocument;

    private ArrayList<LesserElement> oldChapters;
    private ArrayList<LesserElement> newChapters;

    private ArrayList<LesserElement> compared;

    public WholeElement (Document oldDocument, Document newDocument){

        this.oldDocument = oldDocument;
        this.newDocument = newDocument;

        oldChapters = new ArrayList<>();
        newChapters = new ArrayList<>();
        compared = new ArrayList<>();
    }

    public int chaptersSize (){
        return getNewChapters().size() <= getNewChapters().size() ?
                getNewChapters().size() : getOldChapters().size();
    }

    public void removeOldChapters(int index){
        oldChapters.remove(index);
    }
    public void removeNewChapters(int index){
        newChapters.remove(index);
    }

    public ArrayList<LesserElement> getNewChapters() {
        return newChapters;
    }

    public ArrayList<LesserElement> getOldChapters() {
        return oldChapters;
    }

    public Document getNewDocument() {
        return newDocument;
    }

    public Document getOldDocument() {
        return oldDocument;
    }

    public String getOldPath(){
        return oldPath;
    }

    public String getNewPath(){
        return newPath;
    }
}
